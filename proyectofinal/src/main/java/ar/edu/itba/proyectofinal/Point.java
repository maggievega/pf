package ar.edu.itba.proyectofinal;

import java.util.Objects;

public class Point {

    private double x;
    private double y;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void times(double scalar) {this.x *= scalar; this.y *= scalar; }

    public void add(Point p) { this.x += p.getX(); this.y += p.getY(); }

    public void substract(Point p) { this.x -= p.getX(); this.y -= p.getY(); }

    public void divide(Point p) {
        if (p.getX() == 0 || p.getY() == 0) {
            System.out.println("Division by 0");
            throw new NumberFormatException("Division by 0");
        }
        this.x /= p.getX();
        this.y /= p.getY();
    }

    public double squaredDistanceBetween (Point p) {
        return (this.getX() - p.getX()) * (this.getX() - p.getX()) + (this.getY() - p.getY()) * (this.getY() - p.getY());
    }

    public double distanceBetween (Point p) {
        return Math.sqrt(squaredDistanceBetween(p));
    }

    public double dotProduct(Point p) {
        return this.getX() * p.getX() + this.getY() * p.getY();
    }

    public double crossProduct(Point p) {
        return this.getX() * p.getY() - this.getY() * p.getX();
    }

    public double module() { return Math.sqrt(getX() * getX() + getY() * getY());}

    @Override
    public String toString() {
        return "( " + x + " , " + y + " )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
