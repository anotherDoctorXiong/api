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


    public final static String HUO_BI_TYPE = "货币型";

    /**
     * 对数据做一遍缓存
     * @param fundCode
     * @return: FundDetail
     * @date: 2019/6/23 15:54
     */

    public FundDTO getFund(String fundCode, LocalDate startDate, LocalDate endDate) {

        FundDTO fundDTODetail = fundComponent.fundCache.get(fundCode);
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
            log.error(String.format("getFund 截取基金信息异常! fund code{%s},message{%s}", fundCode , e.getMessage()));
        }
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
        FundExpectDataDTO fundExpectDataDTO = fundComponent.fundExpectCache.get(fundCode);
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
        PageData<FundShowDataDTO> stockRank =  fundComponent.fundRankCache.get(request);
        stockRank.getRank().forEach(fundShowDataDTO -> {
            fundShowDataDTO.setExpectData(getFundExpect(fundShowDataDTO.getCode()));
        });
        return stockRank;
    }

    /**
     * @param code
     * @name: getFundPosition
     * @date: 2020/5/26 14:26
     * @description: 获取基金的仓位
     */
    public FundPositionDTO getFundPosition(String code) {
        return fundComponent.fundPositionCache.get(code);
    }

    public List<String[]> getAllFund(String keyWord) {
        List<String[]> allFund = fundComponent.allFundCache.get("");
        if(keyWord == null || keyWord.isEmpty()){
            return allFund;
        }
        // 纯数字当基金代码处理
        if(StringUtil.isDigit(keyWord)){
            allFund = allFund.stream().filter(arr -> arr[0].contains(keyWord)).collect(Collectors.toList());
        }else {
            allFund = allFund.stream().filter(arr ->  arr[2].contains(keyWord)).collect(Collectors.toList());
        }
        return allFund;
    }
}
