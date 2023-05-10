package reactor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class MysqlnsertDataService {

    public static void main(String[] args) {

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setPassword("666666");
        dataSource.setUsername("root");
        dataSource.setUrl("jdbc:mysql://159.138.145.155:3306/test");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        for (int i = 0; i < 10000000; i++) {
            jdbcTemplate.update(String.format("insert into data(name) value(%s)", i));
            System.out.println("插入了：" + i);
        }

    }
}
