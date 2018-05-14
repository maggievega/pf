package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.List;

public class Particle {

    private int id;
    private double mass;
    private List<AngularPoint> points;
    private Point massCenter;
    private double maxDistance;
    private double orientation;
    private double radius;

    private Point force;
    private double torque;

    //Referring to desired movement speed to be obtained
    private double desiredVelocity;

    private Point vel;
    private Point acc ;

    private double angularVelocity;
    private double angularAcceleration;

    private double inertiaMoment;

    private List<Point> targets;

    public Particle(int id, double mass, List<AngularPoint> points, Point massCenter, double orientation,
                    double radius, double desiredVelocity, Point vel, double angularVelocity, double angularAcceleration, List<Point> targets) {
        this.id = id;
        this.mass = mass;
        this.points = points;
        this.massCenter = massCenter;
        this.orientation = orientation;
        this.radius = radius;
        this.desiredVelocity = desiredVelocity;
        this.vel = vel;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
        this.targets = targets;
    }

    //TODO
    public void getContactForce(List<Particle> particleList){
        for (Particle p : particleList) {
            if (this.id != p.id) {
                if (this.canCollide(p)) {
                    this.checkCollision(p);
                }
            }
        }
    }

    public void checkCollision(Particle p) {
        double closestDistance, minDistance = Double.MAX_VALUE;
        Point a= null, b= null, closestPoint = null;
        List<Segment> p1Segments, p2Segments;
        List<Point> p1Points, p2Points;
        p1Segments = this.getSegments();
        p2Segments = p.getSegments();
        p1Points = this.getPoints();
        p2Points = p.getPoints();

        for (Segment segment : p1Segments){
            for (Point point : p2Points){
                closestPoint = Utils.completeClosestPoint(segment, point);
                /*TODO: Check if further restrictions could be verified, maybe to cut the for loops earliear
                   TODO: Would finding one point that collides be sufficient? Are we certain there's always gonna be only 1?
                */
                closestDistance = closestPoint.squaredDistanceBetween(point);
                if (closestDistance < minDistance){
                    minDistance = closestDistance;
                    a = closestPoint;
                    b = point;
                }
            }
        }
        for (Segment segment : p2Segments){
            for (Point point : p1Points){
                closestPoint = Utils.completeClosestPoint(segment, point);
                closestDistance = closestPoint.squaredDistanceBetween(point);
                if (closestDistance < minDistance){
                    minDistance = closestDistance;
                    a = closestPoint;
                    b = point;
                }
            }
        }

        if (minDistance < (this.getRadius() + p.getRadius()) * (this.getRadius() + p.getRadius())){
            this.applyCollisionForces(p, a, b);
        }
    }

    public void applyCollisionForces(Particle p, Point a, Point b){
        double force, overlap, versorModule, scalarProjection;
        Point r, f, translationForce;
        overlap = Math.sqrt(a.squaredDistanceBetween(b)) - (this.getRadius() +  p.getRadius());
        //TODO FIND HOW FORCE WAS CALCULATED
        force = overlap * 15512;
        r = new Point(a.getX() - this.massCenter.getX(), a.getY() - p.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());
        versorModule = f.module();
        f.times(force/versorModule);
        p.torque += r.crossProduct(f);
        /* TODO: Double check this
         Si hago el producto escalar de la fuerza contra el versor de rm deberia obtener la proyeccion que es paralela
            https://en.wikipedia.org/wiki/Vector_projection
        */
        r.times(-1/r.module());
        scalarProjection = f.dotProduct(r);
        r.times(scalarProjection);
        translationForce = r;
        //TODO: Check!
        this.force.setX(this.force.getX() + translationForce.dotProduct(new Point(1,0)));
        this.force.setY(this.force.getY() + translationForce.dotProduct(new Point(0,1)));
    }

    public List<Segment> getSegments () {
        List<Segment> aux = new ArrayList<>();
        List<Point> points = getPoints();
        for (int i=1; i < points.size(); i++) {
            Segment s = new Segment(points.get(i-1), points.get(i));
            aux.add(s);
        }
        //Connects the first and the last one
        aux.add(new Segment(points.get(0), points.get(points.size()-1)));
        return aux;
    }

    //TODO Complete method
    public List<Point> getPoints() {
        List<Point> aux = new ArrayList<>();
        for (AngularPoint ap: points) {
            double x = massCenter.getX() + ap.getLength() * Math.cos(ap.getAngle());
            double y = massCenter.getY() + ap.getLength() * Math.sin(ap.getAngle());
            aux.add(new Point(x, y));
        }
        return aux;
    }

    public double getRadius() {
        return radius;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
    }

    public Point getMassCenter() {
        return massCenter;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public double getOrientation() {
        return orientation;
    }

    public Point getForce() {
        return force;
    }

    public double getTorque() {
        return torque;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }

    public Point getVel() {
        return vel;
    }

    public Point getAcc() {
        return acc;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public double getInertiaMoment() {
        return inertiaMoment;
    }

    public List<Point> getTargets() {
        return targets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void getDrivingForce() {
        Point target = targets.get(0); //TODO: Extract target point from target segment
        double desiredAngle = Utils.getAngle( massCenter, target);
        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;
        double drivingTorque = - Data.SD * deltaAngle - Data.beta * angularVelocity + Data.Rt ; //TODO: FALTAN COSAS

        Point desiredDirection = new Point(target.getX() - massCenter.getX(),
                target.getY() - massCenter.getY());
        double abs = Math.sqrt(desiredDirection.getX() * desiredDirection.getX() +
                desiredDirection.getY() * desiredDirection.getY());
        desiredDirection.setX(desiredDirection.getX() / abs);
        desiredDirection.setY(desiredDirection.getY() / abs);
        Point desiredVel = new Point (desiredVelocity * desiredDirection.getX() / abs,
                desiredVelocity * desiredDirection.getY() / abs);
        //TODO check for characteristicT
        Point drivingForce = new Point((desiredVel.getX() - vel.getX()) * mass / Data.characteristicT,
                (desiredVel.getY() - vel.getY()) * mass / Data.characteristicT);
        torque += drivingTorque;
        force.setX(force.getX() + drivingForce.getX());
        force.setY(force.getY() + drivingForce.getY());
    }

    public Point getForce(List<Particle> particles) {
        resetForce();
        getDrivingForce(); //calcula driving torque
        getContactForce(particles); // calcula contact torque
        return force;
    }

    private void resetForce() {
        force.setX(0.0);
        force.setY(0.0);
        torque = 0.0;
    }

    private boolean canCollide(Particle p){
        return this.massCenter.squaredDistanceBetween(p.massCenter)
                <= (this.maxDistance + p.maxDistance) * (this.maxDistance + p.maxDistance);
    }

}
