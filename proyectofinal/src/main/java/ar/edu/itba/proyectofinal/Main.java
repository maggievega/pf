package ar.edu.itba.proyectofinal;

import com.beust.jcommander.JCommander;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] argv) {
        Parameters args = new Parameters();
        JCommander jc = JCommander.newBuilder()
                .addObject(args)
                .build();
        jc.parse(argv);
        if (args.help) {
            jc.usage();
            return;
        }
        for(Type t : Type.values()){
            System.out.println(t.toString()  + "  " + t.getValue());
        }
        Input inputConstant = new Input(Type.CONSTANTS,   args.constants);
        Input inputParticles = new Input(Type.PARTICLES, args.particles);
        Input inputTargets = new Input(Type.TARGETS, args.targets);
        Input inputWalls = new Input(Type.WALLS,  args.walls);

        List<Particle> particles = new ArrayList<>();

        System.out.println("-- Starting to load");
        inputConstant.load();
        inputTargets.loadTargets();
        System.out.println("-- Targets Loaded");
        inputWalls.loadWalls(particles);
        System.out.println("-- Walls Loaded");
        inputParticles.loadParticles(particles);
        System.out.println("-- Particles Loaded");
        System.out.println("Number of particles loaded = " + particles.size());

        double speed = particles.get(50).getDesiredVelocity();

        double x1 = particles.get(3).getPoints().get(1).getX();
        double x2 = particles.get(4).getPoints().get(0).getX();
        double opening = Math.round((x2-x1)*100)/100.0;





        Output o = new Output(args.out, args.exit);

        System.out.println("SD - " + Data.SD);
        System.out.println("ETA - " + Data.eta);
        System.out.println("Vel - " + speed);
        System.out.println("APER - " + opening);

        Populator.getInstance().setParticles(particles);
        System.out.println("-- Start Populating");
        Populator.getInstance().PopulateInforme(new Point(12,11));
//        Populator.getInstance().PopulatePaper(new Point(12,16));
//        Populator.getInstance().PopulateSingleParticle();
//        Populator.getInstance().Populate();
        System.out.println("-- Population Finished");
        System.out.println("-- Start Simulation");
        particles.forEach(p -> p.setUpSprings(particles.size()));
        Simulator s = new Simulator(particles, o);
        s.Simulate();
    }
}
