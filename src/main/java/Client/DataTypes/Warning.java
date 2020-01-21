package Client.DataTypes;

import javafx.scene.control.Button;

public class Warning {

    private String id;
    private String test;
    private String value;
    private String date;
    private Button delete;

    public Warning(String id, String test, String value, String date){
        this.id = id;
        this.test = test;
        this.value = value;
        this.date = date;
        delete = new Button("delete");
    }

    public Button getDelete(){
        return delete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

}
