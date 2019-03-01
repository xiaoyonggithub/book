# 一、html

## 1.1. 图片的格式类型

- `png`：png支持的颜色多，并支持复杂的透明，可显示颜色复杂的透明图片
- `gif`：支持的颜色比较少，只支持简单的透明，支持动态图
- `jpg`：jpg图片支持的颜色比较多，图片可以压缩，但是不支持透明，一般用来保存图片

## 1.2.`meta`

- `meta`用来设置网页的关键字

```html
<meta name="keywords" content="HTML5,前端"/>
```

- 指定网页的描述

```html
<meta name="discription" content="发布HTML5相关的内容"/>
```

注意：搜索引擎在检索页面的时候，会检索关键字和描述，不会影响页面在搜索引擎的排名

- 请求的重定向

```html
<meta http-equiv="refresh" content="5;url=http://www.baidu.com">
```

## 1.3.`xHtml`

- HTML中不区分大小写，一般使用小写
- HTML中的注释不能嵌套
- HTML的结构必须完整，要么成对出现，要么自结束标签
- HTML可以嵌套，但是不能交叉嵌套
- HTML标签的属性必须有值，且值必须有引号

## 1.4.内联框架

内联框架不会被搜索引擎所检索

```html
<a href="https://www.bilibili.com/" target="baidu">在内联框架打开页面</a>
<iframe src="http://www.baidu.com" name="baidu"></iframe>
```

## 1.5.超连接

- 设置超连接为`href="#"`，点击后会跳转到当前页面的顶部

```html
<a href="#">去顶部</a>
```

- 设置去指定位置，`href="#id"`

```html
<a href="#bottom">去底部</a>
<a id="bottom" href="">联系方式</a>
```

```html
<a href="#center">去底部</a>
<p id="center">
    中间位置
</p>
```

- 发送电子邮件的超连接，点击超连接后可自动打开计算机中默认的邮件客户端

```html
<a href="mailto:15181278770@163.com">联系我们</a>
```

## 1.6.文本标签

- `<em>`：表示语气上的强调，默认显示为斜体
- `<strong>`：表示内容上的强调，默认显示为加粗

- `<i>`：斜体显示，没有语义
- `<b>`：加粗显示，没有语义
- `<small>`：显示的内容会比父元素的文字小一些，h5中表示一些细则的内容，如合同中的小字、网站的版权
- `<big>`：显示的内容比父元素的文字打一些，没有语义，已淘汰
- `<cite>`：表示参考的内容，所有加书名号的内容(书名，电影名，歌名...)都可以使用cite，显示为斜体
- `<q>`：短引用，表示一个行内引用，会默认加上`""`显示，样式不统一，一般会去除
- `<blockquote>`：块级引用，表示一个长引用，显示会独占一行
- `<sub>`：下标
- `<sup>`：上标
- `<ins>`：表示一个插入的内容，显示内容会有下划线
- `<del>`：表示一个已删除的内容，自动添加删除线
- `<pre>`：预格式内容，会将代码中的格式保存，不忽略空格
- `<code>`：表示代码，但是不会保留格式

```html
<code>
    <pre>
    window.onload = function(){
    	//显示代码
    }
    </pre>
</code>
```

## 1.7.列表标签

- 无序列表
  - `type`：设置项目符号，不同浏览器中显示不统一，故一般不使用
    - `disc`：默认值，实心圆点
    - `square`：实心小方块
    - `circle`：空心的圆

```html
<ul>
    <li></li>
    <li></li>
</ul>
```

- 有序列表
  - `type`：设置项目符号
    - `1`：使用阿拉伯数字
    - `a`：使用小写字母
    - `A`：使用大写字母
    - `i/I`：使用小写或大写的罗马数字

```html
<ol>
     <li></li>
    <li></li>
</ol>
```

- 定义列表：用来对一些词汇或内容定义
  - `<dt>`：定义的内容
  - `<dd>`：对定义内容的描述

```html
<dl>
    <dt></dt>
    <dd></dd>
</dl>
```

注意：列表之间可以相互嵌套

## 1.8.块元素

- `ul、li、ol`



## 1.9.长度单位

- `px`：像素
- `百分比`：可将单位设置为百分比，相对于父元素
- `em`：相对于当前元素的字体大小来计算，`1em=1font-size`，设置字体相关的样式，一般使用`em`

## 1.10.颜色单位

- 颜色的单词表示颜色，`red、blue`
- `RGB`：通过三原色`red、blue、green`的不同浓度表示颜色，`rgb(225,0,0),rgb(100%,0%,0%)`
  - 格式：`rgb(红色的浓度，绿色的浓度，蓝色的浓度)`
- 使用16进制的`rgb`值表示颜色，语法：`#红色绿色蓝色`，`#726372`
  - 像两位两位重复的颜色，可以简写`#ff0000`-->`#f00`

## 1.11.表格

- `border-spacing`：table与td边框之间的距离
- `border-collapse:collapse`：设置表格边框的合并
  - 设置了`border-collapse`，`border-spcing`自动失效

```css
table{
    border-collapse: collapse;
}
td,th{
    border:1px solid #222;
}
tr:nth-child(even){
    background-color:green;
}
tr:hover{
    background-color:#baf;
}
```

- 如没有写`tbody`，浏览器会自动添加`tbody`

## 1.12.表单

- `form`
  - `action`：服务器地址
