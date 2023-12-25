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

## 1 规划说明

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

## 2 安装

### 2.1 机器准备

参考 1.2

### 2.2 给etcd 签发证书

```shell
# 准备 cfssl 证书生成工具
# cfssl 是一个开源的证书管理工具，使用 json 文件生成证书，相比 openssl 更方便使用;找任意一台服务器操作，这里用 Master 节点。
wget https://pkg.cfssl.org/R1.2/cfssl_linux-amd64 --no-check-certificate
wget https://pkg.cfssl.org/R1.2/cfssljson_linux-amd64 --no-check-certificate
wget https://pkg.cfssl.org/R1.2/cfssl-certinfo_linux-amd64 --no-check-certificate
chmod +x cfssl_linux-amd64 cfssljson_linux-amd64 cfssl-certinfo_linux-amd64 
mv cfssl_linux-amd64 /usr/local/bin/cfssl
mv cfssljson_linux-amd64 /usr/local/bin/cfssljson
mv cfssl-certinfo_linux-amd64 /usr/bin/cfssl-certinfo
```

### 2.3 生成 Etcd 证书

（1）自签证书颁发机构（CA）
创建工作目录：

```shell

mkdir -p ~/TLS/{etcd,k8s}
cd TLS/etcd
## 配置ca config json
cat >ca-config.json <<EOF
{
  "signing": {
    "default": {
      "expiry": "87600h"
    },
    "profiles": {
      "www": {
        "expiry": "87600h",
        "usages": [
          "signing",
          "key encipherment",
          "server auth",
          "client auth"
        ]
      }
    }
  }
}

EOF
cat > ca-csr.json <<EOF
{
  "CN": "etcd CA",
  "key": {
    "algo": "rsa",
    "size": 2048
  },
  "names": [
    {
      "C": "CN",
      "L": "Beijing",
      "ST": "Beijing"
    }
  ]
}
EOF

# 生成证书
cfssl gencert -initca ca-csr.json | cfssljson -bare ca -
# 检查生成结果
ls *pem

```

（2） 使用自签 CA 签发 Etcd HTTPS 证书

```shell

# 创建证书申请文件：
cat > server-csr.json <<EOF
{
  "CN": "etcd",
  "hosts": [
    "192.168.214.141",
    "192.168.214.142",
    "192.168.214.143",
    "192.168.214.144",
    "192.168.214.145",
    "192.168.214.146",
    "192.168.214.147",
    "192.168.214.148",
    "192.168.214.149"
  ],
  "key": {
    "algo": "rsa",
    "size": 2048
  },
  "names": [
    {
      "C": "CN",
      "L": "BeiJing",
      "ST": "BeiJing"
    }
  ]
}
EOF

# 生成证书：
cfssl gencert -ca=ca.pem -ca-key=ca-key.pem -config=ca-config.json -profile=www server-csr.json | cfssljson -bare server
ls server*pem
# result server-key.pem server.pem
```

### 2.4 安装etcd

（1）创建工作目录并解压二进制包

```shell

mkdir /opt/etcd/{bin,cfg,ssl} -p
tar zxvf etcd-v3.4.9-linux-amd64.tar.gz
mv etcd-v3.4.9-linux-amd64/{etcd,etcdctl} /opt/etcd/bin/

```

（2）创建 etcd 配置文件

