package club.doctorxiong.api.interceptor;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.HttpParams;
import club.doctorxiong.api.common.RedisKeyConstants;
import club.doctorxiong.api.entity.Token;
import club.doctorxiong.api.service.ITokenService;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 登录拦截校验
 *
 * @author zhongshenghua
 * @date 2021/5/28
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DefaultRedisScript<Long> visitLimit;


    @Autowired
    private ITokenService tokenProvider;

    private Map<String, Integer> tokenType = new ConcurrentHashMap();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteAddr = CommonHttpInterceptor.getIpAddress(request);

        Long times = 0L;
        try {
            String token = HttpParams.getRequestToken(request);
            if (StringUtils.isEmpty(token)) {
                //无token统计ip
                times = redisTemplate.execute(visitLimit, Collections.singletonList(RedisKeyConstants.getIpLimitRedisKey(remoteAddr)), "100", "3600");
                if (times == 0) {
                    responseMessage(response, CommonResponse.FAIL("每小时免费100次,www.doctorxiong.club获得更多"));
                    return false;
                }
            }else {
                //未缓存token数据库获取
                String redisKey = RedisKeyConstants.getUserTokenRedisKey(token);
                if (!redisTemplate.hasKey(redisKey)) {
                    //token缓存为null
                    Token tokenData = tokenProvider.getToken(token);
                    if (tokenData == null) {
                        responseMessage(response, CommonResponse.FAIL("无效的token"));
                        return false;
                    } else if (tokenData.getEndDate().compareTo(LocalDateTime.now()) < 0) {
                        responseMessage(response, CommonResponse.FAIL("过期的token,请续费"));
                        return false;
                    } else {
                        //创建了token缓存一小时一万次
                        tokenType.put(tokenData.getToken(), tokenData.getType());
                        redisTemplate.execute(visitLimit, Collections.singletonList(redisKey), tokenType.get(tokenData.getToken()) == 3 ? "10000" : "99999", "3600");
                    }
                } else {

                    if(!tokenType.containsKey(token)){
                        times = redisTemplate.execute(visitLimit, Collections.singletonList(redisKey), "10000" , "3600");
                    }else {
                        times = redisTemplate.execute(visitLimit, Collections.singletonList(redisKey), tokenType.get(token) == 3 ? "10000" : "199999", "3600");
                    }
                    if (times == 0) {
                        responseMessage(response, CommonResponse.FAIL("token使用频率过高,稍后尝试"));
                        return false;
                    }
                }
            }
            // LoginUser loginUser = new LoginUser();
            // loginUser.setUid(JwtUtil.getUserId(token));
            // 设置当前用户信息到本地线程
            // UserContext.setLoginUser(loginUser);
            // log.info("token "+ token + " ip:" + remoteAddr + " times:" + times);
        } catch (Exception e) {
            log.error("token 处理未知异常：" + e.getMessage());
            responseMessage(response, CommonResponse.FAIL("校验token失败"));
            return false;
        }
        return true;
    }

    /**
     * 将错误信息转换为json 放到 HttpServletResponse
     */
    public static void responseMessage(HttpServletResponse response, CommonResponse errorResponse) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSONString(errorResponse));
        out.flush();
        out.close();
    }
}
