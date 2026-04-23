import java.util.Arrays;
import java.util.Random;

/**
 * PercolationStats runs Monte Carlo simulations to estimate the percolation threshold.
 */
public class PercolationStats {
    private double[] thresholds;
    private int trials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.trials = trials;
        this.thresholds = new double[trials];
        Random rand = new Random();

        for (int i = 0; i < trials; i++) {
            PercolationSimulation sim = new PercolationSimulation(n);
            int openedSites = 0;
            while (!sim.percolates()) {
                int r = rand.nextInt(n);
                int c = rand.nextInt(n);
                if (!sim.isOpen(r, c)) { // Assuming you added an isOpen method or access
                    sim.open(r, c);
                    openedSites++;
                }
            }
            thresholds[i] = (double) openedSites / (n * n);
        }
    }

    public double mean() {
        double sum = 0;
        for (double t : thresholds) sum += t;
        return sum / trials;
    }

    public static void main(String[] args) {
        int n = 20;
        int t = 100; // Run 100 experiments
        PercolationStats stats = new PercolationStats(n, t);
        System.out.println("Mean threshold for " + t + " trials: " + stats.mean());
    }
}