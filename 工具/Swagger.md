

# 一、`Swagger`

## 1.1.主要的子项目

- `Swagger-tools`:提供各种与Swagger进行集成和交互的工具；如模式检验、Swagger 1.2文档转换成Swagger 2.0文档等
- `Swagger-core`:用于Java/Scala的的Swagger实现；与JAX-RS(Jersey、Resteasy、CXF...)、Servlets和Play框架进行集成
- `Swagger-js`:用于JavaScript的Swagger实现
- `Swagger-node-express`:Swagger模块，用于node.js的Express web应用框架
- `Swagger-ui`:一个无依赖的HTML、JS和CSS集合，可以为Swagger兼容API动态生成优雅文档

- `Swagger-codegen`:一个模板驱动引擎，通过分析用户Swagger资源声明以各种语言生成客户端代码



## 1.2.主要注解

- `@Api`:用在类上，说明的类的作用
- `@ApiOperation`:给API增加方法说明
- `@ApiParam`:
- `@ApiResponse`:
- `@ApiResponses`:
- `@ResponseHeader`:



## 1.3.依赖引入

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.2.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.2.2</version>
</dependency>
```



## 1.4.配置类

```java

```

