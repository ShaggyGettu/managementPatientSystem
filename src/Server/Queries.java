package Server;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Queries {
    private LoginModel loginModel;

    Queries() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
    }

    ResultSet getAllPatients() throws SQLException {
        String sql = "SELECT id FROM patients";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    ResultSet getPatientDataTable(String string) throws SQLException {
        String sql = "SELECT * FROM s" + string;
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }
}