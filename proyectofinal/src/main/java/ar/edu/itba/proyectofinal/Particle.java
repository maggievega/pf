package ar.edu.itba.proyectofinal;

import java.util.LinkedList;
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

    private List<Point> targets = new LinkedList<>();

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
                if (canCollide(this,p)) {
                    checkCollision(this,p);
                }
            }
        }
    }

    public void checkCollision(Particle p1, Particle p2) {
        double closestDistance, minDistance = Double.MAX_VALUE;
        Point a= null,b= null, closestPoint = null;
        List<Segment> p1Segments, p2Segments;
        List<Point> p1Points, p2Points;
        p1Segments = p1.getSegments();
        p2Segments = p2.getSegments();
        p1Points = p1.getPoints();
        p2Points = p2.getPoints();

        for (Segment segment : p1Segments){
            for (Point point : p2Points){
                closestPoint = Utils.completeClosestPoint(segment,point);
                /*TODO: Check if further restrictions could be verified, maybe to cut the for loops earliear
                   TODO: Would finding one point that collides be sufficient? Are we certain there's always gonna be only 1?
                */
                closestDistance =Utils.squaredDistanceBetween(closestPoint,point);
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
                closestDistance = Utils.squaredDistanceBetween(closestPoint, point);
                if (closestDistance < minDistance){
                    minDistance = closestDistance;
                    a = closestPoint;
                    b = point;
                }
            }
        }

        if ((minDistance < (p1.getRadius() + p2.getRadius())*(p1.getRadius() + p2.getRadius()) )){
            applyCollisionForces(p1,p2,a,b);
        }
    }

    public void applyCollisionForces(Particle p1, Particle p2, Point a, Point b){
        double force, overlap, versorModule, scalarProjection;
        Point r, f, translationForce;
        overlap = Math.sqrt(Utils.squaredDistanceBetween(a,b)) - (p1.getRadius() +  p2.getRadius());
        //TODO FIND HOW FORCE WAS CALCULATED
        force = overlap * 15512;
        r = new Point(a.getX() - p1.massCenter.getX(), a.getY() - p1.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());
        versorModule = Utils.module(f);
        f.times(force/versorModule);
        p1.torque += Utils.crossProduct(r,f);
        /* TODO: Double check this
         Si hago el producto escalar de la fuerza contra el versor de rm deberia obtener la proyeccion que es paralela
            https://en.wikipedia.org/wiki/Vector_projection
        */
        r.times(-1/Utils.module(r));
        scalarProjection = Utils.dotProduct(f,r);
        r.times(scalarProjection);
        translationForce = r;
        //TODO: Check!
        p1.force.setX(p1.force.getX() + Utils.dotProduct(translationForce,new Point(1,0)));
        p1.force.setY(p1.force.getY() + Utils.dotProduct(translationForce,new Point(0,1)));
    }

    //TODO Complete method
    public List<Segment> getSegments () {

        return null;
    }

    //TODO Complete method
    public List<Point> getPoints() {
        return null;
    }

    public double getRadius() {
        return radius;
    }

    public int getId() {
        return id;
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
        //TODO: Extract target point from target segment
        Point target = targets.get(0);
        double desiredAngle = Utils.getAngle( massCenter, target);
        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;
        double drivingTorque = - Data.SD * deltaAngle - Data.beta * angularVelocity + Data.Rt ; //TODO: FALTAN COSAS
        Point desiredDirection = new Point(target.getX()-massCenter.getX(),target.getY()-massCenter.getY());
        double abs = Math.sqrt(desiredDirection.getX()*desiredDirection.getX()+desiredDirection.getY()*desiredDirection.getY());
        desiredDirection.setX(desiredDirection.getX()/abs);
        desiredDirection.setY(desiredDirection.getY()/abs);
        Point desiredVel = new Point (desiredVelocity*desiredDirection.getX()/abs, desiredVelocity*desiredDirection.getY()/abs);
        //TODO check for characteristicT
        Point drivingForce = new Point((desiredVel.getX()-vel.getX())*mass/Data.characteristicT, (desiredVel.getY()-vel.getY())*mass/Data.characteristicT);
        torque += drivingTorque;
        force.setX(force.getX()+drivingForce.getX());
        force.setY(force.getY()+drivingForce.getY());
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

    private boolean canCollide(Particle p1, Particle p2){
        return Utils.squaredDistanceBetween(p1.massCenter,p2.massCenter)
                <= (p1.maxDistance+p2.maxDistance) * (p1.maxDistance+p2.maxDistance) ? true : false;
    }


}
