## 一、复制表或数据

1、只复制表结构

```sql
create table users_tmp
select * from users limit 0;

create table users_tmp
as 
select * from users where 1！=1;

create table users_tmp like users;
```

```sql
--like能将一个表完全复制成一个新表，包括复制表的备注、索引、主键外键、存储引擎等，但是不复制数据
CREATE [TEMPORARY] TABLE [IF NOT EXISTS] table_name
{like old_table_name | (like old_table_name)}

create table if not exists users_tmp  
like users;
```

```sql
--此方式只复制字段属性，其它的主键、索引、表备注、存储引擎都没有复制
CREATE [TEMPORARY] TABLE [IF NOT EXISTS] tbl_name
SELECT ...   (Some valid select or union statement)
```

2、复制表结构及其数据

```sqll
create table users_tmp
as
select * from users;
```

3、两个表结构相同，复制表格的数据

```sql
insert into users_tmp
select * from users;
```

4、两个表的结构不同，复制部分列的数据

```sql
insert into users_tmp(name,age)
select name,age from users;
```

5、查看创建表的语句

```sql
show create table table_name;
--此时可以通过创建语句来复制表
```

6、不同数据库之间复制表

```sql
insert into db1.users      --数据库db1
select * from db2.users;   --数据库db2
```

7、复制数据库





## 二、导入导出

1、导出数据库

```sql
--导出SQL文件,显式输入密码
mysqldump -uroot -ppassword database_name>database_name.sql
--隐式输入密码
mysqldump -uroot -p database_name>database_name.sql  --回车后输入密码
```

导出语句加上（--opt）区别

* 建表语句之前会包含`drop table if exists table_name`
* `insert`语句之前包含一个锁表语句`lock tables table_name write`，`insert`之后包含解锁语句`unlock tables table_name`

```dos
--导入SQL脚本
mysqldump -urrot -p --opt databse_name > database_name.sql
```

2、导入数据库

```sql
--1.连接数据库
mysql -uroot -ppassowrd
--2.创建数据库
create database database_name;
--3.切换数据库
use database_name;
--4.设置数据库编码
set names utf8;
--5.执行导出的SQL
source F:\database_name.sql
```

```dos
--不登录数据导入数据
mylsql -uroot -ppassword --default-character-set=utf-8 database_name < f:\database_name.sql
--隐式输入密码
mylsql -uroot -p --default-character-set=utf-8 database_name < f:\database_name.sql 
```

3、跨主机之间备份数据库

```dos
--将主机（host1）中的数据库（sourceDb）备份到主机（host2）中的数据库（targetDb）,数据库targetDb需要已经存在
mysqldump --host=192.168.1.32 --opt sourceDb | mysql --host=192.168.1.24 -C targetDb
--（-C）表示主机之间数据传输使用数据压缩
```

4、只备份表结构

```dos
--(--databases)指示主机上要备份的数据库
mysqldump --no-data --databases database1 database2 > test.sql
--备份主机上所有的数据库
mysqldump --no-data --all-database > test.sql
```

5、导出指定的表

```sql

```

6、执行`sql`脚本

```sql
source f:/file_name.sql
\. f:/file_name.sql
```

## 三、mysqldump

1、mysqldump的选项

* `--help`;帮助语句

  ```dos
  mysqldump --help
  mysqldump -?
  ```


* `--all-database,-A`；备份所有数据库

  ```dos
  mysqldump -uroot -p --all-databases > test.sql
  ```

* `--all-tablespaces,-Y`;导出全部表空间

  ```dos
  mysqldump -utoor -p --all-tablespace
  ```

* `--database,-B`;备份多个数据库

* `--force,-f`；即使发现SQL错误，任然继续备份

* `--no-data，-d`;只导出表结构

* `--password[=paaword],-p[password]`;密码

* `--port=port_num,-P port_num`;指定TCP/IP连接时的端口号

* `--quick,-q`;开始导出

* `--tables`；会覆盖`--datatbases,-B`选项，后面的参数是做表名

* `--user=user_name,-u user_name`;用户名

* `--xml,-X`;导出xml文件

* `--add-drop-database`;每个数据库创建之前添加删除数据库语句`（drop database database_name）`

  ```dos
  mysqldump -uroot -p --all-databases --add-drop-database
  ```

