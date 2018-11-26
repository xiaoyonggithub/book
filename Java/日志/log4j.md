## 一、组件的组成

-  诞生于1996年，属于Apache，是最早的日志框架
- 基本jar包有 log4j-1.2.17.jar

### 1.1.`Logger`

`looger`负责客户端代码的调用

![NOPLogger](E:\typora\images\NOPLogger.png)

> `Logger`的子类

- `RootLogger`：默认的Logger类
- `NOPLogger`：针对日志的数据不做任何的操作，直接丢弃
- `RootCategory`：已过时，使用RootLogger替代

- `Log4JLogger`：

> `Logger`的日志级别（优先级）

- `debug`
- `info`
- `wran`
- `error`
- `fatal`
- 两个特殊的日志级别
  - `all`:所有日志数据，不论级别
  - `off`:所有日志都不输出，与`all`相反



### 1.2.`Appender`

`Appender`负责日志的输出目的地，`Log4J`实现了多种输出方式，可以向控制台输出、向文件输出，向`socket`输出日志等。

- `JDBCAppender`:通过JDBC向数据库输出日志信息
- `FileAppender`:向文件输出日志信息，只产生一个日志文件
  - `Threshold = DEBUG`:设置日志输出的最大级别
  - `ImmediateFlush = TRUE`:默认为`true`，所有信息会被立即输出
  - `File = D:\log4j.log`:设置日志信息输出的文件
  - `Append = FALSE`:默认值为`true`，日志信息追加到文件中；`false`覆盖文件内容
  - `Encoding = UTF-8`:指定文件的编码
- `RollingFileAppender`:日志文件达到指定大小时，产生新的日志文件，可指定最大备份日志数量
  - `Threshold = DEBUG`:设置日志输出的最大级别
  - `ImmediateFlush = TRUE`:默认为`true`，所有信息会被立即输出
  - `File = D:\log4j.log`:设置日志信息输出的文件
  - `Append = FALSE`:默认值为`true`，日志信息追加到文件中；`false`覆盖文件内容
  - `Encoding = UTF-8`:指定文件的编码
  - `MaxFileSize = 100KB`:设置日志文件到达该大小时,将会自动滚动
  - `MaxBackupIndex = 2`:指定可以产生的滚动文件的最大数
- `DailyRollingFileAppender`:可以指定每月、每周、每天、每小时滚动生成日志文件
  - `Threshold = DEBUG`:设置日志输出的最大级别
  - `ImmediateFlush = TRUE`:默认为`true`，所有信息会被立即输出
  - `File = D:\log4j.log`:设置日志信息输出的文件
  - `Append = FALSE`:默认值为`true`，日志信息追加到文件中；`false`覆盖文件内容
  - `DatePattern`:设置文件滚动的频率
    - `yyyy-ww`	:每周滚动一次文件,即每周产生一个新的文件
    - `yyyy-MM`:每月
    - `yyyy-MM-dd`:每天
    - `yyyy-MM-dd-a`:每天两次
    - `yyyy-MM-dd-HH`:每小时
    - `yyyy-MM-dd-HH-mm`:每分钟
  - `Encoding = UTF-8`:指定文件的编码
- `ConsoleAppender `:向控制台输出日志
  - `Threshold = DEBUG`:设置日志输出的最大级别
  - `ImmediateFlush = TRUE`:默认为`true`，所有信息会被立即输出
  - `Target = System.err`:设置输出的方式，默认为`Sysout.out`
- `TelnetAppender `:通过`telnet`向远程输出日志信息

- `SocketAppender`:通过`socket`向远程输出日志信息
- `JMSAppender `:通过`jms`向远程输出日志信息
- `AsyncAppender `:异步输出日志
- `SMTPAppender `:通过`smtp`向远程输出日志信息
- `WriterAppender`:将日志信息通过流格式发送到任意指定位置

![1539584088923](E:\typora\images\1539584088923.png)

### 1.3.`Layout`

`Layout`设置日志输出的格式

![1539590829191](E:\typora\images\1539590829191.png)

- `HTMLLayout `:以`HTML`格式的日志信息
  - `LocationInfo = true`:输出文件名称和行号，默认值`false`
  - `Title`:标题，默认值Log4J Log Messages 
- `XMLLayout `:以`XML`格式的日志信息
  - `LocationInfo = true`:输出文件名称和行号，默认值`false`
- `PatternLayout `:最灵活的格式化方式，可以使用各项参数组合
  - `ConversionPattern = %m%n`:格式化指定的消息
