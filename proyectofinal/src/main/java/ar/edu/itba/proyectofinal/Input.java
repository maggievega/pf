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
    private List<Target> targets;
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

    void loadParticles(List<Particle> p, List<Target> t) {
        particles = p;
        targets = t;
        load();
    }

    void loadTargets(List<Target> t) {
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
        int amount = 0;
        boolean end = false;
        if (target[0].equals("F")) {
            amount = 1;
            end = true;
        }
        if (target.length != 2 + amount) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        Target t = new Target(Double.parseDouble(target[amount]), Double.parseDouble(target[amount + 1]), end);
        t.setEnd(end);
        targets.add(t);
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
        int countPoints = Integer.parseInt(particle[2]);

        if (particle.length != 3 + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double desiredVel = Double.parseDouble(particle[1]);

        Point[] points = new Point[countPoints];

        for(int i = 0; i < countPoints; i++) {
            points[i] = new Point(Double.parseDouble(particle[3 + 2 * i]), Double.parseDouble(particle[4 + 2 * i]));
        }
        
        for (int j = 0; j < countParticles; j ++) {
            double mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;
            Point massCenter = Utils.massCenter(points, Data.precision );
            List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);
            double radius = Math.random() * (Data.rMax - Data.rMin) + Data.rMin;
            double inertiaMoment = Utils.calculateInertiaMoment(ap, mass);
            Particle p = new Particle(particles.size(), mass, ap, massCenter, 0, radius, desiredVel, new Point(0,0), 0 , 0, targets, inertiaMoment);
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
        boolean initial = false;
        int position = 0;

        String[] particle = line.split("\\t");
        if (particle[0].equals("I")){
            initial = true;
            position = 1;
        }

        int countPoints = Integer.parseInt(particle[position]);
        if (particle.length != position + 1 + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double mass = 1;
        double radius = 0;

        Point[] points = new Point[countPoints];

        for(int i = 0; i < countPoints; i++) {
            points[i] = new Point(Double.parseDouble(particle[position + 1 + 2 * i]), Double.parseDouble(particle[position + 2 + 2 * i]));
        }
        if (initial) {
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
        }

        Point massCenter = Utils.calculateMassCenter(points, mass);
        List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(massCenter));

        Particle p = new Particle(particles.size(), mass, ap, massCenter, 0, radius , 0, new Point(0,0), 0, 0, targets, 1);
        p.setWall();
        particles.add(p);
        count ++;
    }

    private void populateConstants(String line) {
        String[] values = line.split("\\t");
        if (values.length > 2) {
            //error
        }
        String param = values[0].toLowerCase();
        switch (param) {
            case "continuous":
                Data.continuous = true;
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
