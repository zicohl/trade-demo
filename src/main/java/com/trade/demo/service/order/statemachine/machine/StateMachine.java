package com.trade.demo.service.order.statemachine.machine;

import com.trade.demo.service.order.statemachine.state.CompleteState;
import com.trade.demo.service.order.statemachine.state.FinalState;
import com.trade.demo.service.order.statemachine.state.InitState;
import com.trade.demo.service.statemachine.state.IState;

/**
 * @author honglu
 * @since 2022/6/11
 */
public class StateMachine extends com.trade.demo.service.statemachine.StateMachine {
    @Override
    public Class<? extends IState> getInitState() {
        return InitState.class;
    }

    @Override
    public Class<? extends IState> getFinalState() {
        return FinalState.class;
    }

    @Override
    protected void config() {
        addTransition(InitState.class, TransitionType.ORDER_COMPLETE, CompleteState.class);
        addTransition(CompleteState.class, TransitionType.FINISH, FinalState.class);
    }
}