```shell

cat > /opt/etcd/cfg/etcd.conf << EOF
#[Member]
ETCD_NAME="etcd-1"
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="https://192.168.214.141:2380"
ETCD_LISTEN_CLIENT_URLS="https://192.168.214.141:2379"
#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="https://192.168.214.141:2380"
ETCD_ADVERTISE_CLIENT_URLS="https://192.168.214.141:2379"
ETCD_INITIAL_CLUSTER="etcd-1=https://192.168.214.141:2380,etcd-2=https://192.168.214.142:2380,etcd-3=https://192.168.214.143:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"
EOF

# ETCD_NAME：节点名称，集群中唯一
# ETCD_DATA_DIR：数据目录
# ETCD_LISTEN_PEER_URLS：集群通信监听地址
# ETCD_LISTEN_CLIENT_URLS：客户端访问监听地址
# ETCD_INITIAL_ADVERTISE_PEER_URLS：集群通告地址
# ETCD_ADVERTISE_CLIENT_URLS：客户端通告地址
# ETCD_INITIAL_CLUSTER：集群节点地址
# ETCD_INITIAL_CLUSTER_TOKEN：集群 Token
# ETCD_INITIAL_CLUSTER_STATE：加入集群的当前状态，new 是新集群，existing 表示加入 已有集群
```
（3）systemd 管理 etcd
```shell
cat > /usr/lib/systemd/system/etcd.service << EOF
[Unit]
Description=Etcd Server
After=network.target
After=network-online.target
Wants=network-online.target
[Service]
Type=notify
EnvironmentFile=/opt/etcd/cfg/etcd.conf
ExecStart=/opt/etcd/bin/etcd  --cert-file=/opt/etcd/ssl/server.pem  --key-file=/opt/etcd/ssl/server-key.pem  --peer-cert-file=/opt/etcd/ssl/server.pem  --peer-key-file=/opt/etcd/ssl/server-key.pem  --trusted-ca-file=/opt/etcd/ssl/ca.pem  --peer-trusted-ca-file=/opt/etcd/ssl/ca.pem  --logger=zap
Restart=on-failure
LimitNOFILE=65536
[Install]
WantedBy=multi-user.target
EOF
```
（4）拷贝刚才生成的证书

```shell
cp ~/TLS/etcd/ca*pem ~/TLS/etcd/server*pem /opt/etcd/ssl/
```
（5）启动并设置开机启动
```shell
systemctl daemon-reload
systemctl start etcd
systemctl enable etcd
```
（6）将上面节点 1 所有生成的文件拷贝到节点 2 和节点 3
```shell
scp -r /opt/etcd/ root@192.168.214.142:/opt/
scp /usr/lib/systemd/system/etcd.service root@192.168.214.142:/usr/lib/systemd/system/ 
scp -r /opt/etcd/ root@192.168.214.143:/opt/
scp /usr/lib/systemd/system/etcd.service root@192.168.214.143:/usr/lib/systemd/system/

# 然后在节点 2 和节点 3 分别修改 etcd.conf 配置文件中的节点名称和当前服务器 IP：
vi /opt/etcd/cfg/etcd.conf
# 需要修改这四个字段
#ETCD_LISTEN_PEER_URLS="https://192.168.214.141:2380"
#ETCD_LISTEN_CLIENT_URLS="https://192.168.214.141:2379"
##[Clustering]
#ETCD_INITIAL_ADVERTISE_PEER_URLS="https://192.168.214.141:2380"
#ETCD_ADVERTISE_CLIENT_URLS="https://192.168.214.141:2379"
```

（7）查看etcd 启动状态
如果输出上面信息，就说明集群部署成功。如果有问题第一步先看日志：
/var/log/messages 或 journalctl -u etcd


etcdctl --endpoints=https://192.168.214.141:2379,https://192.168.214.142:2379,https://192.168.214.143:2379 --cert=/opt/etcd/ssl/server.pem     --key=/opt/etcd/ssl/server-key.pem      --cacert=/opt/etcd/ssl/ca.pem endpoint status

### 2.5 安装 Docker

(1) 下载并配置全局命令
```shell
wget https://download.docker.com/linux/static/stable/x86_64/docker-19.03.9.tgz
tar zxvf docker-19.03.9.tgz
mv docker/* /usr/bin
```

（2） systemd 管理 docker

```shell
cat > /usr/lib/systemd/system/docker.service << EOF
[Unit]
Description=Docker Application Container Engine
Documentation=https://docs.docker.com
After=network-online.target firewalld.service
Wants=network-online.target
[Service]
Type=notify
ExecStart=/usr/bin/dockerd
ExecReload=/bin/kill -s HUP $MAINPID
LimitNOFILE=infinity
LimitNPROC=infinity
LimitCORE=infinity
TimeoutStartSec=0
Delegate=yes
KillMode=process
Restart=on-failure
StartLimitBurst=3
StartLimitInterval=60s
[Install]
WantedBy=multi-user.target
EOF
```

