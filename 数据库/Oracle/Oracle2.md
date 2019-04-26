

# 一、行转列

```sql
select * 
from (select count(*) "total" from emp),
     (select count(*) "1980" from emp where to_char(hiredate,'YYYY')='1980'),
     (select count(*) "1981" from emp where to_char(hiredate,'YYYY')='1981'),
     (select count(*) "1982" from emp where to_char(hiredate,'YYYY')='1982'),
     (select count(*) "1987" from emp where to_char(hiredate,'YYYY')='1987');

--通过判断满足条件的数据，分别统计
select count(*) total,
       sum(decode(to_char(hiredate,'YYYY'),'1980',1,0)) "1980",
       sum(decode(to_char(hiredate,'YYYY'),'1981',1,0)) "1981",
       sum(decode(to_char(hiredate,'YYYY'),'1982',1,0)) "1982",
       sum(decode(to_char(hiredate,'YYYY'),'1987',1,0)) "1987"
from emp;
```

# 二、`oracle`插入多条数

```sql
INSERT ALL 
into emp(EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) values (8369, 'BOM', 'ANALYST', 7902, to_date('17-12-1980', 'dd-mm-yyyy'), 800.00, null, 20)
into emp(EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO) values (8499, 'ALLEN', 'SALESMAN', 7698, to_date('20-02-1981', 'dd-mm-yyyy'), 1600.00, 300.00, 30)
select 1 from dual;  
```

```sql
INSERT ALL 
into emp values (8369, 'BOM', 'ANALYST', 7902, to_date('17-12-1980', 'dd-mm-yyyy'), 800.00, null, 20)
into emp values (8499, 'ALLEN', 'SALESMAN', 7698, to_date('20-02-1981', 'dd-mm-yyyy'), 1600.00, 300.00, 30)
select 1 from dual;  
```

注意:
1. `select 1 from dual`中的dual表可以被替换为任何一个只要不是tb_red的表。
2. 只适合于`Oralce 9i`以上版本
3. 可以向多个表插入多条数据

```sql
insert into tab_name select * from tab_name1;
--写入多条记录，记录集已经确定
insert into tablename (col1,col2,...)
   select * from (
   select 'a1','a2',... from dual union all
   select 'a1','a2',... from dual union all
   select 'a1','a2',... from dual union all
   ...
   select 'a1','a2',... from dual
   )
```

***
# 三、`Oracle`说明`(COMMENT)`
1. 对表的说明
```sql
comment on table table_name is "comments_on _tab_information";
comment on aa10 is "码值表";
```
2. 对表中列的说明
```sql
comment on column table.column_name is "comments_on_column_information";
comment on column aa10.aaa100 is "aaa100 代码类别";
```
3. 查看表的说明
```sql
select * from user_tab_comments where table_name='AA10';
select * from all_tab_comments where table_name = 'AA10';
```
>注意：`table_name='AA10'`的值需要大写

4. 查看表中列的说明
```sql
select * from user_col_comments where table_name = 'AA10';
select * from all_col_comments where table_name = 'AA10';
```
>注意：`user_col_commnets`是当前用户下表的列说明，`all_col_comments`是当前用户所有可见表的列说明

5. 删除表级说明
```sql
comment on table aa10 is '';
```
>删除表级说明，就是将说明置空

6. 删除列级说明
```sql
comment on column aa10.aaa100 is '';
```
>删除列级说明就是将说明置空

***
# 四、复制
## 4.1. 复制表格数据
1. 目标表已经存在
```sql
--复制表中的数据到相应的目标表字段中
insert into aa10_dest(aaa100,aaa101,aaa102,aaa103) 
select aaa100,aaa101,aaa102,aaa103 from aa10;
```
2. 目标表不存在

```sql
create table aa10_dest 
as
select aaa100,aaa101,aaa102,aaa103 from aa10;
```
>复制了指定字段及其数据，先复制表结构，再复制表数据
```sql
create table aa10_dest as select aaa100,aaa101,aaa102,aaa103 from aa10 where 1 = 2;
```
>加入一个用不成立`1=2`调条件，表示只复制表结构
```sql
create table aa10_dest as select * from aa10;
```
>完全复制表,包含表结构和表数据
```sql
select aaa100,aaa101,aaa102,aaa103 into aa10_dest from aa10;
```
>在`oracle`中会抛出异常，这是`mysql`的语句
```sql
select t1.testname into aa from test1 t1 where id=1;
```
>`PL/SQL`的赋值语句

***
3. 不同用户之间复制
```sql
insert into demo.aa10_dest select aaa100,aaa101,aaa102,aaa103 from yhjy.aa10;
```
>此时需要登录到有权限的用户

***
# 五、导入导出
- `exp`和`imp`是客户端工具程序，它们既可以在客户端使用，也可以在服务端使用，`dos`环境下执行命令；
  - 高版本`exp`导出的`dmp`文件，低版本`imp`不能导入；
  - 低版本`exp`导出的`dmp`文件，高版本`imp`可以导入；

- `expdp`和`impdp`是服务器端的工具程序，只能在`oracle`服务器端使用，不能在客户端使用。

- `imp`只适用`exp`导出的文件，不使用与`expdp`导出的文件；`impdp`只适用`expdp`导出的文件，而不使用于`exp`导出的文件。

