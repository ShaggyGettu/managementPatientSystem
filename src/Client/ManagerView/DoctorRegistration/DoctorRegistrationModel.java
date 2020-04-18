package Client.ManagerView.DoctorRegistration;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class DoctorRegistrationModel {
    private LoginModel loginModel;
    private static DoctorRegistrationModel model;

    private DoctorRegistrationModel() throws SQLException, ClassNotFoundException {
        loginModel = LoginModel.getLoginModel();
    }

    static DoctorRegistrationModel getModel() throws SQLException, ClassNotFoundException {
        if (model == null)
            model = new DoctorRegistrationModel();
        return model;
    }

    void addDoctor(String id, String email, String password, String name) throws SQLException {
        String sql = "INSERT INTO doctors (id, email, name, password) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, password);
        preparedStatement.execute();
    }

    boolean idExists(String id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            preparedStatement.close();
            resultSet.close();
            return true;
        }
        preparedStatement.close();
        resultSet.close();
        return false;
    }

    boolean emailExists(String email) throws SQLException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql = "SELECT * FROM doctors WHERE email = ?";
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
}
