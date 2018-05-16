package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private static List<Particle> particles;

    public Simulator(List<Particle> p) {
        particles = p;
    }

    public void Simulate(){
        double time = 0.0;
        int printCont = 0;
        while (time < Data.totalTime) {
            if (Data.printTime * printCont <= time) {
                Output.printSystem(particles, time);
                printCont++;
            }
            /* Previous positions */
            List<Particle> aux  = new ArrayList<>();
            aux.addAll(particles);

            for (Particle p : particles) {
                updatePosition(p, aux);
                // Ask if it reached the target and remove it ?
                // And if it doesn't have more targets remove it from particles ?
            }

            time += Data.dt;
        }
    }

    /* Uses Verlet */
    private void updatePosition(Particle p, List<Particle> particles) {
        // double nextX = getX + delta * xSpeed + (delta * delta * force / mass
    }

    private void Verlet() {

    }


    /*
        Particle next = new Particle(1, mass);

        double nextX = p.getX() + delta * p.getXSpeed() + (delta*delta*getForce(p) / p.getMass());
        next.setX(nextX);

        double predXSpeed = eulerVel(p, delta);

        next.setXSpeed(predXSpeed);

        double nextSpeedX = p.getXSpeed() + (delta / (2 * p.getMass())) * (getForce(p) + getForce(next)) ;

        p.setOldXPos(p.getX());
        p.setOldXAcc(p.getXAcc());

        p.setX(next.getX());
        p.setXSpeed(nextSpeedX);
        p.setXAcc(getAcel(p))
     */

}
