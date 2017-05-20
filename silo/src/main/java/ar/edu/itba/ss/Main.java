package ar.edu.itba.ss;


import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static SimulationAnswer sa = new SimulationAnswer();

    private static ArrayList<Particle> particles;

    private static Random r = new Random();

    private static double mass = 0.01;

    private static double dt = 0.1 * Math.sqrt(mass / SiloData.kn) / 7;
    private static double dt2 = 100 * dt;

    private static double numCaudal = 100;

    private static double runningTime = 3;
    private static double generationTime = 0.05;

    private static double maxRad = SiloData.D / 10;

    private static boolean WRITE_EXTRAS = false;

    private static long relocationCounterDT = 0;
    private static long relocationCounter = 0;

    public static void main(String[] args) {
        particles = Particle.generate(SiloData.N, mass);

        System.out.println(particles.size());
        double printCont = 0.0;
        double t;
        double lastT = 0;

        for (t = 0; t < runningTime; t += dt){
            if (dt2 * printCont <= t){
                System.out.println(t);

                sa.writeAnswer(particles, dt2*printCont);
                if (WRITE_EXTRAS) {
                    sa.writeCinetic(t, getKineticEnergy(particles));
                }
                printCont ++;
            }
            reinjectParticles();
            updateParticles(dt);
            if(relocationCounterDT == numCaudal){
                sa.writeReloc(t, relocationCounterDT/(t-lastT));
                relocationCounterDT = 0;
                lastT = t;
            }

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
            int tries = 0;
            double x,y;
            if(p.getY() - p.getRadius() <= - (SiloData.L / 10)){
                do{
                    x = r.nextDouble() * ((SiloData.W - maxRad) - maxRad) + maxRad;
                    y = SiloData.L - maxRad +  tries * (SiloData.L / 10) ;
                    tries ++;

                }while(!isValid(new Particle(p.getId(), p.getRadius(), x, y)));
                p.setXSpeed(0);
                p.setYSpeed(0);
                p.setX(x);
                p.setY(y);
                relocationCounter ++;
                relocationCounterDT ++;
            }
        }
    }


        private static boolean isValid(Particle p) {
            for (Particle p2 : particles) {
                if ((p.getRadius() + p2.getRadius() - p2.getDistance(p) > 0))
                    return false;
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

        p.setXSpeed(newXSpeed);
        p.setYSpeed(newYSpeed);
    }

    private static double getKineticEnergy(ArrayList<Particle> particles){
        double ek = 0;

        for (Particle p: particles){
            ek += 0.5 * p.getMass() * (Math.pow(p.getXSpeed(), 2) + Math.pow(p.getYSpeed(), 2));
        }

        return ek;
    }
}
