package com.trade.demo.service.statemachine.state;

import com.trade.demo.service.statemachine.IStateMachine;
import com.trade.demo.service.statemachine.StateMachine;
import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.transition.Transition;
import com.trade.demo.service.statemachine.transition.ITransitionType;

import java.text.MessageFormat;

/**
 * @author honglu
 * @since 2022/6/12
 */
public abstract class CompositeState extends StateMachine implements IState {
    private String prefix;
    private IStateMachine parent;
    private IStateMachine root;

    @Override
    public IStateMachine getParentStateMachine() {
        return parent;
    }

    @Override
    public IStateMachine getRootStateMachine() {
        return root;
    }

    @Override
    public String getLogPrefix() {
        return prefix;
    }

    @Override
    public Transition enter(Transition transition) {
        super.initStateMachine(transition);
        return null;
    }

    @Override
    public void init(IStateMachine stateMachine) {
        this.parent = stateMachine;
        this.root = stateMachine;

        if (stateMachine instanceof IState) {
            this.root = (IStateMachine) ((IState) stateMachine).getRootStateMachine();
            prefix = MessageFormat.format("{0}.{1}", ((IState) stateMachine).getLogPrefix(), this.getClass().getSimpleName());
        } else {
            prefix = this.getClass().getSimpleName();
        }
    }

    @Override
    public ITransitionType doHandleEvent(StateEvent event) {
        return handleEvent(event);
    }

    @Override
    public ITransitionType handleEvent(StateEvent event) {
        super.inputEvent(event);
        if (super.currentState().getClass() == this.getFinalState()) {
            return getTransitionType();
        }
        return null;
    }

    @Override
    public ITransitionType getTransitionType() {
        if (super.currentState().getClass() == this.getFinalState()) {
            return super.currentState().getTransitionType();
        }
        return null;
    }

    @Override
    public void leave(Transition transition) {

    }
}
