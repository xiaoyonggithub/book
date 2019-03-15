### 一、缓存机制

mybatis中默认定义了两级缓存

* 一级缓存:本地缓存，默认开启；与数据库同一次会话期间查到的数据放在一级缓存中；
* 二级缓存:全局缓存，需手动开启和配置的，是基于namespace级别的缓存；

![1530890145305](E:\typora\images\1530890145305.png)

#### 1.1.一级缓存

* 默认开启，不能关闭；
* SqlSession级别的缓存，与数据库同一次会话期间查到的数据放在一级缓存中；
* 执行增删改操作后清空缓存

```xml
<select id="getEmpById" resultType="com.xy.domain.Emp">
    select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
```

```java
@Before
public void init() throws IOException {
    String resource = "config/mybatis-config.xml";
    InputStream is = Resources.getResourceAsStream(resource);
    factory = new SqlSessionFactoryBuilder().build(is);
}
```

```java
//验证一级缓存
@Test
public void tset08(){
    SqlSession session = factory.openSession();
    try{
        EmpMapper empMapper = session.getMapper(EmpMapper.class);
        Emp emp = empMapper.getEmpById(7521);
        System.out.println(emp);

        Emp emp1 = empMapper.getEmpById(7521);
        System.out.println(emp1);

        System.out.println(emp == emp1);
    }finally {
        session.close();
    }
}
```

##### 1.1.1.失效的情况

* 不同的SqlSession之间一级缓存失效

```xml
<select id="getEmpById" resultType="com.xy.domain.Emp">
    select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
```

```java
@Before
public void init() throws IOException {
    String resource = "config/mybatis-config.xml";
    InputStream is = Resources.getResourceAsStream(resource);
    factory = new SqlSessionFactoryBuilder().build(is);
}
```

```java
@Test
public void tset08(){
    SqlSession session = factory.openSession();
    SqlSession session1 = factory.openSession();
    try{
        //session
        EmpMapper empMapper = session.getMapper(EmpMapper.class);
        Emp emp = empMapper.getEmpById(7521);
        System.out.println(emp);

        //session1
        EmpMapper empMapper1 = session1.getMapper(EmpMapper.class);
        Emp emp1 = empMapper1.getEmpById(7521);
        System.out.println(emp1);

        System.out.println(emp == emp1);
    }finally {
        session.close();
        session1.close();
    }
}
```

* SqlSeesion相同，当两次查询之间增删改操作

```xml
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

```xml
<select id="getEmpById" resultType="com.xy.domain.Emp">
    select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
public void insertEmp(Emp emp);
```

```java
@Test
public void tset08(){
    SqlSession session = factory.openSession();
    try{
        EmpMapper empMapper = session.getMapper(EmpMapper.class);
        Emp emp = empMapper.getEmpById(7521);
        System.out.println(emp);

        //插入操作
        Emp iemp = new Emp(423,"CS001",10);
        empMapper.insertEmp(iemp);

        Emp emp1 = empMapper.getEmpById(7521);
        System.out.println(emp1);

        System.out.println(emp == emp1);
    }finally {
        session.close();
    }
}
```

* SqlSession相同，但手动清除了缓存

```java
@Test
public void tset08(){
    SqlSession session = factory.openSession();
    try{
        EmpMapper empMapper = session.getMapper(EmpMapper.class);
        Emp emp = empMapper.getEmpById(7521);
        System.out.println(emp);

        //清除缓存一级缓存
        session.clearCache();

        Emp emp1 = empMapper.getEmpById(7521);
        System.out.println(emp1);

        System.out.println(emp == emp1);
    }finally {
        session.close();
    }
}
```

提示：其实一级缓存就是存放在map中，每次查询时先去map中取，有就取出，没有就查询

#### 1.2.二级缓存

* 基于namespace级别的缓存，一个namespace对应一个二级缓存
* 注意：查询的数据默认放在一级缓存，只有SqlSession提交或关闭后才会将数据同步到二级缓存中

##### 1.2.1.工作机制(二级缓存)

1. 在一个会话中，执行查询后，会将数据放在一级缓存中；
2. 若会话关闭了，一级缓存中的数据会清空，但会保存到二级缓存中；
3. 若再次查询就会从二级缓存中获取；
4. 二级缓存中，不同namespace查询的数据会放在对应的缓存（map）中

##### 1.2.2.使用步骤（二级缓存）

1. 开启二级缓存配置

```xml
<!--开启全局二级缓存-->
<setting name="cacheEnabled" value="true"></setting>
```

2. 在mapper.xml中配置`<cache>`

```xml
<!--设置namespace使用缓存
        type:指定自定义缓存的全类名
        blocking:
        eviction:缓存的回收策略
        flushInterval:刷新间隔，默认毫秒；默认没有设置间隔时间，仅在调用语句时刷新
        readOnly:只读，默认false
            true:只读缓存；不能修改数据，为加快速度，直接返回缓存中的引用，不安全速度快
            false:读写缓存；数据可能修改，返回数据的拷贝（通过序列化和反序列化克隆数据对象），安全速度较慢
        size:缓存大小，最大可存储的对象数-->
