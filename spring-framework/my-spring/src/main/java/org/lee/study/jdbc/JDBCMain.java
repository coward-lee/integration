package org.lee.study.jdbc;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("org.lee.study.jdbc")
@EnableAspectJAutoProxy
public class JDBCMain {

	public static void main(String[] args) {

		var context = new AnnotationConfigApplicationContext(JDBCMain.class);
		Repository repository = context.getBean("repository", Repository.class);
		Content query = repository.query("11");
		repository.save(new Content("33", "33"));
		System.out.println(query);
	}
	@Bean
	public DataSource dataSource(){
		var dataSource = new SingleConnectionDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://182.42.106.130:3306/sharding_jdbc_01");
		dataSource.setUsername("root");
		dataSource.setPassword("666666");
		return dataSource;
	}

}
