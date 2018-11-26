## 八、磁盘分区与挂载

### 8.1.分区的方式

- `mbr`分区
- `gtp`分区

#### 8.1.1.`mbr`分区

- 主分区和扩展分区最多只能有4个
- 扩展分区的只能有一个
- 系统只能装在主分区上
- 最大只支持2TB,但拥有最好的兼容性

#### 8.1.2.`gtp`分区

- 支持无限个主分区（但操作系统可能有限制，如windows下最多128个分区）
- 最大支持18EB的大容量（EB=1024PB,1PB=1024TB）
- windows7 64位以后支持gtp

### 8.2.硬盘的分类

- `IDE`硬盘，标识符【hdx~】

  【hd】表示分区所在设备的类型

  【x】盘号，a为基本盘，b为基本从属盘，c为辅助主盘，d为辅助从属盘

  【~】分区，前四个分区用数字1-4表示，他们是主分区或扩展分区；从5开始表示是逻辑分区

  > 【hda3】表示第一块磁盘上的第三个主分区或扩展分区

- `SCSI`硬盘(目前基本是SCSI硬盘)，标识符【sdx~】

  【sd】分区所在设备的类型

  【x】盘号，a为基本盘，b为基本从属盘，c为辅助主盘，d为辅助从属盘

  【~】分区，前四个分区用数字1-4表示，他们是主分区或扩展分区；从5开始表示是逻辑分区

#### 8.2.1.查看磁盘分区和挂载情况

```shell
lsblk -f    #老师不离开
```

```shell
分区情况     文件类型           唯一标识（40位的uuid）				   挂载点
NAME        FSTYPE      LABEL UUID                                   MOUNTPOINT
sda                                                                  
├─sda1      xfs               a0a844cd-9d7d-4b95-97d5-c8c3a6889259   /boot
└─sda2      LVM2_member       HImw1E-6o79-XpcF-KKuh-C6dX-0CGc-UT2SnE 
  ├─cl-root xfs               11f854dd-1740-4644-888c-9977f02591ed   /
  └─cl-swap swap              63c9944e-4502-4355-85f3-4cd94fea7c6a   [SWAP]
```

```shell
lsblk
```

```shell
NAME        MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
sda           8:0    0   30G  0 disk 
├─sda1        8:1    0    1G  0 part /boot
└─sda2        8:2    0   29G  0 part 
  ├─cl-root 253:0    0   27G  0 lvm  /
  └─cl-swap 253:1    0    2G  0 lvm  [SWAP]
sr0          11:0    1 1024M  0 rom              #光驱
```

### 8.3.新增磁盘案例

为`liunx`新增一块硬盘，并挂载到`/home/mnt`下

1. 添加硬盘
2. 分区

```shell
fdisk /dev/sdb   #磁盘分区	
```

```shell
a   toggle a bootable flag
b   edit bsd disklabel
c   toggle the dos compatibility flag
d   delete a partition
g   create a new empty GPT partition table
G   create an IRIX (SGI) partition table
l   list known partition types
m   print this menu
n   add a new partition							#新建一个分区
o   create a new empty DOS partition table
p   print the partition table
q   quit without saving changes
s   create a new empty Sun disklabel
t   change a partition's system id
u   change display/entry units
v   verify the partition table
w   write table to disk and exit			     #写入分区表并退出
x   extra functionality (experts only)
```

```shell
Partition type:									#分区类型
   p   primary (0 primary, 0 extended, 4 free)     #主分区
   e   extended									#扩展分区
```

1. 格式化

   ```shell
   mkfs -t ext4 /dev/sdb1             #将分区格式化为ext4文件类型
   ```

2. 挂载

   ```shell
   mount 设备名     挂载的目录
   mount /dev/sdb1 /home/mnt         #将分区sdb1挂载到mnt,属于临时挂载，重启机器后挂载关系不存在了
   ```

   ```shell
   umount 设备名/挂载的目录           #卸载挂载
   umount /dev/sdb1
   umount /home/mnt
   ```

   ```shell
   [root@localhost mnt]# umount /dev/sdb1
   umount: /home/mnt：目标忙。
           (有些情况下通过 lsof(8) 或 fuser(1) 可以
            找到有关使用该设备的进程的有用信息)
   #表示你正在这个分区的挂载目录下
   ```

