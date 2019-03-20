package ar.edu.itba.proyectofinal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulation entitities modeled as  a list of Angular Points given in relation from a centre of mass and
 * a given orientation.
 */
public class Particle {
    /*
        TODO ( NEXT ) : Allow particle to change direction versor directly to opposite direction if there is rotational simmetry
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

    private boolean wall = false;

    private Point force;
    private double torque;

    //Referring to desired movement speed to be obtained
    private double desiredVelocity;

    private Point vel;

    private double angularVelocity;
    private double angularAcceleration;

    private double inertiaMoment;

    private List<Target> targets;
    private int indexTarget = 0;

    private double previousOrientation;

    private double time = 0.0;


    //For sinusoidal noise
    private double phase;

    private int R = 0;
    private int G = 0;
    private int B = 255;

    //Constructor
    public Particle(int id, double mass, List<AngularPoint> points, Point massCenter, double orientation,
                    double radius, double desiredVelocity, Point vel, double angularVelocity, double angularAcceleration,
                    double inertiaMoment, double phase) {
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
        this.force = new Point(0,0); //Used to initialize the previous position
        this.previousMassCenter = eulerPosition(-Data.dt);
        this.inertiaMoment = inertiaMoment;

        this.phase = phase;

        this.previousOrientation = orientation;

        this.maxDistance = 0;
        for (AngularPoint ap: points){
            if (ap.getLength() > maxDistance){
                maxDistance=ap.getLength();
            }
        }
    }

    /**
     *
     * @param particles
     * @param time: for updating current time and noise calculation.
     */
    public void getForce(List<Particle> particles, double time) {
        this.time = time;
        resetForce();
        getDrivingForce();
        getContactForce(particles);
    }

    private void getDrivingForce() {
        Target target = targets.get(indexTarget);
        double desiredAngle = Utils.getAngle( massCenter, target);

        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;
        List<Point> points = this.getPoints();

        double drivingTorque =  Data.SD * deltaAngle - Data.beta * angularVelocity + sinusoidalNoise(time) ;
//        System.out.println("Orientation : " + this.orientation);
//        System.out.println("Desired orientation" + desiredAngle);
//        System.out.println("--------------");

        Point desiredDirection = new Point(target.getX() - massCenter.getX(),
                target.getY() - massCenter.getY());
        double abs = Math.sqrt(desiredDirection.getX() * desiredDirection.getX() +
                desiredDirection.getY() * desiredDirection.getY());
//
        desiredDirection.times(1 / abs);
//
        Point desiredVel = new Point (desiredVelocity * desiredDirection.getX(),
                desiredVelocity * desiredDirection.getY());
//
        Point drivingForce = new Point((desiredVel.getX() - vel.getX()) * mass / Data.characteristicT,
                (desiredVel.getY() - vel.getY()) * mass / Data.characteristicT);
        this.torque += drivingTorque;
//        System.out.println(this.torque);
        force.add(drivingForce);
    }

    public void getContactForce(List<Particle> particleList){
        for (Particle p : particleList)
            if (!p.equals(this) && this.canCollide(p))
                    this.checkCollision(p);


    }

    /**
     * Shallow aproximation for calculating if both particles could collide.
     * Used for preventing unnecessary operations
     * @param p
     * @return
     */
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
                    a = closestPoint;
                    b = point;

                }
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
                    b = closestPoint;
                    a = point;
                }
            }
        }

        /*If closest distance found between both particle's points is smaller than the addition of both's radius,
        calculate and apply collision force
         */
        if (minDistance < (this.getRadius() + p.getRadius()) * (this.getRadius() + p.getRadius()))
            this.applyCollisionForces(p, a, b);
    }


    public void applyCollisionForces(Particle p, Point a, Point b){
        double overlapForce, overlap;
        Point r, f;

        /* find overlap distance */
        overlap = Math.sqrt(a.squaredDistanceBetween(b)) - (this.getRadius() +  p.getRadius());
        overlapForce = forceFor(overlap);

        /*r vector goes from centre of mass of this particle, to the contac t point
          f vector is the direction in which the force is being applied, going from one contact point, to the other
         */
        r = new Point(a.getX() - this.massCenter.getX(), a.getY() - this.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());

        f.times(1/f.module());
        f.times(overlapForce);

        this.force.add(f);

        //Dampening force
//        this.force.ad

        this.torque -= r.crossProduct(f);

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

        relativeSpeedT = relativeVelocity.dotProduct(Utils.getPerpendicularTo(f));

        //CARE
//        tForce = - Data.kt * overlap * relativeSpeedT - relativeVelocity.dotProduct(tangentVersor);
        tForce = - Data.kt * overlap * relativeSpeedT;   //* relativeVelocity.dotProduct(tangentVersor);

        tangentForce = f;

        Utils.getPerpendicularTo(tangentForce).times(tForce);

        this.torque += r.crossProduct(tangentForce);

        this.force.add(tangentForce);

    }

    public double dampeningTangentialForce(Particle p, Point a, Point b, double overlap){
        Point relativeVelocity = new Point(p.vel.getX() - this.vel.getX(), p.vel.getY() - this.vel.getY());
        Point f = new Point(a.getX() - b.getX(), a.getY() - b.getY());
        f.times(1/f.module());
        double relativeSpeed = relativeVelocity.dotProduct(Utils.getPerpendicularTo(f));
//        Point relativeSpeedT =
        return 0;
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

    /**
     *
     * @return list of segments conforming the particle
     */
    public List<Segment> getSegments () {
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

    /**
     * Provides a way to retrieve cartesian points from the list of Angular Points, mass center and orientation. Needed
     * for easier calculation
     * @return list of cartesian points
     */
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

    /**
     * Calls for a reinserting of the given particle into the system after reaching final target.
     */
    public void resetPosition() {
        Populator.getInstance().resetParticle(this);
    }

    /**
     * Sets previous mass center for integrating purposes
     * @param previousMassCenter
     */
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

    /**
     * Finds if a given particle is colliding onto a wall
     * @param p: wall with which to check collision
     * @return
     */
    public boolean onWall(Particle p){
        //Retrieve walls position
        List<Point> points = p.getPoints();
        Point p1 = points.get(0);
        Point p2 = points.get(1);

        //Check wall's orientation
        if (p1.getX()==p2.getX()){
            //Check for overlapping
            double xDif = this.massCenter.getX() - p1.getX();
            if(xDif*xDif<= (p.radius+this.radius)*(p.radius +this.radius)){
                return true;
            }
        }else {
            //Check for overlapping
            double yDif = this.massCenter.getY() - p1.getY();
            if(yDif*yDif<= (p.radius+this.radius)*(p.radius +this.radius)){
                return true;
            }
        }
        return false;
    }

    /**
     * removes any forces on the particle in order to restart calculation
     */
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

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    public void setColor (int R, int G, int B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    /**
     * Calculates noise based on a sinusoidal function for the given time
     * @param t: time for noise calculation
     * @return
     */
    public double sinusoidalNoise(double t){
        return Data.eta * Data.mMax * this.maxDistance * Data.grav * Math.sin(t * (Math.PI * 2) + phase);
    }

    public double getOrientationX() {
        return Data.angleLength * Math.sin(orientation);
    }

    public double getOrientationY() {
        return Data.angleLength * Math.cos(orientation);
    }
}
