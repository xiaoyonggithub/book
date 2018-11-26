# 一、虚拟机

## 1.1.  虚拟机网络连接方式

* 桥连接模式

  分配的`IP`地址是局域网段中的`IP`

  `Linux`在局域网中可以与其他的系统通信，但是可能会操作`IP`冲突 ，外网与`Linux`可以相互访问

* `NAT`模式

  母机会存在两个`IP`地址，一个是主机的正常`IP`，另一个是任意的一个网段，主机与`Linux`形成的一个局域网形式

  网络地址转化方式，`IP`不能冲突，`Linux`可以访问外网（代理），外网不能找到`Linux`

* 主机模式

  虚拟机中的`Linux`是一台独立的主机，外网与`Linux`之间不能相互通信

## 1.2.`Linux`分区

* 引导分区（boot）
* 交换分区（swap）
* 根分区

## 1.3.`VMware Tools`

1. 可以在`Window`和`Linux`之间相互粘贴
2. 可以在`Windows`和`Linux`中设置共享的文件夹
3. 安装`VMware Tools`工具
   * 在虚拟机中点击"安装VMware Tools"
   * centos 会出现一个 vm 的安装包
   * 点击右键解压, 得到一个安装文件
   * 进入该 vm 解压的目录 ，该文件在 /root/桌面/vmware-tools-distrib/下
   * 安装 ./vmware-install.pl
   * 全部使用默认设置即可
   * 需要 reboot 重新启动即可生效

## 1.4.虚拟机的`NAT`模式没有网络

要确定这几个服务是开启的

![](E:\typora\images\Snipaste_2018-06-04_15-23-53.png)

# 二、目录管理

## 2.1. 目录结构

* `/`:根目录
* `/bin`: 存放常用的指令，用户二进制文件
* `/sbin`:存放管理员使用的系统管理指令，系统二进制文件
* `/home`:存放普通用户的主目录，每个用户都有一个自己的目录
* `/root`:系统管理员，root用户主目录，只有root用户具有该目录下的写权限
* `/boot`:存放启动linux时使用的一些核心文件，包含一些连接文件和镜像文件
* `/proc`:是一个虚拟目录，是系统内存的映射，访问这个目录获取系统信息，系统进程信息
* `/srv`:服务数据,包含服务器特定服务相关的数据,service缩写，存放一些服务启动之后需要提取的数据,如/srv/cvs包含cvs相关的数据
* `/sys`:安装2.6内核中新出现的一个文件系统
* `/tmp`:系统和用户创建的临时文件，系统重启后，该目录下的所有文件会被删除
* `/dev`:类似于windows的设备管理器，把所有硬件用文件的形式存储，即设备文件
* `/media`:可移动媒体设备,用于挂载可移动设备的临时目录,linux会自动识别的一些设备，如U盘、光驱等，Linux识别后会把识别到的设备挂载到这个目录下
* `/mnt`:挂载目录,临时安装目录，系统管理员可以挂载文件系统
* `/etc`:所有程序的配置文件和用于启动/停止单个程序启动和关闭的shell脚本
* `/var`:变量文件（内容可能增长的文件），如系统日志文件（/var/log）;包和数据库文件（/var/lib）;电子邮件（/var/mail）;打印队列（/var/spool）;锁文件（/var/lock）;多次重新启动需要的临时文件（/var/tmp）
* `/usr`:用户程序（包含二进制文件、库文件、文档和二级程序的源代码）
  * `/usr/bin`:用户程序的二进制文件
  * `/usr/sbin`:系统管理员的二进制文
  * `/usr/lib`:包含了/usr/bin和/usr/sbin用到的库
  * `/usr/local`:包含了从源安装的用户程序
* `/boot`:包含引导加载程序文件，内核的initrd、vmlinux、grub文件位于/boot下
* `/lib`:系统库,包含支持位于/bin和/sbin下的二进制文件的库文件,库文件名为 ld*或lib*.so.*
* `/opt`:可选的附加应用程序,附加应用程序应该安装在/opt/或者/opt/的子目录下
* 

![](E:\typora\images\Snipaste_2018-05-22_11-19-27.png)

## 2.2.目录结构的含义

蓝色：表示目录

青色：表示连接

黑色：表示文件

## 2.3.查看是否开启远程服务

```shell
setup
# 查看sshd服务是否开启  有*号表示开启,只有开启了远程服务才能远程连接
```

# 三、`VI/VIM`编辑器

Vim 的全局配置一般在`/etc/vim/vimrc`或者`/etc/vimrc`，对所有用户生效;用户个人的配置在`~/.vimrc`.

- 如果只对单次编辑启用某个配置项，可以在命令模式下，先输入一个冒号，再输入配置

```shell
:set number
```

