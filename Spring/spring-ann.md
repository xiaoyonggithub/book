# 一、`spring`的注解

## 1.1.`@ComponentScan`

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Repeatable(ComponentScans.class)   //表示@ComponentScan组件可重复，需要JDK8
public @interface ComponentScan {
    ...
}
```

```java
//用户定义不同的过滤规则
@ComponentScan(value = "com.xy",includeFilters = {
        //只包含@Controller组件
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={ Controller.class}),
},useDefaultFilters = false)
@ComponentScan(value = "com.xy",includeFilters = {
        //只包含@Service组件
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={ Service.class}),
},useDefaultFilters = false)
```

若`java`版本低于`jdk8`如何使用多个`@ComponentScan`

```java
@ComponentScans(value = {
        @ComponentScan(value = "com.xy",includeFilters = {
           //只包含@Controller组件
          @ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={ Controller.class}),
        },useDefaultFilters = false),
        @ComponentScan(value = "com.xy",includeFilters = {
          //只包含@Service组件
          @ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={ Service.class}),
        },useDefaultFilters = false)
})
```

> **`@ComponentScan`注解值的解释**

| 注解值           | 描述                                   |
| ---------------- | -------------------------------------- |
| `value`          | 默认值，这种扫描的包路径`basePackages` |
| `includeFilters` | 设置扫描时只包含的组件                 |
| `excludeFilters` | 设置扫描时排除的组件                   |

### 1.1.1.`excludeFilters`

`excludeFilters`设置扫描时排除那些组件

```java
@ComponentScan(value = "com.xy",excludeFilters = {
        //排除标注了@Controller和@Service注解的组件
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={ Controller.class, Service.class})
})
```

### 1.1.2.`includeFilters`

`includeFilters`设置扫描时值包含哪些组件

```java
@ComponentScan(value = "com.xy",includeFilters = {
    //只包含@Controller组件
    @ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={ Controller.class}),
    //只包含BookService的组件
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes ={ BookService.class}),
},useDefaultFilters = false)
```

### 1.1.3.`FilterType`  过滤规则

| `FilterType`                 | 描述                    |
| ---------------------------- | ----------------------- |
| `FilterType.ANNOTATION`      | 按注解过滤              |
| `FilterType.ASSIGNABLE_TYPE` | 按给定的类过滤          |
| `FilterType.ASPECTJ`         | 使用`aspectj`表达式过滤 |
| `FilterType.REGEX`           | 使用正则表达式过滤      |
| `FilterType.CUSTOM`          | 使用自定规则过滤        |

### 1.1.4.使用自定义规则过滤

```java
/**
 * 自定义过滤规则
 */
