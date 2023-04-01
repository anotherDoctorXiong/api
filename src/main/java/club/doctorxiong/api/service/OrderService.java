package club.doctorxiong.api.service;



import club.doctorxiong.api.common.InnerException;
import club.doctorxiong.api.common.LocalDateTimeFormatter;
import club.doctorxiong.api.entity.Email;
import club.doctorxiong.api.entity.Token;
import club.doctorxiong.api.common.dto.OrderResultDTO;
import club.doctorxiong.api.common.dto.TokenDTO;
import club.doctorxiong.api.common.dto.UserOrderListDTO;
import club.doctorxiong.api.common.request.EmailOrderRequest;
import club.doctorxiong.api.common.request.PhonePayCallBackRequest;
import club.doctorxiong.api.common.request.TokenOrderRequest;
import club.doctorxiong.api.uitls.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import club.doctorxiong.api.uitls.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;



/**
 * @Auther: 熊鑫
 * @Date: 2020/1/8 21
 * @Description: websocket开启的服务
 */
@Service
@Slf4j
public class OrderService {

    //使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同，也可以不同!
    private static String KEY="aaDJL2d9DfhLZO0z";
    private static String IV="412ADDSSFA342442";

    private static BigDecimal interval=new BigDecimal("0.10");

    @Autowired
    private ITokenService tokenService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param order
     * @name: createTempOrder
     * @auther: 熊鑫
     * @return: club.doctorxiong.stub.dto.pay.TempOrder
     * @date: 2020/1/18 21:46
     * @description: 创建待支付的订单并返回给前端需要支付的金额
     */
    public OrderResultDTO createTokenOrder(TokenOrderRequest order) {

        OrderResultDTO result=new OrderResultDTO();
        ValueOperations valueOperation = redisTemplate.opsForValue();

        //检测手机号码
        if (!StringUtil.isPhone(order.getPhone())) {
            InnerException.exInvalidParam("无效的手机号码");
        }

        //给当前订单上锁,防止重复下单
        if (valueOperation.get(order.getPhone()) != null) {
            InnerException.exInvalidParam("您有未支付的订单正在处理,请几分钟后再次尝试");
        } else {
            redisTemplate.opsForValue().set(order.getPhone(), 1, 5 * 60, TimeUnit.SECONDS);
        }
        //type-3(10块)type-2(20块)目前只有两种
        BigDecimal price=new BigDecimal((order.getOrderType()==3?10:20)*order.getMonthCount()).setScale(2,RoundingMode.UP);
        //生成唯一订单价格
        while (valueOperation.get(price.toString() + order.isPayMode()) != null) {
            price=price.subtract(interval);
            if(price.compareTo(BigDecimal.ZERO)==-1){
                InnerException.exInvalidParam("当前支付人数过多,请几分钟后再次尝试");
            }
        }

        result.setPayAmount(price);
        result.setPayMode(order.isPayMode());
        result.setPhone(order.getPhone());
        result.setCreateTime(LocalDateTimeFormatter.getTimeNow());
        log.info("创建待支付订单:" + price.toString() + order.isPayMode());

        //订单设置为未支付状态
        order.setPaySuccess(false);
        redisTemplate.opsForValue().set(price.toString() + order.isPayMode(), order, 5 * 60, TimeUnit.SECONDS);
        return result;
    }

    public void setPayResult(PhonePayCallBackRequest result)  {

        BigDecimal payMoney=result.getMoney();
        ValueOperations valueOperations=redisTemplate.opsForValue();
        String key=payMoney.toString()+ "alipay".equals(result.getType());
        TokenOrderRequest currentOrder=(TokenOrderRequest)valueOperations.get(key);
        if(currentOrder==null){
            log.error("发现无法处理的订单（及时退款或人工处理）_"+result );
            return;
        }else {
            //更新订单信息
            currentOrder.setPaySuccess(true);
            finishTokenOrder(currentOrder);
            redisTemplate.delete(currentOrder.getPhone());
            valueOperations.set(key,currentOrder,valueOperations.getOperations().getExpire(key, TimeUnit.SECONDS),TimeUnit.SECONDS);
        }
    }

    /**
     * @name: getTokenOrderStatus
     * @auther: 熊鑫
     * @param payId
     * @return: boolean
     * @date: 2020/10/11 11:47
     * @description: 查询订单是否支付成功,前端采用轮询的查询方式
     */
    public boolean getPayStatus(String payId){
        TokenOrderRequest order=(TokenOrderRequest)redisTemplate.opsForValue().get(payId);
        boolean result=order!=null?order.isPaySuccess():false;
        if(result){
            redisTemplate.delete(payId);
        }
        return result;
    }

    /**
     * @name: orderCancel
     * @auther: 熊鑫
     * @param payId
     * @return: void
     * @date: 2020/10/11 14:21
     * @description: 取消支付
     */
    public void payCancel(String payId){
        log.info("取消支付"+payId);
        //删除订单锁
        TokenOrderRequest order=(TokenOrderRequest)redisTemplate.opsForValue().get(payId);
        if(order!=null){
            redisTemplate.delete(order.getPhone());
        }
        redisTemplate.delete(payId);
    }

