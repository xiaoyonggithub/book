#  一、虚拟化

## 1.1.传统虚拟化与`docker`虚拟化的区别

* 传统虚拟化：在硬件层面实现虚拟化，需要额外的虚拟管理应用和虚拟操作系统层
* `docker`虚拟化：在操作系统层面上实现虚拟化，直接复用本地主机的操作系统

![](E:\typora\images\微信图片_20180530195303.jpg)



# 二、安装`Docker`

`Docker`目前只能运行在64位平台上，并且要求内核版本不低于3.10,查看`linux`的内核版本：

```shell
uname -a
cat /proc/version
```

## 2.1.`Ubuntu`安装`Docker`

`Docker`目前支持最低的`Ubuntu`版本为`12.04 LTS`,实际上从稳定性上考虑，推荐至少使用`14.04 LTS`版本。

1. 安装`docker`

```shell
sudo apt-get install -y docker-engine
```

若系统中存在旧版本的`docker`，会提示是否删除，选择删除即可

2. 启动`docker`服务

```shell
sudo service docker start
```

## 2.2.`CentOS`安装`Docker`

`Docker`目前支持`CentOS 6.5`及以后版本，推荐使用`CentOS 7`

1. 添加`yum`软件源

```shell
touch /etc/yum.repos.d/docker.repo   #新增docker软件源配置
```

```shell
#docker.repo文件的内容 
[docker-ce-stable]
name=Docker CE Stable - $basearch
baseurl=https://download.docker.com/linux/centos/7/$basearch/stable
enabled=1
gpgcheck=0 #我把这里设置成了0、说明我信任了这个源，不对它的rpm进行检察
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-stable-debuginfo]
name=Docker CE Stable - Debuginfo $basearch
baseurl=https://download.docker.com/linux/centos/7/debug-$basearch/stable
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-stable-source]
name=Docker CE Stable - Sources
baseurl=https://download.docker.com/linux/centos/7/source/stable
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-edge]
name=Docker CE Edge - $basearch
baseurl=https://download.docker.com/linux/centos/7/$basearch/edge
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-edge-debuginfo]
name=Docker CE Edge - Debuginfo $basearch
baseurl=https://download.docker.com/linux/centos/7/debug-$basearch/edge
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-edge-source]
name=Docker CE Edge - Sources
baseurl=https://download.docker.com/linux/centos/7/source/edge
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-test]
name=Docker CE Test - $basearch
baseurl=https://download.docker.com/linux/centos/7/$basearch/test
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-test-debuginfo]
name=Docker CE Test - Debuginfo $basearch
baseurl=https://download.docker.com/linux/centos/7/debug-$basearch/test
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
 
[docker-ce-test-source]
name=Docker CE Test - Sources
baseurl=https://download.docker.com/linux/centos/7/source/test
enabled=0
gpgcheck=1
gpgkey=https://download.docker.com/linux/centos/gpg
```

> `centos7`软件包和依赖包已经包含在默认的 `CentOS-Extras` 软件源里 

2. 安装`docker`

```shell
sudo yum update
sudo yum install -y docker-engine
sudo service docker start 
systemctl start docker.service
```

3. 通过脚本安装`docker`

```shell
curl -fsSL https://get.docker.com/|sh  
wget -qO- https://get.dcoker.com/|sh
```

4. 设置`docker`自启动

```shell
systemctl enable docker.servcie
```



# 三、镜像

`Dokcer`运行容器前需本地存在对应的镜像，若本地不存在镜像，会尝试从默认的镜像仓库下载（默认使用`Docker Hub`公共注册服务器中的仓库下载）。

## 3.1.获取镜像

```shell
docker pull NAME：[TAG]  
【-a|--all-tags】是否拉取仓库中所有的镜像，默认为否
【NAME】镜像仓库的名称
【TAG】镜像的标签,通常用来表示版本

#若不指定TAG,默认会拉取latest标签，即仓库中最新版本的镜像
docker pull ubuntu
```

