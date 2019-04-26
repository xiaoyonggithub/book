# 一、Spring概念 

## 1.1.Spring的模块划分

![spring overview](E:\typora\images\spring-overview.png) 

* Test:Srping的测试模块
* Core Container：核心容器Spring的基本功能，主要的主件是BeanFactory(工厂模式的实现)
  * Beans：bean的管理
  * Core:
  * Context:Spring上下文的一个配置文件，向Spring框架提供上下文信息；Spring的上下文包含企业服务，如JNDI、EJB、电子邮件、国际化、校验和调度功能
  * SpEl:
* AOP：集成面向切面编程，
* Aspects:
* Instrumentation:
* Messaging:
* Data Access/Integration:
  * JDBC:
  * ORM:抽象集成了若干个ORM框架（JDO、Hibernate、Mybatis）
  * OXM:
  * JMS:
  * Transactions:
* Web:Web上下文模块时建立在应用程序上下文模块之上的，为基于Web的应用程序提供上下文环境；
  * WebSoket:
  * Web:Web上下文模块时建立在应用程序上下文模块之上的，为基于Web的应用程序提供上下文环境；
  * Servlet:
  * Protlet:



* Spring MVC：是构建Web应用的程序的MVC实现



## 1.2.IOC（控制反转）



## 1.3.DI(依赖注入)



## 1.4.AOP（面向切面编程）



# 二、Spring配置文件

## 2.1.配置文件的头部

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/util
       http://www.springframework.org/schema/util/spring-util-4.0.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.0.xsd
       http://www.springframework.org/schema/jdbc
       http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://mybatis.org/schema/mybatis-spring 
       http://mybatis.org/schema/mybatis-spring.xsd
       ">
```

## 2.2.`classpath:`与`classpath*:`的区别

`classpath:`与`classpath*:`都是从类路径(WEB-INF/class)下加载资源文件

区别：

* `classpath:`只会到当前项目的classpath中查找文件，即只会从第一个classpath中加载；
* `classpath*:`不仅会在当前项目classpath下查找，还会在jar文件中（classpath）进行查找，即从所有的classpath中加载；

使用场景：

* 在多个classpath中存在同名资源，都需要加载时，那么用classpath:只会加载第一个，而classpath*会加载所有；
* 若要加载的资源，不在当前ClassLoader的路径里，那么用classpath:前缀是找不到的，此时就需要使用classpath*；

注意： 用`classpath*:`需要遍历所有的classpath，所以加载速度是很慢的；因此，在规划的时候，应该尽可能规划好资源文件所在的路径，尽量避免使用`classpath* `；

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath*:/spring-context-*.xml</param-value>
</context-param>
```

源码分析：

```java
public abstract class ResourceUtils {

	/** Pseudo URL prefix for loading from the class path: "classpath:". */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
}
```

```java
//ResourceLoader只支持classpth，即只支持单文件的载入
public interface ResourceLoader {
 	//类路径的前缀"classpath:"
	String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;
	Resource getResource(String location);
	ClassLoader getClassLoader();
}
```

```java
//ResourceLoader的子类支持classpth*，支持多文件的载入
public interface ResourcePatternResolver extends ResourceLoader {
	String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
}
```

流程图：



## 2.3.在路径上使用通配符`*`进行模糊查找 

Srping匹配资源路径时支持Ant模式通配符匹配，Spring提供AntPathMatcher来进行Ant风格的路径匹配。 

```xml
<param-value>classpath:applicationContext-*.xml</param-value>  
```

* [**]匹配零个或多个目录
  * [**/]表示任意目录

* [?]匹配一个字符

* [*]匹配零个或多个字符


# 三、创建bean的方式

## 3.1.配置文件创建`<bean>`

```xml
<!--class:bean全类名，通过反射的方式在IOC容器创建bean，要求必须提供无参构造器
	id：bean的唯一标识-->
<bean id="emp" class="com.xy.pojo.Emp"></bean>
```

## 3.2.通过静态工厂方法配置bean

静态工厂方法不需要创建工厂本身，直接创建Cars的bean对象

```java
/**
 * @ClassName Cars
 * @Description
 * @Author xiaoyong
 * @Date 2018-07-21 10:25
 */
public class Cars {

    public Cars() {
    }

    public Cars(String brand) {
        this.brand = brand;
    }

    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    @Override
    public String toString() {
        return "Cars{" +
                "brand='" + brand + '\'' +
                '}';
    }
}
```

```java
import com.xy.pojo.Cars;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CarFactory
 * @Description
 * @Author xiaoyong
 * @Date 2018-07-21 11:12
 */
public class CarFactory {

    private static Map<String,Cars> cars = new HashMap<>();

    static{
        cars.put("audi",new Cars("Audi"));
        cars.put("ford",new Cars("Ford"));
    }

    public static Cars getCar(String name){
        return cars.get(name);
    }

}

```

```xml
<!--静态工厂配置bean
    class:指向静态工厂类
    factory-method:指向静态工厂
    constructor-arg:设置静态方法要传入的参数-->
<bean id="cars" class="com.xy.factory.CarFactory" factory-method="getCar">
    <constructor-arg value="audi"/>
</bean>
```

## 3.3.通过实例工厂方法配置bean

需要先创建工厂的bean对象，再调用工厂方法返回Cars的bean对象

```java
/**
 * @ClassName Cars
 * @Description
 * @Author xiaoyong
 * @Date 2018-07-21 10:25
 */
public class Cars {

    public Cars() {
    }

    public Cars(String brand) {
        this.brand = brand;
    }

    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    @Override
    public String toString() {
        return "Cars{" +
                "brand='" + brand + '\'' +
                '}';
    }
}
```

```java
import com.xy.pojo.Cars;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName InstanceCarFactory
 * @Description
 * @Author xiaoyong
 * @Date 2018-07-21 11:26
 */
public class InstanceCarFactory {
    private Map<String,Cars> cars;


    public InstanceCarFactory() {
        cars = new HashMap<>();
        cars.put("audi",new Cars("Audi"));
        cars.put("ford",new Cars("Ford"));
    }

    public Cars getCar(String name){
        return cars.get(name);
    }
}

```

```xml
<!--配置工厂bean的实例-->
<bean id="instanceCarFactory" class="com.xy.factory.InstanceCarFactory"/>
<!--通过实例工厂配置bean
    factory-bean:指向实例工厂的bean
    factory-method:指向静态工厂
    constructor-arg:设置静态方法要传入的参数-->
<bean id="cars" factory-bean="instanceCarFactory" factory-method="getCar">
    <constructor-arg value="ford"/>
</bean>
```

## 3.4.`FactoryBean`配置bean

若配置的bean需要用到spring中其他的bean时，可使用此方式配置

