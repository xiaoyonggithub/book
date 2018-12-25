# 一、`less`

## 1.1.`less`的注释

- `//`开头的注释，不会编译到css文件中
- `/**/`包裹的注释，会被编译到css文件中

## 1.2.变量

- 使用`@`来申明一个变量

```less
@pink:pink;
```

- 作为普通值使用`@pink`

```less
@background: {background:red;};
.wrap{
    color:@pink;
    background();
}
```

- 作为选择器或属性名使用，此时需要加`@{variable}`引用

```less
@selector:#wrap;
@{selector}{
    position: relative;
    width: 300px;
    height: 400px;
}
```

- 作为url使用，此时需要加`@{variable}`引用

```less
@zero:0;  //作为普通值使用
@selector:wrap;//作为选择器使用
@url:"../img/zdy.jpg";//作为url使用
*{
    margin: @zero;
    padding: @zero;
}
#@{selector}{
    position: relative;
    width: 300px;
    height: 300px;
    border: 1px solid;
    margin: @zero auto;
    background: url("@{url}");
}
```

- 变量的延迟加载；在使用前不一定要先申明，会在变量的申明所有加载完成后，才会去使用变量

```less
@var: 0;
.class {
  @var: 1;
  .brass {
    @var: 2;
    three: @var; //3
    @var: 3;
  }
  one: @var; //1
}
```

- 变量的作用域：**就近原则**
- 使用变量定义变量

```less
/* Less */
@fnord:  "I am fnord.";
@var:    "fnord";
#wrap::after{
  content: @@var; //@@var-->@fnord--> "I am fnord."
}
/* 生成的 CSS */
#wrap::after{
  content: "I am fnord.";
}
```

### 1.变量拼串/插值

- 拼串`~"字符@{变量}字符"`
- 插值格式`@{num}`
- 数据拼且放在冒号之后可使用这样的格式`@@var`

```less
.judge(@i) when(@i=1){
  @size:15px;
}
.judge(@i) when(@i>1){
  @size:16px;
}
.loopAnimation(@i) when (@i<16) {
  
  .circle:nth-child(@{i}){
      .judeg(@i);
      border-radius:@size @size 0 0;
      animation: ~"circle-@{i}" @duration infinite @ease;
      transition-delay:~"@{i}ms";
  }
  @keyframes ~"circle-@{i}" {
      // do something...
  }
  .loopAnimation(@i + 1);
}
```

#### 1.1.放在冒号之后

```less
//公共变量
@mkcolor1:#6ec5ff;
@mkcolor2:#ff8365;
@mkcolor3:#fdc139;
@mkcolor4:#83d36d;
@mkcolor5:#03afaf;

.taskSlideBg(@num) when (@num <6){
  @color:"mkcolor@{num}";
  .mkUser-task-title{background:@@color;}
  .taskSlideBg((@num+1))
}
.taskSlideBg(1);
```

#### 1.2.不在冒号后面的情况可以使用`~`

```less
.taskSlideBg(@num) when (@num <6){
  @num2:~"@{num}n";
  @color:"mkcolor@{num}";
  #mkUser-task-con .mkUser-task-box:nth-child(@{num2}) .mkUser-task-title{background:@@color;}
  .taskSlideBg((@num+1))
}
.taskSlideBg(1);
```

```less
#mkUser-task-con .mkUser-task-box:nth-child(1n) .mkUser-task-title {
  background: #6ec5ff;
}
#mkUser-task-con .mkUser-task-box:nth-child(2n) .mkUser-task-title {
  background: #ff8365;
}
#mkUser-task-con .mkUser-task-box:nth-child(3n) .mkUser-task-title {
  background: #fdc139;
}
#mkUser-task-con .mkUser-task-box:nth-child(4n) .mkUser-task-title {
  background: #83d36d;
}
#mkUser-task-con .mkUser-task-box:nth-child(5n) .mkUser-task-title {
  background: #03afaf;
}
```

## 1.3.嵌套规则

- 基本嵌套规则
- `&`表示父级选择器

```less
#list{
    list-style: none;
    a{
        float: left;
        &:hover{/*&表示父级*/
            color: red;
        }
        &_content{//_content-->#list_content
            margin:20px;
        }
    }
}
```

```css
//css
#list{
    list-style: none;
}
#list a{
    float: left;
}
#list a:hover{
    color: red;
}
#list_content{
    margin:20px; 
}
```

## 1.4.混合`Mixins`

### 1.普通混合

```less
.bordered {
    border-top: dotted 1px black;
    border-bottom: solid 2px black;
}
.post a {
    color: red;
    .bordered; //使用混合；.bordered与.bordered()等价
}
```

### 2.不带输出的混合

- 不带输出的混合，就是添加`()`

```less
.bordered() {
    border-top: dotted 1px black;
    border-bottom: solid 2px black;
}
.post a {
    color: red;
    .bordered; //使用混合
}
```

### 3.带参数的混合

```less
.bordered(@style,@width,@color) {
    border-top: @style @width @color;
    border-bottom: @style @width @color;
}
```

