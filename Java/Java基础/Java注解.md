# 一、注解

| 注解                 | 描述                                         |
| -------------------- | -------------------------------------------- |
| `@Target`            | 设置注解的作用范围                           |
| `@Retention`         | 设置在什么级别保存注解信息，即保留时间的长短 |
| `@Documented`        | 将注解包含在`doc`中，标记注解                |
| `@Inherited`         | 允许子类继承父类的注解，标记注解             |
| ` @Override `        | 表示当前方法定义可覆盖超类中的方法，标记注解 |
| ` @Deprecated `      | 设置方法不建议使用，标记注解                 |
| ` @SuppressWarnings` | 告诉编译器忽略特定的警告信息                 |
| ``                   |                                              |
|                      |                                              |

## 1.1.``@Target``

`@Target`设置注解作用范围，即可标注的位置，通过`ElementType`设置：

```java
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
    String[] value();
}
```

| `@Target`取值`ElementType` | 描述                                 |
| -------------------------- | ------------------------------------ |
| `TYPE`                     | 类、接口（包括注释类型）或`enum`声明 |
| `FIELD`                    | 字段声明（包括`enum`常量）           |
| `METHOD`                   | 方法声明                             |
| `PARAMETER`                | 参数声明                             |
| `CONSTRUCTOR`              | 构造方法声明                         |
| `LOCAL_VARIABLE`           | 本地变量声明                         |
| `ANNOTATION_TYPE`          | 注解类型声明                         |
| `PACKAGE`                  | 包 声明                              |
| `TYPE_PARAMETER`           | 类型参数声明 `@since 1.8`            |
| `TYPE_USE`                 | 使用的类型 `@since 1.8`              |

## 1.2.``@Retention``

`@Retention`设置需要在什么级别保存该注解信息 ，通过`RetentionPolicy`设置：

| `RetentionPolicy`取值 | 描述                                                         |
| --------------------- | ------------------------------------------------------------ |
| `SOURCE`              | 注释将被编译器丢弃                                           |
| `CLASS`               | 注解在`class`文件中可用，但会被`VM`丢弃                      |
| `RUNTIME`             | `VM`将在运行期间保留注解，因此可以通过反射机制读取注解的信息 |

## 1.3.`@Repeatable`

`@Repeatable`表明标记的注解可以多次应用



## 1.4.`@SuppressWarnings`

`@SuppressWarnings`用于抑制编译器产生警告信息

| 取值                          | 描述                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| ` all `                       | 抑制所有警告                                                 |
| ` boxing `                    | 要抑制与箱/非装箱操作相关的警告                              |
| ` cast `                      | 为了抑制与铸造操作相关的警告                                 |
| ` dep-ann `                   | 要抑制相对于弃用注释的警告                                   |
| ` deprecation `               | 使用了不赞成使用的类或方法时的警告；                         |
| ` fallthrough  `              | 在`switch`语句中，抑制与缺失中断相关的警告                   |
| ` finally `                   | 任何 `finally`子句不能正常完成时的警告;                      |
| ` hiding `                    | 为了抑制相对于当地的警告，隐藏变量                           |
| ` incomplete-switch  `        | 当 `Switch`程序块直接通往下一种情况而没有 `Break`时的警告;   |
| ` nls `                       | 要抑制相对于非`nls`字符串字面量的警告                        |
| ` null `                      | 为了抑制与`null`分析相关的警告                               |
| ` rawtypes `                  | 在类`params`上使用泛型时，要抑制相对于非特异性类型的警告     |
| ` restriction `               | 禁止使用警告或禁止引用的警告                                 |
| ` serial `                    | 当在可序列化的类上缺少 `serialVersionUID`定义时的警告;       |
| ` static-access  `            | 相对于不正确的静态访问，抑制警告                             |
| ` synthetic-access   `        | 相对于内部类的未优化访问，来抑制警告                         |
| ` unchecked `                 | 执行了未检查的转换时的警告，例如当使用集合时没有用泛型 (Generics) 来指定集合保存的类型; |
| ` unqualified-field-access  ` | 为了抑制与现场访问相关的警告                                 |
| ` unused `                    | 要抑制与未使用代码相关的警告，去除感叹号                     |
| ` path `                      | 在类路径、源文件路径等中有不存在的路径时的警告;              |

## 1.5.`@Documented`

`@Documented` 注解表明，无论何时使用指定的注释，都应该使用Javadoc工具对这些元素进行文档化

```java
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Metadata{
}
```

使用`@Metadata`注解时，会使用Javadoc工具对这些元素进行文档化

## 1.6.`@Inherited`

`@Inherited`允许子类继承父类的注解，不是子注解类型继承符父注解类型

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited  //允许子类继承父类的注解
public @interface SuperAnnotation {
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SubAnnotation {

}
```

```java
@SuperAnnotation
public class Super {

}
```

```java
@SubAnnotation
public class Sub extends Super{
}
```

```java
public static void main(String[] args) {
    Annotation[] annotations = Super.class.getAnnotations();
    Arrays.stream(annotations).forEach(System.out::println);
    System.out.println("-----------------------------");
    Annotation[] anns= Sub.class.getAnnotations();
    Arrays.stream(anns).forEach(System.out::println);
}
```

```java
@com.example.demo.annotation.SuperAnnotation()
-----------------------------
@com.example.demo.annotation.SuperAnnotation()
@com.example.demo.annotation.SubAnnotation()
//子类Sub继承了父类Super的注解类型
```

## 1.7.`@Deprecated`

`@Deprecated`表示方法或类不再建议使用，在新版本中有其他方法或类可以代替这个使用，以后的版本也不会再更新这个方法或类

