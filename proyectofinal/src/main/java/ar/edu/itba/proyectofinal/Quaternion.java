package ar.edu.itba.proyectofinal;

public class Quaternion {
    public double x;
    public double y;
    public double z;
    public double w;

    public Quaternion(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void normalize() {
        double norm = Math.sqrt(x*x + y*y + z*z + w*w);
        this.x = x/norm;
        this.y = y/norm;
        this.z = z/norm;
        this.w = w/norm;
    }
}
