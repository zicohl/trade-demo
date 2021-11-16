package com.trade.demo;

import org.apache.coyote.AbstractProtocol;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(proxyBeanMethods = false, exclude = {TransactionAutoConfiguration.class})
public class TradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<?> customizer() {
        return (container) -> {
            if (container instanceof TomcatServletWebServerFactory) {
                TomcatServletWebServerFactory tomcat = (TomcatServletWebServerFactory) container;
                tomcat.addContextCustomizers(new TomcatContextCustomizer[]{(context) -> {
                    context.setCookieProcessor(new LegacyCookieProcessor());
                }});
                tomcat.addConnectorCustomizers(
                        connector -> ((AbstractProtocol<?>) connector.getProtocolHandler()).setConnectionTimeout(10000));
            }
        };

    }
}
