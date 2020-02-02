package Client.DataTypes;

import java.util.Comparator;

public class Point extends Object {
    private double dx;
    private double dy;
    private int ix;
    private int iy;
    private int state;

    public Point(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public Point(int ix, double dy){
        state = 2;
        this.ix = ix;
        this.dy = dy;
    }

    public Point(int ix, int iy){
        state = 3;
        this.dx = -1;
        this.dy = -1;
        this.ix = ix;
        this.iy = iy;
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
