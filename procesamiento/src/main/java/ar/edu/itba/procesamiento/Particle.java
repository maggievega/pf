package ar.edu.itba.procesamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private int indexTarget;
    private boolean wall = false;
    private List<AngularPoint> ap;
    private double maxDistance;
    private double time;
    private Point force = new Point(0,0);
    private double desiredVelocity;
    private double torque = 0.0;
    private List<Target> targets;

    public Particle(int id, int type, Point massCenter, Point velocity, double radius, double angularVelocity, double phase, double orientation, double mass, int target) {
        this.id = id;
        this.type = type;
        this.massCenter = massCenter;
        this.velocity = velocity;
        this.radius = radius;
        this.angularVelocity = angularVelocity;
        this.phase = phase;
        this.orientation = orientation;
        this.mass = mass;
        this.indexTarget = target;
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
        this.indexTarget = 0;
        this.phase = phase;
    }

    public void updateByType(Map<Integer, ParticleType> particleTypeMap){
        this.ap = particleTypeMap.get(this.type).getAngularPoints();
        this.maxDistance = particleTypeMap.get(this.type).getMaxDistance();
        this.desiredVelocity = particleTypeMap.get(this.type).getDesiredVelocity();
    }

    public boolean isWall() { return wall; }


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

    public int getTarget() { return indexTarget; }

    public void setTarget(int target) { this.indexTarget = target; }

    public void setWall(boolean value) { this.wall = value; }

    public void setTargets(List<Target> targets) { this.targets = targets; }

    public void getForce(List<Particle> particles, double currentTime) {
        this.time = currentTime;
        getDrivingForce();
        getContactForce(particles);
    }

    private void getDrivingForce() {
        Target target = targets.get(indexTarget);
        double desiredAngle = Utils.getAngle( massCenter, target);

        double aux = desiredAngle - orientation;
        double deltaAngle = aux <= Math.PI ? aux : aux - 2 * Math.PI;

        double drivingTorque =  Data.SD * deltaAngle - Data.beta * angularVelocity + sinusoidalNoise(time) ;

        Point desiredDirection = new Point(target.getX() - massCenter.getX(),
                target.getY() - massCenter.getY());
        double abs = Math.sqrt(desiredDirection.getX() * desiredDirection.getX() +
                desiredDirection.getY() * desiredDirection.getY());

        desiredDirection.times(1 / abs);

        Point desiredVel = new Point (desiredVelocity * desiredDirection.getX(),
                desiredVelocity * desiredDirection.getY());

        Point drivingForce = new Point((desiredVel.getX() - velocity.getX()) * mass / Data.characteristicT,
                (desiredVel.getY() - velocity.getY()) * mass / Data.characteristicT);
        this.torque += drivingTorque;
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
        Point relativeVelocity = new Point(p.velocity.getX() - this.velocity.getX(), p.velocity.getY() - this.velocity.getY());

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

    private double forceFor (double overlap ) {
        //TODO faltaria la amortiguacion del resorte gama * v(rel)**n
        return - Data.kn * overlap;
    }

    public double sinusoidalNoise(double t){
        return Data.eta * Data.mMax * this.maxDistance * Data.grav * Math.sin(t * (Math.PI * 2) + phase);
    }

    /**
     * Provides a way to retrieve cartesian points from the list of Angular Points, mass center and orientation.
     * Needed for easier calculation
     * @return list of cartesian points
     */
    List<Point> getPoints() {
        List<Point> cartesianPoints = new ArrayList<>();
        for (AngularPoint aa: ap) {
            // Orientation should be taken into account
            double angle = orientation + aa.getAngle();
            if (angle >= Math.PI * 2){
                angle -= 2 * Math.PI;
            }
            double x = massCenter.getX() + aa.getLength() * Math.sin(angle);
            double y = massCenter.getY() + aa.getLength() * Math.cos(angle);
            cartesianPoints.add(new Point(x, y));
        }
        return cartesianPoints;
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

}
