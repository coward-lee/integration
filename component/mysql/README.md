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

| 字段名称               | 格式                            | 描述                                                  |
|--------------------|-------------------------------|-----------------------------------------------------|
| when               | 4 字节无符号整型, 表示为 struct timeval | 	查询开始的时间，自1970年以来的秒数                                |
| type_code          | 1 字节                          | 	事件类型，见 Log_event_type                              |
| unmasked_server_id | 4 字节无符号整型                     | 创建该事件的 server id                                    |
| data_written       | 4 字节无符号整型                     | 	该事件的总长度，以字节为单位。包含 Common-Header，Post-Header 和 Body |
| log_pos            | 4 字节无符号整型                     | 	下一个事件在 master 节点 binlog 中的位置，距离文件开头的字节数            |
| flags              | 2 字节                          | 取决于 binlog 版本，表示不多于 16 个标志                          |

### 1.3 读写分离

- 解决写延迟的问题
- 基于 mycat

- 一主一从

```mysql
  -- 配置源
/*+ mycat:createDataSource{ "name":"rwSepw","url":"jdbc:mysql://192.168.214.132:3306/mydb1","user":"root","password":"666666"}*/;
-- 配置源
/*+ mycat:createDataSource{ "name":"rwSepr","url":"jdbc:mysql://192.168.214.132:3306/mydb1","user":"root","password":"666666"}*/;
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
/*+ mycat:createDataSource{ "name":"master1","url":"jdbc:mysql://192.168.214.132:3301/mydb1","user":"mycat","password":"666666"}*/;
/*+ mycat:createDataSource{ "name":"slave2","url":"jdbc:mysql://192.168.214.132:3302/mydb1","user":"mycat","password":"666666"}*/;
/*+ mycat:createDataSource{ "name":"master3","url":"jdbc:mysql://192.168.214.132:3303/mydb1","user":"mycat","password":"666666"}*/;
/*+ mycat:createDataSource{ "name":"slave4","url":"jdbc:mysql://192.168.214.132:3304/mydb1","user":"mycat","password":"666666"}*/;
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

#### 2. 分表有哪些方式

#### 3. 分库分表的限制

1. 分表的那些sql 不能执行，或者执行有问题
2. 分库分表之后怎么进行分页

- 全局表查询 - 单独用一张表格存储分页信息
- 禁止跳页查询 - 记录上一次分页的结尾，从上一次结尾的数据开始到到下一页
- 按日期的二次查询法
- 大数据集成法
- NewSql法
- 有序的二次查询法

1. select * from test order by column limit start,num
2. 将sql 改写为 select * from test order by column limit start/n,num (n位分表数量)
3. 从从结果中获取column的最小值，我们假定这个值为 min
4. 再次查询 select * from test between min and table_max limit start/n,num(
   table_max为第2步每一个数据库获取的数据column的最大值)，
5. 这样可以根据放回结果计算 min的全局offset   
   计算方法： offset = start - (second_result_len() - num)+ ..+(second_result_len() - start/n); second_result_len:
   第二次，第4步查询返回的数据长度
   列如 start = 100 , num = 10, n = 3 ，second_result_len 分别为 3，7，8
   那么mid 的offset = 100 - (3-3)-(7-3)-(8-3) = 100 -4-5 = 91 也就是 mid的全局offset 为91
   同时 start/n = 33
6. 但是这个存在一个问题，如果
   存在这样一个情况： 数据分布为
   0 - 999
   222, 1001 - 1999
   2000 - 2999
   我执行：select * from test order by column limit 1000,10
   那么时候算出来
   min = 333 的 全局offset=334 此时怎么从内存里面直接将1000,10 之后的数据取出来，或者有一种快速的方法从MySQL取出来

- 测试分页结果
- 并行查询和排序

### 性能测试

16c 16G
10w 条插入速度
63348 ms
每一个线程维护一个链接
16133 ms

# mysql 索引

## 1. 索引数据结构

### b+ 树

- 使用场景

- 操作逻辑

- 使用限制
  由于mysql每一个页是16K，非叶子结点只记录索引信息，每一个索引信息记录大概使用 16b（页地址指针+索引key）   
  每一个页的节点大概可以存储1000个索引信息   
  所以三层一共可以存储     
  1000*1000*(16k/表格一行的存储空间)，假定 表格一行的存储空间 160b，也就是每一页大概可以存储100行   
  也就是大概 100，000，000 一千万条数据,如果超过1000万，那么就需要4层了也就是需要至少四次IO那么性能就下降了
- hash 索引
  使用场景
  操作逻辑
