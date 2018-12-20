### 一、查看Oracle数据库数据量的大小

1、 查看所有表空间及其大小

```sql
select tablespace_name,sum(bytes)/1024/1024/1024 as GB from dba_data_files group by tablespace_name;
```

2、查看所有表空间对应的数据文件

```sql
select tablespace_name,file_name from dba_data_files;
```

3、修改数据文件的大小

```sql
alter database datafile 'D:\datafile.dbf' resize 1024M;
```

### 二、查看用户的状态（是否被锁）

1、查看用户状态

```sql
select * from dba_users where username = 'JYRC'
```

2、解锁

```sql
alter user jyrc account unlock;
```

3、 修改用户密码

```sql
alter user jyrc identified by newpassword;
```

4、查看`FAILED_LOGIN_ATTEMPTS(数据库默认是10次尝试失败后锁住用户)`的值

```sql
select * from dba_profiles where resource_name = 'FAILED_LOGIN_ATTEMPTS';
```

5、修改`FAILED_LOGIN_ATTEMPTS`的值

```sql
alter profile default limit FAILED_LOGIN_ATTEMPTS 30;  --修改数据库30次尝试失败后锁住用户
alter profile deafult limit FAILED_LOGIN_ATTEMPTS unlimited; --修改为无限次（为安全起见，不建议使用）
```

6、查找用户锁住的原因

```sql
--1.查看用户被锁的时间(LOCK_DATE)
select * from dba_users where username = 'JYRC';
--2.查看那个IP导致用户被锁，查看$ORACLE_HOME$/network/admin/log/listener.log日志
--3.查看哪些用户连接了该用户
--osuser连接(OS)用户信息，username数据库的用户，machine电脑名称
SELECT osuser, a.username,cpu_time/executions/1000000||'s', sql_fulltext,machine
from v$session a, v$sqlarea b
where a.sql_address =b.address order by cpu_time/executions desc; 
```

7、查看密码的有效期限

```sql
 SELECT * FROM dba_profiles WHERE profile='DEFAULT' AND resource_name='PASSWORD_LIFE_TIME';
 --密码将要过期或已经过期，可修改密码
 alter user jyrc identified by newpassword;
```

8、查看当前连接的用户

```sql

alter system kill session'532,4562' 
```



### 三、`Oracle`被锁对象

1、查看`Oracle`被锁的对象

```sql
SELECT 
  a_s.owner,
  a_s.object_name,      --对象名称
  a_s.object_type,      --对象类型
  VN.SID,        
  VN.SERIAL#,
  VS.SPID "OS_PID",
  VN.PROCESS "CLIENT_PID",
  VN.USERNAME,           --用户名
  VN.OSUSER,             --OS的用户名称
  VN.MACHINE "HOSTNAME" ,--电脑名称 
  VN.TERMINAL,
  VN.PROGRAM,
  TO_CHAR(VN.LOGON_TIME,'YYYY-MM-DD HH24:MI:SS')"LOGIN_TIME",     --登录名称
  'alter system kill session '''||vn.sid||','||vn.serial#||''';' "ORACKE_KILL",
  'kill -9 '|| VS.SPID "OS_KILL"   --解锁语句
FROM ALL_OBJECTS A_S,
  V$LOCKED_OBJECT V_T,
  V$SESSION VN,
  V$PROCESS VS
WHERE A_S.OBJECT_ID=V_T.OBJECT_ID
AND V_T.SESSION_ID =VN.SID
AND VS.ADDR=VN.PADDR
AND VN.USERNAME NOT IN('SYSMAN','SYS');  --用户名
```

2、解锁对象

```sql
alter jyrc kill session 'sid,serial';  --不能kill自身
```

3、查看`sid`

```sql
select * from v$sql vl,v$session vn 
where vl.address = decode(vn.sql_address,null,prev_sql_addr,vn.sql_address)
and vn.sid = '';
```

### 四、定时器JOB

1、定义job 

2、删除job



3、查看定义的job

```sql
select * from dba_jobs;
select * from all_jobs;
select * from user_jobs;
```

4、查看正在运行的job

```sql
select * from dba_jobs_running;
```

5、执行job

```sql
exec dbms_job.run(job => &JOBID) ;

begin  
dbms_job.run(job的id);  
end;  
/  
```

6、job_queue_processes工作队列进程

```sql
show parameter job_queue_processes ;  
--修改用户system中job_queue_processes的值
alter system set job_queue_processes=100;  
```



### 五、执行SQL脚本

1、cmd窗口

```sql
@F:ae90.sql
@F:temp\ae90.sql
--建立批处理文件batch.bat
sqlplus scott/scott@orcl @f:ae90.sql 
```



### 六、异常

1、错误：PL/SQL: ORA-00942: 表或视图不存在

* 表或视图确实不存在或名称写错

* 权限问题导致的

  ```sql
  --授权所用的权限
  grant all on ab01 to jyrc;
  ```

2、远程连接 ORA-12541:TNS: 无监听程序

* 重新配置一下监听程序
* ​

### 七、动态执行SQL

1、`EXECUTE IMMEDIATE`



### 八、数据字典

* `v$xxx`:,动态数据字典 , 以`v$xxx`开始的数据字典，在数据库中约有150个左右，这些数据字典反映数据库动态运行状况，在不同时间查询会得到不同的结果 
* `dba_*`:存储数据库结构，查询DBA数据字典可以反映数据库结构设置，管理磁盘空间和表空间、事务与回退段、用户与表空间等信息 ;整个数据库中的对象信息

* `all_*`:用户可以访问的对象信息，即用户自己创建的对象的信息加上其他用户创建的对象但该用户有权访问的信息 
* `user_*`:用户所拥有的对象信息，即用户自己创建的对象信息;反应用户所创建的实体信息

1、查看所有的表

```sql
--查询当前数据库中所有的表（包含系统表）
select * from dba_tables;
--查看所有用户的表
select * from all_tables;
--查看当前用户下的所有表
select * from user_tables;
```

2、查询数据库中的用户

```sql
--查询当前用户
select * from user_users;
--查询对当前用户可见的所有用户
select * from all_users;
--查询数据库中的所有用户
select * from dba_users;
```

3、查询数据库中的表空间

```sql
--查询数据中的所有表空间
select * from dba_tablespaces;
```

4、查询数据库中表空间数据文件信息

