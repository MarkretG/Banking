package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static Connection connection=null;
    public static Connection getConnection() throws PersistenceException {
        int errorCode = 202;
        if (connection != null)
        {
            return connection;
        }

        try {
            String url = "jdbc:mysql://localhost:3306/info";
            String userName = "root";
            String password = "Root@123";

            Class.forName("com.mysql.cj.jdbc.Driver"); // load the Driver Class

            connection = DriverManager.getConnection(url, userName, password);// create the connection now
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new PersistenceException("Exception in get connection",e, errorCode);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new PersistenceException("Exception in get connection",e,errorCode);
        }

      return connection;
    }
    public static void closeConnection()
    {
        try {
            connection.close();
        }
        catch (Exception e){}
    }

}