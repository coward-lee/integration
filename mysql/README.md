## 错误记录

* mysql远程登陆出错       
  ERROR 1130: Host ***.***.***.*** is not allowed to connect to this MySQL server     
  修改 mysql数据库下的       
  mysql的database下面对应登陆用户的host为%即可

# mysql

tips:

1. mysql(版本8.0.17)的表格可以不用指定主键
2.
3.

# 1. 锁

要解决的问题：

1. 当进行select、update、delete、insert
2. 名字解释
3. 应用场景
4. 并发度

>

    行锁 : 锁一行        
    表锁 : 锁整张表     
    间隙锁(gap锁) 
        锁连续多行（不过这里的行时开区间，锁 1-5行：锁的行是 2，3，4 不包括1和5行）            
    基本锁 ：
        共享锁     
        排他锁     
    意向锁(Intention Locks)     
        InnoDB为了支持多粒度(表锁与行锁)的锁并存，引入意向锁。     
        意向锁是表级锁，可分为意向共享锁(IS锁)和意向排他锁(IX锁)。       
        Intention shared (IS)
        Intention exclusive (IX)
        意向锁的意思是说你需要先获取到意向锁才能获取到对应的行锁
        但是意向锁的到底是不是行锁还是值得考量的
    记录锁
        单条索引记录上加锁，record lock锁住的永远是索引，而非记录本身，
        即使该表上没有任何索引，那么innodb会在后台创建一个隐藏的聚集主键索引，
        那么锁住的就是这个隐藏的聚集主键索引。
    插入意向锁
        一种特殊的间隙锁
        官方示例，大致意思就是，两个事务的间隙锁都是4-7但是他们的锁并不冲突，可以并发执行。
        Suppose that there are index records with values of 4 and 7. Separate transactions that attempt to insert values of 5 and 6, respectively, each lock the gap between 4 and 7 with insert intention locks prior to obtaining the exclusive lock on the inserted row, but do not block each other because the rows are nonconflicting.
    next-key锁(Next-Key Locks)
        record lock + gap lock, 左开右闭区间。    
    自增锁(AUTO-INC Locks)
        An AUTO-INC lock is a special table-level lock taken by transactions inserting into tables with AUTO_INCREMENT columns.
        插入自增行的时候发生的锁

# 日志

- redo log 的单位是页
- undo log 的单位是行

优化策略
innodb_undo_directory
innodb_undo_logs
innodb_undo_tablespaces

# 事务

## 事务准备

1. 事务基本概念
   acid
2. undo 日志  
   将原来的操作进行逆操作的行为。       
   如：insert 对应 delete    
   delete 对应 insert  
   update：  
   非主键修改，反向修改  
   主键修改，delete insert

3. redo 日志
    - 在系统崩溃之后更具checkpoint 进行redo操作
    - redo日志类型有
    - 基础sql 的 redo log
    - undo的redo log

4. 事务隔离级别
    1. 读未提交
    2. 读已提交
    3. 可重复读
    4. 序列化
5. mvcc 机制
   多版本并发控制(multi version concurrent control)

# mysql 读写分离方案

## 1. 主从方案

### 1.1 主从复制

- mysql 启动参数配置

<pre>
# master 节点配置

[mysqld]
# 启动二进制日志系统 这个需要在配置文件  
log-bin=mysql-bin
#  要同步的数据库  
binlog-do-db=test
# 服务id，主服务器server-id比从服务器server-id小
server-id=1
# 避免同步mysql用户配置
binlog-ignore-db=mysql

# 从节点配置
[mysqld]
server-id=2                     
replicate-ignore-db=mysql
replicate-do-db=test
</pre>

-
- master以及slave用户信息配置以及启动同步命令

```mysql
-- master
-- 创建从节点的连接用户
CREATE USER 'slave'@'%' IDENTIFIED BY '666666';
-- 授权 slave 可以在 slave用户在任意ip进行数据拉取
grant replication slave on *.* to 'slave'@'%';
-- 修改认证方式，方式从节点连接出现报错
alter user 'slave'@'%' IDENTIFIED with mysql_native_password by '666666';
-- 刷新数据库用户权限
flush privileges;

-- slave
-- sql 执行
-- 创建master 信息
change master to master_host ='192.168.214.133', master_user ='slave', master_password ='666666', master_port =3306, master_log_file ='binlog.000001', master_log_pos = 0, master_connect_retry =30;
-- 开始作为从节点
#    start slave;
-- 开始同步信息
START REPLICA;
#    stop slave ;
stop replica;
-- 
show slave status;
-- 显示同步信息
SHOW REPLICA STATUS;
show processlist;
-- 重新配置从节点信息
reset slave;
```

