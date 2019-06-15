package ar.edu.itba.procesamiento;

import com.beust.jcommander.JCommander;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{
    public static void main( String[] argv ) {
        Parameters args = new Parameters();
        JCommander jc = JCommander.newBuilder()
                .addObject(args)
                .build();
        jc.parse(argv);
        if (args.help) {
            jc.usage();
            return;
        }
        Map<Integer, ParticleType> mapType;
        List<Particle> list;
        List<Particle> walls;
        Input i = new Input(args.input, args.walls, args.particle, args.targets);
        mapType = i.getMap();
        walls = i.getWalls();
        list = i.loadParticles();
    }
}