```java
import com.xy.pojo.Cars;
import org.springframework.beans.factory.FactoryBean;

public class CarFactoryBean implements FactoryBean<Cars> {

    private String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @Author xiaoyong
     * @Description 返回bean对象
     * @Date 2018/7/21 11:42
     * @return com.xy.pojo.Cars
     **/
    @Override
    public Cars getObject() throws Exception {
        return new Cars(brand);
    }

    /**
     * @Author xiaoyong
     * @Description 返回bean的类型
     * @Date 2018/7/21 11:44
     * @return java.lang.Class<?>
     **/
    @Override
    public Class<?> getObjectType() {
        return Cars.class;
    }

    /**
     * @Author xiaoyong
     * @Description 设置返回的bean是否是单例
     * @Date 2018/7/21 11:44
     * @return boolean
     **/
    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

```xml
<!--FactoryBean配置bean
    class:指向FactoryBean
    property:配置FactoryBean的属性
    实际返回的是FactoryBean.getObject()方法返回的对象-->
<bean id="cars" class="com.xy.factory.CarFactoryBean">
    <property name="brand" value="Audi"/>
</bean>
```

## 3.5.基于注解配置bean

命名规则：`<context:component-scan>`扫描组件时，默认的命名规则是类名首字母小写。

```java
public interface UserService {

}
```

```java
@Service
public class UserServiceImpl implements UserService {
    
}
```

```xml
<!--扫描组件
    base-package:设置要扫描的基类包及其子包，若要扫描多个包可用,分隔
    resource-pattern:过滤条件，设置要扫描的规则-->
<context:component-scan base-package="com.xy" resource-pattern="serivce/*/*.class">
</context:component-scan>
```

```xml
<context:component-scan base-package="com.xy">
    <!--设置要排除的组件-->
    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

```xml
<!--注意:使用include-filter时，要设置use-default-filters="false"-->
<context:component-scan base-package="com.xy" use-default-filters="false">
    <!--设置只包含的组件-->
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Service"/>
</context:component-scan>
```

### 3.5.1.过滤的方式如下：

| `FilterType`                 | 描述                    |
| ---------------------------- | ----------------------- |
| `FilterType.ANNOTATION`      | 按注解过滤              |
| `FilterType.ASSIGNABLE_TYPE` | 按给定的类过滤          |
| `FilterType.ASPECTJ`         | 使用`aspectj`表达式过滤 |
| `FilterType.REGEX`           | 使用正则表达式过滤      |
| `FilterType.CUSTOM`          | 使用自定规则过滤        |

```xml
<!--注意:使用include-filter时，要设置use-default-filters="false"-->
<context:component-scan base-package="com.xy" use-default-filters="false">
    <!--设置只包含的组件，assignable根据类名过滤-->
    <context:include-filter type="assignable" expression="com.xy.serivce.impl.UserServiceImpl" />
</context:component-scan>
```

## 3.6.组件的装配

Spring提供一个后置处理器`AutowiredAnnotationBeanPostProcessor`实现自动装配

| 注解         | 描述                                                   |
| ------------ | ------------------------------------------------------ |
| `@Inject`    | 没有required属性                                       |
| `@Autowired` | 可放在普通字段、构造方法、一切据用参数的方法`setter`上 |
| `@Resource`  | 没有required属性                                       |

### 3.6.1.`@Autowired`

`@Autowired`装配时，是根据类型装配的，先判断该类型的bean的个数:

* 若只有一个bean就根据类型装配
* 若存在多个bean就根据名称（id）装配

```java
@Service
public class UserServiceImpl implements UserService {

    //@Autowired 放在普通字段上
    //required = false 设置userDao对象不是必须的，没有就不装配
    @Autowired(required = false)
    private UserDao userDao;

    //@Autowired 放在构造器上
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    //@Autowired 放在方法上
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

* 若存在多个bean且名称有相同的就需要指定要装配那个bean，与` @Qualifier`配合使用

```java
@Service
public class UserServiceImpl implements UserService {

    //@Qualifier("userDao") 装配名称为userDao的bean
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
}
```

```java
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    //@Qualifier("userDao") 装配名称为userDao的bean
    @Autowired
    public void setUserDao(@Qualifier("userDao")UserDao userDao) {
        this.userDao = userDao;
    }
}
```

`@Autowired`也可以放在数据、集合和`Map`属性上

* 数组：将所有匹配的`bean`的装配进数组
* 集合：`Spring`读取集合的类型，然后装配与之匹配的`bean`

* `Map`: 将`bean`的名称作为`key`，将`bean`作为`value`进行装配

```java
public interface Service {
}
@Service
public class EmpService implements Service {
}
@Service
public class UserService implements Service {

}
```

```java
@Configuration
@ComponentScan("com.example.demo.autowired.impl")
public class AutowiredConfig {

    @Autowired
    private List<Service> list;
    @Autowired
    private Map<String, Service> maps;

    public void outputResult() {
        list.stream().forEach(System.out::println);
        System.out.println("----------------------");
        Set<Map.Entry<String, Service>> entries = maps.entrySet();
        entries.stream().map(Map.Entry::getKey).forEach(System.out::println);
        entries.stream().map(x -> x.getValue()).forEach(System.out::println);
    }
}
```

```java
com.example.demo.autowired.impl.EmpService@62fad19
com.example.demo.autowired.impl.UserService@47dbb1e2
----------------------
empService
userService
com.example.demo.autowired.impl.EmpService@62fad19
com.example.demo.autowired.impl.UserService@47dbb1e2
```



### 3.6.2.`@Autowired、@Resource、@Inject`的区别

- `@Autowired`:Spring提供的注解
  - 通过`AutowiredAnnotationBeanPostProcessor`实现依赖注入的
  - 根据**类型**进行自动装配的；若根据ID装配，则需要与`@Qualifier`配合使用（自动注入的策略就从 byType转变成byName）
  - 可设置为`required=false`时，没找到bean时不报错
  - `@Autowired`可设置在变量、setter方法、构造函数上；`@Qualifier`的标注对象是成员变量、方法**入参**、构造函数**入参**
- `@Resource`:JSR-330提供的注解
  - 通过`javax.annotation`实现注入
  - 根据名称（ID）进行自动装配，`name`设置指定名称
  - 可作用在变量、setter方法
- `@Inject`:JSR-250提供的注解
  - 通过`javax.inject.Inject`实现注入
  - 根据类型自动装配；若需要按ID进行装配，需与`@Named`配合使用（**自动注入的策略就从byType转变成 byName了**）
  - 可设置在变量、setter方法、构造函数上





# 四、IOC容器

IOC容器在读取bean配置并实例化之前，需先对IOC容器实例化，实例化的方式：

* `BeanFactory`：IOC容器的基本实现，是Spring框架的基础设施，面向Spring本身；
* `ApplicationContext`：BeanFactory的子接口，提供了更多的高级特性，是面向Spring的开发者 
  * `ClassPathXmlApplicationContext`:从类路径加载配置文件
  * `FileSystemXmlApplicationContext`:从文件系统中加载配置文件

![1531743741690](E:\typora\images\1531743741690.png)

IOC容器在初始化上下文的时候，就实例化所有的单实例bean





# 五、依赖注入的方式

## 5.1.属性注入

属性注入就是通过setter方法注入属性,最常用的方式

```xml
<bean id="emp" class="com.xy.pojo.Emp">
    <property name="ename" value="张三"/>
