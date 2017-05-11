package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static SimulationAnswer sa = new SimulationAnswer();

    private static ArrayList<Particle> particles;
    private static int N;

    private static double mass = 0.01;

    private static double dt = 0.1 * Math.sqrt(mass / SiloData.kn);
    private static double dt2 = 100 * dt;

    private static long runningTime = 10;
    private static long generationTime = 5;


    public static void main(String[] args) {
//        particles = Particle.generate(generationTime, mass);
//        N = particles.size();

        double printTime = 0.0;
        particles = new ArrayList<>();

        Particle p1 = new Particle(0, SiloData.D / 5.0, mass, 0.5, 0.5, 0,0);
        Particle p2 = new Particle(1, SiloData.D / 5.0, mass, SiloData.L/2, 0.1, 0,0);
        particles.add(p1);

        sa.writeAnswer(particles, 0);

        for(double t = 0; t < runningTime; t += dt){
            reinjectParticles();
            if(printTime <= runningTime){
                sa.writeAnswer(particles, printTime);
                printTime += dt2;
            }

            particles.forEach((p) -> calculateForce(p));
            updateParticles(dt);
        }

        sa.printAnswer();

    }


    private static void calculateForce(Particle p){
        p.initializeForce();
        //Updates force with other particles
        for (Particle p2 : particles){
            if (!p.equals(p2)){
                p.updateForce(p2);
            }
        }
        //Updates force with the walls
        p.updateForce();
    }

    private static void reinjectParticles() {
        for (Particle p: particles){
            if(p.getY() + p.getRadius() >= SiloData.L + (SiloData.L / 10)){
                p.setY(0);
                p.setXSpeed(0);
                p.setYSpeed(0);
            }
        }
    }

    /**
     * Updates the position and velocities of the particles using BeeMan
     * @param delta the delta time to advance
     */
    //TODO: CHECK, ES LA FORMA QUE SE ME OCURRIO PORQUE SE NECESITA LA FUERZA EN F (T + DELTA)
    private static void updateParticles(double delta){
        //Me guardo las partículas viejas, haciendo un deep copy
        ArrayList<Particle> oldParticles = new ArrayList<>();
        particles.forEach((p) -> oldParticles.add(p.clone()));

        //Predigo las velocidades y x para todas las parículas
        particles.forEach((p) -> predictVelocities(p,delta));

        //Como necesito la fuerza en delta(t + delta) la calculo para todas las particulas
        //Con las posiciones y velocidades cambiadas
        particles.forEach((p) -> calculateForce(p));

        //Corrijo las velocidades
        particles.forEach((p) -> correctVelocities(p, delta, oldParticles));

    }

    /**
     * Calculates the positions and predicts the velocities for a particle in delta
     * @param p the particle
     * @param delta the delta time to advance
     */
    private static void predictVelocities(Particle p, double delta){
        double newX = p.getX() + p.getXSpeed() * delta + (2.0 / 3.0) * (p.getXForce() / p.getMass()) * Math.pow(delta, 2) - (1.0 / 6.0) * (p.getOldXForce() / p.getMass()) * Math.pow(delta, 2);
        double newY = p.getY() + p.getYSpeed() * delta + (2.0 / 3.0) * (p.getYForce() / p.getMass()) * Math.pow(delta, 2) - (1.0 / 6.0) * (p.getOldYForce() / p.getMass()) * Math.pow(delta, 2);

        p.setX(newX);
        p.setY(newY);

        //PredictV
        double predXSpeed = p.getXSpeed() + (3.0 / 2.0) * (p.getXForce() / p.getMass()) * delta - (1.0/2.0) * (p.getOldXForce() / p.getMass()) * delta;
        double predYSpeed = p.getYSpeed() + (3.0 / 2.0) * (p.getYForce() / p.getMass()) * delta - (1.0/2.0) * (p.getOldYForce() / p.getMass()) * delta;

        p.setXSpeed(predXSpeed);
        p.setYSpeed(predYSpeed);

        p.setOldXForce(p.getXForce());
        p.setOldYForce(p.getYForce());

    }

    private static void correctVelocities(Particle p, double delta, ArrayList<Particle> oldParticles){
         Particle old = oldParticles.get((int)p.getId());

        double newXSpeed = p.getXSpeed() + (1.0 / 3.0) * (p.getXForce() / p.getMass()) * delta + (5.0 / 6.0) * (p.getOldXForce() / p.getMass()) * delta - (1.0 / 6.0) * (old.getOldXForce() / p.getMass()) * delta;
        double newYSpeed = p.getYSpeed() + (1.0 / 3.0) * (p.getYForce() / p.getMass()) * delta + (5.0 / 6.0) * (p.getOldYForce() / p.getMass()) * delta - (1.0 / 6.0) * (old.getOldYForce() / p.getMass()) * delta;

        p.setXSpeed(newXSpeed);
        p.setYSpeed(newYSpeed);
    }
}