3. 设置可以自动挂载（永久挂载）

   ```shell
   vim /etc/fstab  #记录磁盘的挂载情况
   mount -a        #自动挂载
   ```

   ```shell
   /dev/sdb1               /home/mnt               ext4    defaults        0 0 #挂载sdb1 
   /dev/mapper/cl-root     /                       xfs     defaults        0 0
   UUID=a0a844cd-9d7d-4b95-97d5-c8c3a6889259 /boot xfs     defaults        0 0
   /dev/mapper/cl-swap     swap                    swap    defaults        0 0
   ```

4. 结果

   ```shell
   sdb                                                                  
   └─sdb1      ext4              1b99efca-e468-47e7-8a80-21eb36555da0   /home/mnt
   ```

### 8.4.磁盘情况查询

#### 8.4.1.系统磁盘整体使用情况

```shell
df -lh
```

```shell
文件系统             容量   已用   可用 已用% 挂载点
/dev/mapper/cl-root   27G  4.6G   23G   17% /
devtmpfs             897M     0  897M    0% /dev
tmpfs                912M   84K  912M    1% /dev/shm
tmpfs                912M  9.0M  903M    1% /run
tmpfs                912M     0  912M    0% /sys/fs/cgroup
/dev/sda1           1014M  173M  842M   18% /boot
tmpfs                183M   16K  183M    1% /run/user/42
tmpfs                183M     0  183M    0% /run/user/0
```

#### 8.4.2.查询指定目录的磁盘占用情况

```shell
du -h /目录   #不指定目录时，默认查询当前目录
【-s】目录占用大小汇总
【-h】带计量单位
【-a】含文件
【--max-depth=1】子目录的深度
【-c】列出明细的同时，增加汇总值
```

```shell
du -ach --max-depth=1 /home
```

```shell
49M	/home/xy
16K	/home/zwj
15M	/home/dog
16K	/home/tom
20K	/home/jack
12K	/home/jerry
12K	/home/xh
12K	/home/xq
0	/home/mnt
64M	/home
64M	总用量
```

#### 8.4.3.常用指令

- 统计`/home`目录下的文件数，不包含子目录下的文件

```shell
ls -l /home | grep "^-"    #查询/home下的文件
ls -l /home | grep "^-" | wc -l
```

- 统计`/home`目录下目录数，不包含子目录下的目录

```shell
ls -l /home | grep "^d" | wc -l	
```

- 统计`/home`目录下的文件数，包含子目录下的文件

```shell
ls -lR /home | grep "^-" | wc -l
```

- 统计`/home`目录下目录数，包含子目录下的目录

```shell
ls -lR /home | grep "^d" | wc -l
```

- 以树状结构显示目录结果

```shell
yum install tree   #安装tree
tree
```

## 九、网络配置

### 9.1.`linux`网络环境配置

- 自动配置

> 缺点:每次启动后自动获取的IP可以不一样，不适用于服务器端

- 指定固定`IP`

```shell
vim /etc/sysconfig/network-scripts/ifcfg-eth0    #eth0网卡的配置文件
vim /etc/sysconfig/network-scripts/ifcfg-ens33   #ens33网卡的配置文件
```

```shell
BOOTPROTO=static          #以静态的方式获取IP
ONBOOT=yes
IPADDR=192.168.186.129    #静态IP
GATEWAY=192.168.28.1      #网关
DNS1=192.168.28.1         #与网关保持一致即可
```

```shell
service network restart  #修改后重启网络服务，才能生效
systemctl restart network.service
```

```shell
TYPE=Ethernet                             #网络类型
BOOTPROTO=dhcp
DEFROUTE=yes
PEERDNS=yes
PEERROUTES=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_PEERDNS=yes
IPV6_PEERROUTES=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens33
UUID=ea85301f-696c-4bd1-a852-9d553e5cee1d #UUID
DEVICE=ens33                              #接口名称（设备，网卡）
ONBOOT=yes  						    #系统启动时网络接口是否有效
```

## 十、进程管理

- `linux`中，每个执行的程序（代码）都称为一个进程。每个进程多分配一个ID号。
- 每个进程，都会对应一个父进程。而这个父进程可以复制多个子进程。

### 10.1.显示系统执行的进程

```shell
ps 查看系统中，哪些进程正在执行，以及它们执行的状态
【-a】查看所有进程
【-u】以用户的格式显示进程信息
【-x】显示后台进程运行的参数
```

