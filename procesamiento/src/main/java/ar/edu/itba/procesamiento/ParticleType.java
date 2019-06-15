package ar.edu.itba.procesamiento;

import java.util.ArrayList;
import java.util.List;

public class ParticleType {

    private List<Point> list;
    private double desiredVelocity;
    private Point massCentre;
    private List<AngularPoint> angularPoints;

    public ParticleType(List<Point> list, double desiredVelocity) {
        this.list = list;
        this.desiredVelocity = desiredVelocity;

        this.massCentre = Utils.massCenter((Point[])list.toArray(), Data.precision);
        this.angularPoints = Utils.calculateAngularPoints(massCentre,(Point[])list.toArray());
    }

    public List<Point> getList() {
        return list;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }
}