<cache blocking="" eviction="FIFO" flushInterval="6000" readOnly="false" size="1024"></cache>
```

3.POJO需要实现序列化接口

1.2.3.`cache`的属性配置

`<cache></cache>`标签属性的设置

| 属性名称      | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| type          | 指定自定义缓存的全类名                                       |
| blocking      |                                                              |
| eviction      | 缓存的回收策略                                               |
| flushInterval | 刷新间隔，默认毫秒；默认没有设置间隔时间，仅在调用语句时刷新 |
| readOnly      | 只读，默认false                                              |
| size          | 缓存大小，最大可存储的对象数                                 |

##### 1.2.4.`eviction`缓存的回收策略

| 策略 | 描述                                                     |
| ---- | -------------------------------------------------------- |
| LRU  | 最近最少使用的：移除最长时间不被使用的对象               |
| FIFO | 先进先出：按对象进入缓存的顺序来移除它们                 |
| SOFT | 软引用：移除基于垃圾回收器状态和软引用规则的对象         |
| WEAK | 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象 |

##### 1.2.5.验证二级缓存的使用

```xml
<!-- 设置namespace使用二级缓存 -->
<cache></cache>
<select id="getEmpById" resultType="com.xy.domain.Emp">
    select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp where empno = #{empno}
</select>
```

```java
public Emp getEmpById(Integer empno);
```

```java
    @Test
    public void tset11() {
        //会话一
        SqlSession session = factory.openSession();
        EmpMapper empMapper = session.getMapper(EmpMapper.class);
        Emp emp = empMapper.getEmpById(7521);
        System.out.println(emp);

        //关闭session
        session.close();

        //会话二
        SqlSession session1 = factory.openSession();
        EmpMapper empMapper1 = session1.getMapper(EmpMapper.class);
        Emp emp1 = empMapper1.getEmpById(7521);
        System.out.println(emp1);

        System.out.println(emp == emp1);  //false
    }
```

注意：查询的数据默认放在一级缓存，只有SqlSession提交或关闭后才会将数据同步到二级缓存中

##### 1.2.6.缓存修改配置

* `<setting name="cacheEnabled" value="true"></setting>`只影响二级缓存
* `<select useCache="true">`只影响二级缓存，是否使用二级缓存
* 在增删改语句中`flushCache`(清空缓存)默认为`true`,影响一级缓存和二级缓存

```xml
<!--flushCache="true"执行后会清空以一级缓存和二级缓存 -->
<insert flushCache="true" ></insert> 
```

注意：`flushCache=true`清空一级和二级缓存

* `session.clearCache()`只清除一级缓存，不影响二级缓存
* `<setting name="localCacheScope" value="SESSION "></setting>`设置一级缓存作用域
  * SESSION ：使用一级缓存，默认
  * STATEMENT：相当于禁用缓存

##### 1.2.7.缓存的使用顺序

二级缓存>一级缓存>在数据库查询

二级缓存的范围大于一级缓存

##### 1.2.8.`cache-ref`

```xml
<!--指定与那个namespace下的缓存一样-->
<cache-ref namespace="com.xy.mapper.EmpMapper"/>
```

#### 1.3.自定义缓存

实现`Cache`接口



#### 1.4.整合`ehcache`

[ehcache整合mybatis的Cache接口实现](https://github.com/mybatis)

1. 引入使用的jar

```xml
<!-- https://mvnrepository.com/artifact/org.ehcache/ehcache -->
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.4.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
<dependency>
    <groupId>org.mybatis.caches</groupId>
    <artifactId>mybatis-ehcache</artifactId>
    <version>1.1.0</version>
</dependency>
```

2. 设置使用的缓存

```xml
<mapper namespace="org.acme.FooMapper">
  <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
