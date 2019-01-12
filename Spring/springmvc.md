# 一、构建springmvc项目

## 1.1.依赖的jar

```xml

```

## 1.2.配置springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/mvc 
                           http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
					     http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring-beans.xsd
						 http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring-context-4.0.xsd">

    <!--SpringMVC只是控制网站跳转逻辑  -->
    <!-- 只扫描控制器 -->
    <context:component-scan base-package="com.xy.controller"></context:component-scan>

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>

    <!--开启mvc注解-->
    <mvc:annotation-driven></mvc:annotation-driven>
    
    <!--处理静态资源 -->
    <mvc:default-servlet-handler/>
</beans>

```

## 1.3.配置web.xml

```xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                      http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name>SSM</display-name>

    <!--Spring配置： needed for ContextLoaderListener -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <!--配置上下文监听-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!--SpringMVC转发器-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--配置springmvc配置文件位置，默认位置WEB-INF/springmvc-servlet.xml
		   默认的配置文件：/WEB-INF/<servlet-name>-servlet.xml
		   此处的默认配置文件：/WEB-INF/springmvc-servlet.xml-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--SpringMVC处理的资源-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    
    <!--配置过滤器HiddenHttpMethodFilter,处理将POST请求转化为DELET请求和PUT请求-->
    <filter>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
</web-app>
```





# 二、常用注解

## 2.1.`@RequestMapping`

`@RequestMapping`可以设置在方法和类上，设置请求映射的路径;

* 通过URL的方式映射请求

```java
@RequestMapping("/userController")
@Controller
public class UserController{ 
    @RequestMapping(/get)
    public User get(){
        
    }
}
```

* 通过请求头、请求参数、请求方法的方式映射请求
  * `method = RequestMethod.POST`:设置请求方式
  * `params = {},headers = {}`设置请求参数和请求头，支持简单的表达式
    * `param1`:表示请求必须包含param1参数
    * `!param1`:表示请求不能包含param1参数
    * `param1!=value1`:表示请求中参数param1的值不能等于value1
    * `{"param1=value1","param2"}`:表示请求中必须包含参数param1和param2,且参数param1的值必须是value1

```java
//method 设置请求方式
@RequestMapping(value = "/getListEmp",method = RequestMethod.POST)
public String getListEmp(){

    return "list";
}
```

```java
//params设置请求参数，必须包含参数username和password，且password的值不能等于1234
//headers设置请求头
@RequestMapping(value = "login",params = {"username","password!=1234"},headers = {"Accept-Language=zh_CN,zh,q=0.8"})
public String login(){

    return "";
}
```

* @RequestMapping支持Ant风格的通配符
  * `[*]`:匹配任意字符
  * `[?]`:匹配一个字符
  * `[**]`:匹配多层路径

```java
@RequestMapping("/*/login")
public String login(){
    return "";
}
```

```html
<a href="admin/login">登录</a>
```

## 2.2.`@PathVariable`

`@PathVariable`映射URL绑定的占位符到目标方法的参数中，是Spring3.0后新增的功能

```java
@RequestMapping("/login/{username}")
public String login(@PathVariable("username") String username,Model model){
    model.addAttribute("username",username);
    return "main";
}
```

```html
<a href="${pageContext.request.contextPath}/login/zhangsan">登录</a>
```

## 2.3.`@RequestParam`

@RequestParam请求参数的绑定

- `required`设置参数是否必须 
- `defaultValue`设置请求参数默认值

```html\
<a href="${pageContext.request.contextPath}/login?username=zhangsan&password=1234">登录</a>
```

```java
//required设置参数是否必须
@RequestMapping("/login")
public String login(@RequestParam("username") String username,@RequestParam(value = "password",required = false) String password, Model model){
    model.addAttribute("username",username);
    model.addAttribute("password",password);
    return "main";
}
```

```java
//若参数为基本数据类型，不传值是需设置默认值，或修改为包装类型
@RequestMapping("/register")
public String register(@RequestParam(value = "password",required = false,defaultValue = "0") int age, Model model){
    model.addAttribute("age",age);
    return "main";
}
```

## 2.4.`@RequestHeader`

@RequestHeader设置请求头参数

- `required`设置参数是否必须 
- `defaultValue`设置请求参数默认值

```html
<a href="${pageContext.request.contextPath}/register?Accept-Language = zh_CN">注册</a>
```

```java
//@RequestHeader 设置请求头参数 Accept-Language: zh-CN,zh;q=0.9
@RequestMapping("/register")
public String register(@RequestHeader("Accept-Language") String al, Model model){
    model.addAttribute("al",al);
    return "main";
}
```

## 2.5.`@CookieValue`

`@CookieValue`绑定`Cookie`值

```html
<a href="${pageContext.request.contextPath}/register">注册</a>
```

```java
//@CookieValue 映射cookie值
@RequestMapping("/register")
public String register(@CookieValue("JSESSIONID") String sessionId, Model model){
    model.addAttribute("sessionId",sessionId);
    return "main";
}
```

## 2.6.`@SessionAttribute`



## 2.7.`@SessionAttributes`

@SessionAttributes 将值放在session域中，即可以通过属性名指定放入的属性(value)，也可以通过模型属性的对象类型指定放入的属性(type),只能作用在类上面

```java
@Controller
//@SessionAttributes 将值放在session域中，即可以通过属性名指定放入的属性，也可以通过模型属性的对象类型指定放入的属性
@SessionAttributes(value = {"user"},types = {String.class})
public class EmpController {