（3）修改镜像源
```shell
mkdir /etc/docker
cat >/etc/docker/daemon.json <<EOF
{
 "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
}
EOF
```
(4) 启动并配置开机启动
```shell
systemctl daemon-reload
systemctl start docker
systemctl enable docker
```

### 2.6 部署 Master Node - （生成 kube-apiserver 证书）
在 ~/TLS/k8s路径下操作

（1）自签证书颁发机构（CA）
```shell
cat > ca-config.json<< EOF

{
  "signing": {
    "default": {
      "expiry": "87600h"
    },
    "profiles": {
      "kubernetes": {
        "expiry": "87600h",
        "usages": [
          "signing",
          "key encipherment",
          "server auth",
          "client auth"
        ]
      }
    }
  }
}
EOF

cat > ca-csr.json <<EOF
{
  "CN": "kubernetes",
  "key": {
    "algo": "rsa",
    "size": 2048
  },
  "names": [
    {
      "C": "CN",
      "L": "Beijing",
      "ST": "Beijing",
      "O": "k8s",
      "OU": "System"
    }
  ]
}
EOF
```
（2）生成证书
cfssl gencert -initca ca-csr.json | cfssljson -bare ca -

（3）使用自签 CA 签发 kube-apiserver HTTPS 证书

```shell
cat > server-csr.json <<EOF
{
  "CN": "kubernetes",
  "hosts": [
    "10.0.0.1",
    "127.0.0.1",
    "192.168.214.141",
    "192.168.214.142",
    "192.168.214.143",
    "192.168.214.144",
    "192.168.214.145",
    "192.168.214.146",
    "192.168.214.147",
    "192.168.214.148",
    "192.168.214.149",
    "kubernetes",
    "kubernetes.default",
    "kubernetes.default.svc",
    "kubernetes.default.svc.cluster",
    "kubernetes.default.svc.cluster.local"
  ],
  "key": {
    "algo": "rsa",
    "size": 2048
  },
  "names": [
    {
      "C": "CN",
      "L": "BeiJing",
      "ST": "BeiJing",
      "O": "k8s",
      "OU": "System"
    }
  ]
}
EOF

cfssl gencert -ca=ca.pem -ca-key=ca-key.pem -config=ca-config.json -profile=kubernetes server-csr.json | cfssljson -bare server
```

### 下载 安装kubernetes
https://github.com/kubernetes/kubernetes/blob/master/CHANGELOG/CHANGELOG-1.18.md#v1183

kubernetes 下载连接 ：https://dl.k8s.io/v1.18.3/kubernetes-server-linux-amd64.tar.gz
```shell
# 解压二进制包
tar zxvf kubernetes-server-linux-amd64.tar.gz
mkdir -p /opt/kubernetes/{bin,cfg,ssl,logs}
cd kubernetes/server/bin
cp kube-apiserver kube-scheduler kube-controller-manager /opt/kubernetes/bin
cp kubectl /usr/bin/
```

