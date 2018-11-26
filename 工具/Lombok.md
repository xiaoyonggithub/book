## 一、`Lombok`

- Lombok可以减少很多重复代码的书写

- Lombok项目是一种自动接通你的编辑器和构建工具的一个Java库

### 1.1. 在IDEA中的安装步骤

Setting –> 选择Plugins选项 –> 选择Browse repositories –> 搜索lombok –> 点击安装 –> 安装完成重启IDEA –> 安装成功

### 1.2.添加依赖

```xml
<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.16.18</version>
    <scope>provided</scope>
</dependency>
```

### 1.3.注解使用

1. `@NonNull`设置在属性上，用于属性的非空检查，会默认是生成一个无参构造

   ```java
   @NonNull
   private String userId;
   ```

   ```java
   @NonNull
   private String userId;
   
   @NonNull
   public String getUserId() {
       return this.userId;
   }
   
   public void setUserId(@NonNull String userId) {
       if (userId == null) {
           throw new NullPointerException("userId");
       } else {
           this.userId = userId;
       }
   }
   ```

2. `@Cleanup`注解在属性前，用来保证分配的资源被释放

   - 在本地变量上使用该注解，任何后续代码都将封装在try/finally中，确保当前作用于中的资源被释放
   - `@Cleanup`默认的清理方法为`close`
   - `value`设置释放资源的方法

   ```java
   public void test(){
       try {
           @Cleanup InputStream is = new FileInputStream("f:\\Appender.png");
           int i = is.available();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   ```

   ```java
   public void test() {
       try {
           FileInputStream is = new FileInputStream("f:\\Appender.png");
   
           try {
               int var2 = is.available();
           } finally {
               if (Collections.singletonList(is).get(0) != null) {
                   is.close();
               }
   
           }
       } catch (IOException var7) {
           var7.printStackTrace();
       }
   
   }
   ```

3. `@Getter/@Setter`设置在类或属性上，为属性提供getter/setter方法和一个默认的午无参构造方法

   > 注意：boolean类型的getter方法为`isSex`而不是`getSex`

   ```java
   @Getter
   @Setter
   private String userId;
   ```

   ```java
   private String userId;
   
   public User() {
   }
   
   public String getUserId() {
       return this.userId;
   }
   
   public void setUserId(String userId) {
       this.userId = userId;
   }
   ```

4. `@ToString`注解在类上，为类提供toString()方法

   - `includeFieldNames`设置是否包含属性名称

   ```java
   //includeFieldNames = false
   public String toString() {
           return "User(" + this.getUserId() + ", " + this.getUsername() + ", " + this.getPassword() + ", " + this.getAge() + ")";
       }
   ```

   ```java
   ////includeFieldNames = true
   public String toString() {
           return "User(userId=" + this.getUserId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", age=" + this.getAge() + ")";
       }
   ```

   - `exclude `设置需要排除的字段
   - `callSuper`设置是否输出父类的属性

   ```java
   User(super=Person(sex=null, address=null), userId=U001, username=null, password=null, age=null
   ```

5. `@EqualsAndHashCode`注解在类上，为类提供equals()和hashcode()方法

   - `callSuper`设置是否继承父类的方法

6. `@Data`注解在类上，是`@ EqualsAndHashCode，@Getter，@Setter、@ToString`的组合注解

   - `staticConstructor`设置提供静态构造方法的名称，且Lombok自动生成的构造方法设置为private

   ```java
   @Data(staticConstructor = "getInstance")
   public class User {
       private String userId;
       private String username;
       private String password;
       private String age;
   }
   ```

   ```java
   public class User {
       private String userId;
       private String username;
       private String password;
       private String age;
   
       private User() {
       }
   
       public static User getInstance() {
           return new User();
       }
       ....
   }
   ```

7. `@Log`注解类上，默认情况下`topic`是标注该注解的类名称

   ```java
   //@CommonsLog
   private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LogExample.class);
   //@JBossLog
   private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(LogExample.class);
   //@Log
   private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LogExample.class.getName());
   //@Log4j
   private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogExample.class);
   //@Log4j2
   private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LogExample.class);
   //@Slf4j
   private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
   //@XSlf4j
   private static final org.slf4j.ext.XLogger log = org.slf4j.ext.XLoggerFactory.getXLogger(LogExample.class);
   ```

8. `@AllArgsConstructor`设置在类上，提供一个全参数的构造方法，默认不提供无参构造

   ```java
   @AllArgsConstructor
   public class User {
       private String userId;
       private String username;
       private String password;
       private String age;
   }
   ```

   ```java
   public class User {
       private String userId;
       private String username;
       private String password;
       private String age;
   
       @ConstructorProperties({"userId", "username", "password", "age"})
       public User(String userId, String username, String password, String age) {
           this.userId = userId;
           this.username = username;
           this.password = password;
           this.age = age;
       }
   }
   ```

9. `@NoArgsConstructor`设置在类上，提供一个无参构造

   ```java
   @NoArgsConstructor
   public class User {
       private String userId;
       private String username;
       private String password;
       private String age;
   }
   ```

   ```java
   public class User {
       private String userId;
       private String username;
       private String password;
       private String age;
   
       public User() {
       }
   }
   ```

10. `@RequiredArgsConstructor`设置在类上，使用所有带`@NonNull`注解和带`final`修饰的属性生成构造方法

11. `@Value`注解在类上，会生成带所有参数的构造方法、get方法、equal、hashCode和toString方法，注意没有set方法

12. `@SneakyThrows`设置在方法上，将方法中的代码用`try-catch`语句包裹起来，捕获异常并在catch中使用Lombok.sneakyThrow(e) 把异常抛出

    - `value`指定抛出那种异常

    - 注意:该注解需要谨慎使用

13. `@Synchronized`设置在类上或实例方法上

    ```java
    @Synchronized
    public String test(){
        return "test";
    }
    ```

    ```java
    public String test() {
        Object var1 = this.$lock;
        synchronized(this.$lock) {
            return "test";
        }
    }
    ```


