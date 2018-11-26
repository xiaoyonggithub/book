### 一、运行原理框架分分层架构





























































`removeFirstPrepend`

嵌套查询语句的查询，还有一种是嵌套结果集的查询。



会话（session）级别的缓存，为一级缓存

![../images20141121213425390.jpg](E:\typora\images\20141121213425390.jpg)

![](E:\typora\images\20141120100824184.jpg)

![](E:\typora\images\20141119164906640.jpg)



#### 2、一级缓存的生命周期有多长

**a. MyBatis**在开启一个数据库会话时，会 创建一个新的**SqlSession**对象，**SqlSession**对象中会有一个新的**Executor**对象，**Executor**对象中持有一个新的**PerpetualCache**对象；当会话结束时，**SqlSession**对象及其内部的**Executor**对象还有**PerpetualCache**对象也一并释放掉。

**b.** 如果**SqlSession**调用了**close()**方法，会释放掉一级缓存**PerpetualCache**对象，一级缓存将不可用；

**c.** 如果**SqlSession**调用了**clearCache()**，会清空**PerpetualCache**对象中的数据，但是该对象仍可使用；

**d.****SqlSession**中执行了任何一个**update**操作(**update()、delete()、insert()**) ，都会清空**PerpetualCache**对象的数据，但是该对象可以继续使用；

3、**SqlSession 一级缓存的工作流程** 

1. 对于某个查询，根据**statementId,params,rowBounds**来构建一个**key**值，根据这个**key**值去缓存**Cache**中取出对应的**key**值存储的缓存结果；
2. 判断从**Cache**中根据特定的**key**值取的数据数据是否为空，即是否命中；
3. 如果命中，则直接将缓存结果返回；
4. 如果没命中：

​        4.1  去数据库中查询数据，得到查询结果；

​        4.2  将key和查询到的结果分别作为**key**,**value**对存储到**Cache**中；

​        4.3. 将查询结果返回；

1. 结束。

![](E:\typora\images\20141120133247125.jpg)

一级缓存只会涉及到这一个**PerpetualCache**子类 



**MyBatis**认为，对于两次查询，如果以下条件都完全一样，那么就认为它们是完全相同的两次查询： 	

1. 传入的 statementId
2. 查询时要求的结果集中的结果范围 （结果的范围通过rowBounds.offset和rowBounds.limit表示）；
3. 这次查询所产生的最终要传递给JDBC java.sql.Preparedstatement的Sql语句字符串（boundSql.getSql() ）
4. 传递给java.sql.Statement要设置的参数值





**MyBatis**自身提供的分页功能是通过**RowBounds**来实现的，它通过**rowBounds.offset和**rowBounds.limit来过滤查询出来的结果集，这种分页功能是基于查询结果的再过滤，而不是进行数据库的物理分页； 





JDBC认为两次查询完全一样需满足：

- 传入JDBC的SQL语句完全一样
- 传入的参数完全一样

MyBatis认为两次查询完全一致需满足：

- 





| SqlSession          | **一次数据库会话**                  |
| ------------------- | ----------------------------------- |
| **Executor**        | 执行器 , 负责完成对数据库的各种操作 |
| **Cache**           |                                     |
| **PerpetualCache**  |                                     |
| **CachingExecutor** |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |
|                     |                                     |