### 4.带参数且有默认值的混合

```less
.bordered(@style:solid,@width:1px,@color:black) {
    border-top: @style @width @color;
    border-bottom: @style @width @color;
}
```

### 5.命名参数

参数没有按顺序设置，而是通过名称直接引用

```less
.bordered(@style,@width,@color) {
    border-top: @style @width @color;
    border-bottom: @style @width @color;
}
.post a {
    color: red;
    .bordered(@style:solid,@color:red); //使用混合
}
```

### 6.匹配模式

- `@_`表示匹配参数是变量

```less
.triangle(top,@width:20px,@color:#000){
    border-color:transparent  transparent @color transparent ;
}
.triangle(right,@width:20px,@color:#000){
    border-color:transparent @color transparent  transparent ;
}

.triangle(bottom,@width:20px,@color:#000){
    border-color:@color transparent  transparent  transparent ;
}
.triangle(left,@width:20px,@color:#000){
    border-color:transparent  transparent  transparent @color;
}
.triangle(@_,@width:20px,@color:#000){ //匹配模式
    border-style: solid;
    border-width: @width;
}
.main{
    .triangle(left, 50px, #999)
}
//css
.main{
  border-color:transparent  transparent  transparent #999;
  border-style: solid;
  border-width: 50px;
}
```

### 7.`@arguments`

`@arguments`指全部参数

```less
.border(@a:10px,@b:50px,@c:30px,@color:#000){
    border:solid 1px @color;
    box-shadow: @arguments;//指代的是全部参数
}
#main{
    .border(0px,5px,30px,red);//必须带着单位
}
```

### 8.命名空间

```less

```

### 9.混合筛选

- 比较运算有：` > >= = =< <`
- 等于是：`=`
- 除去关键字`true`以外的值都被视为`false`

```less
#card{
    // and 运算符 ，相当于 与运算 &&，必须条件全部符合才会执行
    .border(@width,@color,@style) when (@width>100px) and(@color=#999){
        border:@style @color @width;
    }

    // not 运算符，相当于 非运算 !，条件为 不符合才会执行
    .background(@color) when not (@color>=#222){
        background:@color;
    }

    // , 逗号分隔符：相当于 或运算 ||，只要有一个符合条件就会执行
    .font(@size:20px) when (@size>50px) , (@size<100px){
        font-size: @size;
    }
}
```

### 10.数量不定的混合

```less
.boxShadow(...){
    box-shadow: @arguments;
}
.textShadow(@a,...){
    text-shadow: @arguments;
}
```

```less
/*less */
#main{
    .boxShadow(1px,4px,30px,red);
    .textShadow(1px,4px,30px,red);
}

/*CSS */
#main{
    box-shadow: 1px 4px 30px red;
    text-shadow: 1px 4px 30px red;
}
```

### 11.`!important`

```less
/* Less */
.border{
    border: solid 1px red;
    margin: 50px;
}
#main{
    .border() !important; //所有的属性值都会添加上!important
}
/* CSS */
#main {
    border: solid 1px red !important; 
    margin: 50px !important;
}
```

### 12.混合循环

Less并没有提供for循环功能，但是可以使用递归实现循环

```less
/* Less */
.generate-columns(4);

.generate-columns(@n, @i: 1) when (@i =< @n) {
    .column-@{i} {
        width: (@i * 100% / @n);
    }
    .generate-columns(@n, (@i + 1));
}

/* 生成后的 CSS */
.column-1 {
    width: 25%;
}
.column-2 {
    width: 50%;
}
.column-3 {
    width: 75%;
}
.column-4 {
    width: 100%;
}
```

### 13.混合属性拼接

- `+`表示逗号

```less
/* Less */
.boxShadow() {
    box-shadow+: inset 0 0 10px #555;
}
.main {
    .boxShadow();
    box-shadow+: 0 0 20px black;
}
/* 生成后的 CSS */
.main {
    box-shadow: inset 0 0 10px #555, 0 0 20px black;
}
```

- `+_`表示空格

```less
/* Less */
.Animation() {
  transform+_: scale(2);
}
.main {
  .Animation();
  transform+_: rotate(15deg);
}

/* 生成的 CSS */
.main {
  transform: scale(2) rotate(15deg);
}
```

### 14.例子

```less
/* Less */
.average(@x, @y) {
  @average: ((@x + @y) / 2);
}

div {
  .average(16px, 50px); // 使用混合，@average:33px;
  padding: @average;    // 使用返回值， padding: 33px;
}

/* 生成的 CSS */
div {
  padding: 33px;
}
```



## 1.5.计算

- 任何数字、颜色或者变量都可以参与加减乘除运算
  - 加减法时 以第一个数据的单位为基准
  - 乘除法时 注意单位一定要统一

```less
@rem:100rem;
#wrap .sjx{
   width:(100 + @rem)
}
```

## 1.6.`less`避免编译

- `~''`可避免编译

```less
*{
    margin: 100 *  10px;
    padding: ~"cacl(100px + 100)";
}
```

