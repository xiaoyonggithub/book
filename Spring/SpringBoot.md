
| 注解                       | 描述                                                 |
| -------------------------- | ---------------------------------------------------- |
| `@SpringBootApplication`   | `SpringBoot`的主配置类，标注这是一个`SpringBoot`应用 |
| `@Configuration`           | 标注为配置类                                         |
| `@EnableAutoConfiguration` | 开启自动配置                                         |
|                            |                                                      |
|                            |                                                      |
|                            |                                                      |
|                            |                                                      |
|                            |                                                      |
|                            |                                                      |

#     一、SpringBoot

## 1、`@Value`与`@ConfigurationProperties`的区别

|      | @ConfigurationProperties | @Value |
| ---- | ------------------------ | ------ |
|      |                          |        |
|      |                          |        |
|      |                          |        |



## 2、`@PropertySource`

`@ConfigurationProperties`默认从全局配置文件中获取值，`@PropertySource`指定加载的配置文件`(.properties或.yml文件)`

```java
@PropertySource(value = {"classpath:person.properties"})
```

## 3、`@ImportResource`

`@ImportResource`导入`spring`的配置文件`(.xml)`，一般在主配置类上引入

```java
@ImportResource(laction = {"classpath:beans.xml"})
```

## 4、`@Profile`



## 5、`SpringBoot`的配置

```properties
server.port = 8081 #配置端口
server.context-path = /boot02 #项目的访问路径

spring.config.laction =  #改变默认配置文件位置，使用命令行的方式指定


spring.thymeleft.cache = false #禁用掉模板的缓存

```



## 6、`SpringBoot`配置的加载顺序





## 7、包扫描

```xml
<context:compent-scan base-package="com.example"></context:compent-scan>
@CompentScan
```





```javascript
ApplicationContext
AnnotationConfigApplicationContext
ClassXmlPathApplicationContext
```

