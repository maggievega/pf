package ar.edu.itba.proyectofinal;

import com.beust.jcommander.Parameter;

public class Parameters {

    @Parameter(	names = {"-p", "--particles"},
            description = "Particles archive")
    public String particles = "particlesPaper.txt";

    @Parameter(	names = {"-c", "--constants"},
            description = "Constants archive")
    public String constants = "const.txt";

    @Parameter(	names = {"-w", "--walls"},
            description = "Walls archive")
    public String walls = "wallsPaper.txt";

    @Parameter(	names = {"-t", "--targets"},
            description = "Targets archive")
    public String targets = "targetsPaper.txt";

    @Parameter(	names = {"-e", "--exit"},
            description = "Particles exist archive")
    public String exit = "exit.txt";

    @Parameter(	names = {"-o", "--out"},
            description = "Particles ovito")
    public String out = "out.txt";

    @Parameter(	names = {"-h", "--help"},
            description = "Print this help",
            help = true)
    public boolean help = false;
}
