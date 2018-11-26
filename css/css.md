# 一、元素

## 1.1.内联元素和块元素

- 块元素主要用于页面的布局，内联元素主要用于选中文字设置样式
- 一般只适用块元素包含内联元素，而不使用一个内联元素包含块级元素
- `a`元素可以包含任意元素（包括块级元素），但是除了本身
- `p`元素不能包含其他的块元素

## 1.1.1.块元素

块元素会独占一行，块元素`h1,h2,h3,h4,h5,h6,p,div,ul,li,dl,li,dd,`

## 1.1.2.内联元素

- 内联元素（行内元素）值占自身大小，不会占用一行
- 内联元素：`a,img,iframe,span`

## 1.2.伪类

**伪类** 用于当已有元素处于的某个状态时，为其添加对应的样式，这个状态是根据用户行为而动态变化的。

比如说，当用户悬停在指定的元素时，我们可以通过:hover来描述这个元素的状态。

虽然它和普通的css类相似，可以为已有的元素添加样式，但是它只有处于dom树无法描述的状态下才能为元素添

加样式，所以将其称为伪类。 

## 1.3.伪元素

譬如::before和::after伪元素，用于在CSS渲染中向元素的头部或尾部插入内容，它们不受文档约束，也不影响

文档本身，只影响最终样式。这些添加的内容不会出现在DOM中，仅仅是在CSS渲染层中加入。

它不存在于文档中，所以JS无法直接操作它。而jQuery的选择器都是基于DOM元素的，因此也并不能直接操作伪元素。 

| 伪元素            | 描述                                                         |
| ----------------- | ------------------------------------------------------------ |
| ` :first-letter ` | 向文本的第一个字母添加特殊样式                               |
| ` :first-line `   | 向文本的首行添加特殊样式                                     |
| ` :before `       | 在元素之前添加内容                                           |
| ` :after `        | 在元素之后添加内容                                           |
| ` :placeholder `  | 匹配占位符的文本，只有元素设置了placeholder属性时，该伪元素才能生效 |
| ` :selection `    | CSS伪元素应用于文档中被用户高亮的部分（比如使用鼠标或其他选择设备选中的部分） |
| ` ::backdrop`     | 用于改变全屏模式下的背景颜色，全屏模式的默认颜色为黑色。<br/>该伪元素只支持双冒号的形式(处于试验阶段) |

## 1.4.元素默认样式

- `h1,h2,h3,h4,h5,h6`都有默认的margin值

- `div`元素没有任何语义，且没有没有默认样式，主要用于页面布局

- `span`元素没有任何语义，也没有默认样式，主要用于选中文字，然后为文字设置样式

- `<a>`

```css
a{
    text-decoration:underline;/*下划线*/
}
```



## 1.4.1.清除默认样式

```css
html, body, div, span, applet, object, iframe,
h1, h2, h3, h4, h5, h6, p, blockquote, pre,
a, abbr, acronym, address, big, cite, code,
del, dfn, em, img, ins, kbd, q, s, samp,
small, strike, strong, sub, sup, tt, var,
b, u, i, center,
dl, dt, dd, ol, ul, li,
fieldset, form, label, legend,
table, caption, tbody, tfoot, thead, tr, th, td,
article, aside, canvas, details, embed, 
figure, figcaption, footer, header, hgroup, 
menu, nav, output, ruby, section, summary,
time, mark, audio, video {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}
/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure, 
footer, header, hgroup, menu, nav, section {
	display: block;
}
body {
	line-height: 1;
}
ol, ul {
	list-style: none;
}
blockquote, q {
	quotes: none;
}
blockquote:before, blockquote:after,
q:before, q:after {
	content: '';
	content: none;
}
table {
	border-collapse: collapse;
	border-spacing: 0;
}
```

# 二、`CSS`

## 2.1.[空格的处理](http://www.ruanyifeng.com/blog/2018/07/white-space.html)

- 在html中文字前后或后面的空格会忽略，中间的连续空格会算作一个，这就是浏览器处理空格的基本规则
- 若希望空格空格原样输出，可使用`<pre>`或`&nbsp;`

- 对于制表符（`\t`）和换行符（`\r`和`\n`），浏览器会自动把这些符号转成普通的空格键处理

> ## `white-space`

- `normal`:默认值，浏览器以正常方式处理空格（即浏览器的默认方式处理）
- `nowrap`:文字超出了容器之后不会换行
- `pre`:按照`<pre>`标签的方式处理(即按原样输出)
- `pre-wrap`:基本按照`<pre>`标签的方式处理，但是超出容器宽度时，会发生换行
- `pre-line`:保留换行符，其他空格符号按浏览器的默认规则处理



# 五、浮动

## 5.1.浮动

- `float`：设置框是否浮动，即脱离的文档流
  - `none`
  - `left`：元素会立即脱离文档流，向左侧浮动
  - `right`：元素会立即脱离文档流，向右侧浮动
- 元素浮动以后，会尽量向页面的左上或右下浮动，直到遇到父元素的边框或者其他浮动元素
- 若浮动元素的上面是一个没有浮动的块元素，则浮动元素不会超过该块元素
- 若浮动的元素一行显示不下，就会换行显示
- 浮动的元素水平位置不会超过上一个兄弟元素位置，最多对齐
- 浮动的元素不会盖住文字，文字会自动环绕在浮动元素的周围
- 块元素脱离文档流后，高度和宽度都有内容撑开
- 内联元素脱离文档流后，成为了块元素，可设置宽高了

