package ar.edu.itba.proyectofinal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Input {
    private int N = -1;
    private static int count = 0;
    private List<Particle> particles;
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

    void loadParticles(List<Particle> p) {
        particles = p;
        load();
    }

    void loadTargets() {
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
        if (target[TargetType.TARGET_X.ordinal()].equals("F")) {
            amount = 1;
            end = true;
        }
        if (target.length != 3 + amount) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double interval = Double.parseDouble(target[amount + TargetType.INTERVAL.ordinal()]);
        Target t = new Target(Double.parseDouble(target[amount]), Double.parseDouble(target[amount + TargetType.TARGET_Y.ordinal()]),  interval, end);
        Data.targetList.add(t);
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
        int countParticles = Integer.parseInt(particle[ParticleType.COUNT_PARTICLES.ordinal()]);
        if (countParticles <= 0) {
            System.out.println("Bad argument");
            throw new ExceptionInInitializerError("Bad argument");
        }
        int countPoints = Integer.parseInt(particle[ParticleType.COUNT_POINTS.ordinal()]);

        if (particle.length != ParticleType.POINT_X.ordinal() + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double desiredVel = Double.parseDouble(particle[ParticleType.DESIRED_VEL.ordinal()]);

        Point[] points = new Point[countPoints];

        for(int i = 0; i < countPoints; i++) {
            points[i] = new Point(Double.parseDouble(particle[ParticleType.POINT_X.ordinal() + 2 * i]), Double.parseDouble(particle[ParticleType.POINT_Y.ordinal() + 2 * i]));
        }
        
        for (int j = 0; j < countParticles; j ++) {
            double mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;
            mass = Data.mMin;
            Point massCenter = Utils.massCenter(points, Data.precision );
            List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);
            double radius = Math.random() * (Data.rMax - Data.rMin) + Data.rMin;
            double inertiaMoment = Utils.inertiaMoment(points, massCenter, Data.precision);
            inertiaMoment *= mass;
//            double inertiaMoment2 = Utils.calculateInertiaMoment(ap, mass);
            double phase = Math.random() * Math.PI * 2;
            Particle p = new Particle(particles.size(), mass, ap, massCenter, 0, radius, desiredVel, new Point(0,0), 0 , 0, inertiaMoment, phase);
            p.setColor(255,255,255);
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
        if (particle[WallType.CANT_POINTS.ordinal()].equals("I")){
            initial = true;
            position = 1;
        }

        int countPoints = Integer.parseInt(particle[position]);
        if (particle.length != position + WallType.WALL_X.ordinal() + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double mass = 1;
        double radius = Data.wall_radius;

        Point[] points = new Point[countPoints];

        for(int i = 0; i < countPoints; i++) {
            points[i] = new Point(Double.parseDouble(particle[position + WallType.WALL_X.ordinal() + 2 * i]), Double.parseDouble(particle[position + WallType.WALL_Y.ordinal() + 2 * i]));
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

        Particle p = new Particle(particles.size(), mass, ap, massCenter, 0, radius , 0, new Point(0,0), 0, 0, 1,0);
        p.setWall();
        p.setColor(255, 0, 0);
        particles.add(p);
        count ++;
    }

    private void populateConstants(String line) {
        String[] values = line.split("\\t");
        if (values.length > 2) {
            //error
        }
        int name = ConstantType.NAME.ordinal();
        int value = ConstantType.VALUE.ordinal();
        String param = values[name].toLowerCase();
        switch (param) {
            case "continuous":
                Data.continuous = true;
            case "dt":
                Data.dt = Double.parseDouble(values[value]);
                break;
            case "printtime":
                Data.printTime = Double.parseDouble(values[value]);
                break;
            case "totaltime":
                Data.totalTime = Double.parseDouble(values[value]);
                break;
            case "mmin":
                Data.mMin = Double.parseDouble(values[value]);
                break;
            case "mmax":
                Data.mMax = Double.parseDouble(values[value]);
                break;

            default:
                    //error
        }
    }


}
