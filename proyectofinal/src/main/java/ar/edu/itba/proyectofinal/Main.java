package ar.edu.itba.proyectofinal;

import ar.edu.itba.proyectofinal.Particle;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        Input inputConstant = new Input(Type.CONSTANTS, "input/const.txt");
        Input inputParticles = new Input(Type.PARTICLES, "input/particles.txt");
        Input inputTargets = new Input(Type.TARGETS, "input/targets.txt");
//        Input inputWalls = new Input(Type.WALLS, "input/walls.txt");

        List<Point> targets = new ArrayList<>();
        List<Particle> particles = new ArrayList<>();

        inputConstant.load();
        inputTargets.loadTargets(targets);
//        inputWalls.loadWalls(particles);
        inputParticles.loadParticles(particles, targets);

        System.out.println(particles);
        Populator.Populate(particles);
        Simulator s = new Simulator(particles);
        s.Simulate();

    }
}
