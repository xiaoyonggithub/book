

# 二十、常见问题

## 20.1.`linux `命令终端提示符显示`-bash-4.2# `

原因：因为家目录下缺失了`.bash_profile `和`.bashrc  `文件

解决方式：从主默认文件重新拷贝一份配置信息到对应加目录（`/root`）下

```shell
cp /etc/skel/.bashrc /root/
cp /etc/skel/.bash_profile  /root/
--注销用户，重新登录即可
```

## 20.2.虚拟机无法进入 `Unity `模式,原因是: `- Linux` 客户机中不支持 `Unity`

```shell
yum install gcc
yum install kernel-headers
```

## 20.3.`xiaoyong`不在`sudoers`文件中。此事将被报告。

当用户执行sudo时，系统会主动寻找/etc/sudoers文件，判断该用户是否有执行sudo的权限

- 确认用户具有可执行sudo的权限后，让用户输入用户自己的密码确认
- 若没有权限就会报该异常

```shell
sudo mkdir java 

```





# 二十一、`JavaEE`的环境安装

## 21.1.安装`jdk`

1. 解压`jdk`的安装包

   ```shell
   #下载
   wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u181-b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-linux-x64.tar.gz
   #解压
   tar -zxvf jdk-8u181-linux-x64.tar.gz
   #安装
   rpm -ivh jdk-8u181-linux-x64.rpm
   
   ```

2. 配置环境变量`(vim /etc/profile )`

   ```shell
   JAVA_HOME=/opt/
   PATH=/opt/:$PATH
   export JAVA_HOME PATH
   #配置完成后，需注销用户才能生效，（logout）
   ```

## 21.2.安装`weblogic`





## 21.3.安装`tomcat `

1. 解压`tomcat`的安装包

   ```shell
   #解压
   tar -zxvf apache-tomcat-7.0.94.tar.gz -C /usr/local/java
   #查看是否启动
   ps -ef | grep tomcat
   #开放端口
   sudo firewall-cmd --zone=public --add-port=8080/tcp --permanent
   #重启服务
   sudo firewall-cmd --reload
   #查看端口是否开放
   sudo firewall-cmd --zone=public --query-port=8080/tcp
   ```

2. 配置完成后需重启防火墙，才能生效

   - 直接修改` /etc/sysconfig/iptables`文件
   
   ```shell
   sudo vim /etc/sysconfig/iptables-config 
   #添加内容
   -A RH-Firewall-1-INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT
   #保存
   /etc/rc.d/init.d/iptables save
   #重启服务
   /etc/init.d/iptables restart
   #配置完成后需重启防火墙，才能生效
   ```
   
   - 命令行开发端口
   
   ```shell
   #防火墙开放端口
   iptables -I INPUT -p tcp --dport 8080 -j ACCEPT
   #保存
   /etc/rc.d/init.d/iptables save
   #重启服务
   /etc/init.d/iptables restart
   #查看端口是否开放
   /sbin/iptables -L -n
   ```
   
3. 启动方式

   ```shell
   ./eclipse
   快捷方式
   ```

## 21.4.安装`mysql`(源码版)

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

## 21.5.安装`mysql`



## 21.6.安装`oracle`



## 21.7.安装`docker`



## 21.8.安装`nginx`

# 





# 二十二、软件安装

## 22.1.安装`ifconfig`

```shell
#安装
yum install net-tools

```

## 22.2.安装图形界面

```shell
#安装X(X Window System)
yum groupinstall "X Window System"
#查看已经安装的软件和可以安装的软件
yum grouplist
#安装图形化界面软件
yum groupinstall "GNOME Desktop" "Graphical Administration Tools"
#更新系统的默认运行级别
ln -sf /lib/systemd/system/runlevel5.target /etc/systemd/system/default.target
#
rm /etc/systemd/system/default.target
#启动X Window System
startx
```

```shell
#卸载(X Window System)
yum groupremove "X Window System"
#
```



安装`GNOME`异常

```shell
CentOS安装GNOME时，fwupdate-efi-12-5.el7.centos.x86_64 conflicts with grub2-common-1:2.02-0.65.el7.centos.noarch
```

原因：`grub2-common`包的冲突

解决方式

```shell
yum update grub2-common
yum install fwupdate-efi
```