* `--add-drop-table`;每个数据表创建之前添加删除数据库表的语句`(drop table table_name)`


  ```dos
  --默认的添加删除语句，使用（--skip-add-drop-table）取消选项
  --有drop语句
  mysqldump -uroot -p --all-databases  
  --没有drop语句
  mysqldump -uroot -p --all-databases --skip-add-drop-table   
  ```

* `--all-locks`;在每个表导出之前添加锁表语句，并在之后添加解锁表语句

  默认为打开状态，`--skip-drop-table`取消选项

  ```dos
  mysqldump -uroot -p --all-databases
  --取消lock语句
  mysqldump -uroot -p --all-databases --skip-add-locks 
  ```

* `--comments`；附加注释信息，

  默认打开，`--skip-comments`取消

  ```dos
  --默认记录注释
  mysqldump -uroot -p --all-databases 
  --取消注释
  mysqldump -uroot -p --all-databases --skip-comments 
  ```

* `--no-create-info, -t`;只导出数据，而不添加`CREATE TABLE `语句。

  ```dos
  mysqldump -uroot -p --host=localhost --all-databases --no-create-info
  ```

* `--no-create-db, -n`;只导出数据，而不添加`CREATE DATABASE` 语句

  ```dos
  mysqldump -uroot -p --host=localhost --all-databases --no-create-db
  ```


## 四、密码

```sql
taskkill /f /im mysqld-nt.exe
```



## 五、cmd

1. 启动`mysql`服务

```sql
--net start + 服务名 ：启动Windows中服务
net start mysql 
--net stop + 服务名 ：关闭Windows中服务
net stop mysql
```

2. 异常`(net start MySQL57  发生系统错误 5。)`  

> 使用管理员启动命令行

3. 异常`(net start MySQL 服务名无效)`

> 可能是服务名错误 net start mysql57

4. `shift`+右键，快速打开`cmd`窗口

## 六、数据字典

 1、查看数据库的版本

```sql
--命令查看
--使用系统函数查看数据库版本
select version();
select @@version;
--查看status
status
-- SHOW VARIABLES 命令查看
show variables like '%version%';
```

```shell
mysqladmin -uroot -p -hlocalhost version;
rpm -qa | grep -i mysql
mysql --help | grep distrib
mysql -V
mysql --version
```

2、显示所有的数据库

```sql
show databases;
```

3、查看当前使用的数据库

```sql
select database();
```

4、查看数据库使用的端口

```sql
show variables like 'port';
```

5、查看数据库的大小

```sql
use information_schema
--查看数据的大小
select 
    concat(round(sum(data_length)/(1024*1024),2) + round(sum(index_length)/(1024*1024),2),'MB') as 'DB Size' 
from tables 
where table_schema = 'jyrc_zwb';  --数据库名称
--查看数据所占空间大小
select concat(round(sum(data_length)/(1024*1024),2),'MB') as 'DB Size'
from tables
where table_schema='jyrc_zwb';
--查看索引所占空间大小
select concat(round(sum(index_length)/(1024*1024),2),'MB') as 'DB Size' 
from tables 
where table_schema='jyrc_zwb';
```

6、查看数据库的编码

```sql
show variables like '%character%'；
```

```
+--------------------------+---------------------------------------------------------+
| Variable_name            | Value                                                   |
+--------------------------+---------------------------------------------------------+
| character_set_client     | utf8                                                    |
| character_set_connection | utf8                                                    |
| character_set_database   | utf8                                                    |
| character_set_filesystem | binary                                                  |
| character_set_results    | utf8                                                    |
| character_set_server     | utf8                                                    |
| character_set_system     | utf8                                                    |
| character_sets_dir       | C:\Program Files\MySQL\MySQL Server 5.7\share\charsets\ |
+--------------------------+---------------------------------------------------------+
```

```
character_set_client        : 客户端编码方式
character_set_connection    : 建立连接使用的编码
character_set_database      : 数据库的编码
character_set_filesystem    : 
character_set_results       : 结果集的编码
character_set_server        : 数据库服务器的编码
character_set_system
character_sets_dir
```

7、查看数据库中的表

```sql
show tables;
select * from information_schema.tables where table_schema='databasename';
--查看表的具体信息
select * from information_schema.tables where table_name ='table_name'
```

8、查看数据库的所有用户信息 

```sql
select distinct concat('user: ''',user,'''@''',host,''';') as query from mysql.user;
```