    @RequestMapping("/register")
    public String register(Map<String,Object> map){
        User user = new User("zhangsan","1234","zhangsna@yinhai.com");
        map.put("user",user);
        map.put("school","sichaundaxue");
        return "main";
    }
}
```

## 2.8.`@ModelAttribute`

@ModelAttribute可以修改方法；也可以修饰目标方法的POJO类型的入参，value指定查找的key

```java
//@ModelAttribute 标记的方法，会在每个目标方法之前执行
@ModelAttribute
public void getUser(@RequestParam("id") String id,Map<String,Object> map){
    if(id != null){
        User user = new User(1,"zhangsan","12345","zhangsan@yinhai.com");
        System.out.println("先从数据库获取值:"+user);
        //注意：若没有在入参中标注@ModelAttribute,键应该是目标方法入参类型的首字母小写	
        //若标注了@ModelAttribute，则key为@ModelAttribute中value值
        map.put("user",user);
    }
}

@RequestMapping("/updateUser")
public String updateUser(@ModelAttribute("user") User user){
    System.out.println("修改信息:"+user);
    return "main";
}
```

## 2.8.1.运行流程：

1. 执行@ModelAttribute标注的方法，从数据库中取出对象，将值放入到Map中（键：user）
2. SpringMVC 从Map值取出User对象,并把表单的请求参数赋值给该User对象的对应属性
3. SpringMVC 把上述的对象传入目标对象

效果：修改数据时，没有传入需要修改的值时，默认取数据库中原来的值

## 2.8.2.源码分析





## 2.8.3.SpringMVC确定目标方法POJO类型参数的过程

1. 确定一个key
   * 若目标方法POJO类型参数没有使用@ModelAttribute修饰，则key为POJO类名首字母小写
   * 若目标方法POJO类型参数使用@ModelAttribute修饰，则key为@ModelAttribute注解的value值
2. 在implicitModel中查找key对应的对象，若存在，就作为入参传入
3. 在implicitModel中不存在key对应的对象，则检查当前Handler是否标注了@SessionAttributes注解
   * 若标注了@SessionAttributes注解，则在该注解的value中查找是否包含key
     * 若存在就从HttpSession中取出key对应的值，传入目标方法的入参
     * 若key取出的值不存在就抛出异常
   * 若没有标注@SessionAttributes注解或@SessionAttributes注解的value中不包含key,则通过反射创建POJO类型的参数，传入目标方法的入参
4. SpringMVC 会将key和value（POJO类型的对象）保存到implicitModel中，进而保存到request中。

## 2.9.`@InitBinder`

`@InitBinder`标注在方法上，对WebDataBinder进行初始化

* WebDataBinder是DataBinder的子类，作用是完成单个字段到JavaBean属性的绑定
* initBinder()方法的没有返回值
* initBinder()方法的参数一般是WebDataBinder

```java
//在保存之前，设置不绑定ename的值
@InitBinder
public void initBinder(WebDataBinder binder){
    //设置某个字段不赋值
    binder.setDisallowedFields("ename");
    //用于自定义设置参数的绑定
}
```

## 2.10.`@NumberFormat`

`NumberFormatAnnotationFormatterFactory`支持`@NumberFormat`



## 2.11.`@DateTimeFormat`

`JodaDateTimeFormatAnnotationFormatterFactory`支持`@DateTimeFormat`



# 三、`Rest`

* `GET`:获取资源
* `POST`:新建资源
* `PUT`:更新资源
* `DELETE`:删除资源

## 3.1.POST请求转REST风格的请求

由于浏览器的form表单只支持GTE和POST请求，`Hidd方法enHttpMethodFilter`支持这些请求转换为标准的http方法，从而支持`GET,POST,PUT,DELETE`请求

```xml
<!--配置过滤器HiddenHttpMethodFilter-->
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