</bean>
```

## 5.2.构造器注入

```xml
<!--name:参数的名称
    index:参数的索引，及参数的顺序
    type:设置参数的类型-->
<bean id="empCon" class="com.xy.pojo.Emp">
    <constructor-arg name="ename" value="张三"/>
    <constructor-arg name="job" value="软件工程"/>
</bean>
```

必须提供相应的构造方法

```java
 public Emp(String ename, String job) {
     this.ename = ename;
     this.job = job;
 }
```

## 5.3.工厂方法注入



## 5.4.注入字面值

- 字面值(可用字符串表示的值)可通过`<value>`标签或`value`属性注入;
- 基本数据类型及其包装类型、String等类型可使用字面值的方式注入;
- 若字面值中包含特殊字符，可使用`<![CDATA[]]>`包裹字面值;

```xml
<bean id="empCon" class="com.xy.pojo.Emp">
    <constructor-arg name="ename" value="张三"/>
    <constructor-arg name="job">
        <value><![CDATA[软件>工程]]></value>
    </constructor-arg>
</bean>
```

注意：`<![CDATA[]]>`只能在标签中使用，不能用于属性中

## 5.5.注入pojo

注入pojo对象，与其他的pojo建立关系

- 通过ref引用其他bean

```xml
<bean id="dept" class="com.xy.pojo.Dept">
    <property name="deptname" value="数计学院"/>
</bean>

<bean id="emp" class="com.xy.pojo.Emp">
    <property name="ename" value="张三"/>
    <!-- 引用其他的bean对象-->
    <property name="dept" ref="dept"/>
</bean>
```

- 建立内部bean

```xml
 <bean id="empInner" class="com.xy.pojo.Emp">
     <property name="ename" value="张三"/>
     <property name="dept">
         <bean class="com.xy.pojo.Dept">
             <property name="deptname" value="数计学院"/>
         </bean>
     </property>
</bean>
```

## 5.6.注入null

`<null/>`表示null值，但是注入null的意义不大，因为不注入值时就默认为null;

```xml
<bean id="empNull" class="com.xy.pojo.Emp">
    <property name="ename" value="张三"/>
    <!--注入null-->
    <property name="dept"><null/></property>
</bean>
```

## 5.7.级联属性注入

```xml
<bean id="dept" class="com.xy.pojo.Dept"></bean>

<bean id="emp" class="com.xy.pojo.Emp">
    <property name="ename" value="张三"/>
    <property name="dept" ref="dept"/>
    <property name="dept.deptname" value="数计学院"/>
</bean>
```

此处的dept.name不能直接创建dept对象，故需要先创建dept对象并注入在emp中，才能使用级联属性赋值;

## 5.8.集合属性的注入

list属性值的注入

```xml
<bean id="emp" class="com.xy.pojo.Emp">
    <property name="ename" value="张三"/>
</bean>

<bean id="deptEmp" class="com.xy.pojo.Dept">
    <property name="deptname" value="数计学院"/>
    <property name="emps">
        <list>
            <ref bean="emp"></ref>
            <bean class="com.xy.pojo.Emp">
                <property name="ename" value="李四"/>
            </bean>
        </list>
    </property>
</bean>
```

map属性值的注入

```xml
<bean id="emp" class="com.xy.pojo.Emp">
    <property name="ename" value="张三"/>
</bean>

<bean id="deptEmp" class="com.xy.pojo.Dept">
    <property name="deptname" value="数计学院"/>
    <property name="empMap">
        <map>
            <entry key="7364" value-ref="emp"></entry>
            <entry key="6373">
                <bean class="com.xy.pojo.Emp">
                    <property name="ename" value="李四"/>
                </bean>
            </entry>
        </map>
    </property>
</bean>
```

properties属性值的注入

```xml
<bean id="dataSource" class="com.xy.pojo.DataSource">
    <property name="properties">
        <props>
            <prop key="driverClass">com.mysql.jdbc.Driver</prop>
            <prop key="jdbcUrl">jdbc:mysql://test</prop>
            <prop key="username">root</prop>
            <prop key="password">1234</prop>
        </props>
    </property>
</bean>
    </bean>
```

## 5.9.配置独立的集合bean

1. 需要导入util名称空间

```xml
xmlns:util="http://www.springframework.org/schema/util"
xsi:schemaLocation="http://www.springframework.org/schema/util
    http://www.springframework.org/schema/util/spring-util-4.0.xsd"
```

2.用外部声明的集合

```xml
<!-- 声明集合类型的 bean -->
<util:list id="emps">
    <ref bean="emp"/>
    <ref bean="emp1"/>
</util:list>
```

```xml
<bean id="dept" class="com.xy.pojo.Dept">
    <property name="deptname" value="艺术学院"></property>
    <!-- 引用外部声明的 list -->
    <property name="emps" ref="emps"></property>
</bean>
```

## 5.10.`p`命名空间属性赋值

1. 导入p命名空间

```xml
xmlns:p="http://www.springframework.org/schema/p"
```

2. 使用p命名空间注入属性值

```xml
<bean id="dept" class="com.xy.pojo.Dept" p:deptname="土木学院" p:emps-ref="emp"></bean>
```

## 5.11.泛型依赖注入`spring4.x`

```java
public class User {
}
```

```java
public class BaseDao<T> {
}
```

```java
@Repository
public class UserDao extends BaseDao<User>{
}
```

```java
public class BaseService<T> {

    //此处注入在子类中也会自动装配
    @Autowired
    private BaseDao baseDao;

    public void insert(){
        System.out.println("insert ...");
        System.out.println(baseDao);
    }

}
```

```java
@Service
public class UserService extends BaseService<User> {
}
```

```xml
<context:component-scan base-package="com.xy"></context:component-scan>
```

```java
@Test
public void test08(){
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    UserService userService = (UserService) context.getBean("userService");
    userService.insert();
}
```

```java
insert ...
com.xy.dao.UserDao@769a1df5
```

# 六、自动装配

IOC能自动装配bean，只需要自定义自动装配的模式，自动装配的模式如下：

* `byType`:根据类型自动装配，缺点是有多个目标对象的bean时不知道装配那个;
* `byName`:根据名称自动装配，目标的名称必须与属性名相同;
* `constructor`:构造器自动装配，当Bean存在多个构造器时，会很复杂不推荐使用;

## 6.1.`byName`

```java
public class UserDao {
}
```

```java
public class UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

