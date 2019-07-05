package ar.edu.itba.proyectofinal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class Input {
    private int N = -1;
    private static int count = 0;
    private List<Particle> particles;
    private String fileName;
    private Type type;
    private int particleType = 0;

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
        System.out.println(fileName);
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
                populateParticles(line, Type.PARTICLES.getValue() + particleType);
                particleType++;
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
        if (target[TargetType.TARGET_X1.ordinal()].equals("F")) {
            amount = 1;
            end = true;
        }
        if (target.length != 4 + amount) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double x1 = Double.parseDouble(target[amount + TargetType.TARGET_X1.ordinal()]);
        double y1 = Double.parseDouble(target[amount + TargetType.TARGET_Y1.ordinal()]);
        double x2 = Double.parseDouble(target[amount + TargetType.TARGET_X2.ordinal()]);
        double y2 = Double.parseDouble(target[amount + TargetType.TARGET_Y2.ordinal()]);
        Target t = new Target(end, new Segment(new Point(x1, y1), new Point(x2, y2)));
        Data.targetList.add(t);
        count ++;
    }

    /**
     * Generates all the particles based on the input file
     * Doesn't position them on the board
     * Only creats their geometry
     */
    private void populateParticles(String line, int type) {
        if (N == -1) {
            N = Integer.parseInt(line);
            //Shouldn't move the particle type ID, as it is refering to how many lines there are
            particleType--;
            return;
        }
        if (count >= N) {
            System.out.println("More particles than expected");
            throw new ExceptionInInitializerError("Bad format. More particles than expected");
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
        System.out.println("DESIRED " + desiredVel);

        Point[] points = new Point[countPoints];

        for(int i = 0; i < countPoints; i++) {
            points[i] = new Point(Double.parseDouble(particle[ParticleType.POINT_X.ordinal() + 2 * i]), Double.parseDouble(particle[ParticleType.POINT_Y.ordinal() + 2 * i]));
        }
        
        for (int j = 0; j < countParticles; j ++) {
            double mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;
            double radius = Math.random() * (Data.rMax - Data.rMin) + Data.rMin;
            Point massCenter = Utils.massCenter(points, Data.precision, radius );
            double inertiaMoment = Utils.inertiaMoment(points, massCenter, Data.precision, radius);
            List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);
            inertiaMoment *= mass;
            double phase = Math.random() * Math.PI * 2;
            Particle p = new Particle(particles.size(), type, mass, ap, massCenter, 0, radius, desiredVel, new Point(0,0), 0 , 0, inertiaMoment, phase);
            p.setColor(255,255,255);
            particles.add(p);
            System.out.println("Creating of type : " + type);
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
        if (particle[WallType.WALL_RADIUS.ordinal()].equals("I")){
            initial = true;
            position = 1;
        }

        int countPoints = Integer.parseInt(particle[1 + position]);
        if (particle.length != position + WallType.WALL_X.ordinal() + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double mass = 1;
        double radius = Double.parseDouble(particle[WallType.WALL_RADIUS.ordinal() + position]);

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

        Point massCenter = Utils.calculateWallMassCenter(points, mass);
        List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);

        Particle p = new Particle(particles.size(), Type.WALLS.getValue(), mass, ap, massCenter, 0, radius , 0, new Point(0,0), 0, 0, 1,0);
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
                break;
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
            case "rmin":
                Data.rMin = Double.parseDouble(values[value]);
                break;
            case "rmax":
                Data.rMax = Double.parseDouble(values[value]);
                break;
            case "kn":
                Data.kn = Double.parseDouble(values[value]);
                break;
            case "kt":
                Data.kt = Double.parseDouble(values[value]);
                break;
            case "grav":
                Data.grav = Double.parseDouble(values[value]);
                break;
            case "beta":
                Data.beta = Double.parseDouble(values[value]);
                break;
            case "characteristict":
                Data.characteristicT = Double.parseDouble(values[value]);
                break;
            case "sd":
                Data.SD = Double.parseDouble(values[value]);
                break;
            case "eta":
                Data.eta = Double.parseDouble(values[value]);
                break;
            default:
                    //error
        }
    }


}
