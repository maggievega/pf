package ar.edu.itba.proyectofinal;

public class Target extends Point {

    private Segment s;
    private boolean end = false;

    public int R = 0;
    public int G = 0;
    public int B = 0;

    public Target(double x, double y, double interval, boolean end) {
        super(x, y);
        this.end = end;
        if (end) setMiddle(); else setFinal();
        s = new Segment(x - interval, y, x + interval, y);
    }

    public Target(double x, double y) {
        super(x,y);
    }

    public Target(Point p) {
        super(p.getX(), p.getY());
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Segment getS() {
        return s;
    }

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }

    public void setFinal() {
        R = 255;
        G = 255;
    }

    public void setMiddle() {
        G = 255;
    }
}
