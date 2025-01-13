package org.lee.study;

import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class Main {


    @Test
    void test_get_start() throws Throwable {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0", dataSource("ds0"));
        dataSourceMap.put("ds1", dataSource("ds1"));

        Properties properties = new Properties();


        // 配置分表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("user", "ds0.user${0..1}");

        // 设置分库分表的字段 和对应的规则
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds0"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "user${id % 2}"));

        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 创建 数据源
        ShardingDataSource dataSource = (ShardingDataSource) ShardingDataSourceFactory.createDataSource(
                dataSourceMap,
                shardingRuleConfig,
                properties
        );

        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(" insert into user(id, name) values(?,'lee01'),(?,'lee02')");
        preparedStatement.setLong(1, 3L);
        preparedStatement.setLong(2, 45L);
        preparedStatement.execute();
        preparedStatement.close();


        // 配置读写分离的规则
        // Configure read-write split rule
        // final String name, final String masterDataSourceName, final List<String> slaveDataSourceNames
        // 按理说应该是 master 写 slave 读
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("ds_master_slave", "ds_master", Arrays.asList("ds_slave0", "ds_slave1"));

    }

    public static DataSource dataSource(String dataBase) {

        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource(null, "jdbc:mysql://182.42.106.130:3306/" + dataBase, "root", "666666");
        simpleDriverDataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        return simpleDriverDataSource;
    }


    @Test
    void test_sharding_user() throws SQLException {
        TransactionTypeHolder.set(TransactionType.LOCAL);

        Connection connection = getShardingDataSource().getConnection();
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(" insert into user(id, name)" +
                " values(?,'lee01'),(?,'lee01')," +
                "(?,'lee02'),(?,'lee02')");
        preparedStatement.setLong(1, 5L);
        preparedStatement.setLong(2, 6L);
        preparedStatement.setLong(3, 7L);
        preparedStatement.setLong(4, 8L);
        preparedStatement.execute();
        preparedStatement.close();
        connection.commit();
    }

    @Test
    void test_sharding_item() throws SQLException {

        PreparedStatement preparedStatement = getShardingDataSource().getConnection().prepareStatement(" insert into item(id, name)" +
                " values(?,'lee01'),(?,'lee01')," +
                "(?,'lee02'),(?,'lee02')");
        preparedStatement.setLong(1, 1L);
        preparedStatement.setLong(2, 2L);
        preparedStatement.setLong(3, 3L);
        preparedStatement.setLong(4, 4L);
        preparedStatement.execute();
        preparedStatement.close();
    }

    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getOrderUserTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("user, item");


        // 表格分片规则
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseShardingAlgorithm<Long>() {
            @Override
            public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> shardingValue) {
                for (String tableName : tableNames) {
                    if (tableName.endsWith("_" + shardingValue.getValue() % 2)) {
                        return tableName;
                    }
                }
                return "xxxxx";
            }
        }));


        // 数据库分片规则
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("name", new PreciseShardingAlgorithm() {
            @Override
            public String doSharding(Collection availableTargetNames, PreciseShardingValue shardingValue) {
                String s = shardingValue.getValue().toString();
                if (s.contains("1")) {
                    return "ds0";
                }
                return "ds1";
            }
        }));
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }

    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        return new KeyGeneratorConfiguration("SNOWFLAKE", "order_id");
    }

    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("user", "ds${0..1}.user${0..1}");
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        return result;
    }

    TableRuleConfiguration getOrderItemTableRuleConfiguration() {
        return new TableRuleConfiguration("item", "ds${0..1}.item_${0..1}");
    }

    TableRuleConfiguration getOrderUserTableRuleConfiguration() {
        return new TableRuleConfiguration("user", "ds${0..1}.user_${0..1}");
    }

    Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("ds0", dataSource("ds0"));
        result.put("ds1", dataSource("ds1"));
        return result;
    }


}
