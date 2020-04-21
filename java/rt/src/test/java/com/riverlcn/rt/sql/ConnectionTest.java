package com.riverlcn.rt.sql;

import ch.vorburger.mariadb4j.DB;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * java.sql.Connection 测试.
 *
 * @author river
 */
public class ConnectionTest {

    // 内嵌的MYSQL实例
    private static DB database;

    // 连接数据库实例
    private Connection conn;

    @BeforeClass
    public static void startup() throws Exception {
        // 启动内嵌数据库，类似mysql
        log("start database at port localhost:3306");
        database = DB.newEmbeddedDB(3306);
        database.start();

        // 使用JDBC连接数据库实例，连接URL中不包含数据库
        Connection conn = getDataBaseInstanceConnection();

        // create database
        Statement stmt = conn.createStatement();
        stmt.addBatch("CREATE DATABASE DEMO");
        stmt.addBatch("CREATE TABLE DEMO.user(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(32))");
        stmt.executeBatch();
    }

    public static Connection getDataBaseInstanceConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306";
        return DriverManager.getConnection(url, "root", "");
    }

    @Before
    public void init() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/demo";
        conn = DriverManager.getConnection(url, "root", "");
    }

    /**
     * 使用JDBC连接数据库实例，连接URL中不包含数据库.
     */
    @Test
    public void testNativeSQL() throws SQLException {
        String sql = conn.nativeSQL("SELECT * FROM user WHERE name = ?");
        // 打印的SQL和输入的SQL一样，未变化
        log(sql);
    }

    @Test
    public void testDataBaseMetaData() throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        log("DataBase Product name: " + md.getDatabaseProductName());
        log("DataBase Product version: " + md.getDatabaseProductVersion());
        log("KeyWords: " + md.getSQLKeywords());
        log("Numeric functions: " + md.getNumericFunctions());
        log("String functions: " + md.getStringFunctions());
        log("System functions: " + md.getSystemFunctions());
        log("DateTime functions: " + md.getTimeDateFunctions());
        log("Search escape: " + md.getSearchStringEscape());

        // getMaxXXX 数据库允许的最大的xx
        log("mac column name length: " + md.getMaxColumnNameLength());

    }

    @AfterClass
    public static void teardown() throws Exception {
        if (database != null) {
            log("stop database.");
            database.stop();
        }
    }

    public static void log(String msg) {
        System.out.println(msg);
    }

}
