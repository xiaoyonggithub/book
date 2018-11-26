## 七、正则表达式

### 7.1.创建正则表达式

1. 语法：var reg = new RegExp("正则表达式","模式");

- 匹配模式
  - `i` 忽略大小写
  - `g` 全局匹配模式

1. 使用字面量创建正则表达式

```javascript
var reg = /正则表达式/模式;
```

### 7.2.匹配规则

| 规则       | 描述                          |
| ---------- | ----------------------------- |
| `|`        | a\|b,a或b                     |
| `[]`       | [ab],a或b，即匹配任意一个字符 |
| `^`        | 除了，`[^0-9]`处理数字        |
| `/a{n}/`   | 表示a出现n次                  |
| `/a{1,3}/` | 出现m-n次                     |
| `/a{3,}/`  | 出现m次以上                   |
| `/a+/`     | 至少一个`/a{1,}/`             |
| `/a*/`     | 0个或多个`/a{0,}/`            |
| `/a?/`     | 0个或一个`/a{0,1}/`           |
| `/^a/`     | 字符串以a开头，`^`表示开头    |
| `/a$/`     | 字符串以a结尾，`$`表示结尾    |
| `/./`      | 任意字符                      |

注意：特殊字符需要转义

```js
var reg = new RegExp('\\.'); //表示.,此时需要\\表示一个
var reg = /\./;  //表示.
```

| 规则 | 描述                                 |
| ---- | ------------------------------------ |
| `\w` | 表示任意数字、字母、_ ,`/[A-z0-9_]/` |
| `\W` | 与`\W`相反，`/[^A-z0-9_]/`           |
| `\d` | 任意数字`/[0-9]/`                    |
| `\D` | 非数字`/[^0-9]/`                     |
| `\s` | 空格                                 |
| `\S` | 除了空格                             |
| `\b` | 单词边界，`/\bchild\b/`              |
| `\B` | 除了单词边界                         |

### 7.3.正则函数

1. reg.test(str),测试str是否符合正则表达式
2. str.split(reg);按正则匹配拆分字符
3. str.search(reg);按正则匹配搜索是否包含指定字符，只能匹配第一个（即使指定了全局g）
4. str.match(reg);从字符串中提取正则匹配的字符

```javascript
str.match(/[A-z]/gi); //表示提取所有的匹配的字符，默认只能提取第一个字符
//返回数组
```

1. str.replace(reg,newstr);按正则匹配替换字符

```javascript
str.replace(/a-z/gi,""); //删除字符串中的字母
```

## 八、`DOM（Docment Object Model）`

### 8.1.节点

- 文档节点：整个html文档
- 元素节点：HTML标签
- 属性节点：元素的属性
- 文本节点：HTML标签的文本值

#### 8.1.1.节点的属性

|          | `nodeName` | `nodeType` | `nodeValue` |
| :------: | :--------: | :--------: | :---------: |
| 文本节点 | `#docment` |    `9`     |   `null`    |
| 元素节点 |   标签名   |    `1`     |   `null`    |
| 属性节点 |   属性名   |    `2`     |   属性值    |
| 文本节点 |  `#text`   |    `3`     |  文本内容   |

- 文档节点的对象`(docment)`是window的属性

### 8.2.事件

#### 8.2.1.事件的绑定

1. 绑定一个事件

```js
var btn = document.getElementById('btn');
btn.onclik = function(){

}
```

页面加载完成后才执行

```js
window.onload = function(){
    //页面加载完成之后执行
    var btn = document.getElementById('btn');
    btn.onclik = function(){

    }
}
```

此种方式只能给元素绑定一个事件，绑定多个时后面的会覆盖前面的事件。

2. `addEventListener`

`addEventListener`可以为一个元素同时绑定多个事件，按绑定的先后顺序执行，IE8不支持

```js
//第三个参数表示是否在捕获阶段触发事件
outer.addEventListener('click', function(){

}, false);
```

`attachEvent`在IE8使用此方法绑定函数，也可绑定多个事件，但是后绑定的先执行

```js
//注意绑定的事件要加上on
outer.attachEvent('onclick',function(){

});
```

注意：`addEventListener`中的this是绑定事件的对象，而`attachEvent`中的this是window对象



> 兼容IE8的事件绑定方法、

