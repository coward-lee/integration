# 基于hash
create table test_part
(
    a int primary key,
    b int
) engine = innodb
    partition by hash ( mod(a, 4) )
        partitions 4;

insert into test_part(a, b)
values (1, 2),
       (2, 3);

select table_name, PARTITION_NAME, TABLE_ROWS
from information_schema.PARTITIONS
where table_name like 'test_%';


# 基于列
create table t_columns_range
(
    a int,
    b datetime
) engine = innodb
    partition by range columns (b)(
        partition p0 values less than ('2009-01-01'),
        partition p1 values less than ('2010-01-01')
        );

# 子分区
create table ts
(
    A INT,
    B datetime
)
    partition by range (year(b))
        subpartition by hash (to_days(b))
        subpartitions 2 (
        partition p0 values less than (1900),
        partition p1 values less than (2000),
        partition p2 values less than MAXVALUE
        );

# 该sql的子分区数量不一致 wrong number of subpartitions defined, mismatch with previous setting near
# 会出现如上的报错
# 还有子分区的名字必须是唯一的
create table ts_01
(
    a int,
    b date
)
    partition by range (year(b))
        subpartition by hash ( to_days(b) )(
        partition p0 values less than (1990)(
            subpartition s0,
            subpartition s1
            ),
        partition p1 values less than (2000),
        partition p2 values less than (maxvalue )(
            subpartition s2,
            subpartition s3
            )

        )
select * from information_schema.partitions;
