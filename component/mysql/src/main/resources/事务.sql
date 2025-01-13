create table test_load(
    a int,
    b char(80)
) engine= innodb;

delimiter //
create procedure p_load(count int  unsigned)
begin
    declare s int unsigned default 1;
    declare c char(80) default repeat('a', 80);
    while s <= count do
        insert into test_load select null,c;
        commit;
        set s = s + 1;
        end while ;
end;

call p_load(500000);

show variables like 'innodb_flush_log_at_trx_commit';

set global innodb_flush_log_at_trx_commit=0;

desc information_schema.innodb_trx_rollback_segment;

select * from information_schema.innodb_trx;

begin ;
select * from lock_table for update ;
select * from lock_table ;
commit ;


show variables like '%isolation%';
set global transaction_isolation='read-committed';