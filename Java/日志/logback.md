## 一、`Logback`

- logback是由log4j的创始人离开Apache后又设计的一款开源日志
- 基本jar包有 logback-classic-1.0.13.jar 和 logback-core-1.0.13.jar 和 slf4j-api-1.6.0.jar

---

### 1.1.日志级别

- TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF
- 还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。

### 1.2.配置文件

> 配置文件的加载步骤：

1. 首先查找logback.groovy文件
2. 若找不到logback.groovy文件，则查找logback-test.xml文件
3. 若找不到logback-test.xml文件，则查找logbakc.xml文件
4. 若找不到logbakc.xml文件，则使用默认的配置（打印在控制台）

> 配置文件：logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->  
    <property name="LOG_HOME" value="E://logs/log.log" />  
    <!-- 控制台输出 -->   
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
       <!-- 日志输出编码 -->  
       <Encoding>UTF-8</Encoding>   
        <layout class="ch.qos.logback.classic.PatternLayout">   
             <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符--> 
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n   
            </pattern>   
        </layout>   
    </appender>   
    <!-- 按照每天生成日志文件 -->   
    <appender name="FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">   
        <Encoding>UTF-8</Encoding>   
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/myApp.log.%d{yyyy-MM-dd}.log</FileNamePattern>   
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>   
        <layout class="ch.qos.logback.classic.PatternLayout">  
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符--> 
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n   
            </pattern>   
       </layout> 
        <!--日志文件最大的大小-->
       <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
         <MaxFileSize>10MB</MaxFileSize>
       </triggeringPolicy>
    </appender> 
   <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->  
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder"  level="TRACE" />  
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor"  level="DEBUG" />  
    <logger name="org.hibernate.SQL" level="DEBUG" />  
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />  
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" />  
    
    <!-- 日志输出级别 -->
    <root level="INFO">   
        <appender-ref ref="STDOUT" />   
        <appender-ref ref="FILE" />   
    </root> 
     
     <!--日志异步到数据库 -->  
    <appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
        <!--日志异步到数据库 --> 
        <connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">
           <!--连接池 --> 
           <dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">
              <driverClass>com.mysql.jdbc.Driver</driverClass>
              <url>jdbc:mysql://127.0.0.1:3306/usta</url>
              <user>root</user>
              <password>root</password>
            </dataSource>
        </connectionSource>
  </appender> -->
</configuration>
```

```java
private static final Logger logger = LoggerFactory.getLogger(Logback.class);
```

#### 1.`<configuration>`

- `scan`：配置文件发生变化时，是否将重新加载
  - `ture`:默认值
- `scanPeriod`:设置监测配置文件是否修改的时间间隔
  - 若没有给出时间单位，默认为毫秒
  - 当`scan=true`时生效，默认的时间间隔为1分钟
- `debug`：是否打印logback的内部日志信息，实时监测logback的运行状态
  - false:默认值

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">  
</configuration>  
```

#### 2.`<configuration>`的子节点

- `<contextName>`设置上下文名称
  - 每个logger都关联了logger上下文，默认值为default
  - `<contextName>`可设置为其它值，用于区分不同应用程序的记录
- ` <property>`设置变量

  - ` <property>`设置的值会被插入到logger上下文中
- `<timestamp>`获取时间戳
  - `key`名称
  - `datePattern`转化为字符串的格式，遵循`java.txt.SimpleDateFormat`的格式
- `<loger>`设置某一个包或某一个类的日志打印级别、和输出位置appender
  - `name`指定包名或类名
  - `level`设置日志的打印级别；若为设置值，则会继承上级的级别
  - `addtivity`是否向上级<loger>传递打印信息
  - `<appender-ref>`零个或多个，指定日志输出的位置

- `<root>`特殊的`<loger>`元素，表示根`loger`
  - `level`设置打印级别，默认为`debug`
  - `<appender-ref>`零个或多个，指定日志输出的位置

- `<appender >`写日志的组件，负责设置日志输出的目的地和输出格式

  - `name`指定appender的名称
  - `class`设置appender的全限定名
  - `<encoder>`设置日志输出格式
    - 负责两件事，一是把日志信息转换成字节数组，二是把字节数组写入到输出流

  ```xml
  <encoder>   
     <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>   
  </encoder  
  ```

- > `ConsoleAppender `输出到控制台

  - `<target>`打印的方式` System.err `，默认值`System.out`

