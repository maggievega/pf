package ar.edu.itba.proyectofinal;

import ar.edu.itba.proyectofinal.Particle;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        List<Particle> particles = new ArrayList<>();
        Populator.Populate(particles);
        Simulator s = new Simulator(particles);
        s.Simulate();

    }
}
