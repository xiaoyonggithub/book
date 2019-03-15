# 1、打开`window`功能,`Window+R`

- `optionalfeatures `: 打开`Windwos`功能菜单
- `services.msc`:打开服务菜单
- `mstsc`:打开远程桌面
- `psr.exe`：录像
- `osk`：虚拟键盘



# 2、快捷键

1. `windows++++++`：放大镜
2. `windows+X`



# 3、`IE`操作

1. `Ctrl+Tab`：IE切换页面



# 4.`DOS`

## 4.1.常用指令

- `dir`查看当前目录下所有的文件
- `cd`进入指定目录
- `md`创建目录
- `rd`删除目录

## 4.2.网络命令

- 检查某个地址的某个端口是否畅通

```dos
telnet 218.14.227.197 3128
```

## 4.3.文件的查找路径

1. 首先在当前路径下查看
2. 再在环境变量path下查找



# 5.文件系统

文件系统就是文件的存储方式，硬盘系统的格式化就是为硬盘系统安装文件系统

不同的操作系统有不同的文件系统

| 操作系统       | 文件系统 | 描述 |
| -------------- | -------- | ---- |
| `linux`        | `ext4`   |      |
| `osx`          | `HFS+`   |      |
| `Windows`      | `NTFS`   |      |
| `Solaris/Unix` | `ZFS`    |      |

NTFS 文件系统是 Windows 的专有系统，Mac 可以读，但是默认不能写入

## 5.1.`Windows`的文件系统

- `FAT32`:是最老的文件系统，所有操作系统都支持，兼容性好；它是为32计算机设计的，文件不能超过4GB(2^32^-1字节)

- `NTFS`:Windows默认的文件系统，Windows的系统盘只能使用这个系统，移动硬盘默认的也是使用的它

- `exFAT`:相当于`FAT32`的64位的升级版本，`ex`就是 extended 的缩写（表示"扩展的 FAT32"），功能不如 NTFS，但是解决了文件和分区的大小问题，两者最大都可以到 128PB。

  由于 Mac 和 Linux 电脑可以读写这种系统，所以移动硬盘的文件系统可以改成它。

  - linux需要安装exFAT 支持

  ```shell
  sudo apt-get install exfat-utils exfat-fuse
  ```
