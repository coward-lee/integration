<div style="text-align: center; font-size: 33px; font-weight: bold; ">k8s</div>

# 搭建
## 搭建规划

### 1、 搭建k8s环境平台规划

1. 单 master 多node
2. 多master（高可用），多node

### 2、 服务器硬件配置要求

| 节点类型   | 处理器核心 | 磁盘 |
|--------|-------|----|
| master | 2c    | 4g |  20G|
| node   | 4c    | 8g |  40g|

### 3、 搭建k8s集群部署方式
1. kube admin
2. 二进制包安装包

## 安装
### 服务器准备
```shell
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭 selinux
sed -i 's/enforcing/disabled' /etc/selinux/config # 永久
#setenforce 0 # 临时关闭
# 关闭 swap
#swapoff -a # 临时
sed -ri 's/.*swap.*/#&/' /etc/fstab # 永久

# 根据规划设置主机名称
hostnamectl set-hostname <hostname>





```



