package com.trade.demo;

import com.trade.demo.api.model.TradeVo;
import com.trade.demo.po.PositionPo;
import com.trade.demo.service.PositionService;
import com.trade.demo.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class TradeApplicationTests {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private PositionService positionService;

    @Test
    void tradeTest() {
        TradeVo t1 = new TradeVo();
        t1.setTradeId(1L);
        t1.setVersion(1);
        t1.setSecurityCode("REL");
        t1.setQuantity(50);
        t1.setTradeDirection(TradeVo.TradeDirectionEnum.BUY);
        tradeService.createTrade(t1);

        TradeVo t2 = new TradeVo();
        t2.setTradeId(2L);
        t2.setVersion(1);
        t2.setSecurityCode("ITC");
        t2.setQuantity(40);
        t2.setTradeDirection(TradeVo.TradeDirectionEnum.SELL);
        tradeService.createTrade(t2);

        TradeVo t3 = new TradeVo();
        t3.setTradeId(3L);
        t3.setVersion(1);
        t3.setSecurityCode("INF");
        t3.setQuantity(70);
        t3.setTradeDirection(TradeVo.TradeDirectionEnum.BUY);
        tradeService.createTrade(t3);

        TradeVo t4 = new TradeVo();
        t4.setTradeId(1L);
        t4.setVersion(2);
        t4.setSecurityCode("REL");
        t4.setQuantity(60);
        t4.setTradeDirection(TradeVo.TradeDirectionEnum.BUY);
        tradeService.updateTrade(t4);

        TradeVo t5 = new TradeVo();
        t5.setTradeId(2L);
        t5.setVersion(2);
        t5.setSecurityCode("ITC");
        t5.setQuantity(30);
        t5.setTradeDirection(TradeVo.TradeDirectionEnum.BUY);
        tradeService.cancelTrade(t5);

        TradeVo t6 = new TradeVo();
        t6.setTradeId(4L);
        t6.setVersion(1);
        t6.setSecurityCode("INF");
        t6.setQuantity(20);
        t6.setTradeDirection(TradeVo.TradeDirectionEnum.SELL);
        tradeService.createTrade(t6);

        List<PositionPo> list = positionService.queryPosition("");
        for (PositionPo position : list) {
            log.info("{} {}", position.getSecurityCode(), position.getQuantity());
            if (position.getSecurityCode().equals("REL")) {
                assertEquals(60,position.getQuantity());
            }
            if (position.getSecurityCode().equals("ITC")) {
                assertEquals(0,position.getQuantity());
            }
            if (position.getSecurityCode().equals("INF")) {
                assertEquals(50,position.getQuantity());
            }
        }
    }

}
