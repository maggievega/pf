package ar.edu.itba.proyectofinal;

import java.util.Objects;

public class Segment {

    private Point p1;
    private Point p2;
    private double length;



    public Segment(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        this.length = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public Point getP1() {
        return p1;
    }


    public Point getP2() {
        return p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(segment.length, length) == 0 &&
                Objects.equals(p1, segment.p1) &&
                Objects.equals(p2, segment.p2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(p1, p2, length);
    }

    @Override
    public String toString() {
        return "Segment{" +
                "p1 = " + p1.toString() +
                ", p2 = " + p2.toString() +
                ", length = " + length +
                '}';
    }

    public double getLength () {
        double x = p1.getX() - p2.getX();
        double y = p1.getY() - p2.getY();
        return Math.sqrt(x*x+ y*y);
    }

    public Point middlePoint() {
        return new Point((p1.getX()+p2.getX())/2, (p1.getY()+p2.getY())/2);
    }

}