9、查看某个具体用户的权限

```sql
show grants for 'root'@'localhost';
```

10、查看数据库的最大连接数

```sql
show variables like '%max_connections%';
```

11、查看数据库当前连接数，并发数

```sql
show status like 'Threads%';
```

```
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| Threads_cached    | 1     |
| Threads_connected | 1     |
| Threads_created   | 2     |
| Threads_running   | 1     |
+-------------------+-------+
```

```sql
Threads_cached      :  代表当前此时此刻线程缓存中有多少空闲线程
Threads_connected   :  代表当前已建立连接的数量，因为一个连接就需要一个线程，所以也可以看成当前被使用的线程数
Threads_created     :  代表从最近一次服务启动，已创建线程的数量
Threads_running     :  代表当前激活的（非睡眠状态）线程数。并不是代表正在使用的线程数，有时候连接已建立，但是连接处于sleep状态，这里相对应的线程也是sleep状态
```

12 、查看数据文件存放路径

```sql
show variables like '%datadir%';
```



```sql
show global status;
```



## 七、 `MYSQL`的`show`命令

```sql
--查看数据库
show databases;

--查看当前数据库的所有表
show tables;
show tables from bolg;

--查看表结构
desc orders;
describe orders;
show columns from orders;
--查看指定数据库中的指定表结构
show columns from orders from blog;
show columns from blog.orders;

--查看建表语句
show create table orders;
--查看创建数据库语句
show create database blog;
--创建某个存储过程的语句
show create procedure procedure_name;

--查看指定数据库中表状态
show table status from blog;
--查看当前数据库中表状态
show table status;

--查看系统中正在运行的所有进程，也就是当前正在执行的查询
--大多数用户只能查看他们自己的进程，但如果他们拥有process权限，就可以查看所有人的进程，包括密码
show processlist;

--显示用户的权限，结果为授权语句
show grants for root@localhost;
--显示服务器所支持的权限
show privileges;

--查看表的索引
show index from orders;

--查看系统变量及其值
show variables;
--显示一些特定的系统资源信息
show status;

--查看默认的引擎和可用引擎
show engines;

--查看引擎状态
show innodb status;  --低版本
show engine innodb status;
--查看引擎日志
show engine innodb  logs;

--显示最后一个执行的语句所产生的错误、警告和通知
show warnings;       
--显示最后一个执行的语句所产生的错误
show errors;

--查看存储过程的状态
show procedure status;
```

```sql
--show variables 命令查看当前参数的值,like 'pattern'用于模式匹配，查找指定的参数
show variables like '%sort_buffer_size%';

--用set session命令设置会话级变量的新值
--修改会话级变量对当前会话来说立刻生效
--退出重新连接后，此参数恢复原值
set session sort_buffer_size=7000000;

--修改全局变量,用set global 命令设置全局变量
--修改全局变量不立即生效
--退出重新连接后，参数才生效
 set global sort_buffer_size = 7000000;

--注意：修改全局变量或会话级变量，在重启服务器后都会失效；若需在重启的时候载入，需要修改配置文件
```

### 7.1.查看定义的过程

```sql
select `name` from mysql.proc where db = 'chat_system' and `type` = 'PROCEDURE';
show procedure status;
-- 查看过程的定义
show create procedure createChildList;
```

### 7.2.查看定义的函数

```sql
select * from mysql.proc where db = 'chat_system' and `type` = 'FUNCTION';
show function status;
-- 查看函数的定义
show create function getChildList;
```





## 八 、系统数据库

* `information_schema`: 信息数据库，保存着关于MySQL服务器所维护的所有其他数据库的信息，61个表，提供了访问数据库元数据（元数据是关于数据的数据，如数据库名或表名，列的数据类型，或访问权限等）的方式

* `mysql`:31个表

* `performance_schema`:87个表，用于收集数据库服务器性能参数；并且库里表的存储引擎均为`PERFORMANCE_SCHEMA`，而用户是不能创建存储引擎为`PERFORMANCE_SCHEMA`的表 

* `sys`: 1个表，100个视图


### 8.1.`information_schema`

