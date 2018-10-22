package ar.edu.itba.proyectofinal;

import ar.edu.itba.proyectofinal.Particle;

import java.util.ArrayList;
import java.util.List;


public class Main {

    //TODO Que la reinsercion arriba sea despues de bajar unos 2 metros.

    public static void main(String[] args) {
        Input inputConstant = new Input(Type.CONSTANTS, "input/const.txt");
        Input inputParticles = new Input(Type.PARTICLES, "input/particlesPaper.txt");
        Input inputTargets = new Input(Type.TARGETS, "input/targetsPaper.txt");
        Input inputWalls = new Input(Type.WALLS, "input/wallsPaper.txt");
//        Input inputConstant = new Input(Type.CONSTANTS, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/const.txt");
//        Input inputParticles = new Input(Type.PARTICLES, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/particlesPaper.txt");
//        Input inputTargets = new Input(Type.TARGETS, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/targetsPaper.txt");
//        Input inputWalls = new Input(Type.WALLS, "/Users/seguido/IdeaProjects/pf/proyectofinal/input/wallsPaper.txt");


        List<Particle> particles = new ArrayList<>();

        System.out.println("-- Starting to load");
        inputConstant.load();
        inputTargets.loadTargets();
        System.out.println("-- Targets Loaded");
        inputWalls.loadWalls(particles);
        System.out.println("-- Walls Loaded");
        inputParticles.loadParticles(particles);
        System.out.println("-- Particles Loaded");

        Populator.getInstance().setParticles(particles);
        System.out.println("-- Start Populating");
//        Populator.getInstance().Populate();
        Populator.getInstance().PopulatePaper(new Point(12,16));
        System.out.println("-- Population Finished");
        System.out.println("-- Start Simulation");
        Simulator s = new Simulator(particles);
        s.Simulate();

    }
}
