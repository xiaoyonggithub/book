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
@Value('${bbok.name}')
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
@PropertySource(value = {"classpath:person.properties"})
```

## 1.3.`@ImportResource`

`@ImportResource`导入`spring`的配置文件`(.xml)`，一般在主配置类上引入

```java
@ImportResource(laction = {"classpath:beans.xml"})
```

## 1.4.`@Profile`

- 加了环境标识的Bean只有这个环境被激活后，才能注册在IOC容器中
- `@Profile`设置在配置类类上时，只有是指定的该环境时，才会加载该配置类
- 没有标注`@Profile`的Bean，会在任何环境下加载

```java
@Configuration
@PropertySource("classpath:/db.properties")
public class ProfileConfig implements EmbeddedValueResolverAware {

    @Value("${db.user}")
    private String pwd;

    private StringValueResolver valueResolver;

    private String driverClass;

    @Bean
    @Profile("dev")
    public DataSource devDataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/dev");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword(pwd);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
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



## 1.6.`@Configuration`

`@Configuration`标注一个类为注解类，有如下要求：

- `@Configuration`标注的类不能是`final`修饰类

- `@Configuration`标注的类不可以是匿名类

  



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

## 1.21.`@Sheduled`



## 1.22.`@Import`

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





# 二、`SpringBoot`的配置

全局配置使用Java配置（如数据库相关配置、MVC相关配置），业务配置使用注解配置`(@Controller、@Component、@Service、@Respository)`



## 2.1.`Profile`

Profile为在不同的环境下使用不同的配置提供支持

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
spring.profiles.active
spring.profiles.default
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

Spring使用任务执行器`(TaskExecutor)`来实现多线程和并发编程



# 六、测试

集成测试是提供一个无须部署或运行程序来完成系统各个部分是否能正常协同工作的能力；

`Spring TestContext Framework`对集成测试提供顶层支持，不依赖特定的测试框架，即可使用`Junit`，也可使用`TestNG`:





