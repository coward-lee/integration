<div style="text-align: center; font-size: 33px; font-weight: bold; ">k8s</div>

# 1 基于 kubeadm 的方式搭建

## 1 搭建规划

### 1.1、 搭建k8s环境平台规划

1. 单 master 多node
2. 多master（高可用），多node

### 1.2、 服务器硬件配置要求

| 节点类型   | 处理器核心 | 磁盘 |
|--------|-------|----|
| master | 2c    | 4g |  20G|
| node   | 4c    | 8g |  40g|

### 1.3、 搭建k8s集群部署方式

1. kube admin
2. 二进制包安装包

## 2 安装

### 2.1服务器准备

```shell

# 修改主机为静态ip
vim /etc/sysconfig/network-scripts/ifcfg-ens33  
# 修改项如下
BOOTPROTO="static" 
IPADDR=192.168.214.135
DNS1=8.8.8.8 # 配置dns
GATEWAY=192.168.214.2 # 配置网关不然可能导致无法ping通域名
ONBOOT="yes"
######

service network restart


# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭 selinux
sed -i 's/enforcing/disabled/' /etc/selinux/config # 永久
#setenforce 0 # 临时关闭
# 关闭 swap
#swapoff -a # 临时
sed -ri 's/.*swap.*/#&/' /etc/fstab # 永久

# 根据规划设置主机名称
hostnamectl set-hostname <hostname>




# 在master添加hosts
cat >> /etc/hosts << EOF
192.168.214.141 master
192.168.214.142 node1
192.168.214.143 node2
EOF


## 将桥接的ipv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system ## 生效

# 时间同步
yum install ntpdate -y
ntpdate time.windows.com
```

### 2.2 安装docker

```shell

# 修改镜像源
yum install wget -y 
wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
yum -y install docker-ce-18.06.1.ce-3.el7
systemctl enable docker && systemctl start docker
docker --version
# 输出 Docker version 18.06.1-ce, build e68fc7a

# 修改镜像源
cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
}
EOF

cat /etc/docker/daemon.json
systemctl daemon-reload
systemctl restart docker  # 重启docker
```

### 2.3 安装 kubeadm

```shell
# 添加yum软件源
cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# 安装 并且需要指定版本，因为更新频率比较快
yum install -y kubelet-1.18.0 kubeadm-1.18.0 kubectl-1.18.0
systemctl enable kubelet 
```

## 3. 部署 kubernetes master

```shell
# 只 master 执行
kubeadm init \
  --apiserver-advertise-address=192.168.214.135 \    # 当前节点的ip地址
  --image-repository registry.aliyuncs.com/google_containers \    # 镜像地址
  --kubernetes-version v1.18.0 \   # 指定当前的版本
  --service-cidr=10.96.0.0/12 \    # 自己链接使用的内部ip？？
  --pod-network-cidr=10.244.0.0/16

# 根据安装提示执行以下命令
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
# 查看安装结果
kubectl get nodes

# 根据提示内容加入节点(但是这个不是在master 上面执行的命令，这个是在node节点上面执行的命令，表示我要加入这个master集群下的node集群)
kubeadm join 192.168.214.135:6443 --token q56ls5.wvmkyun7tx1ho0xo \
    --discovery-token-ca-cert-hash sha256:9bd3a8e55bb69be044d57134c15186dc06eb6ac64f371198d86ad81b64b7d79c 
```

## 4. 配置网络插件 CNI

```shell
wget https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```

默认镜像地址无法访问，sed命令修改为docker hub镜像仓库。

```shell
# 安装网络插件 flannel ， -f 后面也可以填写本地文件
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
# 查看pod 状态
kubectl get pods -n kube-system
```

## 5. 测试 kubernetes

在Kubernetes集群中创建一个pod，验证是否正常运行：

```shell
kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --type=NodePort
kubectl get pod,svc
```


# 2. 基于二进制包的方式进行安装
## 2.1 规划说明
1. 多台虚拟机
2. 初始化操作系统
3. 为etcd和apiserver自签证书
4. 部署etcd 
5. 部署master组件
   1. kube-apiserver
   2. kube-controller-manager
   3. kube-scheduler
   4. etcd
6. 部署node组件
   1. kube-proxy
   2. docker
   3. kubelet
   4. etcd
7. 部署集群网络