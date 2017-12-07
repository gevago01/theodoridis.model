import java.util.HashMap;
import java.util.Map;

/**
 * Created by ge14 on 24/11/2017.
 */
public class FutileModel {

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
    private final static int DIMENSIONALITY = 2;
    private final static double DATASET_SIZE = 1e6;
    private static double queryArea = 5e-6;
    /************************************************************************/
    private final static int FANOUT = 70;
    private static long L;

    private static double queryExtent_i = Math.pow(queryArea,1.0/DIMENSIONALITY);
    private static double leafNodeArea= leafNodeDensity * FANOUT / DATASET_SIZE;
    private static double leafNodeExtent_i = Math.pow(leafNodeArea,1.0/DIMENSIONALITY);
    FutileModel() {
        L = (int)Math.pow(Math.ceil(1+queryExtent_i/leafNodeExtent_i),DIMENSIONALITY);
//        System.out.println("L is:"+L);
    }



    public double calculateModel() {
        double sum = 0;
        for (long i = 1; i <= L; i++) {
//            System.out.println(calculateProbability(i));
            double pro=(1.0/L)*calculateProbability(i);
//            System.out.println(pro);
//            if (!(Double.isNaN(pro) || pro>1 || pro<0)) {
                sum += pro;

//            }
        }

        return sum;
    }

    private double calculateProbability(long numOfLeafNodes) {
//        System.out.println(FANOUT + " AND "+numOfLeafNodes+" gives "+((double)FANOUT / numOfLeafNodes));
//        System.out.println(queryArea+ " AND "+numOfLeafNodes+" gives "+(queryArea / numOfLeafNodes));
        double d=Math.pow((1 - ((queryArea / numOfLeafNodes) / (leafNodeDensity * FANOUT / DATASET_SIZE))), ((double)FANOUT / numOfLeafNodes));
//        if (Double.isNaN(d)) {
//            System.out.println("-------------------");
//            System.out.println(queryArea);
//            System.out.println(numOfLeafNodes);
//            System.out.println(leafNodeDensity);
//            System.out.println(FANOUT);
//            System.out.println(DATASET_SIZE);
//            System.out.println("-------------------");
//        }
        return Math.pow((1 - ((queryArea / numOfLeafNodes) / (leafNodeDensity * FANOUT / DATASET_SIZE))), ((double)FANOUT / numOfLeafNodes));

    }

    public static void main(String[] args) {
        FutileModel mm=new FutileModel();
        double ds_prob = mm.calculateModel();
        System.out.println(ds_prob);


    }
}