```java
public class UserController {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

```xml
<bean id="userDao" class="com.xy.dao.UserDao"></bean>
<bean id="userSrvice" class="com.xy.serivce.UserService" autowire="byName"></bean>
<!--注意：id的名称与属性名称一致，才能通过byName自动装配 -->
<bean id="userController" class="com.xy.controller.UserController" autowire="byName"></bean>
```

## 6.2.`byType`

```xml
<bean id="userDao" class="com.xy.dao.UserDao"></bean>
<bean id="userSrvice" class="com.xy.serivce.UserService" autowire="byType"></bean>
<bean id="userController" class="com.xy.controller.UserController" autowire="byType"></bean>
```

## 6.3.自动装配的缺点

* 设置了`autowire`自动装配的属性，所有的属性值都会自动装配，不能指定个别属性自动装配;
* 两种装配方式不能同时使用



# 七、bean之间的关系

## 7.1.继承

* parent继承bean的配置，子bean可覆盖父bean的配置
* 父bean可以作为一个配置模板，也可以是一个bean实例

```xml
<!--抽象bean（abstract="true"）不能被IOC容器初始化,只用来作为继承配置模板，可以不指定class-->
<bean id="emp" abstract="true">
    <property name="ename" value="默认值"/>
</bean>

<!--parent设置继承那个bean的配置-->
<bean id="emp2" class="com.xy.pojo.Emp" parent="emp">
    <property name="ename" value="张三"/>
</bean>
```

注意：不是所有的属性都被继承，如abstract就不会被继承

## 7.2.依赖

```xml
<bean id="car" class="com.xy.pojo.Car" p:company="大众"></bean>

<!--配置person必须关联一个car,depends-on设置必须依赖的bean-->
<bean id="person" class="com.xy.pojo.Person" p:age="20" p:name="小米" p:car-ref="car" depends-on="car"></bean>
```

# 八、bean的作用域

`@Scope`默认的作用域是` scope="singleton"`，作用域分类：

* `singleton`在IOC容器创建的时候初始bean，在容器的整个生命周期值创建一个bean

* `prototype`在获取的时候创建bean，每次的获取的都是一个新的bean

* `request`：web项目中，给每个http request新建一个实例

* `session`：web项目中，给每个http session新建一个实例

* `globalSession`:只在`portal`应用中有效，给每个gloabl http session新建一个实例

  

# 九、引入外部属性文件

Spring提供了一个BeanFactory的后置处理器`PropertyPlaceholderConfigurer`加载外部的属性文件

```xml
<!-- 导入context命名空间 -->
<beans xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.0.xsd
       ">
```

```xml
<!-- 导入外部的资源文件 -->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!-- 配置数据源 -->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="user" value="${jdbc.username}"></property>
    <property name="password" value="${jdbc.password}"></property>
    <property name="driverClass" value="${jdbc.driverClass}"></property>
    <property name="jdbcUrl" value="${jdbc.url}"></property>
</bean>
```

# 十、SpEL表达式

SpEL可以为bean的属性动态赋值，支持在注解或xml中使用表达书

* 算数运算：`+, -, *, /, %, ^ `

* 比较运算：`<, >, ==, <=, >=, lt, gt, eq, le, ge `
* 连接字符串`+`

## 10.1.引用其它的对象

```xml
<property name="car" value="#{car}"/>
```

## 10.2.引用其它对象的属性

```xml
<property name="city" value="#{address.city}"/>
```

## 10.3.调用方法

```xml

```

## 10.4.链式调用

```xml

```

## 10.5.调用静态方法或静态属性

```xml
<bean id="car" class="com.xy.pojo.Car">
    <property name="brand" value="Audi"/>
    <property name="price" value="500000"/>
    <!--使用SpEL引入静态属性-->
    <property name="tyrePerimeter" value="#{T(java.lang.Math).PI * 80}"/>
</bean>
```

## 10.6.逻辑运算`and, or, not, |` 



## 10.7.正则表达式（matches）

```xml
<bean id="address" class="com.xy.pojo.Address">
    <!--SpEL赋值一个字面值-->
    <property name="city" value="#{'ChengDu'}"/>
    <property name="street" value="JingJangQu"/>
</bean>

<bean id="car" class="com.xy.pojo.Car">
    <property name="brand" value="Audi"/>
    <property name="price" value="500000"/>
    <!--使用SpEL引入静态属性-->
    <property name="tyrePerimeter" value="#{T(java.lang.Math).PI * 80}"/>
</bean>

<bean id="person" class="com.xy.pojo.Person">
    <!--SpEL引入其他的bean-->
    <property name="car" value="#{car}"/>
    <!--SpEL引入其他bean的属性-->
    <property name="city" value="#{address.city}"/>
    <!--在SpEL使用运算符-->
    <property name="info" value="#{car.price > 30000 ? '金领' : '白领'}"/>
    <property name="name" value="Tom"/>
</bean>
```





# 十一、bean的生命周期

Spring提供一个后置处理器`BeanPostProcessor`，允许在调用初始化方法`init()`的前后对bean进行操作;后置处理器是对IOC容器的所有bean进行逐一扫描处理;

bean生命周期的管理过程如下：

1. 创建IOC完成后，调用构造方法(constructor)或工厂方法创建bean
2. 为bean的属性赋值（setter）
3. 调用后置处理器（BeanPostProcessor）的postProcessBeforeInitialization()方法
4. 调用bean的初始化方法(init)
5. 调用后置处理器（BeanPostProcessor）的postProcessAfterInitialization()方法
6. 使用bean
7. IOC容器关闭时，调用bean的销毁方法（destroy）

```java
public class Cars {

    public Cars() {
        System.out.println("Car's Construtor...");
    }

    public Cars(String brand) {
        this.brand = brand;
    }

    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        System.out.println("setBrand....");
        this.brand = brand;
    }
    
    public void init(){
        System.out.println("init...");
    }
    
    public void destory(){
        System.out.println("destroy...");
    }
    
    @Override
    public String toString() {
        return "Cars{" +
                "brand='" + brand + '\'' +
                '}';
    }
}
```

```java
import com.xy.pojo.Cars;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @ClassName MyBeanPostProcessor
 * @Description
 * @Author xiaoyong
 * @Date 2018-07-21 10:57
 */
public class MyBeanPostProcessor implements BeanPostProcessor {

