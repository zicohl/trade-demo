package com.trade.demo.filter;

import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.util.ServletPathMatcher;
import com.trade.demo.util.SafeRequestUri;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class ExclusionSupportFilter implements Filter {
    private static final String PARAM_NAME_EXCLUSIONS = "exclusions";

    private static final String PARAM_NAME_CONTENT = "context";

    private List<String> excludesPattern;
    private String contextPath;

    private PatternMatcher pathMatcher = new ServletPathMatcher();

    public ExclusionSupportFilter(String context, String exclusions) {
        excludesPattern = Arrays.asList(exclusions.split("\\s*,\\s*"));
        contextPath = context;
    }

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        String exclusions = filterConfig.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (exclusions != null && exclusions.trim().length() != 0) {
            excludesPattern = Arrays.asList(exclusions.split("\\s*,\\s*"));
        }
        String context = filterConfig.getInitParameter(PARAM_NAME_CONTENT);
        if (context != null && context.trim().length() != 0) {
            contextPath = context;
        }
    }

    abstract void doInit(FilterConfig filterConfig) throws ServletException;

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        requestUri = SafeRequestUri.getSaftRequestUri(requestUri);
        if (isExclusion(requestUri)) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            doFilter(httpRequest, httpResponse, chain);
        }
    }

    abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    private boolean isExclusion(String requestUri) {
        if (excludesPattern == null) {
            return false;
        } else {
            if (contextPath != null && requestUri.startsWith(contextPath)) {
                requestUri = requestUri.substring(contextPath.length());
                if (!requestUri.startsWith("/")) {
                    requestUri = "/" + requestUri;
                }

                for (String exclude : excludesPattern) {
                    if (pathMatcher.matches(exclude, requestUri)) {
                        return true;
                    }

                }
            }

            return false;
        }

    }
}