## 3.1. `VIM`的三种模式

### 3.1.1.正常模式

```
默认的模式，可以使用快捷键，删除，复制，粘贴
esc -- 回到正常模式
x   -- 删除光标处的字符
dd  -- 删除光标所在的行
5dd -- 删除光标下的5行(包含光标所在行)
u   -- 撤销最近的修改
U   -- 撤销当前行上所有的修改
r   -- 替换光标位置上的一个字符
R   -- 替换从光标位置开始的字符，且进入插入模式
.   -- 重复上次的修改
yy  -- 复制当前行
5yy -- 拷贝光标下的5行(包含光标所在行)
p   -- 粘贴
gg  -- 定位到文档首行
G   -- 定位到文档末行
```

### 3.1.2.插入模式

```
I,i,O,o,A,a,R,r可进入插入模式，进行编辑
I -- 在光标所在行的行首输入正文
i -- 在光标左侧输入正文
O -- 在光标所在行的上一行增添新行，光标位于新行的行首	
o -- 在光标所在行的下一行增添新行，光标位于新行的行首
A -- 在光标右侧输入正文
a -- 在光标所在行的行尾输入正文
R
r
```

### 3.1.3.命令模式

```
：或/进入命令模式
w  -- 写入
q  -- 退出
q! -- 强制退出，不保存文件
ZZ -- 保存文件，退出Vim编辑器
/hello      -- 查找hello,n查找下一个
：set nu    -- 显示行号
：set nonu  -- 隐藏行号
```

### 3.1.4.可视模式

```
V,V,Ctrl+V 进入可视模式
```

> 定位到指定的行
>
> * 显示行号  set nu
> * 在正常模式下，输入要跳转的行号（20）
> * shift + g

## 3.2.`ViM`配置

- `vim --version`查看vim支持的功能以及配置文件目录
- 关闭一般在配置项前加`no`，`set nonumber`
- 查询某个配置项是否打开，在配置项后面添加问号，`set number?`
- 查看帮助`help number`

### 3.2.1.基本配置

#### 3.2.1.1.显示行号

```shell
set number    #显示行号
set nonumber  #关闭行号
set number?   #查看行号配置是否打开
```

#### 3.2.1.2.设置是否与`Vi`兼容

```shell
set nocompatible #即值采用Vim自己的命令
```

#### 3.2.1.3.打开语法高亮

```shell
syntax on  #此时会自动识别代码，使用多种颜色显示
```

#### 3.2.1.4.显示模式

```shell
set showmode  #在底部显示当前处于的模式
```

#### 3.2.1.5.命令模式在底部显示命令

```shell
set showcmd #命令模式下，在底部显示当前输入的命令
```

- 当输入`d`时，表示操作完成，显示消失，如输入`sy3d`，则在显示底部显示`sy3`

#### 3.2.1.6.设置支持鼠标

```shell
set mouse=a
```

#### 3.2.1.7.设置编码

```shell
set encoding=utf-8
```

#### 3.2.1.8.启用256色

```shell
set t_Co=256
```

#### 3.2.1.9.开启文件类型检查

```shell
filetype indent on
```

开启文件类型检查，并且载入与该类型对应的缩进规则

- 如编辑的是`.py`文件，Vim 就是会找 Python 的缩进规则`~/.vim/indent/python.vim`

### 3.2.2.缩进

#### 3.2.2.1.下一行缩进与上一行保持一致

```shell
set autoindent
```

#### 3.2.2.2.设置`Tab`的空格数

```shell
set tabstop=2  #设置按下 Tab 键时，Vim 显示的空格数
```

#### 3.2.2.3.设置每级缩进的字符

在文本上按下`>>`（增加一级缩进）、`<<`（取消一级缩进）或者`==`（取消全部缩进）时，每一级的字符数

```shell
set shiftwidth=4
```

#### 3.2.2.4.`Tab`转空格

由于 Tab 键在不同的编辑器缩进不一致，该设置自动将 Tab 转为空格

```shell
set expandtab
```

#### 3.2.2.5.`Tab`转化的空格数

```shell
set softtabstop=2
```

### 3.2.3.外观

#### 3.2.3.1.显示光标所在行号

显示光标所在的当前行的行号，其他行都为相对于该行的相对行号。

```shell
set relativenumber
```

#### 3.2.3.2.光标所在行高亮

```shell
set cursorline
```

#### 3.2.3.3.设置行宽

设置行宽，即一行显示多少个字符

```shell
set textwidth=80
```

#### 3.2.3.4.设置折行

```shell
set wrap     #自动折行，太长的行分成几行显示
set nowrap   #关闭自动折行
```

#### 3.2.3.5.设置折行的符号

只有遇到指定的符号（比如空格、连词号和其他标点符号），才发生折行；防止在单词内部折行

