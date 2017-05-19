package ar.edu.itba.ss;


public class SiloData {

    static double G = 9.8;

    static double W = 1.0;
    static double L = 4;
    static double D = 0.2;

    static double kn = 100000;
    static double kt = 2 * kn;

    static int N = getN(L);

    private static int getN(double l) {
        if(l == 2){
            return 446;
        }else if(l == 4){
            return 781;
        }else if( l == 1.5){
            return 398;
        }
        return 0;
    }
}