    /**
     * @Author xiaoyong
     * @Description 在init()方法之前执行
     * @Date 2018/7/21 10:58
     * @param bean 处理的bean
     * @param beanName bean的id
     * @return java.lang.Object
     **/
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization.."+bean+","+beanName);
        return bean;
    }

    /**
     * @Author xiaoyong
     * @Description 在init()方法之后执行
     * @Date 2018/7/21 10:58
     * @param bean 处理的bean
     * @param beanName bean的id
     * @return java.lang.Object
     **/
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization.."+bean+","+beanName);
        if(bean.getClass().equals(Cars.class)){
            Cars cars  = (Cars)bean;
            cars.setBrand("Auid");
        }
        return bean;
    }
}
```

```xml
<bean id="cars" class="com.xy.pojo.Cars" init-method="init" destroy-method="destory">
    <property name="brand" value="BaoMa"/>
</bean>
<bean class="com.xy.common.MyBeanPostProcessor"/>
```

```java
@Test
public void test06(){
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    System.out.println("IOC容器创建完成....");
    Cars cars = (Cars) context.getBean("cars");
    System.out.println(cars);
    context.close();
}
```

```java
Car's Construtor...
setBrand....
postProcessBeforeInitialization..Cars{brand='BaoMa'},cars
init...
postProcessAfterInitialization..Cars{brand='BaoMa'},cars
setBrand....
IOC容器创建完成....
Cars{brand='Auid'}
destroy...
```

# 十二、AOP

* 切面(Aspect):横切关注点(跨域应用程序多个模块的功能)被模块化的特殊对象
* 通知(Advice):切面必须完成的工作
* 目标对象(Target):被通知的对象
* 代理对象(Proxy):向目标对象通知之后创建的对象
* 连接点(Joinpoint):程序执行的某个特定位置,如某个方法执行前、执行后或抛出异常后等; 
* 切点(Pointcut):每个类都有多个连接点,它使用类和方法作为连接点的查询条件,`Pointcut`接口描述切点

## 12.1.动态代理实现日志

```java
public class User {
}
```

```java
public class UserDao{

    public Integer insert() {
        return null;
    }

    public Integer update() {
        return null;
    }

    public User query() {
        return null;
    }

    public List<User> queryList() {
        return null;
    }

    public Integer delete() {
        return null;
    }

}
```

```java
public interface UserService{
    Integer insert();
    Integer update();
    User query();
    List<User> queryList();
    Integer delete();
}
```

```java
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer insert() {
        Integer row = userDao.insert();
        return row;
    }

    @Override
    public Integer update() {
        Integer row = userDao.update();
        return row;
    }

    @Override
    public User query() {
        User user = userDao.query();
        return user;
    }

    @Override
    public List<User> queryList() {
        List<User> userList = userDao.queryList();
        return userList;
    }

    @Override
    public Integer delete() {
        Integer row = userDao.delete();
        return row;
    }
}
```

```java
import com.xy.serivce.UserService;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserServiceLogProxy {

    //代理的目标对象
    private UserService target;

    public UserServiceLogProxy(UserService target) {
        this.target = target;
    }

    public UserService getLoggingProxy(){
        //代理对象
        UserService proxy = null;
        //代理对象有那个类加载加载
        ClassLoader loader = target.getClass().getClassLoader();
        //代理对象的类型,即其中有哪些方法
        Class[] interfaces = new Class[]{UserService.class};
        //对代理对象的进行的处理操作
        InvocationHandler handler = new InvocationHandler() {
            /**
             * @Author xiaoyong
             * @Description  代理的操作
             * @Date 2018/7/21 23:17
             * @param proxy 正在返回的代理对象，一般在invoke中不使用
             * @param method 正在调用的方法
             * @param args 正在调用方法的参数
             * @return java.lang.Object
             **/
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //日志
                System.out.println(method.getName()+" start execute...");
                System.out.println(method.getName()+" include args is [" + Arrays.asList(args) + "]");
                //执行方法
                method.invoke(target,args);
                //日志
                System.out.println(method.getName()+" end execute");
                return null;
            }
        };
        //创建代理对象
        proxy = (UserService) Proxy.newProxyInstance(loader,interfaces,handler);
        return proxy;
    }

}
```

```java
@Test
public void test09() {
    UserService target = new UserServiceImpl();
    UserService proxy = new UserServiceLogProxy(target).getLoggingProxy();
    proxy.query();
}
```

## 12.2.`AspectJ`实现日志基于注解

AspectJ最流行的AOP框架

```java
@Configuration
@ComponentScan("com.xy")
@EnableAspectJAutoProxy
public class AopConfig {

    
}
```

```java
@Repository
public class UserDao{

    public Integer insert(User user) {
        return null;
    }

    public Integer update() {
        return null;
    }

    public User query(Integer userId) {
        return null;
    }

    public List<User> queryList() {
        return null;
    }

    public Integer delete() {
        return null;
    }
}
```

```java
public interface UserService{

    Integer insert(User user);
    Integer insert();
    Integer update();
    User query(Integer userId);
    List<User> queryList();
    Integer delete();
}
```

```java
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer insert(User user) {
        Integer row = userDao.insert(user);
        return row;
    }

    @Override
    public Integer insert() {
        Integer row = userDao.insert(new User());
        return row;
    }

    @Override
    public Integer update() {
        Integer row = userDao.update();
        return row;
    }

    @Override
    public User query(Integer userId) {
        User user = userDao.query(userId);
        return user;
    }

    @Override
    public List<User> queryList() {
        List<User> userList = userDao.queryList();
        return userList;
    }

    @Override
    public Integer delete() {
        Integer row = userDao.delete();
        return row;
    }
}
```

```java
//定义一个切面
@Aspect
@Component
public class LoggingAspect {

    //@Before 设置改方法是一个前置通知，在目标方法之前执行
    @Before("execution(public Integer com.xy.serivce.UserService.*())")
    //@Before("execution(public Integer com.xy.serivce.UserService.insert())")
    public void beforeMethod(JoinPoint joinPoint){
        //方法名
        String methodName = joinPoint.getSignature().getName();
        //参数
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The Method ["+methodName+"] start with" + args);
    }
}
```

```java
@Test
public void test08() {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    UserService userService = (UserService) context.getBean("userService");
    userService.insert();
}
```

> 注意：切面类只针对在IOC容器中的类有效

## 12.3.`AspectJ`支持的通知类型

| 通知类型          | 描述                                           |
| ----------------- | ---------------------------------------------- |
| `@Before`         | 前置通知，在方法执行之前执行                   |
| `@After`          | 后置通知，在方法执行之后(无论是否发生异常)执行 |
| `@AfterReturning` | 返回通知，在方法返回结果之后执行               |
| `@AfterThrowing`  | 异常通知，在方法抛出异常后执行                 |
| `@Around`         | 环绕通知，环绕着方法执行                       |

注意：在后置通知中不能访问方法的执行结果，返回通知可以访问到方法的执行结果;

```java
@Aspect //声明该类为一个切面类
@Component
public class LoggingAspect {

