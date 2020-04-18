package Client.ManagerView;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerMenuPageModel {
    private LoginModel loginModel;
    private static ManagerMenuPageModel menuPageModel;

    private ManagerMenuPageModel() throws SQLException, ClassNotFoundException {
        loginModel = LoginModel.getLoginModel();
    }

    static ManagerMenuPageModel getModel() throws SQLException, ClassNotFoundException {
        if (menuPageModel == null)
            menuPageModel = new ManagerMenuPageModel();
        return menuPageModel;
    }

    ResultSet getDoctors() throws SQLException {
        String sql = "SELECT * FROM doctors";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    void deleteDoctor(String id) throws SQLException {
        String sql = "SELECT  id FROM patients WHERE  doctor = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getString("id"));
            sql = "DROP TABLE s" + resultSet.getString("id");
            preparedStatement = loginModel.getConnection().prepareStatement(sql);
            preparedStatement.execute();
        }
        sql = "DELETE FROM patients WHERE doctor = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        preparedStatement.execute();
        sql = "DELETE From doctors Where id = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        preparedStatement.execute();
    }

    public ResultSet getPatients() throws SQLException {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        String sql = "SELECT * FROM patients";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    void deletePatient(String id) throws SQLException {
        deletePatient(id, loginModel);
    }

    public static void deletePatient(String id, LoginModel loginModel) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1 ,id);
        preparedStatement.execute();
        sql = "DROP TABLE s" + id;
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.execute();
    }

    String[] managerDetails(String id) throws SQLException {
        String sql = "SELECT email, name FROM managers WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String string[] = new String[2];
        string[0] = resultSet.getString("email");
        string[1] = resultSet.getString("name");
        return string;
    }
}
