package club.doctorxiong.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Auther: 熊鑫
 * @Date: 2020/7/16 22
 * @Description: 行业板块信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndustryDetailDTO implements Serializable {
    //行业代码
    private String industryCode;
    private String name;

    private String averagePrice;
    private String priceChange;
    private String changePercent;
    //成交量手
    private String volume;
    //成交额万
    private String turnover ;
    //股票数量
    private String stockNum;
    //领涨股
    private String stockCode;
    //示例:012090~石油石化~0~0~24~24~724.18~-15.91~-2.15~10929400.00~684518.00~sh603353~sz300839
    public IndustryDetailDTO(String raw) {
        String[] value=raw.split("~");
        this.industryCode=value[0];
        this.name=value[1];
        this.stockNum=value[5];
        this.averagePrice=value[6];
        this.priceChange=value[7];
        this.changePercent=value[8];
        this.volume=value[9];
        this.turnover=value[10];
        this.stockCode=value[11];
    }

    public IndustryDetailDTO(String industryCode, String name) {
        this.industryCode = industryCode;
        this.name = name;
    }
}
