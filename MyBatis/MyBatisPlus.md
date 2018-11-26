## 一、引入MyBatisPlus

MyBatisPlus是Mybatis的增强工具包，只做增强不做改变

POJO对象一般使用包装类型，因为基本类型有默认值，对null会产生影响，如判空时

### 1.1.依赖的jar

```xml
<!-- https://mvnrepository.com/artifact/com.baomidou/mybatis-plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus</artifactId>
    <version>2.3</version>
</dependency>
```

注意：为了避免版本的冲突，使用mybatis-plus后就不要再添加mybatis和mybatis-spring的依赖

### 1.2.配置`SqlSessionFactory`

```xml
<bean id="sessionFactory" class="com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--configLocation指定Mybatis全局配置文件-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <!--mapperLocations: 指定mapper文件的位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"/>
</bean>
```

## 二、简单`CRUD`

只需要XXXMapper继承BaseMapper即可，不需要提供SQL映射文件

```java
//默认根据类名映射表名
//@TableName表名注解，value实体对应的表名,resultMap实体映射结果集
@TableName(value = "emp",resultMap = "")
public class Emp {

    //@TableId设置主键的处理，value设置对应的表的主键，type设置主键的生成策略
    @TableId(value = "empno",type = IdType.INPUT)
    private Integer empno;
    //@TableField表字段标识,value表对应的字段,exist设置表中是否包含该字段
    @TableField(value = "ename")
    private String ename;
    @TableField(exist = false)
    private Integer age;
    private String job;
    private Integer mgr;
    private Date hiredate;
    private Double sal;
    private Double comm;
    private Integer deptno;

    public Emp() {
    }
}
```

```java
public interface EmpMapper extends BaseMapper<Emp> {

}
```

```java
public interface EmpService {
    Emp selectById();
    Emp selectOne();
    List<Emp> selectBatchIds();
    List<Emp> selectByMap();
    List<Emp> selectPage();

    Integer insert();
    Integer update();
    Integer deleteById();
    Integer deleteByMap();
    Integer deleteBatchIds();
}
```

```java
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public Emp selectById() {
        Emp emp = empMapper.selectById(7364);
        return emp;
    }

    @Override
    public Emp selectOne() {
        //查询的参数
        Emp emp = new Emp();
        emp.setEname("Jrry");
        emp.setJob("软件工程师");
        emp.setHiredate(new Date());
        //只能查询一条数据
        Emp emp1 = empMapper.selectOne(emp);
        return emp1;
    }

    @Override
    public List<Emp> selectBatchIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(3748);
        ids.add(7566);
        ids.add(7521);
        List<Emp> empList = empMapper.selectBatchIds(ids);
        return empList;
    }

    @Override
    public List<Emp> selectByMap() {
        //参数，key表的列名，value参数的值
        Map<String,Object> param = new HashMap<>();
        param.put("ename","爱丽丝");
        param.put("job","Python工程师");
        List<Emp> empList = empMapper.selectByMap(param);
        return empList;
    }

    /**
     * 使用的是Mybatis的RowBounds进行分页，即是逻辑(内存)分页，不是物理分页
     * @return
     */
    @Override
    public List<Emp> selectPage() {
        //RowBounds分页参数
        Page page = new Page<Emp>();
        //当前页码
        page.setCurrent(1);
        //当前页条数
        page.setSize(10);
        List<Emp> empList = empMapper.selectPage(page, null);
        //获取分页参数
        return empList;
    }

    @Override
    public Integer insert(){
        Emp emp = new Emp();
        emp.setEmpno(7364);
        emp.setEname("Jrry");
        emp.setJob("软件工程师");
        emp.setHiredate(new Date());
        emp.setSal(8274.00);
        emp.setDeptno(10);

        //insert语句中只会包含有值的字段
        empMapper.insert(emp);
        //不论字段是否为null,都会包含所有的字段
        empMapper.insertAllColumn(emp);
        //获取主键值
        Integer empno = emp.getEmpno();
        return empno;
    }

    @Override
    public Integer update() {
        Emp emp = new Emp();
        emp.setEmpno(7364);
        emp.setEname("BOM");
        emp.setJob("Java工程师");
        emp.setHiredate(new Date());
        emp.setSal(8274.00);
        emp.setDeptno(10);

        //只修改非空字段,为null的字段就不修改
        Integer row = empMapper.updateById(emp);
        //会修改所有的字段，为null的字段就改为null
        empMapper.insertAllColumn(emp);

        return row;
    }

    @Override
    public Integer deleteById() {
        Integer row = empMapper.deleteById(7384);
        return row;
    }

    @Override
    public Integer deleteByMap() {
        //参数，key表的列名，value参数的值
        Map<String,Object> param = new HashMap<>();
        param.put("ename","爱丽丝");
        param.put("job","Python工程师");

        Integer row = empMapper.deleteByMap(param);
        return row;
    }

    @Override
    public Integer deleteBatchIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(3748);
        ids.add(7566);
        ids.add(7521);
        //批量删除
        Integer row = empMapper.deleteBatchIds(ids);
        return row;
    }

}
```