## 5.1.1.高度塌陷

在文档流中，父元素的高度由子元素撑开，当子元素浮动（脱离了文档流），父元素的高度就会塌陷

在页面中元素都有一个隐含的属性叫做`Block Formatting Context`，简称`BFC`，默认关闭

- 开启BFC后，元素会具有以下特性
  - 父元素的垂直外边距不会与子元素重叠
  - 元素不会被浮动元素所覆盖
  - 元素可以包含浮动的子元素
- 如何开启BFC
  - 设置元素浮动
    - 设置父元素也浮动后，可以撑开父元素了，但是会导致父元素的宽度丢失，和会导致下面元素上移
  - 设置元素绝对定位
  - 设置元素为`inline-block`
    - 会导致元素的宽度丢失
  - 将元素的`overflow`设置为一个非`visible`的值
  - 推荐方式：`overflow:hidden`，副作用最小
    - 副作用：在IE6及以下浏览器不支持BFC，但是具有另一个隐含的属性hasLayout
- 开启hasLayout
  - 将元素的zoom设置为1，zoom只在IE中支持
  - 若为符元素设置一个宽度，则会默认开启hasLayout
- 解决方式

```css
overflow:hidden;
zoom:1;
```

- `clear`解决高度塌陷

```html
<div class="outer">
    <div class="left"></div>
    <div class="clear"></div> <!--添加一个空白的div，由它来撑开父元素-->
</div>
```

```css
.outer{
    border:1px solid #fa5168;
}
.left{
    width: 100px;
    height: 100px;
    float: left;
    background-color: antiquewhite;
}
/*设置空白div清除浮动*/
.clear{
    clear: both;
}
```

```css
/*通过伪类添加空白元素，清除浮动，解决高度塌陷问题，不兼容IE6*/
.clearfix:after{
    content:"";
    display:block;
    clear:both;
}
/*兼容IE6*/
.clearfix{
    zoom:1;
}
```

```css
<!--添加去除浮动的样式clearfix-->
<div class="outer clearfix">
    <div class="left"></div>
</div>
```

> 使用table优化高度塌陷

```css
.clearfix:after{
    content:"";
    display:table;
    clear:both;
}
```

> 使用table解决父子元素外边距重叠问题

```css
.clearfix:before{
    content:"";
    display:table;
}
```

> 解决高度塌陷和父子元素外边距重叠

```css
.clearfix:before,
.clearfix:after{
    content:"";
    display:table;
    clear:both;
}
/*兼容IE6*/
.clearfix{
    *zoom:1;
}
```





## 5.2.清除浮动

```html
<div class="outer">
    <div class="div1">1</div>
    <div class="div2">2</div>
    <div class="div3">3</div>
</div>
```

```css
.outer{border: 1px solid #ccc;background: #fc9;color: #fff; margin: 50px auto;padding: 50px;}
.div1{width: 80px;height: 80px;background: red;float: left;}
.div2{width: 80px;height: 80px;background: blue;float: left;}
.div3{width: 80px;height: 80px;background: sienna;float: left;}
```

问题：这儿没有给最外层`.outer`设置高度（我们知道若它里面的元素不浮动，外层的高度会自动撑开），此时：

- 背景不能显示
- 边框不能撑开
- `margin `设置值不能正确显示 

原因：元素一旦浮动，就会脱离文档流，父级元素`div`就没有内容了

## 5.2.1.空标签清除浮动

```html
<div class="outer">
    <div class="div1">1</div>
    <div class="div2">2</div>
    <div class="div3">3</div>
    <div class="clear"></div>  <!--此元素用于清除浮动-->
</div>
```

```css
.clear{clear:both; height: 0; line-height: 0; font-size: 0}
```

## 5.2.2.父级元素定义 `overflow: auto`

```html
<div class="outer over-flow">  <!--这里添加了一个class(.over-flow),用于清除浮动-->
    <div class="div1">1</div>
    <div class="div2">2</div>
    <div class="div3">3</div>
</div>
```

```css
.over-flow{
    overflow: auto;
    zoom: 1;    /*zoom: 1; 是在处理兼容性问题 */
}
```

原理：使用`overflow`清除浮动需注意：只能使用`hidden`和`auro`来清除浮动，而`visible`无法清除浮动

## 5.2.3.`  :after `清除浮动

```css
 /*==for IE6/7 Maxthon2==*/
.outer {zoom:1;}   
/*==for FF/chrome/opera/IE8==*/
.outer:after {
    clear:both;       /*清除所有浮动*/
    content:'.';
    display:block;
    width: 0;
    height: 0;
    visibility:hidden;  /*作用是允许浏览器渲染它，但是不显示出来，这样才能实现清楚浮动*/
}   
```

原理：`.outer`利用其伪类`clear:after`在元素内部增加一个类似于`div.clear`的效果 

## 5.2.4.`<br>`清除浮动

```html
<div class="outer"> 
    <div class="div1">1</div>
    <div class="div2">2</div>
    <div class="div3">3</div>
    <br  clear="all">     <!--br标签自带的属性清除浮动-->
</div>
```