```shell
PID  TTY      TIME     CMD
2821 pts/0    00:00:00 bash
7548 pts/0    00:00:00 ps
【PID】进程ID
【TTY】终端机号
【TIME】此进程所消耗CPU的时间
【CMD】正在执行的命令或进程名
```

```shell
ps -a
ps -aux
ps -aux | more
ps -aux | grep sshd
```

```shell
用户名     进程号
USER        PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
root          2  0.0  0.0      0     0 ?        S    15:26   0:00 [kthreadd]
root          3  0.0  0.0      0     0 ?        S    15:26   0:00 [ksoftirqd/0]
【%CPU】进程对CPU的占有率
【%MEM】进程对内存的占有率
【VSZ】虚拟内存的占用情况
【RSS】物理内存的使用情况
【TTY】终端机号
【STAT】当前进程的状态，【S】:休眠，【r】:运行,【z】僵死进程
【START】启动时间
【TIME】占用CPU的总时间
【COMMAND】进程启动的命令和参数
```

### 10.2.进程状态

【S】**可中断的睡眠状态** 

【R】**可执行状态** 

【D】**不可中断的睡眠状态** 

【Z】**退出状态，进程成为僵尸进程** 

【T】**暂停状态或跟踪状态** 

【X】**退出状态，进程即将被销毁** 

对于BSD格式，状态码可以附加额外的字符（如S+）：

【<】高优先级进程

【N】低优先级进程

【L】进程进行了内存锁定 (避免swap)

【s 】进程是session leader

【l】多线程（using CLONE_THREAD, like NPTL pthreads do）

【\+】属于前台进程组

### 10.3.查看进程的父进程

```shell
ps -ef    #查看进程的父进程
```

```shell
UID         PID   PPID  C STIME TTY          TIME CMD
root          2      0  0 09:18 ?        00:00:00 [kthreadd]
root          3      2  0 09:18 ?        00:00:00 [ksoftirqd/0]
root          5      2  0 09:18 ?        00:00:00 [kworker/0:0H]
【PPID】父进程的进程号
【C】CPU用于计算执行优先级的因子
【STIME】进程启动时间
【CMD】启动进程的命令及参数
```

### 10.4.`kill`杀死进程

```shell
kill [选项] 进程号 
killall 进程名称    #支持通配符
【-9】 强制杀死进程


kill 2837
kill gedit
```

### 10.5.`pstree`查看进程树

```shell
pstree [选项]
【-p】显示进程的PID
【-u】显示进程的所属用户
```

### 10.6.动态监控进程`(top)`

`top`与`ps`类似，区别是`top`在执行一段时间可以更新正在运行的进程。

```shell
top [选项]
【-d 秒数】指定top命令每个几秒更新，默认为3秒数，在top命令的交互模式中可以执行 
【-i】使top不显示任何闲置或僵死的进程
【-p】通过指定监控进程的id来仅仅只监控该进程的状态

top -d 10  #每隔10秒更新一次
```

```shell
top - 22:14:42 up 4 min,  2 users,  load average: 0.01, 0.09, 0.05
Tasks: 176 total,   1 running, 175 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.1 us,  0.0 sy,  0.0 ni, 99.9 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  1867292 total,  1173472 free,   287816 used,   406004 buff/cache
KiB Swap:  2097148 total,  2097148 free,        0 used.  1372752 avail Mem 

   PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND                      9 root      20   0       0      0      0 S   0.3  0.0   0:00.46 rcu_sched 
   708 root      20   0  302636   6148   4788 S   0.3  0.3   0:00.56 vmtoolsd                 
  3074 root      20   0  157708   2284   1564 R   0.3  0.1   0:00.12 top                     
     1 root      20   0  193628   6712   3948 S   0.0  0.4   0:03.86 systemd       
```

```shell
#top - 22:14:42(启动时间) up 4 min（系统运行时间）,  2 users（系统登录的用户数）,  load average: 0.01, 0.09, 0.05（负载均衡）
#Tasks(进程): 176 total（总进程）,   1 running（运行）, 175 sleeping（休眠）,   0 stopped（停止）,   0 zombie（僵死）
#%Cpu(s)（cpu使用率）:  0.1 us（用户占用率）,  0.0 sy（系统占用率）,  0.0 ni, 99.9 id（空闲的）,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
#KiB Mem (内存的使用情况):  1867292 total（总内存）,  1173472 free（空闲内存）,   287816 used（已使用的内存）,   406004 buff/cache
#KiB Swap（Swap使用情况）:  2097148 total(总Swap),  2097148 free（空闲Swap）,        0 used.  1372752 （已使用的Swap）avail Mem 

```