```sql
--查询表空间数据文件信息
select * from dba_data_files;
```

4、查询索引信息

```sql
--查询当前用户的所有索引
select * from user_indexes;
--查询对当前用户可见的所有索引
select * from all_indexes;
--查询当前数据库中的所有索引
select * from dba_indexes;
```

5、查看约束信息

```sql
-- 查看哪张表存在约束
select * from user_constraints;
-- 查看那个列上存在约束
select * fron user_cons_columns;
--constraint_约束类型，其简写：primary key(P)、foreign key(F)、check(C)、not null(C)、unique(U)
```

6、查询注释信息

```sql
--查询当前用户所有表的注释信息
select * from user_tab_comments;
--查询对当前用户可见的所有表的注释信息
select * from all_tab_comments;
--查询当前数据中所有表的注释信息
select * from dba_tab_comments;

--查询字段的注释信息
select * from user_col_comments;
--查询对当前用户可见的所有字段的注释信息
select * from all_tab_comments;
--查询当前数据中所有字段的注释信息
select * from dba_tab_comments;
```

7、查询数据库表的字段信息

```sql
--all_tab_cols / all_tab_columns 查看对当前用户可见的表及视图结构
select * from all_tab_cols;
select * from all_tab_columns;
--user_tab_cols / user_tab_columns 查看当前用户下的表及视图结构
select * from user_tab_cols;
select * from user_tab_columns;
--dba_tab_cols / dba_tab_columns 查询当前数据中所有的表及视图结构
select * from dba_tab_cols;
select * from dba_tab_columns;
```

8、数据库的权限

```sql
--数据库的对象权限的授权情况
select * from dba_tab_privs;
--数据库的角色的授权情况
select * from dba_role_privs;
--数据库的系统权限授权情况
select * from dba_sys_privs;

--授予当前用户的对象权限
select * from user_tab_privs;
--授予当前用户的角色
select * from user_role_privs;
--授予当前用户的系统权限
select * from user_sys_privs;

--授予角色的对象权限
select * from role_tab_privs;
--授予角色的角色
select * from role_role_privs;
--授予角色的系统权限
select * from role_sys_privs;

--当前用户拥有的全部权限
select * from session_privs;
--当前用户被激活的角色,其中包括嵌套授权的角色
select * from session_roles;
```

9、数据库的角色

```sql
--数据库的所有角色
select * from dba_roles;
--查看角色的权限
select * from dba_role_privs where grantee = upper('dba');
select * from role_role_privs where role = upper('dba');
```



### 九、表空间

1、创建表空间

```sql
--创建临时表空间
create temporary tablespace sean_temp   --表空间名称
logging
tempfile 'C:\APP\ADMINISTRATOR\ORADATA\ORCL\SEAN_TEMP01.DBF'  --表空间存储文件的位置
size 10m                   --表空间大小
autoextend on next 10m     --达到容器大小后，每次自动增长10M
maxsize 100m               --表空间的最大容量
extent management local    --默认的管理方式

--创建数据表空间
create tablespace sean_data  --表空间名称
logging 
datafile 'C:\APP\ADMINISTRATOR\ORADATA\ORCL\SEAN_DATA01.DBF' --表空间存储文件的位置
size 10m 
autoextend on next 10m   --达到容器大小后，每次自动增长10M
maxsize 100m             --表空间的最大容量
extent management local  --默认的管理方式
```

2、删除表空间

```sql
--删除空的表空间，但不删除物理文件
drop tablespace tablespace_name;
--删除非空的表空间，但不删除物理文件
drop tablespace tablespace_name including contents;
--删除空的表空间，且删除物理文件
drop tablespace tablespace_name including datafiles;
--删除非空的表空间，且删除物理文件
drop tablespace tablespace_name including contents and datafiles;
--其他表空间的表有外键等约束关联了本表空间的表字段，级联删除
drop tablespace tablespace_name  --表空间名称
including contents               --contents删除非空的表空间 
and       datafile               --删除物理文件
cascade constraints              --级联删除
```

3、查看表空间信息

```sql
--查询数据中的所有表空间
select * from dba_tablespaces;
--查询表空间数据文件信息
select * from dba_data_files;
```

4、修改某一数据表的表空间

```sql
--修改某一数据表的表空间
--将表tb_user移到表空间tablespace_name下
alter table tb_user move tablespace tablespace_name;  
--修改tb_user索引的表空间
alter index tb_user_id rebuild tablespace tablespace_name;
```

5、扩展表空间

```sql
--扩充表空间的物理文件
alter tablespace tablespace_name
add 
datafile 'D:/TEMP02.DBF'     --新增表空间文件
size 100M                    --大小
AUTOEXTEND ON NEXT 100M      --自增
MAXSIZE 10000M;              --最大容量

--使表空间脱机
alter tablespace tablespace_name offline;
--使表空间联机
alter tablespace tablespace_name online;
--使数据文件脱机
alter datafile datafile_name offline;
--使数据文件联机
alter datafile datadile_name online;
--设置表空间只读
alter tablespace tablespace_name read only;
--设置表空间可读写
alter tablespace tablespace_name read write;
```

6、修改表空间的大小为不限制

```sql
 ALTER USER dealer QUOTA UNLIMITED ON USERS;
```



### 十、复制表

1、复制表结构

```sql
--复制表结构及其表数据，但不会创建索引
create table uh10_tmp 
as
select * from uh10 where yuh040 = '2';
--只复制表结构
create table uh10_tmp
as
select * from uh10 where 1<>1;
--只复制指定表的指定字段，且不包含数据
create table uh10_tmp
as 
select yuh040,yuh050,yuh051 from uh10 where 1<>1;
--两个表的结构一样，复制表的数据
insert into uh10_tmp
select * from uh10;
--两个表的结构不一样，复制部分列
insert into uh10_tmp(yuh040,yuh050,yuh051,yuh052,yuh053 )
select yuh040,yuh050,yuh051,yuh052,yuh053 from uh10;

```

### 十一、触发器

触发器就是某个条件成立的时候，触发器里面所定义的语句就会被自动的执行。

故触发器不需要人为的调用，也不能认为的调用。

触发器可以分为语句级触发器和行级触发器 （`for each now`）

#### 11.1.触发器的使用

```plsql
create [or replace] tigger 触发器名 触发时间 触发事件
on 表名
[for each now]
declare
	定义变量
begin
	pl/sql语句
end
```

