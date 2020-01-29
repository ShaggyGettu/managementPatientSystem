package Client.DataTypes;

public class Value {
    private Point point;
    private String date;
    private double doubleNumber;
    private int intNumber;

    Value(String string, Point point){
        doubleNumber = -1;
        date = string;
        this.point = point;
    }

    public Value(String string, double d){
        date = string;
        doubleNumber = d;
    }

    Value(String string, int number){
        doubleNumber = -1;
        date = string;
        intNumber = number;
    }

    public Point getPoint() {
        return point;
    }

    public String getDate() {
        return date;
    }

    public double getDoubleNumber() {
        return doubleNumber;
    }

    public int getIntNumber() {
        return intNumber;
    }

    @Override
    public String toString() {
        String string = date + " ";
        if (point !=null)
            return string + point;
        if (doubleNumber != -1)
            return string + doubleNumber;
        return string + intNumber;
    }
}
