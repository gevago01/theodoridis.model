import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ge14 on 24/11/2017.
 */
public class Theodoridis {

    //
    private static final double TDISK = 0.054527;
    private static Map<String, Double> objectsDensities = new HashMap<>();

    static {
        objectsDensities.put("abs02", 0.69);
        objectsDensities.put("bit02", 0.0);
        objectsDensities.put("dia02", 0.000002);
        objectsDensities.put("par02", 0.4492);
        objectsDensities.put("ped02", 0.0);
        objectsDensities.put("pha02", 0.0);
        objectsDensities.put("rea02", 0.1216);
        objectsDensities.put("uni02", 0.0);
        objectsDensities.put("uniform02", 0.1002);
        objectsDensities.put("fake02", 0.0001);

        objectsDensities.put("abs03", 0.69);
        objectsDensities.put("bit03", 0.0);
        objectsDensities.put("dia03", 0.0);
        objectsDensities.put("par03", 0.1495);
        objectsDensities.put("ped03", 0.0);
        objectsDensities.put("pha03", 0.0);
        objectsDensities.put("rea03", 0.0);
        objectsDensities.put("uni03", 0.0);
        objectsDensities.put("uniform03", 0.103);
        objectsDensities.put("myreal03", 0.0);
    }


    /********************change these to get the results******************/
    private final static String DATASET = "rea02";
    private final static int DIMENSIONALITY = 2;
    private static double queryArea = 1e-3;
    private final static double DATASET_SIZE = 1888012;
    //dataset size is 10^6 unless the dataset is any of the following:
    //PCL   45320389
    //par02 1048576
    //ped02 1034621
    //pha02 1025397
    //rea02 1888012
    private static double spaceArea = 10;
//    private static double spaceArea =25016536423895D ;
    //space area is 1 unless the dataset is any of the following:
    //par02 1.11
    //ped02 0.998
    //pha 1.16
    //rea02 10

   //abs03  1.012048064
   //bit03  0.997002999
   //dia03  1
   //par03  3.29184
   //ped03  0.990032964
   //pha03  2.211125
   //rea03  1060.51887
   //uni03  0.96833979

    //abs03 1000000
    //bit03 1000000
    //dia03 1000000
    //par03 1048576
    //ped03 1032323
    //pha03 1032554
    //rea03 11958999
    //uni03 1000000

    /************************************************************************/
    private final static int FANOUT = 70;
    private final static double objectDensity = objectsDensities.get(DATASET);
    private final static double leafNodeDensity = Math.pow((1 + ((Math.pow(objectDensity, 1.0 / DIMENSIONALITY) - 1) / (Math.pow(FANOUT, 1.0 / DIMENSIONALITY)))), DIMENSIONALITY);
    private final static int HEIGHT = 1 + (int) Math.ceil(Math.log(DATASET_SIZE / FANOUT) / Math.log(FANOUT));
    private static double queryExtent_i = Math.pow(queryArea, 1.0 / DIMENSIONALITY);
    private static double leafNodeArea = leafNodeDensity * FANOUT / DATASET_SIZE;
    private static int numberOfLeafNodes = (int) Math.ceil(DATASET_SIZE / FANOUT);
    private double ln_extent_i = Math.pow(leafNodeArea, 1.0 / DIMENSIONALITY);
    private static double leafNodeExtent_i = Math.pow(leafNodeArea, 1.0 / DIMENSIONALITY);
    private static double avgObjectArea = objectDensity * spaceArea / DATASET_SIZE;
    private static double spaceExtent = Math.pow(spaceArea, 1.0 / DIMENSIONALITY);

    public double predictLeafNodeAccesses() {
        return numberOfLeafNodes * Math.pow((ln_extent_i + queryExtent_i), DIMENSIONALITY) / spaceArea;
    }




    public static void main(String[] args) {


        Theodoridis model = new Theodoridis();

        System.out.println(model.predictLeafNodeAccesses());


    }
}
