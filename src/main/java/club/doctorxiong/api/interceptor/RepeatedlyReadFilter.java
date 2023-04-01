package club.doctorxiong.api.interceptor;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 把Http请求参数对象替换为自定义实现的可重复读取对象RequestWrapper(由于流只能读取一次，所以使用此包装类对HttpServletRequest对象进行包装，读取完之后再将内容塞回去)
 * @author zhongshenghua
 * @date 2021/5/17
 */
public class RepeatedlyReadFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if (servletRequest instanceof HttpServletRequest) {
			requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
		}
		if (requestWrapper == null) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			filterChain.doFilter(requestWrapper, servletResponse);
		}
	}

	@Override
	public void destroy() {

	}
}