### 2.1.主键策略

MP支持以下4中主键策略，可根据需求自行选用：

| 值               | 描述                                     |
| ---------------- | ---------------------------------------- |
| IdType.AUTO      | 数据库ID自增                             |
| IdType.INPUT     | 用户输入ID                               |
| IdType.ID_WORKER | 全局唯一ID，内容为空自动填充（默认配置） |
| IdType.UUID      | 全局唯一ID，内容为空自动填充             |

Sequence分布式高效有序ID生产黑科技工具，主要是来源于`Twitter-Snowflake算法`，ID_WORKER在Sequence的基础上进行部分优化，用于产生全局唯一ID 

### 2.2.MP启动注入SQL原理分析





## 三、条件构造器`Wrapper`

EntityWrapper封装条件的构造器，实体包装器，主要用于处理sql的拼接、排序和实体参数的查询等；

注意：使用的是数据库字段，不是Java实体类的属性

![1532958957884](E:\typora\images\1532958957884.png)

### 3.1.带条件的查询

```java
List<Emp> empList = empMapper.selectList(new EntityWrapper<Emp>()
                .eq("job", "MANAGER")
                .ge("sal", 1000));
```

#### 3.1.1.`or()`与`orNew()`区别

```java
//SELECT* FROM emp WHERE (comm IS NOT NULL OR sal >= ?)
List<Emp> empList = empMapper.selectList(
    new EntityWrapper<Emp>().isNotNull("comm").or().ge("sal", 2000));
//SELECT * FROM emp WHERE (comm IS NOT NULL) OR (sal >= ?)
List<Emp> empList = empMapper.selectList(
    new EntityWrapper<Emp>().isNotNull("comm").orNew().ge("sal", 2000));
```

### 3.2.带条件的修改

```java
Emp emp = new Emp();
emp.setEmpno(new BigDecimal("7837"));
emp.setJob("高级测试工程师");
emp.setHiredate(new Date());
emp.setEname("莫凡");
emp.setMgr(new BigDecimal("6373"));
emp.setSal(new BigDecimal(7394));
emp.setComm(new BigDecimal(242));
emp.setVersion("2");

empMapper.update(emp, new EntityWrapper<Emp>().eq("empno", 7837));
```

### 3.3.带条件的删除

```java
empMapper.delete(new EntityWrapper<Emp>().eq("job", "C++工程师"));
```

### 3.4.排序

```java
//SELECT * FROM emp ORDER BY hiredate DESC
List<Emp> empList = empMapper.selectList(
    new EntityWrapper<Emp>().orderDesc(Arrays.asList(new String[]{"hiredate"})));
List<Emp> empList = empMapper.selectList(
    new EntityWrapper<Emp>().orderBy("hiredate", false));
List<Emp> empList = empMapper.selectList(
    new EntityWrapper<Emp>().orderBy("hiredate").last("desc"));
```

### 3.5.`Condition`

```java
List list = empMapper.selectList(new Condition().groupBy("job").);
List list = empMapper.selectList(Condition.create().groupBy("job"));
```

### 3.6.自定义SQL使用Wrapper

```xml
<select id="getByDeptName" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List"/>
    from emp
    where deptno = (select deptno from dept <where>${ew.sqlSegment}</where>)
</select>
```

```java
public interface EmpMapper extends BaseMapper<Emp> {

    //查询某一部门下的所有人员
    List<Emp> getByDeptName(RowBounds rowBounds, @Param("ew")Wrapper<Emp> wrapper);

}
```

