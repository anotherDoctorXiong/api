package club.doctorxiong.api.service;

import club.doctorxiong.api.common.InnerException;
import club.doctorxiong.api.common.LocalDateTimeFormatter;
import club.doctorxiong.api.common.RedisKeyConstants;
import club.doctorxiong.api.common.dto.IndustryDetailDTO;
import club.doctorxiong.api.common.dto.StockDTO;
import club.doctorxiong.api.common.request.StockRankRequest;
import club.doctorxiong.api.component.CommonDataComponent;
import club.doctorxiong.api.component.ExpireComponent;
import club.doctorxiong.api.component.StockComponent;
import club.doctorxiong.api.entity.KLine;
import club.doctorxiong.api.uitls.LambdaUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.common.page.PageData;

import club.doctorxiong.api.uitls.StringUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static club.doctorxiong.api.common.LocalDateTimeFormatter.printValidTime;
import static club.doctorxiong.api.common.RedisKeyConstants.INDUSTRY_RANK;



/**
 * @Auther: 熊鑫
 * @Date: 2019/6/24 21
 * @Description: 不同K线的编码格式, 股票代码+d(日),w(周),m(月)+0(不复权),1(前复权),2(后复权)
 */
@Service
@Slf4j
public class StockService  {
    @Autowired
    private StockComponent stockComponent;
    @Autowired
    private CommonDataComponent commonDataComponent;
    @Autowired
    private ExpireComponent expireComponent;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IKLineService kLineService;


    /**
     * @name: getIndustryRank
     * @auther: 熊鑫
     * @return: java.util.List<club.doctorxiong.stub.dto.stock.IndustryDetail>
     * @date: 2020/7/16 22:49
     * @description: 获取板块排行
     */
    public List<IndustryDetailDTO> getIndustryRank() {
        List<IndustryDetailDTO> list = (List<IndustryDetailDTO>) redisTemplate.opsForValue().get(INDUSTRY_RANK);
        if (list == null) {
            list = stockComponent.getStockIndustryRank();
            long validTime = expireComponent.getDataExpireTime(20);
            log.info("缓存板块有效期截至:" + printValidTime(validTime));
            redisTemplate.opsForValue().set(INDUSTRY_RANK, list, validTime, TimeUnit.MILLISECONDS);
        }
        return list;
    }

    public PageData<StockDTO> getStockRank(StockRankRequest request) {
        String redisKey = RedisKeyConstants.getStockRankKey(request);
        JSONArray json = (JSONArray) redisTemplate.opsForValue().get(redisKey);
        if (json == null) {
            json = stockComponent.getSinaStockRank(request);
            long validTime = expireComponent.getDataExpireTime(5);
            redisTemplate.opsForValue().set(redisKey, json, validTime, TimeUnit.MILLISECONDS);
            log.info("股票排行" + redisKey + "股票数据缓存至:" + printValidTime(validTime));
        }
        if (json.size() < 1) {
            InnerException.exInvalidParam("无效的排行数据");
        }
        PageData<StockDTO> stockRank = new PageData();
        stockRank.setPageIndex(request.getPageIndex());
        stockRank.setPageSize(request.getPageSize());
        Integer allCount = (Integer) redisTemplate.opsForValue().get("StockCount" + request.getNode());
        if (allCount == null) {
            allCount = stockComponent.getSinaStockCount(request.getNode());
            redisTemplate.opsForValue().set("StockCount" + request.getNode(), allCount, LocalDateTimeFormatter.DAY_MILLISECOND, TimeUnit.MILLISECONDS);
        }
        stockRank.setTotalRecord(allCount);
        stockRank.setRank(getStockList(json));
        return stockRank;
    }


