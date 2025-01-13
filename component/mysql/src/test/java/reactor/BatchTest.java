package reactor;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class BatchTest {

    /**
     * statement 中的 batch 处理
     */

    private final static Logger log = LoggerFactory.getLogger(BatchTest.class);

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
        preparedStatement.setString(1, "xxx");
        preparedStatement.addBatch();
        preparedStatement.setString(1, "p_s_demo");
        preparedStatement.addBatch();
        int[] ints = preparedStatement.executeBatch();

        connection.commit();
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void prepare_data() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.214.133:3306/test", "root", "666666");
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into test.demo(name) values(?)");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10000; j++) {
                preparedStatement.setString(1, "xxx" + (i * j));
                preparedStatement.addBatch();
                preparedStatement.setString(1, "p_s_demo" + (i * j));
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            System.out.println(i);
        }

        connection.commit();
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void press_test1() {
        System.out.println("test");
    }

    /**
     * 压测
     */
    @Test
    public void press_test() throws Exception {
        new BatchTest().test_in_one_connection_per_insert("192.168.214.134:3301", "root", "666666");
    }

    static
    ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public void pres_test_in_one_connection(String ipPort, String user, String password) {

        try {

            Random random = new Random();
            ExecutorService executorService = Executors.newFixedThreadPool(12);
            Class.forName("com.mysql.cj.jdbc.Driver");
            printTime(() -> {
                CompletableFuture[] roots = IntStream.range(0, 100000).parallel().mapToObj(i -> {
                    return CompletableFuture.runAsync(
                            () -> {
                                try {
                                    Connection connection = threadLocal.get();
                                    if (connection == null) {
                                        connection = DriverManager.getConnection("jdbc:mysql://" + ipPort + "/mydb1", user, password);
                                        connection.setAutoCommit(false);
                                        threadLocal.set(connection);
                                    }
                                    char c1 = (char) random.nextInt(128);
                                    char c2 = (char) random.nextInt(128);
                                    char c3 = (char) random.nextInt(128);
                                    char c4 = (char) random.nextInt(128);
                                    char c5 = (char) random.nextInt(128);
                                    PreparedStatement preparedStatement = connection.prepareStatement("insert into test2(name, content) values(?,?)");
                                    preparedStatement.setString(1, "value" + c1 + c2 + c3 + c4);
                                    preparedStatement.setString(2, "content" + c1 + c2 + c3 + c5);
                                    preparedStatement.execute();
                                    connection.commit();
                                    preparedStatement.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }
                            , executorService);
                }).toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(roots).join();
            });


//            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    public void test_in_one_connection_per_insert(String ipPort, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipPort + "/mydb1", user, password);
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into test2(name, content) values(?,?)");
            preparedStatement.setString(1, "value");
            preparedStatement.setString(2, "content");
            preparedStatement.execute();
            connection.commit();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test_in_one_connection_per_insert() {
        printTime(() -> {
            CompletableFuture[] roots = IntStream.range(0, 100000).parallel().mapToObj(i -> {

                return CompletableFuture.runAsync(() -> new BatchTest().test_in_one_connection_per_insert("192.168.214.134:3301", "root", "666666"));

            }).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(roots).join();
        });
    }


    public static void main(String[] args) {
        new BatchTest().pres_test_in_one_connection("192.168.214.132:8066", "root", "123456");
    }

    static void printTime(Runnable runnable) {

        long start = System.currentTimeMillis();
        runnable.run();
        System.out.println((System.currentTimeMillis() - start) + " ms");
    }
}
