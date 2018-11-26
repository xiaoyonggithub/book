## 一、创建对象的方式

```java
import java.io.Serializable;  
  
public class Worker implements Cloneable,Serializable {  
  
    private static final long serialVersionUID = 1L;  
    private String name;  
    private int age;  
      
    public Worker()  
    {  
        this.name = "";  
        this.age = 0;  
    }  
      
    public Worker(String name,int age)  
    {  
        this.name = name;  
        this.age = age;  
    }  
      
    public void work()  
    {  
        System.out.println(name +"is working");  
    }  
      
    public Worker clone()  
    {  
        Worker worker = null;  
        try {  
            return (Worker) super.clone();  
        } catch (CloneNotSupportedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return worker;  
    }  
} 
```

```java
  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.ObjectInput;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.lang.reflect.Constructor;  
import java.lang.reflect.InvocationTargetException;  
  
public class CreateInstanceUtil {  
  
    /* 
     * 方式1： 直接使用new的方式,不使用参数 
     */  
    public static Worker createWorker() {  
        return new Worker();  
    }  
  
    /* 
     * 方式2： 使用new方式，带参数 
     */  
    public static Worker createWorker(String name, int age) {  
        return new Worker(name, age);  
    }  
  
    /* 
     * 方式3： 使用反射机制，不带参数 Class 對象的 newInstance() 方法 
     */  
    public static Worker createWorker1() {  
  
        Class clazz = null;  
        Worker worker = null;  
        try {  
            clazz = Class.forName("com.lou.creation.Worker");  
            worker = (Worker) clazz.newInstance();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
  
        return worker;  
    }  
  
    /* 
     * 方式4： 使用反射机制 ， Constructor的 newInstance方法 
     */  
    public static Worker createWorker2() {  
        Worker worker = null;  
        try {  
            Class clazz = null;  
            clazz = Class.forName("com.lou.creation.Worker");  
  
            // 获取不带参数的构造器  
            Constructor constructor = clazz.getConstructor();  
            // 使用构造器创建对象  
            worker = (Worker) constructor.newInstance();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return worker;  
    }  
  
    /* 
     * 方式5： 使用反射机制 ：带参数的构造函数创建新对象 
     */  
    public static Worker createWorker3(String name, Integer age) {  
        Worker worker = null;  
        try {  
            Class clazz = null;  
            clazz = Class.forName("com.lou.creation.Worker");  
  
            // 获取不带参数的构造器  
            Constructor constructor = clazz.getConstructor(name.getClass(),  
                    age.getClass());  
            // 使用构造器创建对象  
            worker = (Worker) constructor.newInstance(name, age);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return worker;  
    }  
  
    /* 
     * 方式6： 使用序列化和反序列化创建对象，这种方式其实是根据既有的对象进行复制，这个需要事先将可序列化的对象线存到文件里 
     */  
    @SuppressWarnings("resource")  
    public static Worker createWorker4(String objectPath) {  
        ObjectInput input = null;  
        Worker worker = null;  
        try {  
            input = new ObjectInputStream(new FileInputStream(objectPath));  
            worker = (Worker) input.readObject();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return worker;  
    }  
  
    /* 
     * 将创建的对象存入到文件内 
     */  
    public static void storeObject2File(String objectPath) {  
        Worker worker = new Worker();  
        ObjectOutputStream objectOutputStream;  
        try {  
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(  
                    objectPath));  
            objectOutputStream.writeObject(worker);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
    }  
  
    /* 
     * 方式7， 使用对象的 深复制进行复制，创建对象 
     */  
    public static Worker createWorker5(Worker worker) {  
        return (Worker) worker.clone();  
    }  
        
}  
```

## 二、转发和重定向

#### 2.1.转发

```java
request.getRequestDispatcher("/student_list.jsp").forward(request, response);
```

请求转发是服务器内部把对一个`request/response`的处理权，移交给另外一个对于客户端而言,它只知道自己最早请求的那个A，而不知道中间的B，甚至C、D。 **传输的信息不会丢失** 。

#### 2.2.重定向

```java
response.sendRedirect(request.getContextPath() + "/student_list.jsp");
```

#### 2.3.转发与重定向的区别

| 转发                           | 重定向                         |
| ------------------------------ | ------------------------------ |
| 转发在服务器端完成的           | 重定向是在客户端完成的         |
| 转发的速度快                   | 重定向速度慢                   |
| 转发的是同一次请求             | 重定向是两次不同请求           |
| 转发不会执行转发后的代码       | 重定向会执行重定向之后的代码   |
| 转发地址栏没有变化             | 重定向地址栏有变化             |
| 转发必须是在同一台服务器下完成 | 重定向可以在不同的服务器下完成 |

## 三、读取属性文件（Properties）

`ResourceBundle`和`Properties`都能读取属性文件,它们的区别：