public class MyTypeFilter implements TypeFilter {
    /**
     * @param metadataReader:读取当前正在扫描类的信息
     * @param metadataReaderFactory:可以获取当其他任何类的信息
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前扫描类的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前扫描类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前正在扫描类的资源信息（类路径）
        Resource resource = metadataReader.getResource();

        //设置匹配规则
        if(classMetadata.getClassName().contains("Service")){
            return true;
        }
        return false;
    }
}
```

```java
//设置扫描的规则，此处使用自定义的过滤规则 
@ComponentScan(value = "com.xy",includeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM,classes = MyTypeFilter.class)
},useDefaultFilters = false)
public class MainConfig {

}
```

```java
//测试代码
 @Test
public void test(){
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
    String[] names = context.getBeanDefinitionNames();
    System.out.println(names.toString());
    for(String name : names){
        System.out.println(name);
    }
}
```

## 1.2.`@Scope`

| `@Scope`的作用范围 | 描述                                   |
| ------------------ | -------------------------------------- |
| `singleton`        | 单实例，默认值                         |
| `prototype`        | 多实例                                 |
| `request`          | `web`环境，同一个请求创建一个实例      |
| `session`          | `web`环境，同一个`session`创建一个实例 |

* `sigleton`：在`ioc`容器创建的时候就创建对象，并放入容器
* `prototype`：在获取对象的时候创建，每获取一次就创建一次。

## 1.3.`@Lazy`

`@Lazy`设置对象创建懒加载

```java
@Lazy   //懒加载
@Bean
public Person person(){
    return new Person("1","zs",13);
}
```

* `singleton`：在容器启动的时候不创建对象，在第一次获取的创建。

## 1.4.`@Conditonal`

`@Conditional`按条件注册`bean`，满足条件的才注册；该注解可以放在方法上，也可以放在类上。

### 1.4.1.获取运行环境

```java
//获取运行的环境
Environment environment = context.getEnvironment();
String os = environment.getProperty("os.name");
System.out.println(os);
```

### 1.4.2.自定义条件

```java
//判断是否是Windows系统
public class WinCondition implements Condition {
    /**
     * @param context :判断条件使用的上下文环境
     * @param metadata :注释信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取ioc使用的BeanFactory
        ConfigurableListableBeanFactory factory = context.getBeanFactory();
        //获取类加载器
        ClassLoader loader = context.getClassLoader();
        //获取bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        //获取运行环境
        Environment environment = context.getEnvironment();
        //获取系统名称
        String osname = environment.getProperty("os.name");
        if(osname.contains("Windows")){
            return true;
        }else{
            return false;
        }
    }
}
```

```java
//判断是否是linux系统
public class LinuxConditon implements Condition {
    /**
     * 定义过滤规则
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取运行环境
        Environment environment = context.getEnvironment();
        //获取系统名称
        String osname = environment.getProperty("os.name");
        if(osname.contains("Linux")){
            return true;
        }else{
            return false;
        }
    }
}
```

```java
@Bean("bill")
@Conditional(value = {WinCondition.class})
public Person person01(){
    return new Person("2","Bill Gates",60);
}

@Bean("linus")
@Conditional(value = {LinuxConditon.class})
public Person person02(){
    return new Person("3","linus",50);
}
```

## 1.5.`@Import`

### 1.5.1.给容器注册组件的方式

* 包扫描+组件标注注解`(@Controller/@Repository/@Service/@Component)`【自己定义的类】
* `@Bean+new Person()`【导入第三方包中的组件】
* `@Import`
  * 快速导入组件
  * `ImportSeletor`返回导入组件的全类名的数组
  * `ImportBeanDefinitionRegistrar`:自己定义注册的`bean`

```java
{@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
```

```java
//简单快速导入
@Import(Color.class)  //bean的id默认是类的全类名（com.xy.bean.Color）
public class MainConfig {
}
```

```java
@Configuration
//导入多个类
@Import({Color.class, Red.class})
public class MainConfig {
}
```

* 使用`Spring`提供`FactoryBean(工厂bean)`

### 1.5.2.自定义导入选择器

```java
public class MyImportSelector implements ImportSelector {
    /**
     * @param importingClassMetadata : 当前标注@Import注解的全部注解信息
     * @return 要导入到容器中组件的全类名,不要返回null
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //条件:注入了com.xy.Color，就添加颜色
        String[] imports = null;
        //判断是否包含@Import注解
        if (importingClassMetadata.hasAnnotation(Import.class.getName())){
            //获取注解的属性
            Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(Import.class.getName());
            //获取@Import的value中
            Class[] clazzs = (Class[]) attributes.get("value");
            //判断是否注入了Color
            for(Class clazz : clazzs){
                if (Color.class.getName().equals(clazz.getName())){
                    imports = new String[]{Blue.class.getName(), Red.class.getName(), Yellow.class.getName()};
                    break;
                }else{
                    imports = new String[]{};
                }
            }
        }
        return imports;
    }
}
```

```java
@Configuration
@Import({Color.class,MyImportSelector.class})
public class MainConfig {
}
```

### 1.5.3.自定义`ImportBeanDefinitionRegistrar`

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * @param importingClassMetadata :当前类的注册信息
     * @param registry : bean的注册类（registry.registerBeanDefinition()注册bean）
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //Import注册bean默认id是全类名
        boolean isRed = registry.containsBeanDefinition("com.xy.bean.Red");
        boolean isBlue = registry.containsBeanDefinition("com.xy.bean.Blue");
        //若包含red和blue就注册彩虹类
        if (isBlue && isRed){
            //bean的定义信息（bean的类型，bean的作用域）
            BeanDefinition definition = new RootBeanDefinition(RainBow.class);
            //注册bean到容器中
            registry.registerBeanDefinition("rainBow",definition);
        }
    }
}
```

```java
@Configuration
@Import({MyImportBeanDefinitionRegistrar.class})
public class MainConfig {
}
```

### 1.5.4.`FactoryBean`注册`Bean`

```java
public class Color {
}
```

```java
//注册Color到Spring的IOC容器
public class ColrFactoryBean implements FactoryBean<Color> {

    //返回加载到容器中的对象
    @Override
    public Color getObject() throws Exception {
        return new Color();
    }

    //返回bean的类型
    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }

    //设置bean加载到容器是否是单实例;[true]:单实例，[false]:多实例
    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

```java
//加载bean
@Configuration
public class MainConfig {
    @Bean
    public ColorFactoryBean colorFactoryBean(){
        return new ColorFactoryBean();
    }
}    
```

```java
@Test
public void test(){
    //获取厂bean的getObject对象
    Object bean = context.getBean("colorFactoryBean");
    System.out.println(bean.getClass()); //class com.xy.bean.Color
    //获取工厂bean的本身
    Object contextBean = context.getBean("&colorFactoryBean");
    System.out.println(contextBean.getClass());//class com.xy.config.ColorFactoryBean
}
```

获取工厂`bean`本身使用`&`的原因：

```java
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 */
	String FACTORY_BEAN_PREFIX = "&";
}
```

## 1.6.`@Value`

* 基本数值
* `SpEl,#{}`
* `${}`，取出配置文件中的值，即取运行环境变量的值
* 

## 1.7.`@RequestMapping`

设置控制器（`Controller`）可以处理那些`URL`请求：

