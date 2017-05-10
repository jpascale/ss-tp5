package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SimulationAnswer {
    StringBuilder sb = new StringBuilder();

    public void writeAnswer(ArrayList<Particle> particles, double t){
        sb.append("\t").append(particles.size()).append("\n");
        sb.append("\t").append(t).append("\n");

        for(Particle p: particles){
            sb.append("\t").append(p.getId()).append("\t").append(p.getX()).append("\t").append(p.getY()).append("\t").append(p.getRadius()).append("\n");
        }

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
