package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.List;

public class Particle {
    /*
        TODO (Fixed: Requirement2) : Particle should have the orientation as an independant versor, and all other internal angles should be referrenced
        orientation should be described as a versor from the centre of mass to the desired direction.
        (BUT) center of mass is unkown at the start, so the provided vector could be taken to be from centre of mass to
        the first point in the list to start with.

        TODO : Orientation from a given geometry should be taken as the north of the provided poligon {Check at input}
        Closely related to first issue

        TODO ( NEXT ) : Allow particle to change direction versor directly to opposite direction if there is rotational simmetry
        Solution: Allign direction versor with 0 or PI of desired direction according to which is closerg

        TODO: r in crossProduct should not be a versor

        TODO (NEXT): Check that criterion for reaching a target is appropiate
        Partial Solution:
    */
    private int id;
    private double mass;
    private List<AngularPoint> points;

    private Point massCenter;
    private Point previousMassCenter;

    private double maxDistance;
    private double radius;

    //Orientation in degrees.
    private double orientation;

    //In response to Requirement2
    private Point facingDirection;

    private boolean wall = false;

    private Point force;
    private double torque;

    //Referring to desired movement speed to be obtained
    private double desiredVelocity;

    private Point vel;
    private Point acc ;

    private double angularVelocity;
    private double angularAcceleration;

    private double inertiaMoment;

    private List<Target> targets;
    private int indexTarget = 0;

    private double previousOrientation;

    private double time = 0.0;


    private boolean thisone = false;

    //Constructors
    public Particle(int id, double mass, List<AngularPoint> points, Point massCenter, double orientation,
                    double radius, double desiredVelocity, Point vel, double angularVelocity, double angularAcceleration,
                    List<Target> targets, double inertiaMoment) {
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
        this.force = new Point(0,0); //Used to initialize the previous position
        this.previousMassCenter = eulerPosition(-Data.dt);
        this.inertiaMoment = inertiaMoment;

        //TODO check if this works.
        this.previousOrientation = orientation;

        this.maxDistance = 0;
        for (AngularPoint ap: points){
            if (ap.getLength() > maxDistance){
                maxDistance=ap.getLength();
            }
        }
    }


    public void getForce(List<Particle> particles, double time) {
        this.time = time;
        resetForce();
        getDrivingForce();
        getContactForce(particles);
    }

    public void getContactForce(List<Particle> particleList){
        for (Particle p : particleList)
            if (!p.equals(this) && this.canCollide(p))
                    this.checkCollision(p);
    }

    public boolean canCollide(Particle p){
        return this.massCenter.squaredDistanceBetween(p.massCenter)
                <= (this.maxDistance + this.radius + p.maxDistance +p.radius) *
                    (this.maxDistance + this.radius + p.maxDistance + p.radius);
    }

    public void checkCollision(Particle p) {

        double closestDistance, minDistance = Double.MAX_VALUE;
        Point a = null, b = null, closestPoint = null;
        List<Segment> p1Segments, p2Segments;
        List<Point> p1Points, p2Points;
        p1Segments = this.getSegments();
        p2Segments = p.getSegments();
        p1Points = this.getPoints();
        p2Points = p.getPoints();

        /*
        Discard if cannot collide
         */



        /*For each segment in the current particle, find the closest point to each of the other particle's edges.
          If the distance to this edge is smaller, than the previosly recorded minimum distance to the other particle,
          save the closest point on the current particle's segment and the corresponding closest point
         */
        for (Segment segment : p1Segments){
            for (Point point : p2Points){
                closestPoint = Utils.completeClosestPoint(segment, point);
                /*TODO: Check if further restrictions could be verified, maybe to cut the for loops earlier
                   TODO: Would finding one point that collides be sufficient? Are we certain there's always gonna be only 1?
                */
                closestDistance = closestPoint.squaredDistanceBetween(point);
                if (closestDistance < minDistance){
                    minDistance = closestDistance;
//                    System.out.println(minDistance);
                    a = closestPoint;
                    b = point;
                    thisone = true;
//                    System.out.println("Segment :   " + segment.getP1().getX() +","+ segment.getP1().getY() + "\t\t\t\t"+
//                            segment.getP2().getX() +","+ segment.getP2().getY() );
//                    System.out.println("Point :     " + point.getX()+","+point.getY());
//                    System.out.println(a.getX() +","+a.getY() + "    " +b.getX() +","+b.getY() + " FIRSt\n");

                }
//                if (Double.compare(a.getX(),b.getX()) == 0 && Double.compare(a.getY(),b.getY()) == 0) {
//                    System.out.println("IGUALES2");
//                }
            }
        }

        /*Repeat process for other particle's segments
         */
        for (Segment segment : p2Segments){
            for (Point point : p1Points){
                closestPoint = Utils.completeClosestPoint(segment, point);
                closestDistance = closestPoint.squaredDistanceBetween(point);
                if (closestDistance < minDistance){
                    minDistance = closestDistance;
//                    System.out.println(minDistance);
                    b = closestPoint;
                    //todo remove
                    double c;
                    a = point;
                    thisone = false;
//                    System.out.println(a.getX() +","+a.getY() + "     " +b.getX() +","+b.getY());
//                    if (Double.compare(a.getX(),b.getX()) == 0 && Double.compare(a.getY(),b.getY()) == 0) {
//                        System.out.println("IGUALES2");
//                    }
                }
            }
        }

//        System.out.println("__");
//        System.out.println(a.getX() +","+a.getY() + "    " +b.getX() +","+b.getY());
//        System.out.println("-----------------------");
        /*If closest distance found between both particle's points is smaller than the addition of both's radiuses,
        calculate and apply collision force
         */
        if (minDistance < (this.getRadius() + p.getRadius()) * (this.getRadius() + p.getRadius())){
//            System.out.println(a.getX() +","+a.getY() + "    " +b.getX() +","+b.getY());
            this.applyCollisionForces(p, a, b);
        }
    }


