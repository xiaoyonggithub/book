## 一、jQuery

### 1.1.jQuery的两把利器

jQuery的核心函数：`$或jQuery`

jQuery的核心对象：执行`$()`返回的对象

### 1.2.作为函数调用

1).参数为函数：当DOM加载完成后，执行此回调函数

```js
$(function(){
    
})
```

2).参数为选择器字符串：查找所匹配的标签，并将它们封装成jQuery对象

```js
$("#btn")
```

3).参数为DOM对象：将DOM对象封装为jQuery对象

```js
$(this).html()
```

4).参数为html标签字符串：创建标签对象并封装为jQuery对象

```js
$("<button id='save'>保存</button>")
```

### 1.3.作为对象使用`$.xxx()`

1).`$.each()`：隐式遍历数组

```js
var arr = [1,2,3,4];
$.each(arr,function(index,item){
    //index 索引
    //item 索引对应的值
});
```

```js
//遍历元素
$buttons.each(function(index,domEle){
    //this  调用函数的dom元素
    
})
```

2).`$.trim()`：去除两端的空格

```js
var str = '  str trim  ';
str.trim();
$.trim(str);
```

### 1.4.对象操作函数

1).`index()`：得到在所在兄弟元素中的下标 

```js
$("btn3").index()
```

2).伪数组

- Object对象
- length属性
- 数值的下标属性
- 没有数组特别的方法：forEach()、pop()、push()

```js
var $buttons = $("button")
console.log($buttons instanceof Array) //false
```

```js
var weiArr = {}
weiArr.length = 0
weiArr[0] = '伪数组'
weiArr.length = 1
weiArr[1] = 123
weiArr.length = 2

for(var i = 0 ; i< weiArr.length;i++){
    var obj = weiArr[i];
    console.log(i,obj);
}

console.log(weiArr.forEach,$buttons.forEach) //undefined undefined
```

### 1.5.选择器

```js
$('div.box')  //选择所有div且class为box，交集
```

选择第二个和第三个li元素

```js
$('li:gt(0):lt(2)')  //注意：是一步一步选择的
$('li:lt(3):gt(0)')
```

> 多个选择器是一次执行的

属性选择器

```js
$('input[type=text]') //属性值可以不加引号
$('input[type="text"]')
```

### 1.6.工具

1).`$.each()`

```js
var obj = {
    name : 'zhangsan',
    age : 12,
    setName : function(name){
        this.name = name;
    }
}
$.each(obj,function(key,val){
    
})
```

2).`$.type(obj)`得到数据的类型(typeof)

3).`$.isArray()`判断是不是数组

4).`$.isFunction()`判断是不是函数

5).`$.parseJSON(json)`解析json字符串转化为js对象/数组

### 1.7.属性操作

- `attr()`：操作属性值不为布尔值的属性
- `prop()`：操作属性值为布尔值的属性

### 1.8.CSS操作

- css()中的可以不用加`px`

- `offset()`：获取元素在当前视图的相对偏移，即`top`和`left`的值

```js
var offset = $('#inner').offset()
offset.top  offset.left   //相对于页面的位置
```

- `position()`：获取相对于父元素的位置

```js
var position = $('#inner').position();
position.top  position.left
```

- `scrollTop()`：获取滚动的高度
- `scrollLeft()`:获取滚动的宽度

```js
//获取页面的滚动高度，兼容IE/chrome
$('body').scrollTop() + $('html').scrollTop()
$(document.body).scrollTop() + $(document.documentElement).scrollTop()
//设置页面滚动的高度
$('html,body').scrollTop(height)
```

### 1.9.尺寸

1).内容尺寸

- `height()`：height
- `width()`：width

2).内部尺寸

- `innerHeight()`：height+padding
- `innerWidth()`：width+padding

3).外部尺寸

- `outerHeight(false/true)`：height+padding+border，若是true，加上margin
- `outerWidth(false/true)`：width+padding+border，若是true，加上margin

### 1.10.过滤

- `first()`
- `last()`
- `eq()`：取第几个元素
- `filter()`

```js
$lis.filter('[type=text]')
```

- `not()`

```js
$lis.not('[type!=text]')
```

- `has()`

```js
$lis.has('sapn')  //有span的li
```

### 1.11.查找

