# 一、`nodejs`

- `nodejs`是一个服务器端的JavaScript运行环境
- `nodejs`采用的是google的V8引擎运行js代码，使用事件驱动、非阻塞、异步`I/O`模型等技术提高性能，优化程序的传输量和规模
  - 事件驱动
  - 非阻塞
  - 异步`I/O`模型
- `nodejs`是单线程
- `nodejs`的版本，奇数版本为开发版，偶数版为稳定版



## 1.1.命令

- 查看nodejs版本

```cmd
node -v
npm -v
```

- `nodejs`执行js文件

```cmd
ndoe view.js
```

## 1.2.`npm`

- `npm`是`nodejs`下的包管理器，帮助其完成了第三方模块的发布、安装和依赖等
- 查看`npm`版本

```js
npm -v
//查看所有模块的版本
npm version
```

- 帮助命令

```js
//查看所有可用命令信息
npm -l
//查看命令使用详情
npm help <term>
```

- 搜索模块包

```js
npm search 包名
npm search math 
```

- 安装包

```js
//创建package.json
npm init
npm init --yes //自动填写默认选项

//在当前目录下安装math包，若不创建package.json有些可能不会安装在当前目录下
npm install math
npm i math //简写

//全局安装包，一般是安装工具
npm install math -g

//删除安装的包
npm remove math
npm r math
//删除包和依赖
npm remove math --save 

//安装包并添加当依赖，此时会在package.json添加依赖配置
npm install 包名 --save

//下载当前项目依赖的包，根据package.json
npm install 

//安装最新版本的npm 
npm install npm@latest -g
```

- 安装淘宝的镜像工具

```cmd
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

![1545835990618](../images/1545835990618.png)

从镜像上下载的包，会加一些版本或前缀，通过快捷方式访问，为了与核心库下载的文件区分开

- 通过npm下载的包都会放在`node_modules`目录下，且通过包名就可直接引入

## 1.3.包

包实际就是一个压缩文件，包规范有包结构和包描述组成

- 包结构：用于组织包中的各种文件
  - `package.json` 描述文件，必须；是一个json格式的文件
    - `name`
    - `description`
    - `version`
    - `keywords`
    - 
  - `bin` 可执行文件，不必须
  - `lib` js代码，不必须
  - `doc` 文档，不必须
  - `test` 单位测试，不必须
- 包描述：描述包的相关信息，以供外部读取分析

> 注意：json文件中不能写注释

### 1.3.1.包的搜索流程

使用模块名字引入包时

- 首先会在当前的node_modules目录下寻找，若有就使用
- 若没有，则再在上一级的node_modules目录下寻找，直到找到为止
- 若找到磁盘根目录都没找到，则报错

---



## 1.4.`Buffer`

- `Buffer`(缓存区)与数组结构类似，且操作也类似
- 数组不能存放二进制文件，而`Buffer`就是专门用来存放二进制数据
- 使用`Buffer`不需要引入模块
- 在`Buffer`中存放的都是二进制数据，但是显示时是以16进制形式显示
- `Buffer`中每一个元素的范围是[00 - ff]，即[0 - 225]，即一个元素占用一个字节
- 一个汉字占用3个字节

```js
var str = '你好';
var buf = Buffer.from(str);//将字符串保存到buffer
buf = Buffer.from(str,'utf-8');//将字符串保存到buffer
console.log(buf);
console.log(buf.length); //字符串占用的字节，6
console.log(str.length); //字符串的长度，2
console.log(buf.toString()); //将缓存区的字符串转化的字符串
```

- `Buffer`大小一旦创建，就不能修改了，因为实际上`Buffer`是大小底层内存直接操作，且是连续的内存地址

```js
//创建指定大小的buffer
var buffer = Buffer.alloc(10);
//操作buffer
buffer[0] = 88;
buffer[1] = 0x16;
//值超出了255时，只取后8位的二进制
buffer[2] = 556;
//此时超出了buffer的大小，不会有任何效果
buffer[10] = 15;
//只要是数字在控制台或页面输出一定是十进制
console.log(buffer[1]);
//显示其它进制，转化为字符串显示
console.log(buffer[1].toString(16));
```

- ` Buffer.allocUnsafe(10)`可能会包含敏感数据

```js
//allocUnsafe()不会清空分配空间中的数据，alloc()会清空分配空间中的数据
buffer = Buffer.alloc(10);//<Buffer 00 00 00 00 00 00 00 00 00 00>
console.log(buffer);
buffer = Buffer.allocUnsafe(10);//<Buffer f8 02 14 33 08 02 00 00 98 02>
console.log(buffer);
```

## 1.5.文件系统

- `fs`是核心模块，不需要下载可直接引入
- `fs`模块的所有操作都有两种形式：同步或异步
  - 同步方法会阻塞程序的执行
  - 异步方法不会阻塞程序的执行
- `fs`的异步方法都有回调函数，而同步方法没有回调函数

### 1.5.1.文件写入

- 简单文件写入，适用于小文件

```js
let fs = require("fs");
fs.writeFile('file.js','写入内容',function (err) {
    if(!err){
        console.log('文件写入成功');
    }
});
```

- 流式文件的读取或写入，适用于大文件

```js
let fs = require("fs");
let rs = fs.createReadStream('(5).png');
let ws = fs.createWriteStream('copy.png');
//读取流打开或关闭事件监听，这两事件都只会执行一次
rs.once('open',function () {

});
rs.once('close',function () {
    //在文件数据读取完成后，关闭读取流端的管道
    ws.end();
});
//写入流打开或关闭事件监听，这两事件都只会执行一次
ws.once('open',function () {

});
ws.once('close',function () {

});
//流式文件读取数据，绑定了data事件，就会自动读取数据
rs.on('data',function (data) {
    ws.write(data);
});
//通过在读取流和写入流直接建立通道进行传输
rs.pipe(ws);
```

### 1.5.2.异步文件操作方法

- ` fs.open(path, flags[, mode], callback)`打开文件
- `fs.close(fd, callback)`关闭文件
- `fs.writeFile(file, data[, options], callback)`简单文件写入
- ` fs.readFile(file[, options], callback)`简单文件读取
- `fs.write(fd, data[, position[, encoding]], callback)`异步文件写入
- `fs.read(fd, buffer, offset, length,position, callback)`异步文件读取

### 1.5.3.其他文件操作

- `fs.existsSync(path)`验证路径是否存在
- ` fs.stat(path, callback)`获取文件信息
- `fs.unlink(path, callback)`删除文件
- `fs.readdir(path[, options], callback)`列出文件
- ` fs.truncate(path, len, callback)`截断文件，即设置文件为指定大小
- ` fs.mkdir(path[, mode], callback)`建立目录
- ` fs.rmdir(path, callback)`删除目录
- ` fs.rename(oldPath, newPath, callback)`重命名，也可由于剪贴文件
- `fs.watchFile(filename[, options], listener)`监视文件的更改或写入，即文件修改时就触发`listener`回调







# 二、`CommonJS`

## 2.1.模块的定义

- 模块引用
- 模块定义
- 模块标识

## 2.2.引入其它模块

- `require()`:引入外部模块，传递一个文件路径作为参数；
  - 若使用相对路径，必须以`.`或`..`开头
  - 引入模块后，会返回一个对象，对象就是引入的模块

```js
let md = require('./imgCompress');
```

- 在node中一个js文件就是一个模块；

  - 每个js文件中的代码都是独立运行在一个函数中，不是在全局作用域中
  - 所以模块中的变量和函数在其他模块中不能访问

- 向外部暴露属性或方法，使用`exports`

  - 作为`exports`的属性向外暴露

  ```js
  exports.name = '张三';
  exports.getName = function(){
  }
  ```

## 2.3.模块的定义

- 模块的定义

```js
/**
 * 通过url加载图片对象
 * @param url 图片的url
 * @param callback  回调函数
 */
