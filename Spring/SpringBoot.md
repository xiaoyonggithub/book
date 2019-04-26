

# 一、常用注解

注解本身没有功能，与`xml`一样，都是一种元数据（解释数据的数据）

## 1.1.`@Value`

1)、注入普通字符

```java
@Value("姓名")
private String name;
```



2)、注入操作系统属性

```java
@Value("#{systemProperties['os.name']}")
private String osName;
```



3)、注入表达式运算结果

```java
@Value('#{T(java.lang.Math).random()*100.0}')
private double randomNumber;
```



4)、注入其他的Bean属性

```java
@Value('#{userSerive.author}')
private String anothor;
```



5)、注入文件内容

```java
@Value('calsspath:com/example/test.txt')
private Resource resource;
```



6)、注入网址内容

```java
@Value('http://www.baidu.com')
private Resource url;
```



7)、注入属性文件

```properties
#属性文件
book.name=spring boot
```

```java
@Value('${book.name}')
private String bookName;
```

此时需要使用`@PropertySource`注入属性文件，

```java
@PropertySource('classpath:com/example/test.properties')
public class ElConfig{}
```




## 1.2.`@PropertySource`

`@ConfigurationProperties`默认从全局配置文件中获取值，`@PropertySource`指定加载的配置文件`(.properties或.yml文件)`

```java
@Data
@Component
@ConfigurationProperties(prefix = "user")
//指定配置文件位置，不指定默认读取application.yml
@PropertySource(value = {"classpath:user.yml"}) 
@Validated
public class User {

    private String userId;
    private String username;
    @Min(value = 0)
    private Integer age;
    private Address address;
    private List<String> hobby;
    private Map<String, Object> scores;

}
```

## 1.3.`@ImportResource`

`@ImportResource`导入自定义的`spring`的配置文件`(.xml)`，一般在主配置类上引入

```java
@ImportResource(laction = {"classpath:beans.xml"})
```

## 1.4.`@Profile`

- 加了环境标识的Bean只有这个环境被激活后，才能注册在IOC容器中
- `@Profile`设置在配置类上时，只有是指定的该环境时，才会加载该配置类
- 没有标注`@Profile`的Bean，会在任何环境下加载

```java
@Configuration
@PropertySource("classpath:/db.properties")
public class ProfileConfig implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String user;

    private StringValueResolver valueResolver;

    private String driverClass;

    @Bean
    @Profile("dev")
    public DataSource devDataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/dev");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/prod");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.valueResolver = stringValueResolver;
        driverClass = valueResolver.resolveStringValue("${db.driverClass}");
    }
}
```

- 使用命令参数激活，在虚拟机参数位置

```java
-Dspring.profiles.active=dev
```

- 使用代码的方式激活

```java
public void test02() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    //获取配置环境
    ConfigurableEnvironment environment = context.getEnvironment();
    //设置默认的profile
    environment.setDefaultProfiles("prod");
    //设置激活的profile
    environment.setActiveProfiles("dev");
    //注册配置
    context.register(ProfileConfig.class);
    context.refresh();

    UserService userService = context.getBean(UserService.class);
    System.out.println(userService.getUsername());
    context.close();
}
```

- 





## 1.5.`@ConfigurationProperties`

`@ConfigurationProperties`告诉`SpringBoot`将`JavaBean`的所有属性与配置文件中相关的配置进行绑定

```java
@Data
@Component
@ConfigurationProperties(prefix = "user")
public class User {

    private String userId;
    private String username;
    private Integer age;
    private Address address;
    private List<String> hobby;
    private Map<String, Object> secodes;

}
```

```yaml

```





## 1.6.`@Configuration`

`@Configuration`标注一个类为注解类，有如下要求：