## 5.2.5.`clear`

- `clear`：设置元素哪一侧不允许浮动，可以清除其他浮动元素对当前元素的影响
  - `none`
  - `left`：清除左侧浮动元素对当前元素的影响
  - `right`：清除右侧浮动元素对当前元素的影响
  - `both`：清除两侧浮动元素对当前元素的影响

## 5.3.IE6双倍边距

- 在IE6中，当为一个向左浮动的元素设置左外边距，或者为一个向右浮动的元素设置右外边距时，这个边距将是设置值的2倍
- 解决方法：添加`display:inline;`

## 5.4.浮动元素的层级

- 浮动提升半个层级

- 一个元素分两层，上层盒模型相关的东西，下层是文字相关的东西（只需在浮动的时候考虑）





# 六、选择器

## 6.1.元素选择器

```css
p{
    
}
```

## 6.2.id选择器

```css
#id{
    
}
```

## 6.3.class选择器

```css
.outer{
    
}
```

## 6.4.通用选择器

```css
*{
    
}
```

## 6.5.选择器分组

同时选择多个选择器对应的元素

```css
p,span,.outer,#file{
    
}
```

## 6.6.复合选择器

选中同时满足多个条件的元素

```css
div.outer{ //即class="outer"的div
    
}
```

## 6.7.子元素选择器

```css
div>span{
    
}
```

## 6.8.后代元素

```css
div span{
    
}
```

## 6.9.伪类选择器

伪类专门用来表示元素的一种特殊状态

- 链接的4种状态
  - `a:link`：正常链接，即没有访问过的链接
  - `a:visited`：访问过的链接，只能设置字体颜色
    - 不支持半透明色，语法上支持，但是表现上要么纯色要么全透明
    - 设置颜色时，需要有一个默认的颜色，背景色才能呈现（只能重置，不能凭空设置）
    - `:visited`设置的样色不能获取
  - `a:hover`：鼠标滑过的链接
  - `a:active`：正在点击的链接

> 浏览器是通过历史记录来判断一个链接是否访问

- `:hover`和`:active`其他元素也支持，但IE6不支持
- `:focus`：获取焦点
- `:before`：指定元素前

```css
p:before{
    content:"我是在段落的最前面";
}
```



- `:after`：指定元素后
- `::selection`：选中的元素，注意：在火狐中需这样`::-moz-selection`

## 6.10.伪元素选择器

伪元素是用来表示元素中一些特殊的位置

- `:first-letter`：首字母
- `:first_line`：首行

## 6.11.属性选择器

```css
p[title]{
    
}
```

```css
input[type=text]{//属性type=text的input元素
    
}
```

```css
p[title^=ab]{ //属性title以ab开头的p元素
    
}
```

```css
p[title$=c]{//属性title以c结尾的元素
    
}
```

```css
p[title*=c]{//匹配属性中包含指定字符串的元素
    
}
```

- `[属性名~=属性值]`：
- `[属性名|=属性值]`：

## 6.12.子元素伪类

- `:first-child`：元素的第一个子元素

```css
p:first-child{//只有第一个元素是p元素，才会选中
    
}
```

- `:last-child`：元素的最后一个元素
- `:nth-child`：元素指定位置的子元素

```css
p:nth-child(2){//第二个p元素
    
}
```

```css
p：nth-child(even){//匹配偶数位置的元素
    
}
p:nth-child(odd){//匹配奇数位置的元素
    
}
```

- `:first-of-type`：元素第一个指定类型子元素

```css
p:first-of-type{
    
}
```

- `:last-of-type`：元素最后一个指定类型的元素
- `:nth-of-type`：元素指定位置的元素(指定类型)

> 区别：`:first-child`是在所有子元素中排序，而`:first-of-type`只在当前类型的子元素中排序

## 6.13.兄弟元素选择器

```css
span + p{  //后一个兄弟选择器，只能选中紧挨着的元素
    
}
```

```css
spn ~ p{
    //选中后面所有的兄弟元素
}
```

## 6.14.否定伪类

`:not(选择器)`从选中的元素中剔除出某些元素

```css
p:not(.outer){
    
}
```

# 七、样式

## 7.1.样式的继承

- 祖先元素的样式会被它的后代元素继承
- 不是所有的样式都能被继承
  - 背景相关的样式都不能被继承 
  - 边框相关的样式
  - 定位相关的样式

## 7.2.选择器的优先级

- 样式之间产生冲突时，最终采用那个选择器的样式，由选择器的优先级（权重）决定，优先级高的优先显示。

- 优先级的规则
  - 内联样式，优先级 1000
  - id选择器，优先级 100
  - 类和伪类，优先级 10 
  - 元素选择器，优先级 1
  - 通配`*`，优先级 0
  - 继承的样式，没有优先级，即没有设置样式时使用继承样式，设置了样式就使用设置的样式
- 当选择器包含多种选择器时，需将多种选择器的优先级相加，然后在相加

```css
p#outer{}//10+100，优先级高
#outer{}//100
```

- 注意：选择器优先级的计算不会超过它的最大权重值
- 若优先级相同，则使用靠后的样式，即后面的覆盖前面的
- 并集选择器的优先级是单独计算的

```css
p,.outer,#print{
    
}
```

