package ar.edu.itba.procesamiento;

import com.beust.jcommander.JCommander;

import java.util.List;

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
        List<Particle> list;
        Input i = new Input(args.input);
        list = i.loadParticles();
    }
}
