package ar.edu.itba.ss;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

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

    private double old_x_force;
    private double old_y_force;

    private double radius;
    private double mass;

    Particle(long id, double radius, double mass, double x, double y, double x_speed, double y_speed){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.x_pos = x;
        this.y_pos = y;
        this.x_speed = x_speed;
        this.y_speed = y_speed;

    }

    void initializeForce() {
        x_force = 0.0;
        y_force = getMass() * SiloData.G;
    }

    /**
     * Updates the normal and tangent force in this particle with regards to the particle p
     * @param p the other particle
     */
    void updateForce(Particle p){
        double e = p.getRadius() + getRadius() - getDistance(p);
        if (e > 0){
            double relativeSpeedX = this.x_speed - p.getXSpeed();
            double relativeSpeedY = this.y_speed - p.getXSpeed();

            double enx = (p.getX() - this.x_pos) / getDistance(p);
            double eny = (p.getY() - this.y_pos) / getDistance(p);

            updateForce(relativeSpeedX, relativeSpeedY, enx, eny, e);
        }

    }

    private void updateForce(double rsx, double rsy, double enx, double eny, double e){
        double relativeSpeedT = rsx * -eny + rsy * enx;

        double normalForce = - SiloData.kn * e;
        double tangentForce = - SiloData.kt * e * relativeSpeedT;

        this.x_force += normalForce * enx + tangentForce * - eny;
        this.y_force += normalForce * eny + tangentForce * enx;
    }

    /**
     * Updates the normal and tangent force in this particle with regards to the walls
     */
    //TODO: HAY QUE VER EL ENX Y ENY EN LOS CASOS PORQUE AHORA ESTA HECHO CUANDO ES DERECHA LA COLISIÓN
    void updateForce(){
        double e;
        double enx,eny;

        //LEFT WALL
        e = this.getX() - this.getRadius();

        if(e < 0 && this.getY() + this.getRadius() < SiloData.L){
            enx = -1;
            eny = 0;
            updateForce(this.getXSpeed(), this.getYSpeed(), enx, eny, e);

        }

        //RIGHT WALL
        e = this.getX() + this.getRadius() - SiloData.W;

        if(e > 0 && this.getY() + this.getRadius() < SiloData.L){
            enx = 1;
            eny = 0;
            updateForce(this.getXSpeed(), this.getYSpeed(), enx, eny, e);
        }

        //TOP WALL
        e = this.getY() - this.getRadius();

        if(e < 0 ){
            enx = 0;
            eny = -1;
            updateForce(this.getXSpeed(), this.getYSpeed(), enx, eny, e);

        }

        //BOTTOM WALL
        e = this.getY() + this.getRadius() - SiloData.L;

        if(getY() + getRadius() > SiloData.L){
            if(!isGap()){
                enx = 0;
                eny = 1;
                updateForce(this.getXSpeed(), this.getYSpeed(), enx, eny, e);

            }
        }

    }

    /**
     * @return if the particle is in the gap
     */
    private boolean isGap() {
        return (this.getX() > (SiloData.L - SiloData.D) / 2) && (this.getX() < (SiloData.L + SiloData.D) / 2);
    }

    public Particle clone(){
        Particle newParticle = new Particle(id, radius, mass, x_pos, y_pos, x_speed, y_speed);
        newParticle.x_force = x_force;
        newParticle.y_force = y_force;
        newParticle.old_x_force = old_x_force;
        newParticle.old_y_force = old_y_force;

        return newParticle;
    }

    double getDistance(Particle p){
        return Math.sqrt(Math.pow(p.getX() - this.getX(), 2) + Math.pow(p.getY() - this.getY(), 2));
    }

    double getX() {
        return x_pos;
    }

    void setX(double x_pos) {
        this.x_pos = x_pos;
    }

    double getY() {
        return y_pos;
    }

    void setY(double y_pos) {
        this.y_pos = y_pos;
    }

    double getXSpeed() {
        return x_speed;
    }

    void setXSpeed(double x_speed) {
        this.x_speed = x_speed;
    }

    double getYSpeed() {
        return y_speed;
    }

    void setYSpeed(double y_speed) {
        this.y_speed = y_speed;
    }

    double getXForce() {
        return x_force;
    }

    double getYForce() {
        return y_force;
    }

    double getOldXForce() {
        return old_x_force;
    }

    void setOldXForce(double old_x_force) {
        this.old_x_force = old_x_force;
    }

    double getOldYForce() {
        return old_y_force;
    }

    void setOldYForce(double old_y_force) {
        this.old_y_force = old_y_force;
    }

    double getMass() {
        return mass;
    }

    double getRadius(){
        return radius;
    }


    public long getId(){
        return id;
    }

    public static ArrayList<Particle> generate(long time, double mass) {
        ArrayList<Particle> particles = new ArrayList<>();
        Random rand = new Random();

        int N = 0;
        long startingTime = System.currentTimeMillis();
        long runningTime = time * 1000;

        double randomX, randomY, randomR;

        while(System.currentTimeMillis() - startingTime < runningTime){
            do{
                randomX = SiloData.W * rand.nextDouble();
                randomY = SiloData.L * rand.nextDouble();
                randomR = (rand.nextDouble() * (SiloData.D / 5.0 - SiloData.D / 7.0) + SiloData.D / 7.0) / 2.0;
            }while(!valid(randomX, randomY, particles, SiloData.W, SiloData.L, randomR));
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
}