* 在类定义处设置：提供初步的请求映射信息 ，相对于`WEB `应用的根目 

* 在方法定义处设置：提供进一步的细分映射信息 ，相对于类定义处的`URL； `

  若类定义处未标注`@RequestMapping`，则方法处标记的`URL `相对于`WEB `应用的根目录 

`DispatcherServlet `截获请求后，就通过控制器上`@RequestMapping` 提供的映射信息确定请求所对应的处理方法。



## 1.8.定义组合注解





## 1.9.`@Repeatable`



# 二、`Bean`的生命周期

## 2.1.自定义初始化和销毁方法

初始化方法在对象创建完成并赋值完成之后，调用初始化方法。

销毁方法调用的时机

* 单实例：容器关闭的时候执行
* 多实例：容器不会管理销毁方法

### 2.1.1.在配置文件中的指定

```xml
<bean id="bookService" init-method="" destory-method="">
</bean>
```

### 2.1.2.`@Bean`指定初始化和销毁方法

```java
public class Car {

    public Car() {
        System.out.println("Car...Constructor");
    }

    public void init(){
        System.out.println("Car...init()");
    }

    public void destory(){
        System.out.println("Car...destory()");
    }
} 
```

```java
@Configuration
public class MainOfLefeCycleConfig {

    @Bean(initMethod = "init",destroyMethod = "destory")
    public Car car(){
        return new Car();
    }

}
```

```java
@Test
public void test(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainOfLefeCycleConfig.class);
    System.out.println("IOC容器的创建...");

    System.out.println("关闭IOC容器...");
    context.close();
}
```

```java
Car...Constructor
Car...init()
IOC容器的创建...
关闭IOC容器...
Car...destory()
```

### 2.1.3.实现接口的方式定义初始化和销毁方法

```java
public class Cat implements InitializingBean,DisposableBean {
    public Cat() {
        System.out.println("Cat...Constructor");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Cat...destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Cat...init()");
    }
}
```

```java
@Configuration
public class MainOfLefeCycleConfig {
    @Bean
    public Cat cat(){
        return new Cat();
    }
}
```

```java
@Test
public void test(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainOfLefeCycleConfig.class);
    System.out.println("IOC容器的创建...");

    System.out.println("关闭IOC容器...");
    context.close();
}
```

```java
Cat...Constructor
Cat...init()
IOC容器的创建...
关闭IOC容器...
Cat...destroy()
```

### 2.1.4.`JS250`的注解定义初始化和销毁方法

```java
public class Cat {
    public Cat() {
        System.out.println("Cat...Constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("Cat...@PostConstruct");
    }

    @PreDestroy
    public void destory(){
        System.out.println("Cat...@PreDestroy");
    }

}
```

```java
@Configuration
public class MainOfLefeCycleConfig {
    @Bean
    public Cat cat(){
        return new Cat();
    }
}
```

```java
@Test
public void test(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainOfLefeCycleConfig.class);
    System.out.println("IOC容器的创建...");

    System.out.println("关闭IOC容器...");
    context.close();
}
```

```java
Cat...Constructor
Cat...@PostConstruct
IOC容器的创建...
关闭IOC容器...
Cat...@PreDestroy
```

### 2.1.5.`BeanPostProcessor`后置处理器

```java
@Component
public class MyBeanPostPreocessor implements BeanPostProcessor {

    /**
     * @param bean :刚创建的bean的实例
     * @param beanName :刚创建bean的名字
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization..."+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization..."+beanName);
        return bean;
    }
}
```

```java
public class Cat {
    public Cat() {
        System.out.println("Cat...Constructor");
    }

    @PostConstruct
    public void init(){
        System.out.println("Cat...@PostConstruct");
    }

    @PreDestroy
    public void destory(){
        System.out.println("Cat...@PreDestroy");
    }

}
```

```java
@Configuration
@ComponentScan(value = {"com.xy"},includeFilters = {
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,classes={Cat.class,MyBeanPostPreocessor.class})
},useDefaultFilters = false)
public class MainOfLefeCycleConfig {
}
```

```java
@Test
public void test(){
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainOfLefeCycleConfig.class);
    System.out.println("IOC容器的创建...");
    
    System.out.println("关闭IOC容器...");
    context.close();
}
```

```java
postProcessBeforeInitialization...org.springframework.context.event.internalEventListenerProcessor
postProcessAfterInitialization...org.springframework.context.event.internalEventListenerProcessor
postProcessBeforeInitialization...org.springframework.context.event.internalEventListenerFactory
postProcessAfterInitialization...org.springframework.context.event.internalEventListenerFactory
Cat...Constructor
postProcessBeforeInitialization...cat   //在初始化方法之前调用
Cat...@PostConstruct
postProcessAfterInitialization...cat   //在初始化方法之后调用

IOC容器的创建...
关闭IOC容器...
Cat...@PreDestroy
```

### 2.1.6.`BeanPostProcessor`的原理(源码)分析

