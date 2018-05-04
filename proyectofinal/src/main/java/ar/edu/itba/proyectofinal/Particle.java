package ar.edu.itba.proyectofinal;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Particle {

    private int id;
    private double mass;
    private List<AngularPoint> points;
    private Point massCenter;
    private double orientation;
    private double radio;

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
                    double radio, double desiredVelocity, Point vel, double angularVelocity, double angularAcceleration, List<Point> targets) {
        this.id = id;
        this.mass = mass;
        this.points = points;
        this.massCenter = massCenter;
        this.orientation = orientation;
        this.radio = radio;
        this.desiredVelocity = desiredVelocity;
        this.vel = vel;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
        this.targets = targets;
    }

    //TODO
    public void getContactForce(List<Particle> particleList){
    }


    /**
     * Points must be provided from TOP to BOTTOM OR LEFT to RIGHT

     */
//    public Point drivingForce(double x1, double y1, double x2, double y2){
//
//        //TODO possible check to see if the parameters where given correctly, see what to do with this
////        if ((y1 == y2 && x1 > x2) || (x1 == x2 && y1 < y2 ){
////            return null;
////        }
//
//        double vxDir, vyDir, xF, yF;
//
//        //TODO SET TAU
//        double tau= 1;
//
//
//        double limit = r + s.getLength() / 2;
//        Point midPoint = s.getMiddlePoint();
//        double yTop = midPoint.getY() + limit;
//        double yBot = midPoint.getY() - limit;
//        double xLeft = midPoint.getX() - limit;
//        double xRight = midPoint.getX() + limit;
//
//        if(yTop < y1 && yBot > y2){
//            vyDir = 0;
//        }else if (yTop > y1) {
//            vyDir = (y1 - limit) - midPoint.getY();
//        }else {
//            vyDir = (y2 + limit) - midPoint.getY();
//        }
//
//        if (xLeft > x1 && xRight < x2) {
//            vxDir = 0;
//        }else if (xLeft < 1) {
//            vxDir = (x1 + limit) - midPoint.getX();
//        }else {
//            vxDir = (x2 - limit) - midPoint.getX();
//        }
//
//        xF = mass * (vxDir - vel.getX()) / tau;
//        yF = mass * (vyDir - vel.getY()) / tau;
//        return new Point(xF, yF);
//
//    }
//
//    public Segment getS() {
//        return s;
//    }
//
//    public void setS(Segment s) {
//        this.s = s;
//    }

    public double getRadio() {
        return radio;
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


}
