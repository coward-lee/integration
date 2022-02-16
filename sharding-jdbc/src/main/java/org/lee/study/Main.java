package org.lee.study;

import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    Properties properties = new Properties();
    ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
    Map<String, DataSource> dataSourceMap = new HashMap<>();
    DataSource dataSource = ShardingDataSourceFactory.createDataSource(
            dataSourceMap,
            shardingRuleConfig,
            properties
    );

    public Main() throws SQLException {
    }
}
