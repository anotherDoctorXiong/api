package club.doctorxiong.api.common.dto;

import club.doctorxiong.api.uitls.StringUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * @author : 熊鑫
 * @ClassName : Fund
 * @description : 基金详情信息
 * @date : 2019/6/6 16:43
 */
@Data
@ToString
@Slf4j
public class FundDTO extends CacheDTO implements Serializable {
    /**
     * 基金代码
     * */
    private String code;
    /**
     * 基金名称
     * */
    private String name;
    /**
     * 基金类型
     * */
    private String type;
    /**
     * 单位净值
     * */
    private BigDecimal netWorth;
    /**
     * 净值估算
     * */
    private BigDecimal expectWorth;
    /**
     * 累计净值
     * */
    private BigDecimal totalWorth;
    /**
     * 净值估算增长率
     * */
    private String expectGrowth;
    /**
     * 日增长
     * */
    private String dayGrowth;
    /**
    * 周增长
    */
    private String lastWeekGrowth;
    /**
     * 一月增长
     * */
    private String lastMonthGrowth;
    /**
     * 三月增长
     * */
    private String lastThreeMonthsGrowth;
    /**
     * 六月增长
     * */
    private String lastSixMonthsGrowth;
    /**
     * 年增长
     * */
    private String lastYearGrowth;
    /**
     * 起购价格
     * */
    private String buyMin;
    /**
     * 买入费率
     * */
    private String buySourceRate;
    /**
     * 折扣买入费率
     * */
    private String buyRate;
    /**
     * 基金经理
     * */
    private String manager;
    /**
     * 当前基金规模
     * */
    private String fundScale;
    /**
     * 净值更新日期 指定格式
     * */
    private LocalDate netWorthDate;
    /**
     * 净值估算更新日期
     * */
    private LocalDateTime expectWorthDate;
    /**
     * 净值走势
     * */
    private String[][] netWorthData;
    /**
     * 累计净值走势
     * */
    private String[][] totalNetWorthData;
    /**
     * 每万分收益(货币基金数据)
     * */
    private BigDecimal millionCopiesIncome;
    /**
     * 每万分收益走势
     * */
    private String[][] millionCopiesIncomeData;
    /**
     * 每万分收益更新日期
     * */
    private LocalDate millionCopiesIncomeDate;
    /**
     * 七日年化(货币基金数据)
     * */
    private BigDecimal sevenDaysYearIncome;
    /**
     * 七日年化走势
     * */
    private String[][] sevenDaysYearIncomeData;


    /****************************/

    /**
     * 基金类型
     * */
    private String fundType;

    /**
     * 年增长
     * */
    private String thisYearGrowth;

    public FundDTO(FundDTO fundDTODetail) {
        this.code = fundDTODetail.getCode();
        this.name = fundDTODetail.getName();
        this.netWorth = fundDTODetail.getNetWorth();
        this.totalWorth = fundDTODetail.getTotalWorth();
        this.dayGrowth = fundDTODetail.getDayGrowth();
        this.lastWeekGrowth = fundDTODetail.getLastWeekGrowth();
        this.netWorthDate = fundDTODetail.getNetWorthDate();
        this.expectWorthDate = fundDTODetail.getExpectWorthDate();
        this.expectWorth = fundDTODetail.getExpectWorth();
        this.expectGrowth = fundDTODetail.getExpectGrowth();
        this.lastMonthGrowth= fundDTODetail.getLastMonthGrowth();
        this.lastThreeMonthsGrowth= fundDTODetail.getLastThreeMonthsGrowth();
        this.lastSixMonthsGrowth= fundDTODetail.getLastSixMonthsGrowth();
        this.lastYearGrowth= fundDTODetail.getLastYearGrowth();
    }



    //    0         1              2            3        4       5     6     7      8     9     10      11
    // 162717,广发再融资主题(LOF)A,GFZRZZTLOFA,2022-04-27,1.0879,1.0879,9.72,-4.17,-16.70,-24.28,-25.33,-24.66,-5.81,14.76,-27.32,8.79,2016-12-19,1,-24.6607,1.50%,0.15%,1,0.15%,1,7.82


    public FundDTO(String[] arr) {
        this.code = arr[0];
        this.name = arr[1];
        this.fundType=arr[2];

        this.netWorth = new BigDecimal(arr[4]);
        try {
            this.netWorthDate = LocalDate.parse(arr[3]);
            this.totalWorth = new BigDecimal(arr[5]);
        } catch (Exception e) {
            log.error("FundDTO 解析",e.getMessage(), Arrays.deepToString(arr));
        }
        this.dayGrowth = arr[6];
        this.lastWeekGrowth = arr[7];
        this.lastMonthGrowth = arr[8];
        this.lastThreeMonthsGrowth = arr[9];
        this.lastSixMonthsGrowth = arr[10];
        this.lastYearGrowth = arr[11];
        this.thisYearGrowth = arr[14];
    }

    public FundDTO() {
    }


    public void setExpectData(FundExpectDataDTO expectData){
        if(expectData!=null){
            this.expectWorthDate=expectData.getExpectWorthDate();
            this.expectWorth=expectData.getExpectWorth();
            this.expectGrowth=expectData.getExpectGrowth();
        }
    }

    public boolean validFund(){
        return StringUtil.isNotBlank(this.code) && StringUtil.isNotBlank(this.name) && StringUtil.isNotBlank(this.type);
    }

}