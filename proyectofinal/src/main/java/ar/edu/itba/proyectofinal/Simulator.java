package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulator {

    private static List<Particle> particles;
    private static Output output;
    private static CellIndex cellIndex;

    private static double lastExit = 0;
    private static double time150 = 0;
    private static int leftRoom = 1;

    private long prevtime;
    private int frame = 0;

    public Simulator(List<Particle> p, Output o) {
        output = o;;
        particles = p;
//        cellIndex = new CellIndex(10,10, -0.1, -1, 8.1, 8.1);
//        cellIndex.populate(particles);
    }

    public void Simulate(){
        prevtime = System.currentTimeMillis();
        double time = 0.0;
        int printCont = 0;
        while (time < Data.totalTime) {
            if (Data.printTime * printCont <= time) {
                System.out.println("Completion: " + (time * 100/Data.totalTime ) + "%");
                output.printSystem(particles, time);

                //This one should be the only remaining one
//                output.printSimSystem(particles,time);
//                System.out.println( 100.0 * time / Data.totalTime);
//                System.out.println(printCont);
//                System.out.println("TARDO :  " + (System.currentTimeMillis() - prevtime));
                prevtime = System.currentTimeMillis();


//                List<Double> maxSprings = new ArrayList<>();
//                for (int i = 0; i < particles.size(); i++) {
//                    maxSprings.add(particles.get(i).maxSpring());
//                }
//                System.out.println("Frame: " + frame++ + " - " +  Collections.max(maxSprings));

                printCont++;
            }

            // If 20 seconds have passed and no particles leave. They are stuck, finish simulation
            if (Double.compare(time - lastExit, 20) >= 0) {
                System.out.println("system stuck");
                break;
            }

            /* Previous positions */
            List<Particle> previousPositions  = new ArrayList<>(particles);

            //Calculate forces
            final double aarr = time;
//            particles.parallelStream().forEach((p)->{if(!p.isWall()) p.getForce(previousPositions,aarr);});


//            prevtime = System.currentTimeMillis();
            //Single threaded
//            particles.forEach((p) -> {if(!p.isWall()) p.getForces(previousPositions,aarr);});

            //Multi threaded
                        particles.parallelStream().forEach((p)->{if(!p.isWall()) p.getForces(previousPositions,aarr);});
//            System.out.println(System.currentTimeMillis()-prevtime);

            //Cell Index
//            particles.parallelStream().forEach( p -> {
//                if(!p.isWall()){
//                    p.getForces(cellIndex.getNeighbours(p, previousPositions), aarr);
//                }
//            });

            for (Particle p : particles) {
                if (!p.isWall()){
                    updatePosition(p);
                    updateTarget(p, time);
//                    cellIndex.updateParticle(p);
                }
            }

            time += Data.dt;
        }
        System.out.println(calculateCaudal(time, leftRoom, time150, 0.7));
        output.done();
    }


    private void updateTarget(Particle p, double t) {
        if (p.reachedTarget()) {
            if (!p.getCurrentTarget().isEnd())
                p.nextTarget();
            else {
                resetParticle(p);
                output.printExit(t, leftRoom);
                System.out.println(leftRoom);
                leftRoom += 1;
                lastExit = t;
                if (leftRoom == Data.caudal) {
                    time150 = t;
                }
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

    private double calculateCaudal(double time, double amount, double time150, double width) {
        return (amount - 150) / ((time - time150) * width);
    }

    /* Uses Verlet */
    private void updatePosition(Particle p) {
        double nextX = 2 * p.getMassCenter().getX() - p.getPreviousMassCenter().getX() + Data.dt * Data.dt * p.getForce().getX() / p.getMass() ;
        double nextY = 2 * p.getMassCenter().getY() - p.getPreviousMassCenter().getY() + Data.dt * Data.dt * p.getForce().getY() / p.getMass() ;

        double nextVelX = (nextX - p.getPreviousMassCenter().getX()) / (2 * Data.dt);
        double nextVelY = (nextY - p.getPreviousMassCenter().getY()) / (2 * Data.dt);

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
