package cn.wolfcode.sso.filter;

import com.sun.deploy.net.HttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @category ：登录拦截器
 *
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //如果是登录有关操作的，不拦截
        String path = request.getRequestURI();
        //1.session还有效
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        if (token != null) {
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        if (path.contains("login") || path.endsWith(".css") || path.endsWith(".js")) {
            //放行
            filterChain.doFilter(request, response);
            return;
        } else {
            System.out.println("path:"+path);
            if (path.endsWith("verify")) {
                //放行
                filterChain.doFilter(request,response);
                return;
            }else {
                response.sendRedirect("login");
            }
        }

    }

    @Override
    public void destroy() {

    }
}