- `@Configuration`标注的类不能是`final`修饰类
- `@Configuration`标注的类不可以是匿名类
- 配置类也是容器的一个组件`@Component`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
    @AliasFor(
        annotation = Component.class
    )
    String value() default "";
}
```





## 1.7.`@ComponentScan`

`@ComponentScan`会自动扫描包下所有使用了`@Controller、@Component、@Service、@Respository`的类，并注册为`Bean`





## 1.8.`@Bean`

`@Bean`声明在方法上，声明当前方法的返回值为一个Bean

## 1.9.`@AspectJ`

`@AspectJ`声明一个切面

## 1.10.建言注解

- `@After`
- `@Before`
- `@Around`

## 1.11.`@PointCut`

`@PointCut`定义建言的拦截规则（即声明切点），然后在`@After,@Before,@Around`的参数中调用规则；

其中符合条件的每一个被拦截处称为连接点`JoinPoint`



## 1.12.`@Transcation`



## 1.13.`@Cacheable`



## 1.14.`@Async`

- 标注在方法上，表示该方法为异步方法
- 标注在类上，表示该类的所有方法都是异步方法



## 1.15.`@Scope`



## 1.16.`@stepScope`



## 1.17.`@PropertySource`

`@PropertySource`设置属性文件的位置

```xml
<context:property-placeholder location="classpath:jdbc.properties"/>
```

```java
@PropertySource("classpath:application.properties")
public class ElConfig {
}
```

## 1.18.`@PostConstruct`

在构造函数执行完之后执行，与`initMethod、destroyMethod`类似

```java
public class UserService {
    @PostConstruct
    private void init(){
        System.out.println("UserService...init");
    }

    public EmpService() {
        System.out.println("UserService...constructor");
    }

    @PreDestroy
    public void destory(){
        System.out.println("UserService...destory");
    }
}
```

```java
@Bean(initMethod = "init",destroyMethod = "destory")
public UserService userService() {
    return new UserService();
}
```

```java
UserService...constructor
UserService...init
UserService...destory
```

## 1.19.`@PreDestory`

在Bean销毁之前执行



## 1.20.开启配置`@Enable*`

- `@EnableAsync`:开启异步任务的支持
- `@EnableAspectJAutoProxy`:开启Spring对AspectJ自动代理的支持
- `@EnableScheduling`：开启计划任务的支持
  - `@Scheduled`:声明一个计划任务
- `@EnableWebMvc`:开启WebMVC的支持
- `@EnbaleConfigurationProperties`:开启对`@ConfigurationProperties`注解配置Bean的支持
- `@EnbaleJpaRespositories`：开启对`Spring Data JPA Respository`的支持
- `@EnbaleTransactionManagement`:开启注解式事务的支持
- `@EnbaleCaching`:开启注解式缓存的支持
- `@EnableAutoConfiguration`：开启自动配置支持

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage  //自动配置包
@Import({AutoConfigurationImportSelector.class})  //给spring容器导入一个组件
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
```

`@AutoConfigurationPackage`自动配置包

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({Registrar.class}) //给spring容器导入一个组件
public @interface AutoConfigurationPackage {
}
```

```java
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {
    Registrar() {
    }

    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AutoConfigurationPackages.register(registry, (new AutoConfigurationPackages.PackageImport(metadata)).getPackageName());
    }

    public Set<Object> determineImports(AnnotationMetadata metadata) {
        return Collections.singleton(new AutoConfigurationPackages.PackageImport(metadata));
    }
}
```

`AutoConfigurationImportSelector`会给容器中导入非常多的自动配置类（xxxAutoConfiguration）；就是给容器中导入这个场景需要的所有组件，并配置好这些组件。

```java
public String[] selectImports(AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
        return NO_IMPORTS;
    } else {
        AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);
        AutoConfigurationImportSelector.AutoConfigurationEntry autoConfigurationEntry = this.getAutoConfigurationEntry(autoConfigurationMetadata, annotationMetadata);
        return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
    }
}

protected AutoConfigurationImportSelector.AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata, AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
        return EMPTY_ENTRY;
    } else {
        AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
        //得到导入到自动配置
        List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
        configurations = this.removeDuplicates(configurations);
        Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
        this.checkExcludedClasses(configurations, exclusions);
        configurations.removeAll(exclusions);
        configurations = this.filter(configurations, autoConfigurationMetadata);
        this.fireAutoConfigurationImportEvents(configurations, exclusions);
        return new AutoConfigurationImportSelector.AutoConfigurationEntry(configurations, exclusions);
    }
}
```

```java
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
    //加载
    List<String> configurations = SpringFactoriesLoader.loadFactoryNames(this.getSpringFactoriesLoaderFactoryClass(), this.getBeanClassLoader());
    Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");
    return configurations;
}
```

```java
public static List<String> loadFactoryNames(Class<?> factoryClass, @Nullable ClassLoader classLoader) {
    String factoryClassName = factoryClass.getName();
    return (List)loadSpringFactories(classLoader).getOrDefault(factoryClassName, Collections.emptyList());
}

