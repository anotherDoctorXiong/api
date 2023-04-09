package club.doctorxiong.api.interceptor;


import club.doctorxiong.api.common.HttpParams;
import club.doctorxiong.api.common.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

/**
 * 通用拦截器，进行请求日志的打印
 * @author zhongshenghua
 * @date 2021/5/17
 */
@Slf4j
public class CommonHttpInterceptor extends HandlerInterceptorAdapter {

    private static final String UNKNOWN = "unknown";
    private static final String MONITOR_HEALTH = "/monitor/health";
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long start = System.currentTimeMillis();
        String url = request.getRequestURL().toString();
        // 接口过滤打印
        if (url.contains(MONITOR_HEALTH)) {
            return true;
        }

        String method = request.getMethod();
        String queryString = "";
        // 去掉最后一个空格
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (String value : values) {
                queryString += key + "=" + value + "&";
            }
        }
        // URL 参数
        queryString = "".equals(queryString) ? null : queryString.substring(0, queryString.length() - 1);

        // body 参数
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String bodyParams = HttpParams.replace(requestWrapper.getBody());

        // header参数
        String headersParams = HttpParams.getHeadersInfo(requestWrapper);
        // 对方公网IP
        String cIp = CommonHttpInterceptor.getIpAddress(request);
        long end = System.currentTimeMillis();
        String traceId = generateTraceId();
        MDC.put("traceId", traceId);
        RequestContext.setRequest(String.format(
                "应用请求参数  url: %s, method: %s, query-params: %s, body-params: %s, headers-params: %s, c-ip: %s, run-time: %s, traceId: %s",
                url, method, queryString, bodyParams, headersParams, cIp, (end - start) ,traceId + ""));
        /*log.info(String.format(
                "应用请求参数  url: %s, method: %s, query-params: %s, body-params: %s, headers-params: %s, c-ip: %s, run-time: %s, traceId: %s",
                url, method, queryString, bodyParams, headersParams, cIp, (end - start) ,traceId + ""));*/

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {
        RequestContext.clear();
    }

    private String generateTraceId() {
        byte[] randomBytes = new byte[10];
        new SecureRandom().nextBytes(randomBytes);
        return ENCODER.encodeToString(randomBytes);
    }


    /**
     * 获取用户真实IP地址
     * http://blog.csdn.net/zhenzhendeblog/article/details/49702575
     *
     * @param request 请求参数
     * @return ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        //不管转发多少次，取第一位
        String remoteIp = ip.split(",")[0];
        MDC.put("remote_ip", remoteIp);
        return remoteIp;
    }
}

