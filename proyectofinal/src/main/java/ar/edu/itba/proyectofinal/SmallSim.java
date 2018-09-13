package ar.edu.itba.proyectofinal;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmallSim {

    public static void main(String[] args) {
        List<Particle> particles = new ArrayList<>();

        Particle p1 = createParticle2(1,80,2.5,5, 8.5, 8);
        particles.add(p1);
        Particle p2 = createParticle2(2,80,7.5,5,2.5, 8);
        particles.add(p2);
//        Particle p3 = createParticle2(3,1,5,7,6.5, 25);
//        particles.add(p3);
//        Particle p4 = createParticle(4,1,2,2,6.5,25);
//        particles.add(p4);
//        Particle p5 = createParticle(5,1,5,3,6.5,25);
//        particles.add(p5);
//        Particle wl = createParticle2(6,2,9,9,0,0);
//        wl.setWall();
//        particles.add(wl);
//        Particle wr = createParticle2(7, 2,4,9,0,0);
//        wr.setWall();
//        particles.add(wr);
//        p2.setWall();


        Simulator sim = new Simulator(particles);

        sim.Simulate();
    }


    public static Particle createParticle(int id,double m, double massX, double massY, double targetX, double targetY){
        double mass = m;
//        AngularPoint a1 = new AngularPoint(Math.PI/4,1);
//        AngularPoint a2 = new AngularPoint(Math.PI*3/4,1);
//        AngularPoint a3 = new AngularPoint(Math.PI*5/4,1);
//        AngularPoint a4 = new AngularPoint(Math.PI*7/4,1);
        AngularPoint a1 = new AngularPoint(0,0.2);
        AngularPoint a2 = new AngularPoint(Math.PI,0.2);
//        AngularPoint a3 = new AngularPoint(Math.PI,1);
//        AngularPoint a4 = new AngularPoint(Math.PI*3/2,1);
        List<AngularPoint> angularPoints = new ArrayList<>();
        angularPoints.add(a1);
        angularPoints.add(a2);
//        angularPoints.add(a3);
//        angularPoints.add(a4);
        Point massCenter = new Point(massX,massY);
        double orientation=0;
        double radius = 0.2;
        double desiredVelocity = 0.5;
        Point vel = new Point(0,0);
        double angularVelocity=0;
        double angularAcceleration=0;

        Target t1 = new Target (targetX,targetY);
        List<Target> targets = new ArrayList<>();
        targets.add(t1);
        return new Particle(id,mass,angularPoints,massCenter,orientation,radius,desiredVelocity,vel,angularVelocity,angularAcceleration,targets,1);
    }

    public static Particle createParticle2(int id,double m, double massX, double massY, double targetX, double targetY){
        double mass = m;

        AngularPoint a1 = new AngularPoint(Math.PI/4,1);
        AngularPoint a2 = new AngularPoint(Math.PI*3/4,1);
        AngularPoint a3 = new AngularPoint(Math.PI*5/4,1);
        AngularPoint a4 = new AngularPoint(Math.PI*7/4,1);
        List<AngularPoint> angularPoints = new ArrayList<>();
        angularPoints.add(a1);
        angularPoints.add(a2);
        angularPoints.add(a3);
        angularPoints.add(a4);
        Point massCenter = new Point(massX,massY);
        double orientation=0;
        double radius = 0.2;
        double desiredVelocity = 1;
        Point vel = new Point(0,0);
        double angularVelocity=0;
        double angularAcceleration=0;

        Target t1 = new Target (targetX,targetY);
        List<Target> targets = new ArrayList<>();
        targets.add(t1);
        return new Particle(id,mass,angularPoints,massCenter,orientation,radius,desiredVelocity,vel,angularVelocity,angularAcceleration,targets,1);
    }



}
