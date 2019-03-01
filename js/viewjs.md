# 一、`viewerjs`

## 1.1.动态添加图片时，使用viewerjs

```js
var viewer;
var $image;
//进入页面初始化加载viewerjs
$image = $('.fileList');
$image.viewer({url: 'data-imgurl'});
viewer = $image.data('viewer');
//动态添加图片后，动态加载viewerjs
$image.viewer('destroy');
$image.viewer({url: 'data-imgurl', show: 'update'});
$image.viewer('update');
```

