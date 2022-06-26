package com.example.reggei.filter;

import com.alibaba.fastjson.JSON;
import com.example.reggei.common.BaseContext;
import com.example.reggei.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * @author Hao zeng
 * @create 2022-06-23 21:41
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求: {}", requestURI);
        // 定义不需要处理的请求路径
        String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/static/**",
            "/user/sendMsg", // 移动端发送短信
            "/user/login"   // 移动端登录
        };
        // 2. 判断本次请求是否需要处理
        boolean check = check(requestURI, urls);

        // 3. 如果不需要处理，则直接放行
        if(check){
            log.info("本次请求不需要处理: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 4.1 判断登录状态，如果已经登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录， 用户id为: {}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }
        // 4.2 (移动端用户)判断登录状态，如果已经登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录， 用户id为: {}", request.getSession().getAttribute("user"));

            Long empId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        // 5. 如果未登录则返回未登录结果,通过输出流向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean check(String requestURI, String[] urls){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) return true;
        }
        return false;
    }
}
