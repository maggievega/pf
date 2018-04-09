import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Particle> particles = new ArrayList<Particle>();
        Populator.Populate(particles);

        Simulator s = new Simulator(particles);
        s.Simulate();

    }
}
