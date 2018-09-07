package ar.edu.itba.proyectofinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class Output {

    private BufferedWriter writer;
    private int count = 1;


    Output() {
        try {
            this.writer = new BufferedWriter(new FileWriter("sim.xyz",true));
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    void printSystem(List<Particle> particles, double time) {
        count = 1;
        try {
            int particleCount = sumAP(particles);
            this.writer.write(particleCount + "\nTime:   \t" + time + "\n");
            printAllSnapshots(particles);
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    private int sumAP(List<Particle> particles) {
        int sum = 0;
        for (Particle p: particles) {
            sum += p.getPoints().size();
        }
        return sum;
    }

    private void printAllSnapshots(List<Particle> particles) {
        particles.forEach(this::printParticleSnapshot);
    }

    private void printParticleSnapshot(Particle p) {
        try {
            for(Point point: p.getPoints()){
                this.writer.write((count + "\t" + point.getX() + "\t" + point.getY() + "\t" + 0 + "\t" + 0.2 + "\n"));
                count++;
            }
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