### 部署 kube-apiserver
1. 创建配置文件
```shell
cat > /opt/kubernetes/cfg/kube-apiserver.conf << EOF
KUBE_APISERVER_OPTS="--logtostderr=false \\
--v=2 \\
--log-dir=/opt/kubernetes/logs \\
--etcd-servers=https://192.168.214.141:2379,https://192.168.214.142:2379,https://192.168.214.143:2379 \\
--bind-address=192.168.214.141 \\
--secure-port=6443 \\
--advertise-address=192.168.214.141 \\
--allow-privileged=true \\
--service-cluster-ip-range=10.0.0.0/24 \\
--enable-admission-plugins=NamespaceLifecycle,LimitRanger,ServiceAccount,ResourceQuota,NodeRestriction \\
--authorization-mode=RBAC,Node \\
--enable-bootstrap-token-auth=true \\
--token-auth-file=/opt/kubernetes/cfg/token.csv \\
--service-node-port-range=30000-32767 \\
--kubelet-client-certificate=/opt/kubernetes/ssl/server.pem \\
--kubelet-client-key=/opt/kubernetes/ssl/server-key.pem \\
--tls-cert-file=/opt/kubernetes/ssl/server.pem \\
--tls-private-key-file=/opt/kubernetes/ssl/server-key.pem \\
--client-ca-file=/opt/kubernetes/ssl/ca.pem \\
--service-account-key-file=/opt/kubernetes/ssl/ca-key.pem \\
--etcd-cafile=/opt/etcd/ssl/ca.pem \\
--etcd-certfile=/opt/etcd/ssl/server.pem \\
--etcd-keyfile=/opt/etcd/ssl/server-key.pem \\
--audit-log-maxage=30 \\
--audit-log-maxbackup=3 \\
--audit-log-maxsize=100 \\
--audit-log-path=/opt/kubernetes/logs/k8s-audit.log"
EOF

# 注：上面两个\ \ 第一个是转义符，第二个是换行符，使用转义符是为了使用 EOF 保留换行符。
# –logtostderr：启用日志
# —v：日志等级
# –log-dir：日志目录
# –etcd-servers：etcd 集群地址
# –bind-address：监听地址
# –secure-port：https 安全端口
# –advertise-address：集群通告地址
# –allow-privileged：启用授权
# –service-cluster-ip-range：Service 虚拟 IP 地址段
# –enable-admission-plugins：准入控制模块
# –authorization-mode：认证授权，启用 RBAC 授权和节点自管理
# –enable-bootstrap-token-auth：启用 TLS bootstrap 机制
# –token-auth-file：bootstrap token 文件
# –service-node-port-range：Service nodeport 类型默认分配端口范围
# –kubelet-client-xxx：apiserver 访问 kubelet 客户端证书
# –tls-xxx-file：apiserver https 证书
# –etcd-xxxfile：连接 Etcd 集群证书
# –audit-log-xxx：审计日志
```
2. 拷贝刚才生成的证书    
cp ~/TLS/k8s/ca*pem ~/TLS/k8s/server*pem /opt/kubernetes/ssl/
3. 启用 TLS Bootstrapping 机制
<pre>
TLS Bootstraping：Master apiserver 启用 TLS 认证后，Node 节点 kubelet 和 kube-proxy 要与 kube-apiserver 进行通信，必须使用 CA 签发的有效证书才可以，
当 Node节点很多时，这种客户端证书颁发需要大量工作，同样也会增加集群扩展复杂度。
为了简化流程，Kubernetes 引入了 TLS bootstraping 机制来自动颁发客户端证书，kubelet会以一个低权限用户自动向 apiserver 申请证书，kubelet 的证书由 apiserver 动态签署。
所以强烈建议在 Node 上使用这种方式，目前主要用于 kubelet，kube-proxy 还是由我们统一颁发一个证书。
</pre>
复制token？
```shell
cat > /opt/kubernetes/cfg/token.csv << EOF
c47ffb939f5ca36231d9e3121a252940,kubelet-bootstrap,10001,"system:node-bootstrapper"
EOF

# 格式：token，用户名，UID，用户组
# token 也可自行生成替换：
# head -c 16 /dev/urandom | od -An -t x | tr -d ' '
```
4. systemd 管理 apiserver
```shell
cat > /usr/lib/systemd/system/kube-apiserver.service << EOF
[Unit]
Description=Kubernetes API Server
Documentation=https://github.com/kubernetes/kubernetes
[Service]
EnvironmentFile=/opt/kubernetes/cfg/kube-apiserver.conf
ExecStart=/opt/kubernetes/bin/kube-apiserver \$KUBE_APISERVER_OPTS
Restart=on-failure
[Install]
WantedBy=multi-user.target
EOF
```
5. 启动并设置开机启动
```shell
systemctl daemon-reload
systemctl start kube-apiserver
systemctl enable kube-apiserver
   
```
6. 授权 kubelet-bootstrap 用户允许请求证书 这一步失败了（先往前走）
```shell
kubectl create clusterrolebinding kubelet-bootstrap \
--clusterrole=system:node-bootstrapper \
--user=kubelet-bootstrap
```
### 部署 kube-controller-manager

