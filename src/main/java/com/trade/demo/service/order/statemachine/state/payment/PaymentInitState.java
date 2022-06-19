package com.trade.demo.service.order.statemachine.state.payment;

import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.AbstractState;
import com.trade.demo.service.statemachine.transition.ITransitionType;
import com.trade.demo.service.statemachine.transition.Transition;
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
public class PaymentInitState extends AbstractState {

    @Override
    public ITransitionType getTransitionType() {
        return null;
    }

    @Override
    public Transition enter(Transition transition) {
        log.info("Entering state {} {}", this.getLogPrefix(), transition.getStateEvent().getId());

        log.info("payment init");

        return new Transition(transition.getTransitionType(), this, transition.getStateEvent());
    }

    @Override
    public void leave(Transition transition) {
        log.info("Leaving state {} {}", this.getLogPrefix(), transition.getStateEvent().getId());
    }

    @Override
    public ITransitionType handleEvent(StateEvent event) {
        return null;
    }
}
