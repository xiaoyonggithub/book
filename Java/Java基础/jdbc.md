

事务管理

```java
Connection conn = null;
try{
    conn.setAutoCommit(false);//开启事务
    ...
    conn.commit();//提交事务
}catch (SQLException e){
    conn.rollback();//回滚事务
}finally {
    conn.close();//关闭连接
}
```

回滚事务到指定的点

```java
//设置回滚的检查点
Savepoint point = conn.setSavepoint("check-point");
//回滚到指定的点
conn.rollback(point);
```

事务的隔离级别

```java
//获取事务的隔离级别
int isolation = conn.getTransactionIsolation();
//设置事务的隔离级别
conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
```

