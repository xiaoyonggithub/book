### 1、判空的区别

* `isNull`;判断参数为`null`
* `isNotNull`;判断参数不为`null`
* `isEmpty`;判断参数为`null`或空（空字符串或`Collectoin`不为空）
* `isNotEmpty`;判断参数不为`null`或空（空字符串或`Collectoin`不为空）
* `isPropertyAvailable `;判断入参中有指定的参数时，有效
* `isNotPropertyAvailable`;判断入参中没有指定参数时，有效
* `isEqual`;相当于`equal`,常用于判断状态值
* `isNotEqual`;不等于指定的值时，有效
* `isGreaterThan`;大于指定的值，有效
* `isGreaterEqual`;大于等于指定值，有效
* `isLessEqual`;小于指定值，有效
* `isLessEqual `;小于等于指定的值，有效
* `isParameterPresent`;参数类不为空，有效

### 2、`Mapper`的配置方式

* `resource`

  * Mapper的xml配置文件单独放置到 resources 中，和Mapper类分开了
  * 好处是便于统一管理 xml 配置文件，不好的的地方是无法使用注解模式了 

  ```xml
  <mapper resource="com/example/mapper/StudentMapper.xml"/>
  ```

* `url`

  ```xml
  #路径对应的是网络上了某个文件，注意file:// 前缀 +路径+文件名
  <mapper url="file:///var/mappers/BlogMapper.xml"/> 
  ```

* `class`

  * 若是非注解模式的话xml配置文件必须和这个类在同一级目录，且与Mapper类同名
  * 当然，使用注解模式的话，Mapper.xml文件就没有必要存在了 

  ```xml
  <mapper class="com.mybatis.builder.BlogMapper"/> 
  ```

* `package`

  * Mapper.xml 文件位置必须在和其内部`<mapper namespace="">`的类在一起
  * 当然，使用注解模式的话，Mapper.xml文件就没有必要存在了 

  ```xml
  <mappers> 
  	<package name="com.mybatis.builder"/>
  </mappers>    
  ```

* `spring`下配置`mapper`路径

  ```xml
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">  
          <property name="dataSource" ref="dataSource" />  
          <property name="typeAliasesPackage" value="实体类包路径" />  
          <property name="typeAliasesSuperType" value="实体类顶级包路径" />  
          <property name="mapperLocations" value="classpath:/mybatis/mappings/**/*.xml" />  
          <property name="configLocation" value="classpath:/mybatis/mybatis-config.xml"/>	
   </bean>  
  ```

  ```xml
  <!-- MustConfigPoint 扫描basePackage下所有以@MyBatisDao注解的接口 -->  
  <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
      <property name="basePackage" value="mapper类的包路径" />  
      <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />  
      <property name="annotationClass" value="com.msyd.framework.common.persistence.annotation.MyBatisDao" />  
  </bean>  
  ```

* `Mybatis`中接口和对应的`mapper`文件放在同一个包下,且接口的名称和`mapper`文件的名称要相同,目的是为了`Mybatis`进行自动扫描，就不需要配置`mapper`文件的位置了 

  ```sql
  
  ```

---



### 3、`SqlSession`

* `SqlSesion`表示与数据库的一次会话，用完就要关闭。
* `SqlSeesion`与`Connection`一样都是非线程安全的，故每次使用都需要获取一个新的。
* `SqlSession`不会自动提交，需手动提交

```java
//设置session为自动提交
SqlSession session = factory.openSession(true);
//SqlSession openSession(boolean autoCommit);
```

> 注意：定义`POJO`对象时，一定要提供无参构造方法，因为很多框架都是通过无参构造器反射创建对象的。

### 4、`mybatis-config.xml`

包含数据库连接池的信息、事务管理信息和系统运行环境信息等

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!--配置运行环境-->
    <environments default="development">

        <!--mysql的运行环境-->
        <environment id="development">
            <!--事务管理器-->
            <transactionManager type="JDBC"/>
            <!--数据源-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/vote"/>
                <property name="username" value="root"/>
                <property name="password" value="1234"/>
            </dataSource>
        </environment>
    </environments>

    <!--映射文件-->
    <mappers>
        <mapper resource="mapper/AdminMapper.xml"/>
    </mappers>
</configuration>

```

![1529936108112](E:\typora\images\1529936108112.png)

#### 4.1.`Properties`

* `properties`：引入外部`properties`配置文件的内容
  * `[url]`：引入网络路径或磁盘路径下的资源
  * `[resource]`：引入类路径下的资源

```xml
<configuration>
     <!--引入数据库的配置文件-->
    <properties resource="config/jdbc.properties"></properties>
    <!--配置运行环境-->
    <environments default="development">
        <!--mysql的运行环境-->
        <environment id="development">
            <!--事务管理器-->
            <transactionManager type="JDBC"/>
            <!--数据源-->
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.Driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
</configuration>
```

#### 4.2.`setting`

* `settings`

| `setting`                          | 描述                                                         | 取值                 | 默认值    |
| ---------------------------------- | ------------------------------------------------------------ | -------------------- | --------- |
| `cacheEnabled`                     | 开启二级缓存                                                 | `false/true`         | `true`    |
| `lazyLoadingEnabled`               | 开启懒加载                                                   | `false/true`         | `false`   |
| `aggressiveLazyLoading`            | 开启时任何方法的调用都会加载该对象的所有属性，否则会按需加载，即按需加载 | `false/true`         | `true`    |
| `multipleResultSetsEnabled`        |                                                              |                      |           |
| `useColumnLabel`                   |                                                              |                      |           |
| `useGeneratedKeys`                 |                                                              |                      |           |
| `autoMappingBehavior`              |                                                              |                      |           |
| `autoMappingUnknownColumnBehavior` |                                                              |                      |           |
| `defaultExecutorType`              |                                                              |                      |           |
| `defaultStatementTimeout`          |                                                              |                      |           |
| `defaultFetchSize`                 |                                                              |                      |           |
| `safeRowBoundsEnabled`             |                                                              |                      |           |
| `safeResultHandlerEnabled`         |                                                              |                      |           |
| `mapUnderscoreToCamelCase`         | 是否开启自动驼峰命名规则映射                                 | `true/false`         | `false`   |
| `localCacheScope`                  | 设置本地（一级）缓存作用域                                   | `SESSION/STATEMENT`  | `SESSION` |
| `jdbcTypeForNull`                  | 设置`null`的`jdbcType`类型的取值                             | `OTHER/NULL/VARCHAR` | `OTHER`   |
| `lazyLoadTriggerMethods`           |                                                              |                      |           |
| `defaultScriptingLanguage`         |                                                              |                      |           |
| `callSettersOnNulls`               |                                                              |                      |           |
| `logPrefix`                        |                                                              |                      |           |
| `logImpl`                          |                                                              |                      |           |
| `proxyFactory`                     |                                                              |                      |           |
| `vfsImpl`                          |                                                              |                      |           |
| `useActualParamName`               | 多个参数是否使用参数名取值，`JDK8`才支持                     | `true/false`         |           |

```xml
<settings>
    <!--开启自动驼峰命名规则映射-->
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