private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    MultiValueMap<String, String> result = (MultiValueMap)cache.get(classLoader);
    if (result != null) {
        return result;
    } else {
        try {
            //加载默认的自动配置
            Enumeration<URL> urls = classLoader != null ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories");
            LinkedMultiValueMap result = new LinkedMultiValueMap();

            while(urls.hasMoreElements()) {
                //把扫描的文件内容包装成Properties对象
                URL url = (URL)urls.nextElement();
                UrlResource resource = new UrlResource(url);
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                Iterator var6 = properties.entrySet().iterator();
			  //从properties中获取到EnableAutoConfiguration.class类（类名）对应的值，然后把他们添加在容器中
                while(var6.hasNext()) {
                    Entry<?, ?> entry = (Entry)var6.next();
                    String factoryClassName = ((String)entry.getKey()).trim();
                    String[] var9 = StringUtils.commaDelimitedListToStringArray((String)entry.getValue());
                    int var10 = var9.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        String factoryName = var9[var11];
                        result.add(factoryClassName, factoryName.trim());
                    }
                }
            }

            cache.put(classLoader, result);
            return result;
        } catch (IOException var13) {
            throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", var13);
        }
    }
}
```

将类路径下`spring-boot-autoconfigure-2.1.3.RELEASE.jar!/META-INF/spring.factories`配置的所有`EnableAutoConfiguration`的值加入到了容器中

```factories
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
.....
```





## 1.21.`@Sheduled`



## 1.22.`@Import`

给`Spring`容器导入一个组件

- 直接导入配置类

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SchedulingConfiguration.class})
@Documented
public @interface EnableScheduling {
}
```

- 根据条件选择配置类

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AsyncConfigurationSelector.class})//AsyncConfigurationSelector通过条件选择导入的配置文件
public @interface EnableAsync {
    Class<? extends Annotation> annotation() default Annotation.class;

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;

    int order() default 2147483647;
}
```

```java
public class AsyncConfigurationSelector extends AdviceModeImportSelector<EnableAsync> {
    private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";

    public AsyncConfigurationSelector() {
    }
	
    /**
     * 选择导入不同的配置类
     */
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch(adviceMode) {
        case PROXY:
            return new String[]{ProxyAsyncConfiguration.class.getName()};
        case ASPECTJ:
            return new String[]{"org.springframework.scheduling.aspectj.AspectJAsyncConfiguration"};
        default:
            return null;
        }
    }
}
```

- 动态注册Bean

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
/**
 * ImportBeanDefinitionRegistrar作用是运行时，自动添加Bean到已有的配置类
 * 
 */
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {
    AspectJAutoProxyRegistrar() {
    }

    /**
     * AnnotationMetadata :获取当前配置类上的注解
     * BeanDefinitionRegistry :用来注册Bean
     */
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

## 1.23.`@RunWith`

 `@RunWith`指定测试运行时的运行器

```java
@RunWith(SpringJUnit4ClassRunner.class)//使用Spring测试环境
@RunWith(JUnit4.class)//指定JUnit4运行
@RunWith(Suite.class)//一套测试集合
```



## 1.24.`@ContextConfiguration`

`@ContextConfiguration`测试时，需要引入的配置文件

```java
@ContextConfiguration(locations = { "classpath*:/spring1.xml", "classpath*:/spring2.xml" })  
```

```java
@ContextConfiguration(classes = {TestBean.class})
```





## 1.25.`@SpringBootTest`





## 1.26 ` @SpringBootApplication`

` @SpringBootApplication`标注一个类为主程序类，表明这是一个`Spring Boot`应用。会将主配置类（`@SpringBootApplication`标注的类）的所在包及下面所有子包里面的所有组件扫描到`Spring`容器。

```java
@SpringBootApplication
public class SpringDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }
}
```

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration //标注在类上，表示这是一个SpringBoot配置类
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    @AliasFor(
        annotation = EnableAutoConfiguration.class
    )
    Class<?>[] exclude() default {};

    @AliasFor(
        annotation = EnableAutoConfiguration.class
    )
    String[] excludeName() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackages"
    )
    String[] scanBasePackages() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackageClasses"
    )
    Class<?>[] scanBasePackageClasses() default {};
}
```

