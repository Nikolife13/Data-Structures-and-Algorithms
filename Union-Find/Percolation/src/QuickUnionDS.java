/**
 * Optimized Union-Find implementation using Weighted Quick Union 
 * and Path Compression.
 */
public class QuickUnionDS {
    private int[] parent; // parent[i] = parent of i
    private int[] sz;     // sz[i] = number of objects in tree rooted at i
    private int count;    // number of components

    /**
     * Initializes an empty union-find data structure with n sites.
     * @param n the number of sites
     */
    public QuickUnionDS(int n) {
        count = n;
        parent = new int[n];
        sz = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; // Each node is its own root initially
            sz[i] = 1;     // Each tree starts with size 1
        }
    }

    /**
     * Returns the root of the component containing site i.
     * Implements path compression to keep trees flat.
     */
    public int find(int i) {
        while (i != parent[i]) {
            // Path Compression: Make every other node in the path 
            // point to its grandparent.
            parent[i] = parent[parent[i]]; 
            i = parent[i];
        }
        return i;
    }

    /**
     * Returns true if the two sites are in the same component.
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the components containing site p and site q.
     * Uses weighting to ensure the smaller tree is linked to the larger tree.
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // Make smaller tree point to larger tree
        if (sz[rootP] < sz[rootQ]) {
            parent[rootP] = rootQ;
            sz[rootQ] += sz[rootP];
        } else {
            parent[rootQ] = rootP;
            sz[rootP] += sz[rootQ];
        }
        count--;
    }
}