```html

```

## 3.2.处理静态资源

问题：

   优雅的Rest风格的URL不希望带.html或.do等后缀，若将DispathcerServlet请求映射配置为`/`,SpringMVC会拦截Web容器所有的请求（包括静态资源的请求），SpringMVC将其当做一个普通的请求处理，因此找不到对应的处理器处理，故找不到资源。

解决方案：

```xml
<!-- 处理静态资源 -->
<mvc:default-servlet-handler/>
<!-- 其他请求可用-->
<mvc:annotation-driven></mvc:annotation-driven>
```

`<mvc:default-servlet-handler/>`将在SpringMVC的上下文定义一个DefaultServletHttpRequestHandler,它会对进入DispatcherServlet的请求进行筛选;

* 若发现没有经过映射请求，就将改请求交有Web应用服务器默认的Servlet处理
* 若不是静态资源的请求，才由DispatcherServlet继续处理

一般Web应用服务器默认的Servlet的名称时default,若Web应用服务器默认的Servlet不是default，可使用default-servlet-name修改

```xml
<mvc:default-servlet-handler default-servlet-name="default"/>
```

## 3.3.REST风格的CRUD

1.springmvc的配置文件springnvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">

    <!--SpringMVC只是控制网站跳转逻辑  -->
    <!-- 只扫描控制器 -->
    <context:component-scan base-package="com.xy.controller"></context:component-scan>

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>

    <!--配置BeanNameViewResolver视图解析：根据视图名称解析视图-->
    <bean id="beanNameViewResolver" class="org.springframework.web.servlet.view.BeanNameViewResolver">
        <!--设置视图的优先级，值越小优先级越高；常用的视图解析器的优先级低，即其他视图解析器解析不了，再使用常用的视图解析器解析-->
        <property name="order" value="100"></property>
    </bean>

    <!-- 处理静态资源 -->
    <mvc:default-servlet-handler/>
    <!-- 其他请求可用-->
    <mvc:annotation-driven></mvc:annotation-driven>
    
</beans>
```

2.mapper层

```java
public interface EmpMapper extends BaseMapper<Emp> {

    //查询某一部门下的所有人员
    List<Emp> getByDeptName(RowBounds rowBounds, @Param("ew")Wrapper<Emp> wrapper);
}
```

3.service层

```java
public interface EmpService extends IService<Emp> {

}
```

```java
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

}
```

4.controller层

```java
@Controller
@RequestMapping("/emp")
public class EmpController {

    @Autowired
    private EmpService empService;

    @Autowired
    private DeptService deptService;

    //查询人员列表
    @RequestMapping(value = "/emps")
    public String list(Map<String,Object> map){
        List<Emp> empList = empService.selectList(null);
        map.put("emps",empList);
        return "list";
    }

    //进入新增页面
    @RequestMapping(value = "/emp",method = RequestMethod.GET)
    public String input(Map<String,Object> map){
        map.put("depts",deptService.selectList(null));
        map.put("emp",new Emp());
        map.put("emps",empService.selectList(null));
        return "input";
    }

    //保存信息
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    public String insert(Emp emp){
        boolean insert = empService.insert(emp);
        return "redirect:emps";
    }

