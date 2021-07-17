package com.trade.demo.service.process;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Slf4j
public class SendMainService implements JavaDelegate {
    public void execute(DelegateExecution execution) {
      log.info("Sending rejection main to author.");
    }
}
