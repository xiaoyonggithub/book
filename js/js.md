

## 一、滚动条

#### 1.1.`Easy Scroll`

1. 添加 `CSS `和 `JQUERY `文件 

```html
<!-- include CSS & JS files -->
<!-- CSS file -->
<link rel="stylesheet" type="text/css" href="easyscroll.css" media="screen" />
<!-- jQuery files -->
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="easyscroll.js"></script>
```

2. 调用`EASYSCROLL `插件 

```html
<script type="text/javascript">
    $(document).ready(function(){
      $(".div_scroll").scroll_absolute({arrows:true});
    });
</script>
```

3. 准备测试的`html`

```html
<div class="container">
  <div class="div_scroll">
    <div style="width: 1000px; height: auto; float: left; margin-top: 4px; margin-bottom: 8px">
      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec in ligula id sem 
      tristique ultrices eget id neque. Duis enim turpis, tempus at accumsan vitae,
       lobortis id sapien. Pellentesque nec orci mi, in pharetra ligula.
        Nulla facilisi. Nulla facilisi. Mauris convallis venenatis massa, 
        quis consectetur felis ornare quis. Sed aliquet nunc ac ante molestie
         ultricies. Nam pulvinar ultricies bibendum. Vivamus diam leo, 
         faucibus et vehicula eu, molestie sit amet dui. Proin nec orci 
         et elit semper ultrices. Cum sociis natoque penatibus et magnis
          dis parturient montes, nascetur ridiculus mus. Sed quis urna mi,
           ac dignissim mauris. Quisque mollis ornare mauris, sed laoreet 
           diam malesuada quis. Proin vel elementum ante. Donec 
            endrerit arcu ac odio tincidunt posuere. Vestibulum nec risus
             eu lacus semper viverra. Mauris convallis venenatis massa,
              quis consectetur felis ornare quis. Sed aliquet nunc ac 
              ane molestie ultricies. Nam pulvinar ultricies bibendum.
               Vivamus diam leo, faucibus et vehicula eu, molestie sit
                amet dui. Proin nec orci et elit.</p>
    </div>
  </div>        
</div>
```



#### 1.2.自定义滚动条







## 二、跨域请求

| 概念     | 描述                                                         |
| -------- | :----------------------------------------------------------- |
| 源       | 源（origin）就是协议、域名和端口号                           |
| 同源     | 若地址里面的协议、域名和端口号均相同则属于同源               |
| 同源策略 | 同源策略是浏览器的一个安全功能，不同源的客户端脚本在没有明确授权的情况下，不能读写对方资源 |
| 跨域     | 浏览器同源策略的影响，只要网站的协议名`protocol`、 主机`host`、 端口号`port `这三个中的任意一个不同，网站间的数据请求与传输便构成了跨域调用 |

注意：跨域的安全限制都是对浏览器端来说的，服务器端是不存在跨域安全限制的 。

#### 2.1.不受同源策略限制 

* 页面中的链接，重定向以及表单提交是不会受到同源策略限制的 
* 跨域资源的引入是可以的。但是`js`不能读写加载的内容。如嵌入到页面中的`<script src="..."></script>，<img>，<link>，<iframe>(即拥有src属性的标签)`等 

#### 2.2. 图片`ping`

图片可以从任何`URL`中加载，所以将`img`的`src`设置成其他域的`URL`，即可以实现简单的跨域 ,可以使用`onload`和`onerror`事件来确定是否接受到了响应 。

```js
//用于测试是否能跨域
var img=new Image();
img.src='//www.jb51.net';
img.onerror=function(){
 alert('error');
}
img.onload=function(){
 alert('success');
}
```

注意：使用图片`ping`跨域只能发送`get`请求，并且不能访问响应的文本（单向请求），只能监听是否响应而已，可以用来追踪广告点击 。

#### 2.3.`jsonp`





缺点：

- 只能使用`Get`请求
- 不能注册`success`、`error`等事件监听函数，不能很容易的确定`JSONP`请求是否失败
- `JSONP`是从其他域中加载代码执行，容易受到跨站请求伪造的攻击，其安全性无法确保

#### 2.4.`CORS`

`Cross-Origin Resource Sharing（CORS）`跨域资源共享是一份浏览器技术的规范 





#### 2.5.`window.name+iframe`



#### 2.6.`window.postMessage()`



## 三、日期处理

#### 3.1. 日期格式化

```js
Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
} 
```

```js
var time1 = new Date().format("yyyy-MM-dd hh:mm:ss");
```

#### 3.2. 计算两个日期之间的天数

```js
// 给日期类对象添加日期差方法，返回日期与diff参数日期的时间差，单位为天
Date.prototype.diff = function(date){
  return (this.getTime() - date.getTime())/(24 * 60 * 60 * 1000);
}
```

```js
// 构造两个日期，分别是系统时间和2013/04/08 12:43:45
var now = new Date();
var date = new Date('2013/04/08 12:43:45');
// 调用日期差方法，求得参数日期与系统时间相差的天数
var diff = now.diff(date);
```

## 四、操作伪元素

由于伪元素是在CSS渲染中操作元素，不受文档约束，也不影响文档本身，只影响最终样式。这些添加的内容不会出现在DOM中，仅仅是在CSS渲染层中加入。它不存在于文档中，所以JS无法直接操作它。而jQuery的选择器都是基于DOM元素的，因此也并不能直接操作伪元素。