* 【触发器名 】：由于触发器有数据库自动执行，故改名称没有实际用途  
* 【触发时间】：指明触发器何时执行 
  * `before`：在数据库动作之前执行触发器
  * `after`：在数据库动作之后执行触发器
* 【触发事件】：指明哪些数据库动作会触发此触发器
  * `insert`：数据库的插入会触发此触发器
  * `update`：数据库的修改会触发此触发器
  * `delete`：数据库的删除会触发此触发器
* `for each now`：对数据库表的每一行触发器执行一次，否则整个表只执行一次

1. 设置周末不能修改数据库表

```plsql
create or replace trigger auth_secure before insert or update or DELETE
on tb_emp
begin
  IF(to_char(sysdate,'DY')='星期日') THEN
    RAISE_APPLICATION_ERROR(-20600,'不能在周末修改表tb_emp');
  END IF;
END;
/
```

2. 触发器实现序号自增

```sql
create sequence my_seq increment 
by 1 
start with 1 
nomaxvalue 
nocycle cache 20;
```

```plsql
create or replace trigger auto_increment_tigger before insert 
on tb_tmp
for each now
declare
	next_id number;
begin 
	select my_seq.nextval into next_id from daul;
	--【：new】表示最新的一条记录
	--【：old】表示旧值
	:new.id := next_id;
end;
	
```

3. 当用户执行`DML`语句时，记录相关的日志

```sql
--准备测试的表
--创建测试表
create table test(
  t_id  number(4),
  t_name varchar2(20),
  t_age number(2),
  t_sex char
);
--创建记录测试表
create table test_log(
  l_user  varchar2(15),
  l_type  varchar2(15),
  l_date  varchar2(30)
);
```

```plsql
create or replace trigger dml_log_tigger after insert or update or delete
on test
declare
   --操作类型
   v_type test_log.l_type%type;
begin 
  --insert
  if inserting then
    v_type := 'insert';
  --update
  elsif updating then
    v_type := 'update';
  --delete  
  elsif deleting then
    v_type := 'delete';
  end if;
  --写入日志信息,user为当前的用户名
  insert into test_log
  values(user,v_type,to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'));
end;  
```

#### 11.2.触发器的限制

* 触发器中不能使用事务控制语句：`commit\rollback\savepoint`
* 由触发器调用的过程和函数与不能包含事务控制语句
* 触发器中不能使用`LONG、LONG RAW`
*   触发器内可以参照`LOB `类型列的列值，但不能通过 `:NEW `修改`LOB`列中的数据； 

#### 11.3.触发器前后列的值

* ` :OLD` 修饰符访问操作完成前列的值 
* `:NEW `修饰符访问操作完成后列的值 

|  特性  | `insert` | `update` | `delete` |
| :----: | :------: | :------: | :------: |
| `:OLD` |  `null`  |  实际值  |  实际值  |
| `:NOW` |  实际值  |  实际值  |  `null`  |

#### 11.4.触发器的数据字典

```sql
select * from user_triggers;
```

#### 11.5.触发器的常用操作

```sql
--设置触发器生效disable
alter trigger trigger_name disbale;
--设置触发器生效enable
alter trigger trigger_name enable;
--设置某个表上的触发器批量失效
alter table table_name disable all trigger;
alter table table_name enable all trigger;
--重编译触发器
alter trigger trigger_name compile;
--删除触发器
drop trigger trigger_name;
```

#### 11.6.触发器的谓词

* `inserting`：当触发事件是`insert`时，取值`true`，否则`false`
* `updating`：当触发事件是`udpate`时，取值`true`，否则`false`
* `deleting`：当触发事件是`delete`时，取值`true`，否则`false`



### 十二、调度作业

`dbms_scheduler`包的功能比`dbms_job`包强大很多 ,主要围绕着三个基本概念`schedule，program和job `

* `schedule`：表示调度计划表 ,调度从什么时间开始被调度，什么时候结束，以什么频度调度 
* `program`:表示调度应该做什么事情，是对程序的抽象 
* `job`:表示按照指定的`schedule`，执行指定`program`，完成用户指定的工作。

#### 12.1.`shcedule`

```sql
begin
    DBMS_SCHEDULER.CREATE_SCHEDULE (
      --名称
      schedule_name     => 'daily_schedule',
      --开始执行时间  
      start_date        => SYSDATE,
      --下次执行时间
      repeat_interval   => 'FREQ=DAILY ; INTERVAL=1',
      --注释
      comments          => 'every one day');
end;
/
```

#### 12.2.`program`

```sql
 begin
   dbms_scheduler.create_program(
      --名称
      program_name   => 'time_synchronizatio',
      --执行内容，若为过程，则为过程名 
      program_action => '/sbin/ntpdate 128.59.67.10',
      --程序类型 
      program_type   => 'EXECUTABLE',
      --若为true则创建后激活反之不激活 
      enabled        => true);
 end;
 /
```

`program_type `程序类型：

* `stored_procedure` :`oracle`定义好的储存过程
* `plsql_block`：一段标准的`pl/sql`代码 
* `executable` ：指定外部命令的命令行信息(含路径信息) 

> 注意：创建的程序需要输入参数，则必须定义完参数后在激活，即创建这个`program`时将`enable`设为`false `，否则会报错：

```sql
Ora-27456：程序“ ”的参数并未全部定义；然后再对该program定义参数即执行define_program_argument过程。
```



#### 12.3.`job`

```sql
begin
  sys.dbms_scheduler.create_job(
      --定时任务的名称
      job_name            => 'JYRCN.JOB_AEA7',  
      --定时任务的类型
      job_type            => 'STORED_PROCEDURE',
      --定时任务的动作，即执行过程的名称
      job_action          => 'pkg_jyrc_dpmzs.prc_insertaea7',
      --开始执行时间
      start_date          => to_date('22-02-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
      --下次执行时间
      repeat_interval     => 'Freq=Minutely;Interval=2',
      --结束时间
      end_date            => to_date(null),
      --
      job_class           => 'DEFAULT_JOB_CLASS',
      --若为true则创建后激活反之不激活
      enabled             => true,
      --job禁用后是否自动删除
      auto_drop           => false,
      --注释
      comments            => '');
end;
/
```

#### 12.4.删除`job`

```sql
exec dbms_scheduler.drop_job(job_name=>'SCOTT.SAM_JOB');
```