    //@Before 设置改方法是一个前置通知，在目标方法之前执行
    @Before("execution(* com.xy.serivce.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        //方法名
        String methodName = joinPoint.getSignature().getName();
        //参数
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] start");
    }

    //后置通知
    @After(value = "execution(* com.xy.serivce.*.*(..))")
    public void after(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] end");
    }

    //返回通知,注意此处result的类型必须是Object,否则不执行
    @AfterReturning(value = "execution(* com.xy.serivce.impl.*.*(..))",
                   returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] return is " + result);
    }

    /**
     * @Author xiaoyong
     * @Description 异常通知,在目标方法抛出异常的时执行
     * @Date 2018/7/22 22:57
     * @param joinPoint 连接点
     * @param ex 若方法抛出该异常,就执行异常通知
     * @return void
     **/
    @AfterThrowing(value = "execution(* com.xy.serivce.*.*(..))",throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint,NullPointerException ex){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] exception is " + ex);
    }

    /**
     * @Author xiaoyong
     * @Description 环绕通知类似于动态代理的全过程，ProceedingJoinPoint参数可以决定是否执行目标方法;
     * 注意:1.必须携带ProceedingJoinPoint类型的参数
     *      2.必须有返回值,返回值即目标方法的返回值
     * @Date 2018/7/22 23:04
     * @param point
     * @return java.lang.Object
     **/
    @Around("execution(* com.xy.serivce.*.*(..))")
    public Object around(ProceedingJoinPoint point){
        //目标方法名
        String methodName = point.getSignature().getName();
        //目标方法参数
        Object[] args = point.getArgs();
        Object result = null;
        try {
            //前置通知
            System.out.println(methodName+"("+Arrays.asList(args)+") Before advice");
            //执行目标方法
            result = point.proceed();
            //返回通知
            System.out.println(methodName+"("+Arrays.asList(args)+") AfterReturning advice");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //异常通知
            System.out.println(methodName+"("+Arrays.asList(args)+") AfterThrowing advice");
        }
        //后置通知
        System.out.println(methodName+"("+Arrays.asList(args)+") After advice");

        return result;
    }

}
```

## 12.4.通知执行的位置

```java
InvocationHandler handler = new InvocationHandler() {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            //前置通知
            Object result = method.invoke(target, args);
            //返回通知，能获取到放回值
        }catch (Exception e){
            //异常通知
            e.printStackTrace();
        }
        //后置通知，可能会有异常所以获取不到返回值
        return result;
    }
};
```

## 12.5.`pointCut()`

### 12.5.1.`pointCut()`切点的配置规则

```java
@Before("execution(public Integer com.xy.serivce.UserService.insert())")
@Before("execution(* com.xy.serivce.*.*(..))")
```

第一`[*]`表示任意修饰符和任意放回值，如`public int,public String`等

第二`[*]`表示包下的任意类

第三`[*]`表示类型的任意方法

`[..]`表示任意参数

### 12.5.2.抽取公共的切点方法

```java
@Order(1)
@Aspect
@Component
public class LoggingAspect {

    /**
     * @Author xiaoyong
     * @Description 定义切点表达式
     * @Date 2018/7/22 23:25
     * @return void
     **/
    @Pointcut("execution(* com.xy.serivce.*.*(..))")
    public void pointcut(){}

    //若是其他的切面引用LoggingAspect.pointcut()
    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint){
        //方法名
        String methodName = joinPoint.getSignature().getName();
        //参数
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] start");
    }
}
```

注意：一个切面引用其他切面的切点

* 同一包下：`LoggingAspect.pointcut()`
* 不同的包下：`com.xy.common.LoggingAspect.pointcut()`

## 12.6.切面的优先级

`@Order(1)`设置切面的优先级，值越小优先级越高

```java
@Order(1)
@Aspect
@Component
public class LoggingAspect {
}
```

## 12.7.`AspectJ`实现日志基于XML

```java
public class User {
}
```

```java
public class UserDao{
    public Integer insert() {
        return 1;
    }
}
```

```java
public interface UserService{
    Integer insert();
}
```

```java
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    //注意：使用xml引用，需提供setter方法
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Integer insert() {
        Integer row = userDao.insert();
    }
}

```

```java
public class LoggingAspectXml {

    public void before(JoinPoint joinPoint){
        //方法名
        String methodName = joinPoint.getSignature().getName();
        //参数
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] start");
    }

    public void after(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] end");
    }

    //joinPoint必须是第一个参数，result接收返回值
    public void afterReturning(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName(); 
        Object[] args = joinPoint.getArgs();
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] return is " + result);
    }

    //ex接收异常
    public void afterThrowing(JoinPoint joinPoint,NullPointerException ex){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("The Method ["+methodName+"("+Arrays.asList(args)+")] exception is " + ex);
    }

    public Object around(ProceedingJoinPoint point){
        //目标方法名
        String methodName = point.getSignature().getName();
        //目标方法参数
        Object[] args = point.getArgs();
        Object result = null;
        try {
            //前置通知
            System.out.println(methodName+"("+Arrays.asList(args)+") Before advice");
            //执行目标方法
            result = point.proceed();
            //返回通知
            System.out.println(methodName+"("+Arrays.asList(args)+") AfterReturning advice");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //异常通知
            System.out.println(methodName+"("+Arrays.asList(args)+") AfterThrowing advice");
        }
        //后置通知
        System.out.println(methodName+"("+Arrays.asList(args)+") After advice");

        return result;
    }

}
```



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/util
       http://www.springframework.org/schema/util/spring-util-4.0.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.0.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
       ">

    <bean id="userDao" class="com.xy.dao.UserDao"/>
    <bean id="userService" class="com.xy.serivce.impl.UserServiceImpl">
        <property name="userDao" ref="userDao"/>
    </bean>

    <!--配置切面的bean-->
    <bean id="loggingAspect" class="com.xy.common.LoggingAspectXml"/>

    <!--配置AOP-->
    <aop:config>
        <!--配置切点表达式-->
        <aop:pointcut id="pointcut" expression="execution(* com.xy.serivce.*.*(..))"/>
        <!--配置切面及通知-->
        <aop:aspect ref="loggingAspect" order="1">
            <!--前置通知-->
            <aop:before method="before" pointcut-ref="pointcut"/>
            <!--返回通知-->
            <aop:after-returning method="afterReturning" pointcut-ref="pointcut" returning="result"/>
            <!--异常通知-->
            <aop:after-throwing method="afterThrowing" pointcut-ref="pointcut" throwing="ex"/>
            <!--后置通知-->
            <aop:after method="after" pointcut-ref="pointcut"/>
            <!--环绕通知-->
            <aop:around method="around" pointcut-ref="pointcut"/>
        </aop:aspect>
    </aop:config>

</beans>
```

