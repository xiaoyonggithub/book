# 一、`Chrome`的使用

## 1.1. `$0`

- `$0`当前选中的节点
- `$1`上次选中的节点
- `$2`、`$3`、`$4`依次之前选中的节点，只到`$4`

```js
$1.appendChild($0)
```

## 1.2.`$`和`$$`

- `$`是`document.querySelector`的别名
- `$$`是`document.QuerySelectorAll`的别名，返回节点的数组

## 1.3.`$_`

`$_`关联上次执行的结果

## 1.4.`$i`

`Console Importer`





