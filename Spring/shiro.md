## 一、`Shiro`

Apache Shiro是Java的一个安全（权限）框架

### 1.1.`Shiro`的功能点

- `Authentication`:认证
- `Authorization`:授权
- `Session Management`:session管理
- `Cryptography`:加密
- `Web Support`:
- `Caching`:
- `Concurrency`:
- `Testing`:
- `“Run As”`:
- `“Remember Me”`:

![](E:\typora\images\ShiroFeatures.png)

### 1.2.`Shiro`的组件

- `Subject`:当前用户
- `SecurityManager`:
- `Realm`:

![](E:\typora\images\ShiroBasicArchitecture.png)

### 1.3.`Shiro`的架构

![](E:\typora\images\ShiroArchitecture.png)





## 二、配置文件



## 三、整合

### 3.1.`spring`与`shiro`整合

1. 依赖的jar 

```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.3.2</version>
</dependency>

<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.25</version>
    <scope>runtime</scope>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-web -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>4.3.20.RELEASE</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>4.3.20.RELEASE</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-aop -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>4.3.20.RELEASE</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-aspects -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>4.3.18.RELEASE</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjrt -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.9</version>
</dependency>
```



