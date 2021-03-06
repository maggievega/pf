package ar.edu.itba.proyectofinal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

class Output {

    private BufferedWriter writer;
    private BufferedWriter exitWriter;

    private int count = 0;
    private int particleCount = -1;


    Output(String out, String exit) {
        File file = new File(out);
        File exitFile = new File(exit);
        try {
            Files.deleteIfExists(file.toPath());
            Files.deleteIfExists(exitFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.exitWriter = new BufferedWriter(new FileWriter(exit, true));
            this.writer = new BufferedWriter(new FileWriter(out,true));
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

    void printExit(double time, int amount) {
        try {
            this.exitWriter.write((amount + " \t " + time + " \n"));
            this.exitWriter.flush();
        } catch (IOException e) {
            System.out.println("Unable to start simulation printer. Simulation cannot be outputted");
        }
    }

    private int sumParticles(List<Particle> particles) {
        int sum = 0;
        for (Particle p: particles) {
            if (p.isWall()) {
                sum += 1;
            }
            else
                sum += p.getPoints().size();
        }
        return sum;
    }

    private int sumTargets(List<Target> targets) {
        int sum = 0;
        for (Target t: targets) {
            sum += 1;

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
        return (int) (variation / Data.spacing) + 1;
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
            for(Segment s: p.getSegments()) {
                Quaternion q = getQuaternionFromSegment(s);
                double len = s.getLength();
                double rad = p.getRadius();
                Point middle = s.middlePoint();
                this.writer.write((
                        p.getR() + "\t" + p.getG() + "\t" + p.getB() + "\t" +
                        p.getOrientationX() + "\t" + p.getOrientationY() + "\t" +  1  + "\t" + p.getId()
                        + "\t" + q.x + "\t" + q.y + "\t" + q.z + "\t" + q.w
                        + "\t" + len + "\t" + rad
                        + "\t" + middle.getX() + "\t" + middle.getY() + "\t" + 0
                        + "\n"));
                count++;
            }
        } catch (IOException e) {
            System.out.println("Unable to print. Simulation cannot be outputted");
        }
    }

    private Quaternion getQuaternionFromSegment(Segment s) {
        double x = 0;
        double y = 0;
        double z = 1;
        double x2 = s.getP2().getX() - s.getP1().getX();
        double y2 = s.getP2().getY() - s.getP1().getY();
        double z2 = 0;

        double norm1 = Math.sqrt(x*x + y*y + z*z);
        double norm2 = Math.sqrt(x2*x2 + y2*y2 + z2*z2);

        x/=norm1;
        y/=norm1;
        z/=norm1;
        x2/=norm2;
        y2/=norm2;
        z2/=norm2;


        Quaternion q = new Quaternion(y*z2-z*y2, z*x2-x*z2, x*y2-y*x2);
        q.w = 1;
        q.normalize();
        return q;
    }

    private void printWallSnapshot(Particle p) {
        List<Segment> listSeg = p.getSegments();
        Point p1 = listSeg.get(0).getP1();
        Point p2 = listSeg.get(0).getP2();
        Quaternion q = getQuaternionFromSegment(listSeg.get(0));
        double len = listSeg.get(0).getLength();
        double rad = p.getRadius();
        Point middle = listSeg.get(0).middlePoint();
        printParticle(p1, 139, 0, 0, p.getRadius(), p.getOrientationX(), p.getOrientationY(), p.getId(), q, len, rad, middle);
    }

    private void printParticle(Point p1, int R, int G, int B, double radius, double orientationX,
                               double orientationY, int id, Quaternion q, double len, double rad, Point middle) {

        try {
            this.writer.write((
                    R + "\t" + G + "\t" + B + "\t" + orientationX
                    + "\t" + orientationY + "\t" + 0 + "\t" + id
                    + "\t" + q.x + "\t" + q.y + "\t" + q.z + "\t" + q.w
                    + "\t" + len + "\t" + rad
                    + "\t" + middle.getX() + "\t" + middle.getY()+  "\t" + 0
                    + "\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printSegment(Point p1, Point p2, int R, int G, int B, double radius,
                              double orientationX, double orientationY, int id) {
        double i;
        if (Utils.doubleEqual(p1.getX(), p2.getX())) {
            int signDiff =  Utils.getSign(p2.getY() - p1.getY());
            int signStart = Utils.getSign(p1.getY());
            int sign = signStart > 0 ? signDiff : signStart;
            double start = Math.abs(p1.getY());
            double end = Math.abs(p2.getY());
            for (i = start; i <= end; i += Data.spacing)
                try {
                    this.writer.write((count + "\t" + p1.getX() + "\t" + i  * sign + "\t"
                            + 0 + "\t" + radius + "\t" + R + "\t" + G + "\t" + B + "\t" + orientationX
                            + "\t" + orientationY + "\t" +  0 + "\t" + id + "\n"));
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
            for (i = start; i <= end; i += Data.spacing)
                try {
                    this.writer.write((count + "\t" + i + "\t" + (m * i * sign + b) + "\t" + 0 + "\t"
                            + radius + "\t" + R + "\t" + G + "\t" + B + "\t" + orientationX + "\t"
                            + orientationY + "\t" + 0 + "\t" + id + "\n"));
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void printAllTargets() {
        for (Target t : Data.targetList) {
            Quaternion q = getQuaternionFromSegment(t.getSegment());
            double len = t.getSegment().getLength();
            double rad = 0.05;
            Point middle = t.getSegment().middlePoint();
            printParticle(t.getSegment().getP1(), 204, 204, 0, 0.05, 0, 0, 0, q, len, rad, middle);
        }

    }

    void done() {
        try {
            this.writer.close();
            this.exitWriter.close();
        } catch (IOException e) {
            System.out.println("Error while closing BufferedWriter");
        }
    }
}
