import java.util.HashMap;
import java.util.Map;

/**
 * Created by ge14 on 24/11/2017.
 */
public class FutileModel {

    private static final double TDISK = 0.054527;
    private static final double TCELL = 100e-9;//40 ~ 100 ns
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
        objectsDensities.put("uniform02", 0.0101);

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


    /********************change these to get the probability******************/
    private final static String DATASET = "par02";
    private final static int DIMENSIONALITY = 2;
    private static double queryArea = 1e-4;
    private final static double DATASET_SIZE = 1e6;
    //dataset size is 10^6 unless the dataset is any of the following:
    //par02 1048576
    //ped02 1034621
    //pha02 1025397
    private static double spaceArea = 1;
    //space area is 1 unless the dataset is any of the following:
    //par02 1.11
    //ped02 0.998
    //pha 1.16
    /************************************************************************/
    private final static int FANOUT = 70;
    private final static double objectDensity = objectsDensities.get(DATASET);
    private final static double leafNodeDensity = Math.pow((1 + ((Math.pow(objectDensity, 1.0 / DIMENSIONALITY) - 1) / (Math.pow(FANOUT, 1.0 / DIMENSIONALITY)))), DIMENSIONALITY);
    private double g = -1;
    private final static int HEIGHT = 1 + (int) Math.ceil(Math.log(DATASET_SIZE / FANOUT) / Math.log(FANOUT));
    private static double queryExtent_i = Math.pow(queryArea, 1.0 / DIMENSIONALITY);
    private static double leafNodeArea = leafNodeDensity * FANOUT / DATASET_SIZE;
    private int numberOfLeafNodes = (int) Math.ceil(DATASET_SIZE / FANOUT);
    private double ln_extent_i = Math.pow(leafNodeArea, 1.0 / DIMENSIONALITY);
    private static double leafNodeExtent_i = Math.pow(leafNodeArea, 1.0 / DIMENSIONALITY);


    public double predictLeafNodeAccesses() {
        return numberOfLeafNodes * Math.pow((ln_extent_i + Math.pow(queryArea, 1.0 / DIMENSIONALITY)), DIMENSIONALITY) / spaceArea;
    }


    public double predictFutileLNAccesses() {
        return numberOfLeafNodes * Math.pow((ln_extent_i + Math.pow(retrieveEmptyArea(), 1.0 / DIMENSIONALITY)), DIMENSIONALITY) / spaceArea;
    }


    FutileModel(int resolution) {
        g = resolution;
    }

    public double cellAccesses() {

        return Math.pow((Math.pow(spaceArea, 1.0 / DIMENSIONALITY) / g + queryExtent_i), DIMENSIONALITY) * Math.pow(g, DIMENSIONALITY) / spaceArea;
    }

    /**
     * Double checked
     *
     * @return empty area of the query
     */
    public double retrieveEmptyArea() {
        return (1.0 - objectDensity) * spaceArea / Math.pow(g, DIMENSIONALITY) * cellAccesses();
    }


    public static void main(String[] args) {
        double last = Double.MAX_VALUE;
        for (int i = 1000; i < 20e3; i += 1000) {
            FutileModel mm = new FutileModel(i);
            //predict the total leaf node accesses including futile and non futile
            double totalLA = mm.predictLeafNodeAccesses();
            //predict the FUTILE leaf node accesses
            double futile = mm.predictFutileLNAccesses();
            //(totalLA - futile) < 0 happens with point datasets
            //if ((totalLA) < futile) {
            //System.err.println("NOT POSSIBLE");
            //System.exit(1);
            //}

            double disk_time = (totalLA - futile) * TDISK;
            //System.out.println("Useful Accesses:" + (totalLA - futile));
            double grid_time = mm.cellAccesses() * TCELL;
            double access_time = disk_time + (HEIGHT) * grid_time;
            if (access_time <= last) {
                System.out.printf("\n" + i + " %.10f", access_time);
            } else {
                System.out.printf("\n--" + i + ": %.10f",access_time);
            }


            last = access_time;
        }

    }
}
