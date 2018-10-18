package ar.edu.itba.proyectofinal;


import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private static List<Particle> particles;
    private static Output o;

    public Simulator(List<Particle> p) {
        particles = p;
    }

    public void Simulate(){
        double time = 0.0;
        int printCont = 0;
        while (time < Data.totalTime) {
//
            if (Data.printTime * printCont <= time) {
//                System.out.println(particles.get(0).getMassCenter().getX()+  "  : " + particles.get(0).getMassCenter().getY());
                o.printSystem(particles, time);
                printCont++;
            }

            /* Previous positions */
            List<Particle> previousPositions  = new ArrayList<>(particles);

            //Calculate forces
            final double aarr = time;
            particles.forEach((p) -> {if(!p.isWall()) p.getForce(previousPositions,aarr);});

            for (Particle p : particles) {
                if (!p.isWall()){
                    updatePosition(p);
                    updateTarget(p, time);
                }
            }

            time += Data.dt;
        }
        o.done();
    }

    private void updateTarget(Particle p, double time) {
        if (p.reachedTarget()) {
            if (!p.getCurrentTarget().isEnd())
                p.nextTarget(); //TODO: ASK IF NEXT TARGET SHOULD SORT THE REMAINING TARGETS
            else {
                resetParticle(p);
                o.printCaudal(1, time);
            }

        }
    }

    private void resetParticle(Particle p) {
        if (Data.continuous) {
            particles.remove(p);
        } else {
            p.resetTargets(); p.resetPosition();
        }
    }


    /* Uses Verlet */
    private void updatePosition(Particle p) {
        double nextX = 2 * p.getMassCenter().getX() - p.getPreviousMassCenter().getX() + Data.dt * Data.dt * p.getForce().getX() / p.getMass() ;
        double nextY = 2 * p.getMassCenter().getY() - p.getPreviousMassCenter().getY() + Data.dt * Data.dt * p.getForce().getY() / p.getMass() ;

        double nextVelX = (nextX - p.getPreviousMassCenter().getX()) / (2 * Data.dt);
        double nextVelY = (nextY - p.getPreviousMassCenter().getY()) / (2 * Data.dt);

        //TODO check if logic applies
        double nextOrientation = 2 * p.getOrientation() - p.getPreviousOrientation() + Data.dt * Data.dt * p.getTorque() /p.getInertiaMoment();
        double nextAngularVel = (nextOrientation - p.getPreviousOrientation()) / (2 * Data.dt);

        p.setPreviousOrientation(p.getOrientation());
        p.setOrientation(nextOrientation);
        p.setAngularVelocity(nextAngularVel);

        p.setPreviousMassCenter(p.getMassCenter());
        p.setMassCenter(new Point(nextX, nextY));
        p.setVel(new Point(nextVelX, nextVelY));
    }


}
