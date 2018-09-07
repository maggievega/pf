package ar.edu.itba.proyectofinal;

import java.util.List;

public class Populator {

    /**
     * particles are all loaded in particles, the only thing left to do is position them and their orientation
     * @param particles the loaded particles
     */
    public static void Populate(List<Particle> particles){
        for (Particle p: particles) {
            Point mc;
            if (!p.isWall()) {
                double orientation = Math.random() * 2 * Math.PI;
                p.setOrientation(orientation);
                do {
                    mc = generateMassCenter(p);
                    p.setMassCenter(mc);
                } while (!isValid(p, particles));

                p.positionParticle(mc, orientation);

                //TODO: TARGETS HAVE TO BE ORGANIZED BASED ON THE DISTANCE TO THE PARTICLE
            }
        }
    }

    private static boolean isValid(Particle p,  List<Particle> particles){
        for (Particle p2: particles)
            if (!p.equals(p2) && !p2.isWall() && p.canCollide(p2))
                return false;
        return true;
    }

    private static Point generateMassCenter(Particle p) {
        double maxX = Data.maxX - p.getMaxDistance();
        double minX = Data.minX + p.getMaxDistance();
        double maxY = Data.maxY - p.getMaxDistance();
        double minY = Data.minY + p.getMaxDistance();

        double x = (Math.random() * (maxX - minX)) + minX;
        double y = (Math.random() * (maxY - minY)) + minY;
        return new Point(x, y);
    }


}
