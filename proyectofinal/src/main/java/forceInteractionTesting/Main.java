package forceInteractionTesting;


import ar.edu.itba.proyectofinal.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {



        Segment ab = new Segment(new Point(5.79276, 0.0537232), new Point(6.57767, 0.208362));
        Point wall = new Point(6, 0);

        Point closest = testclosest(ab,wall);

        System.out.println(testclosest(ab, wall));

        System.out.println("Distance is " + closest.distanceBetween(wall) + " radius add " +  (0.05 + 0.14004));
        System.out.println((0.05 + 0.14004) - closest.distanceBetween(wall));

//        Point[] pol = new Point[]{a,b};
//        double[] bounds = Utils.poligonBounds(pol, 0.12);
//        System.out.println(bounds[0] +  " " + bounds[1] + " " + bounds[2] + " " + bounds[3]);
//
//        Point mc = Utils.massCenter(pol,Data.precision, 0.12);
//        double im1 = Utils.inertiaMoment(pol,mc,Data.precision,0.12);
//        double im2 = Utils.inertiaMoment2(pol,mc,Data.precision,0.12);
//
//        System.out.println(im1 +  "    "  + im2);
//
//        System.out.println(mc.getX() +  "  -  " + mc.getY());




//        List<Double> springs = new ArrayList<>(5);
//        for (int i = 0; i < 5; i++) {
//            springs.add(0.0);
//        }
//        System.out.println("hi");
//        for (Double d: springs){
//            System.out.println("hi");
//            System.out.println(d);
//        }
//         double minmr = Double.MAX_VALUE;
//
//         for(double a = Data.mMin ; a <= Data.mMax; a+= 0.01){
//             for(double b = a; b <= Data.mMax; b+=0.01){
//                 System.out.println(a);
//                 double mr = a*b /(a+b);
//                 if (mr < minmr){
//                     minmr = mr;
//                 }
//             }
//         }
//
//        System.out.println(minmr);
//        Point[] poligon = new Point[8];
//        poligon[0] = new Point(0,0);
//        poligon[1] = new Point(0,5);
//        poligon[2] = new Point(2,5);
//        poligon[3] = new Point(2,1);
//        poligon[4] = new Point(6,1);
//        poligon[5] = new Point(6,5);
//        poligon[6] = new Point(8,5);
//        poligon[7] = new Point(8,0);
//
//        Point[] poligon2 = new Point[8];
//        poligon2[0] = new Point(0,0);
//        poligon2[1] = new Point(0,6);
//        poligon2[2] = new Point(1,6);
//        poligon2[3] = new Point(1,1);
//        poligon2[4] = new Point(4,1);
//        poligon2[5] = new Point(4,6);
//        poligon2[6] = new Point(5,6);
//        poligon2[7] = new Point(5,0);
//
//        Point[] square =  new Point[4];
//        square[0]=new Point(0,0);
//        square[1]=new Point(1,0);
//        square[2]=new Point(1,1);
//        square[3]=new Point(0,1);
//
//        Point[] triangle =  new Point[3];
//        triangle[0] = new Point(0,0);
//        triangle[1] = new Point(6,3);
//        triangle[2] = new Point(6,0);

//        double[] bounds = poligonBounds(poligon);
//        for (int i = 0; i < bounds.length; i++) {
//            System.out.println(bounds[i]);
//        }

//        Point poin = new Point(9, 3);
//        System.out.println(massCenter(square, 0.00001));


//        Point A = new Point(0,0);
//        Point B = new Point(0,3);
//        Point C= new Point(3,3);
//        Point D = new Point(3,0);
//        Point E = new Point(2,-1);
//        Point F = new Point(1,-2);
//        Point G = new Point(0,-1);
//        Point H = new Point(1,0);
//        Point[] poligon = {A,B,C,D};
//        double precision = 0.001;


//
//        Point A = new Point( 8.055799544003124 , 7.379459543866440);
//        Segment seg = new Segment( new Point(10.0 , 10.0 ), new Point(0.0 , 9.999999999999998));
//
//        System.out.println(Utils.completeClosestPoint(seg,A));

//      for(int i = 0; i< 5;i++){
//        long start = System.currentTimeMillis();
//        Point center = massCenter(poligon,precision);
//        System.out.println(inertiaMoment(poligon,center,precision,1));
//        long end = System.currentTimeMillis();
//        System.out.println(" Precision :" + precision  + "      Time : " + ((end-start)/1000.0));
        //precision/=10;
//        }


//        System.out.println(dotProduct(A,B));

//        List<Particle> particles = new ArrayList<Particle>();
//
//        Point[] p1points = new Point[2];
////        List<Point> p1points = new ArrayList<Point>();
//        p1points[0]=(new Point(5.329584190765531,0.20916659820020084));
//        p1points[1]=(new Point(5.444584162485996,0.20924724739286732));
//
//        List<Segment> segments1 = new ArrayList<>();
//        segments1.add(new Segment(p1points[0], p1points[1]));
//
//        Point[] p2points = new Point[2];
//
////        List<Point> p2points = new ArrayList<Point>();
//        p2points[0]=(new Point(5.00682340941375,	0.1706952529048219));
//        p2points[1]=(new Point(5.121823402907966,0.17073393533496034));
//        System.out.println(p2points[1].squaredDistanceBetween(p1points[0]));
//        List<Segment> segments2 = new ArrayList<>();
//        segments2.add(new Segment(p2points[0], p2points[1]));
//        double closestDistance, minDistance = Double.MAX_VALUE;
//        Point a = null, b = null, closestPoint = null;
//        for (Segment segment : segments1){
//            System.out.println("Checking segment" + segment.toString());
//            for (Point point : p2points){
//                System.out.println("Checking point" + point.toString());
//                closestPoint = Utils.completeClosestPoint(segment, point);
//                System.out.println("Closest point is: " + closestPoint.toString());
//                closestDistance = closestPoint.squaredDistanceBetween(point);
//                if (closestDistance < minDistance){
//                    System.out.println("new min : " + closestDistance);
//                    minDistance = closestDistance;
//                    a = closestPoint;
//                    b = point;
//                }
//            }
//
//            double r1 = 0.123146;
//            double r2 = 0.161865;
//
//            System.out.println("min dist: " +minDistance);
//            System.out.println((0.123146 + 0.161865) * (0.123146 + 0.161865));
//            if (minDistance < (r1+r2)*(r1+r2)){
//                System.out.println("CHOCAN2");
//            }else{
//                System.out.println("NOCHOC");
//            }
//                this.applyCollisionForces(p, a, b);
        }


