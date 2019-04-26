# 一、`JSTL`

- 







# 二、`EL`

## 2.1.`EL`默认取值顺序

`pageScope-->requestScope-->sessionScope-->applicationScope`

## 2.2.取值

- 取对象中的属性值

```html
${user.name}
```

- 取`Map`中的值

```html
${map.key}
```

- 取数组中的值

```html
${arr[0]}
```

- 取集合中的值

```html
${list[0].name}
```