#### 10.6.1.交互操作

【P】以`cpu`使用率排序，默认

【M】以内存使用率排序

【N】以`PID`排序

【u】输入用户名，监控指定用户的进程

【k】输入进程号，杀死指定进程

【q】退出`top`的交互模式

### 10.7.监控网络状态`(netstat)`

```shell
netstat [选项] 
【-an】按一定顺序排列输出
【-p】显示那个进程在调用

netstat -anp #查看系统所有网络服务
netstat -anp | grep sshd

```



## 十一、管道及重定向

- 管道通常用来组合不同的命令，以实现一个复杂的功能
- 重定向通常用来保存某个命令的输出信息和错误信息，可以用来记录执行的结果或保存错误信息到一个指定的文件。
- 通过管道和重定向可以控制命令行界面（CLI）的数据流。

#### 11.1.命令行shell的数据流定义

- `0`：【STDIN 】 标准输入，默认接受来自键盘的输 
- `1`【STDOUT】 标准输出 ，默认输出到终端窗口 
- `2`【SDTERR】标准错误 ，默认输出到终端窗口 

> 命令行通过STDIN接收参数或数据，通过STDOUT输出结果或通过STDERR输出错误 

#### 11.2.重定向的分类

【>】将STDOUT重定向到文件(覆盖) 

```shell
ls -l > outfile.log
```

【>>】将STDOUT重定向到文件(追加) 

```shell
date >> outfile.log
```

【2>】将STDERR重定向到文件(追加) 

```shell
ls nothere 2> errorout.log     #将错误信息写入到errorout.log
```

【2>&1】将STDERR与STDOUT结合 

```shell
？？？
```

【<】重定向STDIN 

```shell
grep linuxcast < /etc/passwd   #在/etc/passwd中搜索linuxcast，即将/etc/passwd文件内容作为输入
```



## 十二、`Linux`系统的启动详情

### 12.1.启动流程

```shell
BIOS
-->MBR: Boot Code
-->执行引导代码 - GRUB
-->加载内核
-->执行init
-->runlevel
-->运行级别对应的服务
```

### 12.2 .`BIOS`

`BIOS(Basic Input Output System)`基本输入输出系统，一般保存在主板上的BIOS芯片中 。

计算机启动的时候第一个运行的就是BIOS，BIOS负责**检查硬件**并且**查找可启动设备** 。

可启动设备在BOIS设置中进行定义，如USB、CDROM、HD。

### 12.3.`MBR `



## 十三、服务管理

服务的本质就是进程，但是是运行在后台的，通常会监听某个端口，等待其它程序的请求。如`mysqld`、`sshd`

和防火墙等。又称为守护进程。

```shell
#此方式的操作的服务只能临时生效，重启系统后，回到服务之前的设置状态。
service 服务名 [start|stop|restart|reload|status] 

service iptables status     #查看防火墙的状态
```

### 13.1.`systemctl`

`systemctl`是一个`systemd`工具，主要负责控制`systemd`系统和服务管理器。

`systemctl`是一个系统管理守护进程、工具和库的集合，用于取代`System V`初始进程。

`systemd`的功能是用于集中管理和配置类`UNIX`系统。 

#### 13.1.1.`systemd`和`systemctl`的二进制文件和库文件的安装位置

```shell
systemd: /usr/lib/systemd /etc/systemd /usr/share/systemd /usr/share/man/man1/systemd.1.gz
systemctl: /usr/bin/systemctl /usr/share/man/man1/systemctl.1.gz
```

#### 13.1.2. 检查`systemd`是否运行

```shell
ps -eaf | grep [s]ystemd
```

#### 13.1.3.启动、重启、停止、重载服务以及检查服务（如` httpd.service`）状态

```shell
systemctl [start|stop|restart|reload|status] 服务名

systemctl start httpd.service      #启动服务
systemctl restart httpd.service    #重启服务
systemctl stop httpd.service       #停止服务
systemctl reload httpd.service     #重载服务
systemctl status httpd.service     #查看服务状态
```

#### 13.1.4.激活服务并在启动时启用或禁用服务

```shell
systemctl is-active httpd.service   #激活服务
systemctl enable httpd.service      #启动时自动启用服务
systemctl disable httpd.service     #禁用服务
```

#### 13.1.5.`systemctl`命令杀死服务

