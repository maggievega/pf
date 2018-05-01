package forceInteractionTesting;




public class Main {

    public static void main(String[] args) {

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

}
