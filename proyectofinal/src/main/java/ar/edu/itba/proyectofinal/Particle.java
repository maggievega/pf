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
        this.force = new Point(0,0); //Used to initialize the previous position
        this.previousMassCenter = eulerPosition(-Data.dt);
    }

    public Particle(int id, double mass, List<AngularPoint> points,
                   double desiredVelocity, Point vel, double angularVelocity, double angularAcceleration, List<Point> targets) {
        this.id = id;
        this.mass = mass;
        this.points = points;
        this.desiredVelocity = desiredVelocity;
        this.vel = vel;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
        this.targets = targets;
        this.force = new Point(0,0); //Used to initialize the previous position
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
        //TODO: Find a proper way to do this
        if (this.wall) { return; }

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
        /*Repeat process for other particle's segments
         */
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
        /*If closest distance found between both particle's points is smaller than the addition of both's radiuses,
        calculate and apply collision force
         */
        if (minDistance < (this.getRadius() + p.getRadius()) * (this.getRadius() + p.getRadius())){
            this.applyCollisionForces(p, a, b);
        }
    }

    public void applyCollisionForces(Particle p, Point a, Point b){
        double overlapForce, overlap, versorModule, scalarProjection;
        Point r, f, translationForce;

        /* find overlapment */
        overlap = Math.sqrt(a.squaredDistanceBetween(b)) - (this.getRadius() +  p.getRadius());
        overlapForce = forceFor(overlap);

        /*r vector goes from centre of mass of this particle, to the contact point
          f vector is the direction in which the force is being applied, going from one contact point, to the other
         */
        r = new Point(a.getX() - this.massCenter.getX(), a.getY() - p.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());
        versorModule = f.module();
        f.times(overlapForce/versorModule);
        this.torque += r.crossProduct(f);
        // TODO: Double check this
        r.times(-1 / r.module());
        scalarProjection = f.dotProduct(r);
        r.times(scalarProjection);
        translationForce = r;
        //TODO: Check!
        this.force.setX(this.force.getX() + translationForce.dotProduct(new Point(1,0)));
        this.force.setY(this.force.getY() + translationForce.dotProduct(new Point(0,1)));

        tangentialForce(p, a, b, overlapForce);
    }
    
    public void tangentialForce(Particle p, Point a, Point b, double overlap){
        double rModule, fModule, tForce, scalarProjection;
        Point r,f,tangentForce,tangentVersor, translationForce;
        Point relativeVelocity = new Point(this.vel.getX() - p.vel.getX(), this.vel.getY() - p.vel.getY());
        r = new Point(a.getX() - this.massCenter.getX(), a.getY() - p.massCenter.getY());
        f = new Point(a.getX() - b.getX(), a.getY() - b.getY());
        fModule = f.module();
        rModule = r.module();
        f.times(fModule);
        r.times(rModule);

        //TODO Check this
        //p.torque += r.crossProduct(relativeVelocity.dotProduct());
        tangentVersor = Utils.getPerpendicularTo(f);
        tForce = - Data.kt * overlap * relativeVelocity.dotProduct(tangentVersor);
        tangentForce =  tangentVersor;
        tangentForce.times(tForce);

        this.torque += r.crossProduct(tangentForce);
        scalarProjection = tangentForce.dotProduct(r);
        r.times(scalarProjection);
        translationForce = r;
        this.force.setX(this.force.getX() + translationForce.dotProduct(new Point(1,0)));
        this.force.setY(this.force.getY() + translationForce.dotProduct(new Point(0,1)));

    }

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

    double forceFor (double overlap ) {
        return - Data.kn * overlap;
    }

    public List<Point> getPoints() {
        List<Point> cartesianPoints = new ArrayList<>();
        for (AngularPoint ap: points) {
            // Orientation should be taken into account
            double angle = orientation + ap.getAngle();
            if (angle >= Math.PI * 2){
                angle -= 2 * Math.PI;
            }
            double x = massCenter.getX() + ap.getLength() * Math.cos(angle);
            double y = massCenter.getY() + ap.getLength() * Math.sin(angle);
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

    public List<Point> getTargets() {
        return targets;
    }

    public Point getPreviousMassCenter() {
        return previousMassCenter;
    }

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

    public void getDrivingForce() {

        //TODO: Extract target point from target segment
        Point target = targets.get(0);
        double desiredAngle = Utils.getAngle( massCenter, target);
        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;

        //TODO: R(t) should be : sinusoidal, uniform. It shouldn't be changed on every step.
        double drivingTorque = - Data.SD * deltaAngle - Data.beta * angularVelocity + Data.Rt ;


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
