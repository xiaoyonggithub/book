# 一、`Git`

- git类似于链表的结构，将每次的提交串联起来，每次提交都会产生一个`SHA1`（唯一标识）
- 

## 1.1.`SVN`与`GIT`的区别

* `SVN`是集中式版本控制工具，有单点故障问题（就是服务器宕机了，版本就丢失）,断网后不能进行版本控制了
  * 增量式版本控制
* `Git`是分布式版本控制工具，本地保存有完整的版本历史，断网后不影响操作
  * 尽可能添加数据而不是删除或修改数据
  * 快照流，保存每个版本的快照





# 二、`Git`结构

【工作区】： 写代码

【暂存区】：历史存储

【本地库】：历史版本

```shell
#是从工作区添加到暂存区
git add 
#从暂存区提交到本地库 
git commit
#从本地库到远程库
git push
#从远程库到本地库
git clone
#拉取远程库的修改
git pull
#复制一个远程库
git fork
#fork的远程库合并到原来的远程库
fork的远程库--> pull request --> 审核 --> merge -->原始远程库
```

# 三、`Git`命令行操作

## 3.1.本地库初始化

```shell
git init
#.git目录存放的是本地库相关的子目录和文件
```

## 3.2.设置签名

```shell
#作用是区分开发人员的身份，与代码托管中心的用户没有关系
#【项目级别/仓库级别】：仅在当前的本地库有效
git config user.name xiaoyong 
git config user.email xiaoyong@163.com
#【系统用户级别】：登录当前操作系统的用户范围
git config --global user.name xiaoyong 
git config --global user.email xiaoyong@163.com
#优先级(就近原则)：项目级别优先于系统用户级别，不允许不存在用户级别
```

```shell
#查看用户信息保存的位置（config中[user]）
$ cat ./.git/config
[core]
        repositoryformatversion = 0
        filemode = false
        bare = false
        logallrefupdates = true
        symlinks = false
        ignorecase = true
[user]
        name = xiaoyong
        email = xiaoyong@163.com

#查看系统用户级别的用户信息
cat ~/.gitconfig
```

## 3.3.基本操作

```shell
#查看工作区、暂存区的状态
git status
#添加文件
git add text.txt
#回退添加的新增文件
git rm --cached test.txt
#提交
git commit test.txt -m 注释信息
git commit -m "注释信息" test.txt
#回退添加的修改文件
git reset HEAD test.txt
#对修改文件添加并提交
git commit -a text.txt 
```

```shell
#提交所有文件
git add -A
git commit -a -m "comment"
git push
```

```shell
git add -A  #提交所有变化
git add -u  #提交被修改（modified）和被删除的（deleted）文件，不包含新增的文件（new）
git add .   #提交被修改（modified）和新文件（new），不包括被删除（deleted）的文件
git add [file...] #添加指定文件到暂存区
git add [dir]  #添加指定目录到暂存区

```



## 3.4.查看历史记录

```shell
#显示提交的日志
git log 
#显示提交的日志，按简介的方式
git log --pretty=oneline
git log --oneline      #只显示当前版本历史版本
git log reflog
```

```shell
50403a8 HEAD@{1}: commit (initial): :
#HEAD@{1} 表示移动到当前版本需要移动的步数（{1}中的值）
#HEAD表示指针
```

## 3.5.前进后退

### 3.5.1.基于索引值操作(推荐)

```shell
git reflog
#调整指定的版本,14bfd43版本的标识
git reset --hard  14bfd43
【--soft】仅仅在本地库移动指针
【--mixed】在本地库移动指针，重置暂存区
【--hard】在本地库移动指针，重置暂存区，重置工作区
```

### 3.5.2.基于`^`符号，只能后退

```shell
#回退一个版本，几个^就后退几个版本
git reset --hard HEAD^   
git reset --hard HEAD^^^
```

### 3.5.3.基于`~`符号，只能后退

```shell
#后退3步
git reset --hard HEAD~3
```

## 3.6.帮助

```shell
git help reset  #查看帮助
```

## 3.7.删除文件恢复

```shell
#前提：删除前，文件存在时的状态提交到了本地库
#恢复删除的文件，只需要回退到未删除文件的版本（在本地库删除了）
git reset --hard 14bfd43
#恢复删除状态的在暂存区的文件
git reset --hard HEAD #重置暂存区和工作区
```

## 3.8.比较文件差异

```shell
#将工作区中的文件和暂存区的文件进行比较
git diff [文件名]
#将工作区的文件与本地库历史记录比较
git diff [本地库的历史版本] [文件名]
#不带文件名比较多个文件
git diff

git diff test.txt
```

```shell
 sdasd
 sasfaos
 sdasds
-sdas     #删除的行
+apply    #添加的行
```

# 四、分支管理

```shell
#查看所有分支
git branch -v
#新建分支
git branch hot_fix
#切换分支
git checkout hot_fix
#合并分支,[1]切换到接受修改的分支上（被合并的分支）,一般为master
git checkout master
git merge hot_fix
#合并冲突的解决,手动解决后，提交文件
git add test.txt
git commit -m "resolve confilict"    #此时不能带文件名
```

```shell
<<<<<<<<<<<<HEAD  #当前分支的内容
test edit by hot_fix
============
test edit by master
>>>>>>>>>>>master #另一分支的内容
```

# 五、`Git`的基本原理



# 六、远程仓库

## 6.1.记录远程地址

```shell
#记录远程地址，设置一个别名（origin）
git remote add origin https://github.com/xiaoyonggithub/xyeclipse.git
#查看备注地址
git remote -v
```

## 6.2.推送本地仓库到远程仓库

```shell
#推送本地仓库到远程仓库
git pull origin master
```

## 6.3.克隆远程库

```shell
#克隆远程仓库
git clone https://github.com/xiaoyonggithub/xyeclipse.git
```

### 6.3.1.克隆远程库的效果

* 完整的把远程库下载到本地
* 创建远程地址（`origin`）的别名
* 初始化本地库

## 6.4.拉取远程库

拉取操作`git pull`是操作：`git fetch`和`git merge`的合并

```shell
#抓取操作,只是把远程仓库的文件下载到本地,但没有修改本地库的文件  
git fetch [远程地址别名] [远程分支名]
git fetch origin matser
#查看抓取的文件
git checkout [远程地址别名/远程分支名]
git checkout origin/master
cat filename
#合并远程的master到本地的master
git merge origin/mater

#抓取远程的文件
git pull origin master
```

> 开发中一般先拉取（pull）代码，再推送（push）代码

## 6.5.跨团队开发

* 【远程身份】`fork`代码
* 克隆`fork`的仓库

```shell
git clone https://github.com/xiaoyonggithub/xyeclipse.git
```

* 本地修改，推送到远程仓库`push`
* `pull requests`

# 七、`SSH`和`HTTP`的免密登录

## 7.1.`HTTP`的免密登录

在`windows 10`中,在【凭据管理】中会记录你的账号和密码。

## 7.2.`SSH`的免密登录

* 初始化`ssh`

```shell
ssh-keygen -t rsa -C 15181278720@163.com
cd ~/.ssh
cat id_rsa.pub
```

```shell
git remote add origin_ssh git@github.com:xiaoyonggithub/xyeclipse.git
git remote -v
git push origin_ssh master
```

# 8.`gitlab`服务器的搭建







```
[track] 追踪
[Untracked]未追踪
[present]存在
[discard]取消
```

