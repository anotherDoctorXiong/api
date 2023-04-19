package club.doctorxiong.api.component;


import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.FundExpectDataDTO;
import club.doctorxiong.api.common.dto.FundPositionDTO;
import club.doctorxiong.api.common.page.PageData;
import club.doctorxiong.api.common.request.FundRankRequest;
import club.doctorxiong.api.fundfactory.FillFundDetailFactoryService;
import club.doctorxiong.api.uitls.StringUtil;
import club.doctorxiong.api.uitls.UrlUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static club.doctorxiong.api.common.LocalDateTimeFormatter.getLocalDateTimeByTimestamp;


/**
 * @Auther:
 * @Date: 2022/6/9 22
 * @Description: 基金信息缓存层
 */
@Component
@Slf4j
public class FundComponent {

    @Resource
    private OkHttpComponent httpSupport;

    @Resource
    private FillFundDetailFactoryService fillFundDetail;

    @Resource
    private ExpireComponent expireComponent;

    /**
     * 基金估值缓存
     */
    public LoadingCache<String, FundExpectDataDTO> fundExpectCache = Caffeine.newBuilder().softValues().expireAfter(new Expiry<String, FundExpectDataDTO>() {
        @Override
        public long expireAfterCreate(String key, FundExpectDataDTO fundExpectDataDTO, long currentTime) {
            currentTime = System.currentTimeMillis() / 1000;
            if (fundExpectDataDTO.getRequestFail() == 1) {
                log.error(String.format("FundExpectCache 请求失败,五分钟后过期 code{%s}", key));
                return TimeUnit.MINUTES.toNanos(5);
            }

            if (fundExpectDataDTO.getResolveFail() == 1) {
                log.error(String.format("FundExpectCache 解析失败,缓存当日 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }

            if (LocalDate.now().getDayOfWeek().getValue() > 5) {
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            // 未开盘缓存至开盘
            if (currentTime < expireComponent.getTimestampOfOpenAM()) {
                log.info(String.format("FundExpectCache 缓存到上午收盘 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfOpenAM() - currentTime);
            }
            if (currentTime < expireComponent.getTimestampOfClosePM()) {
                log.info(String.format("FundExpectCache 缓存一分钟 code{%s}", key));
                return TimeUnit.MINUTES.toNanos(1);
            }
            if (currentTime - expireComponent.getTimestampOfClosePM() > 60 && fundExpectDataDTO.getExpectWorthDate() != null) {
                log.info(String.format("FundExpectCache 缓存至当日结束 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            log.info(String.format("FundExpectCache 未命中任何策略 缓存五分钟 code{%s}", key));
            return TimeUnit.MINUTES.toNanos(5); // 设置缓存过期时间为5分钟
        }

        @Override
        public long expireAfterUpdate(String key, FundExpectDataDTO value, long currentTime, long currentDuration) {
            return currentDuration; // 返回当前剩余时间，表示不更新过期时间
        }

        @Override
        public long expireAfterRead(String key, FundExpectDataDTO value, long currentTime, long currentDuration) {
            return currentDuration; // 返回剩余时间，不更新过期时间
        }
    }).build(key -> getFundExpectData(key));

    /**
     * 基金详情缓存
     */
    public LoadingCache<String, FundDTO> fundCache = Caffeine.newBuilder().softValues().expireAfter(new Expiry<String, FundDTO>() {
        @Override
        public long expireAfterCreate(String key, FundDTO fundDTODetail, long currentTime) {
            currentTime = System.currentTimeMillis() / 1000;

            if (fundDTODetail.getRequestFail() == 1) {
                log.error(String.format("FundCache 请求失败,五分钟后过期 code{%s}", key));
                return TimeUnit.MINUTES.toNanos(5);
            }

            if (fundDTODetail.getResolveFail() == 1) {
                log.error(String.format("FundCache 解析失败,缓存当日 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            if (LocalDate.now().getDayOfWeek().getValue() > 5) {
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }
            long random = System.nanoTime() % 3600 + 1800;
            /*if (fundDTODetail.getNetWorthDate() == null && fundDTODetail.getMillionCopiesIncomeDate() == null) {
                // 新发售基金或者无效基金缓存至今天结束
                log.info(String.format("FundCache 空的基金数据,缓存至当日结束 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }*/
            // 不到收盘不会更新
            if (currentTime < expireComponent.getTimestampOfClosePM() && fundDTODetail.getCode() != null) {
                log.info(String.format("FundCache 缓存至下午收盘并添加随机后缀 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime + random);
            }
            if (fundDTODetail.getNetWorthDate() != null && fundDTODetail.getNetWorthDate().isEqual(LocalDate.now())) {
                log.info(String.format("FundCache 缓存十五小时 code{%s}", key));
                return TimeUnit.HOURS.toNanos(15);
            }
            log.info(String.format("FundCache 未命中任何策略,随机缓存 code{%s}", key));
            return TimeUnit.SECONDS.toNanos(random); // 设置缓存过期再随机加个后缀
        }

        @Override
        public long expireAfterUpdate(String key, FundDTO value, long currentTime, long currentDuration) {
            return currentDuration; // 返回当前剩余时间，表示不更新过期时间
        }

        @Override
        public long expireAfterRead(String key, FundDTO value, long currentTime, long currentDuration) {
            return currentDuration; // 返回剩余时间，不更新过期时间
        }
    }).recordStats().build(key -> getFundDetail(key));

    /**
     * 获取基金排行的分页数据
     */
    public LoadingCache<FundRankRequest, PageData<FundDTO>> fundRankCache = Caffeine.newBuilder().softValues().expireAfter(new Expiry<FundRankRequest, PageData<FundDTO>>() {
        @Override
        public long expireAfterCreate(FundRankRequest key, PageData<FundDTO> pageData, long currentTime) {
            if (pageData.getRequestFail() == 1) {
                log.error(String.format("FundRankCache 缓存失败,五分钟后过期 code{%s}", key));
                return TimeUnit.MINUTES.toNanos(5);
            }
            return TimeUnit.HOURS.toNanos(3);
        }

        @Override
        public long expireAfterUpdate(FundRankRequest key, PageData<FundDTO> value, long currentTime, long currentDuration) {
            return currentDuration;
        }

        @Override
        public long expireAfterRead(FundRankRequest key, PageData<FundDTO> value, long currentTime, long currentDuration) {
            return currentDuration;
        }
    }).build(key -> getFundRank(key));

    /**
     * 获取基金持仓数据
     */
    public LoadingCache<String, FundPositionDTO> fundPositionCache = Caffeine.newBuilder().softValues().expireAfter(new Expiry<String, FundPositionDTO>() {
        @Override
        public long expireAfterCreate(String key, FundPositionDTO fundPositionDTO, long currentTime) {
            if (fundPositionDTO.getRequestFail() == 1) {
                log.error(String.format("FundPositionCache 请求失败,一小时后过期 code{%s}", key));
                return TimeUnit.HOURS.toNanos(1);
            }
            if (fundPositionDTO.getRequestFail() == 1) {
                log.error(String.format("FundPositionCache 解析失败,当天过期 code{%s}", key));
                return TimeUnit.SECONDS.toNanos(expireComponent.getTimestampOfDayEnd() - currentTime);
            }

            return TimeUnit.DAYS.toNanos(10);
        }

        @Override
        public long expireAfterUpdate(String key, FundPositionDTO value, long currentTime, long currentDuration) {
            return currentDuration;
        }

        @Override
        public long expireAfterRead(String key, FundPositionDTO value, long currentTime, long currentDuration) {
            return currentDuration;
        }
    }).build(key -> getFundPosition(key));


    /**
     * 获取全部基金数据
     */
    public LoadingCache<String, List<String[]>> allFundCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(key -> getAllFund());


    /**
     * @param fundCode : 基金代码
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.Fund
     * @date: 2020/6/9 22:51
     * @Description: 通过基金代码获取最新的基金信息
     */
    private FundDTO getFundDetail(String fundCode) {
        FundDTO fundDTODetail = new FundDTO();
        String detailUrl = UrlUtil.getFundDetailUrl(fundCode);
        try {
            String detailStr = httpSupport.get(detailUrl);
            if (detailStr.contains("<html>")) {
                return fundDTODetail;
            }
            String[] arr = detailStr.replace(" ", "").split(";");
            for (int i = 0; i < arr.length; i++) {
                //经split分割的字符串数组为空时长度仍然为1
                if (arr[i].length() <= 1) {
                    continue;
                }
                //从字符串获取相关数据
                fillFundDetail.fillFundDetail(arr[i], fundDTODetail);
            }
        } catch (IOException e) {
            log.error(String.format("FundCache http connect fail! connect url{%s},error message{%s}", detailUrl, e.getMessage()));
            fundDTODetail.setRequestFail(1);
        } catch (Exception e) {
            fundDTODetail.setResolveFail(1);
            log.error(String.format("FundCache http connect fail! connect url{%s},error message{%s}", detailUrl, e.getMessage()));
        }
        return fundDTODetail;
    }


    private FundExpectDataDTO getFundExpectData(String fundCode) {
        // 从缓存中加载数据的具体实现
        FundExpectDataDTO fundExpectDataDTO = new FundExpectDataDTO();
        String expectUrl = UrlUtil.getFundExportUrl(fundCode);
        String expectStr;
        try {
            expectStr = httpSupport.get(expectUrl);
            //部分类型的基金无法不提供净值估算
            if (expectStr != null && expectStr.length() > 10) {
                JSONObject export = JSONObject.parseObject(expectStr.substring(expectStr.indexOf('(') + 1, expectStr.lastIndexOf(')')));
                fundExpectDataDTO.setExpectGrowth(export.getString("gszzl"));
                fundExpectDataDTO.setExpectWorth(new BigDecimal(export.getString("gsz")));
                fundExpectDataDTO.setExpectWorthDate(getLocalDateTimeByTimestamp(export.getLong("gztime")));
            }
        } catch (IOException e) {
            log.error(String.format("FundExpectCache http connect fail! connect url{%s},error message{%s}", expectUrl, e.getMessage()));
            fundExpectDataDTO.setRequestFail(1);
        } catch (Exception e) {
            fundExpectDataDTO.setResolveFail(1);
            log.error(String.format("FundExpectCache data resolve fail! connect url{%s},error message{%s}", expectUrl, e.getMessage()));
        }
        return fundExpectDataDTO;
    }


    /**
     * @param code
     * @name: getFundPosition
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.fund.FundPosition
     * @date: 2020/5/26 10:18
     * @description: 获取基金持仓信息, 此处是直接爬取的html标签, 后期更新注意标签更换即可
     */
    private FundPositionDTO getFundPosition(String code) {

        FundPositionDTO fundPositionDTO = new FundPositionDTO();
        try {
            String res = httpSupport.get(UrlUtil.getHTMLUrl(code));
            Document doc = Jsoup.parse(res);
            List<String[]> list = new LinkedList<>();
            Elements links = doc.getElementsByTag("tr");
            if (links.size() > 0) {
                fundPositionDTO.setTitle(doc.getElementsByClass("left").get(0).text());
                for (int i = 1; i < links.size(); i++) {
                    Elements one = links.get(i).getElementsByTag("td");
                    String[] arr = new String[5];
                    if (one.size() == 9) {
                        arr[0] = one.get(1).text();
                        arr[1] = one.get(2).text();
                        arr[2] = one.get(6).text();
                        arr[3] = one.get(7).text();
                        arr[4] = one.get(8).text();
                        list.add(arr);
                    }
                }
            } else {
                log.error(String.format("FundPositionDTO get 基金持仓股票 fail !, url{%s}", UrlUtil.getHTMLUrl(code)));
                return fundPositionDTO;
            }
            fundPositionDTO.setStockList(list);
            String res1 = httpSupport.get(UrlUtil.getHTMLUrl1(code));
            Document doc1 = Jsoup.parse(res1);
            Elements el = doc1.getElementsByClass("w782 comm tzxq");
            if (el.size() > 0) {
                Elements td = el.first().getElementsByTag("tbody").first().getElementsByTag("tr").first().getElementsByTag("td");
                if (td.size() >= 5) {
                    fundPositionDTO.setDate(LocalDate.parse(td.get(0).text()));
                    fundPositionDTO.setStock(td.get(1).text());
                    fundPositionDTO.setBond(td.get(2).text());
                    fundPositionDTO.setCash(td.get(3).text());
                    fundPositionDTO.setTotal(td.get(td.size() - 1).text());
                }
            }
        } catch (IOException e) {
            log.error(String.format("fundRankCache http connect fail! connect url{%s},error message{%s}", UrlUtil.getHTMLUrl(code) + UrlUtil.getHTMLUrl(code), e.getMessage()));
            fundPositionDTO.setRequestFail(1);
        } catch (Exception e) {
            fundPositionDTO.setResolveFail(1);
            log.error(String.format("fundRankCache resolve fail! connect url{%s},error message{%s}", UrlUtil.getHTMLUrl(code) + UrlUtil.getHTMLUrl(code), e.getMessage()));
        }
        return fundPositionDTO;
    }


    /**
     * @param fundRankRequest : 排行及分类的条件
     * @return : java.util.List<club.doctorxiong.stub.dto.fund.ShowFund>
     * @description : 获取基金排行信息,主要用于基金的展示
     * @Exception :
     * @author : 熊鑫
     * @date : 2019/6/11 14:17
     */
    private PageData<FundDTO> getFundRank(FundRankRequest fundRankRequest) {
        PageData<FundDTO> stockRank = new PageData();
        Headers headers = new Headers.Builder().add("Referer", "http://fund.eastmoney.com/data/fundranking.html").build();
        String fundRankUrl = UrlUtil.creatGetUrlWithParams(UrlUtil.getFundRankBaseUrl(), fundRankRequest);
        try {
            String str = httpSupport.getWithHeaders(fundRankUrl, headers).replaceAll(";", "");
            JSONObject jsonObject = JSONObject.parseObject(StringUtil.getValue(str));
            JSONArray array = jsonObject.getJSONArray("datas");
            List<FundDTO> rank = new LinkedList<>();
            array.forEach((v) -> {
                String[] arr = v.toString().split(",");
                rank.add(new FundDTO(arr));
            });
            stockRank.setPageIndex(jsonObject.getInteger("pageIndex"));
            stockRank.setPageSize(jsonObject.getInteger("pageNum"));
            stockRank.setTotalRecord(jsonObject.getInteger("allNum"));
            stockRank.setRank(rank);
        } catch (IOException e) {
            log.error(String.format("fundRankCache http connect fail! connect url{%s},error message{%s}", fundRankUrl, e.getMessage()));
            stockRank.setRequestFail(1);
        } catch (Exception e) {
            log.error(String.format("fundRankCache http connect fail! connect url{%s},error message{%s}", fundRankUrl, e.getMessage()));
        }
        return stockRank;
    }


    private List<String[]> getAllFund() throws IOException {
        String str = httpSupport.get(UrlUtil.getAllFundUrl());
        JSONArray jsonArray =JSONArray.parseArray(StringUtil.getValue(str).replaceAll(";", ""));
        return jsonArray.stream().filter(obj -> {
            JSONArray arr = (JSONArray) obj;
            return !arr.getString(2).contains("后端");
        }).map(obj -> {
            JSONArray arr = (JSONArray) obj;
            return arr.toArray(new String[0]);
        }).collect(Collectors.toList());
    }


}
