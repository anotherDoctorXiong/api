package club.doctorxiong.api.common.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件订单
 * @Author: 熊鑫
 * @Date: 2020/8/11 17
 * @Description:
 */
@Data
public class EmailOrderRequest implements Serializable {

    private int orderId;
    private String email;
    private String captcha;
    private Integer monthCount;


}