## 12.8.为一个切面设置多个切点

可以使用`&&、||、!、and、or`等



## 12.9.`AOP`原理

`@EnableAspectJAutoProxy`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AspectJAutoProxyRegistrar.class})
public @interface EnableAspectJAutoProxy {
    boolean proxyTargetClass() default false;

    boolean exposeProxy() default false;
}
```

```java
//ImportBeanDefinitionRegistrar用于下容器中注册自定义组件

class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {
    AspectJAutoProxyRegistrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
        AnnotationAttributes enableAspectJAutoProxy = AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
        if (enableAspectJAutoProxy != null) {
            if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
                AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
            }

            if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
                AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
            }
        }

    }
}
```











# 十三、`JdbcTempalte`

## 13.1.配置`JdbcTemplate`

```xml
<!--加载配置文件jdbc.properties-->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!--配置数据源-->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driverClass}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="user" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!--配置JdbcTemplate-->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

## 13.2.常用方法的使用

```java
@Before
public void init() {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
}

//更新
@Test
public void updateTest() {
    String sql = "UPDATE emp SET comm = ? WHERE empno = ? ";
    int row = jdbcTemplate.update(sql, 20, 7369);
    System.out.println(row);
}

//批量插入
@Test
public void batchInsertTest() {
    String sql = "INSERT INTO scott.emp (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    ArrayList<Object[]> list = new ArrayList<Object[]>();
    list.add(new Object[]{"3748", "张三", "软件工程师", "7282", new Date(), 3948, 0, 10});
    list.add(new Object[]{"3749", "李四", "Java工程师", "7282", new Date(), 3948, 0, 10});
    list.add(new Object[]{"3750", "爱丽丝", "Python工程师", "7282", new Date(), 3948, 0, 10});
    list.add(new Object[]{"3751", "莫凡", "C++工程师", "7282", new Date(), 3948, 0, 10});
    jdbcTemplate.batchUpdate(sql, list);
}

//查询单条记录
@Test
public void queryForObjectTest() {
    String sql = "SELECT * FROM emp WHERE empno = ?";
    //行记录的映射，即表字段与对象属性映射,不支持级联属性的查询
    RowMapper<Emp> rowMapper = new BeanPropertyRowMapper<>(Emp.class);
    Emp emp = jdbcTemplate.queryForObject(sql, rowMapper, 3749);
    System.out.println(emp);
}

//查询多条记录
@Test
public void queryTest() {
    String sql = "SELECT * FROM emp WHERE empno > ?";
    RowMapper<Emp> rowMapper = new BeanPropertyRowMapper<>(Emp.class);
    List<Emp> emps = jdbcTemplate.query(sql, rowMapper, 4000);
    System.out.println(emps);
}


//获取单个列的属性值或统计查询
@Test
public void queryForObjectTest2() {
    String sql = "select count(*) from emp";
    Long count = jdbcTemplate.queryForObject(sql, Long.class);
    System.out.println(count);
}
```

## 13.3.在项目中的使用

方式一：直接注入JdbcTemplate

```java
@Repository
public class EmpDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Emp get(Integer empno){
        String sql = "select * from emp where empno = ?";
        RowMapper<Emp> rowMapper = new BeanPropertyRowMapper<>(Emp.class);
        Emp emp = jdbcTemplate.queryForObject(sql, rowMapper);
        return emp;
    }
}
```

方式二：继承JdbcDaoSupport,必须注入datatSource或jdbcTemplate属性

```java
@Repository
public class DeptDao extends JdbcDaoSupport {

    @Autowired
    public void setDataSource2(DataSource dataSource){
        setDataSource(dataSource);
    }
    
    public Dept get(){
        String sql = "select * from dept where deptno = ?";
        RowMapper<Dept> rowMapper = new BeanPropertyRowMapper<>(Dept.class);
        Dept dept = getJdbcTemplate().queryForObject(sql, rowMapper, 10);
        return dept;
    }
}
```

方式三：继承JdbcDaoSupport,必须注入datatSource或jdbcTemplate属性

```java
@Repository
public class DeptDao extends JdbcDaoSupport {
	private JdbcTemplate jdbcTemplate;
}
```

```xml
<!--加载配置文件jdbc.properties-->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!--配置数据源-->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driverClass}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="user" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!--配置JdbcTemplate-->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"/>
</bean>

<bean id="deptDao" class="com.xy.dao.DeptDao">
    <property name="jdbcTemplate" ref="jdbcTemplate"/>
</bean>
```

## 13.4.具名参数

```xml
<!--没有无参构造器-->
<bean id="namedParameterJdbcTemplate" class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate">
    <constructor-arg ref="dataSource"/>
</bean>
```

```java
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

@Before
public void init() {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("namedParameterJdbcTemplate");
}

//map封装参数
@Test
public void testNamedParameterJdbcTemplate(){
    String sql = "insert into dept(deptno,dname,loc) values(:deptno,:dname,:loc)";
    Map<String,Object> param = new HashMap<>();
    param.put("deptno",50);
    param.put("dname","开发部");
    param.put("loc","ChengDu");

    int row = namedParameterJdbcTemplate.update(sql, param);
    System.out.println(row);
}

//pojo封装参数
@Test
public void testNamedParameterJdbcTemplate2(){
    String sql = "insert into dept(deptno,dname,loc) values(:deptno,:deptname,:loc)";
    Dept dept = new Dept();
    dept.setDeptno(60);
    dept.setDeptname("技术部");
    dept.setLoc("ChengDu");

    SqlParameterSource param = new BeanPropertySqlParameterSource(dept);
    int row = namedParameterJdbcTemplate.update(sql, param);
    System.out.println(row);
}
```

# 十四、事务管理

`Spring`并不会直接管理事务，而是提供了事务管理器，将事务管理的职责委托给`JPA JDBC JTA DataSourceTransaction JMSTransactionManager` 等框架提供的事务来实现

## 14.1.事务的四个属性（ACID）

* 原子性`atomicity`：事务是一个原子操作，有一系列动作组成。务的原子性确保动作要么全部完成，要么全不起作用；
* 一致性`consistency`:一旦事务动作完成，事务就提交。数据和资源处于满足业务规则的一致状态中；
* 隔离性`isolation`：可能许多事务会同时处理相同的数据，因此每个事务都应该与其他事务隔开，防止数据损坏；
* 持久性`durability`：一旦事务完成，无论发生什么系统错误，它的结果都不应该受影响。通常情况下，事务的结果被写入到持久化存储器中；

## 14.2.事务管理器的实现

事务的管理器的顶层接口`PlatformTransactionManager`

![1532437449857](E:\typora\images\1532437449857.png)

`TransactionDefinition`包含事务的定义