- `SimpleLayout `:格式化结果包含日志信息的级别和信息字符串
- `TTCCLayout `:包含日志产生的时间、线程、类别等等信息

| 参数 | 作用                                                         |
| :--- | ------------------------------------------------------------ |
| `%c` | logger名称空间的全称，                                       |
| `%C` | 调用logger的类的全名（包含包路径）                           |
| `%d` | 日志记录时间，默认格式`ISO8601`，可指定格式`%d{yyy MMM dd HH:mm:ss,SSS}` |
| `%F` | 调用logger的源文件名                                         |
| `%l` | 日志事件发生的位置，包含类目名、发生的线程，以及在代码中的行数 |
| `%L` | 调用logger的代码行                                           |
| `%m` | 输出信息                                                     |
| `%M` | 调用logger的方法名                                           |
| `%n` | 当前平台下的换行符，Windows平台为`/r/n`，Unix平台为`/n`      |
| `%p` | 该条日志的优先级                                             |
| `%r` | 执行时间，从程序启动时到该条日志记录时经过的毫秒数           |
| `%t` | 产生该日志事件的线程名                                       |
| `%x` | 按`NDC（Nested Diagnostic Context，线程堆栈）`顺序输出日志   |
| `%X` | 按`MDC（Mapped Diagnostic Context，线程映射表）`输出日志<br>通常用于多个客户端连接同一台服务器，方便服务器区分是那个客户端访问留下来的日志 |
| `%%` | 显示一个百分号                                               |

> 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式
>
> - `%5c`: 输出category名称，最小宽度是5，category<5，默认的情况下右对齐
> - `%-5c`:输出category名称，最小宽度是5，category<5，"-"号指定左对齐,会有空格 
> - `%.5c`:输出category名称，最大宽度是5，category>5，就会将左边多出的字符截掉，<5不会有空格 
> - `%20.30c`:category名称<20补空格，并且右对齐，>30字符，就从左边交远销出的字符截掉 



## 二、`Log4J`的配置文件

`Log4j`的配置文件用来设置记录器的级别、存放器和布局，可使用`key=value`格式的设置或`xml`格式设置信息。

### 2.1.`properties`配置

```properties
#配置根Logger，level是日志记录的优先级；appenderName是指定日志信息输出位置
log4j.rootLogger = [level],appenderName,appenderName2,...
#配置日志信息输出位置Appender
log4j.appender.appenderName = fully.qualified.name.of.appender.class 
log4j.appender.appenderName.optionN = valueN
#配置日志信息的格式(布局)
log4j.appender.appenderName.layout = fully.qualified.name.of.layout.class 
log4j.appender.appenderName.layout.optionN = valueN
#为特定的包指定日志级别
log4j.logger.org.springframework=DEBUG
```