- > `FileAppender `输出到文件

  - `<file>`设置被写入的文件名称
    - 可以是相对目录，也可以是绝对目录
    - 上级目录不存在会自动创建
    - 没有默认值
  - `<append>`日志是否被追加到文件结尾
    - true 
    - false 默认值，会清空现存文件
  - `<prudent>`如果是 true，日志会被安全的写入文件，即使其他的`FileAppender`也在向此文件做写入操作，效率低，默认是 false

- > `RollingFileAppender`滚动记录输出到文件

  - `<file>`设置被写入的文件名称（活动文件）
    - 可以是相对目录，也可以是绝对目录
    - 上级目录不存在会自动创建
    - 没有默认值

  - `<rollingPolicy>`设置滚动策略

    - `class` 指定策略类型

    	. `TimeBasedRollingPolicy`根据时间指定滚动策略，及负责滚动也负责触发滚动	
       - `<fileNamePattern>`滚动时文件名的格式（注意：file设置活动的文件名称，若未设置file，活动文件名会按该滚动策略变化）
       - `<maxHistory>`设置保留归档文件的最大数，超出数量就删除就文件
         - 注意：删除就文件时，哪些为归档而创建目录也会被删除
    2. `FixedWindowRollingPolicy`根据固定窗口算法重命名文件的滚动策略
       - `<minIndex>`窗口索引的最小值
       - `<maxIndex>`窗口索引的最大值
       - `<fileNamePattern >`必须包含`%i`
         - 若最小值和最大值为1和2，命名模式为`mylog%i.log`,则产生归档文件`mylog1.log`和`mylog2.log`
         - 可设置文件压缩选项，如`mylog%i.log.gz `

  - `<triggeringPolicy>`设置滚动的时间
    - `class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy"`查看当前活动文件大小，若超出指定大小会被通知
    - `<maxFileSize>`设置活动文件的大小，默认值为10MB

  - `<append>`日志是否被追加到文件结尾
    - true 
    - false 默认值，会清空现存文件
  - `<prudent>`如果是 true，日志会被安全的写入文件，即使其他的`FileAppender`也在向此文件做写入操作，效率低，默认是 false

  ```xml
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">   
  
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">   
          <fileNamePattern>logFile.%d{yyyy-MM-dd}.log</fileNamePattern>   
          <maxHistory>30</maxHistory>    
      </rollingPolicy>   
  
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">   
        <maxFileSize>5MB</maxFileSize>   
      </triggeringPolicy>   
      
      <encoder>   
          <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>   
      </encoder>   
  </appender> 
  ```

  - 其他的一些` SocketAppender、SMTPAppender、DBAppender、SyslogAppender、SiftingAppender`

> 应用的实例

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">  
    <property name="APP_Name" value="myAppName" /> 
    <timestamp key="bySecond" datePattern="yyyyMMdd'T'HHmmss"/>  
    <contextName>${APP_Name} - ${bySecond}</contextName> 
    
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">  
    	<encoder>  
      		<pattern>%-4relative [%thread] %-5level %logger{35} - %msg %n</pattern>  
    	</encoder>  
   </appender>
    
    
</configuration>  
```

#### 3.`<pattern>`转化符



#### 4.格式修饰符

格式修饰符位于`%`和转换符之间

1. 左对齐标志`[-]`
2. 最小宽度，十进制表示
   - 若字符小于最小宽度，则填充
     - 默认为左填充（右对齐）
     - 填充符为空格
   - 若字符大于最小宽度，字符不会被截取
3. 最大宽度修饰符`.`加十进制数，如`[.10]`
   - 若字符大于最大宽度，会从前面截取

#### 5.`<filter>`

过滤器，每执行一个过滤器都会返回一个枚举值

- `DENY`日志会立即被抛弃不再经过它的filter
- `NEUTRAL`按顺序执行下一个filter
- `ACCEPT`日志会立即被处理，不再经过其它filter

`<filter>`被添加到`<appender>`，可以用一个或多个，多个`<filter>`时，按配置的顺序执行

##### 5.1.`LevelFilter`

`LevelFilter`级别过滤器，可根据日志级别过滤

- `<level>`设置过滤的级别
- `<onMatch>`配置符合过滤条件的操作
- `<onMismatch>`配置不符合过滤的操作

```xml
<filter class="ch.qos.logback.classic.filter.LevelFilter">   
    <level>INFO</level>   
    <onMatch>ACCEPT</onMatch>   
    <onMismatch>DENY</onMismatch>   