## 1.27.`@SpringBootConfiguration`

`@SpringBootConfiguration`标注在类上，表示这是一个`SpringBoot`配置类

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration //标注在类上，表明这是一个配置类
public @interface SpringBootConfiguration {
}
```



## 1.28.`@Conditional`



### 1.28.1.`@Conditional`派生注解

在`@Conditional`指定的条件成立，才给容器中添加组件，配置配里面的所有内容才生效。

| @Conditional扩展注解            | 作用（判断是否满足当前指定条件）                 |
| ------------------------------- | ------------------------------------------------ |
| @ConditionalOnJava              | 系统的java版本是否符合要求                       |
| @ConditionalOnBean              | 容器中存在指定Bean；                             |
| @ConditionalOnMissingBean       | 容器中不存在指定Bean；                           |
| @ConditionalOnExpression        | 满足SpEL表达式指定                               |
| @ConditionalOnClass             | 系统中有指定的类                                 |
| @ConditionalOnMissingClass      | 系统中没有指定的类                               |
| @ConditionalOnSingleCandidate   | 容器中只有一个指定的Bean，或者这个Bean是首选Bean |
| @ConditionalOnProperty          | 系统中指定的属性是否有指定的值                   |
| @ConditionalOnResource          | 类路径下是否存在指定资源文件                     |
| @ConditionalOnWebApplication    | 当前是web环境                                    |
| @ConditionalOnNotWebApplication | 当前不是web环境                                  |
| @ConditionalOnJndi              | JNDI存在指定项                                   |

**自动配置类必须在一定的条件下才能生效；**如何查看那些自动配置类生效：

- 使用` debug=true`属性，在控制台打印自动配置报告

```yml
debug: true
```

```java
=========================
AUTO-CONFIGURATION REPORT
=========================


Positive matches:（自动配置类启用的）
-----------------

   DispatcherServletAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.servlet.DispatcherServlet'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)
        
    
Negative matches:（没有启动，没有匹配成功的自动配置类）
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.ConnectionFactory', 'org.apache.activemq.ActiveMQConnectionFactory' (OnClassCondition)

   AopAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.aspectj.lang.annotation.Aspect', 'org.aspectj.lang.reflect.Advice' (OnClassCondition)
```







# 二、`SpringBoot`

- 简化了`Spring`应用的开发的一个框架
- 是`Spring`技术栈的一个大整合
- 是`JavaEE`开发的一站式解决方案









# 三、`SpringBoot`的配置

全局配置使用Java配置（如数据库相关配置、MVC相关配置），业务配置使用注解配置`(@Controller、@Component、@Service、@Respository)`

## 3.1.`pom.xml`

父项目设置了一些常用依赖的默认依赖的版本

- 已经管理了依赖版本的，导入时可以不设置版本
- 没有管理依赖版本的，导入时需要指定版本

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.9.RELEASE</version>
</parent>
```

```xml
<properties>
    <activemq.version>5.15.8</activemq.version>
    <antlr2.version>2.7.7</antlr2.version>
    <appengine-sdk.version>1.9.71</appengine-sdk.version>
    ....
</properties>
```

## 3.2.简化部署

```xml
<!-- 这个插件，可以将应用打包成一个可执行的jar包；-->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

将这个应用打成`jar`包，直接使用`java -jar`的命令进行执行；

```xml
java -jar sample.jar
```

## 3.3.`Profile`

`Profile`为在不同的环境下使用不同的配置提供支持，可以`application-{profile}.yml`

1).使用`Environment`设置激活的配置环境

```java
@Configuration
public class ProfileConfig {
    @Bean
    @Profile("dev")
    public UserService userServiceDev() {
        return new UserService("dev");
    }

