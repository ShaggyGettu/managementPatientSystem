package Client.DataTypes;

public class Value{
    private Point point;
    private String date;
    private double doubleNumber;
    private int intNumber;


    public Value(Point point, int number){
        doubleNumber = -1;
        intNumber = number;
        this.point = point;
    }

    public Value(String string, double d){
        intNumber = -1;
        date = string;
        doubleNumber = d;
    }

    public Value(String string, int number){
        doubleNumber = -1;
        date = string;
        intNumber = number;
    }

    public Value(String string, Point point1){
        doubleNumber = -1;
        date = string;
        point = point1;
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
