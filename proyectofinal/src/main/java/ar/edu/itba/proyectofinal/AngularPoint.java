package ar.edu.itba.proyectofinal;

public class AngularPoint {

    private Double angle;
    private Double length;


    public AngularPoint(Double angle, Double length){
        if (angle <= Math.PI * 2 && angle >= 0)
            this.angle = angle;
        //sino ver
        this.length = length;

        //Verificar que el angulo este entre 0 y 2 pi y sino despues se resta para que sea.
    }
}
