package ar.edu.itba.proyectofinal;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static double angleLength = 2;
    public static double mMin = (159/2) - 20;
    public static double mMax = (159/2) + 20;
    public static double rMin = 0.12;
    public static double rMax = 0.165;

    static List<Target> targetList = new ArrayList<>();

    static double kn = 2.2 * Math.pow(10,6);
    static double kt = kn / 25.0;

    private static double en = 0.2;
    private static double mr = 22.5;

    private static double tc = Math.PI * Math.sqrt(mr / (2 * kn));
    static double dt = tc/300;
    static double totalTime = 1000;
    static double printTime = 0.05;

    static double maxStringExtension = 0.3;

    static int caudal = 150;

    public static double precision = 0.001;

    static double wall_radius = 0.05;
    static double spacing = 0.5;

    static boolean continuous = false;

    static double minX = 0;
    static double minY = 0;
    static double maxX = 12;
    static double maxY = 12;



    static double SD = 15;
    static double eta = 0.75;
    static double grav = 10;
    static double T = 1;


    static double characteristicT = 0.5;

    static double beta = 4.5 * Math.sqrt(SD);


    //MR should be accounted for separately
    static double yn = Math.sqrt((4 * kn) / (Math.pow((Math.PI/Math.log(1/en)),2) + 1   ) );

    static double yt = yn;


    static double u = 0.5;

    static double AmpModifier = 1;

    static boolean simOut = true;

}
