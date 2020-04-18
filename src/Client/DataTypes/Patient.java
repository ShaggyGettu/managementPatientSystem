package Client.DataTypes;

import Client.Login.LoginModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient extends Person{

    private String doctor;
    private String background;
    private String tests[];
    private String doctorName;

    public Patient(String id, String email, String phone, String doctor, String name) throws SQLException, ClassNotFoundException {
        super(email,name,id);
        setPhone(phone);
        this.doctor = doctor;
        background = "";
        String sql = "SELECT name FROM doctors WHERE id = ?";
        PreparedStatement preparedStatement = LoginModel.getLoginModel().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, doctor);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        doctorName = resultSet.getString(1);
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getBackground() {
        return background;
    }

    public String[] getTests() {
        return tests;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setTests(String tests[]) {
        this.tests = tests;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}