</mapper>    
```

3. 运行时可动态改变参数的值,即配置缓存参数

```xml
<mapper namespace="org.acme.FooMapper">
  <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
    <property name="timeToIdleSeconds" value="3600"/><!--1 hour-->
    <property name="timeToLiveSeconds" value="3600"/><!--1 hour-->
    <property name="maxEntriesLocalHeap" value="1000"/>
    <property name="maxEntriesLocalDisk" value="10000000"/>
    <property name="memoryStoreEvictionPolicy" value="LRU"/>
  </cache>
  ...
</mapper>
```

4. `ehcache`运行需要提供`ehcache.xml`配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
 <!-- 磁盘保存路径 -->
 <diskStore path="D:\44\ehcache" />
 
 <!--配置缓存的默认参数-->
 <defaultCache 
   maxElementsInMemory="1" 
   maxElementsOnDisk="10000000"
   eternal="false" 
   overflowToDisk="true" 
   timeToIdleSeconds="120"
   timeToLiveSeconds="120" 
   diskExpiryThreadIntervalSeconds="120"
   memoryStoreEvictionPolicy="LRU">
 </defaultCache>
</ehcache>
 
<!-- 
属性说明：
l diskStore：指定数据在磁盘中的存储位置。
l defaultCache：当借助CacheManager.add("demoCache")创建Cache时，EhCache便会采用<defalutCache/>指定的的管理策略
 
以下属性是必须的：
l maxElementsInMemory - 在内存中缓存的element的最大数目 
l maxElementsOnDisk - 在磁盘上缓存的element的最大数目，若是0表示无穷大
l eternal - 设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效，如果为false那么还要根据timeToIdleSeconds，timeToLiveSeconds判断
l overflowToDisk - 设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上
 
以下属性是可选的：
l timeToIdleSeconds - 当缓存在EhCache中的数据前后两次访问的时间超过timeToIdleSeconds的属性取值时，这些数据便会删除，默认值是0,也就是可闲置时间无穷大
l timeToLiveSeconds - 缓存element的有效生命期，默认是0.,也就是element存活时间无穷大
 diskSpoolBufferSizeMB 这个参数设置DiskStore(磁盘缓存)的缓存区大小.默认是30MB.每个Cache都应该有自己的一个缓冲区.
l diskPersistent - 在VM重启的时候是否启用磁盘保存EhCache中的数据，默认是false。
l diskExpiryThreadIntervalSeconds - 磁盘缓存的清理线程运行间隔，默认是120秒。每个120s，相应的线程会进行一次EhCache中数据的清理工作
l memoryStoreEvictionPolicy - 当内存缓存达到最大，有新的element加入的时候， 移除缓存中element的策略。默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出）
 -->
```

![缓存原理图](E:\typora\images\1530892409176.png)

### 二、`Spring`整合`MyBatis`

