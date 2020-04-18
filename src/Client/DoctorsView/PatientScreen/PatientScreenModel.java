package Client.DoctorsView.PatientScreen;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientScreenModel {
    private static PatientScreenModel patientScreenModel;
    private LoginModel loginModel;

    private PatientScreenModel() throws ClassNotFoundException, SQLException {
        loginModel = LoginModel.getLoginModel();
    }

    public static PatientScreenModel getInstance() throws ClassNotFoundException, SQLException {
        if(patientScreenModel == null)
            patientScreenModel = new PatientScreenModel();
        return patientScreenModel;
    }

    String[] findPatient(String id) throws SQLException {
        String sql = "SELECT diseaes, tests FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String details[] = new String[2];
        details[0] = resultSet.getString(1);
        details[1] = resultSet.getString(2);
        return details;
    }
}