## 1.7.继承

- `extend `是Less的一个伪类，它可继承所匹配声明中的全部样式

```less
/* Less */
.animation{
    transition: all .3s ease-out;
    .hide{
      transform:scale(0);
    }
}
#main{
    &:extend(.animation);//继承于混合.animation();
}
#con{
    &:extend(.animation .hide);//继承于混合.hide();
}

/* 生成后的 CSS */
.animation,#main{
  transition: all .3s ease-out;
}
.animation .hide , #con{
    transform:scale(0);
}
```

- `all`全局搜索替换

```less
/* Less */
#main{
    width: 200px;
}
#main {
    &:after {
        content:"Less is good!";
    }
}
#wrap:extend(#main all) {}//会继承所有的匹配的#main

/* 生成的 CSS */
#main,#wrap{
    width: 200px;
}
#main:after, #wrap:after {
    content: "Less is good!";
}
```

## 1.8.导入

### 1.`@import`

`@import`导入其它的less文件，位置可以任意

```less
import "main";   //可以省略文件后缀
import "main.less";
```

### 2.`reference`

`@import (reference)`导入外部文件，但是不会把导入的文件编译到最终的输出中，只引用

```less
@import (reference) "bootstrap.less"; 
#wrap:extend(.navbar all){}
```

### 3.`once`

`@import`语句的默认行为；表明相同的文件只会被导入一次，而随后的导入文件的重复代码都不会解析

```less
@import (once) "foo.less";
@import (once) "foo.less";//这条将被忽略
```

### 4.`multiple`

`@import (multiple)`允许导入多个同名文件

```less
// file: foo.less
.a {
  color: green;
}
```

```less
@import (multiple) "foo.less";
@import (multiple) "foo.less";
```

```css
//css，引用了两次，因此有两条语句
.a {
  color: green;
}
.a {
  color: green;
}
```

## 1.9.函数

### 1.判断函数

- `isnumber()`判断是否是一个数字

```less
isnumber(1234);     // true
isnumber(56px);     // true
isnumber(7.8%);     // true
isnumber(#ff0);     // false
isnumber(blue);     // false
isnumber("string"); // false
isnumber(keyword);  // false
isnumber(url(...)); // false
```

- `iscolor()`判断是否是颜色

```less
iscolor(blue);  //true
```

- `isurl()`判断是否是url

### 2.颜色操作

- `saturate()`增加一定数值的颜色饱和度
- `lighten()`增加一定数值的颜色亮度
- `darken()`降低一定数值的颜色亮度
- `fade()`给颜色设定一定数值的透明度
- `mix()`根据比例混合两种颜色

### 3.数学函数

- `ceil()`向上取整
- `floor()`向下取整
- `percentage()`将浮点数转换为百分比字符串
- `round()`四舍五入
- `sqrt()`计算平方根
- `abs()`计算绝对值，保存单位不变
- `pow()`计算乘方

## 1.10.`Less`中使用`JS`

由于less是有js编写，所以less中可以使用js

```less
@content:`"aaa".toUpperCase()`;
#randomColor{
  @randomColor: ~"rgb(`Math.round(Math.random() * 256)`,`Math.round(Math.random() * 256)`,`Math.round(Math.random() * 256)`)";
}
#wrap{
  width: ~"`Math.round(Math.random() * 100)`px";
  &:after{
      content:@content;
  }
  height: ~"`window.innerHeight`px";
  alert:~"`alert(1)`";
  #randomColor();
  background-color: @randomColor;
}
```

```css
#wrap{
  width: 随机值（0~100）px;
  height: 743px;//由电脑而异
  background: 随机颜色;
}
#wrap::after{
  content:"AAA";
}
```

## 1.11.环境的搭建

### 1.在服务器端使用

1. 安装nodejs
2. 安装less

```less
//全局安装less
npm install less -g 
//安装最新版本的less
npm install less@latest
//查看版本
lessc -v
```

3. 编译less

```less
lessc index.less index.css
```

### 2.在客户端使用

```html
<link rel="stylesheet/less" type="text/css" href="styles.less">
<!--添加在页面的底部-->
<script src="less.js" type="text/javascript"></script>
```

#### 2.1.监视模式

监视模式是客户端的一个功能，该功能允许你改变样式时，客户端自动刷新；有如下两种方式开启监视模式：

- 在URL后面添加`#!watch`
- 在终端运行`less.watch()`来启动监视模式



  

参考：

[https://juejin.im/post/5a2bc28f6fb9a044fe464b19#heading-11](https://juejin.im/post/5a2bc28f6fb9a044fe464b19#heading-11)

[https://segmentfault.com/a/1190000007472208](https://segmentfault.com/a/1190000007472208)

[https://segmentfault.com/a/1190000004867139](https://segmentfault.com/a/1190000004867139)

[https://segmentfault.com/a/1190000015606220](https://segmentfault.com/a/1190000015606220)

[http://www.bootcss.com/p/lesscss/](http://www.bootcss.com/p/lesscss/)