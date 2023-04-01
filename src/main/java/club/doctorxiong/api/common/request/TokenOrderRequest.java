package club.doctorxiong.api.common.request;

import java.io.Serializable;


/**
 * 临时订单信息
 * @Author: 熊鑫
 * @Date: 2020/1/11 18
 */
public class TokenOrderRequest implements Serializable {

    private int orderId;
    private String phone;
    private Integer monthCount;
    private int orderType;
    private boolean payMode;

    private boolean paySuccess;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public boolean isPayMode() {
        return payMode;
    }

    public void setPayMode(boolean payMode) {
        this.payMode = payMode;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }



    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}
