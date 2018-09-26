package ar.edu.itba.proyectofinal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

class Output {

    private BufferedWriter writer;
    private int count = 1;

    Output() {
        File file = new File("sim.xyz");
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.writer = new BufferedWriter(new FileWriter("sim.xyz",true));
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    void printSystem(List<Particle> particles, double time) {
        count = 1;
        try {
            int particleCount = sumAP(particles) + Data.targetList.size();
            this.writer.write((particleCount) + "\nTime:   \t" + time + "\n");
            printAllSnapshots(particles);
            printAllTargets();
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
                this.writer.write((count + "\t" + point.getX() + "\t" + point.getY() + "\t" + 0 + "\t" + p.getRadius() + "\t" +
                        p.getR() + "\t" + p.getG() + "\t" + p.getB() + "\t" + p.getOrientationX() + "\t" + p.getOrientationY() + "\t" +  p.getSelected() + "\n"));
                count++;
            }
        } catch (IOException e) {
            System.out.println("Unable to print. Simulation cannot be outputted");
        }
    }

    private void printAllTargets() {
        for (Target t : Data.targetList) {
            try {
                this.writer.write((count + "\t" + t.getX() + "\t" + t.getY() + "\t" + 0 + "\t" + Data.wall_radius + "\t" + t.getR() + "\t" + t.getG() + "\t" + t.getB() +  "\t" + 0  +  "\t" + 0  + "\t" +  0 + "\n"));
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void done() {
        try {
            this.writer.close();
        } catch (IOException e) {
            System.out.println("Error while closing BufferedWriter");
        }
    }
}
