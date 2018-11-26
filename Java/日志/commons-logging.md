## 一、`commons-logging`

- Jakarta Commons Logging(JCL)是一个接口，自身的日志系统十分弱小
- 基本jar包有 commons-logging-1.2.jar

### 1.1.日志级别



### 1.2.配置文件

> 默认的配置（自动配置）步骤:

1. 先在classpath下查找commons-logging.properties文件
2. 若没有commons-logging.properties文件，则在classpath中寻找log4j的包
3. 若没有log4j的包，则使用java日志类（JUL）的实现类（自定义）
4. 若JUL的实现类也没有，就使用commons-logging自己提供的一个简单日志实现类（SimpleLog）

> 配置文件:`commons-logging.properties`

```properties
#默认配置
org.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog
#指定使用log4j日志框架
org.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger
```

```java
private static Log logger = LogFactory.getLog(CommonsLogging.class);
```



### 1.3.优点

- 解耦:若不希望系统完全依赖于某个日志框架，使用该接口可以切换到对应的日志框架

### 1.4.缺点

- 实现查找logger的算法比较复杂，效率不高
- 存在无法修复的bug，当出现一些class loader之类的异常时，不能修复