1. 创建配置文件

```shell
cat > /opt/kubernetes/cfg/kube-controller-manager.conf << EOF
KUBE_CONTROLLER_MANAGER_OPTS="--logtostderr=false \\
--v=2 \\
--log-dir=/opt/kubernetes/logs \\
--leader-elect=true \\
--master=127.0.0.1:8080 \\
--bind-address=127.0.0.1 \\
--allocate-node-cidrs=true \\
--cluster-cidr=10.244.0.0/16 \\
--service-cluster-ip-range=10.0.0.0/24 \\
--cluster-signing-cert-file=/opt/kubernetes/ssl/ca.pem \\
--cluster-signing-key-file=/opt/kubernetes/ssl/ca-key.pem \\
--root-ca-file=/opt/kubernetes/ssl/ca.pem \\
--service-account-private-key-file=/opt/kubernetes/ssl/ca-key.pem \\
--experimental-cluster-signing-duration=87600h0m0s"
EOF
    #  –master：通过本地非安全本地端口 8080 连接 apiserver。
    #  –leader-elect：当该组件启动多个时，自动选举（HA）
    #  –cluster-signing-cert-file/–cluster-signing-key-file：自动为 kubelet 颁发证书的 CA，与 apiserver 保持一致
```

2. systemd 管理 controller-manager

```shell
cat > /usr/lib/systemd/system/kube-controller-manager.service << EOF
[Unit]
Description=Kubernetes Controller Manager
Documentation=https://github.com/kubernetes/kubernetes
[Service]
EnvironmentFile=/opt/kubernetes/cfg/kube-controller-manager.conf
ExecStart=/opt/kubernetes/bin/kube-controller-manager \$KUBE_CONTROLLER_MANAGER_OPTS
Restart=on-failure
[Install]
WantedBy=multi-user.target
EOF
```

3. 启动并设置开机启动
```shell
systemctl daemon-reload
systemctl start kube-controller-manager
systemctl status kube-controller-manager
systemctl enable kube-controller-manager
```

###  部署 kube-scheduler
1. 创建配置文件
```shell
cat > /opt/kubernetes/cfg/kube-scheduler.conf << EOF
KUBE_SCHEDULER_OPTS="--logtostderr=false \
--v=2 \
--log-dir=/opt/kubernetes/logs \
--leader-elect \
--master=127.0.0.1:8080 \
--bind-address=127.0.0.1"
EOF
# –master：通过本地非安全本地端口 8080 连接 apiserver。
# –leader-elect：当该组件启动多个时，自动选举（HA）

# systemd 管理 scheduler
cat > /usr/lib/systemd/system/kube-scheduler.service << EOF
[Unit]
Description=Kubernetes Scheduler
Documentation=https://github.com/kubernetes/kubernetes
[Service]
EnvironmentFile=/opt/kubernetes/cfg/kube-scheduler.conf
ExecStart=/opt/kubernetes/bin/kube-scheduler \$KUBE_SCHEDULER_OPTS
Restart=on-failure
[Install]
WantedBy=multi-user.target
EOF

# 启动并设置开机启动
systemctl daemon-reload
systemctl start kube-scheduler
systemctl enable kube-scheduler
```

## 部署 worker node
## 1 创建工作目录并拷贝二进制文件
在所有 **worker node** 创建工作目录：
```shell
mkdir -p /opt/kubernetes/{bin,cfg,ssl,logs}
```
从 **master** 节点拷贝：  
```shell
cd kubernetes/server/bin  
cp kubelet kube-proxy /opt/kubernetes/bin # 本地拷贝
```
### 2 部署 kubelet

