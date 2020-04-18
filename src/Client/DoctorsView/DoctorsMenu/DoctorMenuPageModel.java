package Client.DoctorsView.DoctorsMenu;

import Client.Login.LoginModel;
import Client.ManagerView.ManagerMenuPageModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class DoctorMenuPageModel {
    private LoginModel loginModel;
    private static DoctorMenuPageModel doctorMenuPageModel;

    private DoctorMenuPageModel() throws ClassNotFoundException, SQLException {
        loginModel = LoginModel.getLoginModel();
    }

    static DoctorMenuPageModel getDoctorMenuPageModel() throws ClassNotFoundException, SQLException {
        if(doctorMenuPageModel == null)
            doctorMenuPageModel = new DoctorMenuPageModel();
        return doctorMenuPageModel;
    }

    ResultSet getDoctorDetails(String id) throws SQLException {
        String sql = "SELECT id,email,name FROM doctors WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }

    ResultSet getPatients(String id) throws SQLException {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        String sql = "SELECT * FROM Patients WHERE doctor = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }


    ResultSet getWarning(String id) throws SQLException {
        String sql = "SELECT warnings FROM doctors WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }

    void endWarnings(String id) throws SQLException {
        String sql = "UPDATE doctors SET warnings = CONCAT(warnings, 'end\n') WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        preparedStatement.execute();
    }

    void updateWarnings(String id, String warnings) throws SQLException {
        String sql = "UPDATE doctors SET warnings = ? WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, warnings);
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    void deletePatient(String id) throws SQLException {
        ManagerMenuPageModel.deletePatient(id, loginModel);
    }

    String getPatientName(String id) throws SQLException {
        String sql = "SELECT name FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    public String getPatientPhone(String id) throws SQLException {
        String sql = "SELECT phone FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }
}
