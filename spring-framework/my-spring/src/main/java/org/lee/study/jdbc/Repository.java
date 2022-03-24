package org.lee.study.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Service
public class Repository {
	private final JdbcTemplate jdbcTemplate;

	public Repository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public Content query(String id){
		return jdbcTemplate.query("select * from shading_table where id="+id, new ContentMapper()).get(0);
	}
	public int save(Content content){

		return jdbcTemplate.update("insert into shading_table(id, content) values(?,?)",
				new Object[]{content.id, content.content}, new int[]{Types.INTEGER, Types.VARCHAR});
	}
	class ContentMapper implements RowMapper<Content> {
		@Override
		public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Content(rs.getString("id"), rs.getString("content"));
		}
	}
}
