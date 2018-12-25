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

- `npm`是`nodejs`下的包管理器

- 查看`npm`版本

```cmd
npm -v
```



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

