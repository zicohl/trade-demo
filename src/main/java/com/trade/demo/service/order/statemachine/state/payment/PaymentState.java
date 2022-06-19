package com.trade.demo.service.order.statemachine.state.payment;

import com.trade.demo.service.order.statemachine.machine.TransitionType;
import com.trade.demo.service.order.statemachine.state.FinalState;
import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.CompositeState;
import com.trade.demo.service.statemachine.state.IState;
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
public class PaymentState extends CompositeState {

    @Override
    public Transition enter(Transition transition) {
        log.info("Entering state {} {}", this.getLogPrefix(), transition.getStateEvent().getId());

        super.initStateMachine(transition);

        return null;
    }

    @Override
    public void leave(Transition transition) {
        super.leave(transition);
        log.info("Leaving state {} {}", this.getLogPrefix(), transition.getStateEvent().getId());
    }


    @Override
    public ITransitionType handleEvent(StateEvent event) {
        log.info("begin event for state {} {} {}", this.getLogPrefix(), event.getType(), event.getId());
        return super.handleEvent(event);
    }

    @Override
    public Class<? extends IState> getInitState() {
        return PaymentInitState.class;
    }

    @Override
    public Class<? extends IState> getFinalState() {
        return FinalState.class;
    }

    @Override
    protected void config() {
        addTransition(PaymentInitState.class, TransitionType.PAY_PARTIAL, PaymentIdleState.class);
        addTransition(PaymentIdleState.class, TransitionType.ORDER_COMPLETE, FinalState.class);
    }

}
