# 一、`pdfbox`

## 1.1.`pdfbox`的特性

- 提取文本，包含`Unicode`字符
- 和`Lucence`等文本搜索引擎整合十分简单
- 加密、解密`PDF`文档
- 从`PDF`和`XPDF`格式中导入和导出表单数据
- 向已有的`PDF`文档追加内容
- 将一个`PDF`文档切分为多个文档
- 覆盖`PDF`文档

## 1.2.`pdfbox`基本操作

```java
//设置系统属性
System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
//新建pdf文件
PDDocument document = new PDDocument();

//加载现有的pdf文件
PDDocument.load(new File("f:\\test1.pdf"));

//创建页面
PDPage page = new PDPage();
//添加页面
document.addPage(page);

//列出PDF文档中存在的页面数量
document.getNumberOfPages();
//删除页面
document.removePage(page);
document.removePage(2);

//获取文档属性
PDDocumentInformation pinfo = document.getDocumentInformation();

//添加现有的pdf文档

//保存文档
document.save("f:\\test01.pdf");
//释放资源
document.close();
```



## 1.3.`pdfbox`加密

- `pdf`文档加密采用一个主密码和一个可选的用户密码
  - 若设置了用户密码，那么显示之前需要提示输入密码
  - 主密码用于授权修改文档内容
- `PDF`文档的安全模型是可插拔式的（`pluggable`）
- ` StandardProtectionPolicy`用于向文档添加基于密码的保护
- ` AccessPermission`用于通过为其分配访问权限来保护PDF文档，可以限制用户如下操作
  - 打印文档
  - 修改文档的内容
  - 复制或提取文档的内容
  - 添加或修改注释
  - 填写交互式表单域
  - 提取文字和图形以便视障人士使用
  - 汇编文件
  - 打印质量下降

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
PDPage page = document.getPage(0);
//创建访问权限对象
AccessPermission ap = new AccessPermission();
//设置可修改权限
ap.canModify();
//创建密码保护，并设置密码
StandardProtectionPolicy spp = new StandardProtectionPolicy("1234","1234",ap);
//设置加密密钥的长度
spp.setEncryptionKeyLength(128);
//设置权限
spp.setPermissions(ap);
//保护文档
document.protect(spp);
document.save("f:\\test02.pdf");
document.close();
```

> 异常：`java.lang.NoClassDefFoundError: org/bouncycastle/jce/provider/BouncyCastleProvider`

解决方案：添加`bcprov-jdk16`

```xml
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk16</artifactId>
    <version>1.46</version>
</dependency>
```

## 1.4.`PDF`文档属性

文档属性对应的类是`PDDocumentInformation`

- `File`:该属性保存文件的名称

- `Title`:文档的标题
- `Author`:文档的作者
- `Subject`:`PDF`文档的主题
- `Keywords`:可以搜索文档的关键字
- `Created`:文档修改的日期
- `Application`:设置文档的应用程序

![1540526074694](F:\1540526074694.png)

## 1.5.向现有文档中添加内容（一行）

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
//获取页面
PDPage page = document.getPage(0);
//PDPageContentStream插入各种数据元素
PDPageContentStream pcs = new PDPageContentStream(document, page);
//文本开始插入点
pcs.beginText();
//设置文本位置
pcs.newLineAtOffset(25,700);
//设置字体
//pcs.setFont(PDTrueTypeFont.load(document,new File("C:\\Windows\\FONTS\\times.ttf"), Encoding.getInstance(COSName.WIN_ANSI_ENCODING)),16);
pcs.setFont(PDType1Font.HELVETICA_BOLD,16);
//插入文本
//pcs.drawString("写入字符串");
pcs.showText("Font");
//文本结束插入点
pcs.endText();
//释放资源
pcs.close();
//保存文件
document.save("f:\\test02.pdf");
//
document.close();
```

## 1.6.添加多行文本

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
//获取页面
PDPage page = document.getPage(0);
//PDPageContentStream插入各种数据元素
PDPageContentStream pcs = new PDPageContentStream(document, page);
//文本开始插入点
pcs.beginText();
//设置文本位置
pcs.newLineAtOffset(25, 700);
//设置字体
PDFont font = PDType0Font.load(document, new File("c:/windows/fonts/times.ttf"));
pcs.setFont(font, 16);
//插入文本
pcs.setLeading(14.5f);
pcs.showText("Font");
pcs.newLine();
pcs.showText("Font2");
//设置文本位置
pcs.newLineAtOffset(50,50);
pcs.showText("Text");

