package ar.edu.itba.procesamiento;

import java.util.List;

public class Particle {
    private int id;
    private int type;
    private Point massCenter;
    private Point velocity;
    private double radius;
    private double angularVelocity;
    private double phase;
    private double orientation;
    private double mass;
    private double target;
    private List<AngularPoint> ap;

    public Particle(int id, int type, Point massCenter, Point velocity, double radius, double angularVelocity, double phase, double orientation, double mass, double target) {
        this.id = id;
        this.type = type;
        this.massCenter = massCenter;
        this.velocity = velocity;
        this.radius = radius;
        this.angularVelocity = angularVelocity;
        this.phase = phase;
        this.orientation = orientation;
        this.mass = mass;
        this.target = target;
    }

    public Particle(int id, int type, double mass, List<AngularPoint> points, Point massCenter, double orientation,
                    double radius, double angularVelocity, double phase) {
        this.id = id;
        this.type = type;
        this.mass = mass;
        this.ap = points;
        this.massCenter = massCenter;
        this.orientation = orientation;
        this.radius = radius;
        this.angularVelocity = angularVelocity;
        this.mass = mass;

        this.phase = phase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Point getMassCenter() {
        return massCenter;
    }

    public void setMassCenter(Point massCenter) {
        this.massCenter = massCenter;
    }

    public Point getVelocity() {
        return velocity;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getTarget() { return target; }

    public void setTarget(double target) { this.target = target; }
}
