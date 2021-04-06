package com.trade.demo.filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseXssFilter extends ExclusionSupportFilter {
    public ResponseXssFilter(String context, String exclusions) {
        super(context, exclusions);
    }

    @Override
    public void destroy() {

    }

    @Override
    void doInit(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException {
        ResponseWrapper responseWrapper = new ResponseWrapper(httpResponse);
        chain.doFilter(httpRequest, responseWrapper);

        try (PrintWriter printWriter = httpResponse.getWriter()) {
            printWriter.write(Jsoup.clean(new String(responseWrapper.getResponseData(), "UTF-8"), Whitelist.relaxed()));
        }
    }
}
