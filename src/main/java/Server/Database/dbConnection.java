package Server.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONN = "jdbc:mysql://localhost/patients";
    private static final String SQCONN = "jdbc:mysql://localhost/hospital";

    public static Connection getConnect() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        return DriverManager.getConnection(SQCONN, USERNAME, PASSWORD);
    }
}
