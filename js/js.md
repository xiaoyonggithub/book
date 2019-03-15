

# 一、滚动条

## 1.1.`Easy Scroll`

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



## 1.2.自定义滚动条







# 二、跨域请求

| 概念     | 描述                                                         |
| -------- | :----------------------------------------------------------- |
| 源       | 源（origin）就是协议、域名和端口号                           |
| 同源     | 若地址里面的协议、域名和端口号均相同则属于同源               |
| 同源策略 | 同源策略是浏览器的一个安全功能，不同源的客户端脚本在没有明确授权的情况下，不能读写对方资源 |
| 跨域     | 浏览器同源策略的影响，只要网站的协议名`protocol`、 主机`host`、 端口号`port `这三个中的任意一个不同，网站间的数据请求与传输便构成了跨域调用 |

注意：跨域的安全限制都是对浏览器端来说的，服务器端是不存在跨域安全限制的 。

## 2.1.不受同源策略限制 

* 页面中的链接，重定向以及表单提交是不会受到同源策略限制的 
* 跨域资源的引入是可以的。但是`js`不能读写加载的内容。如嵌入到页面中的`<script src="..."></script>，<img>，<link>，<iframe>(即拥有src属性的标签)`等 

## 2.2. 图片`ping`

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

## 2.3.`jsonp`





缺点：

- 只能使用`Get`请求
- 不能注册`success`、`error`等事件监听函数，不能很容易的确定`JSONP`请求是否失败
- `JSONP`是从其他域中加载代码执行，容易受到跨站请求伪造的攻击，其安全性无法确保

## 2.4.`CORS`

`Cross-Origin Resource Sharing（CORS）`跨域资源共享是一份浏览器技术的规范 





## 2.5.`window.name+iframe`



## 2.6.`window.postMessage()`



# 三、日期处理

## 3.1. 日期格式化

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

## 3.2. 计算两个日期之间的天数

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

# 四、操作伪元素

由于伪元素是在CSS渲染中操作元素，不受文档约束，也不影响文档本身，只影响最终样式。这些添加的内容不会出现在DOM中，仅仅是在CSS渲染层中加入。它不存在于文档中，所以JS无法直接操作它。而jQuery的选择器都是基于DOM元素的，因此也并不能直接操作伪元素。

## 4.1.获取伪元素的属性值

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

# 五、POST提交

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

# 六、对象之间的转化

## 6.1.json与js对象的转化

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

## 6.2.jquery与dom对象转化

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

## 6.3.dom与字符串的转化

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

# 七、`iframe`

```html
<iframe id="ifm" name="ifm" src="img/(36).jpg" frameborder="0"></iframe>
```

## 7.1.iframe操作子页面

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

## 7.2.iframe操作父页面

```javascript
//返回自身iframe的引用
console.log(ifmWindow.self);
//获取iframe最顶层容器
console.log(ifmWindow.top);
//获取iframe的父级容器
console.log(ifmWindow.parent);
```

## 7.3.iframe轮询

# 八、JSON数据处理

## 8.1.获取json的键值

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

# 九、js模板引擎

```js
var TemplateEngine = function (html, options) {
    var re = /<%([^%>]+)?%>/g, reExp = /(^( )?(if|for|else|switch|case|break|{|}))(.*)?/g, code = 'var r=[];\n',
        cursor = 0, match;
    var add = function (line, js) {
        js ? (code += line.match(reExp) ? line + '\n' : 'r.push(' + line + ');\n') :
        (code += line != '' ? 'r.push("' + line.replace(/"/g, '\\"') + '");\n' : '');
        return add;
    }
    while (match = re.exec(html)) {
        add(html.slice(cursor, match.index))(match[1], true);
        cursor = match.index + match[0].length;
    }
    add(html.substr(cursor, html.length - cursor));
    code += 'return r.join("");';
    return new Function(code.replace(/[\r\t\n]/g, '')).apply(options);
}
```

```js
var template =
    'My skills:' +
    '<%if(this.showSkills) {%>' +
    '<%for(var index in this.skills) {%>' +
    '<a href="#"><%this.skills[index]%></a>' +
    '<%}%>' +
    '<%} else {%>' +
    '<p>none</p>' +
    '<%}%>';
console.log(TemplateEngine(template, {
    skills: ["js", "html", "css"],
    showSkills: true
}));
```

