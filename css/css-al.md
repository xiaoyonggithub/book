

# 一、自定义标签

```css
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
<style type="text/css">
    input[type=checkbox] {
        display: inline-block;
        height: 20px;
        width: 20px;
        border: 1px solid #000;
        overflow: hidden;
        vertical-align: middle;
        text-align: center;
        -webkit-appearance: none;
        font: normal normal normal 14px/1 FontAwesome;
        outline: 0;
        background: 0 0;
    }

    input[type=checkbox]:checked:after {
        content: '\f00c';
        font-size: 15px;
        text-align: center;
        line-height: 17px;
        color: #000;
    }
</style>
<input type="checkbox">
```



