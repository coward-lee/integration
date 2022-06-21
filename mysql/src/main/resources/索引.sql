


-- auto-generated definition
create table test_index
(
    id    bigint auto_increment
        primary key,
    name  varchar(200) null,
    age   double       null,
    price int          null
);

create index test_index_price_index
    on test_index (price);




explain select  * from test_index where age>20 and age< 30;

explain select  * from test_index where price>20 and price< 30;
-- 开启 mrr（multi-range read）
set  @@optimizer_switch='mrr=on,mrr_cost_based=off';;


show engine innodb status;

-- ====================================================================================================================
-- 全文检索
show variables like 'innodb_ft_aux_%';

set global  innodb_optimize_fulltext_only=1;

optimize table fts_a;

create table fts_a(
    FTS_DOC_ID BIGINT UNSIGNED AUTO_INCREMENT NOT NULL primary key ,
    body text
);

insert into fts_a(body)
values ('pease porridge in the pot'),
       ('pease porridge hot, pease porridge cold'),
       ('nine days old'),
       ('some like it int hte pot'),
       ('nine days old'),
       ('i like code days');

create fulltext index idx_fts on fts_a(body);

select * from fts_a;

SET GLOBAL innodb_ft_aux_table='test_table/fts_a';

select * from information_schema.INNODB_FT_INDEX_TABLE;
