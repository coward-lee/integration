package reactor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

@RestController
@RequestMapping("/query")
public class RestControllerDbQuery {

    Random random = new Random();

    @RequestMapping
    public String query() {
        return query("select * from test2 where id=" + random.nextInt(100000));
    }

    private String query(String sql) {
        try ( Connection connection  = DataSource.getConnection()){

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String content = resultSet.getString("content");
                result.append(id).append("- name:").append(name).append("-content:").append(content).append("\n");
            }
            return result.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
