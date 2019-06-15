package ar.edu.itba.procesamiento;

import java.util.List;

public class Snapshot {
    private double time;
    private List<Particle> list;

    public Snapshot() {

    };

    public Snapshot(double time, List<Particle> list) {
        this.time = time;
        this.list = list;
    }

    public double getTime() {
        return time;
    }

    public List<Particle> getList() {
        return list;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setList(List<Particle> list) {
        this.list = list;
    }
}
