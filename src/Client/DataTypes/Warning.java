package Client.DataTypes;

import javafx.scene.control.Button;

public class Warning {

    private String id;
    private String test;
    private String value;
    private String date;
    private Button delete;
    private int warningId;
    private static int count = 1;

    public Warning(String id, String test, String value, String date){
        this.id = id;
        this.test = test;
        this.value = value;
        this.date = date;
        delete = new Button("delete");
        warningId = count;
        count++;
    }

    public static void setCount() {
        count = 1;
    }

    public int getWarningId() {
        return warningId;
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

    @Override
    public String toString() {
        return id + " " + test + " " + value + " " + date + "\n";
    }
}