    /**
     * @param type 区分复权数据
     * @name: getKLine
     * @auther: 熊鑫
     * @return: void
     * @date: 2020/9/2 16:10
     * @description:
     */
    public String[][] getDayData(String stockCode, LocalDate startDate, LocalDate endDate, int type) {
        LocalDateTime now = LocalDateTime.now();
        if (type < 0 || type > 2) {
            InnerException.exInvalidParam("type(0-不复权,1-前复权,2-后复权)");
        }
        //key规则,股票代码+d(日),w(周),m(月)+0(不复权),1(前复权),2(后复权)
        stockCode = StringUtil.getTotalStockCode(stockCode);
        String key = stockCode + "d" + type;
        /*if (!CommonDataComponent.allStockCode.contains(stockCode) && !CommonDataComponent.fundCodeAndTypeMap.containsKey(stockCode.substring(2))) {
            InnerException.exInvalidParam("无效的股票代码");
        }*/

        KLine dayKLine = kLineService.getByKey(key);
        //K线数据为两部分组成,历史K线和当日K线,当日K线为实时K线数据一般由当日分时数据
        String[][] kLineData;
        //数据库有符合要求的数据
        if (dayKLine != null && dayKLine.getValidTime().compareTo(now) > 0) {
            kLineData = dayKLine.getArr();
        } else {
            //数据库无符合要求的数据则从网络获取数据
            StockDTO stockDTO = stockComponent.getKLineData(stockCode, type);
            if(stockDTO.invalidStock()){
                InnerException.exInvalidParam("无效的股票代码");
            }
            kLineData = stockDTO.getArrData();

            //将新数据确定有效时间后插入数据库
            LocalDateTime validDate = expireComponent.getDailyDataValidTime(stockDTO.getDate().toLocalDate());

            //插入或更新
            KLine newKLine = new KLine();
            newKLine.setCode(key);
            log.info(key + "有效期:" + validDate);
            newKLine.setValidTime(validDate);
            newKLine.setData(StringUtil.ArrToBytes(kLineData));
            kLineService.saveOrUpdate(newKLine);
        }
        // 开始替换最后一条数据,一般是当日日K,由分时数据统计出来
        StockDTO stockDTO = getStock(stockCode);
        LocalDate stockDate = stockDTO.getDate().toLocalDate();
        if(kLineData.length == 0){
            return kLineData;
        }
        LocalDate kLineDate = LocalDate.parse(kLineData[kLineData.length-1][0]);
        // 日期;开盘;收盘;最高;最低;成交量(手)
        if(kLineDate.compareTo(stockDate) == 0)  {
            kLineData[kLineData.length -1] =  new String[]{stockDate.toString(),stockDTO.getOpen(),stockDTO.getPrice(),stockDTO.getHigh(),stockDTO.getLow(),stockDTO.getVolume()};
        }
        // 考虑开始时间和结束时间为null的情况
        int start = startDate == null ? 0 : StringUtil.getIndexOrLeft(kLineData, startDate.toString());
        int end = endDate == null ? (kLineData.length - 1) : StringUtil.getIndexOrRight(kLineData, endDate.toString());
        if (start > end) {
            InnerException.exInvalidParam("该股票无数据返回");
        }
        //返回对应时间段内的数据
        return Arrays.copyOfRange(kLineData, start, end + 1);
    }


