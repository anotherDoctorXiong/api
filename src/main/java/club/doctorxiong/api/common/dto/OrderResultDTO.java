package club.doctorxiong.api.common.dto;


import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单结果
 * @Author: 熊鑫
 * @Date: 2020/3/5 14
 */
@ToString
public class OrderResultDTO implements Serializable {
    private String phone;
    private BigDecimal payAmount;
    private boolean payMode;
    private LocalDateTime createTime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public boolean isPayMode() {
        return payMode;
    }

    public void setPayMode(boolean payMode) {
        this.payMode = payMode;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