- `!important`：设置优先级最高，但尽量避免使用(不利于后期维护，如不能通过js修改样式)

## 7.3.伪类的顺序

- `a:link`：正常链接，即没有访问过的链接
- `a:visited`：访问过的链接，只能设置字体颜色
- `a:hover`：鼠标滑过的链接
- `a:active`：正在点击的链接

```css
a:hover{
    color:green;
}
a:active{
    color:red;
}
```

- `a:link`和`a:visited`不能放在`a:hover`和`a:active`之后
- `a:active`不能放在`a:hover`之前
- 原因：伪类选择器的优先级是一样的，后面的样式会覆盖前面的样式

## 7.4.字体样式

- `font-size`：设置的并不是字体的大小
  - 在页面中，每个文字是处在一个看不见的框中，`font-size`就是设置的这个格的大小
  - 一般情况下文字都要比格子小一些，也有时或比格大
  - 根据字体的不同，显示效果也不同
- `font-family`：当采用某种字体时，若浏览器支持则使用该字体，若字体不支持，则使用默认字体
  - 浏览器使用的字体默认就是计算机的字体
  - 字体文字`C:\Windows\Fonts`

```css
font-family:arial,宋体；
```

 - `font-style`：设置字体的样式
    - `italic`：文字以斜体显示
    - `oblique`：文字以倾斜效果显示
    - 但大部分浏览器不对斜体和倾斜作区分，故一般只使用`italic`
 - `font-weight`：设置文本的加粗效果
    - `normal`：正常
    - `blod`：加粗
    - `100-900之间的9个值`:但是由于计算机中往往没有这么级别的字体，所以达到的效果就是，有可能200的比100粗，300比200粗，也可能是一样的

- `font-variant`：设置小型大写字母
  - `normal`：正常
  - `small-caps`：文本以小型大写字母显示
- `font`样式简写
  - 无顺序要求
  - 不写的项取默认值
  - 文字大小和字体必须写，且字体必须是最后一个样式，文字大小必须是倒数第二个样式

```css
font:italic small-caps blod 60px "宋体";
```

- 字体分类

  - `serif`：衬线字体
  - `sans-serif`：非衬线字体
  - `monospasce`：等宽字体
  - `cursive`：草书字体
  - `fantasy`：虚幻字体
  - 若将字体设置为这些大的分类时，浏览器会自动的选择指定并应用样式
  - 一般将大分类的字体指定为最后一个字体

  ```css
  p{
      font-family:"serif";
  }
  ```

## 7.5.行高设置

- `line-height`：设置行高
  - 行间距 = 行高 - 字体大小
  - `line-height:20px`
  - `line-height:100%`：相对于字体大小的百分比
  - `line-heightL:1.5`：数值，行高设置为字体大小相应的倍数
  - 对于单行文本，可以将行高设置为和父元素的高度一致，这样单行文本就在父元素中垂直居中

```css
font:16px/20px "宋体"； //在font样式中设置行高20px
```

```css
p{
    line-height:30px;
    font:16px "宋体";  //此时line-height不生效，因为font中行高的默认值覆盖了line-height的值
}
```

## 7.6.文字样式

- 注意：在chrome中字体最小支持12px，1-11px都显示为12px

- `text-transform`：设置文本的大小写

  - `none`
  - `capitalize`：单词首字母大小
  - `uppercase`：所有字母大写
  - `lowercase`：所有字母小写

- `text-decoration`：给文本(下方，中间，上方)添加线条

  - `none`
  - `underline`：下划线
  - `overline`：上划线
  - `line-through`：删除线

  ```css
  a{
     text-decoration:none;/*去除超连接的下划线*/ 
  }
  ```

- `letter-spacing`：设置字符之间的间距
- `wrod-spacing`：设置单词之间的间距，实际就是设置词语词之间空格的大小
- `text-align`：设置文本的对齐方式
  - `center`
  - `left`：默认值
  - `right`
  - `jusify`：两端对齐，是通过调整文本之间空格来达到两端对齐

- `text-indent`：首行缩进

  - 设置为负值，文字会向左移，一般用于隐藏一些文字

```css
p{
    text-indent:2em;
}
```

## 7.7.文档流

文档流处在页面的最底层，它表示的是一个页面中的位置，我们所创建的元素默认都在文档流中

- 元素在文档流中的特点
  - 块元素：在文档流中都占一行，块元素会自上向下排列
    - 块元素在文档流中默认宽度是父元素的100%，值为auto
    - 块元素在文档流中默认高度是被内容撑开
  - 内联元素：在文档流中只占自身的大小，默认会自左向右排列；若一行不足以容纳所有元素，会换到下一行
    - 内联元素在文档流中默认的宽度和高度都是有内容撑开

> 注意:当元素宽度的值为auto时，此时指定内边距不会影响可见可见框的大小，而是会自动修改宽度，以适应内边距

## 7.8.背景样式

- `background`：简写样式
  - 没有顺序要求
  - 注意：没设置的属性，使用默认值，且该默认值会覆盖前面设置的属性

```css
background:#baf url(img/3.jpg) center center no-repeat fixed;
```

- `background-image`：设置背景图片
  - 若图片大于元素，默认只显示左上角的
  - 若图片小于元素，则将图片平铺显示以充满元素
  - 可以同时设置背景颜色和背景图片，且背景颜色显示为背景图片的底色

