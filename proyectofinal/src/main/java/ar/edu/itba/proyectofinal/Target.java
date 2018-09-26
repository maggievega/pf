package ar.edu.itba.proyectofinal;

public class Target extends Point {

    private Segment s;
    private boolean end = false;
    private double interval;

    public int R = 0;
    public int G = 0;
    public int B = 0;

    public Target(double x, double y, double interval, boolean end) {
        super(x, y);
        this.end = end;
        if (end) setFinal(); else setMiddle();
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

    public void place(Point mc) {
        if (this.s == null)
            this.s = new Segment(getX() - interval, getY(), getX() + interval, getY());
        Point closest = Utils.completeClosestPoint(s, mc);
        this.setPoint(closest);
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

    public void setMiddle() {
        R = 255;
        G = 255;
    }

    public void setFinal() {
        G = 255;
    }
}
