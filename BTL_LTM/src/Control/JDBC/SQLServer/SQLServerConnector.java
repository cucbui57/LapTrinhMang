package Control.JDBC.SQLServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServerConnector { // Kết nối đến cơ sở dữ liệu
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String host_name, int port, String instance, String database_name, String user_name, String password) throws SQLException {
        String url = "jdbc:sqlserver://" + host_name + ":" + port + ";instance=" + instance + ";databaseName=" + database_name;
        Connection connection = DriverManager.getConnection(url, user_name, password);
        return connection;
    }
}