#### 12.5.`repeat_interval `参数

`repeat_interval `参数支持两种格式

* `interval `：
* `日历表达式`：即`linux`的`crontab`格式，日历表达式分为三部分
  * 频率：`FREQ `，必须指定
  * 时间间隔 ：`INTERVAL `，可选
  * 附加的参数 ：用于精确地指定日期和时间，可选

#### 12.6.数据字典

```sql
--查看定义的programs
SELECT * FROM user_scheduler_programs;
--

```







### 十三、命令窗口

1、连接数据库

```sql
--进入sqlplus界面
sqlplus nolog
--连接用户
sqlplus scott/scott
```

```sql
--使用dba用户连接
connect / as sysdba
connect sys/sys as sysdba
--启动数据库实例
startup
--关闭数据库实例
shutdown
```

2、查看数据库信息

```sql
--查看所有的数据库
select * from v$databse;
show parameter db_name;
--查看数据库的结构字段
desc v$database;

--查看所有数据库实例
select * from v$instance;
show parameter db_name;
--查看所有的数据库表
select * from all_tables;
--查看表结构
desc table_name;

--查看数据库参数
select * from v$parameter;
--查看数据库的域名
select * from v$parameter where name = 'db_domain';
show parameter domain;
--查看数据库的服务器名
select * from v$parameter where name = 'service_names';
show parameter service;
show parameter names;
--数据库如果有域，则数据库服务名就是全局数据库名；如果没有，则数据库服务名就是数据库名。

--oracle数据库版本的一些特殊信息
select * from v$version;
--查看控制文件信息
select * from v$controlfile;
--查看数据库的重做日志配置信息
select * from v$log;
--查看重做日志（成员）文件所存放的具体位置
select * from v$logfile;
```

3、格式化脚本

```sql
set term on;        //查询结果既显示于假脱机文件中，又在SQLPLUS中显示；
set term off;       //查询结果仅仅显示于假脱机文件中。
set colsep'|';　　　 //-域输出分隔符
set echo off;　　　 //显示start启动的脚本中的每个sql命令，缺省为on
set echo on             //设置运行命令是是否显示语句
set feedback on;       //设置显示“已选择XX行”
set feedback off;　    //回显本次sql命令处理的记录条数，缺省为on
set heading off;　　 //输出域标题，缺省为on
set pagesize 0;　　    //输出每页行数，缺省为24,为了避免分页，可设定为0。
set linesize 80;　　   //输出一行字符个数，缺省为80
set numwidth 12;　    //输出number类型域长度，缺省为10
set termout off;　　   //显示脚本中的命令的执行结果，缺省为on
set trimout on;　　    //去除标准输出每行的拖尾空格，缺省为off
set trimspool on;　   //去除重定向（spool）输出每行的拖尾空格，缺省为off
set serveroutput on; //设置允许显示输出类似dbms_output
set timing on;           //设置显示“已用时间：XXXX”
set autotrace on;      //设置允许对执行的sql进行分析
set verify off                       //可以关闭和打开提示确认信息old 1和new 1的显示.
```

```sql
--方式一
set colsep '|';  --设置|为列分隔符
set trimspool on;
set linesize 120;
set pagesize 2000;
set newpage 1;
set heading off;
set term off;
set num 18;
set feedback off;
spool 路径+文件名;         -- 将显示的内容输出到指定文件
select * from tablename;
spool off;
```

```sql
--方式二
set trimspool on
set linesize 120
set pagesize 2000
set newpage 1
set heading off
set term off
spool 路径+文件名
select col1||','||col2||','||col3||','||col4||'..' from tablename;
spool off
```

4、执行脚本文件

```sql
--指定全路径，否则从缺省路径(可用SQLPATH变量指定)下读取指定的文件
@ file_name.sql
strat file_name.sql

--执行的sql脚本文件与@@所在的文件在同一目录下,而不用指定要执行sql脚本文件的全路径，也不是从SQLPATH环境变量指定的路径中寻找sql脚本文件
@@ file_name.sql
```





### 十四、生成注释

##### 14.1.查询注释信息

```sql
--生成注释sql
select 'comment on column '|| lower(table_name)||'.'||lower(column_name)||' is  '''';'   
from user_col_comments where table_name = upper('pre_node');

select 'comment on column '|| lower(table_name)||'.'||lower(column_name)||' is  '''';' 
from all_tab_cols where  table_name = upper('pre_service_material') and owner = upper('jyrc_zwb') order by column_id; 

select '// '||lower(a.column_name)||' '||a.comments|| ' '||decode(b.nullable,'N','必填')
from user_col_comments a
left join all_tab_cols b on a.column_name = b.column_name
where a.table_name = upper('pre_apasinfo') 
  and b.table_name = upper('pre_apasinfo') 
  and b.owner = upper('jyrc');
```

##### 14.2`mysql`注释转`oracle`注释

```sql
select concat('comment on column ',lower(table_name),'.',lower(column_name),' is ''',column_comment,''';') from columns where table_name = 'pre_service_material' 
and table_schema = 'jyrc_zwb';
```

##### 14.3. `oracle`注释转`mysql`注释

```sql
--部分字段类型的转化存在异常
select 'alter table '||lower(a.table_name)||' modify column '||lower(a.column_name)||' '||decode(lower(a.data_type),'varchar2','varchar('||data_length||')',lower(a.data_type)||'('||data_length||')')||' comment '||''''||b.comments||''';'
from user_tab_cols a 
left join user_col_comments b on a.column_name = b.column_name
where a.table_name = upper('pre_service') and b.table_name = upper('pre_service'); 
```

##### 14.4 数据库之间的迁移或转化可使用`Navicat Premium`或`sqldeveloper`



### 十五、锁



### 十六、函数

#### 16.1.`sys_guid() `

```sql
--获取uuid
select sys_guid() from dual;
```

#### 16.2.`clob`的函数

```sql
--获取clob字段长度
dbms_lob.getlength(clob)

--截取clob字段的文本
dbms_lob.substr(clob,len,pos);
--从第一个字节取4000个字节
select dbms_lob.substr(clob,4000,1) from dual;
--截取clob字段中的全部数据
select dbms_lob.substr(clob) from dual;

--关闭文件
dbms_lob.fileclose(image_bfile);

--clob转字符串
utl_raw.cast_to_varchar2(dbms_lob.substr(clob,400,1))

```

