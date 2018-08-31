package ar.edu.itba.proyectofinal;

import java.util.List;

public class Populator {

    /**
     * particles are all loaded in particles, the only thing left to do is position them and their orientation
     * @param particles the loaded particles
     */
    public static void Populate(List<Particle> particles){
        for (Particle p: particles) {
            if (!p.isWall()) {
                Point mc = generateMassCenter(p, particles);
                double orientation = 0;

                p.setMassCenter(mc);
                p.setOrientation(orientation);
                //TODO Consultar si es requerido que las particulas puedan aparecer con cualquier orientacion
                while (!isValid(p, particles)) {
                    //TODO: Change this so that particles is the walls
                    mc = generateMassCenter(p, particles);
                    //TODO: Random between 0 and 2pi
                    orientation = 0;
                    p.setMassCenter(mc);
                    p.setOrientation(orientation);

                }
                //TODO: TARGETS HAVE TO BE ORGANIZED BASED ON THE DISTANCE TO THE PARTICLE
            }
        }
    }

    private static boolean isValid(Particle p,  List<Particle> particles){
        for (Particle p2: particles) {
            if (!p2.isWall() && !p.equals(p2) && p.canCollide(p2)) {
                return false;
            }
        }
        return true;
    }

    private static Point generateMassCenter(Particle p, List<Particle> particles) {
        Point mc = new Point(0,0);
        //TODO: HAVE SOME WAY OF KNOWING THE AREA TO START THERE
        for (Particle p2: particles) {
//            if(p.isWall() && p.isInitial() && !p.equals(p2)) {
//
//            }

        }
        return mc;
    }

}
