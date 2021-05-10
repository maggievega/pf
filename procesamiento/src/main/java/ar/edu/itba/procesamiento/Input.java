package ar.edu.itba.procesamiento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Input {
    private static Stream<String> stream = null;
    private static State state = State.Initial;
    private String particleName;
    private int count = 0;
    private int N = 0;
    private Double time = null;
    private int cant = -1;
    private boolean stop = false;
    private int snapshotCount = 0;
    private static List<Particle> p;
    private static List<Particle> walls = new ArrayList<>();
    private static List<Target> targets = new ArrayList<>();
    private Map<Integer, ParticleType> map = new HashMap<>();
    private Scanner s = null;
    String name;

    public Input(String nameParticles, String nameWalls, String nameType, String nameTargets, String nameConstants) {
        this.name = nameParticles;
        loadWalls(nameWalls);
        loadParticleType(nameType);
        loadTargets(nameTargets);
        loadConstants(nameConstants);
        this.particleName = nameParticles;
    }

    private Snapshot loadParticles() throws IOException {
        if (s == null)
            s = new Scanner(particleName);

        while (s.hasNextLine() && !stop) {
            String line = s.nextLine();
            switch (state) {
                case Initial:
                    N = Integer.parseInt(line.split("\\t")[0]);
                    state = State.Time;
                    break;
                case Time:
                    time = Double.parseDouble(line.split("\\t")[0]);
                    count = 0;
                    state = State.Particle;
                    p = new ArrayList<>(N);
                    break;
                case Particle:
                    p.add(loadParticle(line));
                    count ++;
                    if (count == N) {
                        state = State.Time;
                        time = null;
                        stop = true;
                    }
                    break;
            }

        }
        if (time == null)
            return null;
        return new Snapshot(time, p);

    }
    public Particle loadParticle(String line) {
        String[] array = line.split("\\t");

        int id = Integer.parseInt(array[InputType.Id.ordinal()]);
        int type = Integer.parseInt(array[InputType.Type.ordinal()]);
        Point massCenter = new Point(Double.parseDouble(array[InputType.MassCenterX.ordinal()]), Double.parseDouble(array[InputType.MassCenterY.ordinal()]));
        double radius = Double.parseDouble(array[InputType.Radius.ordinal()]);
        double orientation = Double.parseDouble(array[InputType.Orientation.ordinal()]);
        Point velocity = new Point(Double.parseDouble(array[InputType.VelX.ordinal()]), Double.parseDouble(array[InputType.VelY.ordinal()]));
        double angularVelocity = Double.parseDouble(array[InputType.AngularVelocity.ordinal()]);
        double phase = Double.parseDouble(array[InputType.Phase.ordinal()]);
        double mass = Double.parseDouble(array[InputType.Mass.ordinal()]);
        int indexTarget = Integer.parseInt(array[InputType.Target.ordinal()]);
        return  new Particle(id, type, massCenter, velocity, radius, angularVelocity, phase, orientation, mass, indexTarget);
    }


    private void loadConstants(String nameConstants){
        try {
            stream = Files.lines(Paths.get(nameConstants));
            stream.forEach(this::parseConstants);
        } catch (IOException e) {
            System.out.println("Error opening file. Does not exist");
        }
    }

    private void parseConstants(String line) {
        String[] values = line.split("\\t");
        if (values.length > 2) {
            //error
        }
        int name = 0;
        int value = 1;
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

    private void loadTargets(String nameTargets) {
        resetCounter();
        try {
            stream = Files.lines(Paths.get(nameTargets));
            stream.forEach(this::parseTargets);
        } catch (IOException e) {
            System.out.println("Error opening file. Does not exist");
        }
    }

    private enum TargetType {
        TARGET_X, TARGET_Y, INTERVAL
    }


    private void parseTargets(String line) {
        resetCounter();
        if (cant == -1) {
            cant = Integer.parseInt(line);
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
        targets.add(t);
        count ++;
    }

    private void loadWalls (String name) {
        resetCounter();
        try {
            stream = Files.lines(Paths.get(name));
            stream.forEach(this::parseWalls);
        } catch (IOException e) {
            System.out.println("Error opening file. Does not exist");
        }
    }

    private enum WallType {
        CANT_POINTS, WALL_X, WALL_Y
    }

    private void parseWalls(String line) {
        resetCounter();
        if (cant == -1) {
            cant = Integer.parseInt(line);
            return;
        }

        if (count >= N) {
            System.out.println("More particles than expected");
            throw new ExceptionInInitializerError("Bad formatted. More particles than expected");
        }
        String[] particle = line.split("\\t");

        int position = 0;
        if (particle[WallType.CANT_POINTS.ordinal()].equals("I")){
            position = 1;
        }

        int countPoints = Integer.parseInt(particle[position]);
        if (particle.length != position + 1 + countPoints * 2) {
            System.out.println("More or less parameters");
            throw new ExceptionInInitializerError("Bad formatted. More or less parameters than expected");
        }
        double mass = 1;

        Point[] points = new Point[countPoints];

        for(int i = 0; i < countPoints; i++) {
            points[i] = new Point(Double.parseDouble(particle[position + WallType.WALL_X.ordinal() + 2 * i]), Double.parseDouble(particle[position + WallType.WALL_Y.ordinal() + 2 * i]));
        }

        Point massCenter = Utils.calculateWallMassCenter(points, mass);
        List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);

        Particle p = new Particle(count, 0,mass, ap, massCenter, 0, Data.wall_radius, 0, 0);
        p.setWall(true);
        walls.add(p);
        count ++;
    }

    private void loadParticleType (String name) {
        try {
            stream = Files.lines(Paths.get(name));
            stream.forEach(this::parseParticleType);
        } catch (IOException e) {
            System.out.println("Error opening file. Does not exist");
        }
    }

    private  void parseParticleType(String line) {
        resetCounter();
        if (cant == -1) {
            cant = Integer.parseInt(line);
            return;
        }
        if (count >= N) {
            System.out.println("More particles than expected");
            throw new ExceptionInInitializerError("Bad formatted. More particles than expected");
        }
        String[] particle = line.split("\\t");
        List<Point> list = new ArrayList<>();
        double desiredVelocity = Double.parseDouble(particle[1]);
        int countPoints = Integer.parseInt(particle[2]);
        for (int i = 0; i < countPoints; i++) {
            list.add(new Point(Double.parseDouble(particle[2 + 2 * i]), Double.parseDouble(particle[3 + 2 * i])));
        }
        ParticleType type = new ParticleType(list, desiredVelocity);
        map.put(count, type);
    }

    public Map<Integer, ParticleType> getMap() {
        return map;
    }

    public List<Particle> getWalls() {
        return walls;
    }


    private void resetCounter() {
        this.count = 0;
        this.cant = -1;
    }
}
