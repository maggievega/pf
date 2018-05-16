package ar.edu.itba.proyectofinal;

class Data {

    static int N = 1;
    static double xSize = 8;
    static double ySize = 8;
    //Wall number refering to TOP(1), RIGHT(2), BOTTOM(3), LEFT(4)
    static int wall =1;
    static double distance = 4;
    static double size = 1;
    static double rMin = 0.24;
    static double rMax = 0.33;
    static double aMin = 0.35;
    static double aMax = 0.5;
    static double mMin = 45;
    static double mMax = 114;
    static double dt = Math.pow(10, -4);
    static double totalTime = 60;
    static double printTime = 0.1;

    static double kn = 2.2 * Math.pow(10,6);
    static double kt = kn / 25.0;

    static double en = 0.2;
    static double mr = 1; // TODO: FIND

    static double SD = 20;
    static double eta = 1;
    static double T = 1;

    static double Rt = 0; // TODO: CHANGE
    static double RtChange = 5;//TODO : Should only be used when uniform noise is used

    static double characteristicT = 0.5;

    static double beta = 4.5 * Math.sqrt(SD);

    static double yn = Math.sqrt((4 * kn * mr) / (Math.pow(Math.PI / Math.log(1/ en) , 2)) + 1);

}