```shell
set linebreak
```

#### 3.2.3.6.设置折行的右边距

指定折行处与编辑窗口的右边缘之间空出的字符数

```shell
set wrapmargin=2
```

#### 3.2.3.7.滚动的上下边距

垂直滚动时，光标距离顶部/底部的位置（单位：行）

```shell
set scrolloff=5
```

#### 3.2.3.8.滚动的左右边距

水平滚动时，光标距离行首或行尾的位置（单位：字符）。该配置在不折行时比较有用

```shell
set sidescrolloff=5
```

#### 3.2.3.9.状态栏

是否显示状态栏

- `0`不显示
- `1`只在多窗口时显示
- `2`显示

```shell
set laststatus=2
```

#### 3.2.3.10.光标的位置

在状态栏显示光标的当前位置（位于哪一行哪一列）。 

```shell
set ruler
```

### 3.2.4.搜索

#### 3.2.4.1.括号匹配

光标遇到圆括号、方括号、大括号时，自动高亮对应的另一个圆括号、方括号和大括号

```shell
set showmatch
```

#### 3.2.4.2.高亮匹配结果

搜索时，高亮显示匹配结果

```shell
set hlsearch
```

#### 3.2.4.3.自动跳转匹配结果

输入搜索模式时，每输入一个字符，就自动跳到第一个匹配的结果。 

```shell
set incserach
```

#### 3.2.4.4.忽略大小写

搜索时忽略大小写

```shell
set ignorecase
```



### 3.2.5.编辑

#### 3.2.5.1.开启英语单词拼写检查    

```shell
set spell spelllang=en_us
```

#### 3.2.5.2.开启备份

默认情况下，文件保存时，会额外创建一个备份文件，它的文件名是在原文件名的末尾，再添加一个波浪号（〜）

```shell
set backup  #开启备份
set nobackup #关闭备份
```

#### 3.2.5.3.创建交换文件

交换文件主要用于系统崩溃时恢复文件，文件名的开头是`.`、结尾是`.swp`

```shell
set swapfile    #创建交换文件
set noswapfile  #不创建交换文件
```

#### 3.2.5.4.保留撤销历史

Vim 会在编辑时保存操作历史，用来供用户撤消更改；默认情况下，操作记录只在本次编辑时有效，一旦编辑结束、文件关闭，操作历史就消失了。

打开这个设置，可以在文件关闭后，操作记录保留在一个文件里面，继续存在。这意味着，重新打开一个文件，可以撤销上一次编辑时的操作。撤消文件是跟原文件保存在一起的隐藏文件，文件名以`.un~`开头。

```shell
set undofile
```

#### 3.2.5.5.设置备份文件保存位置

设置备份文件、交换文件、操作历史文件的保存位置

结尾的`//`表示生成的文件名带有绝对路径，路径中用`%`替换目录分隔符，这样可以防止文件重名

```shell
set backupdir=~/.vim/.backup//  
set directory=~/.vim/.swp//
set undodir=~/.vim/.undo//
```

#### 3.2.5.6.自动切换工作目录

自动切换工作目录；用在一个 Vim 会话之中打开多个文件的情况，默认的工作目录是打开的第一个文件的目录；

开启该配置可以将工作目录自动切换到，正在编辑的文件的目录

```shell
set autochdir
```

#### 3.2.5.7.关闭出错的声音

出错时，不要发出响声

```shell
set noerrorbells
```

#### 3.2.5.8.出错视觉提示

出错时，发出视觉提示，通常是屏幕闪烁

```shell
set visualbell
```

#### 3.2.5.9.记住历史操作次数

Vim 需要记住多少次历史操作

```shell
set history=100
```

#### 3.6.10.文件监视

打开文件监视。如果在编辑过程中文件发生外部改变（比如被别的编辑器编辑了），就会发出提示。

```shell
set autoread
```

#### 3.2.5.11.行尾空格可视化

如果行尾有多余的空格（包括 Tab 键），该配置将让这些空格显示成可见的小方块。 

```shell
set listchars=tab:»■,trail:■
set list
```

#### 3.2.5.12.命令自动补齐

命令模式下，底部操作指令按下 Tab 键自动补全。

- 命令模式下，底部操作指令按下 Tab 键自动补全。
- 第二次按下 Tab，会依次选择各个指令。

```shell
set wildmenu
set wildmode=longest:list,full
```

## 3.3.







# 四、用户管理

## 4.1. 用户的登录和注销

```
su -用户名      -- 切换用户
logout         -- 注销用户，在图形运行级别无效
```

## 4.2.用户的管理

### 4.2.1.家目录

```
/home/xy   用户xy的家目录
用户登录成功后会进入到自己的家目录，即/home/xy
```
### 4.2.2.用户