```properties
#设置日志输出的级别和输出目的地
log4j.rootLogger =ALL,systemOut,logFile,logDailyFile,logRollingFile,logMail,logDB 
 
#输出到控制台 
log4j.appender.systemOut = org.apache.log4j.ConsoleAppender 
log4j.appender.systemOut.layout = org.apache.log4j.PatternLayout 
log4j.appender.systemOut.layout.ConversionPattern = [%-5p][%-22d{yyyy/MM/dd HH:mm:ssS}][%l]%n%m%n 
log4j.appender.systemOut.Threshold = DEBUG 
log4j.appender.systemOut.ImmediateFlush = TRUE 
log4j.appender.systemOut.Target = System.out 
 
#输出到文件 
log4j.appender.logFile = org.apache.log4j.FileAppender 
log4j.appender.logFile.layout = org.apache.log4j.PatternLayout 
log4j.appender.logFile.layout.ConversionPattern = [%-5p][%-22d{yyyy/MM/dd HH:mm:ssS}][%l]%n%m%n 
log4j.appender.logFile.Threshold = DEBUG 
log4j.appender.logFile.ImmediateFlush = TRUE 
log4j.appender.logFile.Append = TRUE 
log4j.appender.logFile.File = ../Struts2/WebRoot/log/File/log4j_Struts.log 
log4j.appender.logFile.Encoding = UTF-8 
 
#按DatePattern输出到文件 
log4j.appender.logDailyFile = org.apache.log4j.DailyRollingFileAppender 
log4j.appender.logDailyFile.layout = org.apache.log4j.PatternLayout 
log4j.appender.logDailyFile.layout.ConversionPattern = [%-5p][%-22d{yyyy/MM/dd HH:mm:ssS}][%l]%n%m%n 
log4j.appender.logDailyFile.Threshold = DEBUG 
log4j.appender.logDailyFile.ImmediateFlush = TRUE 
log4j.appender.logDailyFile.Append = TRUE 
log4j.appender.logDailyFile.File = ../Struts2/WebRoot/log/DailyFile/log4j_Struts 
log4j.appender.logDailyFile.DatePattern = '.'yyyy-MM-dd-HH-mm'.log' 
log4j.appender.logDailyFile.Encoding = UTF-8 
 
#设定文件大小输出到文件 
log4j.appender.logRollingFile = org.apache.log4j.RollingFileAppender 
log4j.appender.logRollingFile.layout = org.apache.log4j.PatternLayout 
log4j.appender.logRollingFile.layout.ConversionPattern = [%-5p][%-22d{yyyy/MM/dd HH:mm:ssS}][%l]%n%m%n 
log4j.appender.logRollingFile.Threshold = DEBUG 
log4j.appender.logRollingFile.ImmediateFlush = TRUE 
log4j.appender.logRollingFile.Append = TRUE 
log4j.appender.logRollingFile.File = ../Struts2/WebRoot/log/RollingFile/log4j_Struts.log 
log4j.appender.logRollingFile.MaxFileSize = 1MB 
log4j.appender.logRollingFile.MaxBackupIndex = 10 
log4j.appender.logRollingFile.Encoding = UTF-8 
 
#用Email发送日志 
log4j.appender.logMail = org.apache.log4j.net.SMTPAppender 
#发送邮件的格式
log4j.appender.logMail.layout = org.apache.log4j.HTMLLayout 
log4j.appender.logMail.layout.LocationInfo = TRUE 
log4j.appender.logMail.layout.Title = Struts2 Mail LogFile 
#日志的错误级别
log4j.appender.logMail.Threshold = DEBUG 
log4j.appender.logMail.SMTPDebug = FALSE 
#邮件协议
log4j.appender.logMail.SMTPHost = SMTP.163.com 
#邮件端口
log4j.appender.logMail.SMTPPort=587
log4j.appender.logMail.From = xly3000@163.com 
log4j.appender.logMail.To = xly3000@gmail.com 
#log4j.appender.logMail.Cc = xly3000@gmail.com 
#log4j.appender.logMail.Bcc = xly3000@gmail.com 
log4j.appender.logMail.SMTPUsername = xly3000 
#这里一定要去邮箱设置里面修改一下设置,打开AMTP服务！！！！！
log4j.appender.logMail.SMTPPassword = 1234567 
#邮件主题
log4j.appender.logMail.Subject = Log4j Log Messages 
#log4j.appender.logMail.BufferSize = 1024 
#log4j.appender.logMail.SMTPAuth = TRUE 
 
#将日志登录到MySQL数据库 
log4j.appender.logDB = org.apache.log4j.jdbc.JDBCAppender 
log4j.appender.logDB.layout = org.apache.log4j.PatternLayout 
log4j.appender.logDB.Driver = com.mysql.jdbc.Driver 
log4j.appender.logDB.URL = jdbc:mysql://127.0.0.1:3306/xly 
log4j.appender.logDB.User = root 
log4j.appender.logDB.Password = 123456 
log4j.appender.logDB.Sql = INSERT INTOT_log4j(project_name,create_date,level,category,file_name,thread_name,line,all_category,message)values('Struts2','%d{yyyy-MM-ddHH:mm:ss}','%p','%c','%F','%t','%L','%l','%m')
```

```java
//获取日志记录器
Logger logger = Logger.getLogger(this.getClass());
//使用默认的配置文件
BasicConfigurator.configure();
//设置日志的级别，会覆盖配置文件中设置的级别
logger.setLevel(Level.INFO);
//日志输出
logger.info("info:记录日志");
```

```java
BasicConfigurator.configure ();// 自动快速地使用缺省Log4j环境。 
PropertyConfigurator.configure ( String configFilename);//读取使用Java的特性文件编写的配置文件。 
DOMConfigurator.configure ( String filename );//读取XML形式的配置文件
```

### 2.2.`xml`配置





## 三、`log4j`与`Slf4j`的整合

1. 引入的jar

```xml
<!--log4j与slf4j的整合jar-->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.21</version>
</dependency>
```

2. 修改loj4j中原来的日志

```java
final Logger log = Logger.getLogger(Test.class);
log.info("hello this is log4j info log");
```

将其改为下面的方式

```java
Logger log = LoggerFactory.getLogger(Test.class);
log.info("hello, my name is {}", "chengyi");
```