```js
//obj 要绑定的元素
//eventStr 绑定事件的字符串
//callback 回调函数
function bind(obj,eventStr,callback){
    if(obj.addEventListener){
        obj.addEventListener(eventStr, callback,false);	
    }else{
        //兼容IE8,并统一this对象
        obj.attachEvent('on'+eventStr,function(){
            //自己在匿名函数中调用回调函数，并修改this
            callback.call(obj);
        });	
    }
}
```

3.`removeEventListener`

`removeEventListener`取消`addEventListener`绑定的事件，removeEventListener的第二个参数必须是removeEventListener的回调函数，否则取消不到

```js
//obj 事件绑定的元素
//eventStr 绑定事件的字符串
//callback 回调函数
function removeEvent(obj,eventStr,callback){
    if(obj.removeEventListener){
        obj.removeEventListener(eventStr, callback, false)
    }else{
        //兼容IE8,并统一this对象
        obj.detachEvent('on'+eventStr,function(){
            //自己在匿名函数中调用回调函数，并修改this
            callback.call(obj);
        });	
    }
}
```



#### 8.2.2.事件对象

事件对象：当事件的响应函数触发时，浏览器每次都会将一个对象作为实参传递给响应函数；事件对象封装了事件相关的一切信息

- `onmousemove`：鼠标在元素中移动时触发

```js
ele.onmousemove = function(e){
    //鼠标的坐标
    var x = e.clientX; 
    var y = e.clientY;
}
```

在IE8中不是将事件对象传递响应函数的，而是作为window的一个属性

```js
//在IE8中
ele.onmousemove = function(e){
    //鼠标的坐标
    var x = window.event.clientX; 
    var y = window.event.clientY;
}
```

```js
ele.onmousemove = function(event){
    //没有传入event,就在window对象中取
    if(!event){
        event = window.event;
    }
    
    //event = event || window.event;
    
    //鼠标的坐标，IE8不支持event
    var x = event.clientX; 
    var y = event.clientY;
}
```

`clientX`和`clientY`是获取当前可见窗口的坐标

`pageX`和`pageY`是相对于当前页面的坐标，但是IE8不支持

##### 8.2.2.1.元素随鼠标移动

```html
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<style type="text/css" media="screen">
        body{
            position: absolute;
        }
        #move{
            width: 100px;
            height: 100px;
            position: absolute;
        }
    </style>
</head>
<body style="height:1000px;">
	<img id='move' class='img' src="img/(1).jpg" alt="(1).jpg">
	<script type="text/javascript">
			var move = document.getElementById("move");
        	 //此时给document绑定事件，防止其他元素影响
			document.onmousemove = function(event){
				//解决IE8兼容性问题
				event = event || window.event;
				
				//chrome把滚动条算作body的（document.body.scrollTop）
				//firefox等吧滚动条算作html的（ocument.documentElement.scrollTop）
				var scrollHight = document.body.scrollTop || document.documentElement.scrollTop;	

				move.style.left = event.clientX + 'px';
				move.style.top = event.clientY + scrollHight + 'px';

				//此方式IE8不支持
				//move.style.left = event.pageX + 'px';
				//move.style.left = event.pageY + 'px';
			}
		}	
	</script>
</body>
</html>
```

##### 8.2.2.2.元素的拖拽函数

```js
//设置拖拽的函数	
//ele 设置要拖拽的元素
function darg(ele){
    move.onmousedown = function(){
        var left = event.clientX - move.offsetLeft;	
        var top = event.clientY - move.offsetTop;

        //绑定鼠标移动事件	
        document.onmousemove = function(){
            var event = event || window.event;

            //处理IE8拦截浏览器的默认行为
            if(ele.setCaptrue){
                ele.setCaptrue();
            }
            //move.setCaptrue && move.setCaptrue();

            //设置元素随鼠标移动，且设置鼠标与元素的位置相对不变
            var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
            var scrollLeft = document.body.scrollLeft || document.documentElement.scrollLeft;

            ele.style.left = event.clientX + scrollLeft - left + 'px';
            ele.style.top = event.clientY + scrollTop  - top + 'px';

            //阻止浏览器的默认行为（默认搜索选中的文字）
            return false;
        }

        //绑定事件松开事件
        document.onmouseup = function(){
            //取消鼠标移动事件
            document.onmousemove = null;
            //取消鼠标松开事件
            document.onmouseup = null;

            if(ele.releaseCaptrue){
                ele.releaseCaptrue();	
            }
        }
    }
}
```

