## 一、`log4j2`

-  slf4j 、log4j、logback是由同一个作者ceki开发的
- log4j2不是log4j的升级版，而是apache开发的，Log4j2里面可以配置的内容非常的多，功能也是十分的强大
- log4j2有自己的默认配置，即使不配置它，也能正常工作；自定义的配置文件只需要在classpath下防止log4j.xml就可覆盖掉默认的配置文件
- log4j2支持自动重新配置，log4j2每隔一段时间就会检查一遍这个文件是否修改，最小5s一次(可以使用`monitorInterval`设置) 
- 基本jar包有 log4j-api-2.11.1.jar 和 log4j-core-2.11.1.jar

---

### 1.1.日志级别

1.`off`:最高等级，用于关闭所有日志记录     

2.`fatal`:致命错误,级别较高,一般这种级别就不用调试时,直接重写吧     

3.`error`:指出虽然发生错误事件,但仍然不影响系统的继续运行      

4.`warm`:警告,有些信息不是错误信息,但是需要给程序员一些提示      

5.`info`:输出重要信息,使用较多     

6.`debug`:调试,一般作为最低级别      

7.`all`:最低等级，用于打开所有日志记录

---

### 1.2.配置文件

- `<Configuration>`

  - `status`设置日志打印的级别
  - `monitorinterval`设置自动检测配置文件的时间间隔，单位为s，最小值是5s

- `<Appenders>`

  - `<Console>`输出到控制台的Appenders

    - `name`设置Appender的名称
    - `target`设置打印的方式(`SYSTEM_OUT、SYSTEM.ERR`)，默认为`SYSTEM_OUT`
    - `<PatternLayout>`设置输出格式，默认值为`%m%n`

  - `<File>`输出到指定文件的Appender

    - `name`设置Appender的名称
    - `fileName`设置日志输出文件目标路径（全路径）
    - `<PatternLayout>`设置输出格式，默认值为`%m%n`

  - `<RollingFile>`定义超过指定大小自动删除旧的创建新的Appender

    - `name`设置Appender的名称
    - `fileName`设置日志输出文件目标路径（全路径）
    - `<PatternLayout>`设置输出格式，默认值为`%m%n`
    - `filePattern`设置新建日志文件名称的格式
    - `<Policies>`设置滚动的策略
      - `<TimeBasedTriggeringPolicy>`
      - 

    - 

- `<Loggers>`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 需要先配置Appenders,然后定义Logger,这样Appenders才会生效 -->
<Configuration status="WARN" monitorInterval="30">
  <Appenders>
  	<!-- 在控制台输出 -->
    <Console name="Console" target="SYSTEM_OUT">
    	<!-- 设置输出级别(level) -->
		<ThresholdFilter level ="info" onMatch="ACCEPT" onMismatch="DENY"/>
		<!-- 日志输出格式 -->
		<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
    </Console>
    
	<!-- 将日志信息打印到文件中 -->
    <File name="Log" fileName="E://logs/log.log" append="true">
    	<ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
    	<PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>
    </File>
    
	<!-- 每次大小超过size,就会在这个文件夹下面新建一个格式相同的txt文件 -->
	<RollingFile name="RollingFile" fileName="E://logs/web.log" filePattern="logs/$${date:yyyy-MM}/web-%d{MM-dd-yyyy}-%i.log.gz">
		<PatternLayout pattern="%d{yyyy-MM-dd 'at' HH:mm:ss z} %-5level %class{36} %L %M - %msg%xEx%n"/>
		<!-- 设置每个文件的最大容量 -->
		<SizeBasedTriggeringPolicy size="10 MB"/>
		<!-- 设置了同一个文件夹下最多有多少个文件,默认是7个 -->
		<DefaultRolloverStrategy max="20"/>
	</RollingFile>
  </Appenders>
  
  <Loggers>
    <Root level="error">
      <AppenderRef ref="Console"/>
      <AppenderRef ref="Log"/>
      <AppenderRef ref="RollingFile"/>
    </Root>
  </Loggers>
</Configuration>
```

```java
private static final Logger logger = LogManager.getLogger(Log4j2.class);
```

### 1.3.优点

- 插件式结构。可根据需要自己扩展log4j2，如实现自己的apppender、logger、filter
- 配置文件优化。
  - 可在配置文件中引入属性，也可以将属性直接替换或传递到组件
  - 支持jso格式的配置文件
  - 不像其它的日志框架，重新配置时不会丢失之前的日志文件
- 对Java5中并发性的支持，会尽可能的执行最低层次的加锁；解决掉了log4j中存留的死锁问题
- 异步logger，Log4j2是基于LMAX Disruptor库的。在多线程的场景下，和已有的日志框架相比，异步logger提升了10倍左右的效率

### 1.4.json配置文件

由于Log4j2使用的是`Jackson Data Processor`来解析json文件的，因此使用json格式的配置文件，需引入Jackson的依赖

```xml
<dependency>
　　<groupId>com.fasterxml.jackson.core</groupId>
　　<artifactId>jackson-core</artifactId>
　　<version>2.2.2</version>
</dependency>
 
<dependency>
　　<groupId>com.fasterxml.jackson.core</groupId>
　　<artifactId>jackson-databind</artifactId>
　　<version>2.2.2</version>
</dependency>
```

### 1.5.Log4j2配置文件的优先级（加载顺序）

(1).classpath下的名为log4j2-test.json 或者log4j2-test.jsn的文件.

(2).classpath下的名为log4j2-test.xml的文件.

(3).classpath下名为log4j2.json 或者log4j2.jsn的文件.

(4).classpath下名为log4j2.xml的文件.

### 1.6.Log4j2与Slf4j的整合



