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



