import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/* *****************************************************************************
 *  Name:              Alaa Seada
 *  Coursera User ID:  20220130
 *  Last modified:     January 30, 2022
 **************************************************************************** */
public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trialsNum;
    private final double[] percolationThresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        trialsNum = trials;
        percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation trailSite = new Percolation(n);
            while (!trailSite.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                trailSite.open(row, col);
            }
            int openSites = trailSite.numberOfOpenSites();
            percolationThresholds[i] = openSites / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }


    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialsNum)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trialsNum)));
    }

    // validate n and trials
    private void validate(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials have to be > 0.");
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length == 2) {
            int n = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);
            PercolationStats caseStats = new PercolationStats(n, trials);
            System.out.println("mean\t\t\t\t= " + caseStats.mean());
            System.out.println("stddev\t\t\t\t= " + caseStats.stddev());
            System.out.println(
                    "95% confidence interval\t\t\t\t= [" + caseStats.confidenceLo() + ", "
                            + caseStats.confidenceHi() + "]");
        }


    }
}
