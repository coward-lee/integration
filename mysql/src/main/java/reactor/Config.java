package reactor;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Bean
    public JdbcTemplate jdbcTemplate(){
        DataSource build = DataSourceBuilder.create().type(SimpleDriverDataSource.class)
                .password("666666")
                .username("root")
                .url("jdbc:mysql://182.42.106.130:3306/test_db")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        return new JdbcTemplate(build);
    }


    public static JdbcTemplate getJdbcTemplate(){
        DataSource build = DataSourceBuilder.create().type(SimpleDriverDataSource.class)
                .password("666666")
                .username("root")
                .url("jdbc:mysql://182.42.106.130:3306/test_db")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        return new JdbcTemplate(build);
    }
}
