package ar.edu.itba.procesamiento;

import java.util.List;
import java.util.Map;

public class Processor {

    List<Particle> walls;
    Map<Integer, ParticleType> mapType;
    Input input;

    List<Particle> particles;

    public Processor(List<Particle> walls, Map<Integer, ParticleType> mapType, Input input) {
        this.walls = walls;
        this.mapType = mapType;
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
        snap.getList().forEach( p -> updateParticle(p));
    }

    public void updateParticle(Particle p){
        //TODO check if previous speed, position and forces is relevant
        Particle part = particles.get(p.getId());
        part.setMassCenter(p.getMassCenter());
        part.setOrientation(p.getOrientation());
        part.setVelocity(p.getVelocity());
        part.setAngularVelocity(p.getAngularVelocity());
        part.setPhase(p.getPhase());
        part.setTarget(p.getTarget());
    }

    public void calculateData(List<Particle> particles){
        
    }


}
