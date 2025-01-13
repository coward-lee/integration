package org.lee.study;

import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {


    public static void main(String[] args) throws Throwable {
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
        preparedStatement.setLong(1, 1L);
        preparedStatement.setLong(2, 2L);
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


}
