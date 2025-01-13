 # 问题记录

1. centos 8.x 好像没有 rsync 命令


# 日常记录

查看安装的软件 ： rpm -qa|grep jdk

问题 ：ERROR: but there is no HDFS_DATANODE_USER defined. Aborting operation.
解决：
export HDFS_NAMENODE_USER="root"
export HDFS_DATANODE_USER="root"
export HDFS_SECONDARYNAMENODE_USER="root"
export YARN_RESOURCEMANAGER_USER="root"
export YARN_NODEMANAGER_USER="root"