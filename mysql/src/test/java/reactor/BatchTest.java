package reactor;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class BatchTest {

    /**
     * statement 中的 batch 处理
     */

    public void test_batch_statement() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://182.42.106.130:3306/test_db", "root", "666666");
        // 未设置 会报 Can't call commit when autocommit=true 错误
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.addBatch("insert into test_db.alter_test 'a'");
        statement.addBatch("insert into test_db.alter_test 'b'");
        int[] ints = statement.executeBatch();
        connection.commit();
        statement.close();
        connection.close();
    }

    /**
     * statement 中的 batch 处理
     */

    public void test_batch_prepared_statement() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://182.42.106.130:3306/test_db", "root", "666666");
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into test_db.alter_test(name) values(?)");
        preparedStatement.setString(1,"xxx");
        preparedStatement.addBatch();
        preparedStatement.setString(1,"p_s_demo");
        preparedStatement.addBatch();
        int[] ints = preparedStatement.executeBatch();

        connection.commit();
        preparedStatement.close();
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        new BatchTest().test_batch_prepared_statement();
    }
}