* 用户至少要属于一个组
* 用户信息放在`/etc/paaswd`（用户配置文件）中

```
用户名:口令:用户标识号:组标识号:注释性描述:主目录    :登录 Shell
zwj   :x  :1001     :1001    :         :/home/zwj:/bin/bash
```

* 口令配置文件（密码和登录信息，是加密文件），`/etc/shadow`

```
登录名:加密口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志
zwj:!!:17670:0:99999:7:::
```

* 添加用户

```
useradd [选项]  用户名

user xy   --  创建用户xy
若创建用户的时候未指定组，此时会创建一个与用户名称相同的组（xy）,并把用户（xy）放在该组(xy)中

useradd -d 目录 用户名  -- 指定家目录，不要指定存在的目录
```

* 删除用户

```
userdel xy      -- 删除用户，保留家目录
userdel -r xy   -- 删除用户，同时删除家目录
实际生产中一般都会保留家目录
```

* 查询用户信息

```shell
id 用户名

id xy
uid=1000(xy) gid=1000(xy) 组=1000(xy)
用户id        组id         组名
```

* 切换用户

```
su -用户名

若从高权限用户切换到低权限用户，不需要输入密码
若从低权限用户切换到高权限用户，就需要输入密码

exit -- 退回到原来的用户
```
### 4.2.3.组

类似于角色，对有共性的多个角色进行统一管理

* 组配置文件（`/etc/group`）

```
组名:口令:组标识号:组内用户列表
xy  :x   :1000   :xy

```

* 添加组

```
groupadd 组名
```

* 删除组

```
groupdel 组名
```

* 新增用户时指定组

```
useradd -g 组名 用户名

groupadd wudang    -- 创建武当组
useradd -g wudang zhangwuji   --创建用户指定到wudang组
```

* 修改用户组

```
usermod -g 组名 用户名

groupadd shaolin  --创建少林组
usermod -g shaolin zhangwuji  --修改张无忌到少林组中
```



# 五、常用指令

## 5.1. 运行级别

```
0 :  关机，系统默认级别不能设置为0，否则不能正常启动
1 ： 单用户（找回密码），root权限，不需要密码登录，用于系统维护，禁止远程登录
2 ： 多用户无网络服务，没有NFS
3 ： 多用户有网络服务,有NFS,登录后进入控制台命令模式
4 :  保留级别，系统未使用，保留
5 :  图形界面，系统登录进入图形界面
6 ： 重启，系统会正常关闭并重启；系统默认级别不能设置为6，否则不能正常启动
```

> 运行级别的配置文件`/etc/inittib`

### 5.1.2. 切换到指定的运行级别

```shell
init [0123456]
init 3    -- 切换到3号级别

#设置默认的运行级别
修改/etc/inittab中的级别即可，`id:3:initdefault`

若默认级别为0或者6时，可进入单用户模式中修改运行级别
```

### 5.2 .如何找回丢失的root密码

进入单用户模式，然后修改root密码即可。原因：单用户模式下，root用户不需要登录就可以登录

```
passwd 密码
```

1. 在引导的时输入`enter`
2. 输入`e`，选中内核
3. 输入`e`,编辑输入`空格 输入1`
4. `enter`，输入完成，输入`b`，进入到单用户模式
5. `passwd 密码`修改密码

## 5.3.文本处理工具

### 5.3.1.`grep`基于关键字搜索

```shell
grep [选项] 要搜索的文件
【-i】搜索是忽略大小写
【-n】显示结果所在的行数
【-v】反转，输出不带关键字的行
【-Ax】在输出内容时包含结果所在行之后的指定行数（After）
【-Bx】在输出内容时包含结果所在行之前的指定行数（Before）

#查询单个关键字时可以不加引号（''，""）
grep 'linxcast' /etc/passwd       #在/etc/passwd中搜索关键字linuxcast,
find / -user xy | grep -i video   #查询用户xy的所有文件中包含video的文件
find / -user xy 2> /dev/null | grep video  #忽略错误信息 
```

### 5.3.2.`cut`基于列处理文本

```shell
cut [选项] 要处理的文件
【-d】指定分割符（默认为Tab）
【-f】指定输出的列号
【-c】基于字符进行分割

cut -d: -f3 /etc/passwd                   #基于：进行分割，输出第三列
grep liunxcat /etc/passwd | cut -d: f3    
```

### 5.3.3.`wc`文本统计

```shell
wc [选项] 文件       #count word
【-l】只统计行数（line）
【-w】只统计单词数（words）
【-c】只统计字节数（chars）
【-m】只统计字符数

wc -l /etc/passwd    #统计用户数，/etc/password每一行表示一个用户
```

```shell
 wc alloutput.log 
 1    3      53 alloutput.log
 行数 单词数  字节数
```

