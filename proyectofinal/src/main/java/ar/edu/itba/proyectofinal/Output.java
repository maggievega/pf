package ar.edu.itba.proyectofinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class Output {

    private BufferedWriter writer;

    Output() {
        try {
            this.writer = new BufferedWriter(new FileWriter("output/sim.txt"));
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    void printSystem(List<Particle> particles, double time) {
        try {
            this.writer.write(time + "\n");
            //TODO: print borders in another color
            printAllSnapshots(particles);
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    private void printAllSnapshots(List<Particle> particles) {
        particles.forEach(this::printParticleSnapshot);
    }

    private void printParticleSnapshot(Particle p) {
        try {
            this.writer.write(p.getMassCenter().getX() + "\t" + p.getMassCenter().getY() + "\t" +  printAllVertices(p) + "\n");

        } catch (IOException e) {
            System.out.println("Unable to print. Simulation cannot be outputted");
        }
    }

    private String printAllVertices(Particle p) {
        StringBuilder sb = new StringBuilder();
        for (Point point: p.getPoints()) {
            sb.append(point.getX()).append("\t").append(point.getY()).append("\t");
        }
        return sb.toString();
    }

    void done() {
        try {
            this.writer.close();

        } catch (IOException e) {
            System.out.println("Error while closing BufferedWriter");
        }
    }
}