```sql
--查询时显示clob字段的值
select dbms_lob.substr(clob) from dual; 
```

> ？ `clob`字段的增删改

#### 16.3.统计函数

```sql
--数据准备
create table earnings -- 打工赚钱表  
(  
  earnmonth varchar2(6), -- 打工月份  
  area varchar2(20), -- 打工地区  
  sno varchar2(10), -- 打工者编号  
  sname varchar2(20), -- 打工者姓名  
  times int, -- 本月打工次数  
  singleincome number(10,2), -- 每次赚多少钱  
  personincome number(10,2) -- 当月总收入  
);
insert into earnings values('200912','北平','511601','大魁',11,30,11*30);  
insert into earnings values('200912','北平','511602','大凯',8,25,8*25);  
insert into earnings values('200912','北平','511603','小东',30,6.25,30*6.25);  
insert into earnings values('200912','北平','511604','大亮',16,8.25,16*8.25);  
insert into earnings values('200912','北平','511605','贱敬',30,11,30*11);  
  
insert into earnings values('200912','金陵','511301','小玉',15,12.25,15*12.25);  
insert into earnings values('200912','金陵','511302','小凡',27,16.67,27*16.67);  
insert into earnings values('200912','金陵','511303','小妮',7,33.33,7*33.33);  
insert into earnings values('200912','金陵','511304','小俐',0,18,0);  
insert into earnings values('200912','金陵','511305','雪儿',11,9.88,11*9.88);  
  
insert into earnings values('201001','北平','511601','大魁',0,30,0);  
insert into earnings values('201001','北平','511602','大凯',14,25,14*25);  
insert into earnings values('201001','北平','511603','小东',19,6.25,19*6.25);  
insert into earnings values('201001','北平','511604','大亮',7,8.25,7*8.25);  
insert into earnings values('201001','北平','511605','贱敬',21,11,21*11);  
  
insert into earnings values('201001','金陵','511301','小玉',6,12.25,6*12.25);  
insert into earnings values('201001','金陵','511302','小凡',17,16.67,17*16.67);  
insert into earnings values('201001','金陵','511303','小妮',27,33.33,27*33.33);  
insert into earnings values('201001','金陵','511304','小俐',16,18,16*18);  
insert into earnings values('201001','金陵','511305','雪儿',11,9.88,11*9.88); 
```

##### 16.3.1.`rollup /cube`

```sql
--对earnmonth,area分组统计（按照earnmonth分组，在earnmonth内部再按area分组）
select earnmonth,area,sum(personincome)
from earnings
group by earnmonth,area;
```

![1528794906425](E:\typora\images\1528794906425.png)

```sql
--rollup是在group by分组的基础上，对earnmonth的再汇总统计
select earnmonth, area, sum(personincome)  
from earnings  
group by rollup(earnmonth,area);  
```

![1528794926909](E:\typora\images\1528794926909.png)

```sql
--cube对earnmonth的汇总统计基础上对area再统计
select earnmonth, area, sum(personincome)  
from earnings  
group by cube(earnmonth,area)  
order by earnmonth,area nulls last;  
```

![1528794940154](E:\typora\images\1528794940154.png)

##### 16.3.2.`rollup`和`cube`区别

如果是`ROLLUP(A, B, C)`的话，`GROUP BY`顺序

```sql
 (A、B、C)
 (A、B) 
 (A) 
最后对全表进行GROUP BY操作
```

 如果是`GROUP BY CUBE(A, B, C)，GROUP BY`顺序

```sql
(A、B、C)
(A、B) 
(A、C) 
(A)，
(B、C)
(B) 
(C)，
最后对全表进行GROUP BY操作
```

##### 16.3.3.`grouping `

`rollup`和`cube`函数都会对结果集产生`null`，这时候可用`grouping`函数来确认 该记录是由哪个字段得出来的 

`grouping(字段名)`，若当前行是由`rollup`或者`cube`汇总得来的，结果就返回1，反之返回0 

```sql
select 
	decode(grouping(earnmonth),1,'所有月份',earnmonth) 月份,  
	decode(grouping(area),1,'全部地区',area) 地区, 
	sum(personincome) 总金额  
from earnings  
group by cube(earnmonth,area)  
order by earnmonth,area nulls last;  
```

##### 16.3.4.分组排序函数

```sql
-- rank() over()每个分组内单独排序，数据值相同为同一序号，下一个值跳跃式排序
select earnmonth 月份,
       area 地区,
       sname 打工者,
       personincome 收入,
       rank() over(partition by earnmonth, area order by personincome desc) 排名
  from earnings;
```

![1528808704801](E:\typora\images\1528808704801.png)

```sql
--dense_rank() over()每个分组内单独排序，据值相同为统一序号，下一个值递增一个
select earnmonth 月份,
       area 地区,
       sname 打工者,
       personincome 收入,
       dense_rank() over(partition by earnmonth, area order by personincome desc) 排名
  from earnings;
```

![1528808733006](E:\typora\images\1528808733006.png)

```sql
--row_number() over()每个分组单独排序，分组内序列值始终递增，即使值相同
select earnmonth 月份,
       area 地区,
       sname 打工者,
       personincome 收入,
       row_number() over(partition by earnmonth, area order by personincome desc) 排名
  from earnings;
```

![1529544392907](E:\typora\images\1529544392907.png)

##### 16.3.5.`sum() over()`累计求和

```sql
--统计每个月每个区域的收入总和
select earnmonth 月份,
       area 地区,
       sname 打工者,
       personincome 收入,
       sum(personincome) over(partition by earnmonth, area order by personincome) 总收入
  from earnings;
```

![1528809009783](E:\typora\images\1528809009783.png)



##### 16.3.6.统计最大最小值平均值

```sql
--统计每个月每个区域收入的最大值、最小值、平均值和总和
select earnmonth 月份,
       area 地区,
       sname 打工者,
       personincome 收入,
       max(personincome) over(partition by earnmonth, area) 最高值,
       min(personincome) over(partition by earnmonth, area) 最低值,
       avg(personincome) over(partition by earnmonth, area) 平均值,
       sum(personincome) over(partition by earnmonth, area) 总额
  from earnings;
```

![1528888754300](E:\typora\images\1528888754300.png)

