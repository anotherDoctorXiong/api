package club.doctorxiong.api.common.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: 熊鑫
 * @Date: 2020/7/17 10
 * @Description: 板块的股票排行  page=1&num=15&sort=settlement&asc=0&node=sh_b&symbol=
 */
@ToString
@Data
public class KLineRequest {

    private int type;
    private String stockCode;

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
}
