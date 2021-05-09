package ar.edu.itba.proyectofinal;

import java.util.*;

public class CellIndex {

    private int x;
    private int y;
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
    private double deltaX;
    private double deltaY;
    private Map<Integer, Coordinate> positions;
    private Map<Coordinate, Set<Integer>> matrix;


    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "( " + x + " , " + y + " )";
        }
    }

    public CellIndex(int x, int y, double minX, double minY, double maxX, double maxY) {
        this.x = x;
        this.y = y;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.deltaX = (maxX - minX) / x;
        this.deltaY = (maxY - minY) / y;
        positions = new HashMap<>();
        matrix = new HashMap<>();
        initializeMatrix();

    }


    public void initializeMatrix() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                matrix.put(new Coordinate(i,j), new HashSet<>());
            }
        }
    }

    public void removeParticle(int id) {
        Coordinate pos = positions.remove(id);
        matrix.get(pos).remove(id);
    }

    public void populate(List<Particle> list) {
        list.forEach(p -> {
            if (p.isWall())
                addWall(p);
            else
                addParticle(p);
        });
    }

    public void addParticle(Particle p) {
        Coordinate pos = getCoordinate(p.getMassCenter().getX(), p.getMassCenter().getY());
        positions.put(p.getId(), pos);
        matrix.get(pos).add(p.getId());
    }

    public void addWall(Particle p) {
        List<Point> points = p.getPoints();
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        Point dif = new Point(p2.getX()-p1.getX(), p2.getY() - p1.getY());
        double abs = dif.module();
        dif.times(1/abs);
        Point wallVersor = dif;

        for (double i = 0; i < abs; i+= 0.25) {
            double x = p1.getX() + wallVersor.getX() * i;
            double y = p1.getY() + wallVersor.getY() * i;
            addPoint(p.getId(), x, y);
        }
        //In case the end vertex wasnt added
        addPoint(p.getId(),p2.getX(),p2.getY());
    }

    public void addPoint(int id, double x, double y){
        Coordinate pos = getCoordinate(x, y);
        matrix.get(pos).add(id);
    }

    public List<Particle> getNeighbours(Particle p, List<Particle> previousPositions) {
        Coordinate pos = positions.get(p.getId());
        Set<Particle> neighbours = new HashSet<>();
        int xi = pos.x - 1 < 0 ? 0 : pos.x - 1;
        int xf = pos.x + 1 >=  x ? pos.x - 1 : pos.x + 1;
        int yi = pos.y - 1 < 0 ? 0 : pos.y - 1;
        int yf = pos.y + 1 >=  y  ? pos.y - 1 : pos.y + 1;
        for (int i = xi; i <= xf; i++) {
            for (int j = yi; j <= yf; j++) {
                Set<Integer> set = matrix.get(new Coordinate(i, j));
                for (Integer id: set) {
                    neighbours.add(previousPositions.get(id));
                }
            }
        }
        neighbours.remove(p);
        return new ArrayList<>(neighbours);

    }

    public void updateParticle(Particle p) {
        Coordinate c = getCoordinate(p.getMassCenter().getX(), p.getMassCenter().getY());
        if (!positions.get(p.getId()).equals(c)) {
            removeParticle(p.getId());
            addParticle(p);
        }
    }

    public Coordinate getCoordinate(double x, double y) {
        int xc = (int)Math.floor((x - minX) / deltaX);
        int yc = (int)Math.floor((y - minY) / deltaY);
        return new Coordinate(xc, yc);

    }

}