    /**
     * @param week 周K还是月K
     * @name: getMonthData
     * @auther: 熊鑫
     * @return: java.lang.String[][]
     * @date: 2020/9/6 22:53
     * @description:
     */
    public String[][] getDateRangeData(String stockCode, LocalDate startDate, LocalDate endDate, int type, boolean week) {
        LocalDateTime now = LocalDateTime.now();
        //不同K线的编码格式,股票代码+d(日),w(周),m(月)+0(不复权),1(前复权),2(后复权)
        stockCode = StringUtil.getTotalStockCode(stockCode);
        /*if (!CommonDataComponent.allStockCode.contains(stockCode) && !CommonDataComponent.fundCodeAndTypeMap.containsKey(stockCode.substring(2))) {
            InnerException.exInvalidParam("无效的股票代码");
        }*/
        String key = stockCode + (week ? "w" : "m") + type;
        KLine kLine = kLineService.getByKey(key);
        String[][] kLineData;
        if (kLine != null && kLine.getValidTime().compareTo(now) > 0) {
            kLineData = kLine.getArr();
        } else {
            //获取日K数据计算周K
            String[][] dayKLine = getDayData(stockCode, null, null, type);

            ConcurrentHashMap<Integer, String[]> map = new ConcurrentHashMap<>();
            Arrays.asList(dayKLine).parallelStream().forEach(arr -> {
                setWeekOrMonthData(arr, map, week);
            });
            kLineData = map.values().stream().sorted(new Comparator<String[]>() {
                                                         @Override
                                                         public int compare(String[] o1, String[] o2) {
                                                             return o1[0].compareTo(o2[0]);
                                                         }
                                                     }
            ).collect(Collectors.toList()).toArray(new String[map.size()][6]);
            //从数据库获取有效时间
            LocalDateTime validDate = kLineService.getByKey(stockCode + "d" + type).getValidTime();
            //插入或更新
            KLine newKLine = new KLine();
            newKLine.setCode(key);
            log.info(key + "有效期:" + validDate.toString());
            newKLine.setValidTime(validDate);
            newKLine.setData(StringUtil.ArrToBytes(kLineData));
            kLineService.saveOrUpdate(newKLine);

        }
        //考虑开始时间和结束时间为null的情况
        int start = startDate == null ? 0 : StringUtil.getIndexOrLeft(kLineData, startDate.toString());
        int end = endDate == null ? (kLineData.length - 1) : StringUtil.getIndexOrRight(kLineData, endDate.toString());
        if (start > end) {
            InnerException.exInvalidParam("无效的时间区间");
        }
        return Arrays.copyOfRange(kLineData, start, end + 1);
    }

    /**
     * @param oneDayData
     * @param map
     * @name: setWeekData
     * @auther: 熊鑫
     * @return: java.util.concurrent.Future
     * @date: 2020/9/6 15:30
     * @description: 对计算周K月K
     */

