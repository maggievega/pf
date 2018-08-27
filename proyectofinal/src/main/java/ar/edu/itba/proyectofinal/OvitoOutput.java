package ar.edu.itba.proyectofinal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OvitoOutput {
    private BufferedWriter writer;
    private boolean set = false;
    OvitoOutput() {
        try {
            this.writer = new BufferedWriter(new FileWriter("ovito.xyz",true));
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    void printSystem(List<Particle> particles, double time) {
        try {
            int particleCount = 4 + particles.size() *4;
            this.writer.write(particleCount + "\n\n");
            this.writer.write(1+"\t"+12.5+"\t"+12.5+"\t"+0+"\t"+0.8+ "\n");
            this.writer.write(2+"\t"+0+"\t"+12.5+"\t"+0+"\t"+0.8+ "\n");
            this.writer.write(3+"\t"+12.5+"\t"+0+"\t"+0+"\t"+0.8+ "\n");
            this.writer.write(4+"\t"+0+"\t"+0+"\t"+0+"\t"+0.8+ "\n");
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
        int count = 5;
        try {
            for(Point poin: p.getPoints()){
                this.writer.write((count+"\t"+poin.getX()+"\t"+poin.getY()+"\t"+0+"\t"+0.2+ "\n"));
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
