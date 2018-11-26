# 一、`Bean`的管理

## 1.1.`bean`的作用域

【`singleton`】

* 在`Spring IOC`容器中仅存在一个`Bean`实例，`bean`以单例方式存在，默认；
* 在创建容器时就会同时自动创建一个`Bean `的实例，不论你是否使用。

【`prototype`】

* 从容器中调用`Bean`时，都返回一个新的实例；
* 每次对`Bean`请求（将其注入到另一个`Bean`中，或调用容器的`getBean()`）时，都会创建一个`Bean`；
* 对有状态的`Bean`使用`prototype`，对没有状态的`Bean`使用`singleton`。

【`request`】：每次`HTTP`请求都会创建一个新的`Bean`，仅适用于`WebApplicationContext`环境。

【`session`】：同一个`HTTP Session`共享一个`Bean`，不同的`Session`使用不同的`Bean`，仅适用于`WebApplicationContext`环境。

【`globalSession`】：一般用于`Portlet`应用环境，仅适用于`WebApplicationContext`环境。

![](E:\typora\images\微信图片_20180607150112.png)

## 1.2.`Bean`的生命周期

### 1.2.1.`Bean`实例化过程

* `Spring`对`Bean`进行实例化，默认`Bean`是单例的；

* `Spring`对`Bean`进行依赖注入；

* 若`Bean`实现了`BeanNameAware`接口，`Spring`将`Bean`的`id`传给`setBeanName()`方法；

* 若`Bean`实现了`BeanFactoryAware`接口，`Spring `将调用`setBeanFactory()`方法，将`BeanFactory`实例传进来；

* 若`Bean`实现了`BeanPostProcessor`接口，会调用它的`postProcessBeforeInitialization()`方法；

* 若`Bean`实现了`InitializingBean`接口，将调用它的`afterPropertiesSet()`接口方法；

  类似的若`Bean`使用了`init-method`属性声明初始化方法，同样会调用`afterPropertiesSet()`接口方法；

* 若`Bean`实现了`BeanPostProcessor`接口，会调用它的`postProcessAfterInitialization()`方法；

* 此时`Bean`就已经准备就绪，可被应用程序使用，它们将一直驻留在应用上下文中，直到该应用上下文被销毁。

* 若`Bean`实现了`DisposableBean`接口，将调用它的`distory()`方法；

  若`Bean`使用了`destory-method`属性销毁方法，同样将会调用`distory（）`方法。

![](E:\typora\images\微信图片_20180607150108.png)

![](E:\typora\images\微信图片_20180607150041.png)

### 1.2.2.`singleton`对象的管理

* `scope="singleton"`默认在启动容器（初始化容器）时初始化
* `lazy-init="true"`来延迟加载`bean`,此时只有第一次获取`bean`才会初始化`bean`

```xml
<bean id="serviceImpl" clas="cn.xy.service.ServiceImpl" lazy-init="true"></bean>
```

* 对所有默认的单例`bean`都应用延迟初始化

```xml
<bean default-lazy-init="true"></bean>
```

* `spring`读取`xml`文件时创建对象流程

[调用构造方法] `-->`[调用`init-method`的方法]`-->`[调用`destory-method`的方法]

### 1.2.3.非单例对象（`prototype`、`request`、`session`）的管理

* 请求`Bean`的流程

[调用构造方法]`-->`[调用`init-method`的方法]

* 对象销毁时，不会调用任何销毁方法，即使指定了`destory-method`；

* 清除`prototype`作用域对象，并释放被`prototype bean`占用的资源，都是客户端的职责

* 释放`prototype`作用域`bean`占用的资源，可行方式使用`bean`的后置处理器

  原因：后置处理器持有要被清除`bean`的引用

