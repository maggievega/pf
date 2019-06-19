package ar.edu.itba.proyectofinal;

import ar.edu.itba.proyectofinal.Particle;
import com.beust.jcommander.JCommander;

import java.util.ArrayList;
import java.util.List;



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

        String extra = "long_0.7_ONLYEXIT" + "_" + Data.SD + "";
        Output o = new Output(args.out
                , args.exit);

        Populator.getInstance().setParticles(particles);
        System.out.println("-- Start Populating");
        Populator.getInstance().PopulatePaper(new Point(12,16));
//        Populator.getInstance().Populate();
        System.out.println("-- Population Finished");
        System.out.println("-- Start Simulation");
        Simulator s = new Simulator(particles, o);
        s.Simulate();

    }
}
