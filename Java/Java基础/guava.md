# 一、快速开始

## 1.1.引入jar

```xml
<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>27.0.1-jre</version>
</dependency>
```



# 二、`Optional`

避免使用`null`，因为`null`是模棱两可的，会引起令人困惑的错误；它可能表示已经有一个默认值，或没有值，或找不到值。





# 二、`Fluent Interface`

```java
//原来的
User user = new User();
user.setUserId("u001");
user.setUsername("张三");
user.setAge(20);
userService.query(user);
```

```java
//fluent
Users user = new Users()
    .setUserId("u002")
    .setUsername("张三")
    .setAge(32)
    .setPwd("122345");
userService.query(user);
```



# 三、`Ordering`