    @Bean
    @Profile("prod")
    public UserService userServiceProd() {
        return new UserService("prod");
    }
}
```

```java
public void test02() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    //获取配置环境
    ConfigurableEnvironment environment = context.getEnvironment();
    //设置默认的profile
    environment.setDefaultProfiles("prod");
    //设置激活的profile
    environment.setActiveProfiles("dev");
    //注册配置
    context.register(ProfileConfig.class);
    context.refresh();

    UserService userService = context.getBean(UserService.class);
    System.out.println(userService.getUsername());
    context.close();
}
```

2).通过`jvm`的参数值配置环境

```java
-Dsring.profiles.active
-Dsring.profiles.default
```

3).`web`项目中设置在`Servlet`的`context parameter`

```xml
<servlet>
	<servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
    	<param-name>spring.profiles.active</param-name>
        <param-value>production</param-value>
    </init-param>
</servlet>
```

```java
public class WebInit implements WebApplicationInitializer{
	public void onStartup(ServletContext context){
		context.setInitParamter("spring.profiles.active","dev");
         context.setInitParamter("spring.profiles.default","dev");
    } 
}
```

4).在命令行激活

```java
java -jar spring-boot-02-config-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

5).在配置文件中激活

```properties
spring.profiles.active=dev
```



### 3.3.1.`yml`支持多文档块方式

```yml
server:
  port: 8081
spring:
  profiles:
    active: prod

---
server:
  port: 8083
spring:
  profiles: dev


---
server:
  port: 8084
spring:
  profiles: prod  #指定属于哪个环境
```









## 3.4.启动器

`spring-boot-starter-web`是`spring-boot`的场景启动器，导入`web`模块正常运行需要的组件。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

`Spring Boot`将所有的功能场景都抽取出来，做成一个个的`starters`（启动器），引入对应的场景的`starter`就会在对应场景的所有依赖。

## 3.5.`SpringBoot`的目录结构

- `resources`
  - `static`:存放静态资源，如`js、css、images`等
  - `templates`:存放模板文件，如`freemarker、thymeleaf`等模板引擎(`SpringBoot`默认`jar`使用嵌入式`Tocmat`，默认不支持`JSP`页面)
  - `application.properties|application.yml`：`SpringBoot`的全局应用配置文件，可修改一些默认配置，配置名称固定

## 3.6.`YAML`

`YAML（YAML Ain't Markup Language）`**不是一种标记语言**，强调以数据为中心，比`json、xml`等更适合做配置文件，基本语法：

- `K: v`一对键值对，`:`后面有一个空格
- 以空格控制层级关系，左对齐的一类数据都是一个层级

```yaml
server:
    port: 8081
    path: /hello
```

值的写法:

- 字面量：普通的值（数字，字符串，布尔）

  - 直接写，字符串默认不用添加引号

    - `""`双引号，转义显示字符串，按实际效果显示

    ```yaml
    name: "zhangsan \n lisi"  #zhangsan 换行 lisi
    ```

    - `''`单引号，不转义显示字符串，按原样显示

    ```yaml
    name: 'zhangsan \n lisi'  #zhangsan \n lisi
    ```

- 对象、Map（属性和值）（键值对）

```yaml
friends:
 lastName: zhangsan
 age: 20
```

```yaml
friends: {lastName: zhangsan,age: 20}  #行内写法
```

- 数组（List、Set）

```yaml
#用-值表示数组中的一个元素
pets:
 - cat
 - dog
 - pig
```

```yaml
#行内写法
pets: [cat,dog,pig]
```

### 3.6.1.配置文件的注入

```yaml

```

### 3.6.2.`@ConfigurationProperties`与`@Value`的区别

|                      | @ConfigurationProperties | @Value     |
| -------------------- | ------------------------ | ---------- |
| 功能                 | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定（松散语法） | 支持                     | 不支持     |
| SpEL                 | 不支持                   | 支持       |
| JSR303数据校验       | 支持                     | 不支持     |
| 复杂类型封装         | 支持                     | 不支持     |

配置文件不论是`yml`还是`properties`都能获取到值；

