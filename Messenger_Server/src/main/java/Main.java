import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String URL = "jdbc:sqlserver://localhost:1433;databaseName=Messenger;user=underwear;password=mE39Dola4o";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();
            String string = "select username from users where username = " + "'sadasd'";
            ResultSet resultSet = statement.executeQuery(string);
            if(resultSet.next()){
                System.out.println("dsda");
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