### 5.3.4.`sort`文本排序

```shell
sort [选项] 要排序的文件
【-r】进行倒序排序
【-n】基于数字排序
【-f】忽略带小写
【-u】删除重复行
【-t SEP】使用SEP作为分隔符分割为列进行排序
【-k x】当进行基于指定字符分割为列进行排序时，指定基于哪个列排序

sort -r text.txt 
sort -u text.txt 
```

### 5.3.5.删除重复行

```shell
sort -u     #可以删除重复行，但结果进行了排序
uniq        #可以删除重复的相邻行
```

### 5.3.6.`diff`文件比较

```shell
diff [选项] 源文件 目标文件    #比较两个文件的区别
【-i】忽略带小写
【-b】忽略空格的变化
【-u】统一显示比较信息（一般用于生成补丁(patch)文件）

diff linuxcast linuxcast-new
diff -u linuxcast linuxcast-new > final.patch  #生成补丁文件
```

### 5.3.7.`aspell`检查拼写

```shell
aspell check [options] filename  #检查文本中的拼写错误
aspell list  [options] < filename
```

### 5.3.8.`tr`处理文本内容

```shell
tr [OPTION] SET1 [SET2]  # translate or delete characters
【-d】 删除

tr -d 'TMD' < linuxcast       #删除关键字
tr 'a-z' 'A-Z' < linuxcast    #转换大小写
```

### 5.3.9.`sed`搜索替换 

```shell
sed 's/linux/unix/g' linuxcast    #将全部的liunx替换为unix (g:global全部替换)
sed '1,50s/linux/unix/g' linuxcast
sed -e 's/linux/unix/g’ -e ‘s/nash/nash_su/g' linuxcast   #(-e:多个替换)
sed -f sededit linuxcast  
```

## 5.4. 帮助指令

### 5.4.1.`man`

```
man 【命令或配置文件】   -- 获取帮组信息

man ls 
```

### 5.4.2.`help`

```
help 命令
```

### 5.4.3.`info`

```
info 命令
info crontab 
```

## 5.5.文件目录类

### 5.5.1.`pwd`

```
pwd  显示当前工作目录的绝对路径
```

### 5.5.2.`ls`

```
ls [选项] [目录或文件]

-a 显示当前目录所有的文件或目录，包含隐藏文件
-l 显示详细信息，以列表的形式显示
```

5.5.3.`cd`

```
cd 【参数】   -- 切换到指定个目录

cd ~  : 回到家目录
cd    : 回到家目录
cd .. : 回到上级目录
```

### 5.5.4.`mkdir`

```
mkdir [选项]  目录

mkdir /home/dog     --  创建目录

-p    -- 创建多级目录
mkdir -p /home/animal/tiger
```

### 5.5.5.`rmdir`

```
rmdir [选项]  要删除的空目录
rmdir /home/dog    --  删除空目录
rm -rf /home/dog   --  删除非空目录
```

### 5.5.6.`touch`

```
touch 文件名      -- 创建空文件
touch text.txt    -- 创建文件text.txt
touch hello.java test.java  -- 可一次创建多个文件
```

### 5.5.7.`cp`

```
cp 拷贝文件到指定的目录
cp [选项] source dist
-r 递归复制整个文件夹

--拷贝单个文件
cp test.txt /home/xy 
--复制dog目录到zwj目录下
cp -r /home/dog/ zwj/
--复制时强制覆盖文件
\cp -r /home/dog/ zwj/
```

### 5.5.8.`mv`

```
-- 移动文件或重命名
mv old_file_name new_file_name     -- 重命名
mv test.txt hello.txt              -- 重命名，将text.txt重命名为hello.txt

mv move_file target_folder         --移动文件
mv test.txt /home/zwj/
```

### 5.5.9.`rm`

```
rm [选项]  要删除的目录或文件
-f  强制删除不提示
-r  递归删除整个目录

rm test.txt     --  删除文件
rm -rf dog/     --  删除整个文件夹
```

### 5.5.10.`cat`

```
cat [选项]  要查看的文件    -- 查看文件内容
-n  显示行号

cat /home/text.txt     
cat -n /etc/profile | more   --分页显示内容
```

### 5.5.11.`more`

```
more 以一个基于vi编辑器的文本过滤器，以全屏幕的方式按页显示文本内容。
more 要查看的文件
more /etc/profile

快捷键
空格键（space）    -- 向下翻一页
enter             -- 向下翻一行
q                 -- 退出
Ctrl+F            -- 向下滚动一屏
Ctrl+B            -- 返回上一屏
=				 --  输出当前行的行号
：f               -- 显示文件名和当前行号

```

### 5.5.12.`less`

