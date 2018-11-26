# 一、[`gitbook`](https://yuzeshan.gitbooks.io/gitbook-studying/content/index.html)

## 1.1.安装`gitbook`

1. 安装[node.js](https://nodejs.org/zh-cn/)

> 安装了node.js,就会默认安装npm

2. 安装gitbook的命令行工具，`-g`全局安装

```shell
npm install gitbook-cli -g
```

安装gitbook

```shell
npm install gitbook -g 
```

- windows安装[npm](https://github.com/isaacs/npm/tags)

3. 查看gitbook的版本

```shell
gitbook --version
```

4. 更新gitbook

```shell
npm update gitbook-cli -g
```

5. 卸载gitbook

```shell
npm uninstall gitbook-cli -g
```

6. 查看gitbook的安装目录

```shell
which gitbook
```

7. 列举可以下载的版本(远程)

```shell
gitbook ls-remote
```

8. 列出所有的命令

```shell
gitbook help
```

9. 帮助命令

```shell
gitbook --help
```

10. 列出所有的本地版本

```shell
gitbook ls
```

11. 安装指定的版本

```shell
gitbook fetch 标签/版本号
```

12. 卸载指定的版本

```shell
gitbook uninstall 2.0.1
```

13. 设置日志级别

```shell
gitbook build --log=debug
```

14. 设置输出错误信息

```shell
gitbook builid --debug
```









## 1.2.安装的方式

- `gitbook-cli` 和 `gitbook` 是两个软件

- `gitbook-cli` 会将下载的 gitbook 的不同版本放到 `~/.gitbook`中, 可以通过设置`GITBOOK_DIR`环境变量来指定另外的文件夹

- 本地安装：安装包会被下载到当前所在目录，因此只能在当前目录下使用
- 全局安装：安装包会被下载到到特定的系统目录下，安装包能够在所有目录下使用

## 1.3.搭建gitbook平台

### 1.3.1.创建目录

```bash
mkdir mybook 
cd mybook
```

### 1.3.2.初始化目录

```bash
gitbook init
gitbook init ./directory
```

此时会生成两个文件`README.md`和`SUMMARY.md`

- `README.md`:书籍的介绍
- `SUMMARY.md`:书籍目录结构的配置

### 1.3.3.配置目录

在`SUMMARY.md`中配置目录结构后，再次执行`gitbook init`，就会生成对应的目录结构及其文件

```
# 目录

* [前言](README.md)
* [第一章](Chapter1/README.md)
  * [第1节：衣](Chapter1/衣.md)
  * [第2节：食](Chapter1/食.md)
  * [第3节：住](Chapter1/住.md)
  * [第4节：行](Chapter1/行.md)
* [第二章](Chapter2/README.md)
* [第三章](Chapter3/README.md)
* [第四章](Chapter4/README.md)
```

### 1.3.4.预览书籍

`gitbook serve`默认会将Markdown格式转化为html格式，它包含`gitbook build`

```shell
gitbook serve
```

指定端口

```shell
gitbook serve --port 2333
```

### 1.3.5.构建书籍

`gitbook build`默认将生成的静态网站输出到` _book`目录，

```shell
gitbook build [书籍路径] [输出路径]
gitbook build
```

使用选项 --log=debug 和 --debug 来获取更好的错误消息（使用堆栈跟踪）

```shell
gitbook build ./ --log=debug --debug 
```

### 1.3.6.生成指定格式的电子书

1. 生成pdf格式的电子书

```shell
gitbook pdf ./ ./mybook.pdf
```

2. 生成 epub 格式的电子书

```shell
gitbook epub ./ ./mybook.epub
```

3. 生成 mobi 格式的电子书

```shell
gitbook mobi ./ ./mybook.mobi
```

若不能生成，可能需要安装`ebook-convert`或者在`Typora`中安装sPandoc进行导出

![1540435880332](E:\typora\images\1540435880332.png)

## 1.4.gitbook目录结构

### 1.4.1.gitbook中的特殊文件

- `book.json`配置数据 (**optional**)

- `README.md`电子书的前言或简介 (**required**)

- `SUMMARY.md`电子书目录 (**optional**)
- `GLOSSARY.md` 词汇/注释术语列表 (**optional**)

> 静态文件和图片是不需要在`SUMMARY.md`列出，都会复制到输出目录

## 1.4.2.目录锚点

目录中的章节可以使用锚点指向文件的特定部分。

```
# Summary
 
### Part I
 
* [Part I](part1/README.md)
    * [Writing is nice](part1/README.md#writing)
    * [GitBook is nice](part1/README.md#gitbook)
* [Part II](part2/README.md)
    * [We love feedback](part2/README.md#feedback)
    * [Better tools for authors](part2/README.md#tools)
```

## 1.4.3.目录分组

目录可以分为以标题或水平线 `----` 分隔的部分：

```
# Summary
 
### Part I
 
* [Writing is nice](part1/writing.md)
* [GitBook is nice](part1/gitbook.md)
 
### Part II
 
* [We love feedback](part2/feedback_please.md)
* [Better tools for authors](part2/better_tools.md)
 
----
 
* [Last part without title](part3/title.md)
```

## 1.5.`book.json`配置

- `title`书籍标题
- `author`书籍作者
- `description`书籍的描述
- `language`书籍的语言
- `gitbook`gitbook的版本
- `structure`:`readme`文件的位置
- `styles`自定义的样式
- `links`链接跳转
- `plugins`插件
- `pluginsConfig`插件的配置

```json
{
  "title": "标题",
  "author": "作者",
  "description": "简单描素",
  "language": "zh-hans", 
  "gitbook": "3.2.3", 
  "styles": {
    "website": "./styles/website.css" 
  },
  "structure": {
    "readme": "README.md" 
  },
  "links": {
    "sidebar": {
      "我的博客": "https://blog.csdn.net/kuangshp128" 
    }
  },
  "plugins": [ 
    "-sharing",
    "splitter",
    "expandable-chapters-small",
    "anchors",

    "github",
    "github-buttons",
    "donate",
    "sharing-plus",
    "anchor-navigation-ex",
    "favicon"
  ],
  "pluginsConfig": {
    "github": {
      "url": "https://github.com/kuangshp/"
    },
    "github-buttons": {
      "buttons": [{
        "user": "kuangshp",
        "repo": "mysql",
        "type": "star",
        "size": "small",
        "count": true
      }]
    },
    "donate": {
      "alipay": "./source/images/donate.png",
      "title": "",
      "button": "赞赏",
      "alipayText": " "
    },
    "sharing": {
      "douban": false,
      "facebook": false,
      "google": false,
      "hatenaBookmark": false,
      "instapaper": false,
      "line": false,
      "linkedin": false,
      "messenger": false,
      "pocket": false,
      "qq": false,
      "qzone": false,
      "stumbleupon": false,
      "twitter": false,
      "viber": false,
      "vk": false,
      "weibo": false,
      "whatsapp": false,
      "all": [
        "google", "facebook", "weibo", "twitter",
        "qq", "qzone", "linkedin", "pocket"
      ]
    },
    "anchor-navigation-ex": {
      "showLevel": false
    },
    "favicon": {
      "shortcut": "./source/images/favicon.jpg",
      "bookmark": "./source/images/favicon.jpg",
      "appleTouch": "./source/images/apple-touch-icon.jpg",
      "appleTouchMore": {
        "120x120": "./source/images/apple-touch-icon.jpg",
        "180x180": "./source/images/apple-touch-icon.jpg"
      }
    }
  }
}
```

## 1.6.gitbook支持的格式

- 静态站点：GitBook默认输出该种格式，生成的静态站点可直接托管搭载Github Pages服务上；
- PDF：需要安装[ebook-concert](http://calibre-ebook.com/download)依赖；
- eBook：需要安装[ebook-concert](http://calibre-ebook.com/download)；
- 单HTML网页：支持将内容输出为单页的HTML，不过一般用在将电子书格式转换为PDF或eBook的中间过程；
- JSON：一般用于电子书的调试或元数据提取。