> 注意：确定的属性设置都显示配置出来

#### 4.3.`typeAliases`

* `typeAliases`：别名处理器

```xml
<!--别名处理器-->
<typeAliases>
    <!--设置某个Java类型的别名，type:Java的类型的全类名，默认的别名为类名的小写；alias:指定别名-->
    <typeAlias type="com.xy.domain.Admin" alias="admin"></typeAlias>

    <!--批量取别名；name：指定包名；默认的别名为类名的小写-->
    <package name="com.xy.domain"></package>
</typeAliases>
```

> 注意：别名不区分大小写 

若批量取别名时，指定的包下存在同名的类时会报错，此时可使用注解指定一个不同的别名

```java
@Alias("emp")
public class Employee{
    
}
```

> 注意：若使用`<typeAlias>`或`@Alias`指定了别名，它们指定的别名有效，但批量指定的默认别名就失效。
>
> 别名优先级：`mybatis`封装的别名`> <typeAlias>或@Alias >`默认的别名（如批量指定的别名）

`mybatis`默认封装了`Java`类型的别名

* 基本类型加`_`（`int/_int`）
* 包装类型字母小写（`String/string,Long/long`）

> 建议：使用全类名，可方便跳转，查询映射的区别

#### 4.4.`typeHandlers`

* `typeHandlers` ：日期和时间处理器，`JDK1.8`定义`JSR310`规范（日期类型的类库），`Mybatis`可使用基于`JSR310`的各种**日期时间类型的处理器**

```sql

```

> 注意：`Mybatis3.4`之前的版本需要手动注册这些处理器，之后的版本自动注册

![1529940036310](E:\typora\images\1529940036310.png)

#### 4.5.`objectFactoy`

* `objectFactory`

#### 4.6.`plugins`

* `plugins`：拦截一些行为

#### 4.7.`environmnets`

* `environments` ：可配置多个运行环境，动态的切换环境
  * `environment` ：配置具体的环境
    * `transactionManager` ：事务管理器
      *  `[type = "JDBC"]` ：使用`JDBC`的方式控制事务
      * `[type = "MANAGED"]`：使用`JEE`服务容器的方式来控制事务
    * `dataSource`：数据的配置
      * `[type="UNPOOLED"]` :使用非连接池的方式管理连接
      * `[type="POOLED"]`:使用连接池的方式管理连接
      * `[type="JNDI"]`：使用JNDI管理连接池

```Java
//注意：此处的JDBC、MANAGED和POOLED等是别名
public Configuration() {
    //事务管理器
    typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
    typeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class);

    //连接的方式
    typeAliasRegistry.registerAlias("JNDI", JndiDataSourceFactory.class);
    typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);

    //缓存的清除策略
    typeAliasRegistry.registerAlias("PERPETUAL", PerpetualCache.class);
    typeAliasRegistry.registerAlias("FIFO", FifoCache.class);
    typeAliasRegistry.registerAlias("LRU", LruCache.class);
    typeAliasRegistry.registerAlias("SOFT", SoftCache.class);
    typeAliasRegistry.registerAlias("WEAK", WeakCache.class);

    typeAliasRegistry.registerAlias("DB_VENDOR", VendorDatabaseIdProvider.class);

    typeAliasRegistry.registerAlias("XML", XMLLanguageDriver.class);
    typeAliasRegistry.registerAlias("RAW", RawLanguageDriver.class);

    //日志的管理
    typeAliasRegistry.registerAlias("SLF4J", Slf4jImpl.class);
    typeAliasRegistry.registerAlias("COMMONS_LOGGING", JakartaCommonsLoggingImpl.class);
    typeAliasRegistry.registerAlias("LOG4J", Log4jImpl.class);
    typeAliasRegistry.registerAlias("LOG4J2", Log4j2Impl.class);
    typeAliasRegistry.registerAlias("JDK_LOGGING", Jdk14LoggingImpl.class);
    typeAliasRegistry.registerAlias("STDOUT_LOGGING", StdOutImpl.class);
    typeAliasRegistry.registerAlias("NO_LOGGING", NoLoggingImpl.class);

    //代理的方式
    typeAliasRegistry.registerAlias("CGLIB", CglibProxyFactory.class);
    typeAliasRegistry.registerAlias("JAVASSIST", JavassistProxyFactory.class);

    languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
    languageRegistry.register(RawLanguageDriver.class);
}
```

```xml
<!--配置运行环境-->
<environments default="development">
    <!--mysql的开发环境-->
    <environment id="development">
        <!--事务管理器-->
        <transactionManager type="JDBC"/>
        <!--数据源-->
        <dataSource type="POOLED"> 
            <property name="driver" value="${jdbc.Driver}"/>
            <property name="url" value="${jdbc.url}"/>
            <property name="username" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </dataSource>
    </environment>

     <!--oracle的测试环境-->
    <environment id="test">
        <!--事务管理器-->
        <transactionManager type="JDBC"/>
        <!--数据源-->
        <dataSource type="POOLED">
            <property name="driver" value="${oracle.Driver}"/>
            <property name="url" value="${oracle.url}"/>
            <property name="username" value="${oracle.username}"/>
            <property name="password" value="${oracle.password}"/>
        </dataSource>
    </environment>
</environments>
```

```properties
jdbc.Driver = com.mysql.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/vote
jdbc.username = root
jdbc.password = 1234

oracle.Driver = oracle.jdbc.OracleDriver
oracle.url = jdb:oracle:thin:@localhost:1521:orcl
oracle.username = scott 
oracle.password = scott
```

#### 4.8.`databaseIdProvider`

* `databaseIdProvider`：根据不同的数据库执行不同的`sql`语句

`type="DB_VENDOR"`获取到数据库厂商的标识（驱动），`mybatis`就能根据数据库厂商的标识来执行相应的`SQL`

```xml
<databaseIdProvider type="DB_VENDOR">
    <!--为不同的数据库厂商取别名-->
    <property name="MySQL" value="mysql"></property>
    <property name="Oracle" value="oracle"></property>
    <property name="SQL Server" value="sqlserver"></property>
</databaseIdProvider>
```

