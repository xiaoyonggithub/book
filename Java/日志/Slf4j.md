## 一、`Slf4j`

- Slf4j(Simple Logging Facade for Java)简单的日志门面
- Slf4j是一个接口，可以为任何的日志框架提供服务，没有具体的日志实现方案，用于替代commons-logging
- 使用该框架，不需要关心具体实现是使用哪个日志系统
- Slf4j没有自己的配置文件
- 基本jar包有 log4j-1.2.17.jar 和slf4j-api-1.8.0-alpha2.jar 和 slf4j-log4j12-1.8.0-alpha2.jar

```java
private static final Logger logger = LoggerFactory.getLogger(Slf4j.class);
```





### 1.1.优点

- 解耦：独立于任何日志的实现
- Slf4j提供占位符日志记录，从而移除了`isDebugEnabled()`、`isInfoEnabled()`等方法检查，增加了代码的可读性。
- 使用占位日志记录会在使用的时候才去构建日志信息（字符串），可提高内存和CPU的使用率
  - 原因：由于没有了字符串的拼接，减少了临时字符串带来的消耗

> log4j

```java
//isDebugEnabled()是否开启了debug日志级别
if (logger.isDebugEnabled()) {
    logger.debug("Processing trade with id: " + id + " symbol: " + symbol);
}
```

> Slf4j

```java
logger.debug("Processing trade with id: {} and symbol : {} ", id, symbol);
```

### 1.2.整合

![](E:\typora\images\concrete-bindings.png)



### 1.3.模块划分

- `slf4j-api`Slf4j的API模块，即项目接口，负责日志的输出
  - 从slf4j 1.8起，slf4j使用SPI的方式寻找日志实现框架，
  - 之前版本则是通过寻找指定类的方式发现并绑定实现框架
- `slf4j-jdk14`Java日志框架（JUL）与Slf4j的桥接器（即整合模块）
- `slf4j-log4j12`log4j与Slf4j的桥接器
- `slf4j-nop、slf4j-simple`是Slf4j自己提供的简单实现类，很少用
- `slf4j-ext、slf4j-migrator`为工具模块