### 1.2 双主节点

怎么避免回环bin log 写入：

如下格式，这个binlog 会记录创建该事件的server id

| 字段名称               | 格式                              | 描述                                                  |
|--------------------|---------------------------------|-----------------------------------------------------|
| when               | 4 字节无符号整型, 表示为 struct timeval   | 	查询开始的时间，自1970年以来的秒数                                |
| type_code          | 1 字节                            | 	事件类型，见 Log_event_type                              |
| unmasked_server_id | 4 字节无符号整型                       | 创建该事件的 server id                                    |
| data_written       | 4 字节无符号整型                       | 	该事件的总长度，以字节为单位。包含 Common-Header，Post-Header 和 Body |
| log_pos            | 4 字节无符号整型                       | 	下一个事件在 master 节点 binlog 中的位置，距离文件开头的字节数            |
| flags              | 2 字节                            | 取决于 binlog 版本，表示不多于 16 个标志                          |

### 1.3 读写分离
- 解决写延迟的问题
- 基于 mycat

- 一主一从
```mysql
  -- 配置源
  /*+ mycat:createDataSource{ "name":"rwSepw","url":"jdbc:mysql://192.168.214.133:3306/mydb1","user":"root","password":"666666"}*/;
  -- 配置源
  /*+ mycat:createDataSource{ "name":"rwSepr","url":"jdbc:mysql://192.168.214.134:3306/mydb1","user":"root","password":"666666"}*/;
  -- 展示源
  /*+ mycat:showDataSources{}*/;
  -- 创建集群
  /*! mycat:createCluster{"name":"prototype","masters":["rwSepw"],"replicas":["rwSepr"]} */
  -- 显示集群信息
  /*+ mycat:showClusters{}*/
  
```
- 二主二从
- 配置文件
<pre>
[mysqld]
port=3301
#log-bin=lee-bin
#binlog_format=ROW
#binlog-do-db=mydb1
#server-id=1
#binlog-ignore-db=mysql 
#log-slave-updates

[mysqld]
port=3302
server-id=2


[mysqld]
port=3303
log-bin=lee-bin
binlog_format=STATEMENT
# 作为从节点的时候，有写入操作也要更新二进制日志文件
binlog-do-db=mydb1
server-id=3
binlog-ignore-db=mysql
log-slave-updates


[mysqld]
port=3304
server-id=4


</pre>
```mysql
  -- 基于注解的执行
  -- 配置源
  /*+ mycat:createDataSource{ "name":"master1","url":"jdbc:mysql://192.168.214.134:3301/mydb1","user":"root","password":"666666"}*/;
  /*+ mycat:createDataSource{ "name":"slave2","url":"jdbc:mysql://192.168.214.134:3302/mydb1","user":"root","password":"666666"}*/;
  /*+ mycat:createDataSource{ "name":"master3","url":"jdbc:mysql://192.168.214.134:3303/mydb1","user":"root","password":"666666"}*/;
  /*+ mycat:createDataSource{ "name":"slave4","url":"jdbc:mysql://192.168.214.134:3304/mydb1","user":"root","password":"666666"}*/;
  -- 展示源
  /*+ mycat:showDataSources{}*/;
  -- 创建集群
  /*! mycat:createCluster{"name":"prototype","masters":["master1","master3"],"replicas":["master3","slave2","slave4"]} */
  -- 显示集群信息
  /*+ mycat:showClusters{}*/
```
### 1.4 故障转移

### 1.5 分库分表
#### 1. 分库有的方式，有哪些方式

#### 2. 分辨有哪些方式
#### 3. 分库分表的限制
1. 分表的那些sql 不能执行，或者执行有问题
2. 分库分表之后怎么进行分页
### 性能测试
16c 16G
10w 条插入速度
63348 ms
每一个线程维护一个链接
16133 ms