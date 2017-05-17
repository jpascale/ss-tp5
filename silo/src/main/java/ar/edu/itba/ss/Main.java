package ar.edu.itba.ss;


import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static SimulationAnswer sa = new SimulationAnswer();

    private static ArrayList<Particle> particles;

    private static Random r = new Random();

    private static double mass = 0.01;

    private static double dt = 0.1 * Math.sqrt(mass / SiloData.kn) / 10;
    private static double dt2 = 100 * dt;

    private static double runningTime = 1;
    private static double generationTime = 0.04;

    private static double maxRad = SiloData.D / 10;

    private static boolean WRITE_EXTRAS = false;

    private static long relocationCounterDT = 0;
    private static long relocationCounter = 0;

    public static void main(String[] args) {
        particles = Particle.generate(generationTime, mass);

        System.out.println(particles.size());
        double printCont = 0.0;

        for (double t = 0; t < runningTime; t += dt){
            if (dt2 * printCont <= t){
                System.out.println("QUEDA " + (int)((runningTime/dt) - (t/dt)));
                sa.writeAnswer(particles, dt2*printCont);
                if (WRITE_EXTRAS) {
                    sa.writeCinetic(t, getKineticEnergy(particles));
                    sa.writeReloc(t, relocationCounterDT / dt2);
                }
                relocationCounterDT = 0;
                printCont ++;
            }
            updateParticles(dt);
            reinjectParticles();
        }

        System.out.println("CAUDAL GLOBAL: " + relocationCounter/runningTime);
    }



    private static void calculateForce(){
        for(Particle p: particles){
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

    }

    private static void reinjectParticles() {
        for (Particle p: particles){
            if(p.getY() - p.getRadius() <= -(SiloData.L / 10)){
                if(isValid(particles,p)){
                    p.setY(SiloData.L);
                    p.setXSpeed(0);
                    p.setYSpeed(0);
                    relocationCounter ++;
                    relocationCounterDT ++;
                }
            }
        }
    }

    private static boolean isValid(ArrayList<Particle> particles, Particle p) {
        double auxY = p.getY();
        double auxX = p.getX();

        for (Particle p2: particles){
            if(p2.equals(p)){ return false; }
            p.setX(r.nextDouble() * ((SiloData.W - maxRad) - maxRad) + maxRad);
            p.setY(SiloData.L - maxRad);
            if(p.getRadius() + p2.getRadius() - p.getDistance(p2) > 0){
                p.setX(auxX);
                p.setY(auxY);
                return false;
            }

        }
        return true;
    }

    /**
     * Updates the position and velocities of the particles using BeeMan
     * @param delta the delta time to advance
     */
    private static void updateParticles(double delta){
        ArrayList<Particle> oldParticles = new ArrayList<>();
        particles.forEach((p) -> oldParticles.add(p.clone()));

        particles.forEach((p) -> predictVelocities(p,delta));

        calculateForce();

        particles.forEach((p) -> correctVelocities(p, delta, oldParticles));

        calculateForce();

    }

    /**
     * Calculates the positions and predicts the velocities for a particle in delta
     * @param p the particle
     * @param delta the delta time to advance
     */
    private static void predictVelocities(Particle p, double delta){
        double newX = p.getX() + p.getXSpeed() * delta + (2.0 / 3.0) * (p.getXForce() / p.getMass()) * delta * delta
                - (1.0 / 6.0) * (p.getOldXForce() / p.getMass()) * delta * delta;
        double newY = p.getY() + p.getYSpeed() * delta + (2.0 / 3.0) * (p.getYForce() / p.getMass()) * delta * delta
                - (1.0 / 6.0) * (p.getOldYForce() / p.getMass()) * delta * delta;

        p.setX(newX);
        p.setY(newY);

        double predXSpeed = p.getXSpeed() + (3.0 / 2.0) * (p.getXForce() / p.getMass()) * delta
                - (1.0/2.0) * (p.getOldXForce() / p.getMass()) * delta;
        double predYSpeed = p.getYSpeed() + (3.0 / 2.0) * (p.getYForce() / p.getMass()) * delta
                - (1.0/2.0) * (p.getOldYForce() / p.getMass()) * delta;


        if(predXSpeed > 100){
            predXSpeed = 0.1;
        }
        if(predXSpeed < -100){
            predXSpeed = -0.1;
        }
        if(predYSpeed > 100){
            predYSpeed = 0.1;
        }
        if(predYSpeed < -100){
            predYSpeed = -0.1;
        }

        p.setXSpeed(predXSpeed);
        p.setYSpeed(predYSpeed);

        p.setOldXForce(p.getXForce());
        p.setOldYForce(p.getYForce());

    }

    private static void correctVelocities(Particle p, double delta, ArrayList<Particle> oldParticles){
         Particle old = oldParticles.get((int)p.getId());

        double newXSpeed = old.getXSpeed() + (1.0 / 3.0) * (p.getXForce() / p.getMass()) * delta
                + (5.0 / 6.0) * (old.getXForce() / p.getMass()) * delta - (1.0 / 6.0) * (old.getOldXForce() / p.getMass()) * delta;
        double newYSpeed = old.getYSpeed() + (1.0 / 3.0) * (p.getYForce() / p.getMass()) * delta
                + (5.0 / 6.0) * (old.getYForce() / p.getMass()) * delta - (1.0 / 6.0) * (old.getOldYForce() / p.getMass()) * delta;

        if(newXSpeed > 100){
            newXSpeed = 0.1;
        }
        if(newXSpeed < -100){
            newXSpeed = -0.1;
        }
        if(newYSpeed > 100){
            newYSpeed = 0.1;
        }
        if(newYSpeed < -100){
            newYSpeed = -0.1;
        }

        p.setXSpeed(newXSpeed);
        p.setYSpeed(newYSpeed);
    }

    private static double getKineticEnergy(ArrayList<Particle> particles){
        double ek = 0;

        for (Particle p: particles){
            ek += 0.5 * p.getMass() * Math.sqrt(Math.pow(p.getXSpeed(), 2) + Math.pow(p.getYSpeed(), 2));
        }

        return ek;
    }
}
