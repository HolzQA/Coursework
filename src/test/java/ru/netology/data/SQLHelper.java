package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {

    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {}

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void cleanDatabase() {
        runner.execute(getConn(),"DELETE FROM credit_request_entity");
        runner.execute(getConn(), "DELETE FROM payment_entity");
        runner.execute(getConn(),"DELETE FROM order_entity");
    }

    @SneakyThrows
    public static Integer getAmountFromDatabase() {
        var verCode = "SELECT amount FROM payment_entity LIMIT 1";
        return runner.query(getConn(), verCode, new ScalarHandler<Integer>());
    }
}