* `Properties`继承与Hashtable,是基于输入流从属性文件中读取键值对，load()加载资源完毕后，就与输入流脱离了关系，但是不会自动关闭输入流，需手动关闭;
* `ResourceBundle`是基于类读取属性文件，即将属性文件当作类，因此属性文件必须放在包中，使用属性文件的全限定类名指定文件的位置;
* 属性文件采用ISO-8859-1编码方式，该编码方式不支持中文，中文字符将被转化为Unicode编码方式显示;



## 四、`<%@include>`与`<jsp:include>`的区别

#### 4.1.`<%@include>`

```jsp
<%@ include file="relativeURL" %> 
```

处理流程：先将所有包含的文件合并到主文件中,然后一起转化成servlet后在编译为class;

注意：主文件和被包含文件其实是一个文件，不能定义重复的变量;

#### 4.2.`<jsp:include>`

include动作用于页面请求时引入指定文件,[flush="true"]设置读入被保存文件内容前是否清空缓存;

```jsp
<jsp:include page="relativeURL" flush="true" /> 
```

处理流程：若引入的文件是JSP文件，则先编译该JSP程序，然后再把编译的结果引入主文件;

include动作使用`request.getRequestDispatcher("relativeURL").forward(request,response);`来引入被包含文件;include动作会自动检查被包含文件的变化,也就是在每次客户端发出请求时会重新把资源包含进来，进行实时的更新;

#### 4.3.总结

* include指令是“先包含，后编译”，在编译时主文件已经包含被include的文件内容（即源代码);
* include动作是“先运行，后包含”，在运行时主文件才包含被include的文件运行结果 

## 五、instanceof

`instanceof`判断运行时的对象是否是特定类的一个实例

```java
obj instanceof class
```

* 若obj是class的一个实例且obj!=null,则返回true
* 若obj不是class的一个实例或obj==null,则返回false

### 5.1.instanceof在编译状态和运行状态的区别

* #### 编译状态

class可以是obj对象的父类、自身类、子类，都能通过编译

* #### 运行状态

class是obj对象的父类和自身类时返回true

## 六、JSON数据的处理

### 6.2.Java对象转Json

```java
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

User user = new User();
String json = JSONObject.toJSONString(user);
```

### 6.3.Json转Java对象

```java
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

User user = JSONObject.parseObject(json, User.class);
```

### 6.4.Map转Json

#### 6.4.1.`json-lib`

```java
<dependency>
    <groupId>net.sf.json-lib</groupId>
    <artifactId>json-lib</artifactId>
    <version>2.4</version>
    <classifier>jdk15</classifier>
</dependency>
```

```java
JSONObject jsonObject = JSONObject.fromObject(map);
//json-lib是一个比较老的解决方案，近几年都没有升级过，它的适用环境是JDK1.5,在JDK1.6可能会报错，故需加上<classifier>jdk15</classifier>
```

#### 6.4.2.`com.alibaba.fastjson.JSON`

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.41</version>
</dependency>
```

```java
JSONUtils.toJSONString(requestMap);
```

#### 6.4.3.`gson`

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.3.1</version>
</dependency>
```

```java
new Gson().toJson(param);
```

#### 6.4.4.`jackson`

```xml
jackson-core-2.6.0.jar
jackson-databind-2.6.0.jar
jackson-annotations-2.6.0.jar
```

```java
ObjectMapper mapper = new ObjectMapper();
json = mapper.writeValueAsString(map);
```

### 6.5.Json转Map

#### 6.5.1.`com.alibaba.fastjson.JSON`

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.41</version>
</dependency>
```

```java
String str = "{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":\"maliu\"}";
//第一种方式
Map maps = (Map)JSON.parse(str);
/第二种方式
Map mapTypes = JSON.parseObject(str);
//第三种方式
Map mapType = JSON.parseObject(str,Map.class);
//第四种方式 JSONObject是Map接口的一个实现类
Map json = (Map) JSONObject.parse(str);
//第五种方式
JSONObject jsonObject = JSONObject.parseObject(str);
//第六种方式
Map mapObj = JSONObject.parseObject(str,Map.class);
```

#### 6.5.2.`jackson`

```xml
jackson-core-2.6.0.jar
jackson-databind-2.6.0.jar
jackson-annotations-2.6.0.jar
```

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

String json = "{\"name\":\"zitong\", \"age\":\"26\"}";
ObjectMapper mapper = new ObjectMapper();
map = mapper.readValue(json, new TypeReference<HashMap<String,String>>(){});
```

### 6.6.`List`与`JSON`之间的转化

```java
import net.sf.json.JSONArray;
//list转成json
String json =JSONArray.fromObject(list).toString();

//json转成list
JSONArray jsonArray = JSONArray.fromObject(json);
List<String> list2 = (List) JSONArray.toCollection(jsonArray);
```

```java
//fastjson.jar
import com.alibaba.fastjson.JSON;
//list转json
String json = JSON.toJSON(list).toString();

//json转list
List<User> users= JSONObject.parseArray(json, User.class);
```

