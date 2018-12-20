# 一、大数据的概念

- Hadoop是一个由Apache基金会开发的分布式系统基础框架
- 主要解决，海量数据的存储和海量数据的分析计算问题

## 1.1.大数据的特点

- 大量性(Volume)
- 高速性(Velocity)
- 多样性(Variety)
- 低价值密度(Value):价值密度的高低与数据的总量成反比

>  大小单位:bit、byte、KB、MB、GB、TB、PB、EB、ZB、YB、BB、ND、DB、

## 1.2.大数据的生态体系

- 数据传递(Sqoop)--数据库(结构化数据)

- 日志搜集(Flume)--文件日志(半结构化数据)

- 消息队列(Kafka)--实时数据


![1540818132245](E:\typora\images\test.pdf)

## 1.3.Hadoop的三大发行版本

- Apache 版本最原始（最基础）的版本，对于入门学习最好

- Cloudera 在大型互联网企业中用的较多
- Hortonworks 文档较好。

## 1.4.Hadoop的优势  

- 高可靠性:因为Hadoop假设计算元素和存储会出现故障，因为它维护多个工作数据副本，在出现故障时可以对失败的节点重新分布处理。 
- 高扩展性:在集群间分配任务数据，可方便的扩展数以千计的节点
- 高效性:在MapReduce的思想下，Hadoop是并行工作的，以加快任务处理速度。
- 高容错性:自动保存多份副本数据，并且能够自动将失败的任务重新分配。

## 1.5.Hadoop组成

1) Hadoop HDFS：一个高可靠、高吞吐量的分布式文件系统。
2) Hadoop MapReduce：一个分布式的离线并行计算框架。
3) Hadoop YARN：作业调度与集群资源管理的框架。
4) Hadoop Common：支持其他模块的工具模块（Configuration、RPC、序列化机制、日志操作）

## 1.6.Hadoop1.0与Hadoop2.0的区别

- 组成不同
  - MapReduce(计算+资源调度)
  - HDFS()



## 1.7.HDFS的组成

- NameNode(nn):
- DataNode(dn):
- Secondary NameNode(snn):



## 1.8.YARN架构

- ReosurceManager(RM):整个资源的老大
  - 处理客户端请求
  - 监控NodeManager
- NodeManager(NM):单个节点的老大
  - 管理单个节点上的资源
  - 处理来自ResourceManager的命令
- AppliationMaster(AM)



- Container

## 1.9.MapRedure架构

MapRedure将计算分为



## 1.10.推荐系统框架图





## 1.11.`Hadoop`的子项目

- `HDFS`：Hadoop分布式文件系统
- `Map/Reduce`
- `Zookeeper`
- `Pig`
- `Hive`
- `Hbase`



# 二、环境搭建

## 2.1.虚拟机环境准备

1. 修改IP地址

```shell
vim /etc/udev/rules.d/70-snap.core.rules  #查看物理ip地址
vim /etc/sysconfig/network-scripts/igcfg-eth0 #修改物理ip地址
vim /etc/sysconfig/network  #修改主机名称
vim /etc/hosts #配置ip与主机名的映射关系
```

2. 关闭防火墙

3. 配置用户具有root权限

```shell
vim /etc/sudoers
```

4. 创建目录

```shell
sudo mkdir /opt/module  #放置安装程序
sudo mkdir /opt/software #放置软件包
sudo chown xiaoyong:xiaoyong module/ software/ #修改所有者和所有组
```

5. 安装jdk

```shell
tar -zxvf jdk.tar.gz -C /opt/module/
pwd #查看当前路径
#配置环境变量
export JAVA_HOME=/opt/module/jdk
export PATH=$PATH:$JAVA_HOEM/bin
source /etc/profile
java -version
```

6. 安装hadoop

```shell
tar -zxvf hadoop -C /opt/module
pwd
#配置环境变量
export HADOOP_HOME=/opt/module/hadoop
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
source /etc/profile
hadoop
```

## 2.2.Hadoop目录结构

- `bin` 命令

- `etc` 配置文件

- `lib` 本地库

- `share` 文档和案例


# 三、Hadoop的运行模式

Hadoop的运行模式

- 本地模式
- 伪分布式模式
- 完全分布式模式

## 3.1.本地模式

```shell
cp etc/hadoop/*.xml input/
bin/hadoop jar share/hadoop/mapreduce/hadoop-  grep input/ output 


```



`jps` java的查看进程，需要安装jdk



