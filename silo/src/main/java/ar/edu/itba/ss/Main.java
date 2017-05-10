package ar.edu.itba.ss;

import java.util.ArrayList;

public class Main {
    private static SimulationAnswer sa = new SimulationAnswer();

    private static ArrayList<Particle> particles;
    private static int N;

    private static double deltaTime = 0.001;
    private static double printTime = 0.01;

    private static long runningTime = 10;
    private static long generationTime = 5;

    private static double W = 1.0;
    private static double L = 2.0;
    private static double D = 0.1;

    private static double mass = 0.01;

    /*
        Considerar condiciones de contorno periódicas: una vez que las partículas alcanzan (L/10) m por debajo de la salida
        (cara inferior del silo), reinyectarlas en la parte superior del silo con velocidad cero.
     */

    public static void main(String[] args) {
        particles = Particle.generate(generationTime, W, L, mass, D);
        N = particles.size();

        sa.writeAnswer(particles, 0);
        sa.printAnswer();



    }
}
