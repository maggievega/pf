import java.util.List;

public class Populator {

    //TODO Constructor con variables de tamano del entorno, etc

    public static void Populate(List<Particle> particles, int N, double xSize, double ySize, double rMin, double rMax,
                                double aMin, double aMax, double mMax, double mMin){
        for (int i=1; i<=N; i++) {
            double R = Math.random() * (rMax -rMin) + rMin;
            double axis = Math.random() * (aMax - aMin) + aMin;
            double mass = Math.random() * (mMax - mMin) + mMin;

            double x = Math.random() * xSize;
            double y = Math.random() * ySize;
            //TODO Consultar si es requerido que las particulas puedan aparecer con cualquier orientacion
            double m = Math.random() * 2 - 1;

            while (!isValid(x, y, m, R, axis, xSize, ySize)) {
                x = Math.random() * xSize;
                y = Math.random() * ySize;
                m = Math.random() * 2 - 1;
            }

            particles.add(createParticle(i, mass, x, y, m, R, axis));
        }
    }

    //TODO Remove variables after axis
    private static boolean isValid(double x, double y, double m, double R, double axis, double xSize, double ySize){
        double dX = Math.sqrt(Math.pow(0.5*axis,2)/(1+m*m));
        double x1 = x - Math.signum(m) *dX;
        double x2 = x + Math.signum(m) *dX;
        double y1 = y - Math.signum(m) * m * dX;
        double y2= y + Math.signum(m) * m * dX;
        if (x1-R < 0 || x1 + R >xSize || x2-R < 0 || x2 + R >xSize ||
                y1-R < 0 || y1 + R >ySize || y2-R < 0 || y2 + R >xSize) {
            return false;
        }
        //TODO check collisions with other particles
        return true;
    }

    private static Particle createParticle(int id, double mass, double x, double y, double m, double R, double axis){
        double dX = Math.sqrt(Math.pow(0.5*axis,2)/(1+m*m));
        double x1 = x - Math.signum(m) *dX;
        double x2 = x + Math.signum(m) *dX;
        double y1 = y - Math.signum(m) * m * dX;
        double y2= y + Math.signum(m) * m * dX;

        return new Particle(id, mass, x1, y1, x2, y2, R, 0.0,0.0);
    }

}