```
用来分屏查看文件内容，并不是一次将整个文件加载后才显示，而是按需加载，对于显示大型文件有较高的效率

less 要查看的文件

快捷键 
enter     -- 向下翻一行
space     -- 向下翻一页
pagedown  -- 向下翻一页
pageup    -- 向上翻一页
/字符串    -- 向下查找，n:向下查找，N:向上查找
？字符串   -- 向上查找，n:向上查找，N:向下查找
q         -- 退出

```

### 5.5.14.`cal`

```
查看日历
cal
```

### 5.5.15.`tail`

```
tail 显示文件尾部的内容，默认显示文件的后10行
tail 文件
tail -n 5 test.txt      -- 显示文件的后5行
tail test.txt           -- 显示文件的后10行
tail -f test.log        -- 实时追踪文档所有的更新内容，实时监控日志
```

### 5.5.16.`echo`

```
echo [选项]  输出内容
echo $PATH           --输出当前标变量的路径
echo "Hello World"   --输出文本
```

### 5.5.17.`head`

```
head 显示文件的开头
head 文件名           --默认显示前10行
head test.txt        -- 查看文件前十行
head -n 5 test.txt   -- 查看文件前五行
```

### 5.5.18.`ln`

```
软连接（符号链接），类似windows的快捷方式，主要存放链接其他文件的路径
ln -s [源文件或目录] [软连接名] 

ln -s /root rootLink     -- 给root目录建立一个软链接rootLink
rm -rf rootLink          -- 删除软链接，注意rootLink/后不要带/
```

### 5.5.19.`history`

```
history查看已经执行过的历史指令，也可以执行历史指令

history       -- 查看所有的历史指令
history 10    -- 显示最近执行的10个指令
!10           -- 执行第10条历史指令
```

## 5.6.时间日期类指令

### 5.6.1.`date`

```
date 显示日期
date         -- 显示当前时间
date +%Y     -- 显示当前年份
date +%m     -- 显示当前月份
date +%d     -- 显示当前日期
date "+%Y-%m-%d %H:%M:%S"   -- 显示日期，格式“2017-09-29 12:29：22”

date -s 字符串时间       -- 设置日期 
date -s "2017-08-09"    -- 设置系统时间为“2017-08-09”

date -d "2017-10-23 12:22：22"   -- 显示字符串的日期
date -r test.txt                 -- 显示文件最后的修改日期
```

### 5.6.2.`cal`

```
cal显示日历
cal         -- 显示当前月份的日历
cal 2018    -- 显示2018的日历
```

## 5.7.压缩和解压缩

### 5.7.1.`gzip/gunzip`

```
gzip 压缩文件，只能将文件压缩为*.gz的文件

gzip 文件名   
gzip text.log            --压缩后，源文件不存在了，只有压缩文件
gzip text.txt test.log   --压缩多个文件，得到test.log.gz和text.txt.gz
```

```
gunzip  解压缩文件,只能解压缩*.gz的文件
```

### 5.7.2.`zip/unzip`

```
zip 压缩文件，在项目打包中常使用
zip [选项] 压缩的文件  要压缩的文件或目录
-r   递归压缩文件

zip -r home.zip /home    -- 压缩/home目录
```

```shell
unzip [选项] 要解压文件
-d   指定解压的目录

unzip -d /home/dog/ home.zip   -- 解压home.zip到/home/dog目录下	
```

### 5.7.3.`tar`

```
tar打包指令
tar [选项] fie_name.tar.gz    -- 压缩后的文件为*.tar.gz

-c  : 建立新的存档
-f  : 指定压缩后的文件名
-v  : 显示详细信息
-z  : 用 gzip 对存档压缩或解压
-x  : 解压文件

tar -zcvf dog.tar.gz test.log  text.txt   --将文件test.log和text.txt压缩为dog.tar.gz
tar -zcvf home.tar.gz  /home/             --压缩文件夹/home

tar -zxvf dog.tar.gz                      -- 解压文件
tar -zxvf dog.tar.gz -C /tmp              -- 解压到指定目录/tmp,指定的目录要存在
```

### 5.7.4.各个压缩指令的区别

## 5.8. 关机和重启指令

```
shutdown 
shutdown -h now  -- 立即关机
shutdown -h 1    -- 1分钟后关机
shutdown -r now  -- 立即重启

halt     -- 立即关机
reboot   -- 立即重启
sync     -- 把内存中的数据同步到磁盘
```

## 5.9.查找和定位

### 5.9.1.`which`



### 5.9.2.`whereis`



### 5.9.3.`find`



### 5.9.4.`locate`



## 5.10.`shell`/监测

### 5.10.1.`sh`

```shell
sh #shell命令语言解释器，执行命令从标准输入读取或从一个文件中读取
```

## 5.11.网络管理