```sql
--查看进程列表
select * from processlist; 

--当前mysql实例中所有数据库的信息，show databases;
select * from schemata;
--数据库中的表的信息（包括视图） show tables from schemaname;
select * from tables;
--表中的列信息,show columns from schemaname.tablename;
select * from columns;
--表索引的信息,show index from schemaname.tablename;
select * from statistics;
--关于存储子程序（存储程序和函数）的信息,不包含自定义函数（UDF）
select * from routines;
--视图的信息,需要有show views权限，否则无法查看视图信息
select * from views;
--触发程序的信息,必须有super权限才能查看该表
select * from triggers;

--数据库权限，信息来自mysql.db授权表，非标准表
select * from schema_privileges;
--用户权限,给出了关于全程权限的信息,信息源自mysql.user授权表,非标准表
select * from user_privileges;
--表的权限，信息源自mysql.tables授权表，非标准表
select * from table_privileges;
--列权限，信息源自mysql.columns_priv授权表，非标准表
select * from column_privileges;

--mysql实例可用字符集的信息,show character set;
select * from character_sets;  
--关于各字符集的对照信息
select * from collations;  
--可用于校对的字符集,show collation;
select * from collation_character_set_applicability;

--描述了存在约束的表，以及表的约束类型
select * from table_constraints;
--描述了具有约束的键列
select * from key_column_usage;
```

### 8.2.`performance_schema`

    `PERFORMANCE_SCHEMA`这个功能默认是关闭的。需要设置参数： `performance_schema `才可以启动该功能，这个参数是静态参数，只能写在`my.cnf `中 ，不能动态修改。   

```sql
-- 查看performance_schema是否开启，从MySQL5.6开始，默认打开
show variables like 'performance_schema';
--开启performance_schema，在配置文件（my-default.ini）中添加
performance_schema=ON
```

```sql
--查看配置表
show tables like '%setup%';

--配置events的消费者类型，即收集的events写入到哪些统计表中
--更新完后立即生效，但是服务器重启之后又会变回默认值,要永久生效需要在配置文件里添加
select * from setup_consumers;

--配置具体的instrument，主要包含4大类：idle、stage/xxx、statement/xxx、wait/xxx
--idle表示socket空闲的时间
--stage类表示语句的每个执行阶段的统计
--statement类统计语句维度的信息
--wait类统计各种等待事件，比如IO，mutux，spin_lock,condition等。
select * from setup_instruments;

--配置监控对象，默认对mysql，performance_schema和information_schema中的表都不监控，而其它DB的所有表都监控。
select * from setup_objects;


--配置每种类型指令的统计时间单位。MICROSECOND表示统计单位是微妙，CYCLE表示统计单位是时钟周期，时间度量与CPU的主频有关，NANOSECOND表示统计单位是纳秒。但无论采用哪种度量单位，最终统计表中统计的时间都会装换到皮秒。（1秒＝1000000000000皮秒）
select * from setup_timers;
```

### 8.3. `mysql`







## 九、参数

### 9.1. 修改参数值

```sql
--show variables 命令查看当前参数的值,like 'pattern'用于模式匹配，查找指定的参数
show variables like '%sort_buffer_size%';

--用set session命令设置会话级变量的新值
--修改会话级变量对当前会话来说立刻生效
--退出重新连接后，此参数恢复原值
set session sort_buffer_size=7000000;

--修改全局变量,用set global 命令设置全局变量
--修改全局变量不立即生效
--退出重新连接后，参数才生效
 set global sort_buffer_size = 7000000;

--注意：修改全局变量或会话级变量，在重启服务器后都会失效；若需在重启的时候载入，需要修改配置文件
performance_schema=ON
```

```sql
--配置events的消费者类型，即收集的events写入到哪些统计表中
select * from setup_consumers;

--更新完后立即生效，但是服务器重启之后又会变回默认值,要永久生效需要在配置文件里添加
[mysqld]
#performance_schema
performance_schema_consumer_events_waits_current=on
performance_schema_consumer_events_stages_current=on
performance_schema_consumer_events_statements_current=on
performance_schema_consumer_events_waits_history=on
performance_schema_consumer_events_stages_history=on
performance_schema_consumer_events_statements_history=on

--表setup_consumers里面的值有个层级关系
global_instrumentation 
> thread_instrumentation = statements_digest 
> events_stages_current = events_statements_current = events_waits_current 
> events_stages_history = events_statements_history = events_waits_history 
> events_stages_history_long = events_statements_history_long = events_waits_history_long

--只有上一层次的为YES，才会继续检查该本层为YES or NO。global_instrumentation是最高级别consumer，如果它设置为NO，则所有的consumer都会忽略
--

```