    /**
     * @param tokenOrderRequest
     * @name: finishOrder
     * @auther: 熊鑫
     * @return: boolean
     * @date: 2020/1/30 17:52
     * @description: 完成订单需要后台判断是新订单还是续费订单
     */
    public void finishTokenOrder(TokenOrderRequest tokenOrderRequest) {

        LocalDateTime now = LocalDateTime.now();
        Token exitToken = tokenService.getTokenByPhoneAndType(tokenOrderRequest.getPhone(), tokenOrderRequest.getOrderType());
        // 长时间续费优惠6个月以上赠1个月,1年赠三个月
        if(tokenOrderRequest.getMonthCount() >= 12){
            tokenOrderRequest.setMonthCount(tokenOrderRequest.getMonthCount() + 3);
        }else if(tokenOrderRequest.getMonthCount() >= 6){
            tokenOrderRequest.setMonthCount(tokenOrderRequest.getMonthCount() + 1);
        }
        LocalDateTime expireTime = now.plusMonths(tokenOrderRequest.getMonthCount());
        if (exitToken != null) {
            //续费
            if (exitToken.getEndDate().compareTo(now) < 0) {
                // 已过期
                exitToken.setEndDate(expireTime);
            } else {
                //未过期
                exitToken.setEndDate(exitToken.getEndDate().plusMonths(tokenOrderRequest.getMonthCount()));
            }
            //清除该token当天的限制缓存,防止续费后仍提示过期。
            redisTemplate.delete(exitToken.getToken());
            tokenService.saveOrUpdate(exitToken);
        } else {
            //订购token服务
            String token = StringUtil.getRandomString(10);
            while (tokenService.getToken(token) != null) {
                token = StringUtil.getRandomString(10);
            }
            Token newToken = new Token(tokenOrderRequest.getPhone(),expireTime, token, tokenOrderRequest.getOrderType());
            tokenService.save(newToken);
        }
        log.info(tokenOrderRequest.getPhone()+"_充值成功服务为期_"+ tokenOrderRequest.getMonthCount()+"个月");
    }

    public void finishEmailOrder(EmailOrderRequest emailOrderRequest) {
        //校验邮箱
        if(!StringUtil.isEmail(emailOrderRequest.getEmail())){
            InnerException.exInvalidParam("无效的邮箱");
        }

        if(!redisTemplate.hasKey(emailOrderRequest.getEmail())||!emailOrderRequest.getCaptcha().equals(redisTemplate.opsForValue().get(emailOrderRequest.getEmail()))){
            InnerException.exInvalidParam("邮箱验证失败");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMonths(emailOrderRequest.getMonthCount());

        Email exitEmail = emailService.getEmail(emailOrderRequest.getEmail());
        if(exitEmail != null){
            LocalDateTime indexTime = exitEmail.getEndDate().compareTo(LocalDateTime.now()) > 0? exitEmail.getEndDate():LocalDateTime.now();
            exitEmail.setEndDate(indexTime.plusMonths(emailOrderRequest.getMonthCount()));
            emailService.updateById(exitEmail);
        }else {
            Email email = new Email();
            email.setEmail(emailOrderRequest.getEmail());
            email.setEndDate(LocalDateTime.now().plusMonths(emailOrderRequest.getMonthCount()));
            emailService.save(email);
        }
        //发送订购或续订通知
        try {
            messageService.sendMimeMessage(emailOrderRequest.getEmail(),"小熊同学","获取每日资讯服务"
                    + emailOrderRequest.getMonthCount()+"个月");
        } catch (Exception e) {
            log.info("发送邮件失败");
        }
    }

    public void cancelEmailOrder(Integer orderId) {
        Email email=emailService.getById(orderId);
        if(email==null){
            InnerException.exInvalidParam("无效的订单");
        }
        emailService.removeById(orderId);
    }

    public UserOrderListDTO getOrder(String query) {
        try {
            query= StringUtil.phoneDecrypt(query, KEY,IV).trim();
        } catch (Exception e) {
            InnerException.exInvalidParam("数据解析失败");
        }
        UserOrderListDTO result=new UserOrderListDTO();
        //手机号则查询token
        if(StringUtil.isPhone(query)){
            result.setTokenList(BeanUtil.mapList(tokenService.getTokenByPhone(query), TokenDTO.class));
        }else if(StringUtil.isEmail(query)){
            // result.setEmailList(Arrays.asList(emailService.getEmail(query)));
        }

        if(result.getTokenList()==null){
            InnerException.ex("该号码或邮箱未订购任何服务",400);
        }
        return result;
    }



    public void getEmailCaptcha(String email) {
        String captcha= StringUtil.getCaptcha();
        if(redisTemplate.hasKey(email)){
            InnerException.exInvalidParam("验证码已发送,请稍后重试");
        }
        try {
            messageService.sendMimeMessage(email,"验证码","您的验证码为:<h2>"+captcha+"</h2>");
        } catch (Exception e) {
            log.error("getEmailCaptcha fail" + e.getMessage());
            InnerException.exInvalidParam("验证码发送失败");
        }
        redisTemplate.opsForValue().set(email,captcha,300,TimeUnit.SECONDS);
    }

    public void refreshToken(Integer orderId) {
        //订购token服务
        String token = StringUtil.getRandomString(10);
        while (tokenService.getToken(token) != null) {
            token = StringUtil.getRandomString(10);
        }
        Token order = new Token();
        order.setId(orderId);
        order.setToken(token);
        tokenService.saveOrUpdate(order);
    }
}