镜像文件一般有若干层`（layer）`组成,`f2aa67a397c4`这种串是层的唯一标识`（id）`，完整的id包括256bit，有64个十六进制组成

```shell
docker pull mysql
```

```shell
Using default tag: latest     #标签，即版本
Trying to pull repository docker.io/library/mysql ...   #仓库
sha256:d60c13a2bfdbbeb9cf1c84fd3cb0a1577b2bbaeec11e44bf345f4da90586e9e1: Pulling from docker.io/library/mysql
f2aa67a397c4: Pull complete    #层信息
1accf44cb7e0: Pull complete 
2d830ea9fa68: Pull complete 
740584693b89: Pull complete 
4d620357ec48: Pull complete 
ac3b7158d73d: Pull complete 
a48d784ee503: Pull complete 
f122eadb2640: Pull complete 
3df40c552a96: Pull complete 
da7d77a8ed28: Pull complete 
f03c5af3b206: Pull complete 
54dd1949fa0f: Pull complete 
Digest: sha256:d60c13a2bfdbbeb9cf1c84fd3cb0a1577b2bbaeec11e44bf345f4da90586e9e1
Status: Downloaded newer image for docker.io/mysql:latest  
```

不同镜像包含相同的层时，本地仅存储层的一份内容，减少需要的存储空间。

使用不同的镜像仓库服务器的情况，可能会有镜像重名的情况，此时需要添加仓库的地址（即`Register`,注册服务器）做作前缀。

```shell
docker pull [-a|--all-tags] [--help] NAME[:TAG] | [REGISTRY_HOST[:REGISTRY_PORT]/]NAME[:TAG]

docker pull register.hub.docker.com/tomcat:8.0
#若不是从默认的仓库下载，也需要添加仓库地址
```

## 3.2.查看镜像信息

```shell
docker images
【-a,-all=true|false】 列出所有的镜像文件（包含临时文件），默认否
【--digests】列出镜像数字摘要值
【-f,--filter】过滤列出的镜像
【--format=""】控制输出格式，[.ID]表示ID,[.Repository]表示仓库信息
【--no-trunc】对输出结果太长的部分进行截取，默认是true
【-q,--quit】仅输出ID信息

man docker-images  #查看帮组
```

```shell
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
docker.io/mysql     latest              a8a59477268d        3 weeks ago         445 MB

【REPOSITORY】 镜像仓库地址
【TAG】        标签
【IMAGE ID】   镜像ID
【CREATED】    创建时间
【SIZE】       镜像大小，此大小只是镜像的逻辑大小，实际由于相同的镜像层本地只会存储一份，物理上占用的存储空间小于各镜像的逻辑体积之和
```

## 3.3.添加镜像标签

```shell
docker tag mysql:latest mymysql:latest  
```

## 3.4.查看镜像的详细信息

```shell
#返回的是有个JDON
docker inspect mysql:latest
```

```shell
#只查看JSON中的一项内容
docker inspect -f {{".Config"}} mysql:latest  
```

## 3.5.查看镜像历史

```shell
docker history mysql:latest             #列出各层的创建信息，过长的命令会自动截取掉
docker history --no-trunc mysql:latest  #输出完整的信息
```

## 3.6.搜索镜像

```shell
docker search 
【--automated】仅显示自动创建的镜像
【--no-trunc】
【-s,--stars=X】仅显示指定星级以上的镜像，默认为0

docker search --automated -s 3 nginx
```

```shell
【INDEX】  
【NAME】       镜像名称
【DESCRIPTION】描述
【STARS】      星级（表示受欢迎程度）
【OFFICIAL】   是否官方创建
【AUTOMATED】  是否自动创建
#默认按星级评价排序
```

![](E:\typora\images\Snipaste_2018-05-31_22-01-38.png)

## 3.7.删除镜像

* 同一镜像拥有多个标签时，只删除该镜像的标签，不删除镜像
* 镜像只存在一个标签时，会删除镜像
* 指定镜像ID会删除镜像（能区分的部分ID前缀）
* 若删除的镜像创建的容器存在，镜像文件默认是无法删除的

