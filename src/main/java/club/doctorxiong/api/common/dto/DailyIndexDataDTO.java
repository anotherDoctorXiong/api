package club.doctorxiong.api.common.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author xiongxin
 * @since 2022-06-25
 */

public class DailyIndexDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private LocalDate date;


    /**
     * 上证指数
     */
    private BigDecimal indexData;

    /**
     * 上证指数涨幅
     */
    private BigDecimal indexGrowth;

    /**
     * 北向资金
     */
    private BigDecimal northFundAmount;

    /**
     * 主力净资金
     */
    private BigDecimal mainFundAmount;

    /**
     * 主力净资金占比
     */
    private BigDecimal mainFundProportion;

    /**
     * 小单净资金
     */
    private BigDecimal subFundAmount;

    /**
     * 小单净资金占比
     */
    private BigDecimal subFundProportion;

}
