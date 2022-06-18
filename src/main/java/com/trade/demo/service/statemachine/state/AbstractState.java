package com.trade.demo.service.statemachine.state;

import com.trade.demo.service.statemachine.IStateMachine;
import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.transition.ITransitionType;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @author honglu
 * @since 2022/6/12
 */

@Slf4j
public abstract class AbstractState implements IState {
    private transient IStateMachine parent;
    private transient IStateMachine root;
    private transient String prefix;

    @Override
    public IStateMachine getParentStateMachine() {
        return parent;
    }

    @Override
    public IStateMachine getRootStateMachine() {
        return root;
    }

    @Override
    public void init(IStateMachine stateMachine) {
        this.parent = stateMachine;
        this.root = stateMachine;
        if (stateMachine instanceof IState) {
            this.root = ((IState) stateMachine).getRootStateMachine();
            prefix = MessageFormat.format("{0}.{1}", ((IState) stateMachine).getLogPrefix(), this.getClass().getSimpleName());
        } else {
            prefix = this.getClass().getSimpleName();
        }
    }

    @Override
    public ITransitionType handleEvent(StateEvent event) {
        log.warn("event {} for id {} is not handled in current state {}", event.getType(), event.getId(), this.getLogPrefix());
        return null;
    }

    @Override
    public ITransitionType doHandleEvent(StateEvent event) {
        log.info("start to handle event {} id {} in {}", event.getType(), event.getId(), this.getLogPrefix());
        ITransitionType type = this.handleEvent(event);
        log.info("end to handle event {} id {} in {} with transition {}", event.getType(), event.getId(), this.getLogPrefix(), type);
        return type;
    }

    @Override
    public String getLogPrefix() {
        return prefix;
    }
}
