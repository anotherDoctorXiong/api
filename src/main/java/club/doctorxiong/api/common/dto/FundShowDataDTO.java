package club.doctorxiong.api.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * @Auther: 熊鑫
 * @Date: 2020/7/7 23
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FundShowDataDTO extends CacheDTO implements Serializable {

    /**
     * 基金代码
     * */
    private String code;
    /**
     * 基金名称
     * */
    private String name;
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
     * 净值日期
     * */
    private LocalDate netWorthDate;

    /**
     * 基金类型
     * */
    private String fundType;
    /**
     * 净值估算更新日期
     * */
    private LocalDateTime expectWorthDate;

    /**
     * 年增长
     * */
    private String thisYearGrowth;


    public FundShowDataDTO(FundDTO fundDTODetail) {
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

    public void setExpectData(FundExpectDataDTO expectData) {
        this.expectWorth=expectData.getExpectWorth();
        this.expectWorthDate=expectData.getExpectWorthDate();
        this.expectGrowth=expectData.getExpectGrowth();
    }

    //    0         1              2            3        4       5     6     7      8     9     10      11
    // 162717,广发再融资主题(LOF)A,GFZRZZTLOFA,2022-04-27,1.0879,1.0879,9.72,-4.17,-16.70,-24.28,-25.33,-24.66,-5.81,14.76,-27.32,8.79,2016-12-19,1,-24.6607,1.50%,0.15%,1,0.15%,1,7.82


    public FundShowDataDTO(String[] arr) {
        this.code = arr[0];
        this.name = arr[1];
        this.fundType=arr[2];

        this.netWorth = new BigDecimal(arr[4]);
        try {
            this.netWorthDate = LocalDate.parse(arr[3]);
            this.totalWorth = new BigDecimal(arr[5]);
        } catch (Exception e) {
            log.error(e.getMessage(), Arrays.deepToString(arr));
        }
        this.dayGrowth = arr[6];
        this.lastWeekGrowth = arr[7];
        this.lastMonthGrowth = arr[8];
        this.lastThreeMonthsGrowth = arr[9];
        this.lastSixMonthsGrowth = arr[10];
        this.lastYearGrowth = arr[11];
        this.thisYearGrowth = arr[14];
    }
}
