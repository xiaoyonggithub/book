# 一、软件管理

1.1.`apt`

`apt（advanced packaging tool）`是安装包管理工具，在`ubuntu`下可使用`apt`命令安装、删除、清理软件包。

`/etc/apt/source.list`指定了官方的软件仓库地址

```shell
apt-get --help
#更新源
apt-get update 
#安装包
apt-get install package
#删除包
apt-get remove  package

#搜索软件包
apt-cache search package
#获取包相关的信息，如说明、大小、版本等
apt-cache show package
#重新安装包
apt-get install package --reinstall

#修复安装
apt-get -f install
#删除包，包括配置文件
apt-get remove package --purge
#安装相关的编译环境
apt-get build-dep package

#更新安装的包
apt-get upgrade 
#升级系统
apt-get dist-upgrade
#了解安装包的依赖的包
apt-cache depends package
#查看该包被哪些包依赖
apt-cache redepends package
#下载包的源码
apt-get source package
```

```shell
#卸载vim
apt-get remove vim 
#安装vim
apt-get install vim
#查看vim的安装信息
apt-cache show vim 
```

## 1.2.修改仓库地址

`/etc/apt/source.list`指定了官方的软件仓库地址

```shell
https://mirror.tuna.tsinghua.edu.cn/help/ubuntu/   #清华大学镜像源

#备份系统默认源
sudo cp /etc/apt/sources.list /etc/apt/sources.list.bak
#编辑软件源文件
vim /etc/apt/sources.list
```

```shell
# 默认注释了源码镜像以提高 apt update 速度，如有需要可自行取消注释
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial main main restricted universe multiverse
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates main restricted universe multiverse
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse
deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security main restricted universe multiverse

# 预发布软件源，不建议启用
# deb https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-proposed main restricted universe multiverse
# deb-src https://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-proposed main restricted universe multiverse
```

```shell
#更新软件源
sudo apt-get update
#更新系统所有软件
sudo apt-get dist-upgrade
```

```shell
echo '' > source.list   #清空文件内容，需要root下执行
```

# 二、用户管理

## 2.1.设置`root`的密码

```shell
#设置用户密码
sudo passwd 
```

## 2.2.远程登录`ubuntu`

```shell
#安装ssh，openssh-server包含服务器端和客户端
apt-get install openssh-server
#启动ssh服务
service sshd restart
#查看22是否监听
netstat 
```

```shell
#linux远程登录另一台linux
ssh 用户名@ip
ssh root@192.148.184.131

#如访问出错，可查看看是否存在~/ssh/known_ssh，若存在可尝试删除

#登出
logout
exit 
```



