# 一、zookeeper的安装

## 1.1.安装jdk环境

1. 卸载原来的jdk 

   ```shell
   rpm -qa | grep java  #查看安装的所有java
   rpm -e -nodeps jdk   #卸载rpma包
   ```

2. 解压`jdk`的安装包

   ```shell
   #下载
   wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u181-b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-linux-x64.tar.gz
   #解压
   tar -zxvf 
   ```

3. 配置环境变量`(vim /etc/profile )` Go跳转到最后一行

   ```shell
   export JAVA_HOME=/usr/jdk1.8.0_181
   export PATH=$PATH:$JAVA_HOME/bin 
   export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tool.jar
   ```

   ```shell
   source /etc/profile  #重新加载文件或者注销用户（logout）
   ```

## 1.2.检查环境

1. 查看集群时间是否同步

```shell
date  #同时向各个机器发送命令
date -s  #设置时间同步
```

2. 关闭防火墙

```shell
service iptables stop 
chkconfig iptables off
chkconfig iptables --list 
```

3. 配置和检查主机和ip映射

```shell
 
```

## 1.3.安装zookeeper

1.下载zookeeper

```shell

```

2. 解压zookeeper
3. 复制配置文件zoo_sample.cfg，重命名为zoo.cfg，因为默认找的配置文件时zoo.cfg

```shell
cp zoo_sample.cfg zoo.cfg
```

4. 修改配置文件的内容

```shell
vim zoo.cfg
```

```shell
dataDir=/app/zookeeper/zkdata
server.1=node-1:2888:3888  #心跳端口 选举端口
server.2=node-2:2888:3888
server.3=node-3:2888:3888
```

5. 创建myid

```shell
mkdir zkdata
vim myid   #n内容为1
scp -r /app/zookeeper/ root@node-2:/app/   #发送文件夹到其他的机器上
echo 2 > zkdata myid
echo 3 > zkdata myid
```

6. 配置zookeeper的环境变量

```shell
vim /etc/profile
export ZOOKEEPER_HOME=\app\zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin
source /etc/profile
```

7.启动zookeeper

```shell
zKServer.sh start   #启动
zKServer.sh status  #查看状态
```

# 二、配置文件





# 三、zookeeper的数据模型

zookeeper树中每个节点被称为一个Znode，

## 3.1.znode的特点

* znode兼具文件和目录两种特点
* znode具有原子性
* znode存储的数据大小有限制，通常以KB为大小单位
* znode通过路径进行引用，路径必须是绝对的路径

## 3.2.Znode的组成：

* stat: 状态信息，描述Znode的版本和权限等信息
* data: 与该znode关联的数据
* children: 该znode下的子节点

## 3.3.节点的类型

节点的类型在创建的时候就被确定，并且不能被改变。

* 临时节点：该节点的生命周期依赖于创建它们的会话。一旦会话结束，临时节点将被自动删除，当然也可以手

  动删除。临时节点不允许有子节点

* 永久节点：该节点的生命周期不依赖与会话，并且只有在客户端显示执行删除操作时，才能被删除

znode的序列化特性，若在创建的时候指定，该znode的名字后面会自动追加一个不断递增的序列号，这样就可以

记录节点创建的先后顺序



## 3.4.节点的属性

```
get  	
```

* `dataVersion`：数据版本号，每次对节点进行set操作，dataVersion都会增加1，可有效的避免数据更新时 的先后问题

* `cversion`：子节点的版本号，当znode的字节点变化时，cverison会增加

* `aclVsersion`:ACL的版本号

* `cZxid`:Znode创建的事务id

* `mZxid`:Znode被修改的事务id，即每次修改多会更新mZxid

* `ctime`:节点创建的时间

* `mtime`:节点最新一次更新的时间

* `ephemeralOwner`:若该节点为临时节点， ephemeralOwner（临时节点的拥有者）的值表示与该节点绑定的sessionid，若不是，ephemeralOwner的值为0。

  在client与server通信之前，首先需要建立连接，该连接成为session

## 3.5.Zookeeper shell

```shell
zkCli.sh -server ip
jps 
```

## 3.6.shell的基本操作

## 3.6.1.创建节点

```shell
create [-s] [-e] path data acl
[-s] 序列化节点
[-e] 临时节点
acl 进行权限控制
```



