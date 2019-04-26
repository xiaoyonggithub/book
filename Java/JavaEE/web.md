# 一、`Servlet3.0`

- `Servlet3.0`需要`Tomcat7`及其以上的版本才支持

```xml
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
```

## 1.1.`Servlet 3.0`新特性

- 异步处理支持:有了该特性，Servlet线程不再需要一直阻塞，直到业务处理完毕才能再输出响应，最后才结束该Servlet线程。在接收到请求之后，Servlet线程可以将耗时的操作委派给另一个线程来完成，自己在不生成响应的情况下返回至容器。针对业务处理较耗时的情况，这将大大减少服务器资源的占用，并且提高并发处理速度。
- 新增的注解支持:新增了若干注解，用于简化Servlet、过滤器（Filter）和监听器（Listener）的声明，这使得web.xml部署描述文件从该版本开始不再是必选的了。
- 可插性支持:

### 1.1.1.异步处理

- Servlet 3.0之前处理流程：
  - Servlet接收到请求之后，可能需要对请求携带的数据进行一些预处理；
  - 调用业务接口的某些方法，以完成业务处理；
  - 根据处理的结果提交响应，Servlet线程结束。
- Servlet 3.0处理流程：
  - Servlet接收到请求之后，可能首先需要对请求携带的数据进行一些预处理；
  - Servlet线程将请求转交给一个异步线程来执行业务处理，线程本身返回至容器，此时Servlet还没有生成响应数据，异步线程处理完业务以后，可以直接生成响应数据（异步线程拥有ServletRequest和 ServletResponse对象的引用），或者将请求继续转发给其它Servlet；
  - Servlet线程不再是一直处于阻塞状态以等待业务逻辑的处理，而是启动异步线程之后可以立即返回。

- 启用异步处理支持

  `async-supported`配置是否开启异步处理，默认不开启

```xml
<servlet> 
    <servlet-name>DemoServlet</servlet-name> 
    <servlet-class>footmark.servlet.DemoServlet</servlet-class> 
    <async-supported>true</async-supported> 
</servlet>
```

```java
@WebFilter(urlPatterns = "/demo",asyncSupported = true) 
public class DemoFilter implements Filter{...}
```

- 异步处理的一些监听器
  - 异步线程开始时，调用`AsyncListener`的`onStartAsync(AsyncEvent event)`方法；
  - 异步线程出错时，调用`AsyncListener`的`onError(AsyncEvent event)`方法；
  - 异步线程执行超时，则调用`AsyncListener`的`onTimeout(AsyncEvent event`)方法；
  - 异步执行完毕时，调用`AsyncListener`的`onComplete(AsyncEvent event)`方法；

## 1.2.注解

`Servlet 3.0`的部署描述文件`web.xml`的顶层标签`<web-app>`有一个`metadata-complete`属性，该属性指定当前的部署描述文件是否是完全的。

- `metadata-complet=true`时，容器在部署时将只依赖部署描述文件，忽略所有的注解（同时也会跳过`web-fragment.xml`的扫描，亦即禁用可插性支持。
- `metadata-complet=false`时，表示启用注解支持。

```xml

```



### 1.2.1.`@WebServlet`

`@WebServlet`用于将一个类声明为`Servlet`，该注解将会在部署时被容器处理，容器将根据具体的属性配置将相应的类部署为`Servlet`。

- `name`:等同于`<servlet-name>`；若没有显示指定，则为类的全限定名。
- `value`:等价于`urlPatterns `属性，不能同时使用。
- `urlPatterns`:指定一组`Servlet`的`URL`匹配模式；等同于`<url-pattern>`。
- `loadOnStartup`:设置`Servlet`的加载顺序，等同于`<load-on-startup>`。
- `initParams`:设置`Servlet`的初始化参数，等同于`<init-param>`。
- `asyncSupported`:设置是否支持异步加载，等同于`<async-supported>`。
- `description`:设置`Servlet`的描述信息，等同于`<description>`。
- `displayName`:设置`Servlet`的显示名，等同于`<display-name>`。

```java
@WebServlet(urlPatterns = {"/simple"}, asyncSupported = true, 
loadOnStartup = -1, name = "SimpleServlet", displayName = "ss", 
initParams = {@WebInitParam(name = "username", value = "tom")} 
) 
public class SimpleServlet extends HttpServlet{ … }
```

### 1.2.2.`@WebInitParam`

`@WebInitParam`不单独使用，一般配合`@WebServlet`或`@WebFilter`使用，作用是为`Servlet`或`Filter`指定初始化参数。