    public void applyCollisionForces(Particle p, Point a, Point b){
        double overlapForce, overlap, versorModule, scalarProjection;
        Point r, f, translationForce;

        if (Double.compare(a.getX(),b.getX()) == 0 && Double.compare(a.getY(),b.getY()) == 0) {
//            System.out.println("IGUALES");
        }

        /* find overlap distance */
        overlap = Math.sqrt(a.squaredDistanceBetween(b)) - (this.getRadius() +  p.getRadius());
        overlapForce = forceFor(overlap);

//        System.out.println(overlapForce);
        /*r vector goes from centre of mass of this particle, to the contact point
          f vector is the direction in which the force is being applied, going from one contact point, to the other
         */
        r = new Point(a.getX() - this.massCenter.getX(), a.getY() - this.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());

//        f.times(1 / a.distanceBetween(b));
        f.times(1/f.module());
        f.times(overlapForce);

        this.force.add(f);

//        r.times(-1 / r.module());
//        scalarProjection = f.dotProduct(r);
//        r.times(scalarProjection);
//        translationForce = r;

        this.torque += r.crossProduct(f);

//        this.force.setX(this.force.getX() + translationForce.dotProduct(new Point(1,0)));
//        this.force.setY(this.force.getY() + translationForce.dotProduct(new Point(0,1)));

        tangentialForce(p, a, b, overlapForce);
    }

    public void tangentialForce(Particle p, Point a, Point b, double overlap){
        double tForce, relativeSpeedT;
        Point r,f,tangentForce;

        //Swapped this with p
        Point relativeVelocity = new Point(p.vel.getX() - this.vel.getX(), p.vel.getY() - this.vel.getY());

        r = new Point(a.getX() - this.massCenter.getX(), a.getY() - this.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());

        f.times(1 / f.module());

        //TODO Check this
//        p.torque += r.crossProduct(relativeVelocity.dotProduct());
//        tangentVersor = Utils.getPerpendicularTo2(f);
//        tangentVersor.times(1/tangentVersor.module());

        relativeSpeedT = relativeVelocity.dotProduct(Utils.getPerpendicularTo(f));

        tForce = - Data.kt * overlap * relativeSpeedT;   //* relativeVelocity.dotProduct(tangentVersor);
//        tangentVersor.times(tForce/tangentVersor.module());

        tangentForce = f;

        Utils.getPerpendicularTo(tangentForce).times(tForce);

//        tangentForce.times(tForce);

        this.torque += r.crossProduct(tangentForce);
//        scalarProjection = tangentForce.dotProduct(r);
//        r.times(scalarProjection);
//        translationForce = r;
//        translationForce.times(-1);
        this.force.add(tangentForce);

        if(this.force.getX() == Double.NaN)
            System.out.println("PROBLEM");

    }

