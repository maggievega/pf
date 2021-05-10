package ar.edu.itba.proyectofinal;

import java.util.Objects;

/**
 * Modeling of point in relation to particle's mass centre and orientation.
 */
public class AngularPoint {

    private double angle;
    private double length;

    /**
     * Constructor
     * @param angle makes sure that the angle is between 0 and 2 PI
     * @param length
     */
    public AngularPoint(double angle, double length){
        if (angle <= Math.PI * 2 && angle >= 0)
            this.angle = angle;
        //Normalize angle if not in range
        else if (angle > Math.PI * 2) {
            int amount = (int)(angle / (Math.PI * 2));
            this.angle = angle - amount * Math.PI * 2;
        } else {
            int amount = (int)(Math.abs(angle) / (Math.PI * 2)) + 1;
            this.angle = angle + amount * Math.PI * 2;
        }
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AngularPoint that = (AngularPoint) o;
        return Double.compare(that.angle, angle) == 0 &&
                Double.compare(that.length, length) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle, length);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "AngularPoint { " +
                "angle = " + angle +
                " , length = " + length +
               "  }";
    }
}
