package ar.edu.itba.proyectofinal;

public class Collider {

    //Ft = -kt  * ERARO - gammat * vrelt
    public static void findTangentialForce(Particle p1, Particle p2, Point a, Point b, double overlap){
        Point relV = relative(p1.getVel(), p2.getVel());
        Point r = relative(a,b);
        Point tangentVersor = tangentVersor(r);
        Point relativeVelocityTang = vectorTimes(tangentVersor, project(tangentVersor, relV));


        //todo check if overlap is right
        Point tangentialForce =  vectorTimes(tangentVersor, (-Data.kt * overlap));
        Point dampningTangentialForce = vectorTimes(relativeVelocityTang, (-Data.yt));
        Point totalForce = addForces(tangentialForce, dampningTangentialForce);

        p1.addForce(totalForce);
        Point toCollision = relative(p1.getMassCenter(), a);
        p1.addTorque(toCollision.crossProduct(totalForce));

    }

    //Fn = -kn * overlap - gamman * vreln
    public static void findNormalForce(Particle p1, Particle p2, Point a, Point b, double overlap){

    }

    public static Point relative(Point p1, Point p2){
        return new Point(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public static Point versor(Point p){
        double abs = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        return new Point(p.getX()/abs, p.getY()/abs);
    }

    public static Point tangentVersor(Point p){
        double abs = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        return new Point(-p.getY()/abs, p.getX()/abs);
    }

    public static double project (Point p1, Point p2){
        return p1.dotProduct(p2);
    }

    public static Point vectorTimes(Point p, double scalar){
        return new Point(scalar * p.getX(), scalar * p.getY());
    }

    public static Point addForces(Point a, Point b){ return new Point(a.getX() + b.getX(), a.getY() + b.getY()); }

}
