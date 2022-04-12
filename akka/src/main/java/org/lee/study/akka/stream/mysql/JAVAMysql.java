package org.lee.study.akka.stream.mysql;

import java.sql.*;

public class JAVAMysql {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String JDBC_URL = "jdbc:mysql://182.42.106.130:3306/logbin_demo";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "666666";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.setFetchSize(1000);
                try (ResultSet rs = stmt.executeQuery("SELECT * FROM shading_table ")) {
                    while (rs.next()) {
                        String id = rs.getString("id");
                        String content = rs.getString("content");
                        System.out.println("id:" + id + "  content:" + content);
                    }
                }
            }
        }
//        Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
//        Statement stmt = conn.createStatement();
//        stmt.setFetchSize(100);
//        ResultSet rs = stmt.executeQuery("SELECT * FROM shading_table ");
//        rs.next();
////        while (rs.next()){
//            String id = rs.getString("id");
//            String content = rs.getString("content");
//            System.out.println("id:"+id+"  content:"+content);
////        }
//        rs.close();
//        stmt.close();
//        conn.close();
    }

}
