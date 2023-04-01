package club.doctorxiong.api.service;


import club.doctorxiong.api.common.InnerException;
import club.doctorxiong.api.common.LocalDateTimeFormatter;
import club.doctorxiong.api.common.RedisKeyConstants;
import club.doctorxiong.api.common.page.PageData;
import club.doctorxiong.api.component.CommonDataComponent;
import club.doctorxiong.api.component.ExpireComponent;
import club.doctorxiong.api.component.FundComponent;
import club.doctorxiong.api.common.dto.FundDTO;
import club.doctorxiong.api.common.dto.FundExpectDataDTO;
import club.doctorxiong.api.common.dto.FundPositionDTO;
import club.doctorxiong.api.common.dto.FundShowDataDTO;
import club.doctorxiong.api.common.request.FundRankRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static club.doctorxiong.api.common.RedisKeyConstants.getFundExpectKey;
import static club.doctorxiong.api.uitls.LambdaUtil.mapWrapper;


/**
 * @author : 熊鑫
 * @ClassName : FundService
 * @description :
 * @date : 2019/6/4 14:40
 */
@Slf4j
@Service
public class FundService {
    @Autowired
    private FundComponent fundComponent;

    @Autowired
    private ExpireComponent expireComponent;

    @Autowired
    private RedisTemplate redisTemplate;

    public final static String HUO_BI_TYPE = "货币型";

    /**
     * 对数据做一遍缓存
     * @param fundCode
     * @return: FundDetail
     * @date: 2019/6/23 15:54
     */

    public FundDTO getFund(String fundCode, LocalDate startDate, LocalDate endDate) {
        if (!CommonDataComponent.fundCodeAndTypeMap.containsKey(fundCode)) {
            InnerException.exInvalidParam("无效的基金代码");
        }
        String fundKey = RedisKeyConstants.getFundDetailKey(fundCode);
        FundDTO fundDTODetail;
        if (!redisTemplate.hasKey(fundKey)) {
            // 从网络获取数据,新发售或停售的基金只有代码和名称,其它数据均为null,停售的基金有数据但早已停止更新
            fundDTODetail = fundComponent.getFundDetail(fundCode);
            Long expireTime;
            if (fundDTODetail.getNetWorthDate() == null && fundDTODetail.getMillionCopiesIncomeDate() == null) {
                // 新发售基金无最新净值刷新时间
                expireTime = LocalDateTimeFormatter.TEN_MINUTE_MILLISECOND;

            } else {
                expireTime = expireComponent.getDailyDataExpireTime(fundDTODetail.getNetWorthDate() == null ? fundDTODetail.getMillionCopiesIncomeDate() : fundDTODetail.getNetWorthDate());
                // 缓存时间比较大时校验下数据
                if(expireTime > LocalDateTimeFormatter.TEN_HOUR_MILLISECOND && !HUO_BI_TYPE.equals(fundDTODetail.getType())){
                    FundExpectDataDTO expectDataDTO = getFundExpect(fundCode);
                    if(expectDataDTO != null && expectDataDTO.getExpectWorthDate() != null && expectDataDTO.getExpectWorthDate().toLocalDate().compareTo(fundDTODetail.getNetWorthDate()) > 0){
                        expireTime = LocalDateTimeFormatter.MINUTE_MILLISECOND * 3;
                    }
                }
                // 存在结算为0的情况，少缓存点时间
                if(!HUO_BI_TYPE.equals(fundDTODetail.getType()) && "0".equals(fundDTODetail.getDayGrowth())){
                    expireTime = LocalDateTimeFormatter.MINUTE_MILLISECOND;
                }
            }
            log.info("缓存基金信息:" + fundCode + "数据有效期截至:" + LocalDateTimeFormatter.printValidTime(expireTime));
            redisTemplate.opsForValue().set(fundKey, fundDTODetail,expireTime,TimeUnit.MILLISECONDS);
        } else {
            fundDTODetail = (FundDTO)redisTemplate.opsForValue().get(fundKey);
        }
        if (!HUO_BI_TYPE.equals(fundDTODetail.getType())) {
            fundDTODetail.setExpectData(getFundExpect(fundCode));
        }
        if(fundDTODetail.getNetWorthData() == null && fundDTODetail.getMillionCopiesIncomeData() == null){
            return fundDTODetail;
        }
        try {
            if (startDate != null || endDate != null) {
                if (HUO_BI_TYPE.equals(fundDTODetail.getType())) {
                    int start = startDate == null ? 0 : StringUtil.getIndexOrLeft(fundDTODetail.getMillionCopiesIncomeData(), startDate.toString());
                    int end = endDate == null ? (fundDTODetail.getMillionCopiesIncomeData().length - 1) : StringUtil.getIndexOrRight(fundDTODetail.getMillionCopiesIncomeData(), endDate.toString());
                    if (end >= start) {
                        fundDTODetail.setMillionCopiesIncomeData(Arrays.copyOfRange(fundDTODetail.getMillionCopiesIncomeData(), start, end + 1));
                        fundDTODetail.setSevenDaysYearIncomeData(Arrays.copyOfRange(fundDTODetail.getSevenDaysYearIncomeData(), start, end + 1));
                    }
                } else {
                    int start = startDate == null ? 0 : StringUtil.getIndexOrLeft(fundDTODetail.getNetWorthData(), startDate.toString());
                    int end = endDate == null ? (fundDTODetail.getNetWorthData().length - 1) : StringUtil.getIndexOrRight(fundDTODetail.getNetWorthData(), endDate.toString());
                    if (end >= start) {
                        fundDTODetail.setNetWorthData(Arrays.copyOfRange(fundDTODetail.getNetWorthData(), start, end + 1));
                        fundDTODetail.setTotalNetWorthData(Arrays.copyOfRange(fundDTODetail.getTotalNetWorthData(), start, end + 1));
                    }
                }
            }
        } catch (Exception e) {
            log.error("getFund error 获取基金信息异常" + fundCode + e.getMessage());
        }
        //热度权值加一
        redisTemplate.opsForZSet().incrementScore("hotList", fundCode, 1);
        return fundDTODetail;
    }

