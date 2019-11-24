package Client.DoctorsView.DoctorsMenu;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorMenuPageModel {
    private LoginModel loginModel;
    private static DoctorMenuPageModel doctorMenuPageModel;

    private DoctorMenuPageModel() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
    }

    public static DoctorMenuPageModel getDoctorMenuPageModel() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if(doctorMenuPageModel == null)
            doctorMenuPageModel = new DoctorMenuPageModel();
        return doctorMenuPageModel;
    }

    public ResultSet getPatients(String id) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel.connect();
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        String sql = "SELECT * FROM Patients WHERE doctor = ?";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1,id);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }


}
