## 一、`commons-logging`

- `Jakarta Commons Logging(JCL)`是一个接口，自身的日志系统十分弱小
- 基本`jar`包有`commons-logging-1.2.jar`

### 1.1.日志级别



### 1.2.配置文件

> 默认的配置（自动配置）步骤:

1. 先在`classpath`下查找`commons-logging.properties`文件
2. 若没有`commons-logging.properties`文件，则在`classpath`中寻找`log4j`的包
3. 若没有`log4j`的包，则使用`java`日志类（`JUL`）的实现类（自定义）
4. 若`JUL`的实现类也没有，就使用`commons-logging`自己提供的一个简单日志实现类（`SimpleLog`）

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