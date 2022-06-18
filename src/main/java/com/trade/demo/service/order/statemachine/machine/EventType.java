package com.trade.demo.service.order.statemachine.machine;

import com.trade.demo.service.statemachine.event.IEventType;

/**
 * @author honglu
 * @since 2022/6/12
 */
public enum EventType implements IEventType {
    PLACE_ORDER, COMPLETE;

    @Override
    public String getName() {
        return null;
    }
}
