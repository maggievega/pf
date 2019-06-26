package ar.edu.itba.proyectofinal;

public class Target {

    private Segment s;
    private boolean end;
    private Point c;

    public int R = 0;
    public int G = 0;
    public int B = 0;

    public Target(boolean end, Segment segment) {
        this.s = segment;
        this.end = end;
        if (end) setFinal(); else setMiddle();
    }

    public Target(Segment segment, boolean end, Point massCenter) {
        this.s = segment;
        this.end = end;
        if (end) setFinal(); else setMiddle();
        place(massCenter);
    }

    public boolean isEnd() {
        return end;
    }

    public void place(Point mc) {
        Point closest = Utils.completeClosestPoint(s, mc);
        this.c = closest;
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

    public double getX() { return this.c.getX(); }

    public double getY() { return this.c.getY(); }

    public Segment getS() {
        return s;
    }

    public void setFinal() {
        G = 255;
    }

    public Segment getSegment() {
        return s;
    }

    public Point getClosest() {
        return this.c;
    }

    public void setClosest(Point closestTarget) {
        this.c = closestTarget;
    }
}
