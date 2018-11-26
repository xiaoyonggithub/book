1.`java.lang.ClassNotFoundException: org.apache.taglibs.standard.tlv.JstlCoreTLV`

分析原因：引入进来的jsp-api.jar包和tomcat中的jar冲突的了

解决方法：删掉引入进来的sp-api.jar就可以

2.el表达式不解析原样输出，不被解析

![1531404662145](E:\typora\images\1531404662145.png)

分析原因：servlet3.0默认关闭了el表达式的解析 

解决方法：在page指令中添加isELIgnored="false"

```jsp
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
```

3.`org.apache.tomcat.util.bcel.classfile.ClassFormatException: Invalid byte tag in constant pool: 15 `

分析原因：jdk与tomcat的版本不一致

解决方法：使用相同版本的jdk和tomcat

```xml
<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" metadata-complete="true">
```

