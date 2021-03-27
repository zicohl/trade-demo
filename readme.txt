1 执行单元测试 mvn test
2 也可启动服务 mvn spring-boot:run,已集成swagger http://localhost:8080/swagger-ui.html 和 h2 console http://localhost:8080/h2-console 连接jdbc:h2:mem:trade，用户名sa，密码sa。
  在swagger中使用新建交易，更新交易，取消交易和仓位查询即可随时观察交易执行结果

{
  "quantity": 50,
  "securityCode": "REL",
  "tradeDirection": "BUY",
  "tradeId": 1,
  "version": 1
}
新建交易

{
  "quantity": 40,
  "securityCode": "ITC",
  "tradeDirection": "SELL",
  "tradeId": 2,
  "version": 1
}
新建交易


{
  "quantity": 70,
  "securityCode": "INF",
  "tradeDirection": "BUY",
  "tradeId": 3,
  "version": 1
}
新建交易


{
  "quantity": 60,
  "securityCode": "REL",
  "tradeDirection": "BUY",
  "tradeId": 1,
  "version": 2
}
更新交易

{
  "quantity": 30,
  "securityCode": "ITC",
  "tradeDirection": "BUY",
  "tradeId": 2,
  "version": 2
}
取消交易

{
  "quantity": 20,
  "securityCode": "INF",
  "tradeDirection": "SELL",
  "tradeId": 4,
  "version": 1
}
新建交易