## 十、函数

#### 10.1.`uuid()`

```sql
--生成uuid,59563f66-696c-11e8-9636-00ffa13e13e9
select uuid();
--去除"-"的uuid
select replace(uuid(),'-','');
```

#### 10.2.`ifnull()`

```sql
--if(exp1,exp2) 若exp1不为空，值为exp1；若exp1为空，值为exp2；
select ifnull(1,0);      --1
select ifnull(null,1);   --1
```

#### 10.3.`isnull()`

```sql
--isnull(exp) 若exp==null，返回1;若exp!=null,返回0
select isnull(2);   --0
select isnull(1/0); --1
```

#### 10.4.`nullif()`

```sql
--nullif(exp1,exp2) 若exp1==exp2,返回null;若exp1!=exp2，返回exp1
select nullif(1,1); --null
select nullif(1,2);  --1
```

#### 10.5.`if()`

```sql
--if(condition,exp1,exp2);若condition=true,取exp1;若condition=false,取exp1;
select * from users order by if(ISNULL(idcard),1,0) 
```

#### 10.6.数学函数

```sql
--abs();绝对值
select abs(-32);  --32

--mod(n,m);取余
select mod(15,7); --15%7

--floor(x);向下取整
select floor(2.323);  --2

--geiling(x);向上取整
select geiling(2.312);  --3

--round(x);四舍五入
select round(3.5522);  --4
```

#### 10.7.`ascii(str)`

```sql
--ascii(str);返回str最左边字符的ascii代码值;若str='',返回0;若str=null,返回null
select ascii('2');  --50
```

#### 10.8.`concat(str1,str2,...)`

```sql
--concat(str1,str2,...);若有一个参数为null,就返回null;

select concat('My','S','QL');   --MySQL
select concat('My',null,'QL');  --null
```

##### 10.8.1.`concat_ws(separator,str1,str2,...)`

```sql
-- concat_ws(concat with separator)可指定连接符的连接函数

-- 若连接符（separator）为null,返回null
select concat_ws(null,'My','S','QL');   -- null

select concat_ws('~','My',null,'QL');   -- null
```

##### 10.8.2.`group_concat()`

```sql
-- 查询某一分组下的某一字段的所有的取值
group_concat( [distinct] 要连接的字段 [order by 排序字段 asc/desc  ] [separator '分隔符'] )
-- separator是一个字符串值，缺省为一个逗号

-- 查询相同父级菜单的所有菜单（,分隔）
select
	pmenuid,
	group_concat( menuname order by menuid separator  ',' )  
from tamenu
group by pmenuid;
```







#### 10.9.`locate(substr,str)`

```sql
--locate(substr,str);返回子串substr在字符串str中第一次出现的位置;若子串substr不在str里,返回0
select locate('bar','foorbar');   --5
select locate('xbar','foorbar');  --0
```

#### 10.10.`instr(str,substr)`

```sql
--instr(str,substr);返回子串substr在字符串str中第一次出现的位置;若子串substr不在str里,返回0
select instr('foorbar','bar');   --5
```

#### 10.11.`left(str,len)`

```sql
--left(str,len);截取字符串str最左侧的len个字符
select left('forbarbar',5);  --forba
```

#### 10.12.`right(str,len)`

```sql
--right(str,len);截取字符串str最右侧的len个字符
select right('forbarbar',5); --arbar
```

#### 10.13.`substring(str,pos)`

```sql
--substring(str,pos);返回字符串str中[pos-length]的字符
select substring('helloworld',5); --oworld
```

#### 10.14.`trim(str)`

```sql
--trim(str);去除所有的前后空格
select trim('  bar  ');
```

#### 10.15.`ltrim(str)`

```sql
--ltrim(str);去除前置空格
select ltrim(' bar')
```

#### 10.16.`rtrim(str)`

```sql
--rtrim(str);去除最右侧的空格
select rtrim(' bar ');
```

#### 10.17.`replace(str,from_str,to_str)`

```sql
--replace(str,from_str,to_str);替换字符
select replace('www.mysql.com','www','http://wwww');
```

#### 10.18.`repeat(str,count)`

