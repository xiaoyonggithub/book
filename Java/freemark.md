

# 一、`FreeMark`

`FreeMarker (FreeMarker Template Language ,ftl)`是一种基于模板和要改变的数据， 并用来生成输出文本(`HTML`网页，电子邮件，配置文件，源代码等)的通用工具

- `ftl`区分大小写
- `ftl`标签不可以在其它`ftl`标签和插值`(${})`中使用

```ftl
 <#if <#include 'foo'>='bar'>...</#if> 
```

- 注释可以在`ftl`标签和插值`(${})`中使用

```ftl
<h1>Welcome ${user <#-- The name of user -->}!</h1>
```









# 二、`FreeMark`指令

- 指令的类型
  - 预定义指令
  - 用户自定义指令

- 指令使用`#`开头，自定义指令使用`@`开头
- 注释，不会出现在输出中

```ftl
<#--注释-->
```

## 2.1.`if`

```html
<#if user == "Bom">
</#if>
```

```html
<#if animals.python.price != 0>
  ...
<#else>
  ...
</#if>
```

```html
<#if animals.python.price < animals.elephant.price>
  Pythons are cheaper than elephants today.
<#elseif animals.elephant.price < animals.python.price>
  Elephants are cheaper than pythons today.
<#else>
  Elephants and pythons cost the same today.
</#if>
```

## 2.2.`list`

```html
<#list animals as animal>
	<tr><td>${animal.name}<td>${animal.price} Euros
</#list>
```

```html
<#list misc.fruits as fruit>
    <li>${fruit}	
</#list>
```

```html
<#list misc.fruits>
  <ul>
    <#items as fruit>
      <li>${fruit}
    </#items>
  </ul>
</#list>
```

```html
<p>Fruits: <#list misc.fruits as fruit>${fruit}<#sep>, </#list>
<p>Fruits: orange, banana 
<!--使用分隔符，<#sep>,</#sep>的内容只有有下一项才会被执行-->
```

```html
<p>Fruits: <#list misc.fruits as fruit>${fruit}<#sep>, <#else>None</#list>
<!--<#else>只有列表中没有元素时才会执行-->
```

## 2.3.`include`

```html
<#include "/copyright_footer.html">
```

> 注意：FreeMarker不解析FTL标签以外的文本、插值和注释，所以在HTML属性中使用FTL标签也不会有问题

```html
<#list animals as animal>
      <div<#if animal.protected> class="protected"</#if>>
        ${animal.name} for ${animal.price} Euros
      </div>
</#list>
```



# 三、变量/表达式

## 3.1.表达式

- 从哈希表中检索值

```ftl
${user.name}
${user["name"]
```

- 从序列中检索值

```ftl
${users[0]}
```

- 字符串截取

  - 包含结尾

    ```ftl
    ${name[0..4]}
    ```

  - 不包含结尾

    ```ftl
    ${name[0..<5]}
    ```

  - 基于长度(宽容处理)

    ```ftl
    ${name[0..*5]}
    ```

  - 去除开头

    ```ftl
    ${name[5..]}
    ```

- 序列连接

```ftl
${users+["name"]}
```

- 序列切分

  - 包含结尾

    ```ftl
    ${list[0..4]}
    ```

  - 不包含结尾

    ```ftl
    ${list[0..<5]}
    ```

  - 基于长度(宽容处理)

    ```ftl
    ${list[0..*5]}
    ```

  - 去除开头

    ```ftl
    ${list[5..]}
    ```

- 逻辑操作

```ftl
${!registered && (firstVisit || fromEurope)}
<#if  !registered && (firstVisit || fromEurope)>
</#if>
```



- 处理不存在的变量
  - 不存在的变量，使用`??`判断变量是否存在

    ```ftl
    <#if user??>
        ${user}
    </#if>
    <#if (animals.python.price)??>
        ${user}
    </#if>
    ```

  - `null`值的变量 		

    ```ftl
    ${user.name!}
    ${user.name!"张三"} 
    ${(animals.python.price)!0}  <!--当animal或python或price不存在时，都是为0-->
    ```

    

## 3.3.数据类型

### 3.3.1.标量

标量是最简单的数据类型，可以是：

- 字符串
- 数值：不区分整数和非整数
- 布尔值
- 日期

### 3.3.2.容器

容器的目的是包含其它的值，包含的值通常视为`subvariables `

- 哈希表：无序，类似于`HashMap`
- 序列:可以通过索引`index`获取值
- 集合：不能获取集合大小，也不能通过索引`index`获取值，但是使用`<#list>`遍历

### 3.3.3.子程序（方法和函数）

方法和函数是一种数据类型

```ftl
${avg(3, 5)}
```

#### 3.3.3.1内建函数

- 使用`?`访问函数
- `str?html`:给出HTML转义版本
- `str?upper_case`:将字符串转大写
- `str?cap_first`：首字母大写
- `str?length`：字符的个数
- `animals?size`:序列中项目的个数
- 在`<#list animals as animal>`遍历中的函数
  - `animal?index`:基于0开始的索引值
  - `animal?counter`:基于1开始的索引值
  - `animal?item_parity`:基于当前基数的奇偶性，返回字符串`odd`和`even`
- `animal.protected?string('Y','N')`：根据`animal.proteced`布尔值返回`Y`或`N`
- `animal?item_cycle('lightRow','darkRow')`:是`animal?item_parity`的变体
- `fruits?join(',')`：连接所有的项，通过`,`分隔显示
- `user?start_with('J')`:判断首字母是不是`J`

- 链式调用函数

```html
${user?upper_case?html}
```

### 3.3.4.用户自定义指令

自定义`freemark`标签，用户自定义指令是一种子程序，一种可复用的代码段

















## 参考文档

[^1]: <https://freemarker.apache.org/>

