package com.trade.demo.service.statemachine;

import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.IState;
import com.trade.demo.service.statemachine.transition.Transition;

/**
 * @author honglu
 * @since 2022/6/11
 */
public interface IStateMachine {
    IState currentState();

    void registerPostCallback(Runnable runnable, boolean async);

    void setCurrentState(IState state);

    void initStateMachine(Transition transition);

    void inputEvent(StateEvent event);

    IState lookupTransition(Transition transition);

    Class<? extends IState> getInitState();

    Class<? extends IState> getFinalState();

    IState createState(Class<? extends IState> cls);

}