```sql
--repeat(str,count);返回重复count次str的字符串;若count<0;返回空字符（''）;若str=null或count=null,返回null;
select repeat('MySQl',2);  --MySQLMySQL
select repeat('MySQL',-1); --''
select repeat(null,4);     --null
```

#### 10.19.`reverse(str)`

```sql
--reverse(str);颠倒字符串str
select reverse('abc');  --cba
```

#### 10.20.`insert(str,pos,len,newstr)`

```sql
--insert(str,pos,len,newstr);将str的[pos-(pos+len)]的子串替换为newstr
select insert('whatareyou',5,3,'is');  --whatisyou
```

#### 10.21.`dayofweek(date)`

```sql
--dayofweek(date);返回日期的星期几的索引
select dayofweek('2018-06-06');   --4（星期三）;1-星期天,2-星期一
select dayofweek(now());          --3
```

#### 10.22.`now()`

```sql
--now();返回当前日期的字符串,格式2018-06-06 20:12:10
select now();

--CURRENT_DATE();返回当前日期,格式：YYYY-MM-DD或YYMMDD
select CURRENT_DATE();  --2018-06-06
select CURDATE();       --2018-06-06    

--CURRENT_TIME();返回当前时间,格式：HH:MM:SS或HHMMSS
select CURRENT_TIME();  --21:13:04
select curtime();       --21:13:04
```

#### 10.23.`weekday(date)`

```sql
--weekday(date);返回日期的星期几的索引
select weekday('2018-06-06');  --2（星期三）;0-星期一,1-星期二
```

#### 10.24.`dayofmonth(date)`

```sql
--dayofmonth(date);返回日期中的day,值在[1-31]
select dayofmonth('2018-06-06');  --6
```

#### 10.25.`dayofyear(date)`

```sql
--dayofyear(date);返回日期在一年中的天数,值在[1-366]
select dayofyear('2018-06-06');  --157
```

#### 10.26.`month(date)`

```sql
--month(date);返回date的月份,值在[1-12]
select month('2018-01-01');   --1
```

#### 10.27.`dayname(date)`

```sql
--dayname(date);返回日期的星期几的名称
select dayname('2018-06-06');  --Wednesday
```

#### 10.28.`monthname(date)`

```sql
--monthname(date);返回date月份的名称
select monthname('2018-06-06'); -- June
```

#### 10.29.`quarter(date)`

```sql
--quarter(date),返回date季度的索引,值在[1-4]
select quarter('2018-06-06');  --2
```

#### 10.30.`week(date,first)`

```sql
--week(date,first);返回date在当前年的第几个周;first=0,表示一周从星期天开始;first=1,表示一周从星期一开始
select week('2018-01-07');   --1
select week('2018-01-02',0); --0,2018-01-01是星期天
select week('2018-01-02',1); --1
```

#### 10.31.`year(date)`

```sql
--year(date);返回date的年份,值在[1000-9999]
select year('18-06-06');  --2018
```

#### 10.32.`hour(time)`

```sql
--hour(time);返回time的小时,值在[0-23]
select hour('20:43:23');  --20
```

#### 10.33.`mimute(time)`

```sql
--mimute(time);返回time的分钟,值在[0-23]
select mimute('20:43:23');  --43
```

#### 10.34.`second(time)`

```sql
--seconf(time);返回time的秒数,值在[0-59]
select second('20:43:23');  --23
```

#### 10.35.`date_add(date,interval exp type)`

```sql
--date_add(date,inerval exp type);日期的增加,可精确到秒
--date_sub(date,interval exp type);日期的减少,可精确到秒

select '2018-06-06 21:04:59' + interval 1 second;  --2018-06-06 21:05:00
SELECT INTERVAL 1 DAY + "2018-12-31";              --2019-01-01
SELECT DATE_ADD("2018-12-31 23:59:59", INTERVAL 1 SECOND);  --2019-01-01 00:00:00
SELECT DATE_ADD("2018-12-31 23:59:59", INTERVAL "1:1" MINUTE_SECOND); --2019-01-01 00:01:00

SELECT "2018-01-01" - INTERVAL 1 SECOND; --2017-12-31 23:59:59
SELECT DATE_SUB("2018-01-01 00:00:00", INTERVAL "1 1:1:1" DAY_SECOND); --2017-12-30 22:58:59
SELECT DATE_SUB("2018-01-02", INTERVAL 31 DAY); --2017-12-02s
```