[Spring整合mybatis的文档](http://www.mybatis.org/spring/zh/getting-started.html)

1. 导入依赖的jar:
   * Spring依赖的jar
   * MyBatis依赖的jar
   * Spring-MyBatis的整合jar
   * 数据库依赖的jar
   * 缓存依赖的jar

```xml

```

2. 编写jdbc配置文件`jdbc.properties`

```properties
jdbc.Driver = com.mysql.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/vote?allowMultiQueries=true
jdbc.username = root
jdbc.password = 1234

oracle.Driver = oracle.jdbc.OracleDriver
oracle.url = jdb:oracle:thin:@localhost:1521:orcl
oracle.username = scott 
oracle.password = scott
```

3. 编写mybatis的全局配置文件mybatis-config.xml,由于与spring整合，此文件不是必须的了

一般配置一些不常用的属性或常修改的属性

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--设置主要的属性-->
    <settings>
        <!--开启自动驼峰命名规则映射-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!--设置null的jdbcType=NULL-->
        <setting name="jdbcTypeForNull" value="NULL"/>
        <!--开启懒加载-->
        <setting name="lazyLoadingEnabled" value="true"></setting>
        <setting name="aggressiveLazyLoading" value="false"></setting>
        <!--开启全局二级缓存-->
        <setting name="cacheEnabled" value="true"></setting>
    </settings>

    <databaseIdProvider type="DB_VENDOR">
        <!--为不同的数据库厂商取别名-->
        <property name="MySQL" value="mysql"></property>
        <property name="Oracle" value="oracle"></property>
        <property name="SQL Server" value="sqlserver"></property>
    </databaseIdProvider>
</configuration>
```

4. 编写Spring全局配置文件applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc"
       xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
       xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
     http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
     http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
     http://www.springframework.org/schema/aop  http://www.springframework.org/schema/aop/spring-aop.xsd
     http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
     http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd">

    <!-- Spring希望管理所有的业务逻辑组件 -->
    <context:component-scan base-package="com.xy">
        <!--排除@Controller不扫描-->
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller" />
    </context:component-scan>

    <!--引入jdbc.properties-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!--配置数据源（c3p0）,Spring用来控制业务逻辑-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.Driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!--声明式事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--自动扫描services-->
    <!--<context:component-scan base-package="com.xy.service"/>-->

    <!--开启基于注解的事务-->
    <tx:annotation-driven transaction-manager="transactionManager"/>

    <aop:config>
        <aop:advisor advice-ref="txAdvice" pointcut="execution(* *..*Service.*(..))"/>
    </aop:config>

    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--查询方法-->
            <tx:method name="get*" read-only="true"/>
            <tx:method name="select*" read-only="true"/>
            <tx:method name="query*" read-only="true"/>
            <!--插入方法-->
            <tx:method name="insert*" rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
            <tx:method name="add*" rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
            <tx:method name="save*" rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
            <!--修改方法-->
            <tx:method name="update*" rollback-for="Exception" propagation="REQUIRES_NEW"/>
            <tx:method name="edit*" rollback-for="Exception" propagation="REQUIRES_NEW"/>
            <!--删除方法-->
            <tx:method name="delete*" rollback-for="Exception" propagation="REQUIRES_NEW"/>
            <tx:method name="remove*" rollback-for="Exception" propagation="REQUIRES_NEW"/>
        </tx:attributes>
    </tx:advice>

    <!--整合MyBatis
        1.Spring管理所有的组件；mapper实现类
        2.Spring管理声明式事务-->
    <!--创建出SqlSessionFactory对象  -->
    <bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--configLocation指定Mybatis全局配置文件-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!--mapperLocations: 指定mapper文件的位置-->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
     </bean>

    <!--自动扫描mapper-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.xy.mapper"/>
    </bean>

    <!--配置一个可以进行批量执行的sqlSession-->
    <bean id="sessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg name="sqlSessionFactory" ref="sessionFactory"/>
        <constructor-arg name="executorType" value="BATCH"/>
    </bean>

    <!--扫描所有的mapper接口的实现，让这些mapper能够自动注入-->
    <!--<mybatis-spring:scan base-package="com.atguigu.mybatis.dao"/>-->
     <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
         <!--若接口和mapper.xml在同一个包下就可不指定目录-->
        <property name="basePackage" value="com.xy.mapper"></property>
    </bean>

</beans>
```

5. 编写SpringMVC配置文件springmvc-servlet.xml(与web.xml同级目录)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">

	<!--SpringMVC只是控制网站跳转逻辑  -->
	<!-- 只扫描控制器 -->
	<context:component-scan base-package="com.xy" use-default-filters="false">
		<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	</context:component-scan>
	
	<!-- 视图解析器 -->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/pages/"></property>
		<property name="suffix" value=".jsp"></property>
	</bean>

    <!--开启mvc注解-->
	<mvc:annotation-driven></mvc:annotation-driven>
	<mvc:default-servlet-handler/>
</beans>
```

6. 配置web.xml文件

```xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                      http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name>SSM</display-name>

    <!--Spring配置： needed for ContextLoaderListener -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <!--配置上下文监听-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!--SpringMVC转发器-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!--SpringMVC处理的资源-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

#### 2.1.整合mybatis-config的配置

方式一：配置在mybtais-config.xml中，再将文件引入进来

```xml
<bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <!--configLocation指定Mybatis全局配置文件-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
</bean>
```

方式二：使用SqlSessionFactoryBean的属性配置

```xml
<bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--configLocation指定Mybatis全局配置文件-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <!--mapperLocations: 指定mapper文件的位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"/>
</bean>
```

方式三：整合了mybatisplus，使用MybatisSqlSessionFactoryBean的属性配置

```xml
<bean id="sessionFactory" class="com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--configLocation指定Mybatis全局配置文件-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <!--mapperLocations: 指定mapper文件的位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    <!--mybatis的全局配置-->
    <property name="configuration" ref="mybatisConfiguration"/>
    <!--MyBatisPlus全局配置-->
    <property name="globalConfig" ref="globalConfiguration"/>
</bean>
```

方式四：整合mybatisplus，使用MybatisSqlSessionFactoryBean的关联属性

```xml
<bean id="sessionFactory" class="com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--mybatis的全局配置-->
    <property name="configuration" ref="mybatisConfiguration"/>
</bean>
```

```xml
<!--mybatis的全局配置-->
<bean id="mybatisConfiguration" class="com.baomidou.mybatisplus.MybatisConfiguration">
    <property name="mapUnderscoreToCamelCase" value="true"/>
    <!-- 部分数据库不识别默认的NULL类型（比如oracle，需要配置该属性 -->
    <property name="jdbcTypeForNull">
        <util:constant static-field="org.apache.ibatis.type.JdbcType.NULL"/>
    </property>
</bean>
```





### 三、`Mybatis`的逆向工程

1.导入依赖的jar

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.7</version>
</dependency>
```

2. 编写生成的规则mbg.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>

    <!--MYSQL配置环境,MyBatis3Simple生成简单版，MyBatis3生成豪华版-->
    <context id="mysql" targetRuntime="MyBatis3Simple">
        <!--指定连接的数据库
            driverClass:数据库驱动
            connectionURL:数据库的url
            userId:数据的用户名
            password:数据库的密码-->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/scott"
                        userId="root"
                        password="1234">
        </jdbcConnection>

        <!--设置类型解析器-->
        <javaTypeResolver >
            <!--设置是否强制转化BigDecimal-->
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!--定义生成Java模型的属性
            targetPackage:指定的生成JavaBean的目标包
            targetProject:指定生成的目标工程
            -->
        <javaModelGenerator targetPackage="com.xy.pojo" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <!--SQL映射生成策略
           targetPackage:指定生成的目标包
           targetProject:指定生成的目标项目-->
        <sqlMapGenerator targetPackage="mapper"  targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <!--设置Mapper接口生成策略
            type:
            targetPackage:指定生成的目标包
            targetProject:指定生成的目标项目-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.xy.mapper"  targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <!--指定要逆向分析哪些表
            schema:
            tableName:表的名称
            domainObjectName:生成JavaBean的名称-->
        <table  tableName="emp" domainObjectName="Emp"></table>
        <table  tableName="dept" domainObjectName="Dept"></table>

    </context>
</generatorConfiguration>
```

3. 根据规则生成

```java
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SSMTest {

    @Test
    public void test() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //设置配置文件
        File configFile = new File("mbg.xml");
        //解析配置文件
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        //回到
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        //生成
        myBatisGenerator.generate(null);
    }
}
```

### 四、`Mybatis`的插件（`Plugin`）

#### 4.1.`Plugin`的原理



#### 4.2.定义`Plugin`步骤



#### 4.3.`Plugin`实例



#### 4.4.`Plugin`的执行步骤



#### 4.5.多个`Plugin`的定义

* 多个`Plugin`就会产生多层代理
* 创建动态代理对象，是按`plugin`配置顺序层层包装对象
* 执行目标对象是从外层依次执行（逆向执行）代理对象



### 五、`PageHelper`分页插件

#### 5.1.引入jar

```xml
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.4</version>
</dependency>
```

#### 5.2.引入plugin配置

* `com.github.pagehelper.PageInterceptor`:新版拦截器 
* `com.github.pagehelper.PageHelper `：是分页插件的默认实现类（旧版）

方式一：在`mybatis-config.xml`中配置

```xml
<!--配置插件-->
<plugins>
    <!--引入PageHelper的拦截器-->
    <plugin interceptor="com.github.pagehelper.PageInterceptor">
        <!--配置分页方式，即使用哪种数据库,默认会自动检查当前连接的数据库-->
        <property name="helperDialect" value="mysql"/>
    </plugin>
</plugins>
```

方式二:在`spring`中配置

```xml
<bean id="sessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--configLocation指定Mybatis全局配置文件-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <!--mapperLocations: 指定mapper文件的位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    <!--配置插件-->
    <property name="plugins">
        <!--配置PageHelper插件-->
        <array>
            <bean class="com.github.pagehelper.PageInterceptor"></bean>
        </array>
    </property>
</bean>
```

#### 5.3.配置参数









| 常见类   | 描述     |
| -------- | -------- |
| PageInfo | 分页信息 |
| Dialect  |          |
|          |          |
|          |          |
|          |          |
|          |          |
|          |          |
|          |          |
|          |          |



### 六、批量插入



### 七、调用存储过程







### 十、`MyBatis`的问题

1. 在IDEA中使用包扫描配置异常的解决方法：在pom.xml文件中加入如下配置

```xml
<!--问题-->
<mappers>
    <package name="com.xy.mapper"/>
</mappers>
```

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
    </resources>
</build>
```



