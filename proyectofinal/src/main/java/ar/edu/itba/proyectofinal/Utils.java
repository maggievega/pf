

public class Utils {


    /**
     * Orientation angles should be set between 0 and 2PI, whilst driving torque angles should vary between -PI/PI
     * @param massCenter
     * @param desired
     * @return
     */
    public static double getAngle (Point massCenter, Point desired) {
        double x1 = 1;
        double y1 = 0;
        double x2 = desired.getX()-massCenter.getX();
        double y2 = desired.getY()-massCenter.getY();
        double value = Math.atan2(x1 * y2 - y1 * x2, x1 * x2 + y1 * y2);
        return value >= 0 ? value : value + 2*Math.PI;
    }


}
