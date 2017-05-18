package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SimulationAnswer {

    public void writeAnswer(ArrayList<Particle> particles, double t){
        StringBuilder sb = new StringBuilder();
        ArrayList<Particle> borderParticle = addBorderParticles();
        sb.append("\t").append(particles.size() + borderParticle.size()).append("\n");
        sb.append("\t").append(t).append("\n");
        for(Particle p : borderParticle){
            sb.append("\t").append(p.getX()).append("\t").append(p.getY()).append("\t").append(p.getRadius()).append("\n");
        }
        for(Particle p: particles){
            sb.append("\t").append(p.getX()).append("\t").append(p.getY()).append("\t").append(p.getRadius()).append("\n");
        }
        try {
            FileWriter fw = new FileWriter("out.txt", true);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeCinetic(double t, double k){
        StringBuilder ec = new StringBuilder();
        ec.append(t).append('\t').append(k).append('\n');

        try {
            FileWriter fw = new FileWriter("ec.txt", true);
            fw.write(ec.toString());
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeReloc(double t, double r){
        StringBuilder reloc = new StringBuilder();
        reloc.append(t).append('\t').append(r).append('\n');

        try {
            FileWriter fw = new FileWriter("reloc.txt", true);
            fw.write(reloc.toString());
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
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

}
