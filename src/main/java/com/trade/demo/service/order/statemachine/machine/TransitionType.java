package com.trade.demo.service.order.statemachine.machine;

import com.trade.demo.service.statemachine.transition.ITransitionType;

/**
 * @author honglu
 * @since 2022/6/12
 */
public enum TransitionType implements ITransitionType {
    ORDER_COMPLETE, FINISH;

    @Override
    public String getName() {
        return null;
    }
}