- `exp`不能导出分区表，而`expdp`可以。

## 5.1.`exp`导出
1. 查看导出命令的语法信息
```dos
exp help = y
```
2. 完全导出数据库`orcl`
```dos
exp system/manager@orcl file=d:\test.dump full=y
```
3. 数据库中指定用户的表导出
```dos
exp system/manager@orcl file=d:test.dump owner=(system,scott)
```
4. 数据库中指定表的导出
```dos
exp system/manager@orcl file=test.dump tables=(aa10,user)
```
`file=test.dump`此时表示相对路径

5. 数据库表中指定查询条件下的数据
```dos
exp system/maanger@orcl file=d:\test.dump tables=(user) query=\"where username='xy'\"  compress=y 
```
`compress=y `对`dump`文件进行压缩

6. 建立参数文件导出
>创建一个`param.par`文件
```
file=test.dump
log=test.log
compress=y
tables=(aa10,user)
buffer=100000
query=\"where userid = 'u0001'\"
```
>导出
```dos
exp username/password parfile=param.par
```

7.导出密码带有特殊字符的，密码双引号，连接串单引号

```shell
exp 'testuser/"test/15/!&/57"@localhost:1521/ora11g'  tables=inner_notify file=exp_export.dmp log=exp_export.log
```

---



## 5.2.`imp`导入

1. 查看导入命令的语法信息
```sql
imp help = y
```
2. 导入
```shell
imp system/manager@orcl ignore=y full=y file=d:\test.dump log=d:\test.log
```

导入时不加`ignore=y`，若有的表已经存在，则它会报错，且不导入该表；若加了`ignore=y`就可以导入已存在的表，但是导入的数据可能出现重复。

3. 导入指定用户的数据
```shell
 imp system/manager@orcl file=d:\test.dump fromuser=(scott)
```
4. 将一个用户的数据导入到另一个用户
```shell
imp system/manager@orcl file=d:\test.dump fromuser=(scott) touser=(sys)
imp system/manager@orcl file=d:\test.dump fromuser=scott touser=sys
```
>将`scott`用户的数据导入到`sys`用户中

5. 导入指定表的数据
```shell
imp system/manager@orcl file=d:\test.dump tables=(aa10,user)
```
6. 多个导入文件同时导入
```shell
imp system/manager@orcl file=(d:\test.dump,e:\demo.dump) filesize=1G 
```
7. 参数文件导入
>建立参数文件`param.par`

```shell
file=test.dump
fromuser=scott
touser=sys
log=test.log
```
>导入

```shell
imp system/manager parfile=param.par
```
8. 导入模板
```sql
--删除用户
drop user jyrc cascade;
--创建表空间

--创建用户
create user jyrc 
identified by jyrc
default tablespace data
temporary tablespace temp;
--授权
grant connect,resource,dba to jyrc;

--导入
imp jyrc/jyrc@orcl file=f:/jyrc.dmp full=y ignore=y log=f:/jyrc20171226.log
```

## 5.3.`Oracle 11g`使用`exp`导出时，空表不能导出

`11G`中有个新特性，当表无数据时，不分配`segment`，以节省空间。

> 1、 设置`deferred_segment_creation`为`false`，指定之后创建的表有效

```
alter system/system set deferred_segment_creation=false;  
```

> 2、创建表的时候声明立即创建`Segment`

```
create table table_name segment creation immediate;
```

> 3、对于已经创建但是还没有`Segment`的表来说

```sql
--执行此语句来分配空间
alter table table_name allocate extent;
--也可插入一条语句来分配空间
```

> 4、批量处理

```
--查询出所有没有分配segment的表
select 'alter table ' || table_name || ' allocate extent;'  AS SQLSTR 
	from user_tables 
where segment_created= 'NO' ;
--执行结果，分配segment
```

### 5.3.1.`deferred_segment_creation`

`11.2.0.4g`才有的参数,指创建一个表，在没有插入数据时**是否分配空间**，为`true`时，不分配空间，但在sys用户下不支持。

> - 在`sys`用户下，创建一个没有数据的表时，同时自动分配空间。
> - 在普通用户下，创建一个没有数据的表时，不会分配空间。

1. 查看`deferred_segment_creation`

```sql
show parameter deferred_segment_creation;
```

2. 修改`deferred_segment_creation`

```sql
alter system/system set deferred_segment_creation=false;  
```

3. 某`schema`中所有未分配`segment`的表

```sql
select *  
  from user_tables   
 where segment_created = 'NO';
```



## 5.4.`impdp`导入
1. 帮助命令
```dos
impdp -help
```
2. 导入到指定用户

```shell
impdp scott/tiger DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp SCHEMAS=scott;
```

3. 改变表的`owner`，将`scott`的数据导入到`system`用户下

```shell
impdp system/manager DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp TABLES=scott.dept REMAP_SCHEMA=scott:system;
```

4. 导入表空间

```shell
impdp system/manager DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp TABLESPACES=example;
```

5. 导入整个数据库

```shell
impdb system/manager DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp FULL=y;
```

6. 追加数据

```shell
impdp system/manager DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp SCHEMAS=system TABLE_EXISTS_ACTION=append
```

使用`impdp`完成数据库导入时，若表已经存在，有四种的处理方式:

- `skip`:默认操作
- `replace`:先删除`drop`表，然后再创建表，最后插入数据
- `append`:在原来的数据基础上添加数据
- `truncate`:先`truncate`表，再插入数据



## 5.5.`expdp`导出

1. 帮助命令
```dos
expdp -help
```

2. 按指定用户的导出数据

```shell
expdp jyrc/jyrc@porcl schemas=jyrc dumpfile=jyrc20190325.dmp version='11.2.0.1.0' DIRECTORY=PATH
```

3. 并行进程`parallel`导出数据

```shell
expdp scott/tiger@localhost:1521/orcl directory=DUMP_DIR dumpfile=expdp_export.dmp parallel=40 job_name=expdp40
```

4. 按指定表名导出数据

```shell
expdp scott/tiger@localhost:1521/orcl TABLES=emp,dept dumpfile=expdp_export.dmp DIRECTORY=DUMP_DIR
```

5. 按查询条件导出

```shell
expdp scott/tiger@localhost:1521/orcl directory=DUMP_DIR dumpfile=expdp_export.dmp tables=emp query='WHERE deptno=20'
```

6. 按表空间导出数据

```shell
expdp system/manager DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp TABLESPACES=temp,example;
```

7. 导出整个数据库

```shell
expdp system/manager DIRECTORY=DUMP_DIR DUMPFILE=expdp_export.dmp FULL=y;
```







## 5.6.`DIRECTORY`

1. 新建逻辑目录`DIRECTORY`，逻辑目录并不会自动创建，若目录不存在导出会报错，故需要手动创建

```sql
CREATE [OR REPLACE] DIRECTORY directory_name AS 'pathname';
create or replace directory exp_dir as 'h:\tmp';
```

2. 查询所有的`directory`

```sql
select * from dba_directories;
```

3. 授权

```sql
--将读写权限授予指定用户
grant read,write on directory directory_name to user_name;
```

4. 删除`directory`

```sql
drop directory directory_name;
```





# 七、用户

1. 创建用户
```sql
--语法
create user user_name identified by password
[default tablespace tablespace_name]     -- 用户存储使用的表空间
[temporary tablespace tablespace_name]   -- 用户使用的临时表空间
-- 空间配额，unlimited表示不设置配额
[quota 数字 [K|M] | unlimited on tablespace_name,...] 
[profile profile_name | default]   --概要文件，即用户操作的资源文件
[password expire]  --密码失效，第一次使用时必须修改密码
[account lock | unlock] --使用是否为锁定状态
 
 -- 创建demo用户
create user demo 
identified by demo
```
2. 查看用户信息
```sql
select username,user_id,default_tablespace,temporary_tablespace,created,lock_date,profile 
from dba_users
where username='SYS';
```
3. 查看用户用表空间配额
```sql
select * 
from dba_ts_quotas 
where username = 'SYS';
```
4. `12c`的用户分为两类，分别是`Commons User`和`Local User`,目的是为了`Oracle`云平台的创建，区别：1.`Commons User`保存在`CDB`中，`Local User`保存在`PDB`中；2. 创建`CDB`用户需要使用`c#`或`C#`开头。
5. 修改用户的概要文件
```sql
alter user user_name profile profile_name;
```
6. 修改用户密码
```sql
alter user user_name identified by new_password;
```
7. 控制用户的锁定
```sql
alter user user_name account lock | unlock;
```
8. 让用户密码失效
```sql
alter user user_name password expire;
```
密码失效后，用户在进行登录时，必须强制修改密码。
9. 修改空间配额
```sql
alter user user_name quota 数字 [K|M] | unlimited on tablespace_name,...;
```
10. 删除用户
```sql
drop user user_name [cascade];
```
若用户在存在期间进行了数据库对象的创建，则可以利用`cascade`删除模式中所有对象。
删除一个用户后，此用户下的所有对象都会一起删除。

***
# 八、概要文件
1. 语法
```sql
create profile profile_name limit 命令（s）
```
2. 命令格式



3. 查看概要文件
``` sql
select * from dba_profiles;
```
4. 修改概要文件
```sql
alter profile profile_name limit
cpu_per_session 1000
password_life_time 10
```
5. 删除概要文件
```sql
drop profile profile_name [cascade];
```
若已经有用户使用该概要文件时，必须使用`cascade`才能删除。
删除后，使用该概要文件的用户会自动将概要文件改为`default`。
***

# 九、权限管理
系统权限：进行数据库资源操作的权限。如创建表或索引等。
对象权限：维护数据库中对象的能力，即由一个用户操作另一个用户的对象。
所有的权限都由`DBA`控制。
1. 常用权限

2. 系统权限`sysdba`和`sysoper`
    `sysdba`包含的授权操作如下：
* `create database`
* `alter tablespace begin / end backup`
* `recover database until`
  `sysoper`包含的授权操作如下：
* 执行`startup`和`shutdown`操作
* 执行`alter database open | mount | backup`
* `archivelog`和`recovery`
* `create spfile`
* `restricted session`
3. 授权
```sql
grant 权限，...
to [用户名...|角色名，...|public]
[with admin option]
```
`public`表示将权限设为公共权限
`with admin option`将用户的权限能继续授予其他用户。
4. `create seesion`
    `create session` 用户具有此权限才能登陆。因为每一个连接到数据库的用户都是通过一个`session`进行表示。