### 5.11.1.`curl`

```shell
curl [选项]   #用来执行下载、发送各种HTTP请求，指定HTTP头部等操作
【-f】 连接失败时不显示http错误
【-s】 静默模式。不输出任何东西
【-S】 显示错误

curl http://man.linuxde.net/text.iso --silent -O
```

### 5.11.2.`wget`

```shell
wget [选项]  #指定的URL下载文件
wget http://www.linuxde.net/testfile.zip
```

## 5.12.搜索指令

### 5.12.1.`find`

```shell
find [path...] [expression] [action]
[-name filename] 搜索指定的文件，完全匹配
通配符：  
		【*】匹配任意内容
		【?】匹配任意一个字符
		【[]】匹配[]中指定的字符
【-iname】忽略大小写	
    
处理动作action:
【-print】显示出文件的相对路径（相对于搜索起点）
【-exec cmd /】执行指定的shell命令
```

```shell
#列出当前目录及子目录下所有文件和文件夹
find .  
#在/home目录下查找以.txt结尾的文件名
find /home -name "*.txt"
find /home -iname "*.txt"  #忽略大小写
#当前目录及子目录下查找所有以.txt和.pdf结尾的文件
find . \( -name "*.txt" -o -name "*.pdf" \)
find . -name "*.txt" -o -name "*.pdf" 
#匹配文件路径或者文件
find /usr/ -path "*local*"

```

### 5.12.2.`which`

```shell
#依序从PATH环境变量所列的目录中找出command的位置,并显示完整路径的名称
#在找到第一个符合条件的程序文件时，就立刻停止搜索，省略其余未搜索目录
which ls   #/usr/bin/ls
```

```shell
#查看PATH
echo $PATH

[root@localhost bin]# echo $PATH
/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/root/.local/bin:/root/bin
```

### 5.12.3.`whereis`

```shell
#找出特定程序的可执行文件、源代码文件以及manpage的路径
#所提供的name会被先除去前置的路径以及任何.ext形式的扩展名,只会在标准的Linux目录中进行搜索
whereis [option] name
【-b】只搜索可执行文件
【-m】只搜索manpage
【-s】只搜源代码文件
【-B directory】更改或限定搜索可执行的文件的目录
【-M directory】更改或限定搜索manpage的目录
【-S directory】更改或限定搜索源代码文件的目录
```

### 5.12.4.`locate`

```shell

```

## 5.13.用户相关指令

### 5.13.1.`finger`

```shell
finger username   #显示用户的信息
```

### 5.13.2.`who`

```shell
who  #显示当前登录用户
```

### 5.13.3.`sudo`

```shell
sudo commond
sudo ls      #以root用户身份执行
```

## 5.14.`shell`

### 5.14.1.`history`

```shell
history  # 显示在当前shell下命令历史
```

### 5.14.2.`alias`	

```shell
alias  #显示所有的命令别称
alias nls=ls  #修改命令ls的别称为nls
```

### 5.14.3.`env`

```shell
env    # 显示所有的环境变量
export  var = value #设置环境变量var为value
```

## 5.15.显示硬盘、分区、CPU、内存信息

### 5.15.1.`df`

```shell
df -lh   #显示磁盘使用情况
```

### 5.15.2.`du`

```shell
du -sh *  #显示当前目录下各个目录和文件的大小
```





# 六、组管理和权限管理

`linux`中的每个用户必须属于一个组。

`linux`中的每个文件有所有者、所在组、其他组的概念。

## 6.1.文件的所有者

一般文件的创建者就是文件的所有者。

## 6.1.1.查看文件的所有者

```shell
ls -alh     -- 查看文件的所有者
```

```shell
groupadd police         -- 创建组police
useradd -g police tom   -- 创建用户tom
passwd tom              -- 设置tom的密码
touch test.log          -- 使用tom的登录后，创建文件
ls -ahl test.log        -- 查看文件额所有者
```

```shell
-rw-r--r--. 1 tom     police 0 5月  20 22:50 test.log
			 所有者   所在组		
```

### 6.1.2.修改文件的所有者

```shell
chown  用户名 文件名
-R  ： 递归修改目录下所有的子目录和子文件
chown xy test.log            # 修改test.log的所有者为xy

chown owner:group file_name  # 同时修改文件的所有者和所在组
chown tom:police test.log  

chown -R tom tom/            # 修改tom目录下的所有文件和目录的所有者为tom,一般使用root修改 
```

## 6.2.文件/目录的所在组

默认情况下文件的所在组就是文件所有者所在组。

### 6.2.1.修改文件所在组

```shell
chgrp 组名 文件名
-R  ： 递归修改目录下所有的子目录和子文件

chgrp xy test.log     # 修改文件所在组
chgrp -R police tom/            # 修改tom目录下的所有文件和目录的所在组为police,一般使用root修改 
```

