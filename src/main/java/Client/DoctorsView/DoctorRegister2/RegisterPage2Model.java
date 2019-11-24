package Client.DoctorsView.DoctorRegister2;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterPage2Model {
    private LoginModel loginModel;
    private static RegisterPage2Model registerPage2Model;

    private RegisterPage2Model() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel = LoginModel.getLoginModel();
    }

    public static RegisterPage2Model getRegisterPage2Model() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if(registerPage2Model == null)
            registerPage2Model = new RegisterPage2Model();
        return registerPage2Model;
    }


    public void addPatient(String id, String email, String password, String phone, String doctor, String name, String background, String tests) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        loginModel.connect();
        PreparedStatement preparedStatement;
        String sql = "Insert into patients (id, email, password, phone, doctor, name, diseaes, tests) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        preparedStatement = loginModel.getConnection().prepareStatement(sql);
        preparedStatement.setString(1 ,id);
        preparedStatement.setString(2 ,email);
        preparedStatement.setString(3 ,password);
        preparedStatement.setString(4 ,phone);
        preparedStatement.setString(5 ,doctor);
        preparedStatement.setString(6 ,name);
        preparedStatement.setString(7 ,background);
        preparedStatement.setString(8, tests);
        preparedStatement.execute();

    }
}
