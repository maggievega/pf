package ar.edu.itba.proyectofinal;

import java.util.List;

public class Populator {



    //TODO Constructor con variables de tamano del entorno, etc

    public static void Populate(List<Particle> particles){
        for (int i=1; i<= Data.N; i++) {
            double R = Math.random() * (Data.rMax - Data.rMin) + Data.rMin;
            double axis = Math.random() * (Data.aMax - Data.aMin) + Data.aMin;
            double mass = Math.random() * (Data.mMax - Data.mMin) + Data.mMin;

            double x = Math.random() * Data.xSize;
            double y = Math.random() * Data.ySize;
            //TODO Consultar si es requerido que las particulas puedan aparecer con cualquier orientacion
            double m = Math.random() * 2 - 1;

            while (!isValid(x, y, m, R, axis, particles)) {
                x = Math.random() * Data.xSize;
                y = Math.random() * Data.ySize;
                m = Math.random() * 2 - 1;
            }

            particles.add(createParticle(i, mass, x, y, m, R, axis));
        }
    }

    //TODO Remove variables after axis
    private static boolean isValid(double x, double y, double m, double R, double axis, List<Particle> particles){
        double dX = Math.sqrt(Math.pow(0.5 * axis, 2)/(1 + m * m));
        double x1 = x - Math.signum(m) * dX;
        double x2 = x + Math.signum(m) * dX;
        double y1 = y - Math.signum(m) * m * dX;
        double y2= y + Math.signum(m) * m * dX;
        if (x1 - R < 0 || x1 + R > Data.xSize || x2 - R < 0 || x2 + R > Data.xSize ||
                y1 - R < 0 || y1 + R > Data.ySize || y2 - R < 0 || y2 + R > Data.xSize) {
            return false;
        }
        Particle aux = null;
        for (Particle p: particles) {
            //TODO: METHOD THAT TELLS YOU IF THEY COLLIDE
        }
        return true;
    }

    //TODO:BETTER
    private static Particle createParticle(int id, double mass, double x, double y, double m, double R, double axis){
        return null;
    }

}
