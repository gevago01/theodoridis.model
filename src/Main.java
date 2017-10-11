public class Main {

    //number of entries in a node
    private final static int f=40;
    //dimensionality
    private final static int n=2;
    //number of objects in the dataset
    private final static int N= (int) Math.pow(10,6);
    //extent of query at each dimension
    private final static double q_i=1 * Math.pow(10,-3);

    private int series_to;

    public int logOfBase(int base, double num){
        double to=Math.ceil(Math.log(num)/Math.log(base));
        return (int)to;

    }
    Main(){
        series_to=1+logOfBase(f,N/f);
        System.out.println("f:"+f);
        System.out.println("n:"+n);
        System.out.println("N:"+N);
        System.out.println("q_i:"+q_i);
    }



    public double calculateModel(){
        double first_term = N/f, sum=0;

        for (int i = 0; i < series_to; i++) {
            double p=1;
            for (int j = 0; j < n; j++) {
                p*=(q_i);
            }
            sum+=(first_term*p);
        }

        return 1+sum;
    }
    public static void main(String[] args) {
        Main main=new Main();

        double ret=main.calculateModel();
        System.out.printf("%.5f",ret);
    }
}
