# 一、`Mongoose`

- `Mongoose`是`node.js`中操作`MongoDB`的模块
- `Mongoose`是一个对象文档库`(ODM)`，将`MongoDB`中的文档为了对象

## 1.1.好处

- 为文档创建了模式结构`(Schema)`
- 可以对模型中的对象/模型进行验证

## 1.2.对象

- `Schema`(模式对象)
  - 定义约束数据库中的文档对象
- `Model`
  - 是集合中所有对象的表示，相当于`MongoDB`中的`colletion`
- `Document`
  - 表示集合中的具体文档，相当于集合中的一个文档
  - `Docment`是Model的实例

## 1.3.安装

1. 下载安装`Mongoose`

```sql
npm i mongoose --save
```

2. 在项目中引入`Mongoose`

```js
var mongoose = require("mongoose");
```

3. 连接数据库

   `MongoDB`一般连接一次就不会断开

```js
mongoose.connect('mongodb://localhost:27017/test',{useMongoClient:true});
//若为默认端口27017，可以不写
```

4. 监听`MongoDB`数据库的状态

   在mongoose对象中有一个属性`connection`(表示数据库的连接)

```js
//数据库连接成功的事件
mongoose.connection.once("open",function(){
    
});
//数据库连接失败的事件
mongoose.connection.once("close",function(){
    
});
```

5. 断开数据库

```js
mongoose.disconnect();
```

## 1.4.创建`Model`

1. 创建`Schema`
2. 创建`Model`
3. 创建`Docment`



# 二、方法

## 2.1.`Model`的方法

- `Model.create(doc(s),callback)`创建一个或多个文档添加到数据库

```js

```

- `Model.find(condition,projection,options,callbcak)`查询所有符合条件的文档
  - `projection`投影，设置显示的字段
    - `{name:1,_id:0}`
    - `name,-_id`
  - `options`查询选项（skip、limit）
  - `callback`查询结果通过回调函数返回，所以必传
  - 返回数组

```js
stuModel.find({name:"张三"},{name:1,_id:0},{skip:3,limit:1}function(err,docs){
    if(!err){
		console.log(docs);
        var name = docs[0].name;
    }
});
```

```js
stuModel.find({name:"张三"},{name,age,-_id},function(err,docs){
    if(!err){
		console.log(docs);
        var name = docs[0].name;
    }
});
```

- `Model.findOne(condition,projection,oprions,callbak)`查询符合条件的第一个文档

  - 返回一个具体的文档对象

- `Model.findById(id,projection,options,callback)`根据文档的id属性查询

```js
stuModel.find("739283892138193",function(err,doc){
    
});
```

- `Model.update(condition,doc,opetion,callbak)`修改所有匹配的文档

```js

```

- `Model.replace()`替换文档
- `Model.remove(conditions,callback)`删除所有匹配的文档
- `Model.count(conditions.callback)`统计文档的数量

## 2.2.`Document`的方法

Document与数据库中得到文档一一对应，Document是Model的实例