```shell
systemctl kill httpd     #杀死服务
systemctl status httpd
```

#### 13.1.6.屏蔽（让它不能启动）或显示服务

```shell
systemctl mask httpd.service
systemctl unmask httpd.service
```

### 13.2.防火墙

#### 13.2.1.防火墙的启动、重启、停止和查看状态

这种方式的操作防火墙只能临时生效，重启系统后，回到服务之前的设置状态。

```shell
systemctl status firewalld.service   #查看防火墙状态
systemctl start firewalld.service    #启动防火墙
systemctl stop firewalld.service	 #停止防火墙
systemctl restart firewalld.service  #重启防火墙
systemctl reload firewalld.service   #重新加载防火墙
```

```shell
service iptables status     #查看防火墙的状态
service iptables start 
service iptables reload 
service iptables restart 
service iptables stop 
```

```shell
systemctl list-unit-files | grep docker  #查看服务的启动状态
```



#### 13.2.2.`telnet`

```shell
telnet   #检测某个端口是否在监听，并且可访问
telnet ip port
telnet 192.168.23.22 8080  #在window的dos窗口下运行
```

### 13.3.查看系统所有的服务

- 通过`setup-->`系统服务查看，其中带【*】表示会重启。
- `/etc/init.d/服务名称`

### 13.4.服务的运行级别

```shell
0 :  关机，系统默认级别不能设置为0，否则不能正常启动
1 ： 单用户（找回密码），root权限，不需要密码登录，用于系统维护，禁止远程登录
2 ： 多用户无网络服务，没有NFS
3 ： 多用户有网络服务,有NFS,登录后进入控制台命令模式
4 :  保留级别，系统未使用，保留
5 :  图形界面，系统登录进入图形界面
6 ： 重启，系统会正常关闭并重启；系统默认级别不能设置为6，否则不能正常启动
```

> 服务对应的每个运行级别都是单独设置和生效的

### 13.5.`chkconfig`

`chkconfig`给每个服务的各个运行级别设置自启动或关闭。

```shell
chkconfig --list | grep 服务名（mysqlq）  #查看服务各个运行级别是否自启动
chkconfig 服务名  --list                  #查看服务各个运行级别是否自启动
chkconfig --level 5 服务名 on/off         #修改某个服务在某个级别下是否自启动
chkconfig  服务名 on/off                  #修改某个服务在所有级别下是否自启动

#设置了服务是否自启动，需重启后才能生效
```

### 13.6.设置服务自启动

```shell
systemctl enable httpd.service   #centos7 设置服务自启动
chkconfig --level 3 httpd on     #开启自启动

systemctl disable httpd.service  #centos7 设置服务关闭启动
chkconfig --level 3 httpd off    #关闭自启动

systemctl list-units --type=service   #显示所有已启动的服务
chkconfig --list
```





## 十四、软件管理

### 14.1.源代码形式 

- 绝大多数开源软件都是直接以源代码形式发布 
- 源代码一般会被打包成.tar.gz的归档压缩文件 
- 程序源代码需要编译成为二进制形式之后才能够运行使用 
- 源代码形式的软件使用起来较为麻烦，但是兼容性和可控制性较好 
- 缺点：开源软件一般都会大量使用其他开源软件的功能，所以开源软件会有大量的依赖关系(使用某软件需要先安装其他软件) 

#### 14.1.1.源代码基本编译流程 

- 检查编译环境、相关库文件以及配置参数并生成`makefile `

```
./configure    

```

- 对源代码进行编译，生成可执行文件 

```
make              

```

- 将生成的可执行文件安装到当前计算机中 

```
make  install 

```

### 14.2.`RPM `

`RPM(Redhat Package Manager) `通过将源代码基于特定平台系统编译为可执行文件，并保存依赖关系，来简化开源软件的安装管理 

#### 14.2.1.`RPM`的设计目标

- 使用简单
- 使用单一软件包格式文件发布(`.rpm`文件)
- 可升级  
- 追踪软件依赖关系（但是依赖的`rpm`需手动安装）
- 基本信息查询（发布人，发布时间，版本等）
- 软件验证功能（防止软件在传输中被修改）
- 支持多平台（不同平台会发布对应的`rpm包`）

#### 14.2.2.`RPM`包常用命名规范 

```
linuxcast-1.2.0-30.el6.i686.rpm 
firefox-45.4.0-1.el7.centos.x86_64
【名称】firefox
【版本】45.4.0-1
【适用的操作系统】el7.centos.x86_64（centos6.x的64位系统）
   [i686\i386]表示32位系统
   [noarch]表示通用

```

