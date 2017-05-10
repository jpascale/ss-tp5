package ar.edu.itba.ss;

import java.util.ArrayList;

public class Main {
    private static SimulationAnswer sa = new SimulationAnswer();

    private static ArrayList<Particle> particles;
    private static int N;

    private static double dt = 0.001;
    private static double dt2 = 0.01;

    private static double printTime = 0.0;

    private static long runningTime = 10;
    private static long generationTime = 5;

    private static double W = 1.0;
    private static double L = 2.0;
    private static double D = 0.1;

    private static double kn = Math.pow(10, 5);
    private static double kt = 2 * kn;

    private static double mass = 0.01;

    /*
        Considerar condiciones de contorno periódicas: una vez que las partículas alcanzan (L/10) m por debajo de la salida
        (cara inferior del silo), reinyectarlas en la parte superior del silo con velocidad cero.
     */

    public static void main(String[] args) {
        particles = Particle.generate(generationTime, W, L, mass, D);
        N = particles.size();
        sa.writeAnswer(particles, 0);

        for(double t = 0; t < runningTime; t += dt){
            //mover partículas que estan debajo de cierta posicion
            if(printTime <= runningTime){
                sa.writeAnswer(particles, printTime);
                printTime += dt2;
            }
            //calcular fuerza en x
            //calcular fuerz en y
            //updatear las partículas
        }

        sa.printAnswer();



    }
}