- `input`
  - `name`：提交内容的名称
  - `type="radio"`：单选按钮
    - `name`相同的为一组
    - `value`会将该值提交给服务器，不设置时选中时默认为`on`
    - `checked="checked"`设置默认选中
  - `type="checkbox"`：多选框
    - `name`相同的为一组、
    - `value`会将该值提交给服务器，不设置时选中时默认为`on`

- `select`：下拉列表
  - `name`：设置select的名称
  - `multiple="multiple"`：设置下拉选可选择多个
  - `option`：下拉项
    - `value`：设置每个选项的值
    - `selected="selected"`：设置默认选中
  - `optgroup`：给`option`分组
    - `label`：设置分组的名称

```html
<select name="mx" id="mx">
    <optgroup label="男明星">
        <option value="zbs">赵本山</option>
        <option value="ldh">刘德华</option>
        <option value="wf">汪峰</option>
    </optgroup>
    <optgroup label="女明细">
        <option value="lxr">林心如</option>
        <option value="lyf">刘亦菲</option>
        <option value="fbb">范冰冰</option>
    </optgroup>
</select>
```

![1538925256915](E:\typora\images\1538925256915.png)

- `fieldset`：为表单项分组
  - `<legend>`：设置组名

```html
<fieldset>
    <legend>性别</legend>
</fieldset>
```

![1538925608760](E:\typora\images\1538925608760.png)

- `placeholder`在IE8及其以下浏览器不支持,若要兼容IE8可使用JS
  - 

## 1.13.框架集

- `frameset`：
  - `frameset`和`body`不能出现在一个页面中

```html
<frameset cols="30% , * , 30%">
    <!-- 在frameset中使用frame子标签来指定要引入的页面 
引入几个页面就写几个frame
-->	
    <frame src="01.表格.html" />
    <frame src="02.表格.html" />
    <!-- 嵌套一个frameset -->
    <frameset rows="30%,50%,*">
        <frame src="04.表格的布局.html" />
        <frame src="05.完善clearfix.html" />
        <frame src="06.表单.html" />
    </frameset>
</frameset>
```

注意：`frameset`和`iframe`里面的内容不能被搜索引擎所检索；使用`frameset`的页面不能有自己的内容，只能引入框架页，而每次单独加载该页面时，浏览器需要重新去请求一次，引入几个页面就需要重新发送几次请求





# 二、实例

## 2.1.`table`固定列和表头

-  实现`table`固定列和表头，中间可横向滚动
-  高度自适应等





# 三、`Web Components`

- 一种标准化的非侵入的方式封装一个组件，每个组件能组织好它自身的 HTML 结构、CSS 样式、JavaScript 代码，并且不会干扰页面上的其他代码著作权归作者所有。

```html
<template>
  <h1>This won't display!</h1>
  <script>alert("this won't alert!");</script>
</template>
document.body.appendChild(template.content.cloneNode(true));
```







## 3.1.`shadow DOM`

- `shadow DOM`:开发者能通过 shadow DOM 在文档流中创建一些完全独立于其他元素的子 DOM 树（sub-DOM trees），由此我们可以封装一个具有独立功能的组件，并且可以保证不会在不无意中干扰到其它 DOM 元素
  - shadow DOM 和标准的 DOM 一样，可以设置它的样式，也可以用 JavaScript 操作它的行为著作权归作者所有
  - 主文档流和基于 shadow DOM 创建的独立组件之间的互不干扰，所以组件的复用也就变得异常简单方便

- 一组JavaScript API，用于将封装的“影子”DOM树附加到元素（与主文档DOM分开呈现）并控制其关联的功能



## 3.2.`Custom elements`

- 一组JavaScript API，允许您定义custom elements及其行为，然后可以在您的用户界面中按照需要使用它们

```js
class GreetingElement extends HTMLElement {
  constructor() {
    super();
    this._name = 'Stranger';
  }
  connectedCallback() {
    this.addEventListener(
      'click', e => alert(`Hello, ${this._name}!`)
    );
  }
  attributeChangedCallback(attrName, oldValue, newValue) {
    if (attrName === 'name') {
      if (newValue) {
        this._name = newValue;
      } else {
        this._name = 'Stranger';
      }
    }
  }
}
GreetingElement.observedAttributes = ['name'];
customElements.define('hey-there', GreetingElement);
```







## 3.3.`HTML Templates`

`<template>`和 `<slot>`元素使您可以编写不在呈现页面中显示的标记模板。然后它们可以作为自定义元素结构的基础被多次重用



## 3.4.`HTML Imports`

一旦定义了自定义组件，最简单的重用它的方法就是使其定义细节保存在一个单独的文件中，然后使用导入机制将其导入到想要实际使用它的页面中



使用ECMAScript 2015类语法创建一个类，来指定web组件的功能(参阅类获取更多信息)。
使用CustomElementRegistry.define()方法注册您的新自定义元素 ，并向其传递要定义的元素名称、指定元素功能的类以及可选的，其所继承自的元素。
如果需要的话，使用Element.attachShadow()方法将一个shadow DOM附加到自定义元素上。使用通常的DOM方法向shadow DOM中添加子元素、事件监听器等等。
如果需要的话，使用<template> 和 <slot>方法定义一个HTML模板。再次使用常规DOM方法克隆模板并将其附加到您的shadow DOM中。
在页面任何您喜欢的位置使用自定义元素，就像使用常规HTML元素那样。