package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private Connection connection;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "db_test_tiket";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static MySQLConnection instance;

    public static MySQLConnection getInstance() {
        return MySQLConnection.MySQLConnectionHolder.INSTANCE;
    }

    private static class MySQLConnectionHolder {

        private static final MySQLConnection INSTANCE = new MySQLConnection();
    }

    public Connection getConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Koneksi Gagal Karena : " + ex.getMessage());
        }
        return connection;
    }

}
