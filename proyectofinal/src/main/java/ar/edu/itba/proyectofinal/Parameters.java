package ar.edu.itba.proyectofinal;

import com.beust.jcommander.Parameter;

public class Parameters {


    @Parameter(	names = {"--particles"},
            description = "Particles archive")
    public String particles = "particlesSquares.txt";

    @Parameter(	names = {"--constants"},
            description = "Constants archive")
    public String constants = "const.txt";

    @Parameter(	names = {"--walls"},
            description = "Walls archive")
    public String walls = "wallsPaper.txt";

    @Parameter(	names = {"--targets"},
            description = "Targets archive")
    public String targets = "targetsPaper.txt";

    @Parameter(	names = {"--exit"},
            description = "Particles exist archive")
    public String exit = "exit2dspringv1.txt";

    @Parameter(	names = {"--grid"},
        description = "Particles exist archive")
    public double latitude = -1;
    public double longitude = -1;

    @Parameter(	names = {"--out"},
            description = "Particles ovito")
    public String out = "sim2dspringv1.xyz";

    @Parameter(	names = {"--help"},
            description = "Print this help",
            help = true)
    public boolean help = false;
}
