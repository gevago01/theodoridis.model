import java.util.Map;
import java.util.TreeMap;

public class Main {

    //number of entries in a node
    private final static int f = 40;
    //dimensionality
    private final static int n = 2;
    //number of objects in the dataset
    private final static int N = (int) Math.pow(10, 6);
    //extent of query at each dimension
//    //abs02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
//    private final static double q_i = 2 * Math.pow(10, -2);//w2
//    //bit02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
//    private final static double q_i = 2 * Math.pow(10, -2);//w2
//    //dia02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
//    private final static double q_i = 2 * Math.pow(10, -2);//w2
//    //par02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
//    private final static double q_i = 2 * Math.pow(10, -2);//w2
//    //ped02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
//    private final static double q_i = 2 * Math.pow(10, -2);//w2
//    //pha02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
//    private final static double q_i = 2 * Math.pow(10, -2);//w2
//    //rea02
//    private final static double q_i = 1.4 * Math.pow(10, -3);//w0
//    private final static double q_i = 4.6 * Math.pow(10, -3);//w1
//    private final static double q_i = 1.4 * Math.pow(10, -2);//w2
//    //uni02
//    private final static double q_i = 2 * Math.pow(10, -3);//w0
//    private final static double q_i = 6 * Math.pow(10, -3);//w1
    private final static double q_i = 2 * Math.pow(10, -2);//w2
    private static Map<String,Double> dataset_root_densities = new TreeMap<>();
    static {
        dataset_root_densities.put("abs02",0.69951);
        dataset_root_densities.put("bit02",0.0);
        dataset_root_densities.put("dia02",0.000002);
        dataset_root_densities.put("par02",0.449276);
        dataset_root_densities.put("ped02",0.0);
        dataset_root_densities.put("pha02",0.0);
        dataset_root_densities.put("rea02",0.1216);
        dataset_root_densities.put("uni02",0.0);
    }

    private int series_to;

    private Map<Integer, Double> densities = new TreeMap<>();

    private final static double ROOT_DENSITY = dataset_root_densities.get("uni02") ;

    public int logOfBase(int base, double num) {
        double to = Math.ceil(Math.log(num) / Math.log(base));
        return (int) to;

    }

    Main() {
        series_to = 1 + logOfBase(f, N / f);
        calculateDensitiesAtEachLevel();
        System.out.println("f:" + f);
        System.out.println("n:" + n);
        System.out.println("N:" + N);
        System.out.println("q_i:" + q_i);
    }

    /**
     * Calculates the density at level 0 and then at each level.
     */
    public void calculateDensitiesAtEachLevel() {

        densities.put(0, ROOT_DENSITY);
        for (int i = 1; i < series_to; i++) {
            double density_at_i = 1 + ((Math.pow(densities.get(i - 1), 1.0 / n) - 1) / Math.pow(f, 1.0 / n));
            densities.put(i,density_at_i);
        }

    }


    public double calculateModel() {
        double first_term = N / f, sum = 0;

        for (int j = 0; j < series_to; j++) {
            double p = 1;
            for (int i = 0; i < n; i++) {
                double product_term = Math.pow((densities.get(j)*f/N), 1.0/n);
                p *= (product_term + q_i);
            }
            sum += (first_term * p);
        }

        return 1 + sum;
    }

    public static void main(String[] args) {
        Main main = new Main();

        double ret = main.calculateModel();
        System.out.printf("%.5f", ret);
    }
}
