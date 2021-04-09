package com.trade.demo.configuration;

import com.trade.demo.filter.ResponseXssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public FilterRegistrationBean<ResponseXssFilter> responseXssFilterFilterRegistration() {
        FilterRegistrationBean<ResponseXssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(getResponseXssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("ResponseXssFilter");
        registration.setOrder(101);

        return registration;
    }

    private ResponseXssFilter getResponseXssFilter() {
        return new ResponseXssFilter("/trade", "*.html,/webjars/*,/swagger-resourece,/v2/api-docs,/h2-console*");
    }
}