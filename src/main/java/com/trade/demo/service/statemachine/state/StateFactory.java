package com.trade.demo.service.statemachine.state;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author honglu
 * @since 2022/6/11
 */

@Component
public class StateFactory implements ApplicationContextAware {
    private static ApplicationContext context;

    public static IState createState(Class<? extends IState> cls) {
        return context.getBean(cls);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