    public List<FundDTO> getFundList(String codeStr, LocalDate startDate, LocalDate endDate){
        return Arrays.asList(codeStr.split(",")).stream().limit(50).map(
                mapWrapper(code->getFund(code,startDate,endDate))).filter(Objects::nonNull).collect(Collectors.toList());
    }



    /**
     * 获取基金每日的基础信息, 其中dayGrowth无法直接获取, 而且货币基金的估算数据为null注意处理
     * @auther: 熊鑫
     * @return: FundExpectData
     */
    public FundExpectDataDTO getFundExpect(String fundCode) {
        String redisKey = getFundExpectKey(fundCode);
        FundExpectDataDTO fundExpectDataDTO;
        //估算价格在刷新时间内每分钟刷新一次
        if (redisTemplate.hasKey(redisKey)) {
            fundExpectDataDTO = (FundExpectDataDTO) redisTemplate.opsForValue().get(redisKey);
        } else {
            fundExpectDataDTO = fundComponent.getFundExpectData(fundCode);
            long expireTime = LocalDateTimeFormatter.SECOND_MILLISECOND * 10;
            if (fundExpectDataDTO != null) {
                expireTime = expireComponent.getMinuteDataExpireTime(fundExpectDataDTO.getExpectWorthDate());
            }
            log.info("缓存基金净值估算信息:" + fundCode + "数据有效期截至:" + LocalDateTimeFormatter.printValidTime(expireTime));
            redisTemplate.opsForValue().set(redisKey, fundExpectDataDTO, expireTime, TimeUnit.MILLISECONDS);
        }
        return fundExpectDataDTO;
    }