5. 旧版的数据库存在的问题
    `Oracle 10g R2`之前的版本若需要创建表，还需要授予表空间的操作权限。
    一些旧版数据库，用户授权后还需要重新登录才能使用新的权限。
6. 查看用户的权限
```sql
select * from dbs_sys_privs;
select * from session_privs; 
```
7. 回收权限
```sql
revoke 权限，... from 用户名;
revoke create table,create view from user_name;
```
回收权限可以通过管理员，或者为用户授权的其他用户回收。
## 9.1.对象权限
指数据库中某一对象所拥有权限，即可以通过某一用户的对象权限，让其他用户来操作本用户中所授权的对象。分别是`select`、`insert`、`update`、`delete`、`execute`、`alter`、`index(索引)`、`references(关联)`
1. 语法
```sql
grant 对象权限 | all [(cloumn,..)]
on 对象
to [用户名 | 角色名 | public]
[with grant option]
```
2. 查看当前用户的对象权限
```sql
select * from user_tab_privs_recd;
```
3. 查看当前用户所具备列的对象权限
```sql
select * from user_col_privs_recd;
```
4. 查看某一数据库对象分配出去了哪些权限
```sql
select * from user_tab_privs_made;
select * from user_col_privs_made;
```
5. 回收对象权限 
```sql
revoke [权限,... | all]
on 对象
from [用户,... | 角色,.. | public]
```
只能按照对象权限回收，不能按照列权限回收。

***
# 十、角色
角色就是一组相关权限的集合。
1. 查看用户所具有的权限
```sql
select * from dba_sys_privs;
select * from session_privs;
```
2. 查看用户所具有的角色
```sql
select * from dba_role_privs;
select * from dba_roles;
```
3. 查看角色所具有的权限
```sql
select * from role_sys_privs;
```
4. 创建角色
```sql
create role role_name
[not idenntified | identified by 密码]
```
5. 创建角色时设置的密码，是在角色启动时使用。

禁用当前会话中所有的角色
```sql
set role none;
```
启用当前会话中的所有角色
```sql
set role all;
```
启用某一个角色
```sql
set role role_name identified by passwrod;
```
6. `dba`开始的数据字典需要管理员才能访问
7. 角色授权
```sql
grant [权限,...] to role_name;
```
8. 为用户授予角色
```sql
grant [role_name ,...] to user_name;
```
9. 修改角色
```sql
alter role role_name [not identified | identified by password];
```
10. 回收角色权限
```sql
revoke [权限,...] from role_name;
```
11. 删除角色
```sql
drop role role_name;
```
12. `Oracle`的预定义角色

| NO.  | 预定义角色             | 描述                 |
| :--- | :--------------------- | :------------------- |
| 1    | `exp_full_dafabase`    | 导出数据库权限       |
| 2    | `imp_full_database`    | 导入数据库权限       |
| 3    | `select_catalog_role`  | 查询数据字典权限     |
| 4    | `execute_catalog_role` | 数据字典上的执行权限 |
| 5    | `delete_catalog_role`  | 数据字典上的删除权限 |
| 6    | `dba`                  | 系统管理相关的权限   |
| 7    | `connect`              | 授予用户最典型的权限 |
| 8    | `resource`             | 授予开发人员权限     |
```sql
grant connect,resource to user_name;
```
***
# 十一、数据库设计范式
1. 第一范式
    第一范式主要针对单表关系，定义：数据库表中的每个字段都是不可再分的原子数据项。这里的不可再分指的是满足数据库的数据类型。
2. 第二范式





***
# 十二、表的创建
1. `DDL`不受事务的控制，事务只能针对`DML`
2. 执行了任意的`DDL`语句，为提交的事务都将自动提交
3. 数据类型

| 序列 | 数据类型 | 说明 |
| :--: | :------: | :--- |
|  1   |  BFILE   |      |
|  2   |   RAW    |      |
|  3   |   LONG   |      |
|  4   | LONG RAW |      |
4. 查看当前用户拥有的表
```sql
select * from tab;
```
5. 查看表结构
```sql
desc tablename;
```
6. `create`语句创建的都是数据库对象。
7. 重命名表名
```sql
rename old_table_name to new_table_name;
```
8.  数据字典
>数据字典：记录数据库的操作信息
>静态数据字典：由表或视图组成，规则如下
>* `user_*`:存储当前用户的所有对象信息
>* `all_*`:存储当前用户可以访问的大小信息
>* `dba_*`:存储数据库的所有大小信息，`dba`操作
>  动态数据库字典:随数据库的运行不断更新数据，一般保存内存和磁盘的状态

# 删除表数据
`delete from`
* 删除数据，不会释放索引和约束等
* 每删除一行数据，都会在事务日志中记录一项
* 不会自动提交,属于`DML`

`truncate table tablename`
* 表所占用的全部资源都释放；
* 应用：若某些表在一定的时期需被清空，再重新保存数据，可使用
* 通过数据页来删除数据，只会在事务日志中记录页的释放
* 会自动提交,属于`DDL`

