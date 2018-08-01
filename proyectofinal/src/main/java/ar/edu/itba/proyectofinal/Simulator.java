package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulator {

    private static List<Particle> particles;

    public Simulator(List<Particle> p) {
        particles = p;
    }

    public void Simulate(){
        Output o = new Output();
        double time = 0.0;
        int printCont = 0;
        while (time < Data.totalTime) {
            if (Data.printTime * printCont <= time) {
                o.printSystem(particles, time);
                printCont++;
            }
            /* Previous positions */
            List<Particle> previousPositions  = new ArrayList<>(particles);

            //Calculate forces
            particles.forEach((p) -> p.getForce(previousPositions));

            for (Particle p : particles) {
                updatePosition(p);
                // Ask if it reached the target and remove it ?
                // And if it doesn't have more targets remove it from particles ?
            }

            time += Data.dt;
        }
        o.done();
    }


    /* Uses Verlet */
    public void updatePosition(Particle p) {
        double nextX = 2 * p.getMassCenter().getX() - p.getPreviousMassCenter().getX() + Data.dt * Data.dt * p.getForce().getX() / p.getMass() ;
        double nextY = 2 * p.getMassCenter().getY() - p.getPreviousMassCenter().getY() + Data.dt * Data.dt * p.getForce().getY() / p.getMass() ;

        double nextVelX = (nextX - p.getPreviousMassCenter().getX()) / (2 * Data.dt);
        double nextVelY = (nextY - p.getPreviousMassCenter().getY()) / (2 * Data.dt);

        p.setPreviousMassCenter(p.getMassCenter());
        p.setMassCenter(new Point(nextX, nextY));
        p.setVel(new Point(nextVelX, nextVelY));
    }


}