#### 10.36.`case value when [condition] then result [when [condition] then result ...]`

```sql
case value
when [condition] then result
[when [condition] then result ...]
[else result] end

select case 1 when 1 then 'one' when 2 then 'two' else 'more' end;  --one
```

#### 10.37.`strcmp(str1,str2)`

```sql
--strcmp(str1,str2);若str1<str2返回-1;若str1=str2返回0;若str1>str2返回1
select strcmp('text','text2');  --{-1}
```

#### 10.38.`version()`

```sql
--version();查询数据库版本信息
select version();  --5.7.15-log
```

#### 10.39.`connection_id()`

```sql
--connection_id();返回到目前为止MySQL服务的连接次数
select connection_id(); --9
```

#### 10.40.`database()/schema()`

```sql
--database()/schema();返回数据库名
select database();
select schema();
```

#### 10.41.获取用户名

```sql
--获取用户名
--root@localhost
select user();
select system_user();
select session_user();
select current_user();
```

#### 10.42.获取字符串的字符集和排序方式 

```sql
--charset(str);获取字符串的字符集
select charset('str');   --utf8mb4
--collation(str);获取字符串的排序方式
select collation('str'); --utf8mb4_general_ci
```

#### 10.43.`last_insert_id()`

```sql
--last_insert_id();获取最后生成的auto_increment值
select last_insert_id();
```

#### 10.44.`password(str)`

```sql
--password(str);对字符串str加密,一般用于对用户密码加密
select password('root');  --*81F5E21E35407D884A6CD4A731AEBFB6AF209E1B
```

#### 10.45.`md5(str)`

```sql
--md5(str);对str加密,一般对普通数据加密
select md5('str');  --341be97d9aff90c9978347f66f945b77
```

#### 10.46.`encode(str,pwdstr)`

```sql
--encode(str,pwdstr);使用字符串pwdstr加密字符串str;加密结构是一个二进制数,必须使用blob类型的字段保存
select encode('str','pwd');
```

#### 10.47.`decode(cryptstr,pwdstr)`

```sql
--decode(crytstr,pwdstr);使用pwdtsr解密字符串crytstr;crytstr是使用encode()加密的结果
select decode(encode('str','pwd'),'pwd');  --str
```

#### 10.48.`format(x,n)`

```sql
--format(x,n);对数字x进行格式化,保留x后n位小数;这个过程要进行四舍五入
select format(3.2637,3);   --3.264
select format(3.2634,3);   --3.263
```

#### 10.49.不同进制数字进行转换

```sql
--bin(x);10转2
select bin(14);  --1110
--hex(x);10转16
select hex(14);  --E
--oct(x);10z转8
select oct(14);  --16

--conv(x,f1,f2);将x从f1进制转为f2进制
select conv(1110,2,8);  --16
```

#### 10.50.`ip`与数字的转化

```sql
--inet_aton(ip);ip地址转数字
select inet_aton('192.168.3.225');  --3232236513
select inet_aton('255.255.255.255');  --4294967295
--inet_ntoa(n);数字转ip地址;不能转化大于4294967295
select inet_ntoa(3232236513);       --192.168.3.225
```

#### 10.51.`get_lock(name,time)`

```sql
--get_lock(name,time);定义一个名为name、持续时间为time秒的锁;
--锁定成功【1】;
--尝试超时【2】;
--遇到错误【null】
select get_lock('lock',3);
```

#### 10.52.`release_lock(name)`

```sql
--release_lock(name);解除名为name的锁
--解锁成功【1】;
--尝试超时【2】;
--遇到错误【null】
select release_lock('lock');
```

#### 10.53.`is_free_lock(name)`

```sql
--is_free_lock(name);判断是否使用了名为name的锁
--使用【0】
--未使用【1】
select is_free_lock('lock');
```

#### 10.54.`benchmark(count,expr)`

```sql
--benchmark(count,expr);将表达式expr重复执行count,返回执行时间;用来判断mysql处理表达式的速度
select benchmark(10,'select * from pre_attr');
```

#### 10.55.`convert(str using charset)`

```sql
--convert(str using charset);将字符串的字符集变为charset
select charset('abc'),charset(convert('abc' using gbk));  -- utf8mb4	  gbk
```

#### 10.56.`cast(x as type)/convert(x,type)`

