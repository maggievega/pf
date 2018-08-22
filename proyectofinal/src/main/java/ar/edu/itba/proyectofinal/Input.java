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

    void load() {
        N = -1;
        count = 0;
        try {
            stream =  Files.lines(Paths.get(fileName));
            stream.forEach(c -> populate(c)); //Hacer que lo splitee basandose en '\t' y despues usando la máquina de estados cargar donde se tiene que cargar
        } catch (IOException e) {
            System.out.println("Error opening the file. Does not exist");
        }
    }

     void load(List<Particle> p) {
        particles = p;
        load();
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
        }
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
        if (count > N) {
            //error
            return;
        }
        String[] particle = line.split("\\t");
        int countPoints = Integer.parseInt(particle[5]);
        if (particle.length > 6 + countPoints * 2) {
            System.out.println("ERROR");
            throw new IndexOutOfBoundsException("Bad formatted");
        }
        double mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;
        double desiredVel = Double.parseDouble(particle[0]);
        Point vel = new Point(Double.parseDouble(particle[1]), Double.parseDouble(particle[2]));
        double angularVelocity = Double.parseDouble(particle[3]);
        double angularAcc =Double.parseDouble(particle[4]);

        List<AngularPoint> points = new ArrayList<>();
        for(int i = 0; i < countPoints; i++) {
            AngularPoint ap;
            if (particle[6 + (2 * i)].contains("pi")) {
                ap = new AngularPoint(Double.parseDouble(particle[6 + 2 * i].split("pi", 2)[0])*Math.PI, Double.parseDouble(particle[7 + 2 * i]));
            } else {
                ap = new AngularPoint(Double.parseDouble(particle[6 + 2 * i]), Double.parseDouble(particle[7 + 2 * i]));
            }
            points.add(ap);
        }

        Particle p = new Particle(count, mass, points, desiredVel, vel, angularVelocity , angularAcc, null);
        particles.add(p);
        count ++;
    }

    private void populateWalls(String line) {
        if (N == -1) {
            N = Integer.parseInt(line);
            return;
        }
        if (count > N) {
            //error
            return;
        }

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
