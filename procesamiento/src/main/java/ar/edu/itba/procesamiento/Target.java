package ar.edu.itba.procesamiento;

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
        this.interval = interval;
        this.s = new Segment(getX() - interval, getY(),getX() + interval, getY());
        if (end) setFinal(); else setMiddle();
    }

    public Target (double x, double y, Point mc, double interval, boolean end) {
        super(x, y);
        this.end = end;
        this.interval = interval;
        this.s = new Segment(getX() - interval, getY(),getX() + interval, getY());
        if (end) setFinal(); else setMiddle();
        place(mc);
    }

    public boolean isEnd() {
        return end;
    }

    public void place(Point mc) {
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

    public Segment getS() {
        return s;
    }

    public double getInterval() {
        return interval;
    }



    public void setFinal() {
        G = 255;
    }
}