```css
background-image:url(../img/1.jpg)
```

- `background-repeat`：设置是否及如何重复背景图片

  - `repeat`：默认值，背景图片会双方向重复（平铺）
  - `no-repeat`：背景图片不重复，有多大显示多大
  - `repeat-x`：水平方向背景图片重复
  - `repeat-y`：垂直方向背景图片重复

- `background-position`：设置背景图片的位置

  - `top reight left bottom center`：可通过任意两个值指定位置
    - 若只设置一个值，第二个只默认为`center`

  - 可指定两个偏移量（水平偏移量，垂直偏移量），`background-position:100px -20px;`

  - 可指定百分比偏移量(0%,0%)

- `background-attachment`：设置背景图片是否固定或随页面一起滚动
  - `scroll`：默认值，背景图片随页面滚动而滚动
  - `fixed`：背景图片固定在某一位置，不随页面滚动
    - 此时背景图片定位`(background-position)`相对于浏览器窗口
    - 应用于：背景图片不滚动，页面文字滚动
    - 一般只给`body`，因为设置给其他元素div时，仍相对窗口定位，但是div隐藏时，背景图片也会隐藏

- `IE6 png`图片的修复

  - 在IE6中，若图片的格式是png24，则会导致透明效果无法正常显示

  - 解决方法：使用png8代替png24即可，但是图片的清晰度有所下降

  - 使用js解决：

    - 引入js文件

    ```js
    <script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
    <script type="text/javascript">
      DD_belatedPNG.fix("div,img");
    </script>
    ```

    - 优化后只在IE6执行

    ```html
    <!--[if IE 6]>
    	<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
        <script type="text/javascript">
          DD_belatedPNG.fix("div,img");
        </script>
    <!--[endif]-->
    ```


## 7.9.`CSS-hack`

- `CSS-hack`尽量不要使用，且有风险

- 有一些特殊的代码我们只需要在指定的浏览器中执行，而不需要在其他浏览器执行，此时可使用`CSS-hack`来解决问题

- `CSS-hack`指的是一种特殊的代码，只某些浏览器中可以识别，而其他浏览器不能识别

- 条件`Hack`只对IE浏览器有效，其他的浏览器多会将它识别为注释，且IE10及以上的浏览器不支持此种方式了

  ```html
  
  ```

  ```html
  <!--[if IE 6]>
  	<p>只在IE6中显示</p>
  <!--[endif]-->
  ```

  ```html
  <!--[if lt IE 9]>
  	<p>只在IE9以下的浏览器中显示</p>
  <!--[endif]-->
  ```

  - `lt`：小于
  - `lte`：小于等于
  - `gt`：大于
  - `gte`：大于等于
  - `!`：除了

- 为兼容IE，可设置单独的样式表

```html
<style type="text/css" rel="stylesheet" href="css/style.css"></style>
<!--[if IE 6]>
<style type="text/css" rel="stylesheet" href="css/style-ie.css"></style>
<!--[endif]-->
```

- 属性`Hack`
  - `-`：IE6及以下
  - `*`：IE7及以下
  - `\9`：IE6及以上
  - `\0`：IE8及以上

```css
body{
    background-color:baf;
    _background-color:green; /*在IE6中生效*/
}
```

```css
body{
    background-color:baf;
    background-color:green\0; /*在IE8及以上中生效*/
}
```

## 7.10.表单样式

- `resize`：设置文本域不能调整大小
  - `none`：













# 八、盒子模型

![盒模型](E:\typora\images\20180324150509906.jpg)

- `height`：设置的是内容区的高度
- `width`：设置的是内容区的宽度

## 8.1.边框样式

- `border-width`：设置边框的宽度
  - `border-width：top left bottom right`,`border-width：10px 20px 30px 40px`
  - 指定三个值(上 左右 下)，`border-width:10px 20px 30px`
  - 指定两个值(上下 左右)，`border-width:10px 20px`
  - 指定四个值(上下左右)，`border-width:10px`
    - `border-left-width`：设置指定边的样式
- `border-color`：设置边框的颜色
  - `border-left-color`
  - `border-color:red blue green orange`
- `border-style`：设置边框线的样式
  - `none`：无边框
  - `solid`：实线
  - `dotted`：点线
  - `dashed`：虚线
  - `double`：双线
  - `border-style:solid dotted dashed double`

> 注意：大部分的浏览器中，边框的宽度和颜色都有默认值，而边框的样式都是为none，故指定了边框样式边框就显示出来了，一般需要三个属性都设置

- 边框样式简写
  - 没有顺序要求
  - 会同时设置4条边的样式
  - `border-left:1px solid #213234`单独指定某个边的样式

```css
div{
    border:1px solid #222;
}
```

## 8.2.内边距

- 内边距指的是盒子内容区与盒子边框之间的距离
- 设置内边距会影响盒子可见框的大小，元素的颜色会延伸到内边距

- `padding`
- `padding-left`
- `padding-right`
- `padding-top`
- `padding-bottom`

## 8.3.外边距

- 外边距指当前盒子与其他盒子之间的距离，不会影响可见框的大小，而是会影响盒子的位置
- `margin`
- `margin-left`
- `margin-right`
- `margin-top`
- `margin-bottom`

