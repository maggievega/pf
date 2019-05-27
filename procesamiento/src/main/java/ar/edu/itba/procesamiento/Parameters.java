package ar.edu.itba.procesamiento;

import com.beust.jcommander.Parameter;

public class Parameters {


    @Parameter(	names = {"--walls"},
            description = "Walls archive")
    public String walls = "wallsPaper.txt";

    @Parameter(	names = {"--input"},
            description = "Input archive")
    public String input = "input.txt";

    @Parameter(	names = {"--particle"},
            description = "Particle archive")
    public String particle = "particle.txt";

    @Parameter(	names = {"--constant"},
            description = "Constant archive")
    public String constants = "const.txt";

    @Parameter(	names = {"--targets"},
            description = "Targets archive")
    public String targets = "targets.txt";

    @Parameter(	names = {"--output"},
            description = "Output")
    public String output = "exit.xyz";

    @Parameter(	names = {"--help"},
            description = "Print this help",
            help = true)
    public boolean help = false;
}