    public void setWeekOrMonthData(String[] oneDayData, ConcurrentHashMap<Integer, String[]> map, boolean week) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oneDayData[0]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int key;
            if (week) {
                //对于跨年的一周需要特殊处理,WEEK_OF_YEAR均会返回1,将跨年的一周同步到下一年的第一周里面
                if (calendar.get(Calendar.WEEK_OF_YEAR) == 1 && calendar.get(Calendar.MONTH) > 0) {
                    key = calendar.get(Calendar.WEEK_OF_YEAR) * (calendar.get(Calendar.YEAR) + 1);
                } else {
                    key = calendar.get(Calendar.WEEK_OF_YEAR) * calendar.get(Calendar.YEAR);
                }
            } else {
                key = (calendar.get(Calendar.MONTH) + 1) * calendar.get(Calendar.YEAR);
            }
            if (!map.containsKey(key)) {
                map.put(key, oneDayData);
            } else {
                String[] arr = map.get(key);
                //数组{日期,开盘,收盘,最高,最低,成交量}多个数据合并
                //日期和收盘,显示最后一天,一般是周五
                if (arr[0].compareTo(oneDayData[0]) < 0) {
                    arr[0] = oneDayData[0];
                    arr[2] = oneDayData[2];
                } else {
                    //开盘,显示第一天的,一般是周一
                    arr[1] = oneDayData[1];
                }
                //最高价
                if (arr[3].compareTo(oneDayData[3]) < 0) {
                    arr[3] = oneDayData[3];
                }
                //最低价
                if (arr[4].compareTo(oneDayData[4]) > 0) {
                    arr[4] = oneDayData[4];
                }
                arr[5] = new BigDecimal(oneDayData[5]).add(new BigDecimal(arr[5])).toString();
                map.put(key, arr);
            }
        } catch (ParseException e) {
            return;
        }
    }

    /**
     * @param stockCode
     * @name: getStock
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.stock.Stock
     * @date: 2020/9/6 23:44
     * @description: 最新分时及股票数据, 此方法无需异常管理, 线程池会将异常结果全部丢弃
     */
    public StockDTO getStock(String stockCode) {
        stockCode = StringUtil.getTotalStockCode(stockCode);
        /*if (!CommonDataComponent.allStockCode.contains(stockCode) && !CommonDataComponent.fundCodeAndTypeMap.containsKey(stockCode.substring(2))) {
            InnerException.exInvalidParam("无效的股票代码");
        }*/
        StockDTO stockDTO = (StockDTO) redisTemplate.opsForValue().get(stockCode);
        if (stockDTO == null) {
            //无法从缓存获取
            stockDTO = stockComponent.getStockData(stockCode);
            if(stockDTO.invalidStock()){
                InnerException.exInvalidParam("无效的股票代码");
            }
            long validTime = expireComponent.getMinuteDataExpireTime(stockDTO.getDate());
            log.info("缓存股票:" + stockCode + ":" + printValidTime(validTime));
            if("ZS".equals(stockDTO.getType())){
                CommonDataComponent.allIndexMap.put(stockDTO.getCode(),stockDTO.getName());
            }else {
                CommonDataComponent.allStockMap.put(stockDTO.getCode(),stockDTO.getName());
            }

            redisTemplate.opsForValue().set(stockCode, stockDTO, validTime, TimeUnit.MILLISECONDS);
        }
        //热度权值加一
        redisTemplate.opsForZSet().incrementScore("hotList", stockCode, 1);
        return stockDTO;
    }


    public List<StockDTO> getStockList(String stockCodeStr) {
        String[] arr = stockCodeStr.split(",");
        if (arr.length > 100) {
            arr = Arrays.copyOfRange(arr, 0, 100);
        }
        return Arrays.asList(arr).parallelStream().map(
                LambdaUtil.mapWrapper(e -> {
                    StockDTO stockDTO = getStock(e);
                    stockDTO.setMinData(null);
                    return stockDTO;
                })
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public List<StockDTO> getStockList(JSONArray arr) {
        return arr.parallelStream().map(
                LambdaUtil.mapWrapper(e -> {
                    JSONObject object = (JSONObject) e;
                    StockDTO stockDTO = getStock(object.getString("symbol"));
                    stockDTO.setMinData(null);
                    return stockDTO;
                })
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * @param
     * @name: getStockAll
     * @auther: 熊鑫
     * @return: java.util.List<java.lang.String [ ]>
     * @date: 2020/8/25 21:47
     * @description: 返回所有股票(包含以退市)
     */
    public List<String[]> getStockAll(String keyWord) {
        if (!RedisKeyConstants.ALL_STOCK_DATE_OFF_SET.isEqual(LocalDate.now())) {
            commonDataComponent.refreshStock();
            RedisKeyConstants.ALL_STOCK_DATE_OFF_SET = LocalDate.now();
        }
        if (keyWord == null || keyWord.isEmpty()) {
            return CommonDataComponent.allStockMap.entrySet().stream().map(entity -> new String[]{entity.getKey(),entity.getValue()}).collect(Collectors.toList());
        }else if (StringUtil.isDigit(keyWord)) {
            return CommonDataComponent.allStockMap.entrySet().stream().filter(entity -> entity.getKey().contains(keyWord)).map(entity -> new String[]{entity.getKey(),entity.getValue()}).collect(Collectors.toList());
        } else {
            return CommonDataComponent.allStockMap.entrySet().stream().filter(entity -> entity.getValue().contains(keyWord)).map(entity -> new String[]{entity.getKey(),entity.getValue()}).collect(Collectors.toList());
        }
    }

    /**
     * @param
     * @name: getIndexAll
     * @auther: 熊鑫
     * @return: java.util.List<java.lang.String [ ]>
     * @date: 2020/8/25 22:14
     * @description: 获取所有指数
     */
    public List<String[]> getIndexAll() {
        return CommonDataComponent.allIndexMap.entrySet().stream().map(entity -> new String[]{entity.getKey(),entity.getValue()}).collect(Collectors.toList());
    }



}