#### 14.2.3.`RPM`基础命令 

```shell
rpm [选项] 要操作软件名
【-v】显示相关信息
【-h】显示进度

#安装软件
rpm -i software.rpm
#只列出文本文件，本参数需配合"-l"参数使用
rpm -d software
#卸载软件
rpm -e software
rpm -e --nodeps software   #有依赖时，强制删除
#更新软件
rpm -U software-new.rpm
#在线安装，rpm支持通过http、ftp协议安装软件
rpm -ivh http://www.linuxcast.net/software.rpm
```

#### 14.2.4.`RPM`查询

```shell
#RPM会保存软件相关的很多信息
rpm -qa 	                #列出所有rpm软件，包含已安装和未安装的(query all)
rpm -qf file_name           #查询目标文件属于哪个rpm包(query file)
rpm -qi package_name	    #查询指定已安装rpm软件的信息(query information)
rpm -ql package_name 	    #查询指定已安装rpm软件的包含的文件（query list）
rpm -qip software.rpm	    #查询rpm（未安装）文件的信息
rpm -qlp software.rpm       #查询rpm（未安装）文件包含的文件
```

#### 14.2.5.`RPM`验证

软件在传播的过程中可能会被恶意的修改，所以为了安全起见，现代系统都加入了对软件的验证功能。

```shell
#验证一般使用非对称加密算法，所以需要一个密钥
#导入密钥(默认已加入rpm中)
rpm --import RPM-GPG-KEY-CentOS-6
#验证rpm文件
rpm -K software.rpm
#验证已安装的软件
rpm -V software
```

### 14.3.`YUM `

`rpm`软件包形式管理软件虽然方便，但是需要手动解决软件包的依赖关系 。

`YUM(Yellowdog Updater, Modified)`是一个`RPM`的前端程序，主要目的是设计用来自动解决RPM的依赖关系问题 

#### 14.3.1.`YUM`的特点

- 自动解决依赖关系
- 可以对RPM进行分组，并基于组进行安装操作
- 引入仓库概念，支持多个仓库
- 配置简单

#### 14.3.2.`YUM`基本命令 

```shell
yum install software-name     #安装指定软件
yum remove software-name  	  #卸载指定软件
yum update software-name      #升级指定软件
```

#### 14.3.3.`YUM`查询 

```shell
yum search keyword                              #搜索
yum list (all | installed | recent |updates)    #列出全部、已安装、最近、软件更新
yum info packagename                            #显示软件信息
yum whatprovides filename                       #查询哪个rpm软件包含目标文件
```

#### 14.3.4.`YUM`仓库

`yum`的仓库用来存放所有现有的`rpm`软件包 ,当使用`yum`安装一个`rpm`软件时，如果存在依赖关系，会自动在仓库中查找依赖软件并安装 。

- 仓库可以是本地的，也可以通过`http`、`ftp`或`NFS`形式使用集中的、统一的网络仓库 。
- `yum`仓库的配置文件保存在`/etc/yum.repos.d/`目录

```shell
#配置仓库的格式
#仓库名
[LinuxCast]             
#仓库的描述
name=This is LinuxCast.net rpm soft repo
#仓库的地址，仓库支持file、http、ftp、nfs方式
baseurl=http://www.linuxcast.net/yum/centos/6/i386/rpms
#设置仓库是否可用，【1】可用，【0】不可用
enabled=1
#是否校验，【1】可用，【0】不可用
gpgcheck=1
```

- `yum`仓库的配置文件必须以`.repo`结尾 
- 一个配置文件内可以保存多个仓库的配置信息 
- `/etc/yum.repos.d/`目录下可以存在多个配置文件 

#### 14.3.5.创建`YUM`本地仓库

1. 创建一个目录`(/myyum)`作为仓库

2. 将所有的`yum`文件拷贝到仓库目录`(/myyum)`中

   ```shell
   cp -vr yum文件所在的目录 仓库目录（/myyum）
   ```

3. 通过`createrepo `软件建立索引

   ```shell
   #若为安装createrepo，需先安装
   yum createrepo-*.yum
   #建立索引
   createrepo –v 仓库目录（/myyum）
   #若有分组信息，在建立索引时需指定分组文件
   createrepo -g 分组文件         仓库目录
   createrepo -g /tmp/*comps.xml /rpm-directory
   #CentOS/RHEL的分组信息保存在光盘repodata/目录下，文件为以comps.xml结尾的xml文件
   ```