    //进入修改页面
    @RequestMapping(value = "/emp/{empno}",method = RequestMethod.GET)
    public String input(@PathVariable("empno") Integer empno, Map<String,Object> map){
        Emp emp = empService.selectById(empno);
        map.put("emp",emp);
        map.put("depts",deptService.selectList(null));
        map.put("emps",empService.selectList(null));
        return "input";
    }

    //修改前获取数据库的数据
    @ModelAttribute
    public void get(@RequestParam(value = "empno",required = false) Integer empno, Map<String,Object> map){
        if(empno != null){
            map.put("emp",empService.selectById(empno));
        }
    }

    //修改信息
    @RequestMapping(value = "/emp",method = RequestMethod.PUT)
    public String update(Emp emp){
        empService.updateById(emp);
        return "redirect:emps";
    }

    //删除信息
    @RequestMapping(value = "/emp/{empno}",method = RequestMethod.DELETE)
    public String delete(@PathVariable("empno") BigDecimal empno){
        boolean delete = empService.deleteById(empno);
        return "redirect:/emp/emps";
        //return "redirect:emps";
    }

}
```

5. 页面list.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>员工列表</title>
    <%--引入静态资源文件--%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.1.1.js"></script>
    <script type="text/javascript">
        //删除的post请求转DELETE请求
        $(function(){
            $(".delete").click(function(){
                var href = $(this).attr("href");
                $("form").attr("action", href).submit();
                //阻止a标签的href的默认行为
                return false;
            });
        })
    </script>
</head>
<body>
    
    <form:form action="" method="post">
        <%--注意：对于 _method 不能使用 form:hidden 标签, 因为 modelAttribute 对应的 bean 中没有 _method 这个属性--%>
        <input type="hidden" name="_method" value="DELETE">
    </form:form>

    <table border="1">
        <tr>
            <th>序号</th>
            <th>empno</th>
            <th>ename</th>
            <th>job</th>
            <th>mgr</th>
            <th>hiredate</th>
            <th>sal</th>
            <th>comm</th>
            <th>deptno</th>
            <th>修改</th>
            <th>删除</th>
        </tr>
        <c:forEach items="${requestScope.emps}" var="emp" varStatus="status">
            <tr>
                <td>${status.index}</td>
                <td>${emp.empno}</td>
                <td>${emp.ename}</td>
                <td>${emp.job}</td>
                <td>${emp.mgr}</td>
                <td>${emp.hiredate}</td>
                <td>${emp.sal}</td>
                <td>${emp.comm}</td>
                <td>${emp.deptno}</td>
                <td><a href="${pageContext.request.contextPath}/emp/emp/${emp.empno}">修改</a></td>
                <td><a class="delete" href="${pageContext.request.contextPath}/emp/emp/${emp.empno}">删除</a></td>
            </tr>
        </c:forEach>
    </table>

<a href="${pageContext.request.contextPath}/emp/emp">新增</a>
</body>
</html>
```

6.页面input.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>员工新增</title>
</head>
<body>
    <form:form id="emp" action="${pageContext.request.contextPath}/emp/emp" method="post" modelAttribute="emp">
        <input type="hidden" name="_method" value="PUT">
        <c:if test="${emp.empno != null}">
            <form:hidden path="empno"/><br>
        </c:if>
        <c:if test="${emp.empno == null}">
            编号：<form:input path="empno"/>
        </c:if>
        <%--path 等同于html中input标签的path--%>
        姓名：<form:input path="ename"/><br>
        工作：<form:input path="job"/><br>
        上级：<form:select path="mgr" items="${emps}" itemLabel="ename" itemValue="empno"/><br>
        <%--入职时间：<form:input path="hiredate"/><br>--%>
        工资：<form:input path="sal"/><br>
        奖金：<form:input path="comm"/><br>
        部门：<form:select path="dept.deptno" items="${depts}" itemLabel="dname" itemValue="deptno"/><br>
        <input type="submit" value="提交"/>
    </form:form>