- 设置外边距为负值时，元素会向反方向移动
- 将`margin-left:auto`和`margin-right:auto`表示把左边距或右边距设置为最大值
  - 一般只设置在水平方向

```css
div{
	margin:auto 0px; /*水平居中*/
}
```

- 垂直外边距重叠：垂直方向相邻的外边距会发生外边距的重叠

  - 兄弟元素之间相邻外边距会取最大值，而不是取和
  - 父子元素在垂直方向外边距相邻，则子元素的外边距会设置给父元素


## 8.4.内联元素的盒模型

- 内联元素不能设置width和height
- 内联元素能设置水平方向的内边距，可以影响布局
- 内联元素可以设置垂直方向的内边距，但是不会影响页面的布局
- 内联元素可以设置边框，但是垂直方向的边框不会影响布局
- 内联元素支持水平的外边距，不支持垂直方向的外边距

- `display`：规定元素生成框的类型
  - `none`：元素不被显示，且元素不在页面占有位置
  - `block`：块级元素
  - `inline`：内联元素
  - `inline-block`：行内块元素，可以设置宽高，又不会独占一行
    - 行内块元素就是讲元素当做了内容，故代码中空格，显示就有空格
- `visibility`：设置元素是否可见
  - `hidden`：元素隐藏，但是位置依然保持 
  - `visible`：默认值

- `overflow`：设置父元素如何处理溢出的内容
  - `visable`：默认值，不会对溢出的内容处理
  - `hidden`：溢出的内容会被剪切掉，不会显示
  - `scroll`：显示滚动条，无论是否溢出都显示滚动条
  - `auto`：根据需要显示滚动条





# 九、布局

## 9.1.定位

- `position`：设置定位的类型
  - `static`：默认值，没有定位
  - `relative`：相对定位
    - 开启相对定位，若没有设置偏移量，元素的不会发生任何变化 
    - 相对定位是相对于元素在文档流中原来位置进行定位
    - 相对定位元素不会脱离文档流，即相对定位不会改变元素的性质
    - 相对定位会是元素提升一个层级，即可以覆盖其他元素
  - `absolute`：绝对定位
    - 开启绝对定位，元素会脱离文档流，即会改变元素的性质
      - 内联元素会变成块元素
      - 块元素的高度和宽度默认被内容撑开
    - 开启绝对定位，若不设置偏移量，元素位置不变
    - 绝对定位是相对于离它最近祖先元素(开启了定位)进行定位，
      - 若所有的祖先元素都没有开启定位，则会相对于浏览器窗口定位
      - 一般情况下，开启了子元素的绝对定位，就会开启父元素的相对定位
    - 绝对定位会是元素提升一个层级
  - `fixed`：固定定位，相对于窗口定位
    - 固定定位是一种特殊的绝对定位，它的大部分特点与绝对定位相同
    - 不同之处
      - 永远相对于浏览器窗口定位
      - 固定定位会固定在浏览器窗口的某个位置，不会随滚动条滚动
      - IE6不支持固定定位

## 9.2.元素层级

- 若定位元素的层级是一样的，则后面的元素盖住前面的元素
- `z-index`：设置元素的层级，层级越高越优先显示
  - 对于没有开启定位的元素，不能使用`z-index`
  - 父元素的层级再高，也不会盖住子元素
- `opacity`：设置元素背景透明，0-1之间的值（0表示完全透明，1表示不透明）
  -  在IE8及以下浏览器不支持，此时可使用滤镜`filter:alpha(opacity=50)`
    - 0-100之间的值，0表示完全透明，100表示不透明

```css
/*设置透明*/
opacity：0.5;
filter:appha(opacity=50); /*此方式支持IE6,但是在IE Tester中不支持*/
```

## 9.3.包含块

- 对于浮动元素，其包含块是最近的块级祖先元素

- 对于定位

  - “根元素”的包含块（称为初始包含块）由用户代理建立。在html中，根元素就是html，不过部分浏览器会使用body作为根元素。在大部分浏览器中，初始包含块是一个视窗大小的矩形。

  - 对于一个非根元素
    - `position`的值为`relative`或`static`，包含块由最近的块级框{表单单元格或行内块祖先框的内容} (不建议使用表单和行内元素布局)。
    - `position`值为`absolute`，包含块设置为最近`position！=static`的祖先元素
      - 若祖先元素是块级元素，包含块则设置为该祖先元素的内边距边界
      - 若没有祖先，元素的包含块是初始包含块
      - 若内联元素(不建议采用，规则复杂，且各个浏览器不统一)

## 9.4.三列布局

- 三列布局
  - 两边固定，中间自适应
  - 中间要完整显示
  - 中间列要优先加载
- 定位实现（不建议）

```html

```

- 浮动实现（不建议）
  - 先加载`left right`后加载`middle`，若网络不好对用户体验不好

```html
<style>
  body{
    /*防止缩放时，中间缩没了*/
    min-width: 600px;/*left*2+right*/
  }
  .left,.right{
    width: 200px;
    height: 200px;
    background-color: pink;
  }
  .left{
    float: left;
  }
  .right{
    float: right;
  }
  .middle{
    background-color: deeppink;
    height: 200px;
  }
</style>

  <div class="left">left</div>
  <div class="right">right</div>
  <div class="middle">middle</div>
```