- `name`:指定参数的名称，等同于`<param-name>`。
- `value`:指定参数的值，等同于`<param-value>`。
- `description`:设置参数的描述信息，等同于`<description>`。

### 1.2.3.`@WebFilter`

`@WebFilter`声明一个类为过滤器。

- `filterName`:指定过滤器的`name`属性，等同于`<filter-name>`。
- `value`:等同于`urlPatterns`属性，两者不能同时设置。
- `urlPatterns`:设置过滤器的`URL`匹配模式，等价于`<url-pattern>`。
- `servletNames`:指定过滤器应用于那些`Servlet`，取值是`@WebServlet`中的`name`属性的取值，或者是 `web.xml`中`<servlet-name>`的取值。

  



[1]: https://www.ibm.com/developerworks/cn/java/j-lo-servlet30/index.html

[^1]: https://www.ibm.com/developerworks/cn/java/j-lo-servlet30/index.html



# 二、内置对象

## 2.1.`JSP`的内置对象

内置对象必须由支持JSP的容器去创建

- `appliction`：将信息保存在服务器中，直到服务器关闭，`application`保存的信息在整个应用中有效；类似于系统的全局变量。

```java
//获取当前应用的路径
application.getContextPath();
```

- `session`：有服务器自动创建与用户请求相关的对象，服务器为每一个用户生成一个`session`对象，用于保存用户信息和跟踪用户的操作状态；`session`内部使用`Map`保存数据，`value`值可以是复杂对象类型。
- `request`:表示客户端的请求信息，用于接收通过`HTTP`协议传输到服务器的数据（包含头信息，系统信息，请求方式，请求参数等）；作用域为一次请求。

- `response`:表示对客户端的响应信息，是将`JSP`容器处理后的对象返回给客户端；作用域是`JSP`页面内有效。
- `out`：用来向客户端（浏览器）输出信息，并管理应用服务器上的输出缓冲区；

```java
out.write();
out.println();
out.print();
```

- `config`：获取服务器的配置信息，

```java
//通过pageConext获取config
ServletConfig config = pageContext.getServletConfig();
```

- `pageContext`：获取任何范围参数，可以获取`JSP`页面中的`out、request、reponse、session、application`等对象，它的创建和初始化有容器完成。

- `page`：表示`JSP`本身，
- `exception`：显示异常信息，只有`isErrorPage="true"`的页面中才可以使用。



## 2.2.`appliction、session、request`的区别









# 三、请求方式

## 3.1.`POST`与`GET`区别

`GET`与`POST`没有本质的区别，都是基于`HTTP`协议中的请求方法，底层实现都是基于`TCP/IP`协议；所谓的区别都是浏览器厂商根据约定设置

- `GET`请求的参数放在`URL`中，`POST`请求参数放在请求`body`中
- `GET`请求的`URL`传参有长度限制，而`POST`传参没有长度限制
- `GET`请求参数只能是`ASCII`码，所以中文需要URL编码；`POST`传参没有这个限制

`HTTP`请求的八种方法：

- `OPTIONS`：返回服务器所支持的请求方法
- `GET`:向服务器获取指定资源
- `HEAD `:向服务器获取指定资源，响应体不返回，只返回响应头
- `POST `:向服务器提交数据，数据放在请求体里
- `PUT `:向服务器提交数据，数据放在请求体里，具有幂等性，一般用于更新
- `DELETE `:删除服务器指定资源
- `TRACE `:回显服务器接收到的请求，测试的时候回用到这个
- `CONNECT `:预留，暂时不使用

## 3.2.`RESTful API`

`Representational State Transfe(REST)`客户端和服务端的交互形式，而符合这种交互形式的接口设计，就被叫做`RESTful API`，这个风格的特点：

- 使用名词而不使用动词，如`/getStudent、/searchStudents`改为`/students`
- `GET`用于查询，`PUT、POST、DELETE`用于修改
- 使用名词复数而不是使用单数
- 在`HTTP`请求的`head`体里定义序列化类型，如`Content-Type:application/json`
- 请求的集合应设定号过滤条件、排序、字段、分页
- 接口要版本化
- 要有`HTTP`状态
- 允许重写`HTTP`的方法

## 3.3.`HTTP`的状态

- `200`：返回正常
- `304`: 服务端资源无变化，可使用缓存资源
- `400`: 请求参数不合法
- `401 `: 未认证
- `403`: 服务器端禁止访问该资源
- `404 `: 服务器端为找到该资源
- `500 `: 服务器端异常