# 删除表
```sql
drop table tablename
```
>`drop`删除表后，会将数据放在回收站，并且查看`select * from tab`中会用一条`BINXXXX`的数据

闪回技术用于从回收站回收数据表
```sql
flashback table tablename to before drop;
```
直接删除表
```sql
drop table tablename purge;
```
从回收站删除表
```sql
purge table tablename;
```
清空回收站
```sql
purge recyclebin;
```
# 修改表结构
1. 新增字段
```sql
alter table tablename add(columname column_ default 默认值，...)
alter table emp add(age number(3) default 0)
```
2. 修改字段
```sql
alter table tablename modify (字段名称 字段类型 default 默认值)
alter table emp modify(name varchar2(30) default '无名氏')
```
3.  删除字段
```sql
alter table tablename drop column column_name
alter table emo drop column age;
```
删除字段时，表中至少要保留一个字段
删除的字段不论是否有数据，都不影响删除结果

4. 设置字段为无用状态
```sql
alter table tablename set unused(column_name)
alter table tablename set unused column column_name
```
5.  删除无用的字段
```sql
alter table tablename drop unused columns;
```
6. 设置字段可见/不可见`12c支持`
```sql
alter table tablename modify(字段名称 [visible|invisible])
```
7. 创建表时，设置表不可见
```sql
create table mytable(
id number,
name varchar2(30) inviable
)
```
***

# 十三、数据的约束
1. **数据库的完整性**与**数据库的安全性**的区别
    **数据库的完整性**是防止数据库中存在不正确的数据
    **数据库的安全性**是保护数据库，防止恶意的破坏和非法的存取
2. 约束的类型

| 约束        | 说明                                             |
| :---------- | :----------------------------------------------- |
| 非空约束    | 不允许设置`null`                                 |
| 唯一约束    | 该列的数据不允许数据重复，不包含`null`的重复判断 |
| 主键约束    | 唯一性标识，不能重复，且不能为空                 |
| 检查约束    | 用户自行编写设置内容的检查条件                   |
| 主-外键约束 | 两张表进行的关联约束                             |
3. 候补码`(侯补键)`：在关系中，能唯一标识一条元组（记录）的一个属性或属性组（多个属性），这个属性或属性组就是候补码
4. 约束的定义
```sql
--唯一性约束
constraint uk_email unique(email)
--主键约束
constraint pk_mid primary key(mid)
--检查性约束
constraint ck_sex check(sex in ('男','女'))
--主外键约束
constranint fk_mid foreign key(mid) references primary_table(mid)
```
>存在主外键约束时，删除父表的数据时，需要先删除所有子表的对应数据。
>存在主外键约束时，删除父表时，需要先将字表删除

5. 级联删除
    存在主外键约束时，在创建表时，增加级联删除，就能直接删除父表的记录，且会将子表关联的记录删除
```sql
constraint fk_mid foreign key(mid) references primary_table(mid) on delete cascade
```
存在主外键约束时，删除父表数据，不删除子表数据
```sql
constraint fk_mid foreign key(mid) references primary_table(mid) on 
delet set null
```
6. 强制删除
    不考虑表之间的关联关系时，可以强制删除表
```sql
drop table tablename cascade constraint;
```
此时，子表及其数据会保存，但是父表会强制删除。
强制删除后就不能直接使用`purge`选项
7. 查看约束信息
```sql
-- 查看哪张表存在约束
select * from user_constraints;
-- 查看那个列上存在约束
select * fron user_cons_columns;
```
>`constraint_`约束类型，其简写：`primary key(P)、foreign key(F)、check(C)、not null(C)、unique(U)`
8. 修改约束
    不建议修改约束
```sql
-- 增加约束
alter table  tablename add constraint 约束名称 约束类型(约束字段)
alter table mytable add constraint pk_mid primary key(mid);

-- 增加非空约束,只能通过修改字段的形式
alert table tablename modify(name varchar2(30) not null);

--约束会增加性能消耗，当大规模向表中增加数据时，可以暂时关闭约束
--关闭约束
alter table tablename disable constraint 约束名称 [cascade];
alter table member disable constraint pk_mid;
--若member的pk_mid被其他表的外键引用，关闭约束时，需要加上cascade

--启用约束
alter table tablename enable constraint 约束名称;
slert table member enable constraint pk_mid;

-- 删除约束
alter table tablename drop constraint 约束名称 [cascade];
alert table member constraint pk_mid;
--若member的pk_mid被其他表的外键引用，需要加上cascade,进行强制删除
```


# 十四、表空间

临时表空间主要用于排序操作。
1. 创建表空间
```sql
create tablespace tablespacename
default 'd:/data01.dbf' size 50M,
		'd:/data02.dbf' size 40M
autoextend on next 5M
logging;
```
>文件目录需要存在

2. 创建临时表空间
```sql
create temporary tablespace tempname
tempfile 'd:/data_temp01.dbf' size 50M,
         'd:/data_temp02.dbf' size 50M
autoextend on next 2M
nologging;
```
3. 查看表空间的信息
```sql
select * from dba_tablespaces;
```
![Alt text](./1507987938966.png)
>`contents`中`PERMANENT`表示永久表，`TEMPORARY`表示临时表，`UNDO`?

4. `Oracle`的默认表空间

