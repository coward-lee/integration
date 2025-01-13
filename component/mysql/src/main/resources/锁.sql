show engine innodb mutex;

show engine innodb status\G;

show full processlist;

-- 分析事务

select *
from information_schema.INNODB_TRX;
show create table information_schema.INNODB_TRX;

CREATE TEMPORARY TABLE `INNODB_TRX`
(
    `trx_id`                     bigint unsigned NOT NULL DEFAULT '0' comment '存储引擎内部唯一id',
    `trx_state`                  varchar(13)     NOT NULL DEFAULT '' comment '事务的状态',
    `trx_started`                datetime        NOT NULL DEFAULT '0000-00-00 00:00:00' comment '事务开始时间',
    `trx_requested_lock_id`      varchar(105)             DEFAULT NULL comment '等待事务的锁ID',
    `trx_wait_started`           datetime                 DEFAULT NULL comment '等待事务的时间',
    `trx_weight`                 bigint unsigned NOT NULL DEFAULT '0' comment '事务的权重',
    `trx_mysql_thread_id`        bigint unsigned NOT NULL DEFAULT '0' comment 'mysql中的线程id，show full processlist 的列表显示的内容',
    `trx_query`                  varchar(1024)            DEFAULT NULL comment '事务执行的sql',
    `trx_operation_state`        varchar(64)              DEFAULT NULL,
    `trx_tables_in_use`          bigint unsigned NOT NULL DEFAULT '0',
    `trx_tables_locked`          bigint unsigned NOT NULL DEFAULT '0',
    `trx_lock_structs`           bigint unsigned NOT NULL DEFAULT '0',
    `trx_lock_memory_bytes`      bigint unsigned NOT NULL DEFAULT '0',
    `trx_rows_locked`            bigint unsigned NOT NULL DEFAULT '0',
    `trx_rows_modified`          bigint unsigned NOT NULL DEFAULT '0',
    `trx_concurrency_tickets`    bigint unsigned NOT NULL DEFAULT '0',
    `trx_isolation_level`        varchar(16)     NOT NULL DEFAULT '',
    `trx_unique_checks`          int             NOT NULL DEFAULT '0',
    `trx_foreign_key_checks`     int             NOT NULL DEFAULT '0',
    `trx_last_foreign_key_error` varchar(256)             DEFAULT NULL,
    `trx_adaptive_hash_latched`  int             NOT NULL DEFAULT '0',
    `trx_adaptive_hash_timeout`  bigint unsigned NOT NULL DEFAULT '0',
    `trx_is_read_only`           int             NOT NULL DEFAULT '0',
    `trx_autocommit_non_locking` int             NOT NULL DEFAULT '0',
    `trx_schedule_weight`        bigint unsigned          DEFAULT NULL
) ENGINE = MEMORY
  DEFAULT CHARSET = utf8mb3




select * from information_schema.innodb_locks;

select * from information_schema.innodb_lock_waits;

create table lock_table(
    id bigint not null auto_increment primary key ,
    name varchar(255)
);

-- 下面语句可以用来查询 哪一个事务阻塞了另外一个事务
select  r.trx_id waiting_trx_id, r.trx_mysql_thread_id waiting_thread, r.trx_query waiting_query,
    b.trx_mysql_thread_id blocking_thread, b.trx_query blocking_query
from information_schema.INNODB_lock_waits w
inner join information_schema.INNODB_LOCKS b on b.trx_id=w.blocking_trx_id
inner join information_schema.INNODB_TRX r on r.trx_id=w.request_trx_id

-- 事务隔离级别的案例测试
-- 这个需要开启两个session才是展示这个效果, 并且隔离级别为：REPEATABLE-READ
-- 先select未提交之前 update的值不是写进去
show variables ;
-- 查看事务隔离级别 默认为 REPEATABLE-READ
select @@transaction_isolation;
begin;
select * from lock_table where id=1;
commit ;

begin;
update lock_table set name='a' where id=1;
show processlist ;
commit
-- 自增长和锁

select max(id) from lock_table;

create table t_innodb(
                         a int auto_increment,
                         b int,
                         key(a,b)
) engine = innodb;
create table t_myisam(
                         a int auto_increment,
                         b int,
                         key(a,b)
) engine = myisam;
-- phantom problem 幻读
-- The so-called phantom problem occurs within a transaction when the same query produces different sets of rows at different times. For example, if a SELECT is executed twice, but returns a row the second time that was not returned the first time, the row is a “phantom” row.


-- next-key 案例 （record lock 和 gap lock 的结合）

create table next_key_table(
    a int primary key
);

insert into next_key_table values (1),(2),(5);
-- 间隙锁的范围为：左闭右开，
-- 锁范围 (-∞，1) [1,2) [2,5) (5,+∞)
-- 如若对5上锁，可以执行5以下的事务

begin;
select * from next_key_table where a=5;


begin;
insert into next_key_table values (4);


-- 幻读测试
set session transaction_isolation='READ-COMMITTED';
begin;
select * from next_key_table where a>2 for update ;

begin;
insert into next_key_table select 4;
commit;

select * from next_key_table where a>2 for update ;

set session transaction_isolation='REPEATABLE-READ';

begin;
select * from next_key_table where a=3 lock in share mode ;

# u * from next_key_table
