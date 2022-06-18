package com.trade.demo.service.statemachine.transition;

import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.IState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author honglu
 * @since 2022/6/12
 */

@Data
@ToString
@AllArgsConstructor
public class Transition {
    private ITransitionType transitionType;

    private IState originalState;

    private StateEvent stateEvent;
}
