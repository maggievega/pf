package ar.edu.itba.proyectofinal;

public class Collider {

    public static void collisionForces(Particle p1, Particle p2, Point a, Point b, double overlap){
        findTangentialForce(p1,p2,a,b,overlap);
        findNormalForce(p1,p2,a,b,overlap);
    }
    //Ft = -kt  * ERARO - gammat * vrelt
    private static void findTangentialForce(Particle p1, Particle p2, Point a, Point b, double overlap){
        Point relV = relative(p1.getVel(), p2.getVel());
        Point r = relative(a,b);
        Point tangentVersor = tangentVersor(r);
        Point relativeVelocityTang = vectorTimes(tangentVersor, project(tangentVersor, relV));


        //todo check if overlap is right
        Point tangentialForce =  vectorTimes(tangentVersor, (Data.kt * overlap));
        Point dampningTangentialForce = vectorTimes(relativeVelocityTang, Data.yt);
        Point totalForce = addForces(tangentialForce, dampningTangentialForce);

//        p1.addForce(totalForce);
        p1.addForce(tangentialForce);
        p1.addForce(dampningTangentialForce);
        Point toCollision = relative(p1.getMassCenter(), a);

//        p1.addTorque(toCollision.crossProduct(totalForce));
        p1.addTorque(toCollision.crossProduct(tangentialForce));
        p1.addTorque(toCollision.crossProduct(dampningTangentialForce));

    }

    //Fn = -kn * overlap - gamman * vreln
    private static void findNormalForce(Particle p1, Particle p2, Point a, Point b, double overlap){
        Point relV = relative(p1.getVel(), p2.getVel());

        //Inside pointig vector
        Point r = relative(a,b);
        Point normalVersor = versor(r);
        Point relativeVelocityNorm = vectorTimes(normalVersor, project(normalVersor, relV));

        Point normalForce = vectorTimes(normalVersor, Data.kn * overlap);
        Point dampningNormalForce = vectorTimes(relativeVelocityNorm, Data.yn);

        Point totalForce = addForces(normalForce, dampningNormalForce);
        Point toCollision = relative(p1.getMassCenter(), a);
//        p1.addForce(totalForce);
        p1.addForce(normalForce);
        p1.addForce(dampningNormalForce);

//        p1.addTorque(toCollision.crossProduct(totalForce));
        p1.addTorque(-toCollision.crossProduct(normalForce));

        //todo testing this
        p1.addTorque(-toCollision.crossProduct(dampningNormalForce));

    }

    private static Point relative(Point p1, Point p2){
        return new Point(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    private static Point versor(Point p){
        double abs = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        return new Point(p.getX()/abs, p.getY()/abs);
    }

    private static Point tangentVersor(Point p){
        double abs = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        return new Point(-p.getY()/abs, p.getX()/abs);
    }

    private static double project (Point p1, Point p2){
        return p1.dotProduct(p2);
    }

    private static Point vectorTimes(Point p, double scalar){
        return new Point(scalar * p.getX(), scalar * p.getY());
    }

    private static Point addForces(Point a, Point b){ return new Point(a.getX() + b.getX(), a.getY() + b.getY()); }

}
