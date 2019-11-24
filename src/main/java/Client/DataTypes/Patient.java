package Client.DataTypes;

import javafx.scene.control.Button;

public class Patient {
    private  String email;
    private String name;
    private int id;
    private String phone;
    private String doctor;
    private Button show;
    private Button delete;
    private String background;
    private String tests[];

    public Patient(String id, String email, String phone, String doctor, String name) {
        this.id = Integer.valueOf(id);
        this.email = email;
        this.phone = phone;
        this.doctor = doctor;
        this.name = name;
        show = new Button("show");
        delete = new Button("delete");
        background = "";
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Button getShow() {
        return show;
    }

    public void setShow(Button show) {
        this.show = show;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
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
}