```xml
<!--指定使用的数据库厂商-->
<select id="getAdminById" resultType="admin2" databaseId="mysql">
    select
    *
    from admin
    where admin_id = #{adminId}
</select>

<select id="getAdminById" resultType="admin2">
    select
    *
    from admin
    where admin_id = #{adminId}
</select>
<!--若同时存在两个getAdminById，一个带数据库厂商的标识，一个没有，会优先使用带了数库厂商的标识的那个语句-->
```

#### 4.8.`mappers`

* `mappers`：将`sql`映射注册到全局配置中
  * `[resource]`：引用类路径下的`sql`映射文件

  * `[url]`：引用网络路径或磁盘路径下的`sql`映射文件 
  * `[class]`：引用（注册）接口；
    * 有`sql`映射文件：此时需要接口和`sql`映射文件在同一个包下，且名称相同
    * 没有`sql`映射文件：基于注解的方式配置`sql`
  * 批量注册`package`；此时需要接口和`sql`映射文件在同一个包下，且名称相同

```xml
<mappers>
    <!--引用类路径的sql映射文件-->
    <mapper resource="mapper/AdminMapper.xml"/>
    <!--引用网络或磁盘路径下的是SQL映射文件-->
    <mapper url="file:///E:/IDEA/comxymybatis/src/main/resources/mapper/AdminMapper.xml"/>
    <!--引用（注册）接口，此时需要接口与sql映射文件在同一个包下，且名称相同-->
    <mapper class="com.xy.mapper.AdminMapper"/>
</mappers>
```

```xml
<mappers>
    <!--引用（注册）接口，没有sql映射文件，使用注解的方式配置SQL-->
    <mapper class="com.xy.mapper.AdminMapper"/>
</mappers>
```

```java
public interface EmpMapper {

    //基于注解配置SQL，且使用接口的方式注册
    @Select("select * from emp where empno = #{empno}")
    public Emp getEmpById(Integer empno);
}
```

```xml
<mappers>
    <!--批量注册，此时需要接口与sql映射文件在同一个包下，且名称相同-->
    <package name="com.xy.mapper"/>
</mappers>
```

### 5、`Sql`映射文件`mapper`

| 属性            | 描述                 | 取值 |
| --------------- | -------------------- | ---- |
| `parameterType` | 设置参数类型，可省略 | 类名 |
|                 |                      |      |
|                 |                      |      |
|                 |                      |      |
|                 |                      |      |
|                 |                      |      |
|                 |                      |      |
|                 |                      |      |
|                 |                      |      |

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xy.mapper.AdminMapper">

    <select id="getAdminById" resultType="com.xy.domain.Admin">
       select
         admin_id adminId,
         name,
         password,
         logintime
       from admin
       where admin_id = #{adminId}
    </select>


