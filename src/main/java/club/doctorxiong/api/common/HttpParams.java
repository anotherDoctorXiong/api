package club.doctorxiong.api.common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http 参数处理工具
 * @author ...
 */
@Slf4j
public class HttpParams {

	private static final Pattern PATTERN = Pattern.compile("[\t\r\n]");
	/**
	 * token传输的http Header
	 */
	public final static String TOKEN_HEADER = "token";

	public static String getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			if (key.contains("accessToken") || key.contains("user_id") || key.contains("timestamp")
					|| key.contains("sid") || key.contains("sign") ) {
				String value = request.getHeader(key);
				map.put(key, value);
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 去除字符串中的回车、换行符、制表符
	 * @param str str
	 * @return str
	 */
	public static String replace(String str) {
		String dest = "";
		if (str != null) {
			Matcher m = PATTERN.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 功能：从 httpRequest 对象中获取Jwt
	 * 详情：先从header中取,没有拿到,再从request中的参数中取
	 *
	 * @return 没有则返回空
	 */
	public static String getRequestToken(HttpServletRequest httpRequest) {
		String bearToken = httpRequest.getHeader(TOKEN_HEADER);
		if (StringUtils.isEmpty(bearToken)) {
			bearToken = httpRequest.getParameter("token");
		}

		if (StringUtils.isEmpty(bearToken)) {
			return null;
		}
		return bearToken;
	}
}
