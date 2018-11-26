### 一、`linux`安装`mysql`

1. 检查是否安装了`linux`

```shell
rpm -qa | grep -i mysql 
```

2. 安装服务端

```shell
rpm -ivh mysql-community-server-8.0.11-1.el7.x86_64.rpm 
yum install mysql-community-server-8.0.11-1.el7.x86_64.rpm 	
```

```shell
#下载mysql7.x的repo源
wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
#安装mysql7.x的repo源
rpm -ivh mysql-community-release-el7-5.noarch.rpm 
#查看一下是否已经有mysql可安装文件
yum repolist all | grep mysql
#安装mysql
yum install mysql-community-server
```

3. 验证`mysql`是否安装成功

```shell
ps -ef | grep mysql
```

```shell
#查看是否存在mysql的用户
cat /etc/passwd | grep mysql
#查看是否存在mysql的组
cat /etc/group | grep mysql
#查看mysql的版本
mysqladmin --version
```

```shell
#mysql用户
mysql:x:27:27:MySQL Server:/var/lib/mysql:/bin/false
#mysql组
mysql:x:27:
#mysql的版本
mysqladmin  Ver 8.0.11 for Linux on x86_64 (MySQL Community Server - GPL)

```

4. 启动/停止`mysql`服务

```shell
#启动mysql服务
systemctl start mysqld.service
#查看mysql进程
ps -ef | grep mysql
#停止mysql
systemctl stop mysqld.servcie
#设置mysql服务开机自启动
systemctl enable mysqld.service
systemctl list-unit-files | grep mysql #查看自启动状态

chkconfig mysql on
chkconfig --list | grep mysql
#设置后台加载
systemctl daemon-reload
```

