import java.util.HashMap;
import java.util.Map;

/**
 * Created by ge14 on 17/12/2017.
 */
public class FutileModel2 {
    private static Map<String,Double> leafNodesDensities = new HashMap<>();
    static {
        leafNodesDensities.put("abs02", 0.961267);
        leafNodesDensities.put("bit02", 0.77524);
        leafNodesDensities.put("dia02", 0.775538);
        leafNodesDensities.put("par02", 0.922722);
        leafNodesDensities.put("ped02", 0.77524);
        leafNodesDensities.put("pha02", 0.77524);
        leafNodesDensities.put("rea02", 0.850372);
        leafNodesDensities.put("uni02", 0.77524);

        leafNodesDensities.put("abs03", 0.917678);
        leafNodesDensities.put("bit03", 0.434413);
        leafNodesDensities.put("dia03", 0.434413);
        leafNodesDensities.put("par03", 0.695829);
        leafNodesDensities.put("ped03", 0.434413);
        leafNodesDensities.put("pha03", 0.434413);
        leafNodesDensities.put("rea03", 0.434413);
        leafNodesDensities.put("uni03", 0.434413);
        leafNodesDensities.put("myreal03", 0.757);
    }
    /********************change these to get the probability******************/
    private final static double leafNodeDensity = leafNodesDensities.get("uni02");
    private static double QUERYAREA = 1e-6;
    private static double DATASET_SIZE = 1000000;
    private static double FANOUT = 70;
    private static double AVGLEAFNODEAREA= leafNodeDensity * FANOUT / DATASET_SIZE;
    private static double AVGOBJECTAREA = 0;
    private static double SPACEAREA = 1;
    private static int G = 1000;
    private static int D = 2;
    private static long L;
    /************************************************************************/
    FutileModel2(){
        L = (int)Math.pow(Math.ceil(1+Math.pow(QUERYAREA,D)/Math.pow(AVGLEAFNODEAREA,D)),D);
    }

    public double calculateProbability(long numOfLeafNodes){
//        double first_term = Math.pow((QUERYAREA)/(DATASET_SIZE/FANOUT*AVGLEAFNODEAREA),numOfLeafNodes);
        double second_term = ((QUERYAREA/numOfLeafNodes)) / (SPACEAREA * (1.0-DATASET_SIZE*AVGOBJECTAREA/SPACEAREA));

        return second_term;
    }

    public double calculateModel() {
        double sum = 0;
        for (long i = 1; i <= L; i++) {
            double pro=(1.0/L)*calculateProbability(i);
            sum += pro;
        }

        return sum;
    }
    public static void main(String[] args) {

        FutileModel2 fm2 = new FutileModel2();

        double d=fm2.calculateModel();
        System.out.println(d);
    }
}