    /**
     * @param str
     * @name: getFund
     * @auther: 熊鑫
     * @return: java.util.List<FundDetail>
     * @date: 2020/5/16 18:54
     * @description:
     */
    public List<FundShowDataDTO> getFundList(String str) {
        String[] arr = str.split(",");
        if (arr.length > 50) {
            arr = Arrays.copyOfRange(arr, 0, 50);
        }
        return Arrays.asList(arr).stream().map(
            mapWrapper(e->new FundShowDataDTO(getFund(e,null,null)))
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public PageData<FundShowDataDTO> getFundRank(FundRankRequest request) {
        String redisKey = RedisKeyConstants.getFundRankKey(request);
        PageData res = (PageData) redisTemplate.opsForValue().get(redisKey);

        if (res == null) {
            JSONObject jsonObject = fundComponent.getFundRank(request);
            JSONArray array = jsonObject.getJSONArray("datas");
            List<FundShowDataDTO> rank = new LinkedList<>();
            array.forEach((v) -> {
                String[] arr = v.toString().split(",");
                rank.add(new FundShowDataDTO(arr,getFundExpect(arr[0])));
            });
            PageData<FundShowDataDTO> stockRank = new PageData();
            stockRank.setPageIndex(jsonObject.getInteger("pageIndex"));
            stockRank.setPageSize(jsonObject.getInteger("pageNum"));
            stockRank.setTotalRecord(jsonObject.getInteger("allNum"));
            stockRank.setRank(rank);
            redisTemplate.opsForValue().set(redisKey, stockRank, LocalDateTimeFormatter.TWENTY_MINUTE_MILLISECOND, TimeUnit.MILLISECONDS);
            res = stockRank;
        }
        return res;
    }

    /**
     * @param code
     * @name: getFundPosition
     * @date: 2020/5/26 14:26
     * @description: 获取基金的仓位
     */
    public FundPositionDTO getFundPosition(String code) {
        String redisKey = RedisKeyConstants.getFundPositionKey(code);
        FundPositionDTO fundPositionDTO = (FundPositionDTO)redisTemplate.opsForValue().get(redisKey);
        if (fundPositionDTO == null ) {
            fundPositionDTO = fundComponent.getFundPosition(code);
            LocalDateTime validTime = LocalDateTimeFormatter.getTimeNow();
            validTime = validTime.plusDays(7);
            redisTemplate.opsForValue().set(redisKey, fundPositionDTO, ChronoUnit.MILLIS.between(LocalDateTimeFormatter.getTimeNow(),validTime),TimeUnit.MILLISECONDS);
        }
        return fundPositionDTO;
    }

    public List<String[]> getAllFund(String keyWord) {
        List<String[]> allFund = (List<String[]>) redisTemplate.opsForValue().get(RedisKeyConstants.ALL_FUND);
        if (allFund == null) {
            //直接过滤后端
            allFund = fundComponent.getAllFund().stream().filter(obj -> {
                JSONArray arr = (JSONArray) obj;
                return !arr.getString(2).contains("后端");
            }).map(obj -> {
                JSONArray arr = (JSONArray) obj;
                return arr.toArray(new String[0]);
            }).collect(Collectors.toList());
            redisTemplate.opsForValue().set(RedisKeyConstants.ALL_FUND, allFund, LocalDateTimeFormatter.DAY_MILLISECOND, TimeUnit.MILLISECONDS);
        }
        if(keyWord == null || keyWord.isEmpty()){
            return allFund;
        }
        // 纯数字当基金代码处理
        if(StringUtil.isDigit(keyWord)){
            allFund = allFund.stream().filter(arr -> {
                return arr[0].contains(keyWord);
            }).collect(Collectors.toList());
        }else {
            allFund = allFund.stream().filter(arr -> {
                return arr[2].contains(keyWord);
            }).collect(Collectors.toList());
        }
        return allFund;
    }
}