```java
@Test
public void test08(){
    List<Emp> empList = empMapper.getByDeptName(new PageRowBounds(1, 2), 
             new EntityWrapper<Emp>().eq("dname", "ACCOUNTING"));
    System.out.println(empList);
}
```

注意：`${ew.sqlSegment} `使用了 $ 不要误以为就会被 sql 注入，请放心使用 mp 内部对 wrapper 进行了字符转义处理 

## 四、全局配置

| 配置                  | 描述                                     |
| --------------------- | ---------------------------------------- |
| ` dbColumnUnderline`  | 开启下划线命名                           |
| `isCapitalMode`       | 开启大写命名                             |
| `idType`              | 主键策略配置                             |
| `tablePrefix`         | 表前缀配置                               |
| `logicDeleteValue`    | 逻辑删除全局值                           |
| `logicNotDeleteValue` | 逻辑未删除全局值                         |
| `dbType`              | 数据库类型                               |
| `sqlInjector`         | SQL注入器                                |
| `keyGenerator`        | 表关键词 key 生成器                      |
| `metaObjectHandler`   | 元对象字段填充控制器                     |
| `fieldStrategy`       | 字段验证策略                             |
| `isRefresh`           | 是否刷新mapper                           |
| `identifierQuote`     | 标识符                                   |
| `sqlSessionFactory`   | 缓存当前Configuration的SqlSessionFactory |
| `mapperRegistryCache` | 缓存已注入CRUD的Mapper信息               |
| `sqlSession`          | 单例重用SqlSession                       |
| `sqlParserCache`      | 缓存 Sql 解析初始化                      |

```xml
<bean id="sessionFactory" class="com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <!--configLocation指定Mybatis全局配置文件-->
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <!--mapperLocations: 指定mapper文件的位置-->
    <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    <!--MyBatisPlus全局配置-->
    <property name="globalConfig" ref="globalConfiguration"/>
</bean>
```

```xml
<!--MybatisPlus全局策略配置-->
<bean id="globalConfiguration" class="com.baomidou.mybatisplus.entity.GlobalConfiguration">
    <!--开启下划线配置，2.3以后默认值为true,针对表字段-->
    <property name="dbColumnUnderline" value="true"/>
    <!--设置全局的主键生成策略-->
    <property name="idType" value="1"/>
    <!--表前缀配置-->
    <property name="tablePrefix" value="tb_"/>
</bean>
```

##  五、活动记录(ActiveRecord)

活动记录(ActiveRecord)是一种领域模型模式，特点一个模型类对应关系型数据库中的一个表，而模型类的一个实例对应表中的一行记录

### 5.1.使用AR

```java
public class Dept extends Model<Dept>{

    private Integer deptno;
    private String dname;
    private String loc;

    public Dept() {
    }
		
    //设置实体类对应表的主键属性
    @Override
    protected Serializable pkVal() {
        return deptno;
    }
    ...
}
```



## 六、代码生成器

### 6.1.模板引擎

`AbstractTemplateEngine`生成代码的模板

![1533027422315](E:\typora\images\1533027422315.png)

```xml
<!-- https://mvnrepository.com/artifact/org.apache.velocity/velocity-engine-core -->
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>2.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.freemarker/freemarker -->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.28</version>
</dependency>

<!-- sfl4j -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.7</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.7</version>
</dependency>
```

### 6.2.生成代码的模板

