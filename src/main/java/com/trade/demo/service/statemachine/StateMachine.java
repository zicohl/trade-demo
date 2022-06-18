package com.trade.demo.service.statemachine;

import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.IState;
import com.trade.demo.service.statemachine.state.StateFactory;
import com.trade.demo.service.statemachine.transition.Transition;
import com.trade.demo.service.statemachine.transition.ITransitionType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author honglu
 * @since 2022/6/11
 */

@Slf4j
public abstract class StateMachine implements IStateMachine {
    transient protected List<Runnable> callbacks = new ArrayList<>();
    transient protected List<Runnable> asyncCallbacks = new ArrayList<>();

    Map<Class<? extends IState>, Map<ITransitionType, Class<? extends IState>>> stateTransitionMapping = new ConcurrentHashMap<>();

    IState currentState;

    public StateMachine() {
        config();
    }

    protected abstract void config();

    @Override
    public IState currentState() {
        return currentState;
    }

    @Override
    public void setCurrentState(IState state) {
        currentState = state;
    }

    @Override
    public void registerPostCallback(Runnable runnable, boolean async) {
        if (async) {
            asyncCallbacks.add(runnable);
        } else {
            callbacks.add(runnable);
        }
    }

    @Override
    public void initStateMachine(Transition initTransition) {
        if (currentState == null) {
            Class<? extends IState> initState = this.getInitState();
            currentState = createState(initState);
            currentState.init(this);

            Transition media = currentState.enter(initTransition);
            if (media != null) {
                processTransition(media);
            }
        }
    }

    protected void processTransition(Transition media) {
        log.info("handle transtion {} for {}", media.getTransitionType(), media.getStateEvent().getId());
        Transition currentTransition = media;
        while (currentTransition != null) {
            IState state = lookupTransition(currentTransition);
            if (state != null) {
                log.info("leave state {} because of transition {}", currentState.getLogPrefix(), currentTransition.getTransitionType());
                currentState.leave(currentTransition);
                currentState = state;
                log.info("enter state {} because of transition {}", currentState.getLogPrefix(), currentTransition.getTransitionType());
                currentTransition = state.enter(currentTransition);
            }
        }
    }

    @Override
    public void inputEvent(StateEvent event) {
        log.info("enter inputEvent id {} event {}", event.getId(), event.getType());
        ITransitionType transitionType = currentState.doHandleEvent(event);
        if (transitionType != null) {
            Transition transition = new Transition(transitionType, currentState, event);
            processTransition(transition);
        }
        for (Runnable cb : callbacks) {
            cb.run();
        }
        log.info("leave inputEvent id {} event {}", event.getId(), event.getType());
    }

    protected void addTransition(Class<? extends IState> sourceState, ITransitionType transitionType, Class<? extends IState> targetState) {
        Map<ITransitionType, Class<? extends IState>> map = stateTransitionMapping.computeIfAbsent(sourceState, e -> new HashMap<>());
        Class<? extends IState> aClass = map.putIfAbsent(transitionType, targetState);
        if (aClass != null) {
            log.error("duplicate config for state {} and transition {}", sourceState.getSigners(), transitionType);
            throw new RuntimeException("duplicate config");
        }
    }

    @Override
    public IState lookupTransition(Transition transition) {
        Map<ITransitionType, Class<? extends IState>> map = stateTransitionMapping.computeIfAbsent(currentState.getClass(), e -> new HashMap<>());
        Class<? extends IState> targetClass = map.get(transition.getTransitionType());
        if (targetClass != null) {
            IState state = this.createState(targetClass);
            state.init(this);
            return state;
        }
        return null;
    }

    @Override
    public IState createState(Class<? extends IState> cls) {
        return StateFactory.createState(cls);
    }

    public List<Runnable> getAsyncCallbacks() {
        return asyncCallbacks;
    }
}
