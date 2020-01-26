package Client.DataTypes;

public class Point {
    private double dx;
    private double dy;
    private int ix;
    private int iy;

    public Point(double x, double y){
        this.ix = -1;
        this.iy = -1;
        this.dx = x;
        this.dy = y;
    }

    public Point(int x, int y){
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
        if (dx == -1)
            return "(" + ix + "," + iy + ")";
        else
            return "(" + dx + "," + dy + ")";
    }
}