```java
public class MpGenerator {

    @Test
    public void generator() {
        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir("E:\\IDEA\\ssm")  //设置输出的目录
            .setAuthor("xiaoyong") //设置作者
            .setFileOverride(true) //多次生成是否覆盖原来的文件
            .setActiveRecord(true) //是否设置活动记录AR
            .setIdType(IdType.AUTO) //设置主键生成策略
            .setEnableCache(true) //设置是否开启缓存
            .setBaseColumnList(true) //设置生成字段的SQL片段
            .setBaseResultMap(true)  //生成基本的ResultMap
            .setServiceName("%sService"); //设置文件名称格式

        //数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL) //设置数据库的类型
            .setDriverName("com.mysql.jdbc.Driver") //数据库驱动
            .setUrl("jdbc:mysql://localhost:3306/scott") //数据库URL
            .setUsername("root") //数据库用户名
            .setPassword("1234") //数据库密码
            .setTypeConvert(new MySqlTypeConvert()); //数据库表字段类型转换

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true) //全局大写，Oracle
            .setTablePrefix("tb_") //设置表名的前缀
            .setNaming(NamingStrategy.underline_to_camel) //设置表名生成策略
            .setColumnNaming(NamingStrategy.underline_to_camel) //设置列名生成策略
            .setInclude("emp", "dept")  //设置包含的表
            .setExclude(new String[]{}); //设置排除的表

        //包名策略
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.xy")//设置包结构的公共部分
            .setController("controller") //controller的包
            .setService("service") //service的包
            .setServiceImpl("service.impl") //service实现类的包
            .setEntity("pojo") //实体类的包
            .setMapper("mapper"); //xxx.mapper

        AutoGenerator autoGenerator = new AutoGenerator();
        //设置代码生成的模板，默认为Veloctiy
        //autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.setGlobalConfig(globalConfig)  //全局配置
            .setDataSource(dataSourceConfig) //数据源配置
            .setStrategy(strategyConfig)  //策略配置
            .setPackageInfo(packageConfig); //包配置

        autoGenerator.execute();
    }
}

```

### 6.3.自定义模板







## 七、插件的使用

### 7.1.分页插件

`PaginationInterceptor`分页插件,配置分页的属性

| `PaginationInterceptor`属性 | 描述                                                       |
| --------------------------- | ---------------------------------------------------------- |
| `sqlParser`                 | COUNT SQL 解析， 版本 2.0.9 改为使用 jsqlparser 不需要配置 |
| `overflowCurrent`           | 溢出总页数，设置第一页                                     |
| `dialectType`               | 方言类型                                                   |
| `dialectClazz`              | 方言实现类                                                 |
| `localPage`                 | 是否开启 PageHelper localPage 模式                         |

```xml
<property name="plugins">
    <list>
        <!--配置MybatisPlus的分页插件-->
        <bean class="com.baomidou.mybatisplus.plugins.PaginationInterceptor"/>
    </list>
</property>
```

```java
List<Emp> empList = empMapper.selectPage(new Page<Emp>(2, 5), null);
List<Emp> empList = empMapper.selectPage(new Page<Emp>(2, 5,"hiredate",false), null);
```

#### 7.1.1.自定义的方法分页

```xml
<select id="getByDeptName" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List"/>
    from emp
    where deptno = (select deptno from dept <where>${ew.sqlSegment}</where>)
</select>
```

```java
public interface EmpMapper extends BaseMapper<Emp> {
    //查询某一部门下的所有人员
    List<Emp> getByDeptName(RowBounds rowBounds, @Param("ew")Wrapper<Emp> wrapper);
}
```

```java
List<Emp> empList = empMapper.getByDeptName(new Page<Emp>(2, 3), 
              new EntityWrapper<Emp>().eq("dname", "ACCOUNTING"));
```

注意：mybatis-plus 自动替你分页，加上分页语句

### 7.2.执行分析插件

`SqlExplainInterceptor`SQL分析插件，目前只支持 MYSQL-5.6.3 以上版本

作用是分析处理 DELETE UPDATE 语句， 防止小白或者恶意 delete update 全表操作 

```xml
<!--配置插件-->
<property name="plugins">
    <list>
        <!--注册SQL执行分析插件-->
        <bean class="com.baomidou.mybatisplus.plugins.SqlExplainInterceptor">
            <!--若出现对全表的删除和更新就停止操作-->
            <property name="stopProceed" value="true"/>
        </bean>
    </list>
</property>
```

注意：该插件只用于开发环境，不建议生产环境使用 

### 7.3.性能分析插件

`PerformanceInterceptor`性能分析插件，用于输出每条 SQL 语句及其执行时间 

```xml
<!--配置插件-->
<property name="plugins">
    <list>
        <!--注册性能分析插件-->
        <bean class="com.baomidou.mybatisplus.plugins.PerformanceInterceptor">
            <!--是否写入日志文件，true 写入日志文件，不阻断程序执行！<br>
                超过设定的最大执行时长异常提示！-->
            <property name="writeInLog" value="true"/>
            <!--设置sql指定执行时间-->
            <property name="maxTime" value="100"/>
            <!--是否格式化SQL-->
            <property name="format" value="true"/>
        </bean>
    </list>
</property>
```

