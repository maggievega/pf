package ar.edu.itba.proyectofinal;

public class Collider {

    public static void collisionForces(Particle p1, Particle p2, Point a, Point b, double overlap, double time){
        double mr = Math.sqrt(p1.getMass() * p2.getMass() / (p1.getMass() + p2.getMass()));

        Point tForce = findTangentialForce(p1,p2,a,b,overlap,mr, time);
        Point nForce = findNormalForce(p1,p2,a,b,overlap,mr);

        if(tForce.module() >= Data.u * nForce.module()){
            Point tForceVersor = versor(tForce);
            tForce = vectorTimes(tForceVersor, Data.u * nForce.module());
        }
        nForce.add(tForce);
//        p1.addForce(tForce);
        p1.addForce(nForce);
//        p1.addForce(tForce);
        Point toCollision = relative(p1.getMassCenter(), middlePoint(a,b));
        p1.addTorque(-xProduct(toCollision, nForce));
//        p1.addTorque(-xProduct(toCollision, tForce));
//        System.out.println("normal :  " + nForce.module() + " --- " + tForce.module() + "  : tangential");
    }

    //Ft = -kt  * ERARO - gammat * vrelt
    private static Point findTangentialForce(Particle p1, Particle p2, Point a, Point b,double overlap, double mr, double time){
//        Point relV = relative(p2.getVel(), p1.getVel());
        Point relV = relativeVelocity(p1,p2,a,b);
        Point r = relative(a,b);
        Point tangentVersor = tangentVersor(r);
        Point relativeVelocityTang = vectorTimes(tangentVersor, project(tangentVersor, relV));

        Point springExtension = vectorTimes(relativeVelocityTang, Data.dt);

        double direction = getParallelDirection(tangentVersor, springExtension);

        p1.extendSpring(p2.getId(), direction * springExtension.module() );

        double ext = p1.getSpring(p2.getId());

//        System.out.println(time + "\t" + ext);
        //todo check if overlap is right
        Point tangentialForce =  vectorTimes(tangentVersor, -Data.kt * ext  );
        Point dampningTangentialForce = vectorTimes(relativeVelocityTang, -Data.yt * mr);
        Point totalForce = addForces(tangentialForce, dampningTangentialForce);


//        p1.addForce(tangentialForce);
//        p1.addForce(dampningTangentialForce);

//        p1.addTorque(-xProduct(toCollision, totalForce));
//        p1.addTorque(-xProduct(toCollision, tangentialForce));
//        p1.addTorque(-xProduct(toCollision, dampningTangentialForce));

        return tangentialForce;
    }

    //Fn = -kn * overlap - gamman * vreln
    private static Point findNormalForce(Particle p1, Particle p2, Point a, Point b, double overlap, double mr){
//        Point relV = relative(p2.getVel(), p1.getVel());
        Point relV = relativeVelocity(p1,p2,a,b);

        //Inside pointig vector
        Point r = relative(a,b);
        Point normalVersor = versor(r);
        Point relativeVelocityNorm = vectorTimes(normalVersor, project(normalVersor, relV));

        Point normalForce = vectorTimes(normalVersor, -Data.kn * overlap);
        Point dampningNormalForce = vectorTimes(relativeVelocityNorm, -Data.yn * mr);

        Point vel = p1.getVel();
        Point velocityVersor = versor(p1.getVel());
        Point normalForceVersor = versor(normalForce);
        Point normalDampningVersor = versor(dampningNormalForce);
        if (vel.module() < 0.01){
            int afdsa = 23;
        }
        if(dampningNormalForce.getX() != 0 || dampningNormalForce.getY() != 0){
            int asd = 1;
        }
        Point totalForce = addForces(normalForce, dampningNormalForce);
        Point toCollision = relative(p1.getMassCenter(), a);
//        p1.addForce(totalForce);
//        p1.addForce(normalForce);
//        p1.addForce(dampningNormalForce);

        double torque = xProduct(toCollision, normalForce);
        double tangtorque = xProduct(toCollision, dampningNormalForce);
        if (torque != 0 && tangtorque!= 0){
            int asdasd = 2;
        }
//        p1.addTorque(-xProduct(toCollision, totalForce));
//        p1.addTorque(-xProduct(toCollision, normalForce));
        //todo testing this
//        p1.addTorque(-xProduct(toCollision, dampningNormalForce));

        return totalForce;
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

    private static double xProduct(Point p1, Point p2){
        return p1.getX() * p2.getY() - p1.getY() * p2.getX();
    }

    private static double getParallelDirection(Point a, Point b){
        return a.getX() * b.getX() >= 0 && a.getY() * b.getY() >= 0 ? 1.0 : -1.0;
    }

    private static Point middlePoint(Point a, Point b) {
        return new Point((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }

    //velpart2 - velpart1
    private static Point relativeVelocity(Particle p1, Particle p2, Point a, Point b){
        Point p1VelocityAtCollision = velocityAtCollisionPoint(p1, a);
        Point p2VelocityAtCollision = velocityAtCollisionPoint(p2,b);
        return new Point(p2VelocityAtCollision.getX()-p1VelocityAtCollision.getX(),
                p2VelocityAtCollision.getY() - p1VelocityAtCollision.getY());
    }

    private static Point velocityAtCollisionPoint(Particle p, Point cp){
        double angularVelocity = p.getAngularVelocity();
        Point massCenterVelocity = p.getVel();
        Point r = relative(cp,p.getMassCenter());
        double massCenterDistCollision = r.module();
        r = versor(r);
        Point angularAgregatedVelocity = angularVelocity >= 0 ?
                new Point(r.getY(), -r.getX()) : new Point(-r.getY(), r.getX());
        angularAgregatedVelocity.times(massCenterDistCollision);
        return angularAgregatedVelocity;
    }



    private static Point middlePointWithRadius(Point a, Point b, double r1, double r2){
        Point r = relative(a,b);
        Point versor = versor(r);
        double overlap = (r1+r2) - r.module();
        Point contactPointfromA = vectorTimes(versor, r1 + (overlap / 2));
        Point contactPoint = new Point(a.getX()+contactPointfromA.getX(), a.getY()+contactPointfromA.getY());
        return contactPoint;
    }
}
