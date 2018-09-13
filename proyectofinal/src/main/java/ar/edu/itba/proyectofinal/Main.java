package ar.edu.itba.proyectofinal;

import ar.edu.itba.proyectofinal.Particle;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        Input inputConstant = new Input(Type.CONSTANTS, "input/const.txt");
        Input inputParticles = new Input(Type.PARTICLES, "input/particles.txt");
        Input inputTargets = new Input(Type.TARGETS, "input/targets.txt");
        Input inputWalls = new Input(Type.WALLS, "input/walls.txt");

        List<Target> targets = new ArrayList<>();
        List<Particle> particles = new ArrayList<>();

        System.out.println("-- Starting to load");
        inputConstant.load();
        inputTargets.loadTargets(targets);
        System.out.println("-- Targets Loaded");
        inputWalls.loadWalls(particles);
        System.out.println("-- Walls Loaded");
        inputParticles.loadParticles(particles, targets);
        System.out.println("-- Particles Loaded");

        Populator.getInstance().setParticles(particles);
        System.out.println("-- Start Populating");
        Populator.getInstance().Populate();
        System.out.println("-- Population Finished");
        System.out.println("-- Start Simulation");
        Simulator s = new Simulator(particles);
        s.Simulate();

    }
}