注意：该插件只用于开发环境，不建议生产环境使用 



### 7.4.乐观锁插件

`OptimisticLockerInterceptor`乐观锁插件，意图：当要更新一条记录的时候，希望这条记录没有被别人更新 

乐观锁实现方式：

- 取出记录时，获取当前version
- 更新时，带上这个version
- 执行更新时， set version = yourVersion+1 where version = yourVersion
- 如果version不对，就更新失败

```xml
<!--配置插件-->
<property name="plugins">
    <list>
        <!--注册乐观锁插件-->
        <bean class="com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor"/>
    </list>
</property>
```

#### 7.4.1.使用

```java
public class Emp extends Model<Emp> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "EMPNO")
    private BigDecimal empno;
    private String ename;
    private String job;
    private BigDecimal mgr;
    private Date hiredate;
    private BigDecimal sal;
    private BigDecimal comm;
    private BigDecimal deptno;

    @Version
    private String version;
    ...
}
```

```java
@Test
public void test11(){
    Emp emp = new Emp();
    emp.setEmpno(new BigDecimal("7837"));
    emp.setJob("高级测试工程师");
    emp.setHiredate(new Date());
    emp.setEname("莫凡");
    emp.setMgr(new BigDecimal("6373"));
    emp.setSal(new BigDecimal(7394));
    emp.setComm(new BigDecimal(242));
    emp.setVersion("1");

    //注意updateById能实现乐观锁控制，update(emp，Wrapper)有些问题
    Integer row = empMapper.updateById(emp);
    System.out.println(row);
}
```

更新的生成的SQL语句

```sql
   UPDATE
        emp 
    SET
        ename='莫凡',
        job='高级测试工程师',
        mgr=6373,
        hiredate='2018-08-01 17:04:02.8',
        sal=7394,
        comm=242,
        version='1' 
    WHERE
        EMPNO=7837 
        and version='1';
```

注意：更新的数据库表和对应的实体类需要提供版本控制字段；@Vsersion仅支持int,Integer,long,

Long,Date,Timestamp

### 7.5.注入自定义SQL

在项目启动时注入自定义的SQL

```java
public interface EmpMapper extends BaseMapper<Emp> {
    Integer deleteAll();
}
```

```java
public class MySqlInjector extends AutoSqlInjector {

    /**
     * 设置注入的SQL
     *
     * @param configuration
     * @param builderAssistant
     * @param mapperClass      Mapper接口对应的类
     * @param modelClass       参数类型
     * @param table            数据库表的信息
     */
    @Override
    public void inject(Configuration configuration, MapperBuilderAssistant builderAssistant, Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        //注入的SQL
        String sql = "delete from " + table.getTableName();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        //方法名，有XXXMapper接口中的方法名一致
        String method = "deleteAll";
        //addMappedStatement(mapperClass, method, sqlSource, SqlCommandType.DELETE, Integer.class);
        addDeleteMappedStatement(mapperClass, method, sqlSource);
    }
}
```

```xml
<!-- 定义 MP 全局策略，安装集成文档部分结合 -->
<bean id="globalConfig" class="com.baomidou.mybatisplus.entity.GlobalConfiguration">
    .....

  <!-- 自定义注入 deleteAll 方法  -->
  <property name="sqlInjector" ref="mySqlInjector" />
</bean>

<!-- 自定义注入器 -->
<bean id="mySqlInjector" class="com.baomidou.test.MySqlInjector" />
```

### 7.6.公共字段的填充

`IMetaObjectHandler `元数据处理接口

```xml
<bean id="sessionFactory" class="com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean">
    <!--MyBatisPlus全局配置-->
    <property name="globalConfig" ref="globalConfiguration"/>
</bean>

<!--MybatisPlus全局策略配置-->
<bean id="globalConfiguration" class="com.baomidou.mybatisplus.entity.GlobalConfiguration">
    <!--公共字段填充器-->
    <property name="metaObjectHandler" ref="myMetaObjectHandler"/>
</bean>

<!--自定义的字段填充器-->
<bean id="myMetaObjectHandler" class="com.xy.util.MyMetaObjectHandler"/>
```