- 若只是在某个业务逻辑中获取配置文件中的某项值，使用`@Value`；
- 若有专门的`JavaBean`与配置文件进行映射时，建议使用`@ConfigurationProperties`；



### 3.6.3.配置文件注入值数据校验

```java
@Data
@Component
@ConfigurationProperties(prefix = "user")
@Validated //数据检验
public class User {

    private String userId;
    private String username;
    @Min(value = 0)
    private Integer age;
    private Address address;
    private List<String> hobby;
    private Map<String, Object> scores;

}
```

`SpringBoot`推荐给容器添加组件的方式是：不使用`Spring`的配置文件，而是使用`Java`配置类的方式。

```java
@Configuration
public class MyAppConfig {

    //将方法的返回值添加到容器中；容器中这个组件默认的id就是方法名
    @Bean
    public HelloService helloService02(){
        System.out.println("配置类@Bean给容器中添加组件了...");
        return new HelloService();
    }
}
```

### 3.6.4.配置文件`.yml`占位符

- 随机数

```java
${random.value}、${random.int}、${random.long}
${random.int(10)}、${random.int[1024,65536]}
```

- 设置默认值

```yml
person.last-name=张三${random.uuid}
```

## 3.7.配置文件的加载顺序

`springboot`启动会扫描以下位置的`application.properties`或者`application.yml`文件作为`Spring boot`的默认配置文件，优先级从高到底，高优先级的会覆盖底优先级的配置。

- `file:./config/`

- `file:./`

- `classpath:/config/`

- `classpath:/`

`SpringBoot`四个位置的配置文件会全部加载到主配置文件中，**互补配置**。

使用`spring.config.location`可以修改默认配置文件的位置，指定的配置文件和默认加载的配置文件会形成互补配置。

```java
java -jar spring-boot-02.jar --spring.config.location=G:/application.properties
```

### 3.7.1.外部配置文件的加载顺序

`SpringBoot`可以从以下位置加载配置：优先级从高到底；高优先级的配置会覆盖低优先级配置，所有的配置会形成**互补配置**。

1. 命令行参数，配置之间使用`--key=val`

```java
java -jar spring-boot-02.jar --server.port=8087  --server.context-path=/abc
```

2. 来自`java:comp/env`的`JNDI`属性
3. `Java`系统属性（`System.getProperties()`）
4. 操作系统环境变量
5. `RandomValuePropertySource`配置的`random.*`属性值
6. `jar`包外部的`application-{profile}.properties`或`application.yml`(带`spring.profile`)配置文件
7. `jar`包内部的`application-{profile}.properties`或`application.yml`(带`spring.profile`)配置文件

8. `jar`包外部的`application.properties`或`application.yml`(不带`spring.profile`)配置文件

9. `jar`包内部的`application.properties`或`application.yml`(不带`spring.profile`)配置文件

10. `@Configuration`注解类上的`@PropertySource`

11. 通过`SpringApplication.setDefaultProperties`指定的默认属性

> 由`jar`外向`jar`内加载，优先加载带`profile`的配置文件，再加载不带`profile`文件

[^参考官方文档说明]: <https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#boot-features-external-config>

## 3.8.自动配置原理

[^自动配置属性文档参考]: <https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#common-application-properties>

1. `SpringBoot`启动时加载主配置类，并开启了自动配置功能`@EnbaleAutoConfiguration`

