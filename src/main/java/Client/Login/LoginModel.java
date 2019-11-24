package Client.Login;

import Server.Database.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    private Connection connection;
    private static LoginModel loginModel;

    public static LoginModel getLoginModel() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (loginModel == null)
            loginModel = new LoginModel();
        return loginModel;
    }
    private LoginModel() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.connection = dbConnection.getConnect();
        if (connection == null)
            System.exit(2);
    }

    public boolean connect() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        connection = dbConnection.getConnect();
        if (connection == null)
            return false;
        return true;
    }

    public boolean isConnected(){
        return connection !=null;
    }

    String  login(String email, String password, String role) throws SQLException {
        PreparedStatement pr;
        ResultSet rs;
        String sql = "SELECT * FROM " + role.toLowerCase() + "s WHERE email = ? and password = ?";
        pr = connection.prepareStatement(sql);
        pr.setString(1, email);
        pr.setString(2, password);
        rs = pr.executeQuery();
        if (rs.next()){
            String string = rs.getString("id");
            pr.close();
            rs.close();
            return string;
        }
        pr.close();
        rs.close();
        return null;
    }

    public ResultSet Users() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel.connect();
        ResultSet resultSet;
        String sql = "SELECT id FROM patients";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public Connection getConnection() {
        return connection;
    }
}
