import java.util.HashMap;
import java.util.Map;

/**
 * Created by ge14 on 24/11/2017.
 */
public class FutileModel {

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
        objectsDensities.put("my02", 0.0101);

        objectsDensities.put("abs03", 0.69);
        objectsDensities.put("bit03", 0.0);
        objectsDensities.put("dia03", 0.0);
        objectsDensities.put("par03", 0.1495);
        objectsDensities.put("ped03", 0.0);
        objectsDensities.put("pha03", 0.0);
        objectsDensities.put("rea03", 0.0);
        objectsDensities.put("uni03", 0.0);
        objectsDensities.put("myreal03", 0.0);
    }


    /********************change these to get the probability******************/
    private final static String DATASET="uni02";
    private final static double objectDensity = objectsDensities.get(DATASET);
    private final static int FANOUT = 70;
    private final static int DIMENSIONALITY = 2;
    private final static double leafNodeDensity = Math.pow((1+((Math.pow(objectDensity,1.0/DIMENSIONALITY)-1)/(Math.pow(FANOUT,1.0/DIMENSIONALITY)))),DIMENSIONALITY);
    private final static double DATASET_SIZE = 1e6;
//    10000 for my
    private static double queryArea = 1e-3;
    private static double spaceArea = 1;
    private static double g = 1000;
    /************************************************************************/

    private static double queryExtent_i = Math.pow(queryArea, 1.0 / DIMENSIONALITY);
    private static double leafNodeArea= leafNodeDensity * FANOUT  / DATASET_SIZE;
//    private static double leafNodeArea= 3.5e-5;
    //    private static double leafNodeArea = 54.2 * spaceArea / Math.pow(g, DIMENSIONALITY);
    private static double leafNodeExtent_i = Math.pow(leafNodeArea, 1.0 / DIMENSIONALITY);
    private static long L = (int) Math.pow(Math.ceil(1 + queryExtent_i / leafNodeExtent_i), DIMENSIONALITY);



    public double predictLeafNodeAccesses(){
        int numberOfLeafNodes = (int)Math.ceil(DATASET_SIZE/FANOUT);

        double ln_extent_i = Math.pow(leafNodeArea,1.0/DIMENSIONALITY);
        return numberOfLeafNodes * Math.pow((ln_extent_i+queryExtent_i),DIMENSIONALITY)/ spaceArea;
    }



    FutileModel() {
        System.out.println("LeafNodeArea:");
        System.out.println(leafNodeArea);

//        L = (int) Math.pow(Math.ceil(1 + Math.sqrt(retrieveEmptyArea()) / leafNodeExtent_i), DIMENSIONALITY);
        System.out.println("L is:" + L);
    }


    public double calculateModel() {
        double sum = 0;
        for (long i = 1; i <= L; i++) {
//            System.out.println(calculateProbability(i));
            double pro = (1.0 / L) * calculateProbability(i);
//
//            double pro =  retrieveEmptyArea()/leafNodeArea;
//            System.out.println(pro);
//            if (!(Double.isNaN(pro) || pro>1 || pro<0)) {
            sum += pro;

//            }
        }
        return sum;
    }

    public double retrieveEmptyArea() {
        return (1.0 - objectDensity)  * Math.pow((Math.pow(spaceArea, 1.0 / DIMENSIONALITY)/g + queryExtent_i), DIMENSIONALITY);
    }

    public void printValues(){

        System.out.println("Leaf node density:"+leafNodeDensity);
        if (retrieveEmptyArea() > leafNodeArea){
            System.out.println("Query empty area is bigger than leaf node area");
        }
        else{
            System.out.println("Leaf node area is bigger than query empty area");
        }
        System.out.println("this:"+(retrieveEmptyArea()/L) / leafNodeArea);
    }

    private double calculateProbability(long numOfLeafNodes) {
//        System.out.println(FANOUT + " AND "+numOfLeafNodes+" gives "+((double)FANOUT / numOfLeafNodes));
//        System.out.println(queryArea+ " AND "+numOfLeafNodes+" gives "+(queryArea / numOfLeafNodes));
        double d = Math.pow((1 - ((retrieveEmptyArea() / numOfLeafNodes) / (leafNodeArea))), ((double) FANOUT / numOfLeafNodes));
        if (Double.isNaN(d)) {
            System.out.println("-------------------");
            System.out.println(queryArea);
            System.out.println(numOfLeafNodes);
            System.out.println(retrieveEmptyArea());
            System.out.println(leafNodeDensity);
            System.out.println(FANOUT);
            System.out.println(DATASET_SIZE);
            System.out.println("-------------------");
            System.exit(9);
        }
        return Math.pow((1 - ((retrieveEmptyArea() / numOfLeafNodes) / (leafNodeArea))), ((double) FANOUT / numOfLeafNodes));
    }

    public static void main(String[] args) {
        FutileModel mm = new FutileModel();
        double ds_prob = mm.calculateModel();
        double leafNodeAccesses = mm.predictLeafNodeAccesses();
        mm.printValues();
        System.out.println("Theodoridis:"+mm.predictLeafNodeAccesses());
        System.out.println(ds_prob);
//        System.out.println("Dead space probability:"+ds_prob);
//        System.out.println("# of leaf node accesses (Theodoridis):"+leafNodeAccesses);
//        System.out.println("Futile accesses per query:"+(leafNodeAccesses*ds_prob));


    }
}
