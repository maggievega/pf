package ar.edu.itba.proyectofinal;

public class Target extends Point {

    private boolean end = false;

    public Target(double x, double y, boolean end) {
        super(x, y);
        this.end = end;

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
