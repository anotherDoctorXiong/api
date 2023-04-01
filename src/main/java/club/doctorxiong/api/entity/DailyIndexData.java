package club.doctorxiong.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
@TableName("daily_index_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyIndexData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @TableId("date")
    private LocalDate date;


    /**
     * 上证指数
     */
    @TableField("index_data")
    private BigDecimal indexData;

    /**
     * 上证指数涨幅
     */
    @TableField("index_growth")
    private BigDecimal indexGrowth;

    /**
     * 北向资金
     */
    @TableField("north_fund_amount")
    private BigDecimal northFundAmount;

    /**
     * 主力净资金
     */
    @TableField("main_fund_amount")
    private BigDecimal mainFundAmount;

    /**
     * 主力净资金占比
     */
    @TableField("main_fund_proportion")
    private BigDecimal mainFundProportion;

    /**
     * 小单净资金
     */
    @TableField("sub_fund_amount")
    private BigDecimal subFundAmount;

    /**
     * 小单净资金占比
     */
    @TableField("sub_fund_proporion")
    private BigDecimal subFundProporion;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public BigDecimal getIndexGrowth() {
        return indexGrowth;
    }

    public void setIndexGrowth(BigDecimal indexGrowth) {
        this.indexGrowth = indexGrowth;
    }
    public BigDecimal getNorthFundAmount() {
        return northFundAmount;
    }

    public void setNorthFundAmount(BigDecimal northFundAmount) {
        this.northFundAmount = northFundAmount;
    }
    public BigDecimal getMainFundAmount() {
        return mainFundAmount;
    }

    public void setMainFundAmount(BigDecimal mainFundAmount) {
        this.mainFundAmount = mainFundAmount;
    }
    public BigDecimal getMainFundProportion() {
        return mainFundProportion;
    }

    public void setMainFundProportion(BigDecimal mainFundProportion) {
        this.mainFundProportion = mainFundProportion;
    }
    public BigDecimal getSubFundAmount() {
        return subFundAmount;
    }

    public void setSubFundAmount(BigDecimal subFundAmount) {
        this.subFundAmount = subFundAmount;
    }
    public BigDecimal getSubFundProporion() {
        return subFundProporion;
    }

    public void setSubFundProporion(BigDecimal subFundProporion) {
        this.subFundProporion = subFundProporion;
    }

    public BigDecimal getIndexData() {
        return indexData;
    }

    public void setIndexData(BigDecimal indexData) {
        this.indexData = indexData;
    }

    @Override
    public String toString() {
        return "DailyIndexData{" +
                "date=" + date +
                ", indexGrowth=" + indexGrowth +
                ", northFundAmount=" + northFundAmount +
                ", mainFundAmount=" + mainFundAmount +
                ", mainFundProportion=" + mainFundProportion +
                ", subFundAmount=" + subFundAmount +
                ", subFundProporion=" + subFundProporion +
                "}";
    }
}