</body>
</html>
```

# 四、参数的绑定

## 4.1.`@RequestParam`绑定参数

```java
@RequestMapping("/login")
public String login(@RequestParam("username") String username,@RequestParam(value = "password",required = false) String password, Model model){
    model.addAttribute("username",username);
    model.addAttribute("password",password);
    return "main";
}
```

## 4.2.绑定POJO参数

- 请求参数与`POJO`属性名自动匹配，自动为对象填充属性值，支持级联属性

```html
<form action="${pageContext.request.contextPath}/register" method="post">
    用户名：<input type="text" name="username">
    密  码：<input type="password" name="password">
    邮  箱：<input type="email" name="email">
    <!--使用级联属性-->
    地  址(省)：<input type="text" name="address.province">
    地  址(市)：<input type="text" name="address.city">
    <input type="submit" value="注册">
</form>
```

```java
public class User {

    private String username;
    private String password;
    private String email;
    private Address address;
    ...
}
```

```java
public class Address {
    private String province;
    private String city;
    ...
}
```

```java
@RequestMapping("/register")
public String register(User user, Model model){
    model.addAttribute("user",user);
    return "main";
}
```

## 4.3.设置`SerlvetAPI`类型的参数

通过AnnotationMethodHandlerAdapter.resolveStandardArgument()进行解析，支持的SerlvetAPI类型有：

| 类型                      | 描述 |
| ------------------------- | ---- |
| `HttpServletRequest`      |      |
| `HttpSerlvetResponse`     |      |
| `HttpSession`             |      |
| `java.security.Principal` |      |
| `Local`                   |      |
| `InputStream`             |      |
| `OutputStream`            |      |
| `Reader`                  |      |
| `Writer`                  |      |

```html
<a href="${pageContext.request.contextPath}/register">注册</a>
```

```java
@RequestMapping("/register")
public String register(HttpServletRequest request, HttpServletResponse response, Model model){
    model.addAttribute("request",request);
    model.addAttribute("response",response);
    return "main";
}
```





# 五、模型数据的设置

## 5.1.`Model/Map`

实际上最后传入的是`org.springframework.validation.support.BindingAwareModelMap`，所以可以传入`Map`或`Model`类型

![1532879216001](E:\typora\images\1532879216001.png)

![1532879251689](E:\typora\images\1532879251689.png)

```java
@RequestMapping("/register")
public String register(User user,Map<String,Object> map){
    map.put("user",user);
    return "main";
}
```

```java
@RequestMapping("/register")
public String register(User user,Model model){
    model.addAttribute("user",user);
    return "main";
}
```

## 5.2.`ModelAndView`

-  `ModelAndView`既包含视图信息，也包含模型数据
- `ModelAndView`会把`Model`中的数据放入到`request`中

```java
@RequestMapping("/register")
public ModelAndView register(User user,HttpServletRequest request){
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("user",user);
    modelAndView.setViewName("main");
    return modelAndView;
}
```

## 5.3.`HttpServletRequest`

```java
@RequestMapping("/register")
public String register(User user,HttpServletRequest request){
    request.setAttribute("user",user);
    return "main";
}
```

## 5.4.`@SessionAttributes`

- 多个请求之间共享某个模型数据，可以使用`@SessionAttributes`

- `@SessionAttributes`可以通过属性名指定放入的属性(value)，也可以通过模型属性的对象类型指定放入的属性(type)，它会将满足的条件的值放入到`session`中
- 只能标注在类上面

```java
@Controller
//@SessionAttributes 将值放在session域中，即可以通过属性名指定放入的属性，也可以通过模型属性的对象类型指定放入的属性
@SessionAttributes(value = {"user"},types = {String.class})
public class EmpController {

