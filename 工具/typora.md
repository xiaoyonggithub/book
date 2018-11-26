# 一、`HTML Support`

## 1.1.`<details>`

`<details>`折叠语法

- `<details>`折叠语法
- `<summary>`折叠语法展示的摘要
- `<blockcode>`程序代码块
- `<code>`代码范例

```html
<details>
    <summary>折叠语法</summary>
    <pre>
        protected void close(InputStream is) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } 
	</pre>
</details>
```

<details>
    <summary>折叠语法</summary>
    <pre>
protected void close(InputStream is) {
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} </pre>
</details>
## 1.2.行内html

只有行内元素支持

<table>
    <tr>
    	<th>姓名</th>
		<th>描述</th>
    </tr>
    <tr>
    	<td colspan="2">
        	合并行
        </td>
    </tr>
</table>





