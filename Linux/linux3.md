

## 二十、常见问题

### 20.1.`linux `命令终端提示符显示`-bash-4.2# `

原因：因为家目录下缺失了`.bash_profile `和`.bashrc  `文件

解决方式：从主默认文件重新拷贝一份配置信息到对应加目录（`/root`）下

```shell
cp /etc/skel/.bashrc /root/
cp /etc/skel/.bash_profile  /root/
--注销用户，重新登录即可
```

### 20.2.虚拟机无法进入 `Unity `模式,原因是: `- Linux` 客户机中不支持 `Unity`

```shell
yum install gcc
yum install kernel-headers
```



## 二十一、`JavaEE`的环境安装

### 21.1.安装`jdk`

1. 解压`jdk`的安装包

   ```shell
   #下载
   wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u181-b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-linux-x64.tar.gz
   #解压
   tar -zxvf 
   ```

2. 配置环境变量`(vim /etc/profile )`

   ```shell
   JAVA_HOME=/opt/
   PATH=/opt/:$PATH
   export JAVA_HOME PATH
   #配置完成后，需注销用户才能生效，（logout）
   ```

### 21.2.安装`tomcat`

1. 解压`tomcat`的安装包

   ```shell
   tar -zxvf 
   ```

2. 配置环境变量`(vim /etc/profile )`

   ```shell
   JAVA_HOME=/opt/
   PATH=/opt/:$PATH
   export JAVA_HOME PATH
   #配置完成后，需注销用户才能生效，（logout）
   ```

3. 开放端口，即让防火墙放行

   ```shell
   vim /etc/sysconfig/iptables
   ```

   ```shell
   #配置完成后需重启防火墙，才能生效
   ```

### 21.3.安装`eclipse`

1. 解压`tomcat`的安装包

   ```shell
   tar -zxvf 
   ```

2. 启动方式

   ```shell
   ./eclipse
   快捷方式
   ```

### 21.4.安装`mysql`(源码版)

1. 卸载旧版本

```shell
rpm -qa | grep mysql
rpm -e -nodeps mysql
```

1. 安装编译软件需要的环境

```shell
yum -y install make gcc-c++ cmake bison-devel ncurses-devel
```

1. 安装 `mysql`

```shell
tar -zxvf mysql
#编译
cmake -DCMAKE_INSTALL_PREFIX=/usr/local/mysql -DMYSQL_DATADIR=/usr/local/mysql/data -DSYSCONFDIR=/etc -DWITH_MYISAM_STORAGE_ENGINE=1 -DWITH_INNOBASE_STORAGE_ENGINE=1 -DWITH_MEMORY_STORAGE_ENGINE=1 -DWITH_READLINE=1 -DMYSQL_UNIX_ADDR=/var/lib/mysql/mysql.sock -DMYSQL_TCP_PORT=3306 -DENABLED_LOCAL_INFILE=1 -DWITH_PARTITION_STORAGE_ENGINE=1 -DEXTRA_CHARSETS=all -DDEFAULT_CHARSET=utf8 -DDEFAULT_COLLATION=utf8_general_ci
#编译安装
make && make install
```

1. 配置`mysql`

```shell
#查看是否有mysql用户及用户组
cat /etc/passwd   #查看用户列表
cat /etc/group    #查看用户组列表

#没有就创建
groupadd mysql                 #创建mysql组
useradd -g mysql mysql         #创建mysql用户

#修改/usr/local/mysql权限，软件默认安装在/usr/local目录下  
chown -R mysql:mysql /usr/local/mysql   #修改mysql的所有者和所在组

#初始化配置，在/usr/local/mysql目录下执行
cd /usr/local/mysql
scripts/mysql_install_db --basedir=/usr/local/mysql --datadir=/usr/local/mysql/data --user=mysql

#mysql启动时，先查找【/etc/my.cnf】,若找不到则找【$basedir/my.cnf】（即软件安装目录下），此处就是/usr/local/mysql/my.cnf
#centos在安装的时候可能生成一个【/etc/my.cnf】，需将重名为【/etc/my.cnf.bak】，否则会干扰不读取安装的mysql中的my.cnf，导致配置信息错误而无法启动
mv /etc/my.cnf /etc/my.cnf.bak

#启动mysql，注意在【/usr/local/mysql】下执行
cp support-files/mysql.server /etc/init.d/mysql  #添加服务，拷贝服务脚本到init.d目录
chkconfig mysql on    #设置自启动
service mysql start   #启动MySQL

#查询3306端口是否监听
netstat -anp | more 

#设置mysql密码,默认密码为空
cd /usr/local/mysql/bin          #进入bin目录
./mysql -uroot                   #登录
set password = password('root')  #设置密码

#配置环境变量
JAVA_HOME=/opt/
PATH=/opt/:/usr/local/mysql/bin:$PATH   #添加mysql的环境变量
export JAVA_HOME PATH
```

