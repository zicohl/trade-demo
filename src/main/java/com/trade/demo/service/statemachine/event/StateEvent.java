package com.trade.demo.service.statemachine.event;

import lombok.Data;
import lombok.ToString;

/**
 * @author honglu
 * @since 2022/6/11
 */

@Data
@ToString
public class StateEvent {
    private String id;
    private IEventType type;
    private Object payload;

    public StateEvent(String id, IEventType type) {
        this.id = id;
        this.type = type;
    }

    public StateEvent(String id, IEventType type, Object payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }
}