//    /**
//     * Finds out if Point p lies inside the provided poligon. Does so by calculating the number of intersections
//     * an imaginary horizontal line starting on p collides with the poligon's walls. If the number of collisions is odd,
//     * the point lies inside.
//     * @param poligon list of points describing the poligon of interest
//     * @param p Point
//     * @return boolean determining if p lies inside p
//     * idea obtained from: https://www.sanfoundry.com/java-program-check-whether-given-point-lies-given-polygon/
//     */
//    public static boolean liesInside(Point[] poligon, Point p){
//        Point start, end;
//        int wallsCrossed = 0;
//        int startIndex, endIndex;
//        int numberOfSides = poligon.length;
//        for (int i = 0; i < numberOfSides; i++){
//            if (i == numberOfSides-1){
//                end = poligon[0];
//            } else {
//                end = poligon[i+1];
//            }
//            start = poligon[i];
//            //horizontal case
//            if(Math.abs(start.getY() - end.getY()) <= 0.0001){
//                if(Math.abs(start.getY() - p.getY())<= 0.0001){
//                    Point min, max;
//                    if(Math.min(start.getX(), end.getX()) == start.getX()){
//                        min = start;
//                        max = end;
//                    }else{
//                        min = end;
//                        max = start;
//                    }
//                    if(p.getX() >= min.getX() && p.getX() <= max.getX()){
//                        return true;
//                    }
//                }
//            }
//            //vertical case
//            else if (Math.abs(start.getX() - end.getX()) <= 0.0001){
//                if((start.getY() >= p.getY() && end.getY() <= p.getY())
//                        || (start.getY() <= p.getY() && end.getY() >= p.getY())){
//                    if(Math.abs(p.getX()-start.getX())<0.0001){
//                        return true;
//                    }else if (p.getX() < start.getX()){
//                        wallsCrossed++;
//                    }
//                }
//            }
//            //diagonal cases
//            //find intersection of horizontal crossing p with the straigh line crossing start and end described as mx+c
//            else{
//                double m = (end.getY()-start.getY()) / (end.getX()-start.getX());
//                double c = start.getY() - m * start.getX();
//                double xIntersect = (p.getY() - c )/m;
//                if ( Math.abs(xIntersect-p.getX()) < 0.0001){
//                    return true;
//                } else if ( p.getX() < xIntersect){
//                    wallsCrossed++;
//                }
//            }
//
//        }
//        return wallsCrossed%2 == 1;
//    }








//    public static Particle createParticle(int id,Point[] points, double r){
//        double mMin = 45;
//        double mMax = 114;
//        double rMin = 0.12;
//        double rMax = 0.165;
//        double precision = 0.00001;
//        double mass = Math.random() * (mMax - mMin) + mMin;
//        Point massCenter = Utils.massCenter(points, precision );
//        List<AngularPoint> ap = Utils.calculateAngularPoints(massCenter, points);
//        double radius = Math.random() * (rMax - rMin) + Data.rMin;
//        double inertiaMoment = Utils.inertiaMoment(points, massCenter, Data.precision);
//        inertiaMoment *= mass;
//        double phase = Math.random() * Math.PI * 2;
//        Particle p = new Particle(id, mass, ap, massCenter, 0, r, 2.0, new Point(0,0), 0 , 0, inertiaMoment, phase);
//        p.setColor(255,255,255);
//
//
//        return p;
//    }

    public static double getOrientation(Point p1, Point p2){



        return 0;
    }