```js
var move = document.getElementById("move");
darg(move);
```

`ele.setCapture()`此时元素会把下一次所有的对应事件（绑定的那个事件）捕获到自身上，只有IE支持

`ele.releaseCapture()`取消对元素的捕获

##### 8.2.2.3.鼠标的滚轮事件

- `onmousewheel`鼠标滚轮事件，但是火狐不支持该事件
- 在火狐中使用`DOMMouseScroll`来绑定滚动事件，该事件只能通过addEventListener()函数来绑定
- `event.wheelDelta`获取鼠标滚轮滚动的方向，向上滚120，向下滚-120，此值不看大小只看正负，火狐不支持
- `event.detail`火狐中获取滚动的方向，向上滚-3，向下滚3

```js
var move = document.getElementById("move");

move.onmousewheel = function(event){
    var event = event || window.event;

    //滚轮向上滚
    if(event.wheelDelta > 0 || event.detail < 0){
        move.style.height = move.clientHeight - 10 + 'px';
    }else{
        move.style.height = move.clientHeight + 10 + 'px';
    }	

    //取消addEventListener()绑定事件的默认行为
    event.preventDefault && event.preventDefault();

    //取消浏览器的滚动的默认行为（滚动条跟着一起滚动了 ）	
    return false;
};

//兼容火狐
bind(move,'DOMMouseScroll',move.onmousewheel);
```

```js
function bind(obj,eventStr,callback){
    if(obj.addEventListener){
        obj.addEventListener(eventStr, callback,false);	
    }else{
        //兼容IE8,并统一this对象
        obj.attachEvent('on'+eventStr,function(){
            //自己在匿名函数中调用回调函数，并修改this
            callback.call(obj);
        });	
    }
}
```



> 通过addEventListener()绑定的事件时，不能通过`return false`来阻止默认行为，此时需使用`event.preventDefault()`来取消默认行为

##### 8.2.2.4.键盘事件

`onkeydown`：按键被按下，若一直按住按键，则事件会一直触发

- `keyCode`：判断哪个按键被按下
- `altKey`：判断alt是否按下
- `shiftKey`：判断shift是否按下
- `ctrlKey`：判断ctrl是否按下

```js
if(event.ctrlKey && event.keyCode === 89){
	//判断ctrl+y是否同时按下
}
```

```js
var name = document.getElementById("name");
name.onkeydown = function(event){
    var event = event || window.event;
    if(event.keyCode >= 48 && event.keyCode <= 57){
        //阻止键盘输入数字
        return false;
    }
}
```

`onkeyup`：按键被松开

注意：键盘事件一般绑定给能获取到焦点的对象或者document



#### 8.2.3.事件冒泡

事件冒泡（Bubble)指的是事件向上传导，当后代元素的事件触发时，其祖先元素的相同事件也会被触发。

```js
next.onclik = function(event){
    event = event || event.window;

    //取消事件冒泡
    event.cancelBubble = true;
}
```

#### 8.2.4.事件的委派

我们希望只绑定一次事件，即可应用到多个元素上，即使是后添加的元素；

事件委派：指事件统一绑定给元素共同的祖先元素，这样后代元素的事件触发时，会一直冒泡到祖先元素，从而通过祖先元素的响应函数来处理事件。存在的问题：点击祖先元素也会触发事件

`event.target`：触发事件的元素

```html
<div id="outer">
	<img id='contaner' class='img' src="img/(1).jpg" alt="(1).jpg">
	<img class='img' src="img/(11).jpg" alt="(11).jpg">
	<img class='img' src="img/(19).jpg" alt="(19).jpg">
	<img class='img' src="img/(2).jpg" alt="(2).jpg">
</div>
```

```js
var outer = document.getElementById("outer");
outer.oncilk = function(event){
    var event = event || window.event;
    //判断是否是指定元素触发事件
    if(event.taregt.className == 'img'){ //多个class会出问题
        console.log('是图片触发了事件');
    }
}
```

#### 8.2.5.事件的传播

微软认为事件应该由内向外传播，即事件触发时，应该先触发当前元素上的事件，在向当前元素的祖先元素传播，也就是说事件应该在冒泡阶段执行。

