
### sharding jdbc 支持的sql
SELECT * FROM tbl_name
SELECT * FROM tbl_name WHERE (col1 = ? or col2 = ?) and col3 = ?
SELECT * FROM tbl_name WHERE col1 = ? ORDER BY col2 DESC LIMIT ?
SELECT COUNT(*), SUM(col1), MIN(col1), MAX(col1), AVG(col1) FROM tbl_name
WHERE col1 = ?
SELECT COUNT(col1) FROM tbl_name WHERE col2 = ? GROUP BY col1 ORDER
BY col3 DESC LIMIT ?, ?
INSERT INTO tbl_name (col1, col2,…) VALUES (?, ?, ….)
INSERT INTO tbl_name VALUES (?, ?,….)
INSERT INTO tbl_name (col1, col2, …) VALUES (?, ?, ….), (?, ?, ….)
UPDATE tbl_name SET col1 = ? WHERE col2 = ?
DELETE FROM tbl_name WHERE col1 = ?
CREATE TABLE tbl_name (col1 int, …)
ALTER TABLE tbl_name ADD col1 varchar(10)
DROP TABLE tbl_name
TRUNCATE TABLE tbl_name
CREATE INDEX idx_name ON tbl_name
DROP INDEX idx_name ON tbl_name
DROP INDEX idx_name
SELECT DISTINCT * FROM tbl_name WHERE col1 = ?
SELECT COUNT(DISTINCT col1) FROM tbl_name


### 不支持的SQL
SQL 不支持原因
INSERT INTO tbl_name (col1, col2, …) VALUES(1+2, ?, …)
VALUES语句不支持运
算表达式
INSERT INTO tbl_name (col1, col2, …) SELECT col1, col2, …
FROM tbl_name WHERE col3 = ?
INSERT .. SELECT
SELECT COUNT(col1) as count_alias FROM tbl_name GROUP BY
col1 HAVING count_alias > ?
HAVING
SELECT * FROM tbl_name1 UNION SELECT * FROM tbl_name2 UNION
SELECT * FROM tbl_name1 UNION ALL SELECT * FROM
tbl_name2
UNION ALL
SELECT * FROM ds.tbl_name1 包含schema
SELECT SUM(DISTINCT col1), SUM(col1) FROM tbl_name
详见DISTINCT支持情
况详细说明
SELECT * FROM tbl_name WHERE to_date(create_time, ‘yyyymm-
dd’) = ?
会导致全路由

### distinct 支持的情况


SELECT DISTINCT * FROM tbl_name WHERE col1 = ?
SELECT DISTINCT col1 FROM tbl_name
SELECT DISTINCT col1, col2, col3 FROM tbl_name
SELECT DISTINCT col1 FROM tbl_name ORDER BY col1
SELECT DISTINCT col1 FROM tbl_name ORDER BY col2
SELECT DISTINCT(col1) FROM tbl_name
SELECT AVG(DISTINCT col1) FROM tbl_name
SELECT SUM(DISTINCT col1) FROM tbl_name
SELECT COUNT(DISTINCT col1) FROM tbl_name
SELECT COUNT(DISTINCT col1) FROM tbl_name GROUP BY col1
SELECT COUNT(DISTINCT col1 + col2) FROM tbl_name
SELECT COUNT(DISTINCT col1), SUM(DISTINCT col1) FROM tbl_name
SELECT COUNT(DISTINCT col1), col1 FROM tbl_name GROUP BY col1
SELECT col1, COUNT(DISTINCT col1) FROM tbl_name GROUP BY col1

不支持的sql
SELECT SUM(DISTINCT col1), SUM(col1) FROM tbl_name   同时使用普通聚合函数和DISTINCT聚合函数

### 分页
完全支持MySQL的分页查询

查询偏移量过大的分页会导致数据库获取数据性能低下，以MySQL为例：



