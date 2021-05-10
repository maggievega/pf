package ar.edu.itba.procesamiento;

import java.util.ArrayList;
import java.util.List;

public class ParticleType {

    private List<Point> list;
    private double desiredVelocity;
    private Point massCentre;
    private List<AngularPoint> angularPoints;
    private double maxDistance;

    public ParticleType(List<Point> list, double desiredVelocity) {
        this.list = list;
        this.desiredVelocity = desiredVelocity;

        this.massCentre = Utils.massCenter((Point[])list.toArray(), Data.precision);
        this.angularPoints = Utils.calculateAngularPoints(massCentre,(Point[])list.toArray());

        this.maxDistance = Double.MIN_VALUE;
        for (AngularPoint angularPoint : angularPoints){
            if(angularPoint.getLength() > this.maxDistance){
                this.maxDistance = angularPoint.getLength();
            }
        }
    }

    public List<Point> getList() { return list; }

    public double getDesiredVelocity() { return desiredVelocity; }

    public List<AngularPoint> getAngularPoints() { return angularPoints; }

    public double getMaxDistance() { return maxDistance; }
}