网景认为事件应该由外向内传播，即事件触发时，应该先触发当期元素的最外层祖先元素的事件，然后在向内传播事件。

W3C综合了两个公司的方案，将事件的传播分成了三个阶段：

1. 捕获阶段：从最外层的祖先元素向目标元素进行事件的捕获，但是默认此时不会触发事件
2. 目标阶段：事件捕获到目标元素，捕获结束后在目标元素上触发事件
3. 冒泡阶段：事件从目标元素向它的祖先元素传递，依次触发祖先元素上的事件

> 若希望在事件的捕获阶段触发事件，将addEventListener()的第三个参数设置为true即可，在IE8中没有捕获阶段

### 8.3.操作元素的方法和属性

- `innerHTML`获取元素内部的HTML代码
- `innerText`获取元素内部文本内容，即去除了html标签
- 读取元素节点的属性

```html
<button id='btn' class='print inner'>保存</button>
```

```js
var btn = docment.getElementById('btn');
var name = btn.name; //获取name的属性值
//注意:读取class属性的值不能使用ele.class
var clazz = btn.className; //获取class属性值
```

- 获取元素节点的后代节点
  - `getElementByTagName()`：获取当前节点指定标签的后代节点
  - `childNodes`：获取当前节点的所有子节点(包含文本节点);空白会当做文本节【》IE9】
  - `firstChild`：获取当前节点的第一个子节点（包含空白节点）
  - `lastChild`：获取当前节点的最后一个子节点（包含空白节点）
  - `children`：获取当前元素的所有子元素，只会获取元素节点
  - `firstElementChild`：获取元素的第一个子元素，不包含空白节点》IE9】
- 获取父节点和兄弟节点
  - `parentNode`：获取当前节点的父节点
  - `previousSibling`：获取当前节点的前一个兄弟节点（包含空白节点）
  - `nextSibling`：获取当前节点的后一个兄弟节点（包含空白节点）
  - `previousElementSibling`:获取当前节点的前一个兄弟元素【》IE9】
  - `nextElementSibling`：获取当前节点的后一个兄弟元素【》IE9】

```js
item[i].checked = !item[i].checked; //反选，即取反
item[i].checked = this.checked; //复选框全选
```

- 获取元素

  - `docment.body`:获取body标签
  - `docment.docmentElement`：获取html标签
  - `docment.all`：获取页面所有的元素，等同于`docment.getElementByTagName('*')`
  - `docment.getElementByName('')`：根据class属性获取一组元素【》IE9】
  - `docment.querySeletor()`：根据css选择器获取元素，只会返回匹配的第一个元素
  - `docment.querySelectorAll()`：根据css选择器获取所有匹配的元素

- `DOM`增删改

  - `createElement('li')`：创建一个元素节点
  - `createTextNode('')`：创建一个文本节点
  - `appendText()`：向一个父节点添加一个新子节点
  - `insertBefore()`：在子节点前面插入新的子节点
  - `replaceChild(new,old)`：替换子节点
  - `removeChild(子节点)`：删除子节点`ele.parentNode.removeChild(ele)`
  - 使用`innerHTML`添加元素

  ```js
  var ele = docment.getElementById('ele);
  ele.innerHTML += '<li>成都</li>';                                 
  ```

  ```js
  //添加元素的推荐方式
  var ele = docment.createElement('li');
  ele.innerHTML += '成都';
  var city = docment.getElementById('city');
  city.appendChild(ele);
  ```

- 取消超链接`<a>`的默认行为

  - `<a href='javascript:;'></a>`
  - 在点击事件的响应函数中`return false;`

- 操作元素的内联样式`ele.style.样式名`

  - 若样式中包含为`-`，需使用驼峰命名`backgroundColor`
  - 只能设置和读取内联样式，无法读取样式表中的样式

- IE获取元素当前显示的样式

  - `ele.currentStyle.样式名`：读取元素当前显示的样式
  - 只有IE支持该属性
  - 获取的样式没有设置，会获取默认值(如`auto`)，而不是真实值

- 其他浏览器获取元素的样式【》IE9】

  - `getComputedStyle()`：获取元素当前的样式
  - 属于window对象的方法，可直接使用
  - 参数：第一个要获取样式的元素；第二个可以传递一个伪元素，一般为null
  - 返回当前元素的所有样式的对象
  - 获取的样式没有设置，会获取真实的值，而不是默认值

