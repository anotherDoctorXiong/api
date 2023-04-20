package club.doctorxiong.api.interceptor;


import club.doctorxiong.api.common.CommonResponse;
import club.doctorxiong.api.common.HttpParams;
import club.doctorxiong.api.common.dto.TokenDTO;
import club.doctorxiong.api.service.ITokenService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;


/**
 * 登录拦截校验
 *
 * @author zhongshenghua
 * @date 2021/5/28
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private ITokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = HttpParams.getRequestToken(request);
        if (StringUtils.isEmpty(token)) {
            TokenDTO ipToken = tokenService.getTokenCache(CommonHttpInterceptor.getIpAddress(request));
            if (!ipToken.tokenRefreshTimes()) {
                responseMessage(response, CommonResponse.FAIL("每小时免费100次,www.doctorxiong.club获得更多"));
                return false;
            }
        } else {
            TokenDTO tokenDTO = tokenService.getTokenCache(token);
            if (tokenDTO.getType().equals(1)) {
                responseMessage(response, CommonResponse.FAIL("无效的token"));
                return false;
            }
            if (tokenDTO.getEndDate().compareTo(LocalDate.now()) < 0) {
                responseMessage(response, CommonResponse.FAIL("过期的token,请续费"));
                return false;
            }
            if (!tokenDTO.tokenRefreshTimes()) {
                responseMessage(response, CommonResponse.FAIL("token使用频率过高,稍后尝试"));
                return false;
            }
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
