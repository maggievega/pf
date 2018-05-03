
import java.util.List;

public class Simulator {

    private List<Particle> particles;

    public Simulator(List<Particle> particles){
        this.particles=particles;
    }

    public void Simulate(){
        double time = 0.0;
        while (time < Data.totalTime) {
            for (Particle p: particles) {
                p.getForce(particles);
            }
            time += Data.dt;
        }
    }


}