</filter> 
```

##### 5.2.`ThresholdFilter`

`ThresholdFilter`临界值过滤器，过滤掉低于指定临界值的日志

- 日志级别`>=`临界值，返回`NEUTRAL`
- 日志级别`<`临界值，日志会被拒绝

```xml
<filter class="ch.qos.logback.classic.filter.ThresholdFilter">   
    <level>INFO</level>   
</filter>
```

##### 5.3.`EvaluatorFilter`

`EvaluatorFilter`求值过滤器，评估、鉴别日志是否符合指定条件

- 需要额外的jar:`commons-compiler.jar和janino.jar`

- `<evaluator>`鉴别器

  - 默认鉴别器`JaninoEventEvaluato`，使用boolean表达式作为求值条件

  - `<expression>`用于配置求值条件，求值表达式暴露日志的各种字段

    | 名称              | 类型              | 描述                                                         |
    | ----------------- | ----------------- | ------------------------------------------------------------ |
    | `event`           | `LoggingEvent`    | 与记录请求相关联的原始记录事件，下面所有变量都来自event，如event.getMessage()获取`message`的值 |
    | `message`         | `String`          | 日志的原始消息                                               |
    | `formatedMessage` | `String`          | 日志格式化后消息                                             |
    | `logger`          | `String`          | logger名                                                     |
    | `loggerContext`   | `LoggerContextVo` | 日志所属的logger上下文                                       |
    | `level`           | `int`             | 级别对应的整数值(level > INFO)                               |
    | `timeStamp`       | `long`            | 创建日志的时间戳                                             |
    | `marker`          | `Marker`          | 与日志请求相关联的Marker对象，注意Marker可能为null           |
    | `mdc`             | `Map`             | 包含创建日志期间的MDC所有值得map，注意mdc可能为null          |
    | `throwable`       | `Throwable`       |                                                              |
    | `throwableProxy`  | `IThrowableProxy` |                                                              |

> logger.debug("Processing trade with id: {} and symbol : {} ", id, symbol);
>
> - 原始消息：Processing trade with id: {} and symbol : {}
> - 格式化消息：Processing trade with id: 2812 and symbol : 成都 

  - `<matcher>`匹配器
    - `<name>`设置`<matcher>`的名称，用于在求值表达式中引用该`<matcher>`
    - `<regex>`设置匹配条件

- `<onMatch>`配置符合过滤条件的操作

- `<onMismatch>`配置不符合过滤条件的操作

```xml
<!-- 过滤掉所有日志消息中不包含“billing”字符串的日志。 -->
<filter class="ch.qos.logback.core.filter.EvaluatorFilter">         
    <evaluator> <!-- 默认为 ch.qos.logback.classic.boolex.JaninoEventEvaluator -->   
        <expression>return message.contains("billing");</expression>   
    </evaluator>   
    <OnMatch>ACCEPT </OnMatch>  
    <OnMismatch>DENY</OnMismatch>  
</filter>  
```

```xml
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">   
    <filter class="ch.qos.logback.core.filter.EvaluatorFilter">   
        <evaluator>           
            <matcher>   
                <Name>odd</Name>   
                <!-- filter out odd numbered statements -->   
                <regex>statement [13579]</regex>   
            </matcher>   

            <expression>odd.matches(formattedMessage)</expression>   
        </evaluator>   
        <OnMismatch>NEUTRAL</OnMismatch>   
        <OnMatch>DENY</OnMatch>   
    </filter>   
    <encoder>   
        <pattern>%-4relative [%thread] %-5level %logger - %msg%n</pattern>   
    </encoder>   
</appender>   
```





### 1.3.模块分类

- ` logback-core`:其它两个模块的基础模块
- `logback-classic`:是log4j的一个改良版本，且完整实现了SLf4j API
- `logback-access`:与Servlet容器集成提供通过Http来访问日志的功能



### 1.4.优点

- logback比log4j要快10倍左右，且消耗的更小的内存
- logback-classic模块实现了Slf4j接口，能快速切换
- logback不仅支持xml格式的配置文件，还支持groovy格式的配置文件
- logback-classic能够检测到配置文件的更新，并且自动重新加载配置文件
- logback能够优雅的从I/O异常中恢复，从而我们不用重新启动应用程序来恢复logger
- logback能够根据配置文件中设置的上限值，自动删除旧的日志文件
- logback能够自动压缩日志文件
- logback能够在配置文件中加入条件判断（if-then-else)，从而配置不同环境（dev,test,prod...）的定制化配置
- logback提供了很多的filter
- logback-access和Jetty、Tomcat集成提供了功能强大的HTTP-access日志