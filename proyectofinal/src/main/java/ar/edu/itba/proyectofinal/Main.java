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
        String starting = "input/";
        String ending = "output/";
        System.out.println(Data.dt);
//        Input inputConstant = new Input(Type.CONSTANTS, "input/const.txt");
//        Input inputParticles = new Input(Type.PARTICLES, "input/particlesPaperLong.txt");
//        Input inputTargets = new Input(Type.TARGETS, "input/targetsPaper.txt");
//        Input inputWalls = new Input(Type.WALLS, "input/wallsPaper.txt");
        Input inputConstant = new Input(Type.CONSTANTS, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/const.txt");
        Input inputParticles = new Input(Type.PARTICLES, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/particlesPaper.txt");
        Input inputTargets = new Input(Type.TARGETS, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/targetsPaper.txt");
        Input inputWalls = new Input(Type.WALLS, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/wallsPaper.txt");

        List<Particle> particles = new ArrayList<>();

        System.out.println("-- Starting to load");
        inputConstant.load();
        inputTargets.loadTargets();
        System.out.println("-- Targets Loaded");
        inputWalls.loadWalls(particles);
        System.out.println("-- Walls Loaded");
        inputParticles.loadParticles(particles);
        System.out.println("-- Particles Loaded");

        String extra = "long_0.7_FIX" + particles.get(100).getDesiredVelocity() + "_" + Data.SD + "";
        Output o = new Output("sim" + extra + ".xyz", "exit" + extra + ".txt");

        Populator.getInstance().setParticles(particles);
        System.out.println("-- Start Populating");
//        Populator.getInstance().Populate();
        Populator.getInstance().PopulatePaper(new Point(12,16));
        System.out.println("-- Population Finished");
        System.out.println("-- Start Simulation");
        Simulator s = new Simulator(particles, o);
        s.Simulate();

    }
}
