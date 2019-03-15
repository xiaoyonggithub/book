





# 二十、Spring依赖的jar分类

# 20.1.AOP依赖的jar

- cglib动态代理依赖的jar 
- aspectj依赖相关的jar,用来支持切面编程
  - aspectjrt是aspectj的runtime包 
  - aspectjweaver是aspectj的织入包 

```xml
<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjrt -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.1</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.1</version>
</dependency>

<!-- https://mvnrepository.com/artifact/cglib/cglib -->
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.2.7</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-aop -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>4.3.18.RELEASE</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-aspects -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>4.3.18.RELEASE</version>
</dependency>
```





# 二十一、常见问题

## 21.1.MySQL数据库版本描述

```xml
Mon Jul 23 21:52:58 CST 2018 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
```

方式一：

```xml
<value>jdbc:mysql://localhost:3306/scott?characterEncoding=utf8;useSSL=true;createDatabaseIfNotExist=true</value>
```

方式二：

```xml
<value>jdbc:mysql://localhost:3306/scott?characterEncoding=utf8&useSSL=true&createDatabaseIfNotExist=true</value>
```

## 21.2.通配符问题

###### 错误：通配符的匹配很全面, 但无法找到元素 'aop:aspectj-autoproxy' 的声明。

```xml
http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
```