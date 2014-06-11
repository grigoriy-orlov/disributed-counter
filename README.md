# Distributed counter project

## Building

`cd <PROJECT_DIR>`

`mvn clean install`
  
#### initiator
  
`cd <PROJECT_DIR>/initiator`

`mvn assembly:assembly`

#### echo

`cd <PROJECT_DIR>/echo`

`mvn assembly:assembly`

#### proxy

`cd <PROJECT_DIR>/proxy`

`mvn assembly:assembly`

## Starting

#### initiator

`java -jar <PROJECT_DIR>/initiator/target/distributed-counter-initiator-{$version}-jar-with-dependencies.jar -sf /tmp/sender -rf /tmp/receiver -lsa localhost -lsp 8007 -rsa localhost -rsp 8001`

#### echo

`java -jar <PROJECT_DIR>/echo/target/distributed-counter-echo-{$version}-jar-with-dependencies.jar -f /tmp/echo -lsa localhost -lsp 8003 -rsa localhost -rsp 8005`

#### proxy

`java -jar <PROJECT_DIR>/proxy/target/distributed-counter-proxy-{$version}-jar-with-dependencies.jar -esa localhost -esp 8003 -isa localhost -isp 8007 -lsae localhost -lspe 8005 -lsai localhost -lspi 8001`

## Running
1. type `start` in proxy console; 
2. type `start` in echo console;
3. type `start` in initiator console;
4. wait some minutes;
5. type `stop` in initiator console;
6. wait some seconds;
7. type `start` in initiator console;
8. wait some minutes;
9. type `exit` in initiator console;
10. wait some seconds;
11. type `exit` in echo console;
11. type `exit` in proxy console.

# Improvements list
1. normalize console output on startup and running;
2. connection pool like service (+ add reconnect logic) and socket -> connection;
3. common service interface;
4. make the same start params for apps;
5. make per-config-parser;
6. move queue size to config;
7. add instant queue size to JMX;
8. add integration tests for services;
9. add service init-on-startup (@PostConstruct);
10. do NIO receive right (less coping);
11. state: int -> Enum;
12. ConnectionPool.get(): nonblocking -> blocking;
13. move interval in sorter to config;
14. add help printing
15. refactor configParsers (move common parts and others) 





