package ar.edu.itba.procesamiento;

import java.util.List;

public class ParticleType {

    private List<Point> list;
    private double desiredVelocity;

    public ParticleType(List<Point> list, double desiredVelocity) {
        this.list = list;
        this.desiredVelocity = desiredVelocity;
    }

    public List<Point> getList() {
        return list;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }
}