#### 4.1.获取伪元素的属性值

```js
 window.getComputedStyle(element[, pseudoElement]);
//element:伪元素的所在的DOM元素
//pseudoElement:伪元素类型
```

```html
<div id="jadeId"></div>
<script type="text/javascript" charset="utf-8" async defer>
    var element = document.getElementById("jadeId");
    //getComputedStyle()获取伪元素的CSS样式声明对象
    var beforeStyle = window.getComputedStyle(element, ":before");
	//获取伪元素属性的值
    console.log(beforeStyle.width);
    console.log(beforeStyle.getPropertyValue("width")); 
    console.log(beforeStyle.content); 
</script>
```

getPropertyValue()和直接使用键值都是

## 五、POST提交

`window.open()`打开窗口时，post提交请求

```js
function openPostWindow(url, param, title) {
    //创建form标签
    var tempForm = document.createElement("form");
    tempForm.id="tempForm1";
    tempForm.method="post";
    tempForm.action=url;
    tempForm.target=title;
    //参数
    for(var key in param){
        //创建input标签
        var hideInput = document.createElement("input");
        hideInput.type="hidden";
        hideInput.id = key;
        hideInput.name= key;
        hideInput.value= param[key];
        tempForm.appendChild(hideInput);
    }

    if(window.attachEvent){
        tempForm.attachEvent("onsubmit",function(){});        //IE
    }else{
        tempForm.addEventListener("submit",function(){},false);    //firefox
    }
    document.body.appendChild(tempForm);
    if(window.fireEvent){
        tempForm.fireEvent("onsubmit");
    }else{
        var evt = document.createEvent("HTMLEvents");
        evt.initEvent("submit", false, true);
        tempForm.dispatchEvent(evt);
    }
    tempForm.submit();
    document.body.removeChild(tempForm);
}
```

## 六、对象之间的转化

### 6.1.json与js对象的转化

1. js转json

```javascript
//json字符串
var js = {
    name:"张三",
    age:12,
    getName:function(){
        return this.name;
    }
};
console.log(JSON.stringify(js)); //{"name":"张三","age":12},js对象包含方法就忽略,js转json
```

2.json转js

```javascript
var json = '{"name":"张三","age":12}';
console.log(JSON.parse(json)); //json转js
console.log(eval('('+json+')'));//json转js
console.log($.parseJSON(json)); //使用jquery转化
```

### 6.2.jquery与dom对象转化

```html
<div id="main"></div>
```

1. jquery对象转dom对象

```javascript
var main = $("#main");//jquery
console.log(main);
//jquery转dom对象
console.log(main.get(0));
console.log(main[0]);
```

2.dom对象转jquery对象

```javascript
var m = document.getElementById("main");//dom对象
//dom转jquery对象，$(dom)
console.log($(m));
```

### 6.3.dom与字符串的转化

1. dom转字符串

```javascript
function domToString ( node ) {  
    var tmpNode = document.createElement( "div" );  
    tmpNode.appendChild( node.cloneNode( true ) );  
    var str = tmpNode.innerHTML;  
    tmpNode = node = null; // prevent memory leaks in IE  
    return str;  
}      
var str = domToString(document.getElementById("main"));
console.log(str);  //<div id="main">你好</div>
```

2. 字符串转dom

```javascript
function parseDom(str) {
    var objE = document.createElement("div");
    objE.innerHTML = str;
    return objE.childNodes;
};
var dom= parseDom('<div id="main">你好</div>');
console.log(dom);
```

## 七、`iframe`

```html
<iframe id="ifm" name="ifm" src="img/(36).jpg" frameborder="0"></iframe>
```

### 7.1.iframe操作子页面

获取`iframe`的window和docment对象

```javascript
var ifm = document.getElementById("ifm");
//contentWindow获取iframe的window对象
var ifmWindow = ifm.contentWindow;
//使用name属性获取window对象
ifmWinodw = window.frames['ifm'].window;
console.log(ifmWindow);

//contentDocument获取iframe的docment对象
var ifmDocment = ifm.contentDocument;
console.log(ifmDocment);
//获取docment的元素
console.log(ifmDocment.documentElement);
//获取head元素内容
console.log(ifmDocment.head);
//获取body元素内容
console.log(ifmDocment.body);
```

### 7.2.iframe操作父页面

```javascript
//返回自身iframe的引用
console.log(ifmWindow.self);
//获取iframe最顶层容器
console.log(ifmWindow.top);
//获取iframe的父级容器
console.log(ifmWindow.parent);
```

### 7.3.iframe轮询

## 八、JSON数据处理

### 8.1.获取json的键值

```js
var json = {"yuj210":102902,"acc001":82923};
for(var key in json){
    var key = key; //yuj210
    var val = json[key]; //102902
}

var json = {"yuj210":102902,"acc001":82923};
$.each(json,function(key){
    var key = key; //yuj210
    var val = json[key]; //102902
});

var json = {"yuj210":102902,"acc001":82923};
$.each(json,function(key){
    var key = key; //yuj210
    var val = eval('json.'+key); //102902
});
```