4. 创建好仓库后，需在仓库配置文件中`/etc/yum.repos.d/`配置仓库信息

   ```shell
   #本机通过file方式直接使用，如果需要作为对外的YUM仓库服务器，可以通过HTTP、FTP或NFS协议共享出去
   [myyum]             
   name=This is localhost rpm soft repo
   #本地仓库使用file指定
   baseurl=file:///myyum/
   enabled=1
   #本地仓库一般不检验
   gpgcheck=0    
   ```

### 14.4.内核`kernel `版本升级

#### 14.4.1.小版本升级

```shell
#连接并同步CentOS自带yum源，更新内核版本,此方法适用于更新内核补丁
sudo yum list kernel
sudo yum update -y kernel
```

#### 14.4.2.大版本升级

```shell
# 载入公钥
rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
# 安装ELRepo
rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-3.el7.elrepo.noarch.rpm
# 载入elrepo-kernel元数据
yum --disablerepo=\* --enablerepo=elrepo-kernel repolist
# 查看可用的rpm包
yum --disablerepo=\* --enablerepo=elrepo-kernel list kernel*
# 安装最新版本的kernel
yum --disablerepo=\* --enablerepo=elrepo-kernel install -y kernel-ml.x86_64
```

> 重启，选择新版本内核进入系统 ，将内核工具包一并升级 

```shell
# 删除旧版本工具包
yum remove kernel-tools-libs.x86_64 kernel-tools.x86_64
# 安装新版本工具包
yum --disablerepo=\* --enablerepo=elrepo-kernel install -y kernel-ml-tools.x86_64
```

#### 14.4.3.自编译升级

1. 下载源码
2. 安装`ggc/bc/cmake`
3. 编译源码，安装新内核



## 十五、`shell`编程

`Shell`是一个命令行解释器，它为用户提供了一个向`Linux`内核发送请求，以便运行程序的界面系统级程序，用户可以用`Shell`来启动、挂起、停止甚至编写一些程序。

```shell
vim myshell.sh #shell脚本一般以.sh结束，方便区分，但不是必须的
```

```shell
#!/bin/bash
echo "hello world!"
```

```shell
chmod 744   #赋予shell脚本可执行权限
./myshell.sh #相对路径执行
/root/shell/myshell.sh #绝对路径执行

#不授权执行，不推荐
sh ./myshell.sh
sh /root/shell/myshell.sh
```

### 15.1.设置环境变量

环境变量的设置文件`/etc/profile`

```shell
TOMCAT_HOME=/opt/tomcat 
export TOMCAT_HOME
```

> 设置了环境变量，需要立即生效

```shell
source /etc/profile
```

获取环境变量

```shell
echo $TOMCAT_HOME
```

### 15.2.`shell`注释

> 多行注释

```shell
#<<!

!
```

### 15.3.位置参数变量

当执行`shell`脚本时，希望获取命令行的参数，可使用位置参数变量

```shell
./myshell.sh 100 200
```

```shell
【$n】n表示数字,$0表示命令本身，$1-$9表示第1-9个参数，10个以上的参数${10}取得
【$*】表示取得命令中所有的参数，把所有参数看作一个整体
【$@】表示取得命令中所有的参数，不过$@把每个参数分隔了
【$#】获取命令行参数的个数
```

### 15.4.预定义变量

`shell`设计者预先已经定义好的变量，可在`shell`脚本中使用

```shell
【$$】当前进程的进程号PID
【$!】后台运行的最后一个进程的进程号（PID）
【$?】最后一次执行命令的返回状态，【0】正确执行，【非零】命令不正确执行
```

```shell
./myshell.sh &  #表示后台运行myshell.sh

【&】以后台的方式运行
```

### 15.5.条件判断

```shell
#注意condition前后有空格
if [ condition ] then 
fi
#非空返回true，

【&&】且
【||】并
```

```shell
【=】 等于
【-lt】小于
【-le】小于等于
【-gt】大于
【-ge】大于等于
【-ne】不等于
#权限判断
【-r】有读权限
【-w】有写权限
【-x】有执行权限
#文件类型判断
【-f】文件存在且是一个常规文件，既不是一个隐藏文件
【-e】文件存在
【-d】文件存在且是一个目录
```