```java
public class Emp extends Model<Emp> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "EMPNO")
    private BigDecimal empno;
    private String ename;
    private String job;
    private BigDecimal mgr;
    //设置填充的类型
    @TableField(fill = FieldFill.INSERT)
    private Date hiredate;
    private BigDecimal sal;
    private BigDecimal comm;
    private BigDecimal deptno;
    @Version
    private String version;
    ...
}
```

```java
//自定义元数据处理器，在insert和update之前执行
public class MyMetaObjectHandler extends MetaObjectHandler {

    /**
     * insert语句填充的字段
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置hiredate默认值为当前时间
        setFieldValByName("hiredate",new Date(),metaObject);
    }

    /**
     * update语句填充的字段
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //设置hiredate默认值为当前时间
        setFieldValByName("hiredate",new Date(),metaObject);
    }
}
```

此时在insert和update时，若设置了值就使用设置的值，若未设置值就使用默认值

注意：若字段不需要设置值，但又设置了默认值，此时就出错了

### 7.7.逻辑删除

```xml
<bean id="globalConfig" class="com.baomidou.mybatisplus.entity.GlobalConfiguration">
    <!--逻辑删除的值-->
    <property name="logicDeleteValue" value="-1"/>
    <!--逻辑不删除的值-->
    <property name="logicNotDeleteValue" value="1"/>
    <!--sql注入器-->
    <property name="sqlInjector" ref="logicSqlInjector"/>
</bean>

<!-- 逻辑删除SQL注入器 -->
<bean id="logicSqlInjector" class="com.baomidou.mybatisplus.mapper.LogicSqlInjector" />
```

```java
public class Emp extends Model<Emp> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "EMPNO")
    private BigDecimal empno;
    private String ename;
    private String job;
    private BigDecimal mgr;
    //设置填充的类型
    @TableField(fill = FieldFill.INSERT)
    private Date hiredate;
    private BigDecimal sal;
    private BigDecimal comm;
    private BigDecimal deptno;
    @Version
    private String version;
	
    //逻辑删除字段
    @TableField(value = "delete_flag")
    @TableLogic
    private String deleteFlag;
    ...
}
```

```java
Integer row = empMapper.deleteById(new BigDecimal(3751));
```

```sql
 UPDATE
        emp 
    SET
        delete_flag='-1' 
    WHERE
        EMPNO=3751;
```

```java
 List<Emp> empList = empMapper.selectList(null);
```

```sql
 SELECT
        EMPNO AS empno,
        ename,
        job,
        mgr,
        hiredate,
        sal,
        comm,
        deptno,
        version,
        delete_flag AS deleteFlag 
    FROM
        emp 
    WHERE
        delete_flag='1'
```

注意：操作的数据库表和对应的尸体类需要提供逻辑删除字段。

### 7.8.`Sequence`主键

oracle的支持的主键策略sequence配置

1. 设置主键的策略为IdType.INPUT 
2. 在实体类上设置使用的序列

```java
//设置使用的序列，value序列名称，clazz指定主键值Java类型
@KeySequence(value = "SEQ_EMPNO",clazz = BigDecimal.class)
public class Emp extends Model<Emp> {

    private static final long serialVersionUID = 1L;

    //设置主键策略为IdType.INPUT
    @TableId(value = "EMPNO",type = IdType.INPUT)
    private BigDecimal empno;
    private String ename;
    private String job;
    private BigDecimal mgr;
    //设置填充的类型
    @TableField(fill = FieldFill.INSERT)
    private Date hiredate;
    private BigDecimal sal;
    private BigDecimal comm;
    private BigDecimal deptno;

    @Version
    private String version;
    ...
}
```

```xml
<bean id="globalConfig" class="com.baomidou.mybatisplus.entity.GlobalConfiguration">
    <!--主键Sequence-->
    <property name="keyGenerator" ref="keyGenerator"/>
</bean>

<!-- 配置oracle主键Sequence， 其他类型数据库，请配置相应的类型-->
<bean id="keyGenerator" class="com.baomidou.mybatisplus.incrementer.OracleKeyGenerator"/>
```

- 支持父类定义@KeySequence, 子类使用，这样就可以几个表共用一个Sequence

```java
@KeySequence("SEQ_TEST")
public abstract class Parent{

}

public class Child extends Parent{

}
```

## 八、自定义全局操作



