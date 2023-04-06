package club.doctorxiong.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Auther: 熊鑫
 * @Date: 2020/5/26 09
 * @Description: 基金最新持仓
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * 基金持仓数据
 * @author xiongxin
 * @return
 */
public class FundPositionDTO extends CacheDTO implements Serializable {
    // 标题 嘉实农业产业股票  2020年1季度股票投资明细
    private String title;
    // 数据更新日期
    private LocalDate date;
    // 股票占比
    private String stock;
    // 债券占比
    private String bond;
    // 现金占比
    private String cash;
    // 总资产(亿元)
    private String total;
    // 持仓的股票数据
    private List<String[]> stockList;


}
