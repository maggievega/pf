package ar.edu.itba.proyectofinal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Input {
    //TODO: HACER COMO UN CONTADOR PARA NO TENER LOS MAGIC NUMBERS O UN ENUM O ALGO.
    private int N = -1;
    private static int count = 0;
    private List<Particle> particles;
    private List<Point> targets;
    private String fileName;
    private Type type;

    Input(Type type, String fileName) {
        this.type = type;
        this.fileName = fileName;
    }

    static Stream<String> stream;

    /**
     * Load for particles
     * @param p
     */
    void loadWalls(List<Particle> p) {
        particles = p;
        load();
    }

    void loadParticles(List<Particle> p, List<Point> t) {
        particles = p;
        targets = t;
        load();
    }

    void loadTargets(List<Point> t) {
        targets = t;
        load();
    }

    private void resetCounter() {
        N = -1;
        count = 0;
    }

    void load() {
        resetCounter();
        try {
            stream =  Files.lines(Paths.get(fileName));
            stream.forEach(this::populate);
        } catch (IOException e) {
            System.out.println("Error opening the file. Does not exist");
        }
    }



    private void populate(String line) {
        switch (type) {
            case WALLS:
                populateWalls(line);
                break;
            case CONSTANTS:
                populateConstants(line);
                break;
            case PARTICLES:
                populateParticles(line);
                break;
            case TARGETS:
                populateTargets(line);
                break;
        }
    }

    private void populateTargets(String line) {
        if (N == -1) {
            N = Integer.parseInt(line);
            return;
        }
        if (count >= N) {
            System.out.println("More particles than expected");
            throw new ExceptionInInitializerError("Bad formatted. More particles than expected");
        }
        String[] target = line.split("\\t");
        if (target.length != 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        Point p = new Point(Double.parseDouble(target[0]), Double.parseDouble(target[1]));
        targets.add(p);
        count ++;
    }

    /**
     * Generates all the particles based on the input file
     * Doesn't position them on the board
     * Only creats their geometry
     */
    private void populateParticles(String line) {
        if (N == -1) {
            N = Integer.parseInt(line);
            return;
        }
        if (count >= N) {
            System.out.println("More particles than expected");
            throw new ExceptionInInitializerError("Bad formatted. More particles than expected");
        }
        String[] particle = line.split("\\t");
        int countParticles = Integer.parseInt(particle[0]);
        if (countParticles <= 0) {
            System.out.println("Bad argument");
            throw new ExceptionInInitializerError("Bad argument");
        }
        int countPoints = Integer.parseInt(particle[7]);
        if (particle.length != 8 + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }

        double mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;
        double desiredVel = Double.parseDouble(particle[1]);
        Point vel = new Point(Double.parseDouble(particle[2]), Double.parseDouble(particle[3]));
        double angularVelocity = Double.parseDouble(particle[4]);
        double angularAcc =Double.parseDouble(particle[5]);
        double radius = Double.parseDouble(particle[6]);

        List<Point> points = new ArrayList<>();
        for(int i = 0; i < countPoints; i++) {
            Point point;
            point = new Point(Double.parseDouble(particle[8 + 2 * i]), Double.parseDouble(particle[9 + 2 * i]));
            points.add(point);
        }
        Point massCenter = Utils.calculateMassCenter(points, mass);
        List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);

        for (int j = 0; j < countParticles; j ++) {
            mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;
            Particle p = new Particle(particles.size(), mass, ap, massCenter, 0, radius, desiredVel, vel, angularVelocity , angularAcc, targets);
            particles.add(p);
        }
        count ++;
    }

    private void populateWalls(String line) {
        if (N == -1) {
            N = Integer.parseInt(line);
            return;
        }
        if (count >= N) {
            System.out.println("More particles than expected");
            throw new ExceptionInInitializerError("Bad formatted. More particles than expected");
        }
        String[] particle = line.split("\\t");
        int countPoints = Integer.parseInt(particle[0]);
        if (particle.length != 1 + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double mass = 1;
        double radius = 0.2;

        List<Point> points = new ArrayList<>();
        for(int i = 0; i < countPoints; i++) {
            Point point;
            point = new Point(Double.parseDouble(particle[1 + 2 * i]), Double.parseDouble(particle[2 + 2 * i]));
            points.add(point);
        }
        for (Point p: points) {
            if (p.getX() < Data.minX)
                Data.minX = p.getX();
            if (p.getX() > Data.maxX)
                Data.maxX = p.getX();
            if (p.getY() < Data.minY)
                Data.minY = p.getY();
            if (p.getY() > Data.maxY)
                Data.maxY = p.getY();
        }

        Point massCenter = Utils.calculateMassCenter(points, mass);
        List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);

        List<Point> targets = new ArrayList<>();
        targets.add(massCenter);

        Particle p = new Particle(particles.size(), mass, ap, massCenter, 0, radius , 0, new Point(0,0), 0, 0, targets);
        p.setWall();
        particles.add(p);
        count ++;
    }

    private void populateConstants(String line) {
        String[] values = line.split("\\t");
        if (values.length != 2) {
            //error
        }
        String param = values[0].toLowerCase();
        switch (param) {
            case "dt":
                Data.dt = Double.parseDouble(values[1]);
                break;
            case "printtime":
                Data.printTime = Double.parseDouble(values[1]);
                break;
            case "totaltime":
                Data.totalTime = Double.parseDouble(values[1]);
                break;
            case "mmin":
                Data.mMin = Double.parseDouble(values[1]);
                break;
            case "mmax":
                Data.mMax = Double.parseDouble(values[1]);
                break;

            default:
                    //error
        }
    }


}
