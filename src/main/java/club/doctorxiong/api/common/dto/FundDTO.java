package club.doctorxiong.api.common.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @author : 熊鑫
 * @ClassName : Fund
 * @description : 基金详情信息
 * @date : 2019/6/6 16:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FundDTO implements Serializable {
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

    public void setExpectData(FundExpectDataDTO expectData){
        if(expectData!=null){
            this.expectWorthDate=expectData.getExpectWorthDate();
            this.expectWorth=expectData.getExpectWorth();
            this.expectGrowth=expectData.getExpectGrowth();
        }
    }

}