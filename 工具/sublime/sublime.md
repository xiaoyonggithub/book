

# 一、快捷键

`ctrl+左键`多点编辑

`ctrl+shift+p` 命令的执行和提示

`ctrl+ shift + k` 删除当前行

`key bindings` 查看快捷键 

`ctrl+p` 搜索项目中的文件

`ctrl+n` 新建一个页面

`ctrl+ tab` 切换页面

`ctrl+J` 合并两行

`ctrl + []` 缩进一个级别

`ctrl + L` 选中当前行

`ctrl + enter` 在当前行下新建一行

`shift+ enter` 在当前行的上面新建一行

`ctrl + shift + 右键`  块选择

`esc` 退出多行选择模式

`alt/ctrl + ←/→`   单词之间的切换

`ctrl + shift +  ←/→`  单词之间的切换

`ctrl+shift + ↓/↑`  移动当前行的位置

`ctrl +alt + ↓/↑` 行编辑，选择编辑的行

`shfit + ↓/↑` 上下选中

`shift + home/end` 选中光标之前或之后的内容

`ctrl+d` 选择所有相同的字符串，并可多点编辑	

`alt+ -` 跳回原来的位置  `alt+shift+-` 回跳

`tab` 补齐文本

`ctrl+shift+d` 删除当前行

## 1.1.面板的操作

`ctrl + shift + p` 打开面板，可在面板中进行如下操作

* 设置文档语法`(css js )`
* 调整缩进`reindent lines`,设置快捷键（调整css和js）

```json
{"keys": ["shift+tab"],"command": "reindent","args":{"single_line":false}}
```

* 打开文件属性配置`settings` 
* 打开快捷键配置`key bindings`

## 1.2.查看命令及其参数

1. ctrl + `

2. sublime.log_commands(True)
3. 在包管理面板中执行命令

注意设置了快捷键后执行命令可能不显示，通过快捷键可显示

## 1.3.文件的查找

> 如何快速查找文件和字符串

1. `ctrl + p`打开搜索窗口
2. `/test/text:10`查找文件夹下的文件，并跳转到指定的行
3. `/test/text@fnClose` 查找指定的函数或关键字
4. `#` 查找字符串

## 1.4.配置文件

自定制的配置都保存在User文件夹下



代码片段



