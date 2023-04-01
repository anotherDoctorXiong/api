package club.doctorxiong.api.common.request;

import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 手机支付结果回调,金额,时间,类型(支付宝,微信)
 * @Author: 熊鑫
 * @Date: 2020/1/11 17
 * @Description:
 */
@ToString
public class PhonePayCallBackRequest implements Serializable {
    private BigDecimal money;

    private LocalDateTime time;

    private String type;


    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
