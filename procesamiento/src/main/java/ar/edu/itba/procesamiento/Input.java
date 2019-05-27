package ar.edu.itba.procesamiento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Input {
    static Stream<String> stream = null;
    static State state = State.Initial;
    private int count = 0;
    private int count_walls = 0;
    private int N = 0;
    private int N_walls = 0;
    static double time;
    static List<Particle> p;
    static List<Particle> walls;
    String name;

    public Input(String nameParticles, String nameWalls, String nameType) {
        this.name = nameParticles;
//      Carga walls y particle type
        loadWalls(nameWalls);
        loadParticleType(nameType);
    }

    private void loadWalls (String name) {
    }

    private void loadParticleType (String name) {
    }

    public List<Particle> loadParticles() {
        try {
            if (stream == null)
                stream = Files.lines(Paths.get(name));
            else
                state = State.Particle;
            stream.forEach(this::populate);
        } catch (IOException e) {
            System.out.println("Error opening the file. Does not exist");
        }
        return p;
    }

    private void populate(String line) {
        String[] array = line.split("\\t");
        if (state == State.Count) {
            N = Integer.parseInt(array[0]);
            state = State.Time;
            p = new ArrayList<>(N);
        } else if (state == State.Time) {
            time = Double.parseDouble(array[1]);
            state = State.Particle;
        } else if (state == State.Initial) {
            N_walls = Integer.parseInt(array[0]);
            state = State.Time;
        } else if (state == State.Particle) {
            if (count < N)
                count ++;
            else {
                resetCounter();
                state = State.Stop;
            }
            int id = Integer.parseInt(array[ParticleType.Id.ordinal()]);
            int type = Integer.parseInt(array[ParticleType.Type.ordinal()]);
            Point massCenter = new Point(Double.parseDouble(array[ParticleType.MassCenterX.ordinal()]), Double.parseDouble(array[ParticleType.MassCenterY.ordinal()]));
            double radius = Double.parseDouble(array[ParticleType.Radius.ordinal()]);
            double orientation = Double.parseDouble(array[ParticleType.Orientation.ordinal()]);
            Point velocity = new Point(Double.parseDouble(array[ParticleType.VelX.ordinal()]), Double.parseDouble(array[ParticleType.VelY.ordinal()]));
            double angularVelocity = Double.parseDouble(array[ParticleType.AngularVelocity.ordinal()]);
            double phase = Double.parseDouble(array[ParticleType.Phase.ordinal()]);
            double mass = Double.parseDouble(array[ParticleType.Mass.ordinal()]);
            double indexTarget = Double.parseDouble(array[ParticleType.Target.ordinal()]);
            Particle part = new Particle(id, type, massCenter, velocity, radius, angularVelocity, phase, orientation, mass, indexTarget);
            p.add(part);
            count++;
        }
    }

    private void resetCounter() {
        this.count = 0;
    }
}
