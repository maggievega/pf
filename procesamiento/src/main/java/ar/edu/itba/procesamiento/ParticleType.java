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

//        this.massCentre = massCenter();
    }

    public List<Point> getList() {
        return list;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }



    /**
     * Changes the list of cardinal points to angular points with respect to the massCenter
     * @param massCenter
     * @param points
     * @return
     */
    public static List<AngularPoint> calculateAngularPoints(Point massCenter, Point[] points) {
        List<AngularPoint> ap = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            double angle = Utils.getAngle(massCenter, points[i]);
            double length = massCenter.distanceBetween(points[i]);
            ap.add(new AngularPoint(angle, length));
        }
        return ap;
    }
}
