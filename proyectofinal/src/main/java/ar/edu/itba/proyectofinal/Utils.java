package ar.edu.itba.proyectofinal;


import java.util.ArrayList;
import java.util.List;

public class Utils {


    /**
     * Orientation angles should be set between 0 and 2PI, whilst driving torque angles should vary between -PI/PI
     * @param massCenter
     * @param desired
     * @return
     */
    public static double getAngle (Point massCenter, Point desired) {
        double x1 = 0;
        double y1 = 1;
        double x2 = desired.getX() - massCenter.getX();
        double y2 = desired.getY() - massCenter.getY();
        double value = Math.atan2(x1 * y2 - y1 * x2, x1 * x2 + y1 * y2);
        return value >= 0 ? value : value + 2 * Math.PI;
    }

    public static Point getPerpendicularTo(Point p){
        return new Point (-p.getY(), p.getX());
    }

    public static Point getPerpendicularTo2(Point p){
        return new Point (p.getY(), -p.getX());
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
//        if (x1 == x2) {
        if (doubleEqual(x1,x2)) {
            if ((b >= y1 && b >= y2) || (b <= y1 && b <= y2)) {
                if ((b - y1) * (b - y1) >= (b - y2) * (b - y2)) {
//                    System.out.println("Closest point is " + x1 + " , " + y2);
                    return new Point(x1, y2);
                } else {
//                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return new Point(x1, y1);
                }
            } else {
//                System.out.println("Closest point is " + x1 + " , " + b);
                return new Point(x1, b);
            }
            //} else if (y1 == y2) {
        } else if (doubleEqual(y1,y2)){
            if ((a >= x1 && a >= x2) || (a <= x1 && a <= x2)){
                if ((a-x1) * a-x1 >= (a - x2) * (a * x2)){
//                    System.out.println("Closest point is " + x2 + " , " + y1);
                    return new Point(x2, y1);
                } else {
//                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return new Point(x1, y1);
                }
            } else {
//                System.out.println("Closest point is " + a + " , " + y1);
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
//                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return new Point(x1, y1);
                } else {
//                    System.out.println("Closest point is " + x2 + " , " + y2);
                    return new Point(x2, y2);
                }
            } else {
                rx = (slope * x1 - perpendicularSlope * a + b - y1) / (slope - perpendicularSlope);
                ry = perpendicularSlope * (rx - a) + b;
//                System.out.println("Closest point is " + rx + " , " + ry);
                return new Point(rx, ry);
            }
        }
    }

    public static boolean doubleEqual(double x, double y){
        double dif = x-y;
        if (dif*dif < Data.dt){
            return true;
        }
        return false;
    }
//
//    public static Point calculateMassCenter(List<Point> points, double mass) {
//        double totalMass = 0, totalX = 0, totalY = 0;
//        for (Point p: points) {
//            totalX += p.getX() * mass;
//            totalY += p.getY() * mass;
//            totalMass += mass;
//        }
//        return new Point(totalX / totalMass,totalY / totalMass);
//
//    }

    public static List<AngularPoint> calculateAngularPoints(Point massCenter, Point[] points) {
        List<AngularPoint> ap = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            double angle = Utils.getAngle(massCenter, points[i]);
            double length = massCenter.distanceBetween(points[i]);
            ap.add(new AngularPoint(angle, length));
        }
        return ap;
    }

    public static double inertiaMoment(Point[] poligon, Point relative, double precision){
        //TODO-> Check if the return (*mass/points) is correct

        double points  = 0.0;
        double inertia = 0;
        double[] bounds = poligonBounds(poligon);
        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
            for (double j = bounds[2]; j< bounds[3] ;j+=precision){
                if (liesInside(poligon, new Point(i,j))){
                    points+=1.0;
                    double relX = i - relative.getX();
                    double relY = j - relative.getY();
                    inertia += relX * relX + relY * relY;
                }
            }
        }
        return inertia / points;
//        return inertia * (mass/points);
    }

    public static Point massCenter(Point[]poligon, double precision){
        int acum  = 0;
        double xAcum = 0;
        double yAcum = 0;
        double[] bounds = poligonBounds(poligon);
        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
            for (double j = bounds[2]; j< bounds[3] ;j+=precision){
                if (liesInside(poligon, new Point(i,j))){
                    acum++;
                    xAcum+=i;
                    yAcum+=j;
                }
            }
        }
        double x =(double)Math.round((xAcum/acum) * 100000d) / 100000d;
        double y = (double)Math.round((yAcum/acum) * 100000d) / 100000d;
        return new Point(x,y);
    }

    public static double[] poligonBounds(Point[] poligon){
        double xi =  poligon[0].getX();
        double xf = poligon[0].getX();
        double yi = poligon[0].getY();
        double yf = poligon[0].getY();
        for (Point p: poligon){
            if (xi>p.getX()){
                xi = p.getX();
            }
            if (xf< p.getX()){
                xf = p.getX();
            }
            if (yi > p.getY()){
                yi = p.getY();
            }
            if (yf < p.getY()){
                yf = p.getY();
            }
        }
        double[] ans =  {xi,xf,yi,yf};
        return ans;
    }

    public static boolean liesInside(Point[] poligon, Point p){
        Point start, end;
        int wallsCrossed = 0;
        int startIndex, endIndex;
        int numberOfSides = poligon.length;
        for (int i = 0; i < numberOfSides; i++){
            if (i == numberOfSides-1){
                end = poligon[0];
            } else {
                end = poligon[i+1];
            }
            start = poligon[i];


        }
        return !(wallsCrossed%2 == 1);
    }
}
