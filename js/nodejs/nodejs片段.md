



```js
//日志输出
console.log();
//信息输出
console.info();
//格式化 ?
console.log("%"); //%
console.log("%%"); //%%
console.log("%s"); //%s

console.log("%3d", 3.0);
console.log("%3d", 0.3423432);

console.log("%j", {name: "zhangsan", age: 120});

console.log("%s", "%d"); //%d
console.log("%s", "%s"); //%s
//错误输出
console.error();
//警告输出
console.warn();

//重定向错误输出流
//node ./js/test.js 2>error.log

//查看node对象的属性和方法
console.dir();
console.dir(console);

//事件计时器
console.time("标识字符");
//...需计算执行时间的代码
console.timeEnd("标识字符");

//查看当前位置的栈，并跟踪到标准错误输出流
console.trace("栈的标识");
//console.trace()底层实现是Assert模块的assert.ok()完成

//对表达式结果进行评估
console.assert(2 > 3, "OK");


//读取命令行输入信息，readline模块
var readline = require("readline");
let rl = readline.createInterface({
    input: process.stdin,  //要监听的可读流（必须）
    output: process.stdout //要写入readlinde的可写流（必须）
    //completer:用于Tab自动补全的可选函数
    //terminal:
});
/**
 * rl.question(query,callback);
 * query:问题
 * callback:回调
 */
rl.question("你的名字是什么？\n", function (answer) {
    console.log("我的名字是：" + answer);
    rl.close();
});
```





```js
//阶乘计算
var readline = require("readline");
let rl = readline.createInterface({
    input: process.stdin,  //要监听的可读流（必须）
    output: process.stdout //要写入readlinde的可写流（必须）
    //completer:用于Tab自动补全的可选函数
    //terminal:
});
console.log("\n----------继承运算-----------\n");
rl.question("请输入要我的名字是计算的阶乘数：", function (num) {
    console.log(num + "! = " + factoial(num) + "\n");
    rl.close();
});

function factoial(num) {
    if (num > 0) {
        if (num == 1) {
            return 1;
        } else {
            return factoial(num - 1) * num;
        }
    } else {
        return 0;
    }
}
```

## 1.17.向控制台输出组合控制键

`readline`模块的`write()`可实现向控制台输出控制键功能