    @RequestMapping("/register")
    public String register(Map<String,Object> map){
        User user = new User("zhangsan","1234","zhangsna@yinhai.com");
        map.put("user",user);
        map.put("school","sichaundaxue");
        return "main";
    }
}
```

## 5.5.`@ModelAttribute`

- `@ModelAttribute`入参标注该注解，入参对象就会放入数据模型中
- `@ModelAttribute`标记的方法会在每个目标方法执行前执行

```java
//修改前获取数据库的数据
@ModelAttribute
public void get(@RequestParam(value = "empno",required = false) Integer empno, Map<String,Object> map){
    if(empno != null){
        //此时Map的键要与目标方法入参类型首字母小写
        map.put("emp",empService.selectById(empno));
    }
}
//修改信息
@RequestMapping(value = "/emp",method = RequestMethod.PUT)
public String update(Emp emp){
    empService.updateById(emp);
    return "redirect:emps";
}
```

- `@ModelAttribute`可以修饰可以来修饰目标方法POJO 类型的入参, 其value属性值有如下的作用:
  1). SpringMVC会使用value属性值在implicitModel中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
  2). SpringMVC会value为key, POJO类型的对象为value, 存入到request中. 

```java
@ModelAttribute
public void get(@RequestParam(value = "empno",required = false) Integer empno, Map<String,Object> map){
    if(empno != null){
        //此时Map的键要与目标方法入参类型@ModelAttribute("user")设置的一致
        map.put("user",empService.selectById(empno));
    }
}
@RequestMapping(value = "/emp",method = RequestMethod.PUT)
public String update(@ModelAttribute("user") Emp emp){
    empService.updateById(emp);
    return "redirect:emps";
}
```

- 执行流程
  - 执行`@ModelAttribute`标注的方法，并将对象放入Map中
  - 从Map中取出emp对象，并将表单的目标参数赋值给emp对象
  - 将emp传入目标方法的参数

```java

```

### 5.5.1.源码分析

- 调用`@ModelAttribute`标注的方法，实际是把`Map`中的数据放入到`implicitModel`中 

- 解析请求处理器的目标参数，实际上该目标参数来自与`WebDataBinder`对象的`target`属性

  1. `创建`WebDataBinder`对象

  - 确定`objectName`属性
    - 若传入的`attrName`属性值为`""`，则`objectName`为类名首字母小写
    - 若目标目标方法的`POJO`属性使用`@ModelAttribute`修饰，则`attrName`值为`@ModelAttribute`的`value`属性值
  - 确定`target`属性
    - 首先在`implicitModel `中查找 `attrName `对应的属性值，若存在就ok
    - 若不存在，则验证当前 `Handler `是否使用了` @SessionAttributes `进行修饰，若使用了，则尝试从 `Session `中获取 `attrName `所对应的属性值，若 `session `中没有对应的属性值，则抛出了异常
    - 若 `Handler `没有使用` @SessionAttributes `进行修饰, 或` @SessionAttributes` 中没有使用 `value `值指定的 `key`和 `attrName `相匹配，则通过反射创建了 `POJO `对象

  2. `SpringMVC `把表单的请求参数赋给了 `WebDataBinder `的 `target `对应的属性
  3. `SpringMVC `会把 `WebDataBinder `的 `attrName `和 `target `给到 `implicitModel`，近而传`request `域对象中
  4. 把 `WebDataBinder `的 `target `作为参数传递给目标方法的入参

## 5.6.`SpringMVC `确定目标方法 `POJO `类型入参的过程

1. 确定一个key:
  1). 若目标方法的POJO类型的参数没有使用@ModelAttribute作为修饰， 则key为POJO类名第一个字母的小写
  2). 若使用了@ModelAttribute来修饰， 则key为@ModelAttribute注解的value属性值.
2. 在implicitModel中查找key对应的对象， 若存在， 则作为入参传入
   1). 若在@ModelAttribute标记的方法中在 Map 中保存过， 且key和`(1.确定一个 key)`确定的key一致， 则会获取到.
3. 若 implicitModel中不存在key对应的对象， 则检查当前的Handler是否使用@SessionAttributes 注解修饰，若使用了该注解， 且@SessionAttributes注解的value属性值中包含了key， 则会 HttpSession中来获取key所对应的 alue值， 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常.
4. 若Handler没有标识@SessionAttributes 注解或@SessionAttributes注解的value值中不包含 key， 则会通过反射来创建POJO类型的参数， 传入为目标方法的参数
5. SpringMVC会把key和POJO类型的对象保存到mplicitModel中， 进而会保存到request中. 

# 六、视图与视图解析器

- 请求方法执行完成后，最终返回一个`ModelAndView`对象。无论方法返回的是`String、View、ModelMap`等那种类型，`SpringMVC`内部都会将其装配成`ModelAndView`对象返回
- `SpringMVC`通过视图解析器`ViewResolver`等到最终的视图，视图可以是`JSP、Excel、JFreeChart`等各种视图
- 视图的作用是渲染模型数据，将模型中的数据以某种形式呈现给客户
- 视图对象有视图解析器负责实例化，由于视图没有状态，所有视图是线程安全的

