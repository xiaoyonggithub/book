# 一、转发和重定向

## 1.1.转发

```java
request.getRequestDispatcher("/student_list.jsp").forward(request, response);
```

请求转发是服务器内部把对一个`request/response`的处理权，移交给另外一个对于客户端而言,它只知道自己最早请求的那个A，而不知道中间的B，甚至C、D。 **传输的信息不会丢失** 。

## 1.2.重定向

```java
response.sendRedirect(request.getContextPath() + "/student_list.jsp");
```

## 1.3.转发与重定向的区别

| 转发                           | 重定向                         |
| ------------------------------ | ------------------------------ |
| 转发在服务器端完成的           | 重定向是在客户端完成的         |
| 转发的速度快                   | 重定向速度慢                   |
| 转发的是同一次请求             | 重定向是两次不同请求           |
| 转发不会执行转发后的代码       | 重定向会执行重定向之后的代码   |
| 转发地址栏没有变化             | 重定向地址栏有变化             |
| 转发必须是在同一台服务器下完成 | 重定向可以在不同的服务器下完成 |

# 四、`<%@include>`与`<jsp:include>`的区别

## 4.1.`<%@include>`

```jsp
<%@ include file="relativeURL" %> 
```

处理流程：先将所有包含的文件合并到主文件中,然后一起转化成servlet后在编译为class;

注意：主文件和被包含文件其实是一个文件，不能定义重复的变量;

## 4.2.`<jsp:include>`

include动作用于页面请求时引入指定文件,[flush="true"]设置读入被保存文件内容前是否清空缓存;

```jsp
<jsp:include page="relativeURL" flush="true" /> 
```

处理流程：若引入的文件是JSP文件，则先编译该JSP程序，然后再把编译的结果引入主文件;

include动作使用`request.getRequestDispatcher("relativeURL").forward(request,response);`来引入被包含文件;include动作会自动检查被包含文件的变化,也就是在每次客户端发出请求时会重新把资源包含进来，进行实时的更新;

## 4.3.总结

- include指令是“先包含，后编译”，在编译时主文件已经包含被include的文件内容（即源代码);
- include动作是“先运行，后包含”，在运行时主文件才包含被include的文件运行结果 

