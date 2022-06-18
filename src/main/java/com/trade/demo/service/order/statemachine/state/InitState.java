package com.trade.demo.service.order.statemachine.state;

import com.trade.demo.service.order.statemachine.machine.EventType;
import com.trade.demo.service.order.statemachine.machine.TransitionType;
import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.AbstractState;
import com.trade.demo.service.statemachine.transition.Transition;
import com.trade.demo.service.statemachine.transition.ITransitionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author honglu
 * @since 2022/6/12
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InitState extends AbstractState {
    ITransitionType transitionType;

    @Override
    public ITransitionType getTransitionType() {
        return transitionType;
    }

    @Override
    public Transition enter(Transition transition) {
        log.info("Entering state {} {}", this.getLogPrefix(), transition.getStateEvent().getId());
        this.transitionType = transition.getTransitionType();
        return null;
    }

    @Override
    public void leave(Transition transition) {
        log.info("Leaving state {} {}", this.getLogPrefix(), transition.getStateEvent().getId());
    }

    @Override
    public ITransitionType handleEvent(StateEvent event) {
        log.info("handle event {} {} {}", this.getLogPrefix(), event.getId(), event.getType());
        if (event.getType() == EventType.COMPLETE) {
            return TransitionType.ORDER_COMPLETE;
        }
        return null;
    }
}