```java
@Configuration
//启动指定类的ConfigurationProperties功能；将配置文件中对应的值和HttpProperties绑定起来；并把HttpProperties加入到ioc容器中
@EnableConfigurationProperties({HttpProperties.class})
//判断当前应用是否是web应用，如果是，当前配置类生效
@ConditionalOnWebApplication(
    type = Type.SERVLET
)
//判断当前项目有没有这个类CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器
@ConditionalOnClass({CharacterEncodingFilter.class})
@ConditionalOnProperty(
    prefix = "spring.http.encoding",
    value = {"enabled"},
    matchIfMissing = true
)
public class HttpEncodingAutoConfiguration {
    private final Encoding properties;

    public HttpEncodingAutoConfiguration(HttpProperties properties) {
        this.properties = properties.getEncoding();
    }

    @Bean
    @ConditionalOnMissingBean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(this.properties.getCharset().name());
        filter.setForceRequestEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.REQUEST));
        filter.setForceResponseEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.RESPONSE));
        return filter;
    }

    @Bean
    public HttpEncodingAutoConfiguration.LocaleCharsetMappingsCustomizer localeCharsetMappingsCustomizer() {
        return new HttpEncodingAutoConfiguration.LocaleCharsetMappingsCustomizer(this.properties);
    }

    private static class LocaleCharsetMappingsCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>, Ordered {
        private final Encoding properties;

        LocaleCharsetMappingsCustomizer(Encoding properties) {
            this.properties = properties;
        }

        public void customize(ConfigurableServletWebServerFactory factory) {
            if (this.properties.getMapping() != null) {
                factory.setLocaleCharsetMappings(this.properties.getMapping());
            }

        }

        public int getOrder() {
            return 0;
        }
    }
}
```

2. 配置文件中的配置属性都在`xxxxProperties`封装，配置文件配置什么可以参考某个功能对应的属性类

```java
@ConfigurationProperties(
    prefix = "spring.http"
)
public class HttpProperties {
    private boolean logRequestDetails;
    private final HttpProperties.Encoding encoding = new HttpProperties.Encoding();

    public HttpProperties() {
    }
    .....
}
```



# 四、日志





















































## 2.2.`事件`

Spring事件（Application Event）为Bean与Bean之间的消息通信提供支持；

事件遵循的流程：

- 自定义事件，继承`ApplciationEvent`
- 定义事件监听器，实现`ApplicationListener`
- 使用容器发布事件

```java
@Data
public class DemoEvent extends ApplicationEvent {
    private String msg;

    public DemoEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }
}
```

```java
//监听指定的事件类型DemoEvent
@Component
public class DemoListener implements ApplicationListener<DemoEvent> {
    @Override
    public void onApplicationEvent(DemoEvent demoEvent) {
        //对消息进行接收处理
        String msg = demoEvent.getMsg();
        System.out.println("msg:" + msg);
    }
}
```

```java
@Component
public class DemoEventPublisher {
    @Autowired
    ApplicationContext applicationContext;

    public void publisher(String msg) {
        //发布事件
        applicationContext.publishEvent(new DemoEvent(this, msg));
    }
}
```



# 三、`Java`配置





# 四、`Spring Aware`

- `BeanNameAware`:获取容器中Bean的名称
- `BeanFatoryAware`:获取当前的bean factory，可用于调用容器服务
- `ApplicationContextAware*`:获取当前的application context，可用于调用容器服务
- `MessageSourceAware`:获取message source，可用获取文本信息
- `ApplicationEventPublisherAware`:应用事件发布器，用于发布事件
- `ResourceLoaderAware`:获取资源加载器，可以加载外部资源文件

`ApplicationContext`接口集成了`MessageSource、ApplicationEventPublisher、ResourceLoader`等接口，故继承`ApplicationContextAware`可以获取Spring容器的所有服务，但原则是使用什么接口实现什么接口

```java
//源码
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
    @Nullable
    String getId();

    String getApplicationName();

    String getDisplayName();

    long getStartupDate();

    @Nullable
    ApplicationContext getParent();

    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;
}
```

```java
@Service
public class AwareService implements BeanNameAware, ResourceLoaderAware {
    //bean的名称
    private String beanName;
    //资源加载得到服务
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void outputResult() throws IOException {
        System.out.println("beanName的名称：" + beanName);
        Resource resource = resourceLoader.getResource("application.properties");
        System.out.println("加载的文件：" + resource.getFilename());
        System.out.println("加载的文件：" + resource.getInputStream());
    }
}
```

```java
beanName的名称：awareService
加载的文件：application.properties
加载的文件：java.io.BufferedInputStream@1c80e49b
```



# 五、任务执行器

`Spring`使用任务执行器`(TaskExecutor)`来实现多线程和并发编程



# 六、测试

集成测试是提供一个无须部署或运行程序来完成系统各个部分是否能正常协同工作的能力；

`Spring TestContext Framework`对集成测试提供顶层支持，不依赖特定的测试框架，即可使用`Junit`，也可使用`TestNG`:





