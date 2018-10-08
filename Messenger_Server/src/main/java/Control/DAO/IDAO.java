package Control.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public abstract class IDAO<T>  {
    Statement statement;
    PreparedStatement preparedStatement;
    Connection connection;
    ResultSet resultSet;

    public abstract Vector<T> selectAll();
    public abstract Vector<T> selectbyID(int ID);
    public abstract Vector<T> selectbyNumbers(int ID, int numbers);
    public abstract int insert(T object);
    public abstract int update(T object);
    public abstract void closeConnection();
}
