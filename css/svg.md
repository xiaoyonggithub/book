# 一、`SVG`使用方式

SVG 是一种基于 XML 语法的图像格式，全称是可缩放矢量图（Scalable Vector Graphics）；

- 其他图像格式都是基于像素处理的，SVG 则是属于对图像的形状描述
- SVG本质上是文本文件，体积较小，且不管放大多少倍都不会失真。

- SVG 文件可以直接插入网页，成为 DOM 的一部分，然后用 JavaScript 和 CSS 进行操作。

```html
<svg
  id="mysvg"
  xmlns="http://www.w3.org/2000/svg"
  viewBox="0 0 800 600"
  preserveAspectRatio="xMidYMid meet"
>
```

- SVG 代码也可以写在一个独立文件中，然后用`<img>`、`<object>`、`<embed>`、`<iframe>`等标签插入网页

```html
<img src="circle.svg">
<object id="object" data="circle.svg" type="image/svg+xml"></object>
<embed id="embed" src="icon.svg" type="image/svg+xml">
<iframe id="iframe" src="icon.svg"></iframe>
```

- CSS 也可以使用 SVG 文件

```css
.logo {
  background: url(icon.svg);
}
```

- SVG 文件还可以转为 BASE64 编码，然后作为 Data URI 写入网页

```html
<img src="data:image/svg+xml;base64,[data]">
```

# 二、标签

## 2.1.`<svg>`

