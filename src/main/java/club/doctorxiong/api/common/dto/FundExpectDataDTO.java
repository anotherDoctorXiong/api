package club.doctorxiong.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * "fundcode":"007339","name":"鏄撴柟杈炬勃娣�300ETF鑱旀帴C","jzrq":"2020-07-02","dwjz":"1.5193","gsz":"1.5290","gszzl":"0.64","gztime":"2020-07-03 10:32";
 * @author: xiongxin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundExpectDataDTO implements Serializable {

    /**
     * 净值估算更新日期
     */
    private LocalDateTime expectWorthDate;
    /**
     * 净值估算
     */
    private BigDecimal expectWorth;
    /**
     * 净值估算增长率
     */
    private String expectGrowth;

   
}