```js
var ele = document.getElementById('contaner');
var styles = getComputedStyle(ele, null);
console.log(styles.color);
```

- 定义通用获取样式的方法

```js
//ele要获取样式的元素,name要获取的样式名
function getStyle(ele,name){
    //判断window对象中是否有此属性getComputedStyle
    if(window.getComputedStyle){
        return getComputedStyle(ele, null)[name];
    }else{
        return ele.currentStyle[name];
    }

    //return window.getComputedStyle?getComputedStyle(ele, null)[name]:ele.currentStyle[name];
    
    /*if(ele.currentStyle){//此时在IE9以上会优先使用currentStyle
		return ele.currentStyle[name];
	}else{
		return getComputedStyle(ele, null)[name];
	}*/
}
```

注意：`if(window.getComputedStyle)`是判断window对象中是否有getComputedStyle属性，没有返回undefined;而`if(getComputedStyle)`判断是变量getComputedStyle，若没有就没报错

- 其他样式相关的属性

  - `clientWidth`：元素的可见宽度
  - `clientHieght`：元素的可见高度
    - 获取的值没有px，即如100
    - 获取的值包含内容区和内边距
    - 只能只读，不能修改
    - 有滚动条时，也会去除滚动条的高度
  - `offsetWidth`：元素的宽度
  - `offsetHeight`：元素的高度
    - 包含内容区、内边距和边框
    - 获取的值没有px，即如100
    - 只读
  - `offsetParent`：获取当前元素定位的父元素
    - 即获取当前元素最近的祖先元素（开启了定位）
    - 若没有开启定位的祖先元素，就返回body
  - `offsetLeft`：当前元素相对于定位元素的水平偏移量
  - `offsetTop`：当前元素相对于定位元素的垂直偏移量
  - `scrollHeight`：获取元素滚动区域的高度
  - `scrollWidth`：获取元素滚动区域的宽度
  - `scrollTop`：获取滚动条水平滚动的距离
  - `scrollLeft`：获取滚动条垂直滚动的距离

  ```js
  scrollHeight - scrollTop = clientHieght；//表示垂直滚动到底了
  //可用于协议阅读完了
  ```

- `innerheight、innerwidth`：获取页面窗口的大小
- `outerWidth、outerHeight `：获取浏览器本身的尺寸
- 



## 九、浏览器

### 9.1.浏览器的默认行为

1. 当浏览器有滚动条时，滚动鼠标时会默认滚动滚动条



## 十、`BOM`

### 10.1.`Window`

代表整个浏览器的窗口，同时网页中的全局对象

#### 10.1.1.定时器

`setInterval(function(){},1000)`：每个一段时间执行一次，会执行多次，返回值是定时器的唯一标识

`clearInterval(timer)`：关闭定时器，可以接收任意参数，有效参数就执行，无效的参数就不理睬

```js
//自动切换图片
var imgs = ['(1).jpg','(29).jpg','(3).jpg','(30).jpg','(32).jpg','(36).jpg'];
var move = document.getElementById("move");
var index = 1;
setInterval(function(){
    index ++;
    //index = index % imgs.length;
    if(index >= imgs.length){
        index = 0;
    }
    move.src = 'img/' + imgs[index];
},1000);	
```

注意：`setInterval()`绑定在点击事件中时，可能会每点击一次就会增加一个定时器

```js
///////////////////元素移动按方向键///////////////////
//速度
var speed = 10;
//方向
var dir = 0;	
//使用定时器控制速度，可防止按键第一次按下时的一个停顿
setInterval(function(){
    switch (dir) {
        case 37: //向左
            move.style.left = move.offsetLeft - speed + 'px';
            break;
        case 38://向上
            move.style.top = move.offsetTop - speed + 'px';
            break;
        case 39://向右
            move.style.left = move.offsetLeft + speed + 'px';
            break;
        case 40://向下
            move.style.top = move.offsetTop + speed + 'px';
            break;
        default:
            break;
    }

},30);

document.onkeydown = function(event){
    event = event || window.event;

    //设置移动的方向
    dir = event.keyCode;

    if(event.ctrlKey){
        speed = 500; //加速
    }else{
        speed = 10;
    }
}

//按键松开时停止
document.onkeyup = function(){
    dir = 0;
}
```