[参见于](https://juejin.im/post/5c0e5042f265da61524d3a43)



# 十、`iframe`

- `contentWindow`返回`<iframe>`的window对象，可以通过该window对象访问iframe的文档和内部DOM
- 

## 10.1.`iframe`高度自适应子页面的高度

```js

```



`document.documentElement.scrollWidth`获取高度的真实值





# 十一、图片压缩

- `canvas.getContext(contextType, contextAttributes);`返回[`canvas`](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/canvas) 的上下文

```js
/**
 * 通过url加载图片对象
 * @param url 图片的url
 * @param callback  回调函数
 */
function urltoImage(url, callback) {
    var img = new Image();
    img.src = url;
    img.onload = function () {
        callback(img);
    }
}

/**
 * 将Image对象转化为Canvas对象
 * @param image
 * @returns {HTMLElement}
 */
function imageToCanvas(image) {
    var cvs = document.createElement("canvas");
    var ctx = cvs.getContext('2d');//获取canvas的上下文
    cvs.width = image.width;
    cvs.height = image.height;
    ctx.drawImage(image, 0, 0, cvs.width, cvs.height);
    return cvs;
}

/**
 * 将Canvas对象压缩为Blob对象
 * @param canvas
 * @param quality 压缩的质量0-1
 * @param callback 回调函数
 */
function canvasResizetoFile(canvas, quality, callback) {
    canvas.toBlob(function (blob) {
        callback(blob);
    }, 'image/jpeg', quality);
}

/**
 * Canvas对象压缩为dataURL字符串
 * @param canvas
 * @param quality 压缩质量0-1
 * @returns {string}
 */
function canvasResizetoDataURL(canvas, quality) {
    return canvas.toDataURL('image/jpeg', quality);
}

/**
 * 将 File（Blob）类型文件转变为dataURL字符串
 * @param file        File（Blob）类型文件
 * @param callback   回调函数
 */
function filetoDataURL(file, callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
        callback(e.target.result);
    };
    reader.readAsDataURL(file);
}

/**
 * 将dataURL字符串转变为Image类型文件
 * @param dataurl  dataURL字符串
 * @param callback 回调函数
 */
function dataURLtoImage(dataurl, callback) {
    var img = new Image();
    img.onload = function () {
        callback(img);
    };
    img.src = dataurl;
}

/**
 * 将一串dataURL字符串转变为Blob类型对象
 * @param dataurl  dataURL字符串
 * @returns {Blob}
 */
function dataURLtoFile(dataurl) {
    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {type: mime});
}


/**
 * 压缩file对象后返回file对象
 * @param file
 * @param quality  压缩质量
 * @param callback
 */
function fileResizetoFile(file,quality,callback){
    filetoDataURL (file,function(dataurl){
        dataURLtoImage(dataurl,function(image){
            canvasResizetoFile(imagetoCanvas(image),quality,callback);
        })
    })
}
```

# 十二、剪切板操作

## 12.1.复制、剪切、粘贴的事件

- `copy`复制操作时触发

- `cut`剪切操作时触发

- `paste`粘贴时触发

- 上面每个事件都有一个`before`事件，分别是

  - `beforecopy`
  - `beforecut`
  - `beforepaste`

  - 一般不使用，因为不同浏览器触发条件不一致

```js
document.body.oncopy = e => {
    // 监听全局复制 做点什么
}
// 还有这种写法：
document.addEventListener("copy", e => {
    // 监听全局复制 做点什么
});

//也可以为某些dom单独添加剪切板事件
let test1 = document.querySelector('#test1');
test1.oncopy = e => {
    // 监听test1发生的复制事件 做点什么
    // test1发生的复制事件会触发回调，其他地方不会触发回调
}
```

## 12.2.`clipboardData`

`clipboardData`用于访问和修改剪切板中的数据，兼容性：

- 在IE中`clipboardData`是window对象的属性
- 在`Chrome`、`Safari`和`Firefox`中，`clipboardData`对象是相应的`event`对象的属性

```js
document.body.oncopy = e => {
    //使用时兼容性处理
    let clipboardData = (e.clipboardData || window.clipboardData); 
    // 获取clipboardData对象 + do something
}
```

`clipboardData`的方法：

- `getData()`获取剪切板的数据，`getData('text')`设置要取得数据的格式
  - 在chorme上只有`paste`粘贴的时候才能用`getData()`访问到数据

```js
//在chorme下：
document.body.onpaste = e => {
    let clipboardData = (e.clipboardData || window.clipboardData); // 兼容处理
    //clipboardData.getData('text')只能获取粘贴时的数据
    console.log('要粘贴的数据', clipboardData.getData('text')); 
}

document.body.oncopy = e => {
    //window.getSelection(0).toString() 获取复制和剪切的数据
    console.log('被复制的数据:', window.getSelection(0).toString());
}

```

- `setData()`设置剪切板的数据，`setData('text',放在剪切板的文本)`
- `clearData()`?

## 12.3.实现类知乎/掘金复制大段文本添加版权信息





## 12.1掘金、知乎复制大段文本如何不显示版权信息

```js
document.oncopy = 
    event => event.clipboardData.setData('text',window.getSelection(0).toString());
```

# 十三、计算文本框的剩余字数

```html
<ta:panel id="pnl5" key="绩效目标<span style='color:red;'>(剩余字数<span class='surplus'>5000</span>)</span>">
```

```js
//计算剩余字数
function fnSurplusWords(eleId, pnelId) {
    var ele = document.getElementById(eleId);
    var total = +($('#' + pnelId + ' .panel-title .surplus').text());
    setSurplusWords(ele, pnelId, total);
    if ('oninput' in ele) {
        ele.addEventListener('input', function (ev) {
            setSurplusWords(ele, pnelId, total);
        });
    } else if ('onpropertychange' in ele) {
        ele.onpropertychange = setSurplusWords(ele, pnelId, total);
    }
}

function setSurplusWords(ele, pnelId, total) {
    var len = total - $(ele).val().length;
    if (len < 0) {
        len = 0;
        $(ele).val($(ele).val().substr(0, total));
    }
    $('#' + pnelId + ' .panel-title .surplus').html(len);
}
```

# 十四、滚动穿透

