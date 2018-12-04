# 一、激活

http://idea.lanyus.com/

[IDEA文档（https://github.com/tengj/IntelliJ-IDEA-Tutorial）](https://github.com/tengj/IntelliJ-IDEA-Tutorial)

## 1.1.激活失败

![1536669376502](E:\typora\images\1536669376502.png)

1、在C:\Windows\System32\drivers\etc\HOSTS中添加0.0.0.0 account.jetbrains.com

2、 在cmd中执行ipconfig /flushdns![Alt text]

# 二、模板（Template）

## 1.1.常用模板

### 1.1.1.`sout`

```java
//sout
System.out.println();
//soutp 打印形参
System.out.println("args = [" + args + "]");
//soutv 打印变量
System.out.println("args = " + args);
//soutm 打印方法名
System.out.println("IndividualPensionMgServiceImpl.main");
//xxx.sout 打印指定的变量
int num = 1;
System.out.println(num);
```

### 1.1.2.`psvm`

```java
public static void main(String[] args) {

}
```

### 1.1.3.`fori`

```java
String[] arr = new String[]{"Tom","Bom","LiLei","XiaoMing","Jerry"};
//fori 
for (int i = 0; i < arr.length; i++) {

}
//iter  增强for循环
for (String s : arr) {

}
//itar  循环赋值
for (int i = 0; i < arr.length; i++) {
    String s = arr[i];

}
```

### 1.1.4.`list.for`

```java
//list.for
ArrayList<String> list = new ArrayList<>();
for (String s : list) {

}
//list.fori
for (int i = 0; i < list.size(); i++) {

}
//list.forr
for (int i = list.size() - 1; i >= 0; i--) {

}
```

### 1.1.5.`ifn`

```java
//ifn
if (list == null) {

}
//inn
if (list != null) {

}
//xxx.nn
if (list != null) {

}
//xxx.null
if (list == null) {

}
```

### 1.1.6.`psf`

```java
//prsf
private static final
//psf
public static final
//psfi
public static final int
//psfs
public static final String
```

1.1.7.`thr`

```java
//thr
throw new
```

## 1.2.自定义模板

`$var$`设置光标的位置

```groovy
groovyScript("def result=''; def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList(); for(i = 0; i < params.size(); i++) {if(params[i] == '') return result;result+=' * @param ' + params[i] + ((i < params.size() - 1) ? '\\n' : '')}; return result", methodParameters())
```

1. 设置类注释`file->setting->Editor->Filr and Code Templates->Includes->File Header `
```
/**
 * @Author xiaoyong
 * @Description:
 * @Date: $date$ $time$
 * @Modified By:
 * /
```
2. 设置方法注释`file->setting->Editor->LiveTemplates点击右边上面那个绿色的+号`,设置参数的值
```groovy
groovyScript("def result=''; def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList(); for(i = 0; i < params.size(); i++) {result+=' * @param ' + params[i] + ((i < params.size() - 1) ? '\\n' : '')}; return result", methodParameters())
```
3. `Java`方法注释
```
/**
 * @Description:
 * @Author: xiaoyong 
 * @Date: $date$ $time$
$params$
 */
```
4. `Java`类注释
```
/**
 * @Description: $classname$
 * @Author: xiaoyong 
 * @Date: $date$ $time$
 */
```







# 三、快捷键

1. `Ctrl + E`可以打开最近编辑的文件列表,是支持搜索的，直接输入要搜索的内容或者简拼都可以
2. `Crtl+N`定位到`Java`类
3. `Crtl+Shift+N`定位到项目所有的文件
4. `Crtl+Alt+Shift+N`定位到标识,如`Java`类中的属性和函数名
5. 匹配原则
* 模糊匹配,`*`号可以匹配`0`个或多个字母,如：`*Serivce`、`*.xml`
* 骆驼字`(Camel words)`匹配,如`UserManagerDao`的骆驼字`UMD`，若为函数名和属性名称，如`getUerName`可以如此匹配`*UN`

6. `ALT+F1`查看的视图
7. 双击`shift` 全文搜索
8. `Crtl + D`复制当前行数据在下一行
9. `Alt+Enter`快速修复
10. `Ctrl+Shift+Enter`自动补全末尾的字符
11. `F2/ Shift+F2`移动到有错误的代码
12. `Ctrl+Space`基本的代码提示
13. `Ctrl+Shift+Space`按类型信息提示
14. `Ctrl+Shift+Alt+T`
15. `Shift+F6`重命名方法名或类名
16. `Ctrl+Alt+V`提取变量
17. `Alt+Insert`快速生成代码,如`get/set`等
18. `Ctrl + J`查看代码生成模板
19. 自动补全功能，如：`users.for`生成`for(User user : users)`
      `dto.getUser().var`+`tab`生成`ISysUser user = dto.getUser();`

20. `Ctrl + W`自动按语法选中代码
21. `Ctrl+Shift+W`反向选中代码
22. `Ctrl+[`或`Ctrl + ]`移动到前后代码块
23. `Ctrl + Shift + [`或`Ctrl + Shift + ]`选中光标到前后代码块
24. `Ctrl + →`或`Ctrl + ←`移动光标到前后单词
25. `Ctrl + Shift + →`或`Ctrl + Shift + ←`选中光标到前后单词
26. `Ctrl + Y `删除当前行
27. `Alt + Shift + Insert` 列选择模式，即多行选择或右键开启列选择模式
28. `Alt + 左键` 列选择
29. `Ctrl + H`打开类层次窗口
30. `Ctrl + F12`查看当前类的所有方法
31. `Alt + F7` 查找其它同名的方法或类
32. `Ctrl + B` 查看类或方法的定义
33. `Ctrl + Alt + B`查看类或方法的子类实现
34. `Ctrl + Alt + L`格式化代码
35. `Ctrl + Tab` 当前页面与上一个页面的切换
36. `Ctrl + +/-`
37. `Ctrl + Shift + +/-`

`ALT + 7` 、`Ctrl +F12`查看类的方法  `file structure `
`Ctrl + Y`删除当前行
`Ctrl + Shift + U`大小写转化
`Ctrl + E` 打开最近打开的文件
`Shift + F6`重命名
`Alt + 1` 打开`Project View`

## 3.1.智能提示
1.  `Ctrl+Space`基本代码提示
2.  `Ctrl+Shift+Space`按类型信息提示
3.  `F2/ Shift+F2`快速定位有错误的代码
4.  `Ctrl+Shift+Enter`自动补全末尾的字符，包含`{}、if/for`等
5.  `Ctrl + Alt + O`导入`import`

## 3.2.重构
1. `Ctrl+Shift+Alt+T`打开重构的面板
2. `Ctrl+Alt+V`提取变量
3. `Shift + F6`重命名

## 3.3.代码生成
1. `Ctrl + J` 查看所有代码生成的模板
2. `Alt+Insert`快速生成`构造函数、toString、getter/setter、重写父类方法`等
3. 后缀自动补全功能
* `for(User user : users)`只需输入`user.for+Tab`
* 要输入`Date birthday = user.getBirthday();`只需输入`user.getBirthday().var+Tab`即可

## 3.4.编辑
1. `Ctrl+W`自动按语法选中代码
2. `Ctrl+Shift+W`自动按语法反向选中代码

## 3.4.方法参数查看

1. `Ctr + P` 查看方法的参数信息
2. `Ctrl + Q`查看方法或类的`doc`

## 3.5.位置定位
1. `Ctrl + G` 定位到指定行数：列数
2. `Ctrl + [`代码块的开始
3. `Ctrl + ]`代码块的结束
4. `F12`回到最近打开的窗口
5. `Alt + →`回到之前的文件
6. `Alt + ←`回到之后的文件
7. `Ctrl + Shift + Backspace`定位到最后编辑的位置
8. `esc`  从`tool window`或其他`window`切换到文件编辑  
9. `Shift + esc`关闭最近打开的窗口

## 3.6.类、方法的结构查看与定位

1. `Ctrl + B` 跳转到类或方法的声明

2. `Ctrl + U`定位到类的父类或接口

3. `Ctrl + H`查看类的继承结构

4. `Ctrl + Shift + H`查看方法的继承结构

5. `Ctrl + Alt + H` 查看类或方法的调用情况

6. `Ctrl + Shift + I` 原地查看类和方法的声明，不进行跳转

7. `Alt+ ↑` 跳转到下一个方法

8. `Alt + ↓`跳转到上一个方法


# 四、项目创建

## 4.1.创建`maven`项目

![](E:\typora\images\1539786687783.png)



![1539786785601](E:\typora\images\1539786785601.png)

## 4.2.多项目模块的构建

多模块的项目修改模块的名称，只需要对应修改`.iml`文件的名称即可



# 五、IDEA虚拟参数的配置

IDEA的虚拟参数的配置在`idea64.exe.vmoptions`中

```properties
-Xms128m    #设置初始的内存数，增加该值可以提高 Java 程序的启动速度
-Xmx750m    #设置最大内存数，提高该值，可以减少内存 Garage 收集的频率，提高程序性能
-XX:ReservedCodeCacheSize=240m  #保留代码占用的内存容量
-XX:+UseConcMarkSweepGC
-XX:SoftRefLRUPolicyMSPerMB=50
-ea
-Dsun.io.useCanonCaches=false
-Djava.net.preferIPv4Stack=true
-XX:+HeapDumpOnOutOfMemoryError
-XX:-OmitStackTraceInFastThrow
```

若为64为的机器且内存不小于16G,可修改参数

```properties
-Xms512m    
-Xmx1500m   
-XX:ReservedCodeCacheSize=500m  
```

## 5.1.实例参数配置（VM Option）

```
-Xms256M -Xmx512M -XX:PermSize=256m -XX:MaxPermSize=512m 
```







# 六、常用设置

## 6.1.设置鼠标悬浮提示

![1532009167058](E:\typora\images\1532009167058.png)

## 6.2.设置自动导包

![1532009321735](E:\typora\images\1532009321735.png)

* Add unambiguous imports on the fly：自动导入不明确的结构
* Optimize imports on the fly：自动帮我们优化导入的包

## 6.3.  设置显示行号和方法间的分隔符

![1532009437287](E:\typora\images\1532009437287.png)

* Show line numbers：显示行数
* Show method separators： 显示方法分隔线

## 6.4. 忽略大小写提示





## 6.5.生成Javadoc

* Locale：输入语言类型：zh_CN
* Other command line arguments：-encoding UTF-8 -charset UTF-8

## 6.6.设置文件某些文件或文件夹不显示

![1532077779821](E:\typora\images\1532077779821.png)

实例配置

```
*.classpath;*.hprof;*.idea;*.iml;*.project;*.pyc;*.pyo;*.rbc;*.springBeans;*.yarb;*~;.DS_Store;.git;.hg;.myeclipse;.settings;.svn;CVS;__pycache__;_svn;overlays;target;vssver.scc;vssver2.scc;
```

注意：不能把数据目录（maven的target或Java项目的bin目录）忽略了，否则会发布不起项目



# 七、目录的结构

- 蓝色 –> 需要部署的目录
- 绿色 –> 测试目录
- 橘色 –>target目录
- 红色 –> 不存在的目录

# 八、插件

- ` All plugins `显示所有插件

- `Enabled `启用的插件

- `Disabled `禁用的插件

- `Bundled `自带的插件

- `Custom `自己安装的插件

  ![1539935997233](E:\typora\images\1539935997233.png)



## 8.1.`Maven Helper`

![1539933797489](E:\typora\images\1539933797489.png)

![1539934265855](E:\typora\images\1539934265855.png)





## 8.2.`GsonFormat`

![1539934661938](C:\Users\ADMINI~1\AppData\Local\Temp\1539934661938.png)

![1539934705966](E:\typora\images\1539934705966.png)

![1539934751309](E:\typora\images\1539934751309.png)



## 8.3.`Stack Overflow`

编码中几乎所有遇到的错误，都可以在Stack Overflow上找到，只不过默认使用Google搜索

## 8.4.`Background Image Plus`

`Background Image Plus`设置背景图片

![1540172851422](E:\typora\images\1540172851422.png)



## 8.5.`Lombok`

Lombok是一个简化Java代码插件

## 8.6.`CodeGlance`

类似SublimeText的Mini Map插件

## 8.7.`MyBatis plugin`



## 8.8.`MyBatisX`

- 通过`mapper`找到对应的`xml`
- 通过`xml`找到对应的`mapper`

## 8.9.`Alibaba Java Coding Guidelines`

`Alibaba Java Coding Guidelines`是《阿里巴巴Java开发规约》扫描插件

[Alibaba Java Coding Guidelines使用文档](https://github.com/alibaba/p3c/blob/master/idea-plugin/README_cn.md)



## 8.10.`.ignore`

`git`提交时过滤掉不需要提交的文件，如有些本地是不需要提交到`git`上的



## 8.11.`CamelCase`

- ` CamelCase`将不是驼峰格式的名称，快速转成驼峰格式

- 选中要修改的名称，按快捷键`shift+alt+u`

## 8.12.`Rainbow Brackets`

`Rainbow Brackets`彩虹颜色的括号



## 8.13.`grep console`

java开发的过程中，日志都会输出到console，输出的内容是非常多的，所以需要有一个工具可以方便的查找日志，或者可以非常明显显示我们关注的内容，`grep console`就是这样的一个工具。 安装完成后，在console中右键就能打开。

https://zhuanlan.zhihu.com/p/35241754



## 8.14.`MyBatis Log Plugin`

将日志的mybatis脚本转化为可执行的sql语句		

## 8.15.[`Translation`](http://yiiguxing.github.io/TranslationPlugin/start.html)





# 十、常见问题

## 10.1.修改IDEA中Maven默认编译的版本（JDK1.5）

* 方式一：在Maven中指定编译版本

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

## 10.2.IDEA打war包

![1531294876111](E:\typora\images\1531294876111.png)

## 10.3.maven红色波浪线

问题：idea中dependencies中总是有红色波浪线（缺少dependency）

解决方案：对于有波浪线的dependency，将其从pom中删除，保存后，再撤销回来

## 10.4.IDEA中compile、make、build

- Compile只编译选定的目标，不管之前是否已经编译过
- Make编译选定的目标，但是Make只编译上次编译变化过的文件，减少重复劳动，节省时间。
- Build是对整个工程进行彻底的重新编译，而不管是否已经编译过。Build过程往往会生成发布包，这个具体要看对IDE的配置 了，Build在实际中应用很少，因为开发时候基本上不用，发布生产时候一般都用ANT等工具来发布。Build因为要全部编译，还要执行打包等额外工 作，因此时间较长。

## 10.5.spring配置文件报红

错误：`application context not configured for this file`

解决方法：

![1537169269807](E:\typora\images\1537169269807.png)

这儿表示有一些spring文件没有配置，添加`+`

![1537169404165](E:\typora\images\1537169404165.png)

## 10.6.`dependencies`中总是有红色波浪线（缺少`dependency`）

对于有波浪线的`dependency`，将其从`pom`中删除，保存后，再撤销回来




