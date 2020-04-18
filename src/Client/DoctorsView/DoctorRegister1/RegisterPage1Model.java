package Client.DoctorsView.DoctorRegister1;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RegisterPage1Model {
    private LoginModel loginModel;
    private static RegisterPage1Model registerPage1Model;

    private RegisterPage1Model() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
    }

    static RegisterPage1Model getRegisterPage1Model() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (registerPage1Model == null)
            registerPage1Model = new RegisterPage1Model();
        return registerPage1Model;
    }

    boolean emailExists(String email) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql = "SELECT * FROM patients WHERE email = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            return true;
        }
        preparedStatement.close();
        resultSet.close();
        return false;
    }

    boolean idExists(String id) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql = "SELECT * FROM patients WHERE id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            return true;
        }
        preparedStatement.close();
        resultSet.close();
        return false;
    }
}
