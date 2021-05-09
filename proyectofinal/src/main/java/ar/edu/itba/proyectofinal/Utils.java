package ar.edu.itba.proyectofinal;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
        double value = Math.atan2(desired.getX()-massCenter.getX(),desired.getY()-massCenter.getY());
        return value >= 0 ? value : value + 2 * Math.PI;
    }

    public static Point getPerpendicularTo(Point p){
        return new Point (-p.getY(), p.getX());
    }

    public static int getSign (double n) {
        return n >= 0 ?  1 : -1;
    }

    //http://www.fundza.com/vectors/point2line/index.html
    public static Point completeClosestPoint(Segment ab, Point p) {
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

    static boolean doubleEqual(double x, double y){
        double dif = x-y;
        if (dif*dif < Data.dt){
            return true;
        }
        return false;
    }

    static Point calculateWallMassCenter(Point[] points, double mass) {
        double totalMass = 0, totalX = 0, totalY = 0;
        for (Point p: points) {
            totalX += p.getX() * mass;
            totalY += p.getY() * mass;
            totalMass += mass;
        }
        return new Point(totalX / totalMass,totalY / totalMass);

    }

    /**
     * Changes the list of cardinal points to angular points with respect to the massCenter
     * @param massCenter
     * @param points
     * @return
     */
    public static List<AngularPoint> calculateAngularPoints(Point massCenter, Point[] points) {
        List<AngularPoint> ap = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            double angle = Utils.getAngle(massCenter, points[i]);
            double length = massCenter.distanceBetween(points[i]);
            ap.add(new AngularPoint(angle, length));
        }
        return ap;
    }

    /**
     * Finds the poligon's rotational moment of inertia with respect to the 'relative' point.
     * Does so by discretely calculating the integral.
     * @param poligon
     * @param relative
     * @param precision
     * @return
     */
    public static double inertiaMoment(Point[] poligon, Point relative, double precision, double radius){
        double points  = 0.0;
        double inertia = 0;
        double[] bounds = poligonBounds(poligon, radius);
        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
            for (double j = bounds[2]; j<= bounds[3] ;j+=precision){
                if (liesInside(poligon, new Point(i,j)) || insideRadius(poligon, new Point(i,j),radius)) {
                    points += 1.0;
                    double relX = i - relative.getX();
                    double relY = j - relative.getY();
                    inertia += relX * relX + relY * relY;
                }
            }
        }
        return inertia / points;
    }

    public static double inertiaMoment2(Point[] poligon, Point relative, double precision, double radius){
        double points  = 0.0;
        double inertia = 0;
        double[] bounds = poligonBounds(poligon, radius);
        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
            for (double j = bounds[2]; j<= bounds[3] ;j+=precision){
                if (liesInside(poligon, new Point(i,j))) {
                    points += 1.0;
                    double relX = i - relative.getX();
                    double relY = j - relative.getY();
                    inertia += relX * relX + relY * relY;
                }
            }
        }
        return inertia / points;
    }

    public static double getMass(){
        Random r = new Random();
        double mass = r.nextGaussian()*13 + 67;
        if(mass < 45){
            mass = 45;
        }
        if(mass >114){
            mass= 114;
        }
        return mass;
    }

    public static boolean insideRadius(Point[] poligon, Point p, double radius){
//        System.out.println("inside radius");
        for (int i = 0; i < poligon.length; i++) {
            int index = i;
            int indexf = i+1;
            if(i == poligon.length-1){
                indexf = 0;
            }
            Segment s = new Segment(poligon[index], poligon[indexf]);
            Point closest = completeClosestPoint(s, p);
            if(p.distanceBetween(closest)<=radius){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds massCentre of given poligon with the desired precision. Does so by discretely calculating the integral
     * inside the poligon's bounding circle.
     * @param poligon
     * @param precision
     * @return
     */
    public static Point massCenter(Point[]poligon, double precision, double radius){
        long acum  = 0;
        double xAcum = 0;
        double yAcum = 0;
        double[] bounds = poligonBounds(poligon, radius);
        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
            for (double j = bounds[2]; j<= bounds[3] ;j+=precision){
//                System.out.println("x : " + i + " -  y : " + j );
                if (liesInside(poligon, new Point(i,j)) || insideRadius(poligon,new Point(i,j),radius)){
                    acum++;
                    xAcum+=i;
                    yAcum+=j;
                }
            }
        }
//        double x =(double)Math.round((xAcum/acum) * 100000d) / 100000d;
        double x =xAcum/acum;
//        double y = (double)Math.round((yAcum/acum) * 100000d) / 100000d;
        double y = yAcum/acum;
        return new Point(x,y);
    }

    /**Finds the coordinates for an enclusing square limited by the poligon's right,left,top and bottommost coordinates.
     * @param poligon
     * @return 4 value array with [min x, max x, min y, max y] values
     */
    public static double[] poligonBounds(Point[] poligon, double radius){
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
        xi -= (radius + 2*Data.precision);
        yi -= (radius + 2*Data.precision);
        xf += (radius + 2*Data.precision);
        yf += (radius + 2*Data.precision);
        double[] ans =  {xi,xf,yi,yf};
        return ans;
    }



    /**
     * Finds out if Point p lies inside the provided poligon. Does so by calculating the number of intersections
     * an imaginary horizontal line starting on p collides with the poligon's walls. If the number of collisions is odd,
     * the point lies inside.
     * @param poligon list of points describing the poligon of interest
     * @param p Point
     * @return boolean determining if p lies inside p
     * idea obtained from: https://www.sanfoundry.com/java-program-check-whether-given-point-lies-given-polygon/
     */
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
            //horizontal case
            if(Math.abs(start.getY() - end.getY()) <= 0.0001){
                if(Math.abs(start.getY() - p.getY())<= 0.0001){
                    Point min, max;
                    if(Math.min(start.getX(), end.getX()) == start.getX()){
                        min = start;
                        max = end;
                    }else{
                        min = end;
                        max = start;
                    }
                    if(p.getX() >= min.getX() && p.getX() <= max.getX()){
                        return true;
                    }
                }
            }
            //vertical case
            else if (Math.abs(start.getX() - end.getX()) <= 0.0001){
                if((start.getY() >= p.getY() && end.getY() <= p.getY())
                        || (start.getY() <= p.getY() && end.getY() >= p.getY())){
                    if(Math.abs(p.getX()-start.getX())<0.0001){
                        return true;
                    }else if (p.getX() < start.getX()){
                        wallsCrossed++;
                    }
                }
            }
            //diagonal cases
            //find intersection of horizontal crossing p with the straigh line crossing start and end described as mx+c
            else{
                double m = (end.getY()-start.getY()) / (end.getX()-start.getX());
                double c = start.getY() - m * start.getX();
                double xIntersect = (p.getY() - c )/m;
                if ( Math.abs(xIntersect-p.getX()) < 0.0001){
                    return true;
                } else if ( p.getX() < xIntersect){
                    wallsCrossed++;
                }
            }

        }
        return wallsCrossed%2 == 1;
    }

}