> 定时调用实现延时调用效果

```js 

```



#### 10.1.2.延时调用

`setTimeout(function(){},1000)`：函数不马上执行，而是隔一段时间以后再执行，且只会执行一次，返回值是定时器的唯一标识

`clearTimeout(timer)`：关闭延时调用

> 延时调用实定时调用效果

```js

```

#### 10.1.3.动画效果

```js
/**
		 * 动画移动
		 * ele：要操作的元素
		 * attr:要修改的样式,可以是left、top、width、hight
		 * target:目标的距离
		 * speed：速度
		 * callback：回调函数
		 */
function move(ele,attr,target,speed,callback){
    //关闭当前元素上的原来的定时器，元素停止自己的定时器
    clearInterval(ele.timer);
    //获取当前元素的位置，截取字符串（10px）中有效的数字
    var current = parseInt(getStyle(ele,attr));
    //当前位置大于目标位置就向左移动
    if(current > target){
        speed = -speed;
    }

    ele.timer = setInterval(function(){
        //获取当前元素的位置
        var oldValue = parseInt(getStyle(ele,attr));
        //在当前值上修改
        var newValue = oldValue + speed;	

        //向左移动时，判断newValue是否小于target
        //向右移动时，判断newValue是否大于target
        if((speed < 0 && newValue < target)||(speed > 0 && newValue > target)){
            newValue = target; //即超出目标target时，修改为target
        }

        ele.style[attr] = newValue + 'px';

        //到达目标位置，停止动画
        if(newValue === target){
            clearInterval(ele.timer);
            //执行完毕后的回调函数
            callback && callback();
        }
    }, 30);
}

//ele要获取样式的元素,name要获取的样式名
function getStyle(ele,name){
    //判断window对象中是否有此属性getComputedStyle
    if(window.getComputedStyle){
        return getComputedStyle(ele, null)[name];
    }else{
        return ele.currentStyle[name];
    }
}
```

```html
<button id="start" class='start' type="button">开始</button>
<img id='move' class='img' src="img/(1).jpg" alt="(1).jpg">
 
<script type="text/javascript">    
    var ele = document.getElementById("move");
    var start = document.getElementById("start");

    start.onclick = function(){
        move(ele,'width',800,1000,function(){
            move(ele,'height',400,1000,function(){
                move(ele,'top',0,10,function(){

                });
            });	
        });
    }
</script>
```

#### 10.1.4.图片的轮播

