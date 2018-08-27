package ar.edu.itba.proyectofinal;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmallSim {

    public static void main(String[] args) {

        Particle p1 = createParticle(1,5,5, 5, 30);
        Particle p2 = createParticle(2,10,10,5, 30);
        List<Particle> particles = new ArrayList<>();
        particles.add(p1);


        Simulator sim = new Simulator(particles);

        sim.Simulate();
    }


    public static Particle createParticle(int id, double massX, double massY, double targetX, double targetY){
        double mass = 1;
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
        double radius = 1;
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
