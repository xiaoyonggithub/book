#  一、`MongoDB（3.2）`

- MongoDB的数据模型是面向文档的，其文档是一种类似与json的结构（bson）

## 1.1.三个概念

- 数据库（database）
  - 数据库是一个仓库，在仓库中存放集合
- 集合（collection）
  - 集合类似于数组，在集合中存放文档
- 文档（document）
  - 文档数据库的最小单位，我们存储和操作的内容是文档
  - 数据库和集合都不需要手动创建，若文档所在的集合和数据库不存在，会自动创建

## 1.2.版本

- MongoDB的偶数版本为稳定版，奇数版本为开发版
- MongoDB对32为系统支持不佳，故3.2版本后不再对32位系统支持

## 1.3.安装`MongoDB`

1. 配置环境变量

```
D:\Develope\MongoDB\Server\3.2\bin
```

2. 创建仓库目录`data\db`，默认端口是27017

```shell
#设置仓库的目录和端口
mongod --dbpath F:\MongoDB\db --port 123
```

3. 启动mongodb

```shell
mongod
#32位系统的第一次启动
mongod --storageEngine=mmapv1
```

> 注意：服务器端启动后不能选中，若选中后会是服务器挂起

4. 连接mongodb

```shell
mongo
```

5. 将MongoDB设置为系统服务，可以自动后台启动

6. 创建db和log文件夹
7. 创建配置文件`mongod.cfg`，在`D:\Develope\MongoDB\Server\3.2`此目录下

```shell
systemLog:
    destination: file
    path: c:\data\log\mongod.log
storage:
    dbPath: c:\data\db
```
- 安装服务

```shell
"D:\Develope\MongoDB\Server\3.2\bin\mongod.exe" --config "D:\Develope\MongoDB\Server\3.2\mongod.cfg" --install
```

- 创建服务

```shell
sc.exe create MongoDB binPath= "\"D:\Develope\MongoDB\Server\3.2\bin\mongod.exe\" --service --config=\"D:\Develope\MongoDB\Server\3.2\mongod.cfg\"" DisplayName= "MongoDB" start= "auto"
```

- 启动服务

```shell
net start MongoDB
```

- 停止服务

```shell
net stop MongoDB
```

- 删除服务

```shell
#删除安装的服务
"D:\Develope\MongoDB\Server\3.2\bin\mongod.exe" --remove
#删除创建的服务
sc.exe delete MongoDB
```

---

## 1.4.安装客户端



`F6`执行当前行代码

---

# 二、操作指令

## 2.1.基本指令

- 查看当前所有的数据库

```shell
show dbs
show databases
```

- 查看当前的数据库，db表示的就是当前数据库

```shell
db
```

- 进入指定的数据库

```shell
use dbname
```

- 查看数据库所有的集合

```shell
show collections
```

## 2.2.CRUD指令

### 2.2.1.insert

- 向集合插入一个或多个文档（若插入时没有指定`_id`，数据库会自动为文档添加`_id`作为文档的唯一标识；若指定的`_id`，`_id`也不能重复）

```sql
db.<collection>.insert(doc);
```
```sql
db.stus.insert({name:"孙悟空",age:19,gender:"男"});
db.stus.insert([{name:"猪八戒",age:30,gender:"男"},{name:"嫦娥",age:18,gender:"女"}]);
```

- 插入一个文档

```sql
db.<collection>.insertOne(doc);
```

- 插入多个文档

```sql
db.<collection>.insertMany(doc);
```

- 生成随机id

```sql
ObjectId();  //ObjectId("5bfead1df1783356d8a9547f")
```



### 2.2.2.find

- 查询当前集合中所有的文档

```sql
--返回数组
db.<collection>.find();
```

```sql
db.stus.find({name:"孙悟空"});

```

```sql
{ "_id" : ObjectId("5bfea6b5f1783356d8a9547e"), "name" : "嫦娥", "age" : 18, "gender" : "女" }
```

- 查询符合条件中集合的第一个

```sql
--返回对象
db.<collection>.findOne();
```

- 统计数量

```sql
db.stus.count();
db.stus.find().count();
db.stus.find().length(); //db.stus.length();错误
```

- 查看函数的定义

```sql
db.stus.count;
db.stus.find;
```

- 查询属性的属性，可使用`.`

```sql
db.user.find({"hobby.movies":"hero"}); //此时属性必须加""
```

> `username:张三`不是表示username=张三，是username中包含"张三"

- 

### 2.2.3.update

- 替换文档

```sql
--默认只会修改一个
db.<collection>.update(查询条件，新对象);
```

```sql
--替换，此时默认会使用新对象替换就对象
db.stus.update({name:"孙悟空"},{age:100});
```

- 修改指定的属性

```sql
db.stus.update({name:"猪八戒"},{$set:{age:100}});
```

- 删除文档指定属性

```sql
db.stus.update({name:"猪八戒"},{$unset:{age:100}});//此时删除属性的值可任意指定
```

- 修改多个文档

```sql
db.<collection>.updateMany();
```

- 向数组添加元素

```sql
db.user.update({username:"tangseng"},{
    $push:{"hobby.movies":"Interstellar"}
});
```



### 2.2.4.delete

- 删除一个或多个

```sql
db.<collection>.remove({
    justOne:true  -- 是否只删除一个，默认删除所有匹配的
    });
db.<collection>.remove({}); //清空集合，此时建议使用删除集合
```

- 删除一个

```sql
db.<collection>.deleteOne();
```

- 删除多个

```sql
db.<collection>.deleteMany();
```

- 删除集合

```sql
db.<collection>.drop();
```

- 删除数据库

```sql
db.dropDatabase();
```

## 2.3.操作符

- 查询操作符
  - `$gt`大于
  - `$eq`等于
  - `$lt`小于
  - `$gte`大于等于
  - `$lte`小于等于

- 关系操作符

  - `$or`或关系

- 修改操作符

  - `$set`用于修改文档指定的属性

  - `$unset`删除文档指定属性

  - `$push`向数组中添加新的元素

  - `$addToSet`向数组添加一个新元素，若数组中已存在该元素，则不会添加

  - `$inc`自增值，即`+=`


# 三、函数

## 3.1.查询函数

- `limit()`限制查询的条数
- `skip()`跳过指定数量的数据

```sql
--查询前十条数据
db.numbers.find().limit(10);
--查询11-20条数据
db.numbers.find().skip(10).limit(10);  //skip()与limit()的顺序可调整
```

> skip((页码-1)*每页显示的条数).limit(每页显示的条数);

- `find()`返回的是数组

  - 查询是注意数据格式，如数据格式为number，find({id:'32362'})就查不到数据
  - 查询文档是默认按`_id`的升序排列
  - 投影:指定显示的字段；`1`显示，`0`不显示

  ```sql
  db.emp.find({},{ename:1,_id:0});
  ```

- `sort()`设置排序规则

  - `1`升序；`-1`降序

```sql]
db.emp.find({sal:1,empno:-1});
```

> limit()、skip()、sort()三个函数可任意顺序



# 四、映射关系

## 4.1.文档之间的关系

- 一对一
- 一对多
- 多对多





# 十、Java操作MongoDB

