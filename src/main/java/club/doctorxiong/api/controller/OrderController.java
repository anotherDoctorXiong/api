package club.doctorxiong.api.controller;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.dto.OrderResultDTO;
import club.doctorxiong.api.common.dto.UserOrderListDTO;
import club.doctorxiong.api.service.OrderService;
import club.doctorxiong.api.common.request.PhonePayCallBackRequest;
import club.doctorxiong.api.common.request.TokenOrderRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: 熊鑫
 * @Date: 2020/1/18 21
 * @Description: 订单和支付接口
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public CommonResponse<OrderResultDTO> createTokenOrder(@RequestBody TokenOrderRequest order) {
        return CommonResponse.OK(orderService.createTokenOrder(order));
    }

    @RequestMapping(value = "/pay",method = RequestMethod.POST)
    public CommonResponse setPayResult(@RequestBody PhonePayCallBackRequest result){
        orderService.setPayResult(result);
        return CommonResponse.OK();
    }

    /**
     * 查询是否支付成功,让前端轮询调用
     */
    @RequestMapping(value = "/pay/status",method = RequestMethod.GET)
    public CommonResponse<Boolean> getPayStatus(@RequestParam("payId") String price) {
        return CommonResponse.OK(orderService.getPayStatus(price));
    }

    @RequestMapping(value = "/pay/cancel",method = RequestMethod.GET)
    public CommonResponse orderCancel(@RequestParam("payId") String price) {
        orderService.payCancel(price);
        return CommonResponse.OK();
    }

    /*@RequestMapping(value = "/email",method = RequestMethod.POST)
    public CommonResponse createEmailOrder(@RequestBody EmailOrderRequest order) {
        orderService.finishEmailOrder(order);
        return CommonResponse.OK();
    }*/

    /*@RequestMapping(value = "/email",method = RequestMethod.DELETE)
    public CommonResponse cancelEmailOrder(@RequestParam("orderId") Integer orderId ) {
        orderService.cancelEmailOrder(orderId);
        return CommonResponse.OK();
    }*/

    @RequestMapping(value = "/token/refresh",method = RequestMethod.GET)
    public CommonResponse refreshToken(@RequestParam("orderId") Integer orderId ) {
        orderService.refreshToken(orderId);
        return CommonResponse.OK();
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public CommonResponse<UserOrderListDTO> getOrder(@RequestParam("phone") String phone) {
        return CommonResponse.OK(orderService.getOrder(phone));
    }


    /*@RequestMapping(value = "/email/captcha",method = RequestMethod.GET)
    public CommonResponse getEmailCaptcha(@RequestParam("email") String email) {
        orderService.getEmailCaptcha(email);
        return CommonResponse.OK();
    }*/
}
