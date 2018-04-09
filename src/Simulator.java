import java.util.List;

public class Simulator {

    private double totalTime;
    private double printTime;
    private double dt;
    private List<Particle> particles;

    public Simulator(double totalTime, double printTime, double dt, List<Particle> particles){
        this.totalTime=totalTime;
        this.printTime=printTime;
        this.dt=dt;
        this.particles=particles;
    }

    public void Simulate(){

    }


}
