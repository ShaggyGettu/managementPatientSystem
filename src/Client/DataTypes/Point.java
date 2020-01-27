package Client.DataTypes;

import java.util.Comparator;

public class Point {
    private double dx;
    private double dy;
    private int ix;
    private int iy;
    private int state;

    public Point(double x, double y){
        this.dx = x;
        this.dy = y;
    }

    public Point(int dx, double dy){
        state = 2;
        this.ix = dx;
        this.dy = dy;
    }

    public Point(int x, int y){
        state = 3;
        this.dx = -1;
        this.dy = -1;
        this.ix = x;
        this.iy = y;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getIx() {
        return ix;
    }

    public int getIy() {
        return iy;
    }

    @Override
    public String toString() {
        if (state == 3)
            return "(" + ix + "," + iy + ")";
        if (state == 1)
            return "(" + dx + "," + dy + ")";
        else
            return "(" + ix + "," + dy + ")";
    }
}