```html
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>图片轮播</title>
	<style type="text/css" media="screen">
	ul{
		margin:0px;
		padding: 0px;
	}
	.img{
		width: 500px;
		height: 400px;
		margin-right:8px; 
	}	
	#outer{
		width: 500px;
		height: 400px;
		margin:0px auto;
		text-align: center;
		background: #647334;
		border: 8px #736473 solid;
		border-radius: 4px;
		position: relative;
		overflow: hidden;
	}
	#imgList{
		list-style: none;
		position: absolute;
		left:0;
		/* width: 5500px; */
	}
	#imgList li{
		float: left;
		/* display: inline-block; */
	}

	/*设置导航*/
	#nav{
		position: absolute;
		/*设置导航居中*/
		bottom:15px;
	}
	#nav a{
		width: 15px;
		height: 15px;
		float: left;
		margin:0px 5px;
		background: red;
		/*设置透明*/
		opacity: 0.5;
		/*兼容IE8*/
		filter: alpha(opacity=50);
	}
	/*移入的效果*/
	#nav a:hover{
		background: #222;
	}
</style>
</head>
<body >
	<div id="outer">
		<ul id="imgList">
			<li><img id='contaner' class='img' src="img/(1).jpg" alt="(1).jpg"></li>
			<li><img class='img' src="img/(11).jpg" alt="(11).jpg"></li>
			<li><img class='img' src="img/(19).jpg" alt="(19).jpg"></li>
			<li><img class='img' src="img/(2).jpg" alt="(2).jpg"></li>
			<li><img class='img' src="img/(24).jpg" alt="(24).jpg"></li>
			<li><img class='img' src="img/(29).jpg" alt="(29).jpg"></li>
			<li><img class='img' src="img/(3).jpg" alt="(3).jpg"></li>
			<li><img class='img' src="img/(30).jpg" alt="(30).jpg"></li>
			<li><img class='img' src="img/(1).jpg" alt="(1).jpg"></li>
		</ul>
		<!-- 导航按钮-->
		<div id="nav" class="nav">
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
			<a href="javascript:;"></a>
		</div>
	</div>
	<script type="text/javascript">
		window.onload = function(){
			//获取ul
			var imgList = document.getElementById("imgList");
			//获取所有的图片
			var imgArr = imgList.getElementsByTagName('img');
			//设置ul的宽度
			imgList.style.width = imgArr[0].parentNode.offsetWidth * imgArr.length + 'px';
			//设置导航居中
			var nav = document.getElementById("nav");
			var outer = document.getElementById("outer");
			nav.style.left = (outer.offsetWidth - nav.offsetWidth) / 2 + 'px';
			//设置默认选中第一个a
			var allA = nav.getElementsByTagName("a");
			var index = 0; //选中的索引
			allA[index].style.backgroundColor = '#222'; 
			//点击超链接切换图片
			for(var i = 0 ; i < allA.length ; i++){
				//给每个超连接添加属性
				allA[i].index = i;
				allA[i].onclick = function(){
					//关闭自动切换的定时器
					clearInterval(timer);
					 //获取超链接的索引
					 index = this.index;
					 //切换图片
					 //imgList.style.left = -imgArr[0].parentNode.offsetWidth * index + 'px';
					 move(imgList,'left',-imgArr[0].parentNode.offsetWidth * index,20,function(){
					 	//动画执行完毕后，开启动画切换
					 	autoChange();
					 });

					 //设置超连接的样式
					 setAStyle(allA,index);
					 
					}
				}

			//开启自动切换
			autoChange();

			//设置选中超连接样式
			function setAStyle(allA,index){
				//判断是否是最后一张图片
				if(index >= imgArr.length - 1){
					index = 0;
					//此时一瞬间将最后一张替换为第一张
					imgList.style.left = '0px';
				}
				for(var i = 0 ; i < allA.length ; i++){
					//设置为空，是内联样式生效
					allA[i].style.backgroundColor = ''; 
				}
				allA[index].style.backgroundColor = '#222'; 
			}

			var timer;
			//自动切换图片
			function autoChange(){
				timer = setInterval(function(){
					index++;
					index %= imgArr.length;
					move(imgList,'left',-imgArr[0].parentNode.offsetWidth * index,20,function(){
					 	 //设置超连接的样式
					 	 setAStyle(allA,index);
						 //处理最后一张切换到第一张的效果

						});
				}, 3000);
			}		
		}

		//
		/**
		 * 动画移动
		 * ele：要操作的元素
		 * attr:要修改的样式,可以是left、top、width、hight
		 * target:目标的距离
		 * speed：速度
		 * callback：回调函数
		 */
		 function move(ele,attr,target,speed,callback){
			//关闭当前元素上的原来的定时器，元素停止自己的定时器
			clearInterval(ele.timer);
			//获取当前元素的位置，截取字符串（10px）中有效的数字
			var current = parseInt(getStyle(ele,attr));
			//当前位置大于目标位置就向左移动
			if(current > target){
				speed = -speed;
			}

			ele.timer = setInterval(function(){
				//获取当前元素的位置
				var oldValue = parseInt(getStyle(ele,attr));
				//在当前值上修改
				var newValue = oldValue + speed;	

				//向左移动时，判断newValue是否小于target
				//向右移动时，判断newValue是否大于target
				if((speed < 0 && newValue < target)||(speed > 0 && newValue > target)){
					newValue = target; //即超出目标target时，修改为target
				}

				ele.style[attr] = newValue + 'px';

				//到达目标位置，停止动画
				if(newValue === target){
					clearInterval(ele.timer);
					//执行完毕后的回调函数
					callback && callback();
				}
			}, 30);
		}

		//ele要获取样式的元素,name要获取的样式名
		function getStyle(ele,name){
		    //判断window对象中是否有此属性getComputedStyle
		    if(window.getComputedStyle){
		    	return getComputedStyle(ele, null)[name];
		    }else{
		    	return ele.currentStyle[name];
		    }
		}
	</script>
</body>
</html>
```





### 10.2.`Navigator`

代表浏览器的信息，通过对象可以识别不同的浏览器对象

