package Client.DataTypes;

import javafx.scene.control.Button;

public class Person {
    private  String email;
    private String name;
    private String id;
    private String phone;
    private Button show;
    private Button delete;

    Person(String email, String name, String id){
        this.email = email;
        this.name = name;
        this.id = id;
        phone = "";
        show = new Button("show");
        delete = new Button("delete");
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
