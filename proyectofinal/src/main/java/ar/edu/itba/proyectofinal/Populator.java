package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Populator {

    /**
     * particles are all loaded in particles, the only thing left to do is position them and their orientation
     *
     * @param particles the loaded particles
     */

    private List<Particle> particles = null;
    private static Populator instance = null;

    private Populator() {
    }

    static Populator getInstance() {
        if (instance == null) {
            instance = new Populator();
        }
        return instance;
    }

    void setParticles(List<Particle> particles) {
        if (this.particles == null)
            this.particles = particles;
    }

    public void Populate() {
        int i= 0;
        for (Particle p : particles) {
            positionParticle(p);
            populateTargets(p);
        }
    }

    void PopulatePaper(Point grid){
        int part=0;
        double xDiv = Data.maxX / grid.getX();
        double yDiv = Data.maxY / grid.getY();
        for (int i = 0; i < grid.getX(); i++) {
            for (int j = 0; j < grid.getY(); j++) {
                Particle p;
                do {
                    p = particles.get(part++);
                }while (p.isWall());
                paperPosition(p,new Point(i * xDiv + xDiv / 2, j * yDiv + yDiv / 2));
                populateTargets(p);
            }
        }
    }

    private void paperPosition(Particle p, Point position){
        if(!p.isWall()) {
            double orientation = Math.random() * 2 * Math.PI;
            p.setOrientation(orientation);
            p.setPreviousOrientation(orientation);
            p.setMassCenter(position);
            p.setPreviousMassCenter(position);
            p.positionParticle(position,orientation);
        }
    }

    private void populateTargets(Particle p) {
        List<Target> targets = new ArrayList<>();
        for (Target t: Data.targetList) {
            Target aux = new Target(t.getX(), t.getY(), p.getMassCenter(), t.getInterval(), t.isEnd());
            targets.add(aux);
        }
        p.setTargets(targets);
    }

    private void positionParticle(Particle p) {
        Point mc;
        if (!p.isWall()) {
            double orientation = Math.random() * 2 * Math.PI;
            p.setOrientation(orientation);
            p.setPreviousOrientation(orientation);
            do {
                mc = generateMassCenter(p);
                p.setMassCenter(mc);
                p.setPreviousMassCenter(mc);
            } while (!isValid(p));

            p.positionParticle(mc, orientation);
        }

    }

     void resetParticle(Particle p){
        p.setOrientation(Math.PI);
        p.setPreviousOrientation(Math.PI);
        double x = Math.random() * (Data.maxX - 1 ) + 0.3;
        Point mc = new Point(x,7.8);
        p.setMassCenter(mc);
        p.setPreviousMassCenter(mc);
        p.positionParticle(mc,Math.PI);
    }

    private boolean isValid(Particle p) {
        for (Particle p2 : this.particles) {
            if (!p.equals(p2) && !p2.isWall() && p.canCollide(p2))
                return false;
            if (p2.isWall() && p.onWall(p2))
                return false;
        }
        return true;
    }

    private static Point generateMassCenter(Particle p) {
        double maxX = Data.maxX - p.getMaxDistance() - p.getRadius();
        double minX = Data.minX + p.getMaxDistance() + p.getRadius();
        double maxY = Data.maxY - p.getMaxDistance() - p.getRadius();
        double minY = Data.minY + p.getMaxDistance() + p.getRadius();

        double x = (Math.random() * (maxX - minX)) + minX;
        double y = (Math.random() * (maxY - minY)) + minY;
        return new Point(x, y);
    }



}
