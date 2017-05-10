package ar.edu.itba.ss;


import java.util.ArrayList;
import java.util.Random;

public class Particle {
    private long id;

    private double x_pos;
    private double y_pos;

    private double x_speed;
    private double y_speed;

    private double x_force;
    private double y_force;

    private double radius;
    private double mass;

    public Particle(long id, double radius, double mass, double x, double y, double x_speed, double y_speed){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.x_pos = x;
        this.y_pos = y;
        this.x_speed = x_speed;
        this.y_speed = y_speed;
    }

    public double getX() {
        return x_pos;
    }

    public void setX(double x_pos) {
        this.x_pos = x_pos;
    }

    public double getY() {
        return y_pos;
    }

    public void setY(double y_pos) {
        this.y_pos = y_pos;
    }

    public double getXSpeed() {
        return x_speed;
    }

    public void setXSpeed(double x_speed) {
        this.x_speed = x_speed;
    }

    public double getYSpeed() {
        return y_speed;
    }

    public void setYSpeed(double y_speed) {
        this.y_speed = y_speed;
    }

    public double getXForce() {
        return x_force;
    }

    public void setXForce(double x_force) {
        this.x_force = x_force;
    }

    public double getYForce() {
        return y_force;
    }

    public void setYForce(double y_force) {
        this.y_force = y_force;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius(){
        return radius;
    }

    public long getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static ArrayList<Particle> generate(long time, double width, double length, double mass, double D) {
        ArrayList<Particle> particles = new ArrayList<>();
        Random rand = new Random();

        int N = 0;
        long startingTime = System.currentTimeMillis();
        long runningTime = time * 1000;

        double randomX, randomY, randomR;

        while(System.currentTimeMillis() - startingTime < runningTime){
            do{
                randomX = width * rand.nextDouble();
                randomY = length * rand.nextDouble();
                randomR = rand.nextDouble() * (D / 5.0 - D / 7.0) + D / 7.0;
            }while(!valid(randomX, randomY, particles, width, length, randomR));
            particles.add(new Particle(N, randomR, mass, randomX, randomY, 0.0, 0.0));

            N ++;
        }

        return particles;
    }

    private static boolean valid(final double x, final double y, final ArrayList<Particle> list, final double width, final double length, double radius) {
        if(x - radius < 0 || x + radius > width || y - radius < 0 || y + radius > length){
            return false;
        }
        for (Particle p: list){
            double dist = Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2);
            double rad = Math.pow(radius + p.getRadius(), 2);
            if(Double.compare(dist, rad) < 0){
                return false;
            }
        }
        return true;
    }
}
