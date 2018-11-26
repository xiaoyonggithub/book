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



# 二、环境搭建

## 2.1.虚拟机环境准备



## 2.2.安装JDK



## 2.3.安装Hadoop



## 2.4.Hadoop目录结构



# 三、Hadoop的运行模式



