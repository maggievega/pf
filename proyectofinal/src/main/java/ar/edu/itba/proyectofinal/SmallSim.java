package ar.edu.itba.proyectofinal;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmallSim {

    public static void main(String[] args) {

        Particle p1 = createParticle2(1,1,2.5,5, 8.5, 7.6);
        Particle p2 = createParticle2(2,1,8.5,5,1, 8);
        Particle p3 = createParticle2(3,1,5,7.5,5, 1);
        List<Particle> particles = new ArrayList<>();
        particles.add(p1);
//        p2.setWall();
        particles.add(p2);
        particles.add(p3);


        Simulator sim = new Simulator(particles);

        sim.Simulate();
    }


    public static Particle createParticle(int id,double m, double massX, double massY, double targetX, double targetY){
        double mass = m;
//        AngularPoint a1 = new AngularPoint(Math.PI/4,1);
//        AngularPoint a2 = new AngularPoint(Math.PI*3/4,1);
//        AngularPoint a3 = new AngularPoint(Math.PI*5/4,1);
//        AngularPoint a4 = new AngularPoint(Math.PI*7/4,1);
        AngularPoint a1 = new AngularPoint(0,1);
        AngularPoint a2 = new AngularPoint(Math.PI/2,1);
        AngularPoint a3 = new AngularPoint(Math.PI,1);
        AngularPoint a4 = new AngularPoint(Math.PI*3/2,1);
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

        Point t1 = new Point (targetX,targetY);
        List<Point> targets = new ArrayList<>();
        targets.add(t1);
        return new Particle(id,mass,angularPoints,massCenter,orientation,radius,desiredVelocity,vel,angularVelocity,angularAcceleration,targets);
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

        Point t1 = new Point (targetX,targetY);
        List<Point> targets = new ArrayList<>();
        targets.add(t1);
        return new Particle(id,mass,angularPoints,massCenter,orientation,radius,desiredVelocity,vel,angularVelocity,angularAcceleration,targets);
    }

}