```shell
docker rmi [-f|--force] [--help] [--no-prune] IMAGE [IMAGE...]
【IMAGE】删除镜像的标签或ID
【-f|--force】强制删除一个存在容器依赖的镜像，不推荐
```

## 3.8.创建镜像

### 3.8.1.基于已有镜像的容器创建

```shell
docker commit [-a|--author[=AUTHOR]] [-c|--change[=[DOCKERFILE INSTRUCTIONS]]] [--help] [-m|--message[=MESSAGE]] [-p|--pause[=true]] CONTAINER [REPOSITORY[:TAG]]
【-a|--author=""】 作者信息
【-c|--change=[]】提交时执行的Dokerfile指令
【-m|--message=""】提交信息
【-p|--pause】提交时暂停容器运行
【CONTAINER】容器标识,如ID
【REPOSITORY[:TAG]】存放的仓库
```

```shell
#Dockerfile的指令
【CMD】
【ENTRYPOINT】
【ENV】
【EXPOSE】
【LABEL】
【ONBUILD】
【USER】
【VOLUME】
【WORKDIR】
```

```shell
docker commit -m "创建镜像" -a "xy" 8bfd42907609 test:0.1
```

### 3.8.2.基于本地模板的导入

```shell
docker import [-c|--change[=[]]] [-m|--message[=MESSAGE]] [--help] file|URL|-[REPOSITORY[:TAG]]
```

```shell
#下载模板
curl https://download.openvz.org/template/precreated/centos-7-x86_64.tar.gz -o /tmp/centos-7-x86_64.tar.gz  --progress
```

```shell
cat centos-7-x86_64.tar.gz | docker import - centos:7.0
```

