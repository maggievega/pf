package forceInteractionTesting;


import ar.edu.itba.proyectofinal.Point;

public class Main {

    public static void main(String[] args) {

        //testDistances();
//        closestPoint(0,0,2,0,1,1);
//        closestPoint(0,0,0,2,1,1);
//        closestPoint(0,0,10,10,4,0);

        completeClosestPoint(0,0,2,0,1,1);
        completeClosestPoint(0,0,0,2,1,1);
        completeClosestPoint(0,0,10,10,4,0);
    }


    //TODO: At returns, corresponding points should be returned

    /** Provides closest point on the edge to the provided point.
     * Does so by calculating if one of the edge's limits is the closest, otherwise calculates
     * the perpendicular line to the point, intersecting it with the segment.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param a
     * @param b
     */
    public static void completeClosestPoint(double x1, double y1, double x2, double y2, double a, double b){
        double slope, perpendicularSlope, c1, c2, f1, f2, rx, ry;
        //Cases where slope or perpendicular slope would be 0 and Infinity
        if (x1 == x2) {
            if ((b >= y1 && b >=y2) || (b <= y1 && b <=y2)){
                if ((b - y1) * (b - y1) >= (b - y2) * (b - y2)) {
                    System.out.println("Closest point is " + x1 + " , " + y2);
                    return;
                } else {
                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return;
                }
            } else {
                System.out.println("Closest point is " + x1 + " , " + b);
                return;
            }
        }
        if (y1 == y2) {
            if ((a >= x1 && a >= x2) || (a <= x1 && a <= x2)){
                if ((a-x1) * a-x1 >= (a - x2) * (a * x2)){
                    System.out.println("Closest point is " + x2 + " , " + y1);
                    return;
                } else {
                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return;
                }
            } else {
                System.out.println("Closest point is " + a + " , " + y1);
                return;
            }
        }

        //Normal scenario
        //Calculating bounds for first point
        slope = (y2 - y1) / (x2 - x1);
        perpendicularSlope = -1/ slope;
        c1 = y1 - perpendicularSlope * x1;
        f1 = perpendicularSlope * a + c1;

        //Calculating bounds for second point
        c2 = y2 - perpendicularSlope * x2;
        f2 = perpendicularSlope * a + c2;

        //find zone

        if ((b >= f1 && b>= f2) || (b <= f1 && b <= f2)){
            if (pointSquaredDistance(x1,y1,a,b) <= pointSquaredDistance(x2,y2,a,b)){
                System.out.println("Closest point is " + x1 + " , " + y1);
                return;
            } else {
                System.out.println("Closest point is " + x2 + " , " + y2);
                return;
            }
        } else {
            rx = ( slope * x1 - perpendicularSlope * a + b - y1) / ( slope - perpendicularSlope);
            ry = perpendicularSlope * (rx - a) + b;
            System.out.println("Closest point is " + rx + " , " + ry);
            return;
        }
    }


    public static double pointSquaredDistance (double x1, double y1, double x2, double y2) {
        return (x1-x2) * (x1-x2) + (y1-y2) * (y1-y2);
    }


    public static void closestPoint(double x1, double y1, double x2, double y2, double a, double b){
        //Cases where slope or perpendicular slope would be 0 and Infinity
        if (x1 == x2){
            System.out.println("Closest point is " + x1 + " , " + b);
            return;
        }
        if (y1 == y2){
            System.out.println("Closest point is " + a + " , " + y1);
            return;
        }
        double m1, m2,x, y;
        m1 = (y2-y1)/(x2-x1);
        m2 = -1/m1;
        System.out.println(m1);
        System.out.println(m2);
        x = (m1 * x1 - m2 * a + b - y1) / (m1-m2);
        y = m2 * (x - a) + b;
        System.out.println("Closest point is " + x + " , " + y);
    }


    public static double pDistance(double x, double y, double x1, double y1, double x2, double y2) {

        double A = x - x1; // position of point rel one end of line
        double B = y - y1;
        double C = x2 - x1; // vector along line
        double D = y2 - y1;
        double E = -D; // orthogonal vector
        double F = C;

        double dot = A * E + B * F;
        double len_sq = E * E + F * F;

        double dist = Math.abs(dot) / Math.sqrt(len_sq);

        return dist == 0 ? dist : checkForColinearity(x,y,x1,y1,x2,y2,dist);
    }


    //TODO (fix this)
    public static double checkForColinearity(double x, double y, double x1, double y1, double x2, double y2, double dist){

        double d1,d2,seg;
        d1 = Math.sqrt((x-x1)*(x-x1)+(y-y1)*(y-y1));
        d2 = Math.sqrt((x-x2)*(x-x2)+(y-y2)*(y-y2));
        seg = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        return dist;
    }


    public static double distBetweenPointAndLine(double x, double y, double x1, double y1, double x2, double y2) {
        // A - the standalone point (x, y)
        // B - start point of the line segment (x1, y1)
        // C - end point of the line segment (x2, y2)
        // D - the crossing point between line from A to BC

        double AB = distBetween(x, y, x1, y1);
        double BC = distBetween(x1, y1, x2, y2);
        double AC = distBetween(x, y, x2, y2);

        // Heron's formula
        double s = (AB + BC + AC) / 2;
        double area =  Math.sqrt(s * (s - AB) * (s - BC) * (s - AC));

        // but also area == (BC * AD) / 2
        // BC * AD == 2 * area
        // AD == (2 * area) / BC
        // TODO: check if BC == 0
        double AD = (2 * area) / BC;
        return AD;
    }

    public static double distBetween(double x, double y, double x1, double y1) {
        double xx = x1 - x;
        double yy = y1 - y;

        return Math.sqrt(xx * xx + yy * yy);
    }


    public static void testDistances(){
        double test1 = pDistance(0,0,-1,-1,-1,3);
        double test2 = pDistance(2,2,1,1,2,0);
        double test3 = pDistance(3.5,3.5,15.2,3.5,9,3.5);
        double test = pDistance(0,0,6,7,7,9);
        System.out.println(Math.sqrt(6*6+7*7));
        System.out.println(test);


        double test4 = distBetweenPointAndLine(0,0,-1,-1,-1,3);
        double test5 = distBetweenPointAndLine(2,2,1,1,2,0);
        double test6 = distBetweenPointAndLine(3.5,3.5,15.2,3.5,9,3.5);

        System.out.println(Math.sqrt(2));
        System.out.println(test1 + "    " + test2  + "    " + test3);
        System.out.println(test4 + "    " + test5  + "    " + test6);
        System.out.println("asd");
        System.out.println(pDistance(0,0,-2,3,-4,7));
    }

}
