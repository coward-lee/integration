
## 1. 引入依赖

    implementation 'org.apache.shardingsphere:sharding-jdbc-core:4.1.1'
## 2. 分片规则
1. ShardingDataSourceFactory 数据源工厂，用于数据分片的时候使用
2. ShardingRuleConfiguration 分片规则配置对象
3. TableRuleConfiguration 表格分片规则配置对象
4. StandardShardingStrategyConfiguration  单分片键的标准分片场景



## 3. sharding 支持的sql
```sql
SELECT * FROM tbl_name;
SELECT * FROM tbl_name WHERE (col1 = ? or col2 = ?) and col3 = ?;
SELECT * FROM tbl_name WHERE col1 = ? ORDER BY col2 DESC LIMIT ?;
SELECT COUNT(*), SUM(col1), MIN(col1), MAX(col1), AVG(col1) FROM tbl_name;
WHERE col1 = ?;
SELECT COUNT(col1) FROM tbl_name WHERE col2 = ? GROUP BY col1 ORDER;
BY col3 DESC LIMIT ?, ?;
INSERT INTO tbl_name (col1, col2,…) VALUES (?, ?, ….);
INSERT INTO tbl_name VALUES (?, ?,….);
INSERT INTO tbl_name (col1, col2, …) VALUES (?, ?, ….), (?, ?, ….);
UPDATE tbl_name SET col1 = ? WHERE col2 = ?;
DELETE FROM tbl_name WHERE col1 = ?;
CREATE TABLE tbl_name (col1 int, …);
ALTER TABLE tbl_name ADD col1 varchar(10);
DROP TABLE tbl_name;
TRUNCATE TABLE tbl_name;
CREATE INDEX idx_name ON tbl_name;
DROP INDEX idx_name ON tbl_name;
DROP INDEX idx_name;
SELECT DISTINCT * FROM tbl_name WHERE col1 = ?;
SELECT COUNT(DISTINCT col1) FROM tbl_name;
```