//    /**Finds the coordinates for an enclusing square limited by the poligon's right,left,top and bottommost coordinates.
//     * @param poligon
//     * @return 4 value array with [min x, max x, min y, max y] values
//     */
//    public static double[] poligonBounds(Point[] poligon){
//        double xi =  poligon[0].getX();
//        double xf = poligon[0].getX();
//        double yi = poligon[0].getY();
//        double yf = poligon[0].getY();
//        for (Point p: poligon){
//            if (xi>p.getX()){
//                xi = p.getX();
//            }
//            if (xf< p.getX()){
//                xf = p.getX();
//            }
//            if (yi > p.getY()){
//                yi = p.getY();
//            }
//            if (yf < p.getY()){
//                yf = p.getY();
//            }
//        }
//        double[] ans =  {xi,xf,yi,yf};
//        return ans;
//    }

//    /**
//     * Finds massCentre of given poligon with the desired precision. Does so by discretely calculating the integral
//     * inside the poligon's bounding circle.
//     * @param poligon
//     * @param precision
//     * @return
//     */
//    public static Point massCenter(Point[]poligon, double precision){
//        long acum  = 0;
//        double xAcum = 0;
//        double yAcum = 0;
//        double[] bounds = poligonBounds(poligon);
//        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
//            for (double j = bounds[2]; j< bounds[3] ;j+=precision){
//                if (liesInside(poligon, new Point(i,j))){
//                    acum++;
//                    xAcum+=i;
//                    yAcum+=j;
//                }
//            }
//        }
////        double x =(double)Math.round((xAcum/acum) * 100000d) / 100000d;
//        double x =xAcum/acum;
////        double y = (double)Math.round((yAcum/acum) * 100000d) / 100000d;
//        double y = yAcum/acum;
//        return new Point(x,y);
//    }

//    public static double inertiaMoment(Point[] poligon, Point relative, double precision, double mass){
//
//        int points  = 0;
//        double inertia = 0;
//        double[] bounds = poligonBounds(poligon);
//        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
//            for (double j = bounds[2]; j< bounds[3] ;j+=precision){
//                if (liesInside(poligon, new Point(i,j))){
//                    points++;
//                    double relX = i - relative.getX();
//                    double relY = j - relative.getY();
//                    inertia += relX * relX + relY * relY;
//                }
//            }
//        }
//
//        return inertia * (mass/points);
//    }


    private static double xProduct(Point p1, Point p2){
        return p1.getX() * p2.getY() - p1.getY() * p2.getX();
    }


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
        } else if (y1 == y2) {
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
                if (pointSquaredDistance(x1, y1, a, b) <= pointSquaredDistance(x2, y2, a, b)) {
                    System.out.println("Closest point is " + x1 + " , " + y1);
                    return;
                } else {
                    System.out.println("Closest point is " + x2 + " , " + y2);
                    return;
                }
            } else {
                rx = (slope * x1 - perpendicularSlope * a + b - y1) / (slope - perpendicularSlope);
                ry = perpendicularSlope * (rx - a) + b;
                System.out.println("Closest point is " + rx + " , " + ry);
                return;
            }
        }
    }
    public static double dotProduct(Point A, Point B){
        return A.getX() * B.getX() + A.getY() * B.getY();
    }

    public static double pointSquaredDistance (double x1, double y1, double x2, double y2) {
        return (x1-x2) * (x1-x2) + (y1-y2) * (y1-y2);
    }


    public static double dotProduct(double x1, double y1, double x2, double y2){
        System.out.println(x1 * x2 + y1 * y2);
        return x1 * x2 + y1 * y2;
    }

    public static double crossProduct(double x1, double y1, double x2, double y2){
        System.out.println(x1 * y2 - y1 * x2);
        return x1 * y2 - y1 * x2;
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

    public static Point testclosest(Segment ab, Point p) {
        double x1= ab.getP1().getX();
        double y1= ab.getP1().getY();
        double x2= ab.getP2().getX();
        double y2= ab.getP2().getY();
        double a = p.getX();
        double b = p.getY();

        Point lineVec = new Point(x2-x1, y2-y1);
        Point pVec = new Point(a-x1, b-y1);

        double lineLength =  lineVec.module();
        Point lineUnitVec = new Point(lineVec.getX() /lineLength, lineVec.getY() /lineLength);

        Point pointVecScaled = new Point(pVec.getX() / lineLength, pVec.getY()/lineLength);

        double t = lineUnitVec.dotProduct(pointVecScaled);

        if (t<0.0) {
            t = 0.0;
        } else if (t>1.0) {
            t=1.0;
        }

        Point nearest = new Point(lineVec.getX() * t, lineVec.getY() * t);
        nearest.add(ab.getP1());
        return nearest;
    }

}