```sql
select distinct earnmonth 月份,
                area 地区,
                max(personincome) over(partition by earnmonth, area) 最高值,
                min(personincome) over(partition by earnmonth, area) 最低值,
                avg(personincome) over(partition by earnmonth, area) 平均值,
                sum(personincome) over(partition by earnmonth, area) 总额
  from earnings;
```

![1528888784016](E:\typora\images\1528888784016.png)

##### 16.3.7.`lead`和`lag`

```sql
--可以访问组内当前行之前的行;
--【offset】默认为1,原因：组内第一个条记录没有之前的行，最后一行没有之后的行
--【default】处理指定的行不存在是显示的值,默认为空
lag(value_expression [,offset] [,default]) over ([query_partition_clase] order_by_clause);
--可以访问组内当前行置后的行
lead(value_expression [,offset] [,default]) over ([query_partition_clase] order_by_clause);
```

```sql
select earnmonth 本月,
       sname 打工者,
       lag(decode(nvl(personincome, 0), 0, '没赚', '赚了'), 1, 0) over(partition by sname order by earnmonth) 上月,
       lead(decode(nvl(personincome, 0), 0, '没赚', '赚了'), 1, 0) over(partition by sname order by earnmonth) 下月
  from earnings;
```

![1529544438236](E:\typora\images\1529544438236.png)

##### 16.3.8.开窗函数

```sql
--开窗函数over(),指定了分析函数工作的数据窗口大小
--包含三个分子子句：分组（partition by）,排序（order by）,窗口（rows）
over(partition by xxx order by xxx rows between xxx)
```

##### 16.3.9.**求最值对应的其他属性** 

```sql
--first_value()/last_value(),求最值对应的其他属性
--取出每月通话费最高和最低的两个地区
select bill_month,
       area_code,
       sum(local_fare) local_fare,
       first_value(area_code) over(partition by bill_month order by sum(local_fare) desc rows between unbounded preceding and unbounded following) firstval,
       last_value(area_code) over(partition by bill_month order by sum(local_fare) desc rows between unbounded preceding and unbounded following) lastval
  from t
 group by bill_month, area_code
 order by bill_month;
```

![1529544472880](E:\typora\images\1529544472880.png)

```sql
--准备的数据
create table t( 
   bill_month varchar2(12) , 
   area_code number, 
   net_type varchar(2), 
   local_fare number 
);
insert into t values('200405',5761,'G', 7393344.04); 
insert into t values('200405',5761,'J', 5667089.85); 
insert into t values('200405',5762,'G', 6315075.96); 
insert into t values('200405',5762,'J', 6328716.15); 
insert into t values('200405',5763,'G', 8861742.59); 
insert into t values('200405',5763,'J', 7788036.32); 
insert into t values('200405',5764,'G', 6028670.45); 
insert into t values('200405',5764,'J', 6459121.49); 
insert into t values('200405',5765,'G', 13156065.77); 
insert into t values('200405',5765,'J', 11901671.70); 
insert into t values('200406',5761,'G', 7614587.96); 
insert into t values('200406',5761,'J', 5704343.05); 
insert into t values('200406',5762,'G', 6556992.60); 
insert into t values('200406',5762,'J', 6238068.05); 
insert into t values('200406',5763,'G', 9130055.46); 
insert into t values('200406',5763,'J', 7990460.25); 
insert into t values('200406',5764,'G', 6387706.01); 
insert into t values('200406',5764,'J', 6907481.66); 
insert into t values('200406',5765,'G', 13562968.81); 
insert into t values('200406',5765,'J', 12495492.50); 
insert into t values('200407',5761,'G', 7987050.65); 
insert into t values('200407',5761,'J', 5723215.28); 
insert into t values('200407',5762,'G', 6833096.68); 
insert into t values('200407',5762,'J', 6391201.44); 
insert into t values('200407',5763,'G', 9410815.91); 
insert into t values('200407',5763,'J', 8076677.41); 
insert into t values('200407',5764,'G', 6456433.23); 
insert into t values('200407',5764,'J', 6987660.53); 
insert into t values('200407',5765,'G', 14000101.20); 
insert into t values('200407',5765,'J', 12301780.20); 
insert into t values('200408',5761,'G', 8085170.84); 
insert into t values('200408',5761,'J', 6050611.37); 
insert into t values('200408',5762,'G', 6854584.22); 
insert into t values('200408',5762,'J', 6521884.50); 
insert into t values('200408',5763,'G', 9468707.65); 
insert into t values('200408',5763,'J', 8460049.43); 
insert into t values('200408',5764,'G', 6587559.23); 
insert into t values('200408',5764,'J', 7342135.86); 
insert into t values('200408',5765,'G', 14450586.63); 
insert into t values('200408',5765,'J', 12680052.38); 
commit;
```

#### 1.5.格式化

##### 1.5.1.`to_char()`

* 格式化数值

  * 【`0`】：表示一个数值位，当原数值没有数字位与之匹配时，强制补`0`

  ```sql
  select to_char(45.2,'00000.000') from dual;  -- 00045.200
  --没有足够的整数位时，输出####
  select to_char(45.2,'0.0') from dual;        -- ####
  ```

  * 【`9`】：表示一个数值位，当原数值的整数部分没有数字为匹配时，不填充任何字符；但小数部分还是会强制补`0`

  ```sql
  select to_char(45.2,'99999.999') from dual;  --    45.200
  
  --个位一般用`0`，原因：数值小于1时，强制补上小数点前的0
  select to_char(0.452,'999.99999') from dual; --    .45200
  select to_char(0.452,'990.99999') from dual; --   0.45200
  
  --没有足够的整数位时，输出####
  select to_char('634.43','9.9') from dual;  --####
  select to_char('634.43','9999.9') from dual;  --  634.4
  ```

  * 【`,`】：分组符号，常用用于千位分隔符，只能用于整数部分的分隔

  ```sql
  select to_char('73838237.8863','999,999,999,999.999999') from dual;  
  --73,838,237.886300
  ```

  * 【`FM`】：屏蔽所有不必要的`0`

  ```sql
  select to_char('000726.308000','FM9999990.9999999') from dual;  --726.308
  ```

  * 【`L`】：中国货币`￥`，在数值前添加`￥`

  ```sql
  select to_char(45.2,'L990.99') from dual;   --￥45.20
  select to_char(45.2,'FML990.99') from dual; --￥45.2
  ```

  * 【`C`】：在数值后面添加`CNY`

  ```sql
  select to_char(45.2,'990.99C') from dual;     --45.20CNY
  ```

  * 数值格式化为百分数

  ```sql
  select to_char(4/234*100,'9990.99999')||'%' from dual;
  select concat(to_char(4/234*100,'9990.99999'),'%') from dual;
  ```