## 9.5.圣杯布局

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    body {
      /*防止缩放时，中间缩没了*/
      min-width: 600px; /*left*2+right*/
    }

    .header, .footer {
      height: 50px;
      background-color: #fafabc;
      text-align: center;
      border: 1px solid rgba(0, 0, 0, 0.78);
    }

    .center {
      padding: 0 200px;/*显示中间内容*/
    }

    .middle {
      float: left;
      width: 100%;
      background-color: deeppink;
    }

    .left, .right {
      width: 200px;
      background-color: pink;
    }

    .left {
      position: relative;
      left:-200px;
      margin-left: -100%;
      float: left;
    }

    .right {
      position: relative;
      right:-200px;
      /*自身的宽度*/
      margin-left: -200px;
      float: left;
    }

    .clearfix:after {
      content: "";
      display: table;
      clear: both;
    }

    /*兼容IE6*/
    .clearfix {
      *zoom: 1;
    }
  </style>
</head>
<body>
    <div class="header">header</div>
    <!--高度塌陷-->
    <div class="center clearfix">
      <!--middle优先加载-->
      <div class="middle">middle</div>
      <div class="left">left</div>
      <div class="right">right</div>
    </div>
    <div class="footer">footer</div>
</body>
</html>
```

- 使用浮动搭建完整的布局框架
- 使用`margin`赋值是为了调整旁边两列的位置（即使三列显示在一行）
- 使用相对定位是为了调整旁边两列的位置（是旁边两列的位置显示在两头）

## 9.6.双飞翼布局





## 9.7.等高布局



## 9.8.[`Flex`布局](http://www.ruanyifeng.com/blog/2015/07/flex-grammar.html)

`Flex`是`Flexible Box`的缩写，意为弹性布局

- 采用`Flex`布局的元素，称为Flex容器
  - 它所有的子元素为容器成员，成为Flex项目
  - 容器默认有两个轴：水平的主轴（main axis）和垂直的交叉轴（cross axis）
    - 主轴的开始位置（与边框的交叉点）叫做`main start`，结束位置叫做`main end`
    - 交叉轴的开始位置叫做`cross start`，结束位置叫做`cross end`
    - 项目默认沿主轴排列
    - 单个项目占据的主轴空间叫做`main size`，占据的交叉轴空间叫做`cross size`

![](E:\typora\images\bg2015071004.png)

- 任意一个容器都可以指定为`Flex`布局

```css
.box{
    display:flex;
}
```

- 行内元素也可以使用`Flex`布局

```css
.box{
    display:inline-flex;
}
```

- `Webkit `内核的浏览器使用用需加上`-webkit`

```css
.box{
	display:-webkit-flex;/* Safari */
      display: flex;
}
```

> 注意:设为`Flex`布局后，子元素的`float`、`clear`、`vertical-align`属性将生效

## 9.8.1.`Flex`容器的属性

- `flex-direction`设置主轴的方向（即项目的排列方向）

  - `row`（默认值）：主轴为水平方向，起点在左端。
  - `row-reverse`：主轴为水平方向，起点在右端。
  - `column`：主轴为垂直方向，起点在上沿。
  - `column-reverse`：主轴为垂直方向，起点在下沿。

  ```css
  .box {
    flex-direction: row | row-reverse | column | column-reverse;
  }
  ```

  ![](E:\typora\images\bg2015071005.png)

- `flex-wrap`设置是否换行（一条轴线排不下时），默认情况下，项目都排在一条线（又称"轴线"）上

  - `nowrap`（默认）：不换行

  ![](E:\typora\images\bg2015071007.png)

  - `wrap`：换行，第一行在上方

    ![](E:\typora\images\bg2015071008.jpg)

  - `wrap-reverse`：换行，第一行在下方

    ![](E:\typora\images\bg2015071009.jpg)

- `flex-flow`是`flex-direction`属性和`flex-wrap`属性的简写形式，默认值为`row nowrap`

  ```css
  .box {
    flex-flow: <flex-direction> || <flex-wrap>;
  }
  ```

- `justify-content`设置项目在主轴上的对齐方式，具体对齐方式与轴的方向有关，下面假设主轴为从左到右

  - `flex-start`（默认值）：左对齐
  - `flex-end`：右对齐
  - `center`： 居中
  - `space-between`：两端对齐，项目之间的间隔都相等。
  - `space-around`：每个项目两侧的间隔相等。所以，项目之间的间隔比项目与边框的间隔大一倍

  ![](E:\typora\images\bg2015071010.png)

- `align-items`设置项目在交叉轴上如何对齐，具体的对齐方式与交叉轴的方向有关，下面假设交叉轴从上到下

  - `flex-start`：交叉轴的起点对齐
  - `flex-end`：交叉轴的终点对齐
  - `center`：交叉轴的中点对齐
  - `baseline`: 项目的第一行文字的基线对齐
  - `stretch`（默认值）：如果项目未设置高度或设为auto，将占满整个容器的高度

  ![](E:\typora\images\bg2015071011.png)

- `align-content`设置多根轴线的对齐方式。如果项目只有一根轴线，该属性不起作用
  - `flex-start`：与交叉轴的起点对齐
  - `flex-end`：与交叉轴的终点对齐
  - `center`：与交叉轴的中点对齐
  - `space-between`：与交叉轴两端对齐，轴线之间的间隔平均分布
  - `space-around`：每根轴线两侧的间隔都相等。所以，轴线之间的间隔比轴线与边框的间隔大一倍
  - `stretch`（默认值）：轴线占满整个交叉轴

![](E:\typora\images\bg2015071012.png)

## 9.8.2.`Flex`项目的属性

- `order`设置项目的排列顺序。数值越小，排列越靠前，默认为0

  ![](E:\typora\images\bg2015071013.png)

- `flex-grow`设置项目的放大比例，默认为`0`，即如果存在剩余空间，也不放大

  - 若所有项目的`flex-grow`属性都为1，则它们将等分剩余空间（如果有的话）。
  - 若一个项目的`flex-grow`属性为2，其他项目都为1，则前者占据的剩余空间将比其他项多一倍。

![](E:\typora\images\bg2015071014.png)

- `flex-shrink`设置项目的缩小比例，默认为1，即如果空间不足，该项目将缩小

  - 如果所有项目的`flex-shrink`属性都为1，当空间不足时，都将等比例缩小。
  - 如果一个项目的`flex-shrink`属性为0，其他项目都为1，则空间不足时，前者不缩小。

  ![](E:\typora\images\bg2015071015.jpg)

- `flex-basis`设置在分配多余空间之前，项目占据的主轴空间（main size）

  - 默认值为`auto`，即项目的本来大小
  - 设为跟`width`或`height`属性一样的值，则项目将占据固定空间

```css
.item {
  flex-basis: <length> | auto; /* default auto */
}
```

- `flex`是`flex-grow`, `flex-shrink` 和 `flex-basis`的简写，默认值为`0 1 auto`。后两个属性可选s

- `align-self`设置是否允许单个项目有与其他项目不一样的对齐方式，会覆盖`align-items`属性

  - `auto`，默认值，表示继承父元素的`align-items`属性，如果没有父元素，则等同于`stretch`

  - `flex-start`：交叉轴的起点对齐
  - `flex-end`：交叉轴的终点对齐
  - `center`：交叉轴的中点对齐
  - `baseline`: 项目的第一行文字的基线对齐
  - `stretch`：如果项目未设置高度或设为auto，将占满整个容器的高度

## 9.8.3.[`Flex`布局实例](http://www.ruanyifeng.com/blog/2015/07/flex-examples.html)

## 9.8.4.[`Flex`布局的表单](http://www.ruanyifeng.com/blog/2018/10/flexbox-form.html)

```html
<form>
  <input type="email" name="email">
  <button type="submit">Send</button>