```shell
if [ -e /root/shell/myshell.sh] then
 echo "文件存在"
fi
```

### 15.6.流程控制

#### 15.6.1.分支语句

```shell
if [ condition ];then 
...
elif [ condition ]; then 
...
fi
```

```shell
if [ condition ] 
then 
...
elif [ condition ] 
then 
...
fi
```

```shell
case $变量名 in
"值"）
...
;;
"值"）
...
;;
esac
```

```shell
case $1 in 
"1")
 echo "周一"
;;
"2")
 echo "周二"
;;
esac
```

#### 15.6.2.循环语句

```shell
for 变量 in 值 值 值 ...
  do 
  ...
  done	
```

```shell
for i in "$*"
do 
  echo "$i"
done  

for i in "$@"
do 
  echo "$i"
done  
```

```shell
for((初始值;循环控制条件;;变量变化))
do
...
done
```

```shell
#!/bin/bash
SUM=0
for((i=1;i<100;i++))
do
	SUM=$[$SUM+$i]
done
echo "SUM=$SUM"
```

```shell
while [ condition ]
do 
...
done
```

```shell
#!/bin/bash
SUM=0
i=0
while [ $i -ge $1 ]
do
	SUM=$[$SUM+$i]
	i=$[$i+1]
done	
```

### 15.7.`read`读取控制台的输入

```shell
read[选项]（参数）
【-p】指定读取值的提示符
【-t】指定读取值时等待的时间（秒），若没有在指定时间内输入，就不在等待
```

```shell
read -p "请输入一个值value=" value
echo "你输入的值value=$value"
```

```shell
read -t 10 -p "请输入一个值value=" value
echo "你输入的值value=$value"
```

### 15.8.函数

#### 15.8.1.系统函数

```shell
#返回完整的路径最后/的部分，常用于获取文件名
basename[pathname][suffix]
basename[string][suffix]

#/home/xiaoyong/test.txt
basename /home/xiaoyong/test.txt      #test.txt
basename /home/xiaoyong/test.txt .txt #test
```

```shell
#返回路径最后/前面部分，获取路径
dirname 文件绝对路径

dirname /home/xiaoyong/test.txt       #/home/xiaoyong/test
```

#### 15.8.2.自定义函数

```shell
[function] function_name[()]
{
    Action;
    [return int;]
}
```

```shell
function_name [值] #调用函数
```

```shell
function getSum()
{
    SUM=$[$n1+$n2]
    echo "SUM=$SUM"
}

read -p "请输入第一个值=" n1
read -p "请输入第二个值=" n2

getSum $n1 $n2
```

### 15.9.数据库备份

```shell
#1.每日凌晨2：30备份数据库（test）到/tmp/backup/mysql
#2.备份开始和结束给出提示信息
#3.备份的数据库文件以备份数据命名，并打包成.tar.gz的形式
#4.在备份完成的同时，检查是否有10天前的备份文件，若有就删除
```

```shell
#!/bin/bash
#备份目录
BACKUP=/tmp/backup/mysql
#文件名
DATETIME=$(%date+%Y-%m-%d_%H%M%S)

echo "========开始备份数据库=========="
echo "========备份的路径$BACKUP/$DATETIME.tar.gz=========="

#主机
HOST=localhost
#用户名
DB_USER=root
#密码
DB_PWD=1234
#数据库名
DATABASE=test
#创建备份的路径
#[ ! -d "$BACKUP/$DATETIME" ] && mkdir -p "$BACKUP/$DATETIME"
if [ ! -d "$BACKUP/$DATETIME" ]
then 
  mkdir -p "$BACKUP/$DATETIME"
fi 
#备份数据库
mysqldump -u${DB_USER} -p{DB_PWD} --host=$HOST $DATABASE | gzip>
$BACKUP/$DATETIME/$DATETIME.sql.gz 
#打包备份文件
cd $BACKUP
tar -zcvf $DATETIME.tar.gz $DATETIME
#删除临时文件
rm -rf $BACKUP/$DATETIME

echo "========数据库备份结束=========="

echo "========检查和删除过时备份文件开始=========="
#检查是否有10天前的备份文件，若有就删除
find $BACKUP -mtime + 10 -name "*.tar.gz"  -exec rm -rf {} \;
echo "========检查和删除过时备份文件完成=========="

```

```shell
#定时执行
crontab -e
```

```shell
30 2 * * * /usr/sinb/mysql_db_backup.sh  #备份数据库的脚本
```