[OpenVZ模板的下载地址](https://download.openvz.org/template/precreated/)

## 3.9.存出和载入镜像

```shell
#导出镜像到本地文件
docker save [--help] [-o|--output[=OUTPUT]] IMAGE [IMAGE...]
docker save -o tomcat_8.0.tar tomcat:8.0
```

```shell
#将导出的tar文件在导入到本地镜像库
docker load [--help] [-i|--input[=INPUT]] [-q|--quiet]
#会导入镜像及其相关的元数据信息（包括标签）
docker load --input tomcat_8.0.tar
docker load < tomcat_8.0.tar
```

## 3.10.上传镜像

```shell
#将镜像上传到仓库
docker push [--help] NAME[:TAG] | [REGISTRY_HOST[:REGISTRY_PORT]/]NAME[:TAG]

docker tag tomcat:8.0 user/tomcat:8.0
docker push  user/test:latest
```

# 四、容器

容器是镜像的一个运行实例，不同之处是镜像是一个只读文件，而容器带有运行时需要的可写文件层。

## 4.1.新建容器

```shell
#docker create创建的容器处于停止状态
docker create -it ubuntu:latest
#启动容器
docker start 47b0f55f3fe3
#新建并启动容器
docker run ubuntu /bin/echo 'Hello World'
```

> `docker create`和`docker run`的参数分类

> * 与容器运行模式相关的选项

![与容器运行模式相关的选项](E:\typora\images\微信图片_20180531230939.jpg)

> * 与容器环境和配置相关的选项

![与容器环境和配置相关的选项](E:\typora\images\微信图片_20180531230919.jpg)

![](E:\typora\images\微信图片_20180531230933.jpg)

> * 与容器资源限制和安全相关的选项

![与容器资源限制和安全相关的选项](E:\typora\images\微信图片_20180531230939.jpg)

![与容器资源限制和安全相关的选项](E:\typora\images\微信图片_20180531230928.jpg)

```shell
#查看运行容器
docker ps -a 
```

### 4.1.1.`docker run`创建并启动容器执行的后台操作

1. 检查本地是否存在指定的镜像，不存在就从公有仓库下载
2. 利用镜像创建一个容器，并启动容器
3. 分配一个文件系统给容器，并在只读的镜像层外挂载一层可读写层
4. 从宿主主机配置的网桥接口桥接一个虚拟接口到容器中
5. 从网桥的地址池配置一个IP地址给容器
6. 执行用户指定的应用程序
7. 执行完毕后容器会自动终止

```shell
docker run -it ubuntu:latest /bin/bash
【-t】让docker分配一个伪终端（pseudo-tty）,并绑定到容器的标准输入上
【-i】让容器的标准输入保持打开
【-d】让容器在后台一守护形式运行

exit    #退出容器
ctrd+d  #退出容器
```

## 4.2.容器退出的错误代码

【125】`Docker deamon`执行出错，如指定了不支持的`Docker`命令参数

【126】所指定的命令无法执行，如权限不足

【127】容器内命令无法找到

命令执行后错误，会默认返回错误码

## 4.3.获取容器的输出信息

```shell
docker logs [-f|--follow] [--help] [--since[=SINCE]] [-t|--timestamps] [--tail[="all"]] CONTAINER
```

## 4.4.终止容器

```shell
docker stop [--help] [-t|--time[=10]] CONTAINER [CONTAINER...]

docker stop ce5(容器id)
```

首先向容器发送`SIGTERM`信号，等待一段超时时间（默认10秒）后，在发送`SIGKILL`信号历来终止容器。

```shell
docker kill [--help] [-s|--signal[="KILL"]] CONTAINER [CONTAINER...]
```

直接发送`SIGKILL`信号来强行终止容器。

> 注意：当`docker`容器中指定的应用终结时，容器也会自动终止。
>
> 如：如对`ubuntu`只启动一个终端的容器，退出终端后，创建的容器也立即终止。

## 4.5.查看容器信息

```shell
docker ps [-a|--all] [-f|--filter[=[]]] [--format="TEMPLATE"] [--help] [-l|--latest] [-n[=-1]] [--no-trunc] [-q|--quiet] [-s|--size]

[-a|--all] ： 查看所有容器

docker ps -qa   #查看所有容器id
docker ps -a
docker start ce5  #重新启动容器
```

## 4.6.重启容器

```shell
docker restart [--help] [-t|--time[=10]] CONTAINER [CONTAINER...]
#重启容器
dokcer restart ce5 
```

## 4.7.进入容器

在启动时进入后台（`-d`），用户无法看见容器中的信息，也无法进行操作。此时需进入容器的方式：

### 4.7.1.`attach`

```shell
docker attach [--detach-keys[=[]]] [--help] [--no-stdin] [--sig-proxy[=true]] CONTAINER

[--detach-keys[=[]]]:指定退出aatach模式的快捷键，默认是[Ctrl+Q]/[Ctrl+P]
[--no-stdin] : 是否关闭标准输入，默认打开
[--sig-proxy[=true]]：是否代理收到的系统信号给应用进程

docker attach blissful_saha
```

> 当多个窗口同时使用attach连接到统一容器时，所有的窗口都会同步显示。
>
> 当某个窗口因命令阻塞时，其他窗口也无法执行操作。

### 4.7.2.`exec`

```shell
docker exec [-d|--detach] [--detach-keys[=[]]] [-e|--env[=[]]] [--help] [-i|--interactive] [--privileged] [-t|--tty] [-u|--user[=USER]] CONTAINER COMMAND [ARG...]

[-i|--interactive]:打开标准输入输出接受用户输入命令，默认false
[--privileged]:是否给执行命令以高权限，默认false
[-t|--tty]:分配伪终端，默认false
[-u|--user[=USER]]:执行命令的用户名或id

 docker exec -it 985 /bin/bash
```

### 4.7.3.`nsenter`工具

```shell
cd tmp
curl https://www.kernel.org/pub/linux/utils/util-linux/v2.24/util-linux-2.24.tar.gz | tar -zxf
cd util-linux-2.24
./configure --without-ncurses
make nsenter && cp nsenter /usr/local/bin
```

```shell
#nsenter要连接到容器，需要获取容器的PID,如下
PID=${docker inspect --format "{{.State.Pid}}"} <container>
#通过PID连接到容器
nsenter --target $PID --mount --uts --ipc --net --pid
```

```shell
docker run -it ubuntu
docker ps 
PID=${docker-pid 243c32532da8}   #获取PID
nsenter --target 10981 --mount --uts --ipc --net --pid
```

## 4.8.删除容器

```shell
docker rm [-f|--force] [-l|--link] [-v|--volumes] CONTAINER [CONTAINER...]

[-f|--force] ：是否强制终止并删除一个运行的容器
[-l|--link]  ：删除容器的连接，单保留容器
[-v|--volumes]：删除容器挂载的数据卷

docker rm ce73628
```

默认情况下`docker rm`只删除处于终止或退出状态的容器，并不能删除运行的容器。

【-f】`Docker`会先发送`sigkill`信号给容器，终止其中的应用，再强行删除容器。

## 4.9.导入和导出容器

主要用于容器的迁移

### 4.9.1.导出

导出容器是指导出一个已创建的容器到一个文件，不管容器是否运行

```shell
docker export [--help] [-o|--output[=""]] CONTAINER
[-o|--output[=""]] 指定导出的文件名(*.tar)

docker export -o mysql_export.tar ce42323
docker export hs231293 > mysql_export.tar
```

### 4.9.2.导入

```shell
docker import [-c|--change[=[]]] [-m|--message[=MESSAGE]] file|URL|-[REPOSITORY[:TAG]]
[-c|--change[=[]]] 在导入的同时执行对容器进行修改的Dockerfile指令

#导入一个容器的快照到本地镜像库
docker import mysql_export.tar -test/mysql:v1.0
```

### 4.9.3.`dokcer load`与`docker import`的区别

* `docker import`导入容器的快照文件，将丢失所有的历史记录和元数据信息（即仅保存容器当时的快照信息）;从容器快照文件导入时，可重新指定标签等元数据信息。
* `docker load`镜像存储文件将保存完整的记录，体积也更大。



# 五、仓库

`docker`仓库：集中存放镜像的地方，有公有仓库和私有仓库。

注册服务器`(Register)`:存放仓库的具体服务器，一个注册服务器上可有多个仓库

```shell
#仓库地址
private-docker.com/ubuntu
【private-docker.com】注册服务器
【ubuntu】仓库名，可将仓库看作具体的项目或目录
```

## 5.1.[`Docker Hub`](https://hub.docker.com)

```shell
#登录docker
docker login [--help] [-p|--password[=PASSWORD]] [-u|--username[=USERNAME]] [SERVER]

```



# 二十、异常

## 20.1.`docker`启动报错

```shell
systemctl start docker.service
```

```shell
Job for docker.service failed because the control process exited with error code. See "systemctl status docker.service" and "journalctl -xe" for details.
```

```shell
systemctl status docker.servcie
```

![](E:\typora\images\Snipaste_2018-05-31_17-06-42.png)

> 原因分析

```shell
此linux的内核中的SELinux不支持 overlay2 graph driver ，解决方法有两个，要么启动一个新内核，要么就在docker里禁用selinux，--selinux-enabled=false
```

> 解决方案

```shell
vi /etc/sysconfig/docker
#设置
--selinux-enabled=false
```

![](E:\typora\images\20180521105946344.png)

## 20.2.仓库连接不稳定

```shell
error pulling image configuration: Get https://production.cloudflare.docker.com/registry-v2/docker/registry/v2/blobs/sha256/8b/8bfd42907609ea4754df0dff451d44ae777af3128af22078fbb734da06404b86/data?verify=1527767415-qH%2FUfz%2FyOBSCT4aIeSSAKhcT9oE%3D: net/http: TLS handshake timeout
```

![](E:\typora\images\Snipaste_2018-05-31_19-02-27.png)

> 解决方法

```shell
#重启docker服务
systemctl restart docker.service
```

