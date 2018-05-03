
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Particle {

    private int id;
    private double mass;
    private List<AngularPoint> points;
    private Point mc;
    private double orientation;
    private double r;

    private Point force;
    private double torque;

    private Point desiredVelocity;

    private Point vel;
    private Point acc ;

    private double angularVelocity;
    private double angularAcceleration;

    private double inertiaMoment;

    private List<Point> targets = new LinkedList<>();


    public Particle(int id, double mass, double x1, double y1, double x2, double y2, double r, double velx, double vely){
        this.id = id;
        this.mass = mass;
        this.r = r;
        this.vel = new Point(velx, vely);
        this.acc = new Point(0, 0);
        this.force = new Point(0, 0);
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

    public double getR() {
        return r;
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
        double desiredAngle = Utils.getAngle( mc, target);
        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;
        double drivingTorque = - Data.SD * deltaAngle - Data.beta ; //TODO: FALTAN COSAS
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
