package ar.edu.itba.procesamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Processor {

    List<Particle> walls;
    List<Target> targets;
    Map<Integer, ParticleType> mapType;
    Input input;

    List<Particle> particles;
    double currentTime = 0;

    public Processor(List<Particle> walls, Map<Integer, ParticleType> mapType, List<Target> targets, Input input) {
        this.walls = walls;
        this.mapType = mapType;
        this.targets = targets;
        this.input = input;
    }

    public void process(){
        Snapshot snap;
        while((snap = input.getSnapshot()) != null){
            nextStep(snap);
            calculateData(particles);
            //output;
        }
    }

    public void nextStep(Snapshot snap){
        this.currentTime = snap.getTime();
        snap.getList().forEach( p -> updateParticle(p));
        particles = new ArrayList<Particle>();
        particles.addAll(walls);
        particles.addAll(snap.getList());
    }

    public void updateParticle(Particle p){
        p.setTargets(targets);
        p.updateByType(mapType);
    }


    public void calculateData(List<Particle> particles){
        List<Particle> previousPositions = new ArrayList<>(particles);
        particles.parallelStream().forEach(p->{
            if (!p.isWall()) {
                p.getForce(previousPositions, currentTime);
            }});
    }

    public void outputProcessedData(){
        List <Particle> a = new ArrayList<>(particles);
    }


}
