package ar.edu.itba.proyectofinal;

public enum Type {
    CONSTANTS(0), WALLS(1), TARGETS(2), PARTICLES(3);

    private final int value;
    private Type(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
