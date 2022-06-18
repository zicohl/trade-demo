package com.trade.demo.service.order.statemachine.machine;

import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import com.trade.demo.service.order.statemachine.state.FinalState;
import com.trade.demo.service.statemachine.IStateMachine;
import com.trade.demo.service.statemachine.event.IEventType;
import com.trade.demo.service.statemachine.event.StateEvent;
import com.trade.demo.service.statemachine.state.IState;
import com.trade.demo.service.statemachine.transition.Transition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author honglu
 * @since 2022/6/12
 */
@Service
@Slf4j
public class StateMachineService {
    private static final String STATE_MACHINE = "order.state.machine";

    private RedisTemplate<String, IStateMachine> redisTemplate;

    @Autowired
    void setRedisTemplate(RedisConnectionFactory connectionFactory) {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StateSerializer());
        redisTemplate.afterPropertiesSet();
    }

    private String getRedisKey(String orderNum) {
        return MessageFormat.format("{0}.{1}", STATE_MACHINE, orderNum);
    }

    private StateMachine newOrderMachine(String orderNum) {
        StateMachine stateMachine = new StateMachine();
        stateMachine.initStateMachine(new Transition(null, null, new StateEvent(orderNum, EventType.PLACE_ORDER, orderNum)));
        return stateMachine;
    }

    private void persistStateMachine(String orderNum, IStateMachine stateMachine) {
        String key = getRedisKey(orderNum);
        redisTemplate.opsForValue().set(key, stateMachine);
    }

    private IStateMachine restoreStateMachine(String orderNum) {
        String key = getRedisKey(orderNum);
        return redisTemplate.opsForValue().get(key);
    }

    private void removeStateMachine(String orderNum) {
        String key = getRedisKey(orderNum);
        redisTemplate.delete(key);
    }


    public void startOrderState(String orderNum) {
        log.info("start order state for order {}", orderNum);
        //todo lock,check exist orderNum
        StateMachine stateMachine = newOrderMachine(orderNum);
        persistStateMachine(orderNum, stateMachine);
    }

    public void processOrderEvent(String orderNum, IEventType eventType, Object payload) {
        //todo add lock

        IStateMachine iStateMachine = restoreStateMachine(orderNum);
        if (iStateMachine == null) {
            log.error("not found state machine {}", orderNum);
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }

        StateMachine stateMachine = (StateMachine) iStateMachine;
        if (!stateMachine.getAsyncCallbacks().isEmpty()) {
            stateMachine.getAsyncCallbacks().clear();
        }

        StateEvent stateEvent = new StateEvent(orderNum, eventType, payload);
        iStateMachine.inputEvent(stateEvent);
        persistStateMachine(orderNum, iStateMachine);

        log.info("do async call back");
        for (Runnable cb : stateMachine.getAsyncCallbacks()) {
            ForkJoinPool.commonPool().submit(cb);
        }

        if (iStateMachine.currentState() instanceof FinalState) {
            removeStateMachine(orderNum);
        }
    }

    public static class StateSerializer implements RedisSerializer<IStateMachine> {

        @Override
        public byte[] serialize(IStateMachine stateMachine) throws SerializationException {
            List<String> sval = new ArrayList<>();
            sval.add(stateMachine.getClass().getName());
            IStateMachine m = stateMachine;
            do {
                IState state = m.currentState();
                sval.add(state.getClass().getName());
                m = state instanceof IStateMachine ? (IStateMachine) state : null;
            } while (m != null);
            return String.join(";", sval).getBytes();
        }

        @Override
        public IStateMachine deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null) {
                return null;
            }
            String v = new String(bytes);
            String[] classes = v.split(";");
            IStateMachine stateMachine = null;
            try {
                stateMachine = (IStateMachine) Class.forName(classes[0]).newInstance();
                IStateMachine currentMachine = stateMachine;

                for (int i = 1; i < classes.length; i++) {
                    IState state = currentMachine.createState((Class) Class.forName(classes[i]));
                    currentMachine.setCurrentState(state);
                    state.init(currentMachine);
                    currentMachine = (IStateMachine) state;
                }
            } catch (Exception e) {
                log.warn("final class");
            }


            return stateMachine;
        }
    }
}
