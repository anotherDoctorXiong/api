package club.doctorxiong.api.component;



import club.doctorxiong.api.common.dto.ConvertBondDTO;
import club.doctorxiong.api.common.dto.IndustryDetailDTO;
import club.doctorxiong.api.common.dto.KLineDTO;
import club.doctorxiong.api.common.dto.StockDTO;
import club.doctorxiong.api.common.page.PageData;
import club.doctorxiong.api.common.request.KLineRequest;
import club.doctorxiong.api.common.request.StockRankRequest;
import club.doctorxiong.api.uitls.BeanUtil;
import club.doctorxiong.api.uitls.LambdaUtil;
import club.doctorxiong.api.uitls.UrlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;
import club.doctorxiong.api.uitls.StringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;




/**
 * @Author: 熊鑫
 * @Date: 2019/6/24 14
 * @Description: 获取股票相关数据并按照当前时间进行缓存, 查询失败的代码存入数据库进行人工核对
 */
@Component
@Slf4j
public class StockComponent {

    @Resource
    private OkHttpComponent httpSupport;

    @Resource
    private ExpireComponent expireComponent;


    /**
     * 股票实时数据缓存
     */
    public LoadingCache<String, StockDTO> stockCache = Caffeine.newBuilder().expireAfter(new Expiry<String, StockDTO>() {
        @Override
        public long expireAfterCreate(String key, StockDTO stockDTO, long currentTime) {
            currentTime = System.currentTimeMillis() / 1000;
            if (stockDTO.getRequestFail() == 1) {
                log.error("stockCache 请求失败,五分钟后过期 code {}", key);
                return TimeUnit.MINUTES.toNanos(5);
            }

            if (stockDTO.getResolveFail() == 1) {
                log.error("stockCache 解析失败,当天过期 code {}", key);
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }

            if (LocalDate.now().getDayOfWeek().getValue() > 5) {
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            if (currentTime < expireComponent.getTimestampOfOpenAM()) {
                log.info("stockCache 缓存到上午开盘 code {}", key);
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfOpenAM() - currentTime);
            }
            if (currentTime < expireComponent.getTimestampOfClosePM()) {
                log.info("stockCache 缓存一分钟 code {}", key);
                return TimeUnit.MINUTES.toNanos(1);
            }
            if (currentTime - expireComponent.getTimestampOfClosePM() > 60 && stockDTO.getDate().toLocalDate().equals(LocalDate.now())) {
                log.info("stockCache 缓存至当日结束 code {}", key);
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            log.info("stockCache 未命中任何策略 缓存五分钟 code {}", key);
            return TimeUnit.MINUTES.toNanos(5); // 设置缓存过期时间为5分钟
        }

        @Override
        public long expireAfterUpdate(String key, StockDTO value, long currentTime, long currentDuration) {
            return currentDuration; // 返回当前剩余时间，表示不更新过期时间
        }

        @Override
        public long expireAfterRead(String key, StockDTO value, long currentTime, long currentDuration) {
            return currentDuration; // 返回剩余时间，不更新过期时间
        }
    }).build(key -> getStockData(key));


    public KLineDTO getKLineDTO(KLineRequest request){
        // 这里深拷贝一下
        KLineDTO kLineDTO = SerializationUtils.clone(KLineCache.get(request));
        try {
            if(kLineDTO.getBytesData() != null){
                kLineDTO.setArrData(BeanUtil.decompressObject(kLineDTO.getBytesData()));
            }
        } catch (Exception e) {
            log.error("getKLineDTO gzip io异常{}", e);
        }
        return kLineDTO;
    }

    public CacheStats getKLineStat(){
        // 这里深拷贝一下
        return KLineCache.stats();
    }


    /**
     * 股票日k缓存 这里用字节数组压缩下
     */
    private LoadingCache<KLineRequest, KLineDTO> KLineCache = Caffeine.newBuilder().expireAfter(new Expiry<KLineRequest, KLineDTO>() {

        @Override
        public long expireAfterCreate(KLineRequest key, KLineDTO value, long currentTime) {
            currentTime = System.currentTimeMillis() / 1000;
            if (value.getRequestFail() == 1) {
                log.error("KLineDTO 请求失败,五分钟后过期 code {}", key);
                return TimeUnit.MINUTES.toNanos(5);
            }
            if (value.getResolveFail() == 1) {
                log.error("KLineDTO 解析失败,当天过期 code {}", key);
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            if (LocalDate.now().getDayOfWeek().getValue() > 5) {
                log.error("KLineDTO 周末,当天过期 code {}", key);
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            if(value.getLastData() != null){
                if(LocalDate.parse(value.getLastData()[0]).compareTo(LocalDate.now()) == 0){
                    log.info("KLineDTO 缓存至当日结束 code {}", key);
                    return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
                }
            }
            log.info("KLineDTO 未命中任何策略 缓存二十分钟 code {}", key);
            return TimeUnit.MINUTES.toNanos(30); // 设置缓存过期时间为5分钟
        }

        @Override
        public long expireAfterUpdate(KLineRequest key, KLineDTO value, long currentTime, long currentDuration) {
            return currentDuration;
        }

        @Override
        public long expireAfterRead(KLineRequest key, KLineDTO value, long currentTime, long currentDuration) {
            return currentDuration;
        }
    }).recordStats().softValues().build((key)-> getKLineData(key));

    private StockDTO getStockData(String stockCode) {
        StockDTO stockDTO = new StockDTO();
        String stockUrl = UrlUtil.getStockDataUrl(stockCode);
        try {
            String res = httpSupport.get(stockUrl);
            JSONObject object = JSON.parseObject(StringUtil.getValue(res));
            JSONObject data = object.getJSONObject("data").getJSONObject(stockCode);
            //最新基础数据
            String[] stockData = data.getJSONObject("qt").getObject(stockCode, String[].class);
            stockDTO.setStockData(stockData);
            //分时数据
            JSONObject jsonMinData = data.getJSONObject("data");
            String[] minArr = jsonMinData.getObject("data", String[].class);
            List<String[]> minList = new ArrayList<>();
            for (String min : minArr) {
                String[] curMinArr = min.split(" ");
                // 科创板的成交量是股
                if (stockDTO.getType().contains("KCB")) {
                    curMinArr[2] = String.valueOf(Integer.valueOf(curMinArr[2]) / 100 + (Integer.valueOf(curMinArr[2]) % 100 > 50 ? 1 : 0));
                }
                minList.add(curMinArr);
            }
            stockDTO.setMinData(minList);
        } catch (IOException e) {
            stockDTO.setRequestFail(1);
            log.error("StockDTO http connect fail! connect url-{},error {}", stockUrl, e);
        } catch (Exception e) {
            stockDTO.setResolveFail(1);
            log.error("StockDTO data resolve fail! connect url-{},error {}", stockUrl, e);
        }
        return stockDTO;
    }

    /**
     * @param request
     * @name: getKLineData
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.stock.Stock
     * @date: 2020/9/6 16:00
     * @description: 按照复权不同分为三类
     */
    private KLineDTO getKLineData(KLineRequest request) {
        String stockUrl;
        String key;
        switch (request.getType()) {
            case 1:
                stockUrl = UrlUtil.getStockKLineQFQUrl(request.getStockCode());
                key = "qfqday";
                break;
            case 2:
                stockUrl = UrlUtil.getStockKLineHFQUrl(request.getStockCode());
                key = "hfqday";
                break;
            default:
                stockUrl = UrlUtil.getStockKLineUrl(request.getStockCode());
                key = "day";
        }
        KLineDTO kLineDTO = new KLineDTO();
        try {
            String res = httpSupport.get(stockUrl);
            JSONObject jsonObject = JSON.parseObject(res);
            JSONObject data = jsonObject.getJSONObject("data").getJSONObject(request.getStockCode());
            //最新基础数据
            String[] stockData = data.getJSONObject("qt").getObject(request.getStockCode(), String[].class);
            if ("ZS".equals(stockData[61])) {
                key = "day";
            }
            //K线数据
            String klineData = data.containsKey(key) ? data.getString(key) : data.getString("day");
            String[][] arr = JSON.parseObject(klineData, String[][].class);
            if(arr.length > 0){
                kLineDTO.setLastData(arr[arr.length-1]);
            }
            kLineDTO.setBytesData(BeanUtil.compressObject(arr));
        } catch (IOException e) {
            kLineDTO.setRequestFail(1);
            log.error("KLineData http connect fail! connect url-{},error message{}", stockUrl, e);
        } catch (Exception e) {
            kLineDTO.setResolveFail(1);
            log.error("KLineData data resolve fail! connect url-{},error message{}", stockUrl, e);
        }
        return kLineDTO;
    }


    /**
     * @name: getStockDetail
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.stock.StockDetail
     * @date: 2019/6/24 21:32
     * @description: 获取股票详情和日K数据
     */

    public LoadingCache<String, List<IndustryDetailDTO>> stockIndustryCache = Caffeine.newBuilder().expireAfterWrite(20,TimeUnit.MINUTES).build(key -> getStockIndustry());

    private List<IndustryDetailDTO> getStockIndustry() {
        List<IndustryDetailDTO> res = new ArrayList<>();
        try {
            String industryStr = httpSupport.get(UrlUtil.getSWUrl());
            JSONArray jsonArray = JSONArray.parseArray(industryStr);
            JSONArray jsonArray1 = jsonArray.getJSONArray(1);
            JSONArray jsonArray2 = jsonArray1.getJSONArray(0);
            JSONArray jsonArray3 = jsonArray2.getJSONArray(1);
            JSONArray jsonArray4 = jsonArray3.getJSONArray(1);
            JSONArray jsonArray5 = jsonArray4.getJSONArray(1);
            res = jsonArray5.stream().map(arr -> {
                JSONArray jsonArray6 = (JSONArray) arr;
                return new IndustryDetailDTO(jsonArray6.getString(2), jsonArray6.getString(0));
            }).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("IndustryDetailDTO http connect fail! connect url-{},error message{}", UrlUtil.getSWUrl(), e);
        }catch (Exception e) {
            log.error("IndustryDetailDTO data resolve fail! connect url-{},error message{}", UrlUtil.getSWUrl(), e);
        }
        return res;
    }

    public LoadingCache<StockRankRequest,PageData<StockDTO>> stackRankCache = Caffeine.newBuilder().expireAfter(new Expiry<StockRankRequest, PageData<StockDTO>>() {
        @Override
        public long expireAfterCreate(StockRankRequest stockRankRequest, PageData<StockDTO> stockDTOPageData, long currentTime) {
            currentTime = System.currentTimeMillis() / 1000;
            if (stockDTOPageData.getRequestFail() == 1) {
                log.error("stackRank 缓存失败,五分钟后过期");
                return TimeUnit.MINUTES.toNanos(5);
            }
            if (LocalDate.now().getDayOfWeek().getValue() > 5) {
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            if (currentTime < expireComponent.getTimestampOfOpenAM()) {
                log.info("stackRank 缓存到上午开盘");
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfOpenAM() - currentTime);
            }
            if (currentTime - expireComponent.getTimestampOfClosePM() > 60 ) {
                log.info("stackRank 缓存至当日结束 code{%s}");
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            log.info("stackRank 未命中任何策略 缓存五分钟");
            return TimeUnit.MINUTES.toNanos(5); // 设置缓存过期时间为5分钟
        }

        @Override
        public long expireAfterUpdate(StockRankRequest stockRankRequest, PageData<StockDTO> stockDTOPageData, long l,  long l1) {
            return l1;
        }

        @Override
        public long expireAfterRead(StockRankRequest stockRankRequest, PageData<StockDTO> stockDTOPageData, long l,  long l1) {
            return l1;
        }
    }).build(key -> getStockRank(key));
    /**
     * @name: industryStockCodeStr
     * @auther: 熊鑫
     * @return: java.lang.String
     * @date: 2020/7/17 10:12
     * @description: 获取板块的股票代码字符串, 偷懒直接返回json
     * {t:'pt012069/chr',p:1,total:72,l:10,o:0,data:'sz002839,sh601577,sh600908,sh601128,sz002807,sz002142,sh601916,sh601009,sh600000,sh600926'}
     */
    private PageData<StockDTO> getStockRank(StockRankRequest request) {
        PageData<StockDTO> stockRank = new PageData();
        String requestUrl = UrlUtil.getSinaStockRankUrl(request);
        try {
            String stockCodeStr = httpSupport.get(requestUrl);
            JSONArray json = JSONArray.parseArray(stockCodeStr);
            stockRank.setPageIndex(request.getPageIndex());
            stockRank.setPageSize(request.getPageSize());
            stockRank.setRank(json.stream().map(
                    LambdaUtil.mapWrapper(e -> {
                        JSONObject object = (JSONObject) e;
                        StockDTO stockDTO = new StockDTO(object.getString("symbol"));
                        return stockDTO;
                    })
            ).filter(Objects::nonNull).collect(Collectors.toList()));
        } catch (IOException e) {
            stockRank.setRequestFail(1);
            log.error("StockRank http connect fail! connect url-{},error message{}", requestUrl, e);
        } catch (Exception e) {
            log.error("StockRank data resolve fail! connect url-{},error message{}", requestUrl, e);
        }
        return stockRank;
    }


    public LoadingCache<String,Integer> stockCountCache = Caffeine.newBuilder().expireAfterWrite(1,TimeUnit.DAYS).build(key -> getStockCount(key));
    private Integer getStockCount(String node) throws IOException{
        return Integer.valueOf(httpSupport.get(UrlUtil.getSinaStockCountUrl(node)).replaceAll("\"", ""));
    }


    public LoadingCache<Boolean,List<String[]>> allStockAndIndexCache = Caffeine.newBuilder().expireAfterWrite(1,TimeUnit.DAYS).build(key -> getAllStockAndIndex(key));
    private List<String[]> getAllStockAndIndex(boolean stock) throws IOException {
        List<String[]> allStock = new ArrayList<>();
        String requestUrl;
        if (stock) {
            requestUrl = UrlUtil.getAllStockUrl();
        } else {
            requestUrl = UrlUtil.getAllIndexUrl();
        }
        String all = httpSupport.get(requestUrl);
        String[] arr1 = all.toLowerCase().split("<br/>");
        for (int j = 1; j < arr1.length; j++) {
            String[] arr2 = arr1[j].split(",");
            if (arr2.length >= 3) {
                allStock.add(Arrays.copyOfRange(arr2, 1, 3));
            }
        }
        return allStock;
    }

    public LoadingCache<String,List<ConvertBondDTO>> convertBondCache = Caffeine.newBuilder().expireAfterWrite(1,TimeUnit.DAYS).build(key -> getConvertBondList());
    private List<ConvertBondDTO> getConvertBondList() throws IOException{
        Integer pageIndex = 1;
        String convertBondRank = httpSupport.get(UrlUtil.getConvertBondRankUrl(pageIndex));
        JSONObject jsonObject = JSONObject.parseObject(convertBondRank);
        JSONArray list = jsonObject.getJSONObject("result").getJSONArray("data");
        while (jsonObject.getJSONObject("result").getInteger("pages").equals(pageIndex)) {
            convertBondRank = httpSupport.getNoException(UrlUtil.getConvertBondRankUrl(++pageIndex));
            jsonObject = JSONObject.parseObject(convertBondRank);
            list.addAll(jsonObject.getJSONObject("result").getJSONArray("data"));
        }
        return list.toJavaList(ConvertBondDTO.class);
    }
}


