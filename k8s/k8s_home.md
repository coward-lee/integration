<div style="text-align: center; font-size: 33px; font-weight: bold; ">k8s</div>



# 组件介绍
api server
kubectl

webui
replication controller（controller manager） ： 维持副本的期望数量
scheduler: 负责介绍任务，选择合适的节点进行分配
etcd： 存储k8s集群所有重要信息（持久化）
kubelet： 直接跟容器引擎交互实现容器的生命周期管理
kube-proxy： 负责写入规则至 IPTABLES，IPVS，实现服务映射访问
CoreDNS ：可以为器群中svc穿件一个域名IP对应关系解析
ingress controller：官方只能实现四层代理，ingress 可以实现七层代理
Prometheus：提供k8s的监控能力
dashboard ： 给 k8s 提供一个b/s结构访问体系
federation：提供一个跨集群中心多k8s统一管理功能
ELK：提供k8s集群日志统一分析介入

# 基本概念
## pod

- 自主式pod   
  死亡之后不会被重新拉起来的pod，也就是自身自灭的pod  
  在同一个pod 里面共享同一个网络栈（也就是端口不能相同），同时也共享同一个存储卷（也就是存在文件名冲突的情况）

- 控制器管理的pod
分为三类控制器（）
  - replication controller   
    用来确保容器应用的副本数始终保持在用户定义的副本数，即如果容器异常退出会创建新的pod来替代；如果异常多出来的容器也会自动回收。在新版的kubernetes中建议使用replica set来取代replication controller。当然如果资源不足的情况下就无法达到期望的副本数量
  - replica set   
    和 replication controller 没有本质不同，只是名字不一样，并且replica set只是集合式selector
  - deployment
    虽然replica set 可以单独使用，但是一般建议使用deployment来自动管理replica set ，这样就无需担心其他机制的不兼容问题（比如replica set 不支持rolling-update，但是deployment）
  - hpa （horizontal pod autoscaling）  
    仅适用与deployment 和replicaset， 在v1版本中仅支持根据pod的cpu利用率扩缩容，在v1 alpha版本中，支持根据内存和用户自定义的metric扩缩容（可以自己定义规则进行扩容或者缩容）  
    如规则（cpu核数》80 停止扩容，pod数量在2-10之间），只要满足其中一个条件就会停止扩容，如果都溢出了就需要进行缩容
  
## stateFullSet

- 稳定的持久化存储，即pod重新调度后还是能访问到相同的持久化数据，基于pvc实现
- 稳定的网络标志，即pod重新调度后其pod name 和hostname 不变，基于headless service（即没有cluster ip 的service）实现
- 有序部署，有序扩展，pod是有序的，在部署或者扩展的时候要依据定义的顺序依次进行（即从0到N-1，在下一个pod运行之前所有的pod必须是running和ready状态），基于init container 来实现
- 有序收缩，有序删除（即从N-1到0）

## daemon set
 确保全部（或者一些 ）node上运行一个pod的副本。当有node加入集群时，也会为他们新增一个pod。当node从集群移除时，这些pod也会被回收。删除daemonset 会将删除它创建的所有pod。
一些daemon set的典型用法：
  - 云心集群存储daemon。列如在每一个node上运行glusterd，ceph。
  - 在每一个node上运行日志收集daemon，列如fluentd，logstash
  - 在每一个ndoe上运行监控daemon，列如Prometheus node exporter
## job， corn job
job 仅执行一次的任务，它抱枕批处理任务的一个或者多个pod成功结束
corn job 基于时间的job，给定时间点的周期执行
## 服务发现
不同的pod副本集之前范围需要使用一个中间件（service-具体的服务）来进行统一访问管理；通过这个统一的服务来进行访问可以实现负载均衡，节点挂了之后重新分配pod是地址变化后对于客户端的访问的影响。