| 序号 |    表空间     | 说明                                                         |
| :--: | :-----------: | :----------------------------------------------------------- |
|  1   |    SYSTEM     | 数据至少存在一个表空间，即SYSTEM表空间，主要存储全部的`PL/SQL`程序的源代码和编译的代码 |
|  2   |    SYSAUX     | 是SYSTEM表空间的辅助表空间，存储数据库工具和可选组件         |
|  3   |     USERS     | 存储用户数据                                                 |
|  4   | UNDO/UNSOTBS1 | 用于实物的回滚、撤销                                         |
|  5   |     TEMP      | 临时表空间，存储运行需要的临时数据，如排序的中间结果         |
5. 查看表空间下的数据文件信息
```sql
select * from dba_data_files;
select * from dba_temp_files;
```
6. 创建表时指定表空间
```sql
create table mytable(
id number,
name varchar2(40)
)tablespace tablespacename;
```
# 十五、视图
视图时一个不包含任何真实数据的虚拟表，数据库中之存放视图的定义，不存放视图对用的数据，视图的数据仍然存放在原来的尸体表中，视图就相当于一个窗口。
1. 视图语法
```sql
create [force|noforce] [or replace] view view_name((别名1),(别名2)，...)
as 
子查询 [with check option [constraint 约束名称]]
[with read only]
```
`force`表示创建视图的表不存在时，也可以创建视图
`noforce`默认,表示创建视图的表必须存在，否则不能创建
`or replace`视图的替换，视图不存在就创建，视图存在就替换
2. 查看视图
```sql
select * from user_views;
select * from tab where tab='VIEW';
```
3. 创建视图的权限
```sql
grant create view to scott;
```
4. 对视图的增删改，实际操作的是原始数据表的数据。
5. 简单视图（一张表）可以增删改
6. 复杂视图（多张表）只能删除（只能删除一张表），不能插入数据和修改数据。
7. 实现复杂视图的更新，可以使用触发器来实现。
8. 保证创建视图时限制条件不被修改(`where`条件)
```sql
create or replace view v_emp20
as 
select * from emp where deptno = 20
with check option contiraint v_emp20_ck;
```
若不设置约束名，系统会自动分配一个名称。
9. 保证视图的创建条件（显示的字段）不被更改
```sql
create or replace view v_emp20
as 
select * from emp where deptno = 20
with read only;
```

***
# 十六、序列
1. 查看序列
```sql
select * from user_seqences;
```
2. 序列的当前值`currval`,只有第一次调用`nextval`后，序列才有值，才能调用`currval`；`nextval`下一个序列值。
3. 删除序列
```sql
drop sequence sequance_name;
```
4. 创建序列的语法
```sql
create sequence sequenc_name 
[increment by 步长]
[strat with 开始值]
[minvalue 最小值 | nominvalue]
[maxvalue 最大值 | nomaxvalue]
[cycle | nocayle]
[cache 缓存大小 | nocache] --默认缓存大小20 
```
5. 修改序列的语法
```sql
alter sequence sequence_name
[increment by 步长]
[maxvalue 最大值 | nomaxvalue ]
[minvalue 最小值 | nominvalue]
[cycle | nocycle]
[cahce 缓存大小 | nocache]
```
序列真实数值为缓存中保存的内容，所以数据库实例重启后，缓存中的保存的数据就会消失，这就可能导致序出现跳号(即序列值不连贯)。若要避免此类情况，可以不使用缓存`nocache`
6. `12c`提供了自动增长的序列
    ``	sql
    create table table_name(
    cloumn_name  generated by default 
    				 as indentity (
    				 [increment by 步长 ]
    				 [minvalue 最小值 | nominvalue]
    				 [maxvalue 最大值 | nomaxvalue]
    				 [cycle | nocycle]
    				 [cache 缓存大小 | nocache]
    			     )，

    coluumn_name ,
    ...
    )
