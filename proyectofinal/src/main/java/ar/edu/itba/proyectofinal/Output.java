package ar.edu.itba.proyectofinal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

class Output {

    private BufferedWriter writer;
    private int count = 0;
    private int particleCount = -1;

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
        count = 0;
        try {
            if (particleCount == -1)
                particleCount = sumParticles(particles) + sumTargets(Data.targetList);
            this.writer.write((particleCount) + "\nTime:   \t" + time + "\n");
            printAllSnapshots(particles);
            printAllTargets();
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    private int sumParticles(List<Particle> particles) {
        int sum = 0;
        for (Particle p: particles) {
            if (p.isWall()) {
                List<Point> points = p.getPoints();
                if (points.size() != 2)
                    System.out.println("ERROR");
                sum += countPoints(points.get(0), points.get(1));
            }
            else
                sum += p.getPoints().size();
        }
        return sum;
    }

    private int sumTargets(List<Target> targets) {
        int sum = 0;
        for (Target t: targets) {
            Segment s = t.getS();
            sum += countPoints(s.getP1(), s.getP2());
        }
        return sum;
    }

    private int countPoints(Point p1, Point p2) {
        double variation;
        if (Utils.doubleEqual(p1.getX(), p2.getX())) {
            variation = Math.abs(p1.getY() - p2.getY());
        } else {
            variation =  Math.abs(p1.getX() - p2.getX());
        }
        return (int) (variation / Data.spacing);
    }

    private void printAllSnapshots(List<Particle> particles) {
        particles.forEach(this::printSnapshot);
    }

    private void printSnapshot(Particle p) {
        if (p.isWall())
            printWallSnapshot(p);
        else
            printParticleSnapshot(p);
    }

    private void printParticleSnapshot(Particle p) {
        try {
            for(Point point: p.getPoints()){
                this.writer.write((count + "\t" + point.getX() + "\t" + point.getY() + "\t" + 0 + "\t" + p.getRadius() + "\t" + p.getR() + "\t" + p.getG() + "\t" + p.getB() + "\t" + p.getOrientationX() + "\t" + p.getOrientationY() + "\t" +  2  + "\n"));
                count++;
            }
        } catch (IOException e) {
            System.out.println("Unable to print. Simulation cannot be outputted");
        }
    }

    private void printWallSnapshot(Particle p) {
        List<Segment> listSeg = p.getSegments();
        Point p1 = listSeg.get(0).getP1();
        Point p2 = listSeg.get(0).getP2();
        printSegment(p1, p2, p.getR(), p.getG(), p.getB(), p.getRadius(), p.getOrientationX(), p.getOrientationY());
    }

    private void printSegment(Point p1, Point p2, int R, int G, int B, double radius, double orientationX, double orientationY) {
        double i;
        if (Utils.doubleEqual(p1.getX(), p2.getX())) {
            int signDiff =  Utils.getSign(p2.getY() - p1.getY());
            int signStart = Utils.getSign(p1.getY());
            int sign = signStart > 0 ? signDiff : signStart;
            double start = Math.abs(p1.getY());
            double end = Math.abs(p2.getY());
            for (i = start; i < end; i += Data.spacing)
                try {
                    this.writer.write((count + "\t" + p1.getX() + "\t" + i  * sign + "\t" + 0 + "\t" + radius + "\t" + R + "\t" + G + "\t" + B + "\t" + orientationX + "\t" + orientationY + "\t" +  0 + "\n"));
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
            double m = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
            double b = p1.getY() - m * p1.getX();
            int signDiff =  Utils.getSign(p2.getX() - p1.getX());
            int signStart = Utils.getSign(p1.getX());
            int sign = signStart > 0 ? signDiff : signStart;
            double start = Math.abs(p1.getX());
            double end = Math.abs(p2.getX());
            for (i = start; i < end; i += Data.spacing)
                try {
                    this.writer.write((count + "\t" + i + "\t" + (m * i * sign + b) + "\t" + 0 + "\t" + radius + "\t" + R + "\t" + G + "\t" + B + "\t" + orientationX + "\t" + orientationY + "\t" + 0 + "\n"));
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

    }

    private void printAllTargets() {
        for (Target t : Data.targetList) {
            Segment s = t.getS();
            printSegment(s.getP1(), s.getP2(), t.getR(), t.getG(), t.getB(), Data.wall_radius, 0, 0);
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