## 6.1.配置直接转发

```xml
<!--配置直接转发的页面，无需在经过Handler-->
<mvc:view-controller path="/success" view-name="success"/>
<!--注意：配置了该映射方式，还需要@RequestMapping起作用,需要配置mvc:annotation-driven-->
<mvc:annotation-driven></mvc:annotation-driven>
```

## 6.2.自定义视图

```xml
<!-- 视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/pages/"></property>
    <property name="suffix" value=".jsp"></property>
</bean>

<!--配置BeanNameViewResolver视图解析：根据视图名称解析视图-->
<bean id="beanNameViewResolver" class="org.springframework.web.servlet.view.BeanNameViewResolver">
    <!--设置视图的优先级，值越小优先级越高；常用的视图解析器的优先级低，即其他视图解析器解析不了，再使用常用的视图解析器解析-->
    <property name="order" value="100"></property>
</bean>
```

```java
@Component
public class HelloView implements View{

    /**
     * 设置返回的文件类型
     * @return
     */
    @Override
    public String getContentType() {
        return "text/html";
    }

    /**
     * 渲染视图
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().print("hello view, time: " + new Date());
    }
}
```

```java
@RequestMapping("/testView")
public String testView(){
    System.out.println("testView");
    return "helloView";
}
```

## 6.3.自定义Excel视图



## 6.4.常用的视图实现类

- URL资源视图
  - `InternalResourceView`:JSP及其其它资源封装的视图，默认使用的视图实现类
  - `JstlView`：若JSP文件中使用了JSTL国际化标签的功能，则需要使用该视图类
- 文档视图
  - `AbstractExcelView`:Excel文档视图的抽象类，该类基于POI构建Excel文档
  - `AbstractPdfView`:PDF文档视图的抽象类，该类基于iText构建PDF文档
- 报表视图，使用`JasperReports`报表技术的视图
  - `ConfiguableJsperReportsView`
  - `JasperReportsCsvView`
  - `JasperReportsMultiFormatView`
- JOSN视图
  - `MappingJacksonJsonView`将模型数据提高Jackson开源框架ObjetcMapper以Json方式输出

## 6.5.视图解析器

- springmvc为逻辑视图名解析提供的不同策略，可以在SpringWeb上下文配置多种解析策略，并指定解析的先后顺序；每一种映射策略对应一个具体的视图解析器实现类
- 视图解析的作用：将逻辑视图解析为一个具体的视图对象
- 所有的视图解析器都必须实现`ViewResolver`接口

### 6.5.1.常用的视图解析器实现类

- 解析为Bean的名称
  - `BeanNameViewResolver`:将逻辑视图名解析Wie一个Bean，Bean的id为逻辑视图名

- 解析为URL文件

  - `InternalResourceViewResolver`:将视图名解析为一个URL文件，一般使用该解析器将视图名映射为一个保存在WEB-INF目录下的程序文件(如JSP)

  ```xml
  <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
      <property name="prefix" value="/WEB-INF/pages/"></property>
      <property name="suffix" value=".jsp"></property>
  </bean>
  ```

  - `JasperReportViewResolver`:JspaerReports是一个基于Java的开源报表工具，该解析器件视图解析为报表文件对应的URL

- 模板文件解析

  - `FreeMarkerViewResoler`:解析基于FreeMarker模板技术的模板文件
  - `VelocityViewResolver`:解析基于Velovity模板技术的模板文件
  - `VelocityLayoutViewResolver`:解析基于Velovity模板技术的模板文件

- 可以使用一种或多种视图解析器，使用多种视图解析器时，可以通过`order`指定视图解析器的先后顺序，`order`越小优先级越高（每个视图解析器都实现了Ordered接口并开放了一个order属性）
- SpringMVC会按视图解析器的优先顺序对逻辑视图名进行解析，直到解析成功并返回视图对象，否则抛出`ServletException`异常





# 七、重定向

## 7.1.`forward`

返回字符串包含前缀`forward：`进行转发操作

