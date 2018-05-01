package ar.edu.itba.proyectofinal;

public class Segment {

    private Point p1;
    private Point p2;
    private double length;

    public Segment(double x1, double y1, double x2, double y2){
        this.p1 = new Point(x1, y1);
        this.p2 = new Point(x2, y2);
        this.length = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getMiddlePoint(){
        return new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
    }

    public double getLength() {
        return length;
    }
}