    private void getDrivingForce() {
        //TODO: Extract target point from target segment
        Target target = targets.get(indexTarget);
        double desiredAngle = Utils.getAngle( massCenter, target);
//        double help = desiredAngle*-1;
//        help+=2*Math.PI;
        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;

        //TODO: R(t) should be : sinusoidal, uniform. It shouldn't be changed on every step.
        double drivingTorque = Data.SD * deltaAngle - Data.beta * angularVelocity + sinusoidalNoise(time) ;


        Point desiredDirection = new Point(target.getX() - massCenter.getX(),
                target.getY() - massCenter.getY());
        double abs = Math.sqrt(desiredDirection.getX() * desiredDirection.getX() +
                desiredDirection.getY() * desiredDirection.getY());

        desiredDirection.times(1 / abs);

        Point desiredVel = new Point (desiredVelocity * desiredDirection.getX(),
                desiredVelocity * desiredDirection.getY());

        Point drivingForce = new Point((desiredVel.getX() - vel.getX()) * mass / Data.characteristicT,
                (desiredVel.getY() - vel.getY()) * mass / Data.characteristicT);
        this.torque += drivingTorque;
        
        force.add(drivingForce);
    }

    public void positionParticle(Point massCenter, double orientation) {
        this.massCenter = massCenter;
        this.orientation = orientation;
        this.previousMassCenter = eulerPosition(-Data.dt);
    }

    /* Used to initiate the previous position */
    private Point eulerPosition(double delta) {
        double nextX = getMassCenter().getX() + delta * getVel().getX() + (delta * delta * getForce().getX()) / (2 * getMass());
        double nextY = getMassCenter().getY() + delta * getVel().getY() + (delta * delta * getForce().getY()) / (2 * getMass());
        return new Point(nextX, nextY);
    }

    private List<Segment> getSegments () {
        List<Segment> aux = new ArrayList<>();
        List<Point> points = getPoints();
        for (int i=1; i < points.size(); i++) {
            Segment s = new Segment(points.get(i - 1), points.get(i));
            aux.add(s);
        }
        //Connects the first and the last one
        aux.add(new Segment(points.get(0), points.get(points.size() - 1)));
        return aux;
    }

    private double forceFor (double overlap ) {
        //TODO faltaria la amortiguacion del resorte gama * v(rel)**n
        return - Data.kn * overlap;
    }

    List<Point> getPoints() {
        List<Point> cartesianPoints = new ArrayList<>();
        for (AngularPoint ap: points) {
            // Orientation should be taken into account
            double angle = orientation + ap.getAngle();
            if (angle >= Math.PI * 2){
                angle -= 2 * Math.PI;
            }
            double x = massCenter.getX() + ap.getLength() * Math.sin(angle);
            double y = massCenter.getY() + ap.getLength() * Math.cos(angle);
            cartesianPoints.add(new Point(x, y));
        }
        return cartesianPoints;
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

    public void setMassCenter(Point massCenter) {
        this.massCenter = massCenter;
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

    public void setVel(Point vel) {
        this.vel = vel;
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

    public List<Target> getTargets() {
        return targets;
    }

    public Target getCurrentTarget() { return this.targets.get(indexTarget); }

    private boolean reached(Target t) {
        return this.massCenter.squaredDistanceBetween(t)
                <= (this.maxDistance + this.radius) *
                (this.maxDistance + this.radius);

    }

    public boolean reachedTarget() { return this.reached(this.getCurrentTarget());}

    public Point getPreviousMassCenter() {
        return previousMassCenter;
    }

    public void nextTarget() {
        this.indexTarget += 1;
    }

    public void resetTargets() { this.indexTarget = 0;
    }

    public void resetPosition() { Populator.getInstance().positionParticle(this); }

    public void setPreviousMassCenter(Point previousMassCenter) {
        this.previousMassCenter = previousMassCenter;
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

    public boolean onWall(Particle p){
        List<Point> points = p.getPoints();
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        if (p1.getX()==p2.getX()){
            double xDif = this.massCenter.getX() - p1.getX();
            if(xDif*xDif<= (p.radius+this.radius)*(p.radius +this.radius)){
                return true;
            }
        }else {
            double yDif = this.massCenter.getY() - p1.getY();
            if(yDif*yDif<= (p.radius+this.radius)*(p.radius +this.radius)){
                return true;
            }
        }

        return false;
    }

    private void resetForce() {
        force.setX(0.0);
        force.setY(0.0);
        torque = 0.0;
    }


    public double getPreviousOrientation() {  return previousOrientation; }

    public void setPreviousOrientation(double previousOrientation){
        this.previousOrientation = previousOrientation;
    }

    public void setOrientation(double orientation){
        this.orientation = orientation;
    }

    public void setAngularVelocity(double angularVelocity){
        this.angularVelocity = angularVelocity;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall() {
        this.wall = true;
    }


    //todo which is the required amplitude
    public double sinusoidalNoise(double t){
        return Math.random();
    }

}
