package com.example.ribbonconsumer.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @description: initialize the HystrixRequestContext
 * @create: 2019-12-21 09:31
 **/
@WebFilter(filterName = "hystrixRequestContextServletFilter",
            urlPatterns = "/*",asyncSupported = true)
@Slf4j
public class HystrixRequestContextServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("hystrixRequestContextServletFilter....");
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        chain.doFilter(request, response);
    }
}