- `userAgent`：浏览器的信息

> - 火狐
>
> `Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0`
>
> - Chrome
>
> `Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36`
>
> - IE8
>
> `Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3)`
>
> - IE9
>
> `Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3)`
>
> - IE10
>
> `Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3)`
>
> - IE11
>
> `Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3; rv:11.0) like Gecko`

#### 10.2.1.判断浏览器的类型

```js
function getBrowserType(){
    var ua = navigator.userAgent;
    if(/firefox/i.test(ua)){
        return "firefox";//火狐浏览器
    }else if(/chrome/i.test(ua)){
        return "chrome";//chrome浏览器
    }else if('ActiveXObject' in window){
        return "IE";//IE浏览器，通过判断浏览器的特有属性	
    }
}
```

注意：IE11中userAgent中没有的标识字段，故不能通过userAgent判断是否是IE；`if(ActiveXObject)`是否此种方式判断IE中的特有对象也不行，因为IE11中由此对象，但是返回false

### 10.3.`Location`

代表当前浏览器的的地址栏信息，通过Location可以获取地址栏信息，或者操作浏览器的跳转页面

```js
location.reload(true); //强制刷新，清空缓存信息 
location.replace(url);//替换当前页面，不会生成历史记录
```



### 10.4.`History`

代表浏览器的历史记录，由于隐私原因，不能获取到具体的历史记录，只能操作浏览器向前或向后翻页，且该操作只在当次有效

```js
history.back(); //回退
history.forward();//前进
history.go(2);//跳转到指定页面，整数向前跳转几个页面，负数向后跳转几个页面
```



### 10.5.`Screen`

代表用户的屏幕信息，通过该对象可以获取到用户的显示器相关信息

## 十一、类的操作

通过style修改样式，每修改一个样式，就要重新渲染一次页面

### 11.1.一行修改多个样式

通过修改class属性，间接的修改元素的样式，此种方式只会重新渲染一次

```css
.newClass{
    border:1px #232453 solid;
    width:100px;
    hieght:90px;
}
```

```js
ele.className = 'newClass';
```

此种方式会失去所有原来的样式，要在原来基础上新增样式

```js
ele.clasName += ' newClass';//注意：此处有一个空格
```

### 11.2.自定义操作class的函数

```js
//添加class样式
function addClass(ele,classname){
    if(!hasClass(ele,classname)){
        ele.className += ' '+classname; 
    }
}

//删除某一元素中指定的class属性
function removeClass(ele,classname){
    var reg = new RegExp('\\b'+classname+'\\b');
    ele.className = ele.className.replace(reg,"");
}

//若元素存在该class就删除，若不存在该class就添加
function toggleClass(ele,classname){
    if(hasClass(ele,classname)){
        removeClass(ele,classname);
    }else{
        addClass(ele,classname);
    }
}

//判断是否有某一个class样式
function hasClass(ele,classname){
    var reg = new RegExp('\\b'+classname+'\\b');
    return reg.test(ele.className);
}
```

### 11.3.二级菜单的实现



## 十二、`JSON`

- json是特殊格式的字符串，主要用来数据交换
- JSON与JS对象的格式一样，只不过JSON字符串中的属性名必须加双引号
- JSON的分类
  - JSON对象：`{}`
  - JSON数组：`[]`
- JSON对象允许的值
  - 字符串
  - 数值
  - 布尔值
  - null
  - 数组
  - 对象(不能包含函数)

### 12.1.JSON字符串转JS对象

```js
var json = '{"name":"xy","age":12}';
var js = JSON.parse(json);
```

### 12.2.js对象转json字符串

```js
var obj = {
	name:'xy',
    age:12
};
var json = JSON.stringify(obj);
```

注意：JSON在IE7及其以下的浏览器不支持

- `eval()`执行一段字符串形式的JS代码，并将执行结果返回
- 若使用`eval()`执行的字符串中包含`{}`，会将`{}`当做代码块
  - 若不希望将其当做代码块，可在字符串的前后各加一个`()`

```js
var json = '{"name":"xy","age":12}';
var obj = eval('('+json+')'); //可兼容IE7
```

注意：`eval()`执行效率低，且有安全隐患

若需兼容IE可引入一个外部文件

```html
<script type='text/javascript' src="js/json2.js"></script>
```

