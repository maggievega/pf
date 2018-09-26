package ar.edu.itba.proyectofinal;

public class Target extends Point {

    private Segment s;
    private boolean end = false;

    public Target(double x, double y, double interval, boolean end) {
        super(x, y);
        this.end = end;
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
}
