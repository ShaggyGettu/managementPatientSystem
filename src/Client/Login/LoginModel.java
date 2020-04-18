package Client.Login;

import java.sql.*;

public class LoginModel {
    private Connection connection;
    private static LoginModel loginModel;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String SQCONN = "jdbc:mysql://localhost/hospital";

    public static LoginModel getLoginModel() throws ClassNotFoundException, SQLException {
        if (loginModel == null)
            loginModel = new LoginModel();
        return loginModel;
    }
    private LoginModel() throws ClassNotFoundException, SQLException {
        this.connection = getConnect();
        if (connection == null)
            System.exit(2);
    }

    boolean isConnected(){
        return connection !=null;
    }

    public ResultSet  login(String email, String password, String role) throws SQLException {
        PreparedStatement pr;
        ResultSet rs;
        String sql = "SELECT * FROM " + role.toLowerCase() + "s WHERE email = ? and password = ?";
        pr = connection.prepareStatement(sql);
        pr.setString(1, email);
        pr.setString(2, password);
        rs = pr.executeQuery();
        if (rs.next()){
            return rs;
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getUser(String id) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql = "SELECT * FROM patients WHERE id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            return resultSet;
        return null;
    }
    private static Connection getConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(SQCONN, USERNAME, PASSWORD);
    }
}
