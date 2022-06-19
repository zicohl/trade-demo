package com.trade.demo.service.order;

import com.trade.demo.service.order.statemachine.machine.EventType;
import com.trade.demo.service.order.statemachine.machine.StateMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private StateMachineService stateMachineService;

    public void createOrder(String orderNum) {
        stateMachineService.startOrderState(orderNum);
    }

    public void processOrder(String orderNum, String action) {
        stateMachineService.processOrderEvent(orderNum, EventType.valueOf(action), orderNum);
    }
}