**在 node 执行**
```shell
# 1. 创建配置文件
cat > /opt/kubernetes/cfg/kubelet.conf << EOF
KUBELET_OPTS="--logtostderr=false \\
--v=2 \\
--log-dir=/opt/kubernetes/logs \\
--hostname-override=k8s-master \\
--network-plugin=cni \\
--kubeconfig=/opt/kubernetes/cfg/kubelet.kubeconfig \\
--bootstrap-kubeconfig=/opt/kubernetes/cfg/bootstrap.kubeconfig \\
--config=/opt/kubernetes/cfg/kubelet-config.yml \\
--cert-dir=/opt/kubernetes/ssl \\
--pod-infra-container-image=lizhenliang/pause-amd64:3.0"
EOF
# –hostname-override：显示名称，集群中唯一
# –network-plugin：启用 CNI –kubeconfig：空路径，会自动生成，后面用于连接 apiserver –bootstrap-kubeconfig：首次启动向 apiserver 申请证书
# –config：配置参数文件
# –cert-dir：kubelet 证书生成目录
# –pod-infra-container-image：管理 Pod 网络容器的镜像

# 2. 配置参数文件
cat > /opt/kubernetes/cfg/kubelet-config.yml << EOF
kind: KubeletConfiguration
apiVersion: kubelet.config.k8s.io/v1beta1
address: 0.0.0.0
port: 10250
readOnlyPort: 10255
cgroupDriver: cgroupfs
clusterDNS:
- 10.0.0.2
clusterDomain: cluster.local
failSwapOn: false
authentication:
anonymous:
enabled: false
webhook:
cacheTTL: 2m0s
enabled: true
x509:
clientCAFile: /opt/kubernetes/ssl/ca.pem
authorization:
mode: Webhook
webhook:
cacheAuthorizedTTL: 5m0s
cacheUnauthorizedTTL: 30s
evictionHard:
imagefs.available: 15%
memory.available: 100Mi
nodefs.available: 10%
nodefs.inodesFree: 5%
maxOpenFiles: 1000000
maxPods: 110
EOF

# 3. 生成 bootstrap.kubeconfig 文件
KUBE_APISERVER="https://192.168.214.71:6443" # apiserver IP:PORT
TOKEN="c47ffb939f5ca36231d9e3121a252940" # 与 token.csv 里保持一致
# 生成 kubelet bootstrap kubeconfig 配置文件
kubectl config set-cluster kubernetes \
--certificate-authority=/opt/kubernetes/ssl/ca.pem \
--embed-certs=true \
--server=${KUBE_APISERVER} \
--kubeconfig=bootstrap.kubeconfig
kubectl config set-credentials "kubelet-bootstrap" \
--token=${TOKEN} \
--kubeconfig=bootstrap.kubeconfig
kubectl config set-context default \
--cluster=kubernetes \
--user="kubelet-bootstrap" \
--kubeconfig=bootstrap.kubeconfig
kubectl config use-context default --kubeconfig=bootstrap.kubeconfig
# 拷贝配置文件
cp bootstrap.kubeconfig /opt/kubernetes/cfg
# 4. systemd 管理 kubelet
cat > /usr/lib/systemd/system/kubelet.service << EOF
[Unit]
Description=Kubernetes Kubelet
After=docker.service
[Service]
EnvironmentFile=/opt/kubernetes/cfg/kubelet.conf
ExecStart=/opt/kubernetes/bin/kubelet \$KUBELET_OPTS
Restart=on-failure
LimitNOFILE=65536
[Install]
WantedBy=multi-user.target
EOF

# 5. 启动并设置开机启动
systemctl daemon-reload
systemctl start kubelet
systemctl enable kubelet
```


### 3. 批准 kubelet 证书申请并加入集群

```shell
# 查看 kubelet 证书请求
kubectl get csr
NAME AGE SIGNERNAME
REQUESTOR CONDITION
node-csr-uCEGPOIiDdlLODKts8J658HrFq9CZ--K6M4G7bjhk8A 6m3s
kubernetes.io/kube-apiserver-client-kubelet kubelet-bootstrap Pending
# 批准申请
kubectl certificate approve node-csr-uCEGPOIiDdlLODKts8J658HrFq9CZ--K6M4G7bjhk8A
# 查看节点
kubectl get node
```








