import java.util.Map;
import java.util.TreeMap;

public class Main {

    //where the loop terminates
    private int series_to;
    //holds the density at each level
    private Map<Integer, Double> densities = new TreeMap<>();
    //number of entries in a node
    private final static int f = 40;
    //dimensionality
    private final static int n = 2;
    //number of objects in the dataset
    private final static int N = (int) Math.pow(10, 6);
    //extent of query at each dimension
//    //abs02
//    private final static double q_i = 1 * Math.pow(10, -3);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 1 * Math.pow(10, -2);//w2
//    //bit02
//    private final static double q_i = 9 * Math.pow(10, -4);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 1 * Math.pow(10, -2);//w2
//    //dia02
//    private final static double q_i = 1 * Math.pow(10, -3);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 9 * Math.pow(10, -3);//w2
//    //par02
//    private final static double q_i = 9 * Math.pow(10, -4);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 9 * Math.pow(10, -3);//w2
//    //ped02
//    private final static double q_i = 9 * Math.pow(10, -4);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 9 * Math.pow(10, -3);//w2
//    //pha02
//    private final static double q_i = 9 * Math.pow(10, -4);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 9 * Math.pow(10, -3);//w2
//    //rea02
//    private final static double q_i = 7 * Math.pow(10, -4);//w0
//    private final static double q_i = 2 * Math.pow(10, -3);//w1
    private final static double q_i = 7 * Math.pow(10, -3);//w2
//    //uni02
//    private final static double q_i = 1 * Math.pow(10, -3);//w0
//    private final static double q_i = 3 * Math.pow(10, -3);//w1
//    private final static double q_i = 1 * Math.pow(10, -2);//w2
    private static Map<String, Double> dataset_root_densities = new TreeMap<>();

    static {
        dataset_root_densities.put("abs02", 0.69951);
        dataset_root_densities.put("bit02", 0.0);
        dataset_root_densities.put("dia02", 0.000002);
        dataset_root_densities.put("par02", 0.449276);
        dataset_root_densities.put("ped02", 0.0);
        dataset_root_densities.put("pha02", 0.0);
        dataset_root_densities.put("rea02", 0.1216);
        dataset_root_densities.put("uni02", 0.0);
    }

    private final static double ROOT_DENSITY = dataset_root_densities.get("rea02");

    /**
     * Calculates logarithm of a number with any base
     *
     * @param base
     * @param num
     * @return
     */
    public double logOfBase(int base, double num) {
        return Math.log(num) / Math.log(base);
    }

    /**
     * Returns the ceiling
     *
     * @param base
     * @param num
     * @return
     */
    public int myceil(int base, double num) {
        double to = Math.ceil(logOfBase(base, num));
        return (int) to;
    }

    Main() {
        series_to = 1 + myceil(f, N / f);
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
        //ROOT_DENSITY is a constant that
        // has to be defined in the class body
        densities.put(0, ROOT_DENSITY);
        for (int i = 1; i < series_to; i++) {
            double density_at_i = 1 + ((Math.pow(densities.get(i - 1), 1.0 / n) - 1) / Math.pow(f, 1.0 / n));
            densities.put(i, density_at_i);
        }

    }

    /**
     * Calculates theodoridi's model
     *
     * @return disk_accesses
     */
    public double calculateModel() {
        double first_term = N / f, sum = 0;

        for (int j = 0; j < series_to; j++) {
            double p = 1;
            for (int i = 0; i < n; i++) {
                double product_term = Math.pow((densities.get(j) * f / N), 1.0 / n);
                p *= (product_term + q_i);
            }
            sum += (first_term * p);
        }

        return 1 + sum;
    }



    public static void main(String[] args) {
        Main main = new Main();

//        double ret = main.calculateModel();
//        System.out.printf("Disk Accesses:%.5f", ret);


    }
}