- `children()`：查找子元素
- `find()`：查找后代元素
- `parent()`：查找父元素
- `prev()`：查找前面的一个元素
- `prevall()`：查找前面的所有同辈元素
- `siblings()`：查找所有的兄弟元素

### 1.12.文档处理

- `appendTo()`

```js
$('<span>appendTo</span>').appendTo('#li') //可以直接传入选择器
$('<span>appendTo</span>').appendTo($('#li'))
```

### 1.13.事件

1).事件的绑定

- `eventName(function(){})`：绑定对应事件名的监听

```js
$('#div').click(function(){})
```

- `on(eventName,function(){})`：通用事件绑定，一些特殊事件没有单独是事件函数

```js
$('#div').on('click',function(){})
```

注意：两种方式都可以绑定同一个事件多次

2).事件解绑

- `off(eventName)`

```js
$('#div').off() //解除元素上所有的事件
$('#div').off('click')//解除元素上指定的事件
```

3).事件坐标

- `event.clientX 、event.clientY`：相对于窗口的左上角，原点为窗口左上角
- `event.pageX 、 event.pageY`：相对于页面的左上角，原点为页面左上角
- `event.offsetX 、event.offsetY`：相对于事件元素的左上角，原点为事件元素左上角

```js
$('#div').click(function(event){
    console.log(event.clientX,event.clientY);
})
```

4).事件相关处理

- 停止事件冒泡：event.stopPropagation()
- 阻止事件的默认行为：event.preventDefault()  

```js
$('#div').click(function(event){
    
    event.stopPropagation();
})
```

5).事件委托

- 将多个子元素的事件监听委托给父辈元素处理
- 监听回调是加在父辈元素上
- 当操作一个子元素时，事件会冒泡到父辈元素上
- 父辈元素不会直接处理事件，而是根据event.target得到发生事件的子元素，通过子元素来调用回调函数

> 事件委托的双方

- 委托方：业主 子元素 
- 被委托方：中介 父元素

> 事件委托的好处

- 添加新的子元素，可以自动的有事件响应
- 减少事件监听的数量:n==>1

> jQuery事件委托的API

- 设置事件委托：$(parentSeletor).delegate(childrenSeletor,eventName,callback)

```js
$('ul').delegate('li','click',function(){
    this  //是点击的事件的子元素
})
```

- 移除事件委托：$(parentSeletor).undelegate(eventName)



#### 1.13.1.`mouseover`与`mouseenter`的区别

- `mouseover`：在移入子元素也会触发事件，对应`mouseout(移出子元素也会触发事件)`

- `mouseenter`：只在移入当前元素才会触发事件，对应`mouseleave(只有移出当前元素才会触发事件)`
- `hover()`使用的是`mouseenter()`和`mouseleave()`

#### 





## 二、实例

### 2.1.表格隔行变色

```js

```



### 2.2.`Tab`页切换

```js

```



### 2.3.回到顶部

```html
<div id="top" style="position: fixed;bottom: 20px;right: 20px;height: 40px;width: 40px;background: #746758;text-align: center;padding: 5px;">回到顶部</div>
```

```js
$(function(){
    $('#top').click(function(){
        var $page = $('html,body')
        //总距离
        var distance = $('html').scrollTop() + $('body').scrollTop()
        //总时间
        var time = 500	
        //间隔时间
        var intervalTime = 50 
        var itemDistance = distance/(time/intervalTime)
        //使用循环定时器不断滚动
        var intervalId = setInterval(function(){
            distance -= itemDistance
            //到达顶部，定制定时器
            if(distance <= 0){
                distance = 0
                clearInterval(intervalId)
            }
            $page.scrollTop(distance)
        },intervalTime)
        })
})
```

### 2.4.反选

![1538057391843](E:\typora\images\1538057391843.png)

```js
var $items = $(':checked')
var $checkedAllBox = $('#checkedAllBox')
$('#checkedRevBtn').click(function(){
    $items.each(function(){
        //反选
        this.checked = !this.checked;
    })	
    //全选按钮的处理,$items.not(':ckecked').lengthh获取没有的选中的
    //若有没有选中的，就不选中全选
    $checkedAllBox.prop('checked',$items.not(':ckecked').length === 0)
})
```