exports.urltoImage = function urltoImage(url, callback) {
    var img = new Image();
    img.src = url;
    img.onload = function () {
        callback(img);
    }
}
```

- 模块的使用

```js
let imgUtil = rquire('./imgCompress');
imgUtil.urltoImage('../img/1.jpg',function () {
    
})
```

## 2.4.模块的标识

模块的标识，就是使用`require()`来引入模块；通过模块标识就可以找到模块

### 1.模块的分类

- 核心模块

  - 核心模块直接使用模块的名字引入

  ```js
  let fs = require('fs');
  ```

- 文件模块

  - 文件模块的模块标识就是文件路径

## 2.5.node对象

### 1.`global`

`global`是一个node的全局对象，与window类似

- 在全局中创建的变量，都会作为global的属性保存
- 在全局中创建的函数，都会作为global的方法保存

```js
//证明模块化的代码是运行在函数中
console.log(arguments); //arguments是函数的属性
console.log(arhuments.callee+'');  //查看函数代码
```

## 2.6.模块的原理

模块中的代码都是包装在一个函数中执行，并且函数执行时，会传递五个参数

```js
function (exports, require, module, __filename, __dirname) { 
}
```

- `exports`用于将变量或函数暴露在外部
- `require`用于引入外部模块
- `module`表示当前模块本身
  - `exports`是`module`的属性
  - 即可使用`exports`导出，也可使用`module.exports`导出
  - `module.exports == exports`
- `__filename`当前模块的完整路径
- `__dirname`当前模块所在文件夹的路径

### 1.`exports`与`module.exports`的区别

```js
var exports=module.exports
```

`exports`是一个变量，它默认指向`module.exports`；通过`exports`是修改一个变量，通过`module.exports`是修改一个对象

- `exports`只能通过`.`向外暴露

```js
exports.getName = function(){
    return "张三"：
}
//该方式不能向外暴露属性或方法，因为此时修改了变量exports的指向了
exports={ 
    name:'张三',
    getName:function(){
        return this.name;
    }
}
```

- `module.exports`可以通过`.`向外暴露，也可直接赋值对象暴露

```js
module.exports={//修改module.exports对象的值
    name:'张三',
    getName:function(){
        return this.name;
    }
}
```









