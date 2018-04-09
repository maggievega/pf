import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int N = 1;
    private static double xSize = 8;
    private static double ySize = 8;
    //Wall number refering to TOP(1), RIGHT(2), BOTTOM(3), LEFT(4)
    private static int wall =1;
    private static double distance = 4;
    private static double size = 1;
    private static double rMin = 0.24;
    private static double rMax = 0.33;
    private static double aMin = 0.35;
    private static double aMax = 0.5;
    private static double mMin = 45;
    private static double mMax = 114;
    private static double dt = Math.pow(10, -4);
    private static double runningTime = 60;
    private static double printTime = 0.1;

    public static void main(String[] args) {
        List<Particle> particles = new ArrayList<Particle>();
        Populator.Populate(particles, N, xSize, ySize, rMin, rMax, aMin, aMax, mMax, mMin);

        Simulator s = new Simulator(runningTime, printTime, dt, particles);
        s.Simulate();

    }
}
