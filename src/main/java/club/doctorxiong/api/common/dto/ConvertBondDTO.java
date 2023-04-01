package club.doctorxiong.api.common.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 可转债的数据映射
 * @author xiongxin
 */
@Data
public class ConvertBondDTO implements Serializable {


    /**
     * 债券代码
     */
    @JsonProperty("convertBondCode")
    private String SECURITY_CODE;
    /**
     * 债券名称
     */
    @JsonProperty("convertBondName")
    private String SECURITY_NAME_ABBR;

    /**
     * 申购日期
     */
    @JsonProperty("valueDate")
    private String VALUE_DATE;
    /**
     * 股权登记日
     */
    @JsonProperty("securityStartDate")
    private String SECURITY_START_DATE;
    /**
     * 申购代码
     */
    @JsonProperty("subCode")
    private String CORRECODE;

    /**
     * 正股代码
     */
    @JsonProperty("stockCode")
    private String CONVERT_STOCK_CODE;

    /**
     * 正股简称
     */
    @JsonProperty("stockName")
    private String SECURITY_SHORT_NAME;

    /**
     * 正股价
     */
    @JsonProperty("stockPrice")
    private String CONVERT_STOCK_PRICE;

    /**
     * 转股价
     */
    @JsonProperty("transferPrice")
    private String TRANSFER_PRICE;

    /**
     * 转股价值
     */
    @JsonProperty("transferValue")
    private String TRANSFER_VALUE;

    /**
     * 债现价
     */
    @JsonProperty("convertBondPrice")
    private String CURRENT_BOND_PRICE;

    /**
     * 转股溢价
     */
    @JsonProperty("transferPremiumPatio")
    private String TRANSFER_PREMIUM_RATIO;

    /**
     * 每股配售额
     */
    @JsonProperty("firstPerPlacing")
    private String FIRST_PER_PREPLACING;

    /**
     * 发行规模
     */
    @JsonProperty("actualIssueScale")
    private String ACTUAL_ISSUE_SCALE;

    /**
     * 中签发布日
     */
    @JsonProperty("bondStartDate")
    private String BOND_START_DATE;

    /**
     * 中签率
     */
    @JsonProperty("bondGainRate")
    private String ONLINE_GENERAL_LWR;

    /**
     * 最大申购手数
     */
    @JsonProperty("subMax")
    private Integer ONLINE_GENERAL_AAU;



}
