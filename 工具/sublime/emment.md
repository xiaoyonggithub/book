`Tab`触发补全代码

## 一、生成元素

1. 自动补全`<html>`标签

- `!`
- `html:5`
- `html:4s`
- `html:4t`

### 1.1.嵌套操作

1. 使用`>`生成子元素

```html
div>ul>li
```

```html
<div>
    <ul>
        <li></li>
    </ul>
</div>
```

2. 使用`+`生成兄弟元素

```html
div+p+button
```

```html
<div></div>
<p></p>
<button></button>
```

3. 使用`^`生成父级元素

```html
div+div>p>span+em^bq
```

```html
<div></div>
<div>
    <p><span></span><em></em></p>
    <blockquote></blockquote>
</div>
```

4.使用`*`生成多个相同元素

```html
ul>li*5
```

```html
<ul>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
</ul>
```

5. 使用`()`将元素分组

```js
div>(header>ul>li*2)+footer>p  // "+" 后面的元素与括号中的第一个元素属于兄弟关系
```

```html
<div>
    <header>
        <ul>
            <li></li>
            <li></li>
        </ul>
    </header>
    <footer>
        <p></p>
    </footer>
</div>
```

### 1.2.属性操作

1. `id`与`class`属性

元素与id之间使用`#`,元素与class之间使用`.`

```js
div#outer.main
```

```html
 <div id="outer" class="main"></div>
```

2. 使用`[]`标记其他属性

```js
div[name='file']
```

```html
<div name="file"></div>
```

3. 使用`$`实现1到n的自动编号（`*`实现多个元素）

```html
div.item$*4
```

```html
<div class="item1"></div>
<div class="item2"></div>
<div class="item3"></div>
<div class="item4"></div>
```

4. 在`$`后添加`@n`修改编号的起始值为n

```html
div.item$@3*3
```

```html
<div class="item3"></div>
<div class="item4"></div>
<div class="item5"></div>
```

5. 在`$`后添加`@-`修改编号的方向

```html
div.item$@-5*4
```

```html
<div class="item8"></div>
<div class="item7"></div>
<div class="item6"></div>
<div class="item5"></div>
```

6. 使用`{}`添加文本内容

```html
button{保存}
```

```html
<button>保存</button>
```

```html
p>{Click }+a{here}+{ to continue}
```

```html
<p>Click <a href="">here</a> to continue</p>
```

```html
p{Click }+a{here}+{ to continue}
```

```html
<p>Click </p>
<a href="">here</a> to continue
```