//文本结束插入点
pcs.endText();
//释放资源
pcs.close();
//保存文件
document.save("f:\\test02.pdf");
//
document.close();
```

## 1.7.读取文本

`PDFTextStripper`类提供了从PDF文档中检索文本的方法

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\abc.pdf"));
//检索文本的类
PDFTextStripper pts = new PDFTextStripper();
//读取文本
String text = pts.getText(document);
System.out.println(text);
document.close();
```

## 1.8.插入图片

`PDImageXObject`提供了执行与图像相关的操作所需的方法，如插入图像，设置图像高度，设置图像宽

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
PDPage page = document.getPage(0);
//创建图片操作类
PDImageXObject image = PDImageXObject.createFromFile("F:/1540526074694.png", document);
//准备内容流
PDPageContentStream pcs = new PDPageContentStream(document, page);
//绘制图像
pcs.drawImage(image,50,500);
pcs.close();
document.save("f:/test02.pdf");
document.close();
```

## 1.9.将js添加到pdf文档

`PDActionJavaScript`类将js添加到PDF文档

```js
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
String js = "app.alert( {cMsg: 'this is an example', nIcon: 3, nType: 0,cTitle: 'PDFBox Javascript example'});";
//构建js的Action
PDActionJavaScript ajs = new PDActionJavaScript(js);
//向pdf添加js
document.getDocumentCatalog().setOpenAction(ajs);
document.save("f:\\test02.pdf");
document.close();
```

## 1.10.分割PDF中的页面

`Splitter`包含了分割给定的PDF文档的方法

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\阿里开发规范考试.pdf"));
//实例化分割类
Splitter splitter = new Splitter();
//将文档的每个页面分割为单独的文档
List<PDDocument> pages = splitter.split(document);
//遍历页面
Iterator<PDDocument> iterator = pages.listIterator();
int i = 0;
while (iterator.hasNext()) {
    PDDocument next = iterator.next();
    //每个页面单独一个文档
    next.save((i++) + ".pdf");
}
document.close();
```

## 1.11.合并多个文档

`PDFMergerUtility`将多个pdf文档合并为一个pdf文件

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
//创建合并pdf类
PDFMergerUtility merger = new PDFMergerUtility();
//设置目标文件
merger.setDestinationFileName("f:\\target.pdf");
//设置源文件
merger.addSource("f:\\abc.pdf");
merger.addSource("f:\\javascript.pdf");
//合并文档
merger.mergeDocuments(null);
document.close();
```

## 1.12.提取图像

`PDFRenderer`将PDF文档呈现为AWT BufferedImage

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\cassso-china-1.pdf"));
//创建渲染图像的类
PDFRenderer renderer = new PDFRenderer(document);
//渲染图像
BufferedImage bufferedImage = renderer.renderImage(0);
//保存图像
ImageIO.write(bufferedImage,"JPEG",new File("f:/myimage.jpg"));
document.close();
```

## 1.13.添加矩形

```java
//加载现有的pdf文件
PDDocument document = PDDocument.load(new File("f:\\test01.pdf"));
//获取页面
PDPage page = document.getPage(0);
//准备内容流
PDPageContentStream pcs = new PDPageContentStream(document, page);
//设置不划线颜色
pcs.setNonStrokingColor(Color.DARK_GRAY);
//绘制矩形
pcs.addRect(200, 650, 100, 100);
//填充矩形
pcs.fill();
pcs.close();
//保存
document.save("f:\\test02.pdf");
//释放资源
document.close();
```

## 1.14.`PDF`转图片

将一个pdf文件转化为图片，支持pdfbox2.0

```java
/**
     * 将PDF文件转化为图片
     * @param source 要转化的pdf文件
     * @param target 转化后图片的存放位置
     * @param imageType 转化的图片类型
     * @throws IOException
     */
public void toImage(String source,String target,String imageType) throws IOException {
    PDDocument document = PDDocument.load(new File(source));
    //获取页面总数
    int number = document.getNumberOfPages();
    //渲染类
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    //当前绘制的高度
    int heightTotal = 0;
    Graphics2D graphics = null;
    BufferedImage bi = null;
    BufferedImage rbi = null;

    for (int i = 0; i < number; i++) {
        //渲染每一页为图片
        bi = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
        if(i == 0){
            //初始化绘制的图像缓存大小
            rbi = new BufferedImage(bi.getWidth(), bi.getHeight() * number, BufferedImage.TYPE_INT_RGB);
            //创建绘制对象
            graphics = rbi.createGraphics();
        }
        //绘制图片
        graphics.drawImage(bi,0,heightTotal,null);
        //计算下一次绘制高度
        heightTotal += bi.getHeight();
    }
    graphics.dispose();
    ImageIO.write(rbi,imageType, new File(target));
    document.close();
}
```







