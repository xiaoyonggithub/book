# 一、`Object`

- `Object.create()`:创建一个新对象，使用现有的对象来提供新创建的对象的`__proto__`

```js
var element = Object.create(HTMLElement.prototype);
//HTMLElement 接口表示所有的 HTML 元素
```



