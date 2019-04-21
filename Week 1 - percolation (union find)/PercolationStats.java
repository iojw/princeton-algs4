import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double mean, stddev, confidenceLo, confidenceHi;

    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        double[] thresholds = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation trial = new Percolation(n);
            while (!trial.percolates()) {
                int openRow = StdRandom.uniform(1, n+1);
                int openCol = StdRandom.uniform(1, n+1);
                trial.open(openRow, openCol);
            }
            thresholds[i] = (double) trial.numberOfOpenSites()/(double) (n*n);
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        confidenceLo = mean - (CONFIDENCE_95*stddev)/Math.sqrt(t);
        confidenceHi = mean + (CONFIDENCE_95*stddev)/Math.sqrt(t);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t);
        StdOut.println(String.format("%-25s = %f", "mean", stats.mean()));
        StdOut.println(String.format("%-25s = %f", "stddev", stats.stddev()));
        StdOut.println(String.format("%-25s = [%f, %f]", "95% confidence interval", stats.confidenceLo(), stats.confidenceHi()));
    }
}