5. 配置`mysql8`的默认密码（[`mysql8设置默密码`](https://blog.csdn.net/qq_16075483/article/details/80296611)）

```shell
#mysql8
#把密码的加密方式改成之前版本的,8.0版本更换了密码的加密方式，我们就先用旧的
vim /etc/my.cnf
#重启服务
systemctl restart mysqld
#mysql安装完成之后，在/var/log/mysqld.log文件中给root生成了一个默认密码。
#通过下面的方式找到root默认密码，然后登录mysql进行修改：
grep 'temporary password' /var/log/mysqld.log   #qdTVs4oRX8_f
#登录用户
mysql -uroot -p
#登录后不允许进行任何操作,干啥都会报错,说你要修改密码才能用
#修改密码
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '你的密码';
```

![](E:\typora\images\20180521105946344.png)

```shell
#如果修改密码像这个样子提示错误,说明密码的复杂度还不够.
```

![](E:\typora\images\Snipaste_2018-06-03_22-48-24.png)

```shell
#如果不想输入太复杂的密码,可以通过下面的方式降低密码复杂度,这样密码就可以随便设置了

#修改密码强度的方法好像跟之前版本也不一样,我们用下面两条命令修改密码强度限制
#设置密码强度级别,三个数字分别对应低,中,高三个级别.我们设置成"低"也就是0,其他的数字忘了
set global validate_password.policy=0;

#这个是设置密码长度的,不能低于4位,根据需要自己设置长度.
set global validate_password.length=4;

#这个是修改密码的命令,给root用户本地登录设置密码,"xiaoyong"是我设置的密码
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'xiaoyong';
#正确结果应该是这样的,然后quit退出去,使用新密码登陆试试.有问题别找我,解决不了.
```

6. 查看`mysql`的配置文件`my.cnf`

```shell
mysql --help | grep my.cnf
find / -name my.cnf
```

7. `mysql`默认的日志文件

```shell
/var/log/mysqld.log 
```

8. 添加远程登录

```shell
#一般不允许root用户进行远程登录,我先记录一下命令,这个是root远程登录授权的命令
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%'IDENTIFIED BY '密码' WITH GRANT OPTION;
#然后我们去给一个非root用户授权.也就是说root用户可以本地登录,远程登录就用这个用户
#先创建用户,然后再授权(这里貌似也是个坑,之前版本是可以直接用上面这条命令的,系统会帮你创建用户.但是现在需要用下面这这两条命令)

#创建用户(user1:用户名;%:任意ip,也可以指定，root默认就是localhost;123456：登录密码)
CREATE USER 'liuwei'@'%' IDENTIFIED BY 'liuwei';

#授权，默认创建的用户权限是usage,就是无权限，只能登录而已
#(all：所有权限，这里有select,update等等权限，可以去搜一下；后面的*.*：指定数据库.指定表，这里是所有；to后面就是你刚才创建的用户)
grant all on *.* to 'liuwei'@'%';

#注意:用以上命令授权的用户不能给其它用户授权,如果想让该用户可以授权,用以下命令: 
GRANT all ON databasename.tablename TO 'username'@'host' WITH GRANT OPTION;
```

9. `mysql`配置默认密码

```shell
/usr/bin/mysqladmin -u root password root
```

10. `mysql`数据库的位置

```shell
#mysql数据库文件的存放位置
/var/lib/mysql
#配置文件的目录
/usr/share/mysql
#相关命令
/usr/bin
#启停相关脚本,mysql8没有
/etc/init.d/mysql
```

11. 备份配置文件

```shell
cp /usr/share/mysql/my-huge.cnf /etc/my.cnf    #5.5备份配置文件
cp /usr/share/mysql/my-default.cnf /etc/my.cnf #5.6备份配置文件
cp /etc/my.cnf /etc/my.cnf.bak                 #8.0备份配置文件
```

12. 修改字符集

```shell
show variables like '%char%';
set 
```

```shell
mysql> show variables like '%char%';
+--------------------------------------+--------------------------------+
| Variable_name                        | Value                          |
+--------------------------------------+--------------------------------+
| character_set_client                 | utf8mb4                        |
| character_set_connection             | utf8mb4                        |
| character_set_database               | utf8mb4                        |
| character_set_filesystem             | binary                         |
| character_set_results                | utf8mb4                        |
| character_set_server                 | utf8mb4                        |
| character_set_system                 | utf8                           |
| character_sets_dir                   | /usr/share/mysql-8.0/charsets/ |
| validate_password.special_char_count | 1                              |
+--------------------------------------+--------------------------------+
9 rows in set (0.01 sec)
```

| 参数名称                               | 描述                                   |
| :------------------------------------- | -------------------------------------- |
| `character_set_client`                 | 客户端请求数据的字符集                 |
| `character_set_connection`             | 客户机/服务器连接的字符集              |
| `character_set_database`               | 数据库的字符集                         |
| `character_set_filesystem`             |                                        |
| `character_set_results`                | 结果集，返回给客户端的字符集           |
| `character_set_server`                 | 数据库服务器的默认字符集               |
| `character_set_system`                 | 系统字符集，这个值总是utf8，不需要设置 |
| `character_sets_dir`                   |                                        |
| `validate_password.special_char_count` |                                        |

```
ERROR 1819 (HY000): Your password does not satisfy the current policy requirements
```

`【character_set_system】` 这个字符集用于数据库对象（如表和列）的名字，也用于存储在目录表中的函数的名字 



### 二、密码

```sql
--修改默认的密码
grep 'temporary password' /var/log/mysqld.log
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'MyNewPass4!'; 
```

#### 2.1.`centos 7 `修改`mysql 8.0`的密码

1. 查看自动生成的密码

```shell
#mysql安装完成后会给我们生成一个随机密码
grep 'temporary password' /var/log/mysqld.log
```

2. 配置`mysql`免密码登录

```shell
vim /etc/my.cnf
#在词句【pid-file=/var/run/mysqld/mysqld.pid】下添加
skip-grant-tables
```

3. 重启`mysql`服务

```shell
systemctl mysqld restart
```

4. 免密码登录`mysql`数据库

```shell
mysql -u root -p 
#提示输入密码时，直接回车即可
```

5. 将默认密码置空

```sql
use mysql;  
update user set authentication_string='' where user='root'; 
--mysql5.7在此处直接修改密码
update mysql.user set authentication_string=password('123456') where user='root' ;  
```

6. 查看`mysql`数据库中`user`表的信息

```sql
select host, user, authentication_string, plugin from user; 
```

* 【`host`】:允许用户登录的`ip`,`%`表示可远程
* 【`user`】：当前数据库的用户名
* 【`authentication_string `】：用户密码；在`mysql5.7.9`之后废弃了`password`字段和`password()`函数
* 【`plugin`】：密码的加密方式

7. 删除` /etc/my.cnf `文件最后的 `skip-grant-tables `

```shell
vim /etc/my.cnf
```

8. 重启`mysql`服务

```sql
systemctl mysqld restart
```

9. 登录`mysql`

```sql
mysql -u root -p 
--此时密码为空
```

10. 修改用户密码

```sql
ALTER user 'root'@'localhost' IDENTIFIED BY 'Xpf123@';
--若此时修改密码，异常ERROR 1819 (HY000): Your password does not satisfy the current policy require
```

#### 2.2.异常`RROR 1819 (HY000): Your password does not satisfy the current policy require` 

为加强安全性，`mysql`为`root`用户随机生成了一个密码，在`error log`中

1. 查看`error log`的位置

```sql
--若安装的是rmp包，error log的默认位置是/var/log/mysqld.log
select @@log_error;
```

2. 使用临时密码登录，操作数据库时，提示需要修改密码

```sql
--ERROR 1820 (HY000): You must reset your password using ALTER USER statement before executing this statement.
```

3. 修改为一个简单的密码，则会提示

```sql
ALTER user 'root'@'localhost' IDENTIFIED BY 'Xpf123@';
ALTER USER USER() IDENTIFIED BY '12345678';
--ERROR 1819 (HY000): Your password does not satisfy the current policy requirements
--原因：这与mysql设置的密码策略有关
```

4. 查看是否安装了`validate_password `插件

```sql
--查看密码策略验证的参数
SHOW VARIABLES LIKE 'validate_password%';
```

```sql
+--------------------------------------+-------+
| Variable_name                        | Value |
+--------------------------------------+-------+
| validate_password.check_user_name    | ON    |
| validate_password.dictionary_file    |       |
| validate_password.length             | 2     |
| validate_password.mixed_case_count   | 0     |
| validate_password.number_count       | 0     |
| validate_password.policy             | LOW   |
| validate_password.special_char_count | 0     |
+--------------------------------------+-------+
```

| `validate_password.check_user_name`    |                                |
| -------------------------------------- | :----------------------------: |
| `validate_password.dictionary_file`    | 用于验证密码强度的字典文件路径 |
| `validate_password.length`             |           密码的长度           |
| `validate_password.mixed_case_count`   |     密码中大小写字母的长度     |
| `validate_password.number_count`       |        密码中数字的长度        |
| `validate_password.policy`             |           密码的策略           |
| `validate_password.special_char_count` |      密码中特殊字符的长度      |

密码的策略`validate_password.policy`

| `0/LOW`        | 只检查长度                               |
| -------------- | :--------------------------------------- |
| ` 1/MEDIUM   ` | 检查长度、数字、大小写、特殊字符         |
| ` 2/STRONG   ` | 检查长度、数字、大小写、特殊字符字典文件 |

5. 修改密码验证规则函数

```sql
set global validate_password.policy=0;  
set global validate_password.length=1;  

--mysql5.7
set global validate_password_policy=0;  
set global validate_password_length=1;  
```



### 三、`my.cnf`配置文件
