package ru.itis.teamwork.db;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ResourceInsertion {
    private final static int N = 10000;

    @SneakyThrows
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/javalab_db?user=postgres&password=12ER56ui78";
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement("insert into tag(title) values (?)");
        for (int i = 0; i < N; i++) {
            statement.setString(1, "title " + i);
            statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
    }
}