</form>
```

两个控件都是行内块级元素（inline-block），中间的间隔是内置样式指定的

![](E:\typora\images\bg2018101801.png)

```css
form  {
  display: flex;
}
```

两个控件之间的间隔消失了，因为弹性布局的项目（item）默认没有间隔

![](E:\typora\images\bg2018101802.png)

> 弹性布局默认不改变项目的宽度，但是它默认改变项目的高度。如果项目没有显式指定高度，就将占据容器的所有高度



# 十、实例

## 10.1.导航条

```css
.nav{
    padding: 0px;/*去除默认样式*/
    list-style: none;
    width: 1000px;
    margin: 50px auto;
    overflow: hidden;/*解决高度塌陷*/
    background-color: #4274ff;
}
.nav li{
    float:left;
    width:20%;
}
.nav li a{
    display: block;
    width:100%;
    text-align: center;
    padding: 5px 0px;
    text-decoration: none;
    color: white;
    font-weight: bold;
}
.nav li a:hover{
    background-color: #fa5168;
}
```

```html
<ul class="nav">
    <li><a href="#">首页</a></li>
    <li><a href="#">新闻</a></li>
    <li><a href="#">联系</a></li>
    <li><a href="#">关于</a></li>
    <li><a href="#">帮组</a></li>
</ul>
```

## 10.2.设置颜色渐变

截取一个像素宽的渐变图片，设置为水平平铺即可

```css
backgroud-image:url('../img/bck.jpg');
backgrond-repeat:repeat-x;
```

## 10.3.图片按钮

```html
<a class="btn" href="#"></a>
```

```css
.btn:link{
    /*将a转换为块元素*/
    display: block;
    /*设置宽高*/
    width: 93px;
    height: 29px;
    /*设置背景图片*/
    background-image: url(img/btn/btn2.png);
    /*设置背景图片不重复*/
    background-repeat: no-repeat;

}

.btn:hover{

    /*
    * 当是hover状态时，希望图片可以向左移动
    */
    background-position: -93px 0px;
}

.btn:active{
    /*
    * 当是active状态时，希望图片再向左移动
    */
    background-position: -186px 0px;

}
```

> 注意：此时使用多张图片时，在切换的时候可能会出现闪烁，因为请求的延迟
>
> 解决方式：使用图片整合技术（CSS-Sprite），其优点
>
> - 将多个图片整合为一张图片里，浏览器只需要发送一次请求，可以同时加载多个图片，提高访问效率，提高了用户体验。
> - 将多个图片整合为一张图片，减小了图片的总大小，提高请求的速度，增加了用户体验

