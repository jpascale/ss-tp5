package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SimulationAnswer {
    StringBuilder sb = new StringBuilder();

    public void writeAnswer(ArrayList<Particle> particles, double t){
        ArrayList<Particle> borderParticle = addBorderParticles();
         sb.append("\t").append(particles.size() + borderParticle.size()).append("\n");
        sb.append("\t").append(t).append("\n");
        for(Particle p : borderParticle){
            sb.append("\t").append(p.getX()).append("\t").append(p.getY()).append("\t").append(p.getRadius()).append("\n");
        }
        for(Particle p: particles){
            sb.append("\t").append(p.getX()).append("\t").append(p.getY()).append("\t").append(p.getRadius()).append("\n");
        }

    }

    private ArrayList<Particle> addBorderParticles() {
        ArrayList<Particle> particles = new ArrayList<>();
        particles.add(new Particle(0, 0.005, 0, 0, 0, 0, 0));
        particles.add(new Particle(0, 0.005, 0, SiloData.W, 0, 0, 0));
        particles.add(new Particle(0, 0.005, 0, 0, SiloData.L, 0, 0));
        particles.add(new Particle(0, 0.005, 0, SiloData.W, SiloData.L, 0, 0));
        return particles;
    }

    public void printAnswer(){
        try {
            FileWriter fw = new FileWriter("out.txt", true);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