```sql
--convert(x,type);将x的转化为type类型
select create_time,convert(create_time,date) from bbs_post;  --2015-01-21 10:02:39  2015-01-21
--cast(x as type);将x的转化为type类型
select create_time,cast(create_time as date) from bbs_post;  --2015-01-21 10:02:39  2015-01-21
--这两个函数只对BINARY、CHAR、DATE、DATETIME、TIME、SIGNED INTEGER、UNSIGNED INTEGER
--只是改变了输出值的数据类型，并没有改变表中字段的类型
```

#### 10.57.`find_in_set(str,strList)`

```mysql
-- like是广泛的模糊匹配，字符串中没有分隔符
-- find_in_set是精确匹配，字段值以英文”,”分隔

-- 若str不在strList或strList为'',返回0
select find_in_set('a','');        -- 0
select find_in_set('2','a,b,c,d'); -- 0

-- 若str和strList中任意一个为null，则返回null
select find_in_set(null,'a,b,c,d');  -- null
select find_in_set('2',null);        -- null   

-- 若查找到了就返回索引位置
select find_in_set('c','a,b,c,d');  -- 3

-- 精确查询
select * from tamenu where find_in_set(2,pmenuid);
select * from tamenu where pmenuid = '2';
```

#### 10.58.`found_rows()`

```mysql
-- 获取上一个select语句查询到的行数；
select found_rows();
```

#### 10.59.`row_count()`

```sql
-- 获取上一条update， insert ，delete 影响的行数
select row_count();
```

注意： ``found_rows()`和`row_count()`要在同一事务中才有效。

#### 10.60.`substring_index(str, delim, count)`

```mysql
-- substring_index(str, delim, count)：字符串截取
-- 【str】：要处理的字符串
-- 【delim】：分隔符
-- 【count】：返回第几个分隔符之前的字符串，负数就反向取

select substring_index('aa.bb.cc.dd', '.', 3);  -- aa.bb.cc
select substring_index('aa.bb.cc.dd', '.', -3);  -- bb.cc.dd
```



### 十一、临时表

临时表的只在当前会话有效，当会话结束后就自动删除表和释放相应的空间。因此可以在不同的会话中创建同名的临时表。

#### 11.1.临时表的限制条件

* 临时表只在`memory、myisam、merge和innodb`上使用，不支持`mysql cluster`簇
* `show tables`不会列出临时表，且`information_schema`中也不存在临时表；

```mysql
-- 查看临时表
show create table tmp_menu;
```

* 临时表不能使用`rename`来重命名，可使用`alter table rename`重命名

```mysql
alter table tmp_menu rename tempMenu;
```

* 可以复制临时表得到一张新的临时表

```mysql
create temporary table if not exists tempMenu
as 
select * from tmp_menu;
```

* 查询语句中同一临时表不能出现多次

```mysql
-- 使用临时表多次出现的异常
select * from tmp_menu a
inner join tmp_menu b on a.menuid = b.menuid
```

```mysql
-- 异常信息
> 1137 - Can't reopen table: 'b'
```

* 同一临时表在储存函数和过程中也不能出现多次
* 不同的临时表可以出现在同一个语句中

#### 11.2.创建临时表

```mysql
create temporary table if not exists chat_system.tmp_menu(
	id int primary key auto_increment,
	menuid decimal(10,0),
	menulevel decimal(10,0)
);
```

#### 11.3.删除临时表

```mysql
drop temporary table if exists tmp_menu;
```

#### 11.4.临时表的作用

* 临时表可对大数据量的表做一个子集，提高查询的速度











### 十二、表

#### 12.1.重命名

```mysql
rename table tamenu to menu;
alter table menu rename tamenu;
```

#### 12.2.内存表

内存表的的表结构创建在磁盘上，数据存放在内存中；所以`MySQL`重启后，表结构存在，但数据丢失。

内存表的结构存放在磁盘上，扩展名为`.frm `

##### 12.2.1.内存表的创建

```mysql

```



#### 12.3.内存表与临时表的区别

|          | 内存表                 | 临时表                     |
| -------- | ---------------------- | -------------------------- |
| `engine` | `memory`               | `mysql`的默认`engine`      |
| 作用     | 存放一些频繁使用的数据 | 存放一些中间大结果集的子集 |
|          |                        |                            |