```java
@RequestMapping("/register")
public String register(Map<String,Object> map){
    return "forward:main";
}
```

## 7.2.`redirect`

返回字符串包含前缀`redirect：`进行重定向操作

```java
@RequestMapping("/register")
public String register(Map<String,Object> map){
    return "redirect:main";
}
```



# 八、Spring的表单标签

```jsp
<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
```

```jsp
<fm:form method="post" modelAttribute="user" action="${pageContext.request.contextPath}/user/useraddsave" >
    <fm:errors path="userCode"></fm:errors>
    用户编码 ：<fm:input path="userCode" /><br/>
    <fm:errors path="userName"></fm:errors>
    用户名称 ：<fm:input path="userName"/><br/>
    <fm:errors path="userPassword"></fm:errors>
    用户密码 ：<fm:password path="userPassword"/><br>
    用户地址 ：<fm:input path="address" /><br>
    用户电话 ： <fm:input path="phone"/><br>
    <fm:errors path="birthday"></fm:errors>
    用户生日 ：<fm:input path="birthday" /><br>

    用户性别： <fm:radiobutton path="gender" value="1"/>女
    		 <fm:radiobutton path="gender" value="2"/>男<br>		
    用户角色 ：<br>
    <fm:radiobutton path="userRole" value="101" />OrdinaryUser 
    <fm:radiobutton path="userRole" value="110"/>Administrator<br>
    <fm:radiobutton path="userRole" value="111"/>Manager 
    <fm:radiobutton path="userRole" value="100"/>tour<br>
    <input type="submit" name="保存"/>
    <input type="reset" value="重置"/>

```



# 九、数据绑定

## 9.1.数据绑定的流程

1. SpringMVC主框架将ServletRequest对象及目标方法的入参实例传递给 WebDataBinderFactory 实例，以创建 DataBinder 实例对象
2. DataBinder 调用装配在 Spring MVC 上下文中的 ConversionService 组件进行数据类型转换、数据格式
    化工作。将 Servlet 中的请求信息填充到入参对象中
3. 调用 Validator 组件对已经绑定了请求消息的入参对象进行数据合法性校验，并最终生成数据绑定结果
    BindingData 对象
4. Spring MVC 抽取 BindingResult 中的入参对象和校验误对象，将它们赋给处理方法的响应入参	

## 9.2.自定义类型转化器

```xml
<mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven>

<!--添加类型转换器-->
<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <ref bean="stringToDateConverter"/>
        </set>
    </property>
</bean>
```

```java
@Component
public class StringToDateConverter implements Converter<String,Date> {

    private static final List<String> formarts = new ArrayList<String>(4);

    static{
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd hh:mm");
        formarts.add("yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 功能描述：格式化日期
     * @param dateStr String 字符型日期
     * @param format String 格式
     * @return Date 日期
     */
    public  Date parseDate(String dateStr, String format) {
        Date date=null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = (Date) dateFormat.parse(dateStr);
        } catch (Exception e) {
        }
        return date;
    }


    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if(source.matches("^\\d{4}-\\d{1,2}$")){
            return parseDate(source, formarts.get(0));
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            return parseDate(source, formarts.get(1));
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
            return parseDate(source, formarts.get(2));
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            return parseDate(source, formarts.get(3));
        }else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

}
```

问题：如何指定转化应用的范围(字段)

ConversionService的实现

![1534227702709](E:\typora\images\1534227702709.png)

Converter的实现类

FactoryBean的实现类

## 9.3.数据类型格式化

```xml
<mvc:annotation-driven></mvc:annotation-driven>
```

```java
@NumberFormat(pattern = "yyyy-MM-dd")
private Date hiredate;
```

## 9.4.数据类型转化的流程







# 十、`<mvc:annotation-driven>`

`<mvc:annotation-driven>`会自动注册`RequestMappingHandlerMapping`、`RequestMappingHandlerAdapter`、

`ExceptionHandlerExceptionResolver`这三个bean;还会提供一下支持：

* 支持ConversionService实例对单参数的转换
* 支持@NumberFormat、@DateTimeFormat的注解完成数据格式化
* 支持@Valid注解对JavaBean实例进行JSR303验证
* 支持@RequestBody和@ResponseBody注解



# 十一、国际化