```
>不建议修改序列，因为序列的值是连续的，修改后容易导致值的混乱。
>不建议修改数据库对象，因为可能导致对象值或结构的异常
7. 删除自增长序列
自增长序列依附于数据表存在，若删除数据 表时没有使用`purge`，则自增长的序列仍然保存着。
此时要删除就只能通过清空回收站来删除，不能使用`drop sequence`删除
​```sql
purge recyclebin;
```
***
# 十七、同义词
1. 同义词的作用是为了方便访问其他的用户对象。
2. 同义词是指为一个数据库对象起别名。如`dual`属于`sys`，`sys.dual`的同义词是`dual`
3. 创建同义词
```sql
create [public] synonym synonym_name
for 数据库对象
```
创建同义词需要相关的权限,若希望同义词被多个用户访问，就定义为公共同义词`public`。
4. 查看同义词
```sql
select * from user_synonyms;
```
5. 删除同义词
```sql
drop synonym synonym_name;
```
***
# 十八、伪列
1. 常见伪列
    `currval`、`nextvalue`、`sysdate`、`systimestamp`、`rownum`、`rowid`
2. 伪列由`oracle`自动帮用户创建
3. `rowid`是默认为每条记录分配的一个唯一的地址编码。
4. `rowid`的组成
    例子: `AAAWec AAG AAAAC2 AAA`

| 名称         |   编号   | 函数(都属于`dbms_rowid`包下)           | 作用             |
| :----------- | :------: | :------------------------------------- | :--------------- |
| 数据库对象号 | `AAAWec` | `dbms_rowid.rowid_object(rowid)`       | 确定表空间       |
| 相对文件号   |  `AAG`   | `dbms_rowid.rowid_relative_fno(rowid)` | 表空间下那个文件 |
| 数据块号     | `AAAAC2` | `dbms_rowid.rowid_block_number(rowid)` | 数据块           |
| 数据行号     |  `AAA`   | `dbms_rowid.rowid_row_number(rowid)`   | 行目录项         |
5. 删除数据表中重复的记录，只保留一条
```sql
delete from mydept
where rowid  not in(
	select min(rowid) 
	from mydept 
	group by deptno
)
```
6. `rownum` 行号
    根据记录的显示自动进行流水编号，且不与某行数据绑定。
7. `rownum`取第一行记录
```sql
select * from emp where rownum = 1;
```
`rownum`只能查第一行数据，不能取其他某行的数据，如`rownum=2`
8. `12c`的`fetch`
```sql
select * 
from table_name
where 
group by 
having 
order by 
[fetch first 行数]|[offset 开始位置 rows fetch next 个数]|[fetch next 百分比 percent] row only
```
取前n行的记录：`fetch first` 行数` read only `
取指定范围的行数：`offset` 开始位置 `rows fetch next` 个数 `read only`
按百分比取得记录：`fetch next` 百分比 `percent rows only`

***
# 十九、索引
  1. 索引的分类

    B树索引、降序索引、位图索引、函数索引
  2. 打开用户跟踪
```sql
set autotrace on;
```
需要`sys`用户权限
3. 设置主键约束或唯一性约束，会自动创建索引。
4. 创建索引
```sql
create index [用户名].索引名称
on [用户名].表名(列名称 [asc|desc]...)
```
创建了索引，就会在内存中将相关数据构建成一颗索引树。
5. 查看表上存在索引
```sql
select * from user_incexs;
```
6. 查看索引所在的列
```sql
select * from user_ind_columns;
```
7. 索引提升性能的主要手段在于树状结构，而执行了更新操作，就需要重新构建树，因此对于频繁更新的表，索引不一定会提高性能，还可能降低性能。
8. 采用冗余字段避免多表的查询。
9. 在更新频繁数据表上使用索引的解决方式？
    准备两张表，A表的数据进行更新，而另一张B表在空闲的时候（晚上）将A表更新的数据同步到B表中，在B表设置索引，用户检索使用B表，更新时使用A表，这样就能提高效率了，但是牺牲了实时性。
10. 创建降序索引
```sql
create index emp_hiredate_ind_desc on emp(hiredate decs);
```
11. 函数索引
```sql
create index emp_ename_ind on emp(lower(ename));
```
> 降序索引和函数索引都是让索引具有了一定的计算能力

12. 位图索引
      位图索引以一种压缩数据的格式存放，因此占用的磁盘空间比`B*Tree`小。
```sql
create bitmap index [user_name].index_name on 
[user_name].table_name(cloumn_name [asc|desc],...)
```
13. 索引需要对自身的数据结构进行维护，一般会占用较大的磁盘空间 ，故对于数据库中不常使用的索引，应该尽早删除。
```sql
drop index index_name;
```
***
# 二十、`sqlplus`
1. 设置每行显示的记录长度
```sql
set linsize 300;
```
2. 设置每页显示的记录数
```sql
set pagesize 30;
```
3. 打开本地记事本，编辑`sql`
```sql
ed test
```
`test(test.sql)`为打开文件的名称，若不存在就创建。编辑完成后保存`sql`，通过文件名调用`sql`。
```sql
test
-- 若为sql文件可以省略.sql
test.sql
test.txt
D:\test.sql
D:\test
```
使用	`ed`打开记事本后，`sqlplus`会进入阻塞状态，即此时`sqlplus`窗口处于不可编辑状态，直到关闭记事本后才结束。
```sql
--执行sql脚本,在命令行模式中
 f:\file_name.sql
strat f:\file_name.sql
/   --重新执行上次运行的sql
--将执行的内容输出到指定的文件中
spool f:\file_name.sql  
```
```sql
--格式脚本
set colsep ‘|'; –设置|为列分隔符
set trimspool on;
set linesize 120;
set pagesize 2000;
set newpage 1;
set heading off;
set term off;
set num 18;
set feedback off;
spool 路径+文件名;
select * from tablename;
spool off;
```

4. `sqlplus`登录
```sql
sqlplus
--输入用户名和密码

--连接数据库
conn user_name/password [as sysdba];
conn sys/sys as sysdba;
--查看当前用户
show user;
```
5. `12c`容器的切换
```sql
--查看当前容器
show con_name;
--将CDB切换到PDB
alter session set container = pdbmldn;
--打开pdbmldn可插入数据库
alter databse pdbmldn open;
--通过CDB打开pdbmldn可插入数据库
alter pluggable database pdbmldn open;
--切换会CDB容器
alter session set container = CDB$ROOT 
```
每当用户重新登录数据库时，实际上默认使用的都是`CDB`容器。
***
# 二十一、简单查询
1.`''''`四个单引号表示`'`
```sql
select '''' from dual;
```
2. 字符串的连接
```sql
select '姓名：'||name||'，年龄：'||age from emp;
```





# 三十、问题

## 30.1.连接`oracle`数据库时，使用本机`ip`地址可以，使用`localhost`不行

修改配置文件`D:\app\Administrator\product\11.2.0\dbhome_1\NETWORK\ADMIN\tnsnames.ora`，将`HOST`=你自己的主机名。
```
LISTENER =
  (DESCRIPTION_LIST =
    (DESCRIPTION =
      (ADDRESS = (PROTOCOL = IPC)(KEY = EXTPROC1))
      (ADDRESS = (PROTOCOL = TCP)(HOST = Administrator)(PORT = 1521))
    )
  )
