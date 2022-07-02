
# 1. hadoop概览
## 1.x
MapReduce（计算+调度）
HDFS 数据存储
common 辅助工具

##2.x
Yarn（调度）
MapReduce（计算）
HDFS 数据存储
common 辅助工具

# 2. Yarn 概述
1. ResourceManager  yarn集群的老大
2. NodeManager      单个节点资源服务器的老大
3. ApplicationMaster **单个任务**运行的老大
4. Container 容器，相当于一台独立的服务器，里面封装运行所需要的资源，如内存CPU、磁盘、网络等
# 3. MapReduce 
多节点执行任务，单节点返回