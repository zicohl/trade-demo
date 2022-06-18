package com.trade.demo.service.statemachine.state;

import com.trade.demo.service.statemachine.IStateMachine;
import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.transition.Transition;
import com.trade.demo.service.statemachine.transition.ITransitionType;

/**
 * @author honglu
 * @since 2022/6/11
 */
public interface IState {
    IStateMachine getParentStateMachine();

    IStateMachine getRootStateMachine();

    default void init(IStateMachine stateMachine) {

    }

    Transition enter(Transition transition);

    ITransitionType handleEvent(StateEvent event);

    ITransitionType doHandleEvent(StateEvent event);

    void leave(Transition transition);

    default String getLogPrefix() {
        return this.getClass().getSimpleName();
    }

    ITransitionType getTransitionType();

}
