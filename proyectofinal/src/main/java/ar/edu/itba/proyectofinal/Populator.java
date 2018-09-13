package ar.edu.itba.proyectofinal;

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

    public static Populator getInstance() {
        if(instance == null) {
            instance = new Populator();
        }
        return instance;
    }

    public void setParticles(List<Particle> particles) {
        if(this.particles == null)
            this.particles = particles;
    }

    public void Populate() {
        for (Particle p : particles) {
                positionParticle(p);
                //TODO: TARGETS HAVE TO BE ORGANIZED BASED ON THE DISTANCE TO THE PARTICLE CHECK!
                Collections.sort(p.getTargets(),
                        (o1, o2) -> (int) ((int) o1.distanceBetween(p.getMassCenter()) - o2.distanceBetween(p.getMassCenter())));

        }
    }

    public void positionParticle(Particle p) {
        Point mc;
        if (!p.isWall()) {
            double orientation = Math.random() * 2 * Math.PI;
            p.setOrientation(orientation);
            p.setPreviousOrientation(orientation);
            do {
                mc = generateMassCenter(p);
                p.setMassCenter(mc);
            } while (!isValid(p));

            p.positionParticle(mc, orientation);
        }

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
