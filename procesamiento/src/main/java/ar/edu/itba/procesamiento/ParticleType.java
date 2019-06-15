package ar.edu.itba.procesamiento;

import java.util.List;

public class ParticleType {

    private List<Particle> list;
    private double desiredVelocity;

    public ParticleType(List<Particle> list, double desiredVelocity) {
        this.list = list;
        this.desiredVelocity = desiredVelocity;
    }

    public List<Particle> getList() {
        return list;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }
}