##### 1.5.2.字符串格式化

* `ltrim()`：去除字符串前面所有空格
* `rtrim()`：去除字符串后面所有空格
* `trim()`：去除字符串前后所有空格
* `lower()`：将字符串全部转小写
* `upper()`：将字符串全部转大写
* `initcap()`:将字符串首字母转大写

#### 1.6.判空或判断函数

##### 1.6.1.`trunc`
`trunc()`截取时，不进行四舍五入。
```
select trunc(sysdate,'YYYY') from dual;--2017/1/1
select trunc(sysdate,'MM') from dual;  --2017/12/1
--trunc截取数字，设置保留的小树数位数
select trunc(123,-1) from dual;        --120
select trunc(100.1234,2) from dual;    --100.12
```
##### 1.6.2`nvl` 替换`null`值
`nvl(exp1,exp2)`如果`exp1`为空，则取`exp2`；否则取`exp1`
```sql
select nvl(comm,0) from emp;
```
##### 1.6.3.`nvl2` 
`nvl2(exp1,exp2,exp3)`
* 若`exp1 != null`,返回`exp2`
* 若`exp1 = null`,返回`exp3`
```sql
select nvl2(comm,'有奖励金','无奖励金') from emp;
```
##### 1.6.4.`nullif`
`nullif(exp1,exp2)`
* 若`exp1 = exp2`, 返回`null`
* 若`exp1 != exp2`, 返回 `exp1`
```sql
select nullif(ename,'SMITH') from emp;
```
##### 1.6.5`coalesce` 优先返回不为`null`的值
`coalesce(expr1，expr2，expr3)`
* 全为`null`,返回`null`
* 返回第一个不为`null`的值
* 要保证数据类型一致
```sql
select coalesce(comm,sal,empno) from emp;
```

##### 1.6.6.`deceode` 
```sql
select 
--如果condition的值等于search1返回result1,
decode(condition,search1,result1,  --if(condition == search1){}
		         search2,result2,  --else if(condition == search1){}
		         search3,result3,  --else if(condition == search1){}
		         ...               -- ...
		         default)          --else{}
from dual;
```

```sql
select 
       empno,
       ename,
       decode(job,'CLERK','办事员',
                  'SALESMAN','办事员',
                  'SALESMAN','销售员',
                  'MANAGER','经理',
                  'ANALYST','分析员',
                  'PRESIDENT','老总',
                  '未知'),
        mgr,
        sal
from emp;
```

##### 1.6.7.`case then`

```sql
case when condition then result
	 when condition then result
	 ...
	 else default
end
```

```sql
select 
       empno,
       ename,
       case when job = 'CLERK'    then '办事员'
            when job = 'SALESMAN' then '销售员'
            when job = 'MANAGER'  then '经理'
            when job = 'ANALYST'  then '分析员'
            when job = 'PRESIDENT'then '老总'
            else '未知' 
        end as job,
        mgr,
        sal
from emp;
```

#### 1.7.字符串函数

* `concat()`：连接字符串

```sql
--How are you
select concat(concat('How',' are'),' you') from dual;
select 'How'||' are'||' you' from dual;
```

* `instr()`：检索子串在字符串出现的位置

```sql
instr(子串，字符串)
```

```sql
select instr('How are you','are') from dual;  --5，找到了输出子串的起始索引
select instr('How are you','is') from dual;   --0，未找到
```

* `substr()`：从指定位置截取指定的长度；不指定截取长度，默认截取到末尾

```sql
select substr('How are you',5) from dual;   --are you
select substr('How are you',5,3) from dual; --are
```

* `replace()`：替换指定的字符或字符串

```sql
--How are you
select replace('How is you','is','are') from dual; 
```

* `lpad(str1,length,substr)`：在`str1`的左侧使用`substr`填充到指定长度

```sql
--str1.length > length
select lpad('89',6,'0') from dual;   --000089
--str1.length < length,截取指定的长度
select lpad('73839',3,'0') from dual;--738
--填充的substr的长度 > 要填充的长度
select lpad('34','3','7384') from dual;  --734
```

* `rpad(str,length,substr)`：在`str`的右侧使用`substr`填充到指定长度

```sql
select rpad('32','5',0) from dual;      --32000
select rpad('32000','3','0') from dual; --320
select rpad('23',3,'123') from dual;    --231
```

* `length(str)`：获取`str`的字符长度

```sql
select length('你好好') from dual;  --3
```

* `lengthb(str)`：获取`str`的字节长度

```sql
select lengthb('你好好') from dual; --6
```

* `regexp_substr()`：正则截取字符串

```sql
-- regexp_substr(str,pattern,position,occurrence,modifier)
-- 【str】：处理的字符串
-- 【pattern】：正则表达式
-- 【position】：起始位置，即从第几个字符开始匹配正则表达式
-- 【occurrence】：标识第几个匹配组，默认为1，即分割后取第几个值
-- 【modifier】：模式，
	-- [i]：不区分大小写进行检索
	-- [c]：区分大小写进行检索，默认值
	
select regexp_substr('a,b,c,d','[^,]+',1,3,'i') from dual;  -- c
-- 显示所有分割字符
select regexp_substr('a,b,c,d','[^,]+',1,level,'i') from dual
connect by level <= 4 ;
-- 根据实际分割的字符数显示所有字符
select regexp_substr('a,b,c,d', '[^,]+', 1, level, 'i')
  from dual
connect by length('a,b,c,d') - length(regexp_replace('a,b,c,d', ',', '')) + 1

```

* `regexp_replace()`：正则替换字符串

```sql
-- regexp_replace(source_char, pattern [, replace_string [, position [, occurrence [, match_parameter ] ] ] ] )
-- 【source_char】：要替换的字符串
-- 【pattern】: 正则表达式
-- 【replace_string】：替换的字符串
-- 【position】： 开始所搜位置
-- 【occurrence】：指定出现的第几个字符被替换，[0]替换所有，[n]替换第n个匹配的字符
-- 【match_parameter】：模式
	-- [i]：不区分大小写进行检索
	-- [c]：区分大小写进行检索，默认值

-- luck is my network id
select regexp_replace ('itmyhome is my network id', '^(\S*)', 'luck') from dual;
```