## 6.3.其他组

除文件的所有者和所在组的用户外，系统的其他用户都是文件的其他组。

## 6.4.权限

```shell
-rw-r--r--. 1 xy xy 0 5月  20 22:50 test.log
                             
                                        所有者                文件的大小，目录为4096
-        rw-    r--    r--     .    1   xy              xy    0   5月  20 22:50      test.log
文件类型  所有者 所在组  其他组        文件：文件的硬链接数 所在组     文件最后的修改时间  文件名
                                     目录：子目录的数量
```

### 6.4.1.文件类型

* `-`: 表示普通文件
* `d`: 目录
* `l`:软链接文件
* `c`:字符设备(鼠标，键盘)
* `b`:块文件（硬盘）

### 6.4.2.文件的权限类型

* 【r】: 只读
* 【w】:可写，可以修改文件；不代表可删除文件
  * 若该文件所在的目录具有写权限，可删除
  * 若该文件所在的目录不具有写权限，不可删除
*  【x】: 可执行

### 6.4.3.目录的权限类型

* 【r】: 可读，ls查看目录
* 【w】:可写，目录内可创建、删除和重命名目录
* 【x】: 可进入目录

### 6.4.4.权限的数值

* 【r=4】
* 【w=2】
* 【x=1】

### 6.4.5.隐藏目录

* 【.】当前目录
* 【..】上一级目录

### 6.4.6.文件的大小

* 【文件】 显示文件的大小
* 【目录】都是4096

## 6.5.修改权限

```shell
chmod 修改文件或目录的权限

chmod u=rwx,g=rx,o=x  文件/目录名       #设置权限
chmod 751 文件/目录名                   #等同于上一个

chmod o+w 文件/目录名                   #给其他组添加w权限
chmod a-x 文件/目录名                   #给所有人都删除x权限
```

### 6.5.1.权限的类型

* 【u】所有者
* 【g】所在组
* 【o】其他组
* 【a】所有人（包含u,g,o的总和）

# 七、定时任务调度

任务调度：指系统在某个时间执行特定的命令或程序。

任务调度的分类：

1. 系统工作：有些重要的工作必须周而复始地执行，如病毒扫描等。
2. 个别用户工作：个别用户可能希望执行某些程序，比如对 mysql 数据库的备份。

```shell
crontab [选项]
【-e】  编辑crontab定时任务
【-l】  查询crontab任务
【-r】  删除当前用户所有的crontab的任务
```

```shell
crontab -l    #查看任务调度
crontab -r    #终止任务调度
service crond restart #重启任务调度
```

## 7.1.简单的任务调度

```shell
crontab -e 
*/1 * * * * ls -l /etc>>/tmp/crontab.log   #定时任务
```

### 7.1.1.占位符`*`的含义

第一个【*】：分钟，【0-59】

第二个【*】：小时，【0-23】

第三个【*】：日期，【1-31】

第四个【*】：月份，【1-12】

第五个【*】：星期几，【0-7】，0和7都代表星期日

## 7.2.特殊字符的含义

【*】：  表示任何时间，如第一个星号表示每小时执行一次

【,】：  表示不连续的时间，如`【0 8,12,16 * * *】` 表示每天的8点，12点，16点执行。

【-】：表示连续的时间，如【0 5 * * 1-6】表示周一至周五的凌晨5点0分执行

【*/n】:表示每个多久执行一次

```shell
*/10 * * * *   #表示每个10分钟就执行一次
```

## 7.3.实例

```shell
#每隔 1 分钟，就将当前的日期信息，追加到 /tmp/mydate 文件中
#1.编写脚本mytask.sh
vim mytask.sh
date>>/tmp/mydate 
#2.给脚本一个可执行权限
chmod u+x mytask.sh
#3.设置调度时间
crontab -e
*/1 * * * * /home/mytask.sh


#每隔 1 分钟， 将当前日期和日历都追加到 /home/mycal 文件中
#1.编写脚本mytask.sh
vim mytask.sh
date>>/tmp/mycal 
cal>>/tmp/mycal
#2.给脚本一个可执行权限
chmod u+x mytask.sh
#3.设置调度时间
crontab -e
*/1 * * * * /home/mytask.sh 


#每天凌晨 2:00 将 mysql 数据库 testdb ，备份到文件中mydb.bak
#1.编写脚本mytask.sh
vim mytask.sh
/usr/local/mysql/bin/mysqldump -u root -proot testdb > /tmp/mydb.bakdate>>/tmp/mycal 
#2.给脚本一个可执行权限
chmod u+x mytask.sh
#3.设置调度时间
crontab -e
0 2 * * * /home/mytask.sh 
```

