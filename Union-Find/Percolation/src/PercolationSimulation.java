import java.util.Random;

/**
 * Models a percolation system using an N-by-N grid.
 */
public class PercolationSimulation {
    private boolean[][] grid; // grid[row][col] = is the site open?
    private int size;
    private QuickUnionDS uf;
    private int virtualTop;    // Virtual node connected to all top-row sites
    private int virtualBottom; // Virtual node connected to all bottom-row sites

    public PercolationSimulation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.size = n;
        this.grid = new boolean[n][n];
        
        // n*n grid sites + 2 virtual sites
        this.uf = new QuickUnionDS(n * n + 2); 
        this.virtualTop = n * n;
        this.virtualBottom = n * n + 1;
    }

    /**
     * Converts 2D grid coordinates to a 1D array index.
     */
    private int getIndex(int row, int col) {
        return row * size + col;
    }

    /**
     * Opens a site and connects it with adjacent open sites.
     */
    public void open(int row, int col) {
        if (grid[row][col]) return;
        grid[row][col] = true;
        int current = getIndex(row, col);

        // Link to virtual top if in the first row
        if (row == 0) uf.union(current, virtualTop);
        // Link to virtual bottom if in the last row
        if (row == size - 1) uf.union(current, virtualBottom);

        // Check and link with neighbors: up, down, left, right
        int[][] neighbors = {{row-1, col}, {row+1, col}, {row, col-1}, {row, col+1}};
        for (int[] next : neighbors) {
            int r = next[0], c = next[1];
            if (r >= 0 && r < size && c >= 0 && c < size && grid[r][c]) {
                uf.union(current, getIndex(r, c));
            }
        }
    }

    /**
     * Checks if the system percolates (connection from top to bottom).
     */
    public boolean percolates() {
        return uf.connected(virtualTop, virtualBottom);
    }

    public static void main(String[] args) {
        int n = 20; // Grid size
        PercolationSimulation sim = new PercolationSimulation(n);
        Random rand = new Random();
        int openedSites = 0;

        // Run Monte Carlo simulation
        while (!sim.percolates()) {
            int r = rand.nextInt(n);
            int c = rand.nextInt(n);
            if (!sim.grid[r][c]) {
                sim.open(r, c);
                openedSites++;
            }
        }

        double threshold = (double) openedSites / (n * n);
        System.out.println("System percolates!");
        System.out.println("Total opened sites: " + openedSites);
        System.out.println("Percolation threshold estimate: " + threshold);
    }
}