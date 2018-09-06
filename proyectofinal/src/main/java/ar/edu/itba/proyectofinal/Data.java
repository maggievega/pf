package ar.edu.itba.proyectofinal;

class Data {

    static double mMin = 45;
    static double mMax = 114;
    static double dt = 0.00001;
    static double totalTime = 100;
    static double printTime = 0.2;

    static double minX = 0;
    static double minY = 0;
    static double maxX = 10;
    static double maxY = 10;

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