* `regexp_like()`：正则匹配查询

```sql
-- regexp_like ( expression, pattern [, match_parameter ] )
-- 【expression】：要搜索的字段
-- 【pattern】：正则表达式
-- 【match_parameter】：模式
	-- [i]：不区分大小写进行检索
	-- [c]：区分大小写进行检索，默认值

select * from emp where regexp_like(empno,'3','i');
```







### 十七、`DbLink`同步`clob/blob`

#### 17.1.`dblink`传输`clob`字段的方案

* 多库交互时，使用`DBLINK`有时会引起`SCN`传播问题 
* 如要查询`CLOB`字段时，无法直接进行查询，需要做处理才能查询出数据 

##### 17.1.1.使用`to_char()`进行转换

```sql
--若clob的字段的内容不超过4000，可使用to_char()函数进行类型转换
select to_char(clob) from history;
--若clob的字段的内容超过4000，使用to_char()函数处理抛异常，因为varchar2只能存放4000个字节
--此时可以需要进行截取dbms_clob(clob,4000)
select dbms_lob.substr(clob,4000,1) from history;
--但数据会丢失一部分
```

#### 17.2.在目标库创建临时表

```sql
--在目标库创建临时表，同步远程库的数据
--这种临时表不占用表空间，而且不同的SESSION之间互相看不到对方的数据
create global temporary table temp_history on commit preserve rows
as
select * from history@clob_libk;

--在目标库中，将临时表的数据同步的正式表中
insert into history 
select * from temp_history;

--删除临时表
drop table temp_history;
```

```sql
--在commit提交的时候清空临时表数据
on commit delete rows    
--在会话结束的时候清空数据
on commit preserve rows
```

#### 17.3.在目标库创建视图

```sql
--创建视图，将clob类型转化为varchar2类型，字符的长度不能超过4000
create or replace view v_history
as
select history_id,history_name,dbms_lob.substr(change_result,4000,1) change_result
from history;
```







### 十八、排序

#### 18.1.将`null`排序在最后

```sql
--排序时把null放在最后
order by aae036 desc nulls last
```



### 十九、创建临时表

```sql
--创建了一个永久的表结构，并没有同步数据
create global temporary table tmp_uj21  as select * from uj21;  
--原因：默认是on commit delete row这个类型的临时表，commit后会删除数据
--create属于DDL语句，触发后会隐式提交事务
```

```sql
--复制了一个表的结构和数据
create table copy_uj21 as select * from uj21;
```

* `on commit delete row `：默认选项，在`commit`的时候将数据删除 
* `on commit preserve row `：在`commit`的时候将数据保留，会话结束后自动删除 

```sql
--创建on commit preserve row类型的临时表
create global temporary table uj21_tmp  on commit preserve rows as select * from uj21;  
--此时创建的临时表不能直接删除
drop table uj21_tmp;
```

![1528791253130](E:\typora\images\1528791253130.png)

```sql
--解决方式
truncate table uj21_tmp;
drop table uj21_tmp;
```



### 二十、日期

#### 20.1.求某天是星期几

```sql
select to_char(sysdate,'day') from dual;   --星期二
select to_char(sysdate,'day','NLS_DATE_LANGUAGE = American') from dual;  --tuesday  
--设置日期语言
alter session set nls_date_language='AMERICAN';     
```

#### 20.2.求日期之间的天数

```sql
select floor(sysdate - to_date('20180618','yyyymmdd')) from dual;  --1
```

### 二十一、树形递归查询

`start with condition connect by (prior)`对树形结构的数据进行查询 。

* `start with  condition` ：指定数据搜索的范围
* `connect by ` ：指定递归查询条件
  * `prior  `：表示上一条记录

```sql
--表结构（树形）
create table TAMENU(
  menuid          NUMBER(10) not null,
  pmenuid         NUMBER(10) not null,
  menuname        VARCHAR2(60),
  url             VARCHAR2(100),
  menulevel       NUMBER,
  isleaf          CHAR(1)
)
```

#### 21.1.找某个节点所有子节点

```sql
--自上向下找子节点
select 
  menuid,
  pmenuid,
  menuname     
from tamenu
start with menuid = '1'
connect by prior menuid = pmenuid;
```

```sql
--从根节点开始向下查找
select 
  menuid,
  pmenuid,
  menuname     
from tamenu
start with pmenuid = '0'
connect by prior menuid = pmenuid;
```



![1529573295575](E:\typora\images\1529573295575.png)

#### 21.2.查找某个节点的父节点

```sql
--自下向上找父节点
select 
  menuid,
  pmenuid,
  menuname     
from tamenu
start with menuid = '118556'
connect by menuid = prior pmenuid;
```

![1529573261791](E:\typora\images\1529573261791.png)

#### 21.3.查询每个菜单包含的子菜单数

```sql
select 
  menuid,
  max(menuname) menuname,
  count(1)
from tamenu
group by menuid
having count(1) > 1
connect by prior pmenuid = menuid
order by menuid;
```

![1529630146977](E:\typora\images\1529630146977.png)

#### 21.4.查询所有的叶子节点

```sql
--即查询pmenuid中没有的menuid,这部分节点就没有上级节点
select * 
from tamenu 
where menuid not in (select pmenuid from tamenu) 
order by menuid;
```

![1529630715732](E:\typora\images\1529630715732.png)

#### 21.5.查询某个层级的所有节点

```sql
--树形结构在节点很多的情况下，一般使用异步加载刷新的方式，但是默认情况下会展示到某个层级
--这是不但要获取某个节点的祖先节点，还有获取该节点的兄弟节点
select 
  menuid,
  pmenuid,
  menulevel
from tamenu
where menulevel > 1
start with menuid = '1'
connect by prior menuid = pmenuid
order by level;
```

![1529631769892](E:\typora\images\1529631769892.png)

#### 21.6.显示数据的树形级别

```sql
select 
  menuid,
  pmenuid,
  lpad(' ',(menulevel-1)*4)||menuname,
  menulevel
from tamenu
start with menuid = '1'
connect by prior menuid = pmenuid; 
```

![1529634620899](E:\typora\images\1529634620899.png)



### 二十二、执行

```sql
--生成执行计划
explain plan for select * from tamenu where pmenuid = '6';
--查看执行计划
select * from table(dbms_xplan.display);
```