```java
public interface TransactionDefinition {
    //传播机制
    int PROPAGATION_REQUIRED = 0;
    int PROPAGATION_SUPPORTS = 1;
    int PROPAGATION_MANDATORY = 2;
    int PROPAGATION_REQUIRES_NEW = 3;
    int PROPAGATION_NOT_SUPPORTED = 4;
    int PROPAGATION_NEVER = 5;
    int PROPAGATION_NESTED = 6;
    //隔离级别
    int ISOLATION_DEFAULT = -1;
    int ISOLATION_READ_UNCOMMITTED = 1;
    int ISOLATION_READ_COMMITTED = 2;
    int ISOLATION_REPEATABLE_READ = 4;
    int ISOLATION_SERIALIZABLE = 8;
    int TIMEOUT_DEFAULT = -1;
	//获取传播机制
    int getPropagationBehavior();
	//获取隔离级别
    int getIsolationLevel();

    int getTimeout();
	
    boolean isReadOnly();

    String getName();
}
```

`TransactionStatus`事务的状态

```java
public interface TransactionStatus extends SavepointManager, Flushable {
    boolean isNewTransaction();

    boolean hasSavepoint();

    void setRollbackOnly();

    boolean isRollbackOnly();

    void flush();

    boolean isCompleted();
}
```

`Spring`的事务管理

```java
DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
TransactionStatus status = transactionManager.getTransaction(definition);
try{
    //业务代码
    transactionManager.commit(status);
}catch (Exception e){
    transactionManager.rollback(status);
}
```

## 14.3.声明式事务

```xml
<!--配置事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!--开启事务注解-->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

```java
//@Transactional 设置该方法使用事务管理
//propagation 设置事务的传播行为
//isolation 设置隔离级别
//spring声明式事务默认对所有的运行时异常进行回滚
//rollbackFor(类型)/rollbackForClassName(名称) 指定事务会回滚的异常
//noRollbackFor(类型)/noRollbackForClassName(名称) 指定事务不会回滚的异常
//readOnly 设置事务是否只读
@Transactional(propagation = Propagation.REQUIRED,
               isolation = Isolation.READ_COMMITTED,
               rollbackFor = {NullPointerException.class,ClassCastException.class},
               readOnly = true
              )
@Override
public Integer insert() {
    Integer row = userDao.insert(new User());
    return row;
}
```

## 14.4.事务的传播行为

事务传播行为：当一个事务被另一个事务调用时，必须指定事务应该如何传播；`REQUIRED`默认行为

| 事务的传播行为  | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| `REQUIRED`      | 若有事务在运行，当前方法就在这个事务内运行，否则启动一个新的事务运行该方法 |
| `REQUIRES_NEW`  | 当前方法必须启动一个事务运行该方法，若有事务正在运行就将它挂起 |
| `SUPPORTS`      | 若当前有事务在运行，当前方法就在这个事务内运行，否则就可不运行在事务中 |
| `NOT_SUPPORTED` | 当前方法不应该运行在事务中，若有运行的事务，就将其挂起       |
| `MANDATORY`     | 当前事务必须运作在事务内部，若没有正在运行的事务就抛出异常   |
| `NEVER`         | 当前方法不应该运行在事务中，若有运行的事务，就抛出异常       |
| `NESTED`        | 若有事务运行在运行，当前方法就在这个事务的嵌套事务内运行，否则，就启动一个新事务运行该方法 |

| 事务传播行为类型          | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| PROPAGATION_REQUIRED      | 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。 |
| PROPAGATION_SUPPORTS      | 支持当前事务，如果当前没有事务，就以非事务方式执行。         |
| PROPAGATION_MANDATORY     | 使用当前的事务，如果当前没有事务，就抛出异常。               |
| PROPAGATION_REQUIRES_NEW  | 新建事务，如果当前存在事务，把当前事务挂起。                 |
| PROPAGATION_NOT_SUPPORTED | 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。   |
| PROPAGATION_NEVER         | 以非事务方式执行，如果当前存在事务，则抛出异常。             |
| PROPAGATION_NESTED        | 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。 |

```java

```

## 14.5.并发事务所导致的问题

| 问题类型   | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| 脏读       | 对于两个事物 T1、T2, T1读取了已经被 T2 更新但还没有被提交的字段. <br />之后, 若 T2 回滚, T1读取的内容就是临时且无效的 |
| 不可重复读 | 对于两个事物 T1、T2, T1读取了一个字段, 然后 T2 更新了该字段. <br />之后, T1再次读取同一个字段, 值就不同了 |
| 幻读       | 对于两个事物 T1、T2, T1从一个表中读取了一个字段, 然后 T2 在该表中插入了一些新的行.<br />之后, 如果 T1 再次读取同一个表, 就会多出几行 |

## 14.6.隔离级别

| 隔离级别           | 描述     |
| ------------------ | -------- |
| `DEFAULT`          | 默认     |
| `READ_UNCOMMITTED` | 读未提交 |
| `READ_COMMITTED`   | 读已提交 |
| `REPEATABLE_READ`  | 可重复读 |
| `SERIALIZABLE`     | 序列化   |

## 14.7.设置回滚的异常

spring声明式事务默认对所有的运行时异常进行回滚

```java
//spring声明式事务默认对所有的运行时异常进行回滚
//rollbackFor(类型)/rollbackForClassName(名称) 指定事务会回滚的异常
//noRollbackFor(类型)/noRollbackForClassName(名称) 指定事务不会回滚的异常
@Transactional(propagation = Propagation.REQUIRED,
               isolation = Isolation.READ_COMMITTED,
               rollbackFor = {NullPointerException.class,ClassCastException.class}
               //rollbackForClassName ={"NullPointerException","ClassCastException"}
              )
@Override
public Integer insert() {
    Integer row = userDao.insert(new User());
    return row;
}
```

## 14.8.基于XML配置声明式事务

```xml
<!--配置事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!--开启事务注解-->
<tx:annotation-driven transaction-manager="transactionManager"/>

<!--配置事务属性-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <!--配置查询方法为只读-->
        <tx:method name="get*" read-only="true"/>
        <tx:method name="query*" read-only="true"/>
        <tx:method name="select*" read-only="true"/>
        <!--配置修改方法-->
        <tx:method name="update*" isolation="READ_COMMITTED" propagation="REQUIRED" rollback-for="NullPointerException,ClassCastException"/>
    </tx:attributes>
</tx:advice>

<!--配置事务切入点，以及关联事务切入点和事务属性-->
<aop:config proxy-target-class="true">
    <!--事务切入点-->
    <aop:pointcut id="txPointcut" expression="execution(* com.xy.serivce.*.*(..))"/>
    <!--关联事务切入点和事务属性-->
    <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut"/>
</aop:config>
```

[https://segmentfault.com/a/1190000013341344](https://segmentfault.com/a/1190000013341344)

