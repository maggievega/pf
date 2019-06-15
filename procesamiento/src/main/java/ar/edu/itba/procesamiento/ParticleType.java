package ar.edu.itba.procesamiento;

import java.util.List;

public class ParticleType {

    private List<Point> list;
    private double desiredVelocity;
    private Point massCentre;
    private List<AngularPoint> angularPoints;

    public ParticleType(List<Point> list, double desiredVelocity) {
        this.list = list;
        this.desiredVelocity = desiredVelocity;

//        this.massCentre = massCenter();
    }

    public List<Point> getList() {
        return list;
    }

    public double getDesiredVelocity() {
        return desiredVelocity;
    }

    private Point massCenter(Point[] poligon, double precision){
        int acum  = 0;
        double xAcum = 0;
        double yAcum = 0;
        double[] bounds = poligonBounds(poligon);
        for (double i = bounds[0]; i<= bounds[1]; i+= precision){
            for (double j = bounds[2]; j<= bounds[3] ;j+=precision){
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

    private static double[] poligonBounds(Point[] poligon){
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