```
# 二十二、Oracle解除死锁
（1）查看死锁
```sql
 select sess.sid,
        sess.serial#,
        lo.oracle_username,
        lo.os_user_name,
        ao.object_name,
        lo.locked_mode
   from v$locked_object lo, dba_objects ao, v$session sess
  where ao.object_id = lo.object_id
    and lo.session_id = sess.sid
```

（2）解锁
```sql
ALTER SYSTEM KILL SESSION '26,1279' immediate; 
```
（3）查看是哪个session引起的
```sql
select b.username,b.sid,b.serial#,logon_time
  from v$locked_object a,v$session b
  where a.session_id = b.sid order by b.logon_time;
```
（4） 查看锁住表的用户
```sql
select b.username,b.sid,b.serial#,logon_time

from v$locked_object a,v$session b

where a.session_id = b.sid order by b.logon_time;
```

# `for update `死锁
```sql
select * from bd_corp for update
```
在`pl/sql Developer`具的的菜单`“tools”`里面的`“sessions”`可以查询现在存在的会话

# `DBLink`
- `DbLink`实现数据库的跨库访问，`dblink`分为公有和私有；
  - 公有的`dblink`使用`public`修饰，在`create`和`drop`时都需要加上`public`；
    公有的`dblink`对所有的用户开放，在`dblink`上创建的同义词也会随之对所有人开放；
  - 私有的`dblink`只对创建者开放，在`dblink`上创建的同义词也不能被其他用户访问，需要为用户创建视图，并将视图授权给所需用户后，用户才可访问该视图。

1. 查看用户是否具有权限
```sql
select * from user_sys_privs where privilege like upper('%DATABASE LINK%') and username = upper('scott');
```
2. 没有`database link`权限，需要授予`创建DbLink`的权限
```sql
grant create public database link to scott;
```
3. 创建`DbLink`
```sql
create public database link scott  -- 链接名
connect to scott       --用户
identified by `scott ` --密码，注意：密码为数字开头，使用双引号("")
using 'ORCL'           --连接的数据库
```
4. 使用`dblink`
```sql
--查询
select * from scott.emp@scott;
--插入
insert into emp@scott(empno,ename,job,mgr,hiredate,sal,comm,deptno)
values('7999','LISI','CLERK','7902',sysdate,3000,null,20);
--修改
update emp@scott 
set sal = 4000 
where empno = '7999';
--删除
delete from emp@scott 
where empno = '7999';
```
5. 可以使用同义词`synonym`给**`DbLink`链接**创建一个别名
```sql
create synonym scott_emp for emp@scott;
--查询
select * from scott_emp;
```
6. 删除`dblink`
```sql
drop public database link scottlink;
```
7. 查看创建的`dblink`
```sql
select * from dba_objects where object_type like upper('%DATABASE LINK%');
```
8. 不同服务器之间的数据库用户建立`dblink`
```sql
create database link remote_scott
connect to scott
identified by scott
using '(
     description = (address_list = ( address = (protocol = tcp ) (host = 192.168.29.229)(port = 1521)))
     (connect_data = (service_name = orcl))
)'
```
```sql
create public database link PRE_DB_LINK
  connect to PRE_DB
  using '192.168.2.221:1522/orcl';
```

>数据库参数`global_name=true`时要求数据库链接名称跟远端数据库名称一样 
> `dblink`查询的时候，均会与远程数据库创建一个连接，`dblink`应该不会自动释放这个连接 ，如果是大量使用`dblink`查询，会造成`web `项目的连接数不够，导致系统无法正常运行。 






# 获取对象的定义语句`(DDL)`
```
select dbms_metadata.get_ddl('TABLE','T1') from dual;
```


# 查看表空间及其大小
```sql
select tablespace_name ,sum(bytes) / 1024 / 1024 as MB　from dba_data_files group by tablespace_name;

```


# `oracle`的优化器

# `RBO`优化器
`RBO`是一种基于规则的优化器,在最新的`10g`版本中`Oracle`已经彻底废除了`RBO`
# `CBO`优化器
`CBO`是基于成本的优化器，可根据可用的访问路径、对象的统计信息、嵌入的`Hints`来选择一个成本最低的执行计划


# `sql`优化



# `Oracle`数据备份
`oracle`数据备份的方式有：数据泵导出备份、热备份、冷备份
数据泵导出备份属于**逻辑备份**
热备份、冷备份属于**物理备份**




# `Oracle`卸载
1. 停掉所有的`oracle`服务
2. 

```
http://blog.csdn.net/machinecat0898/article/details/7792471
```

