package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	public void destroy() {
		

	}

	public void doFilter(
			ServletRequest req, 
			ServletResponse res, 
			FilterChain chain)
			throws IOException, ServletException {
			//tomcat调用此方法并传入参数，传入的是Http开头的参数。
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			//有3个请求不需要过滤，将他们排除掉
			String[] paths = new String[]{"/toLogin.do","/login.do","/createImg.do"};
			String currPath = request.getServletPath();
			for(String p : paths) {
				if(p.equals(currPath)) {
					chain.doFilter(request, response);
					return;
				}
			}
			//尝试从session中获取帐号，
			//登录成功时可以获取到帐号，否则就获取不到帐号。
			HttpSession session = request.getSession();
			String adminCode = (String)session.getAttribute("adminCode");
			//根据帐号判断是否已登录
			if(adminCode == null) {
				//没登录，重定向到toLogin.do
				response.sendRedirect("/netctoss/toLogin.do");
			} else {
				//已登录，让请求继续执行
				chain.doFilter(request, response);
			}

	}

	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