</mapper>
```

`mybatis`的增删改`(insert、update、delete)`的接口中允许直接定义如下返回值类型（及其包装类型）：

* `Integer(int)` 返回修改的条数
* `Long(long)`  返回修改的条数
* `Boolean(boolean)` 返回是否修改成功
* `void`

#### 4.1.获取自增主键的值

1. `mysql`支持自增主键，`MyBatis`自增主键的获取与`JDBC`相同，同时通过`Statement`的`getGenreatedKeys()`获取。

```xml
<!--useGeneratedKeys:使用自增主键获取策略-->
<!--keyProperty:指定对应的主键属性，就是mybtais获取到主键的值后，将值封装给javaBean的那个属性-->
<!--keyColumn:-->
<insert id="insertEmp" parameterType="com.xy.domain.Emp" databaseId="mysql" useGeneratedKeys="true" keyProperty="empno">
    insert into emp
      (empno, ename, job, mgr, hiredate, sal, comm, deptno)
    values
      (#{empno}, #{ename}, #{job}, #{mgr}, #{hiredate}, #{sal}, #{comm}, #{deptno})
</insert>
```

2. `mybatis`如何获取`oracle`序列的值

```xml
<insert id="insertEmp" parameterType="com.xy.domain.Emp" databaseId="oracle">
    <!--keyProperty:将mybtais获取到主键的值后，将值封装给javaBean的那个属性-->
    <!--order="BEFORE":表示查询主键的SQL在插入之前运行-->
    <!--resultType:设置查询主键的返回值类型-->
    <selectKey keyProperty="empno" order="BEFORE" resultType="Integer">
        <!--查询主键的sql-->
        select seq_empno.nextval from dual
    </selectKey>
    insert into emp
    (empno, ename, job, mgr, hiredate, sal, comm, deptno)
    values
    (#{empno}, #{ename}, #{job}, #{mgr}, #{hiredate}, #{sal}, #{comm}, #{deptno})
</insert>
<!--brfore运行顺序：先查询出主键并封装给javBean,再执行insert语句-->
```

* `order`：指定`<selectKey>`语句执行的时机
  * `[order = "BEFORE"]` : 表示在之前执行`<selectKey>`语句
  * `[order = "AFTER"]` : 表示在之后执行`<selectKey>`语句

3. `oracle`直接取出序列插入

```xml
<!--直接通过oracle取序列插入-->
<insert id="insertEmp" parameterType="com.xy.domain.Emp" databaseId="oracle">
    <!--在insert完成之后，获取直接的值，并封装到javaBean中-->
    <selectKey keyProperty="empno" order="AFTER" resultType="Integer">
        select sequence_empno.currval from dual
    </selectKey>
    insert into emp
    (empno, ename, job, mgr, hiredate, sal, comm, deptno)
    values
    (sequence_empno.nextval, #{ename}, #{job}, #{mgr}, #{hiredate}, #{sal}, #{comm}, #{deptno})
</insert>
<!--aafter运行顺序：先执行insert语句，再查询出主键并封装给JavBean中-->
```

> 注意： 若插入多条记录时，只能获取到最后插入的主键值，故推荐使用`before`的方式

#### 4.2.参数处理

* 单个参数：`mybatis`不会进行特殊处理

```xml
<select id="getEmpById" resultType="com.xy.domain.Emp" databaseId="oracle" >
    select empno,ename,job from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
```

* 多个参数：会将多个参数封装为`map`,`key：param1,param2...;value:value1,value2,...`；

  `key`是`param1,param2`或`0,1`，`value`是传入参数的值；

```java
//异常信息
org.apache.ibatis.binding.BindingException: Parameter 'empno' not found. Available parameters are [0, 1, param1, param2]
```

```java
 Emp getEmpByIdAndName(Integer empno,String name);
```

```xml
<!--多个参数-->
<select id="getEmpByIdAndName" resultType="com.xy.domain.Emp">
    select * from emp where empno = #{param1} and ename = #{param2}
</select>
```

```xml
<!--多个参数-->
<select id="getEmpByIdAndName" resultType="com.xy.domain.Emp">
    select * from emp where empno = #{0} and ename = #{1}
</select>
```

命名参数：通过`@Param()`明确指定封装参数时的`key`

```java
Emp getEmpByIdAndName(@Param("empno") Integer empno, @Param("ename") String ename);
```

```xml
<select id="getEmpByIdAndName" resultType="com.xy.domain.Emp">
    select * from emp where empno = #{empno} and ename = #{ename}
</select>
```

* 多个参数封装成`pojo`传入：多个参数正好对应业务模型，就直接传入`pojo`



* 多个参数封装成`map`传入：多个参数不是业务模型中的数据，没有对应的`pojo`，不经常使用，为了方便可传入`map`对象



* 若多个参数不是业务模型中的数据，但又经常使用，推荐编写一个`TO(Tranfer Object)`数据传输对象



* 注意：若传入的参数是`Collection(List、Set)`类型或数组，也会特殊处理；把传入的`List`或数组封装在`Map`中。`key`的取值：

  | 传入的参数类型 | `key`的取值       |
  | -------------- | ----------------- |
  | `Collection`   | `collection`      |
  | `List`         | `collection/list` |
  | 数组           | `array`           |

```java
public Emp getEmpById(List<Integer> empnos);
```

```xml
<select id="getEmpById" resultType="com.xy.domain.Emp">
    <!--#{list[0]}表示取出第一个值-->
    select empno,ename,job from emp where empno = #{list[0]}
</select>
```

##### 4.2.1.多个参数的案例

```java
public Emp getEmpByIdAndName(@Param("empno")Integer empno,String ename);
```

```xml
<select id="getEmpByIdAndName" resultType="com.xy.domain.Emp">
    select * from emp where empno = #{empno/param1} and ename = #{param2}
</select>
```

---

```java
public Emp getEmpByIdAndName(Integer empno,Emp emp);
```

```xml
<select id="getEmpByIdAndName" resultType="com.xy.domain.Emp">
    select * from emp where empno = #{param1} and ename = #{param2.ename}
</select>
```

---

```java
public Emp getEmpByIdAndName(Integer empno,@Param("emp")Emp emp);
```

```xml
<select id="getEmpByIdAndName" resultType="com.xy.domain.Emp">
    select * from emp where empno = #{param1} and ename = #{emp.ename/param2.ename}
</select>
```

---

##### 4.2.2.`#{}`和`${}`的区别

* `#{}`：以预编译的方式，将参数设置到`sql`语句中，即使用`PreparedStatement`，可防止`SQL`注入；只能取出参数位置的值
* `${}`：取出的值直接拼装在`SQL`语句中，有安全问题

```xml
select * from emp where empno = ${empno} and ename = #{ename}
```

```sql
select * from emp where empno = 7369 and ename = ? 
```

* 原生`jdbc`不支持占位符的地方我么就使用`${}`进行取值，如动态传入表名：

```sql
-- 如分表的情况下，动态取出某张表
select * from ${year}_sal where xxx
```

* `#{}`可指定的规则

  * `javaType`：指定pojo中属性的类型
  * `jdbcType`：在一些特定的情况下使用。如我们的数据为`null`,部分数据库不能识别`mybatis`对`null`的默认处理，比如`Oracle`就会报错

  ```
  ### Cause: org.apache.ibatis.type.TypeException: Could not set parameters for mapping: ParameterMapping{property='comm', mode=IN, javaType=class java.math.BigDecimal, jdbcType=null, numericScale=null, resultMapId='null', jdbcTypeName='null', expression='null'}. 
  Cause: org.apache.ibatis.type.TypeException: Error setting null for parameter #7 with JdbcType OTHER .
  Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: java.sql.SQLException: 无效的列类型: 1111
  ```

  ```sql
  -- 原因：因为mybatis对所有的null都映射的是原生jdbc的OTHER类型，oracle不能正确处理，而mysql能识别
  jdbcTypeForNull=OTHER,orele不支持
  ```

  解决方案：

  1. 修改`jdbTypeForNull`

  ```xml
  <settings>
      <!--设置null的jdbcType=NULL-->
      <setting name="jdbcTypeForNull" value="NULL"/>
  </settings>
  ```

  2. 修改某个字段`jdbcType`

  ```xml
   <!--修改job的jdbcType=NULL-->
  <insert id="insertEmp" parameterType="com.xy.domain.Emp" databaseId="oracle">
      <selectKey keyProperty="empno" order="BEFORE" resultType="Integer">
          <!--查询主键的sql-->
          select seq_empno.nextval from dual
      </selectKey>
      insert into emp
      (empno, ename, job, mgr, hiredate, sal, comm, deptno)
      values
      (#{empno}, #{ename}, #{job,jdbcType=NULL}, #{mgr}, #{hiredate}, #{sal}, #{comm}, #{deptno})
  </insert>
  ```

  * `mode`：存储过程指定是`in /out`的参数
  * `numericScale`： 设置小数点的位数
  * `resultMap`：结果集的映射关系
  * `typeHandler`：指定类型处理器
  * `jdbcTypeName`：指定`jdbc`的类型
  * `expression`：保留的功能

#### 4.3.指定返回值类型

多条记录返回`Map<Integer,Emp>`，键=这条记录的主键值，值=记录封装的`JavaBean`

```java
//告诉mybatis封装map的时候使用那个属性作为主键
@MapKey("empno")
Map<Integer,Emp> getEmpByNameLike(String ename);
```

```xml
<select id="getEmpByNameLike" resultType="com.xy.domain.Emp">
    select * from emp where ename like '%'||#{ename}||'%'
</select>
```

##### 4.3.1`JavaBean`属性名称与列名不一致的处理方式

1. 若列名符合驼峰命名法，可开启驼峰命名自动转换；如列名`last_name `，属性名`lastName`

2. 查询语句中使用别名

   ```sql
   select empno,last_name lastName from emp;	
   ```

3. 使用`resultMap`自定义结果集映射规则

##### 4.3.2.`resultMap`

```xml
<!--自定义结果集映射规则-->
<resultMap id="empResultMap" type="com.xy.domain.Emp">
    <!--设置主键映射规则-->
    <id column="empno" property="empno"/>
    <!--定义普通列-->
    <result column="ename" property="ename"/>
    <!--其余未指定的列会自动封装，但推荐：写resultMap时就写出全部的映射规则-->
    <result column="job" property="job"/>
    <result column="mgr" property="mgr"/>
    <result column="hiredate" property="hiredate"/>
    <result column="sal" property="sal"/>
    <result column="comm" property="comm"/>
    <result column="deptno" property="deptno"/>
</resultMap>
```

```xml
<select id="getEmpById" resultMap="empResultMap">
    select * from emp where empno = #{empno}
</select>
```

##### 4.3.3.`association`

关联查询结果集映射，查询`EMP`关联的`Dept`信息

```java
public class Emp {
    private Integer empno;
    private String ename;
    private String job;
    private Integer mgr;
    private Date hiredate;
    private BigDecimal sal;
    private BigDecimal comm;
    private int deptno;
    private Dept dept;
    ...
}
```

```xml
<resultMap id="empAndDeptMap" type="com.xy.domain.Emp">
    <!--设置主键映射规则-->
    <id column="empno" property="empno"/>
    <!--定义普通列-->
    <result column="ename" property="ename"/>
    <!--其余未指定的列会自动封装，但推荐：写resultMap时就写出全部的映射规则-->
    <result column="job" property="job"/>
    <result column="mgr" property="mgr"/>
    <result column="hiredate" property="hiredate"/>
    <result column="sal" property="sal"/>
    <result column="comm" property="comm"/>
    <result column="deptno" property="deptno"/>
    <!--设置dept对象的映射规则,使用级联属性封装-->
    <result column="deptno" property="dept.deptno"/>
    <result column="dname" property="dept.dname"/>
    <result column="loc" property="dept.loc"/>
</resultMap>
```

```xml
<!--association指定关联的单个对象--> 
<resultMap id="empAndDeptMap" type="com.xy.domain.Emp">
     <!--设置主键映射规则-->
     <id column="empno" property="empno"/>
     <!--定义普通列-->
     <result column="ename" property="ename"/>
     <!--其余未指定的列会自动封装，但推荐：写resultMap时就写出全部的映射规则-->
     <result column="job" property="job"/>
     <result column="mgr" property="mgr"/>
     <result column="hiredate" property="hiredate"/>
     <result column="sal" property="sal"/>
     <result column="comm" property="comm"/>
     <result column="deptno" property="deptno"/>
     <!--association指定联合的JavaBean对象-->
     <association property="dept" javaType="com.xy.domain.Dept">
         <id column="deptno" property="deptno"/>
         <result column="dname" property="dname"/>
         <result column="loc" property="loc"/>
     </association>
</resultMap>
```

```xml
<select id="getEmpAndDept" resultMap="empAndDeptMap">
    select
        a.empno    as empno,
        a.ename    as ename,
        a.job      as job,
        a.mgr      as mgr,
        a.hiredate as hiredate,
        a.sal      as sal,
        a.comm     as comm,
        a.deptno   as deptno,
        b.dname    as dname,
        b.loc      as loc
    from emp a
    left join dept b
    on a.deptno = b.deptno
    where empno = #{empno}
</select>
```

```java
Emp getEmpAndDept(Integer empno);
```

###### 4.3.3.1.`association`分步查询 

```xml
<!--association分步查询:1.先根据员工id查询出员工信息，2.在根据员工信息中的deptno查询部门信息-->
<resultMap id="empAndDeptMap" type="com.xy.domain.Emp">
    <!--设置主键映射规则-->
    <id column="empno" property="empno"/>
    <!--定义普通列-->
    <result column="ename" property="ename"/>
    <!--其余未指定的列会自动封装，但推荐：写resultMap时就写出全部的映射规则-->
    <result column="job" property="job"/>
    <result column="mgr" property="mgr"/>
    <result column="hiredate" property="hiredate"/>
    <result column="sal" property="sal"/>
    <result column="comm" property="comm"/>
    <result column="deptno" property="deptno"/>
    <!--association指定关联对象的封装规则;
            select按指定的方法查询结果;
            column指定传递的值-->
    <association property="dept"
                 select="com.xy.mapper.DeptMapper.getDeptById"
                 column="deptno" >
    </association>
</resultMap>
```

```xml
<select id="getEmpById" resultMap="empAndDeptMap">
    select * from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
```

```xml
<select id="getDeptById" resultType="com.xy.domain.Dept">
    select * from dept where deptno = #{deptno}
</select>
```

```java
Dept getDeptById(String deptno);
```

###### 4.3.3.2.延迟加载

```xml
<!--开启懒加载-->
<setting name="lazyLoadingEnabled" value="true"></setting>
<!--按需加载-->
<setting name="aggressiveLazyLoading" value="false"></setting>
```

```xml
<!--association分步查询:1.先根据员工id查询出员工信息，2.在根据员工信息中的deptno查询部门信息-->
<resultMap id="empAndDeptMap" type="com.xy.domain.Emp">
    <!--设置主键映射规则-->
    <id column="empno" property="empno"/>
    <!--定义普通列-->
    <result column="ename" property="ename"/>
    <!--其余未指定的列会自动封装，但推荐：写resultMap时就写出全部的映射规则-->
    <result column="job" property="job"/>
    <result column="mgr" property="mgr"/>
    <result column="hiredate" property="hiredate"/>
    <result column="sal" property="sal"/>
    <result column="comm" property="comm"/>
    <result column="deptno" property="deptno"/>
    <!--association指定关联对象的封装规则;
            select按指定的方法查询结果;
            column指定传递的值-->
    <association property="dept"
                 select="com.xy.mapper.DeptMapper.getDeptById"
                 column="deptno" >
    </association>
</resultMap>
```

```xml
<select id="getEmpById" resultMap="empAndDeptMap">
    select * from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
```

```xml
<select id="getDeptById" resultType="com.xy.domain.Dept">
    select * from dept where deptno = #{deptno}
</select>
```

```java
Dept getDeptById(String deptno);
```

##### 4.3.4.`collection`

查询部门信息，包含部门下所有的员工信息

```xml
<resultMap id="deptIncludeEmp" type="com.xy.domain.Dept">
    <id column="deptno" property="deptno"/>
    <result column="dname" property="dname"/>
    <result column="loc" property="loc"/>
    <!--定义关联集合类型属性的封装规则
            ofType : 设置集合内元素的属性-->
    <collection property="emps" ofType="com.xy.domain.Emp">
        <id column="empno" property="empno"/>
        <result column="ename" property="ename"/>
        <result column="job" property="job"/>
        <result column="mgr" property="mgr"/>
        <result column="hiredate" property="hiredate"/>
        <result column="sal" property="sal"/>
        <result column="comm" property="comm"/>
        <result column="deptno" property="deptno"/>
    </collection>
</resultMap>
```

```xml
<select id="getDeptByIdPlus" resultMap="deptIncludeEmp">
    select
    a.deptno   as deptno,
    a.dname    as dname,
    a.loc      as loc,
    b.empno    as empno,
    b.ename    as ename,
    b.job      as job,
    b.mgr      as mgr,
    b.hiredate as hiredate,
    b.sal      as sal,
    b.comm     as comm
    from dept a
    left join emp b
    on a.deptno = b.deptno
    where a.deptno = #{deptno}
</select>
```

```xml
 Dept getDeptByIdPlus(String deptno);
```

分步查询传递一个参数的值

```xml
<resultMap id="deptIncludeEmpStep" type="com.xy.domain.Dept">
    <id column="deptno" property="deptno"/>
    <result column="dname" property="dname"/>
    <result column="loc" property="loc"/>
    <!--定义关联集合类型属性的封装规则
            select : 设置关联查询的语句
            column : 设置查询语句的id-->
    <collection property="emps"
                select="com.xy.mapper.EmpMapper.getEmpByDeptno"
                column="deptno">
    </collection>
</resultMap>
```

```xml
<select id="getDeptByIdStep" resultMap="deptIncludeEmpStep">
    select * from dept where deptno = #{deptno}
</select>
```

```xml
<select id="getEmpByDeptno" resultType="com.xy.domain.Emp">
    select * from emp where deptno = #{deptno}
</select>
```

```java
Dept getDeptByIdStep(String deptno);
```

分步查询传递多个参数的值；将多列的值封装为`map`传递

```xml
column="{key1 = val1,key2 = val 2}"
```

```xml
<resultMap id="deptIncludeEmpParam" type="com.xy.domain.Dept">
    <id column="deptno" property="deptno"/>
    <result column="dname" property="dname"/>
    <result column="loc" property="loc"/>
    <!--定义关联集合类型属性的封装规则
            select : 设置关联查询的语句
            column : 设置查询语句的id
            fetchType="lazy" : 开启延迟加载
            fetchType="eager" : 设置为立即加载-->
    <collection property="emps"
                select="com.xy.mapper.EmpMapper.getEmpByDeptno"
                column="{deptno = deptno}"
                fetchType="lazy">
    </collection>
</resultMap>
```

```xml
<select id="getDeptByIdParam" resultMap="deptIncludeEmpParam">
    select * from dept where deptno = #{deptno}
</select>
```

```xml
<select id="getEmpByDeptno" resultType="com.xy.domain.Emp">
    select * from emp where deptno = #{deptno}
</select>
```

```java
Dept getDeptByIdParam(String deptno);
```

##### 4.4.5.`discriminator`

`discriminator` : 鉴别器，可以判断某列的值，从而更具某列的值改变封装行为

```xml
 <resultMap id="discriminatorMap" type="com.xy.domain.Dept">
     <id column="deptno" property="deptno"/>
     <result column="dname" property="dname"/>
     <result column="loc" property="loc"/>
     <!--discriminator : 鉴别器，可以判断某列的值，从而更具某列的值改变封装行为-->
     <discriminator javaType="string" column="deptno">
         <!--若为10部门查询该部门所有的人员信息
			注意此处必须指定类型，指定参数的类型-->
         <case value="10" resultType="com.xy.domain.Dept">
             <collection property="emps"
                         select="com.xy.mapper.EmpMapper.getEmpByDeptno"
                         column="deptno"
                         fetchType="lazy">
             </collection>
         </case>
         <!--若为20部门只查询所以人员的编号和姓名-->
         <case value="20" resultType="com.xy.domain.Dept">
             <collection property="emps" ofType="com.xy.domain.Emp">
                 <id column="empno" property="empno"/>
                 <result column="ename" property="ename"/>
             </collection>
         </case>
         <!--若为30部门查询该部门所有的人员信息-->
         <case value="30" resultType="com.xy.domain.Dept">
             <collection property="emps" ofType="com.xy.domain.Emp">
                 <id column="empno" property="empno"/>
                 <result column="ename" property="ename"/>
                 <result column="job" property="job"/>
                 <result column="mgr" property="mgr"/>
                 <result column="hiredate" property="hiredate"/>
                 <result column="sal" property="sal"/>
                 <result column="comm" property="comm"/>
                 <result column="deptno" property="deptno"/>
             </collection>
         </case>
     </discriminator>
</resultMap>
```

```xml
<select id="getDeptByIdIf" resultMap="discriminatorMap">
    select
    a.deptno   as deptno,
    a.dname    as dname,
    a.loc      as loc,
    b.empno    as empno,
    b.ename    as ename,
    b.job      as job,
    b.mgr      as mgr,
    b.hiredate as hiredate,
    b.sal      as sal,
    b.comm     as comm
    from dept a
    left join emp b
    on a.deptno = b.deptno
    where a.deptno = #{deptno}
</select>
```

```xml
<select id="getEmpByDeptno" resultType="com.xy.domain.Emp">
    select * from emp where deptno = #{deptno}
</select>
```

```java
Dept getDeptByIdIf(String deptno);
```

### 6、动态`SQL`

使用`if`根据设置哪些参数就根据这些参数查询

```xml
<select id="getEmpByConditionIf" resultType="com.xy.domain.Emp">
    select
    a.empno as empno,
    a.ename as ename,
    a.job as job,
    a.mgr as mgr,
    a.hiredate as hiredate,
    a.sal as sal,
    a.comm as comm
    from emp a
    where 1 = 1
    <!--test 设置判断表达式（支持OGNL） -->
    <if test="empno != null">
        and empno = #{empno}
    </if>
    <if test="ename != null and ename.trim() != ''">
        and ename like #{ename}
    </if>
    <!-- &amp;&amp; == &&    &quot; &quot; == ""-->
    <if test="job != null &amp;&amp; job.trim() != &quot; &quot;">
        and job like #{job}
    </if>
</select>
```

```xml
<select id="getEmpByConditionIf" resultType="com.xy.domain.Emp">
    select
    a.empno as empno,
    a.ename as ename,
    a.job as job,
    a.mgr as mgr,
    a.hiredate as hiredate,
    a.sal as sal,
    a.comm as comm
    from emp a
     <!--where 获取去掉第一个条件的and,但只会去除前面的and -->
    <where>
        <if test="empno != null">
            and empno = #{empno}
        </if>
        <if test="ename != null and ename.trim() != ''">
            and ename like #{ename}
        </if>
        <if test="job != null &amp;&amp; job.trim() != &quot; &quot;">
            and job like #{job}
        </if>
    </where>
</select>
```

`<trim>`自定义字符串的截取规则

```xml
 <select id="getEmpByConditionTrim" resultType="com.xy.domain.Emp">
     select
         a.empno as empno,
         a.ename as ename,
         a.job as job,
         a.mgr as mgr,
         a.hiredate as hiredate,
         a.sal as sal,
         a.comm as comm
     from emp a
     <!--trim 截取字符串
            prefix="" : 前缀，给拼接后整个字符串添加前缀
            prefixOverrides="" :前缀覆盖，给拼接后整个字符串去除多余前缀
            suffix="" :后缀，给拼接后整个字符串添加后缀
            suffixOverrides="" :后缀覆盖，给拼接后整个字符串去除多余后缀-->
     <trim prefix="where" prefixOverrides="and">
         <if test="empno != null">
             and empno = #{empno}
         </if>
         <if test="ename != null and ename.trim() != ''">
             and ename like #{ename}
         </if>
         <if test="job != null &amp;&amp; job.trim() != &quot; &quot;">
             and job like #{job}
         </if>
     </trim>
</select>
```

```xml
<select id="getEmpByConditionTrim" resultType="com.xy.domain.Emp">
    select
        a.empno as empno,
        a.ename as ename,
        a.job as job,
        a.mgr as mgr,
        a.hiredate as hiredate,
        a.sal as sal,
        a.comm as comm
    from emp a
    <!--trim 截取字符串
            prefix="" : 前缀，给拼接后整个字符串添加前缀
            prefixOverrides="" :前缀覆盖，给拼接后整个字符串去除多余前缀
            suffix="" :后缀，给拼接后整个字符串添加后缀
            suffixOverrides="" :后缀覆盖，给拼接后整个字符串去除多余后缀-->
    <trim prefix="where" suffixOverrides="and">
        <if test="empno != null">
            empno = #{empno} and
        </if>
        <if test="ename != null and ename.trim() != ''">
            ename like #{ename} and
        </if>
        <if test="job != null &amp;&amp; job.trim() != &quot; &quot;">
            job like #{job}
        </if>
    </trim>
</select>
```

#### 6.1.分支选择

使用`choose`查询设置的那个参数就查询改条件的数据

```xml
 <select id="getEmpByConditionChoose" resultType="com.xy.domain.Emp">
     select
         a.empno as empno,
         a.ename as ename,
         a.job as job,
         a.mgr as mgr,
         a.hiredate as hiredate,
         a.sal as sal,
         a.comm as comm
     from emp a
     <where>
         <!--设置了那参数就根据那个条件查询，只能有一个条件-->
         <choose>
             <when test="ename != null and ename.trim() == ''">
                 and ename like #{ename}
             </when>
             <when test="job != null">
                 and job like #{job}
             </when>
             <otherwise>
                 and sal &gt; 2000
             </otherwise>
         </choose>
     </where>
</select>
```



#### 6.2.`set`

`<set>`设置更新语句根据设置的那些条件就更新那些字段

```xml
<!--按设置的条件更新字段，set会去除拼接后整个字符串最后的逗号-->
<update id="updateById">
    update emp
    <set>
        <if test="ename != null and ename.trim() != ''">
            ename = #{ename},
        </if>
        <if test="job != null and job.trim() != ''">
            job = #{job},
        </if>
        <if test="sal &gt; 0">
            sal = #{sal}
        </if>
    </set>
    where empno = #{empno}
</update>
```

```xml
<!--按设置的条件更新字段，使用trim会去除拼接后整个字符串最后的逗号-->
<update id="updateById">
    update emp
    <trim prefix="set" suffixOverrides=",">
        <if test="ename != null and ename.trim() != ''">
            ename = #{ename},
        </if>
        <if test="job != null and job.trim() != ''">
            job = #{job},
        </if>
        <if test="sal &gt; 0">
            sal = #{sal}
        </if>
    </trim>
    where empno = #{empno}
</update>
```

```java
int updateById(Emp emp);
```

#### 6.3.`foreach`

##### 6.3.1使用`<foreach>`遍历查询

```xml
<select id="getEmpByConditionForeach" resultType="com.xy.domain.Emp" >
    select * from emp where empno in
    <!--collection:指定要变量的集合
            item:将遍历出来的值赋值给指定的变量
            separator:设置分隔符
            open:给遍历出的结果拼接一个开始字符串
            close:给遍历出的结果拼接一个结束字符串
            index:索引，遍历list的时候,index是索引,item是当前值
                  遍历Map的时候，index表示map的key,item表示map的value-->
    <foreach collection="ids" item="id" separator="," open="(" close=")">
        #{id}
    </foreach>
</select>
```

```java
 List<Emp> getEmpByConditionForeach(@Param("ids")List<Integer> ids);
```

##### 6.3.2.mysql批量插入数据

方式1：使用insert into table_name values(),()...

```xml
<!-- MySQL批量插入语句拼接，语法：values(),(),()... -->
<insert id="insertBatch">
    insert into emp
    (empno, ename, job, mgr, hiredate, sal, comm, deptno)
    values
    <foreach collection="emps" item="emp" separator="," >
        (#{emp.empno},#{emp.ename},#{emp.job},#{emp.mgr},#{emp.hiredate},
        #{emp.sal},#{emp.comm},#{emp.deptno})
    </foreach>
</insert>
```

```java
public void insertBatch(@Param("emps")List<Emp> emps);
```

方式2：通过一次指定多个`SQL`语句实现批量插入

```properties
#使用JDBC的属性allowMultiQueries开启支持多条语句的执行，也可用于其余语句的批量
jdbc.url=jdbc:mysql://localhost:3306/mybatis?allowMultiQueries=true
```

```xml
<!--通用批量插入，即多个insert语句使用;分隔 -->
<insert id="insertBatch">
    <foreach collection="emps" item="emp" separator=";">
        insert into emp
        (empno, ename, job, mgr, hiredate, sal, comm, deptno)
        values
        (#{emp.empno},#{emp.ename},#{emp.job},#{emp.mgr},#{emp.hiredate},
        #{emp.sal},#{emp.comm},#{emp.deptno})
    </foreach>
</insert>
```

##### 6.3.3.oracle批量插入数据

方式1：使用代码块的方式执行多条语句实现批量插入

```plsql
begin 
  insert into emp (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
  values (7369, 'BOM', 'ANALYST', 7902, to_date('17-12-1980', 'dd-mm-yyyy'), 800.00, null, 20);
  insert into emp (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
  values (7499, 'ALLEN', 'SALESMAN', 7698, to_date('20-02-1981', 'dd-mm-yyyy'), 1600.00, 300.00, 30);
end;
```

```xml
<!--注意此处语句之间使用;分隔，不能使用separator=";"（因为最后一个语句会没有; ）,--> 
<insert id="insertBatch">
    <foreach collection="emps" item="emp" open="begin" close="end;">
        insert into emp
        (empno, ename, job, mgr, hiredate, sal, comm, deptno)
        values
        (seq_empno.nextval,#{emp.ename},#{emp.job},#{emp.mgr},#{emp.hiredate},
        #{emp.sal},#{emp.comm},#{emp.deptno});
    </foreach>
</insert>
```

```java
public int insertBatch(@Param("emps")List<Emp> emps);
```

方式2：使用中间表的方式实现批量插入

```sql
insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno)
select seq_empno.nextval, ename, job, mgr, hiredate, sal, comm, deptno
from (select 'ALLEN' ename,
             'SALESMAN' job,
             7698 mgr,
             to_date('20-02-1981', 'dd-mm-yyyy') hiredate,
             1600.00 sal,
             300.00 comm,
             30 deptno
        from dual
      union
      select 'WARD' ename,
             'SALESMAN' job,
             7698,
             to_date('22-02-1981', 'dd-mm-yyyy') hiredate,
             1250.00 sal,
             500.00 comm,
             30 deptno 
         from dual
     );
```

```xml
<insert id="insertBatch">
    insert into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno)
    select seq_empno.nextval, ename, job, mgr, hiredate, sal, comm, deptno from
    <foreach collection="emps" item="emp" separator="union" open="(" close=")">
        <!-- 注意此处设置的值要设置别名 -->
        select 
            #{emp.ename} as ename,
            #{emp.job} as job,
            #{emp.mgr} as mgr,
            #{emp.hiredate} as hiredate,
            #{emp.sal} as sal,
            #{emp.comm} as comm,
            #{emp.deptno} as deptno 
        from dual
    </foreach>
</insert>
```

```java
public int insertBatch(@Param("emps")List<Emp> emps);
```

方式3：

```sql
insert all 
into emp values
(8369, 'BOM', 'ANALYST', 7902, to_date('17-12-1980', 'dd-mm-yyyy'), 800.00, null, 20)
into emp values 
(8499, 'ALLEN', 'SALESMAN', 7698, to_date('20-02-1981', 'dd-mm-yyyy'), 1600.00, 300.00, 30)
select 1 from dual;  
```

```sql
insert all 
into emp (empno, ename, job, mgr, hiredate, sal, comm, deptno)
values (7369, 'BOM', 'ANALYST', 7902, to_date('17-12-1980', 'dd-mm-yyyy'), 800.00, null, 20)
into emp (empno, ename, job, mgr, hiredate, sal, comm, deptno)
values (7499, 'ALLEN', 'SALESMAN', 7698, to_date('20-02-1981', 'dd-mm-yyyy'), 1600.00, 300.00, 30)
select 1 from dual;  
```

```xml
<!-- 注意：此方式不能使用序列插入主键，因为一次不能获取多个序列的值 -->
<insert id="insertBatch">
    <foreach collection="emps" item="emp" open=" insert all " close="select 1 from dual">
        into emp(empno, ename, job, mgr, hiredate, sal, comm, deptno)
        values(#{emp.empno},#{emp.ename},#{emp.job},#{emp.mgr},
        #{emp.hiredate},#{emp.sal},#{emp.comm},#{emp.deptno})
    </foreach>
</insert>
```

```java
public int insertBatch(@Param("emps")List<Emp> emps);
```

#### 6.4.mybatis的内置参数

* `_parameter`：代表整个参数
  * 单个参数：`_parameter`就是这个参数
  * 多个参数：此时多个参数或封装为map,`_parameter`就是这个map
* `_databaseId`：若配置了`<databaseIdProvider>`标签，`_databaseId`就表示当前数据库的别名

```xml
<!--同时支持mysql和oracl,不需要手动切换databaseId-->
<select id="getEmpInnerParam" resultType="com.xy.domain.Emp">
    <if test="_databaseId == 'mysql'">
        select * from employees where employee_id = #{empno}
    </if>
    <if test="_databaseId == 'oracle'">
        select * from emp
        <!--当参数不为空时，才跟上查询条件-->
        <if test="_parameter != null">
            where ename = #{_parameter.ename}
        </if>
    </if>
</select>
```

```java
List<Emp>  getEmpInnerParam(Emp emp);
```

#### 6.5.`bind`

`<bind>`将OGNL表达式的值绑定到一个变量中，方便后来引用这个值

```xml
<select id="getEmpByName" resultType="com.xy.domain.Emp">
    <!--模糊查询动态拼接%-->
    <bind name="ename" value="'%'+ename+'%'"/>
    select * from emp where ename like #{ename}
</select>
```

```java
List<Emp>  getEmpByName(Emp emp);
```

#### 6.6.`sql`

`<sql>`抽取可重用的SQL片段

```xml
<!--插入表格的列 -->
<sql id="insertColumn">
    <if test="_databaseId == 'oracle' ">
        empno, ename, job, mgr, hiredate, sal, comm, deptno
    </if>
</sql>
```

```xml
<insert id="insertBatch">
    insert into emp(
    <!--引用sql片段-->
    <include refid="insertColumn"></include>
    )
    select seq_empno.nextval, ename, job, mgr, hiredate, sal, comm, deptno from
    <foreach collection="emps" item="emp" separator="union" open="(" close=")">
        <!-- 注意此处设置的值要设置别名 -->
        select #{emp.ename} as ename,#{emp.job} as job,#{emp.mgr} as mgr,#{emp.hiredate} as hiredate,#{emp.sal} as sal,#{emp.comm} as comm,#{emp.deptno} as deptno from dual
    </foreach>
</insert>
```

```java
public int insertBatch(@Param("emps")List<Emp> emps);
```

在`<include>`中定义`<property>`属性

```xml
<sql id="insertColumn">
    <if test="_databaseId == 'oracle' ">
        empno, ename, job, mgr, hiredate, sal, comm, ${deptno}
    </if>
</sql>
```

```xml
<insert id="insertBatch">
    insert into emp(
    <!--引用sql片段-->
    <include refid="insertColumn">
        <!--自己定义属性的值 -->
        <property name="deptno" value="deptno"></property>
    </include>
    )
    select seq_empno.nextval, ename, job, mgr, hiredate, sal, comm, deptno from
    <foreach collection="emps" item="emp" separator="union" open="(" close=")">
        <!-- 注意此处设置的值要设置别名 -->
        select #{emp.ename} as ename,#{emp.job} as job,#{emp.mgr} as mgr,#{emp.hiredate} as hiredate,#{emp.sal} as sal,#{emp.comm} as comm,#{emp.deptno} as deptno from dual
    </foreach>
</insert>
```

```jav
public int insertBatch(@Param("emps")List<Emp> emps);
```































### 6、自定义时间处理类型



### 7、自定义事务管理器



### 8、自定义数据源