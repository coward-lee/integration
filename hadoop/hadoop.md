

# 华为云 ： 102
# 天翼云 ： 103
# 腾讯云 ： 104
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


# 4. hadoop shell命令

1. -help 命令参数     
   hadoop fs -help [command]
2. 上传      
      
    本地文件剪切到hadoop      
   hadoop fs -moveFromLocal  [本地源文件(夹)] [hadoop目标文件(夹)]      
   本地文件复制到hadoop      
   hadoop fs -copyFromLocal  [本地源文件(夹)] [hadoop目标文件(夹)]      
   本地文件复制到hadoop      
   hadoop fs -put  [本地源文件(夹)] [hadoop目标文件(夹)]      
   追加一个文件到已存在的文件末尾      
   hadoop fs -appendToFile  [本地源文件(夹)] [hadoop目标文件(夹)]      

3. 下载

   本地文件复制到hadoop      
   hadoop fs -copyToLocal  [hadoop目标文件(夹)] [本地源文件(夹)]      
   本地文件复制到hadoop      
   hadoop fs -get  [本地源文件(夹)] [hadoop目标文件(夹)]      
   追加一个文件到已存在的文件末尾      
   hadoop fs -appendToFile  [本地源文件(夹)] [hadoop目标文件]      
4. HDFS直接操作  
下面都是的形式 hadoop fs -[command] [args]
    1. -ls
    2. -cat
    3. -chgrp -chmod -chown 
    4. -mkdir
    5. -cp [hadoop里的源文件] [hadoop里的源文件 (需要指定目标文件名称)]
    6. -mv [hadoop里的源文件] [hadoop里的源文件 (需要指定目标文件名称)]
    7. -tail
    8. -rm
    9. -du 统计文件夹大小信息 ( 多个参数需要单独分开)
      如 hadoop fa -du   -s -h /
    10. -setreq 设置hdfs 的副本数量
        如 hadoop fa -du [副本数量] [目标文件]
    11. 