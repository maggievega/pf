package ar.edu.itba.proyectofinal;


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



    public static Point completeClosestPoint(Segment ab, Point p){
        double slope, perpendicularSlope, c1, c2, f1, f2, rx, ry;
        //Cases where slope or perpendicular slope would be 0 and Infinity
        double x1,y1,x2,y2,a,b;
        x1= ab.getP1().getX();
        y1= ab.getP1().getY();
        x2= ab.getP2().getX();
        y2= ab.getP2().getY();
        a = p.getX();
        b = p.getY();
        if (x1 == x2) {
            if ((b >= y1 && b >=y2) || (b <= y1 && b <=y2)){
                if ((b - y1) * (b - y1) >= (b - y2) * (b - y2)) {
                    System.out.println("Closest point is " + x1 + " , " + y2);
                    return new Point(x1, y2);
                } else {
                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return new Point(x1, y1);
                }
            } else {
                System.out.println("Closest point is " + x1 + " , " + b);
                return new Point(x1, b);
            }
        } else if (y1 == y2) {
            if ((a >= x1 && a >= x2) || (a <= x1 && a <= x2)){
                if ((a-x1) * a-x1 >= (a - x2) * (a * x2)){
                    System.out.println("Closest point is " + x2 + " , " + y1);
                    return new Point(x2, y1);
                } else {
                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return new Point(x1, y1);
                }
            } else {
                System.out.println("Closest point is " + a + " , " + y1);
                return new Point(a, y1);
            }
        } else {

            //Normal scenario
            //Calculating bounds for first point
            slope = (y2 - y1) / (x2 - x1);
            perpendicularSlope = -1 / slope;
            c1 = y1 - perpendicularSlope * x1;
            f1 = perpendicularSlope * a + c1;

            //Calculating bounds for second point
            c2 = y2 - perpendicularSlope * x2;
            f2 = perpendicularSlope * a + c2;

            //find zone

            if ((b >= f1 && b >= f2) || (b <= f1 && b <= f2)) {
                if (p.squaredDistanceBetween(ab.getP1()) <= p.squaredDistanceBetween(ab.getP2())) {
                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return new Point(x1, y1);
                } else {
                    System.out.println("Closest point is " + x2 + " , " + y2);
                    return new Point(x2, y2);
                }
            } else {
                rx = (slope * x1 - perpendicularSlope * a + b - y1) / (slope - perpendicularSlope);
                ry = perpendicularSlope * (rx - a) + b;
                System.out.println("Closest point is " + rx + " , " + ry);
                return new Point(rx, ry);
            }
        }
    }

}
