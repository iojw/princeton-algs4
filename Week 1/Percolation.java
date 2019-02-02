import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] opened;
    private int openedCount;
    private final int n, topNode, bottomNode;
    private final int[] searchOffsets;
    // ufA contains virtual top and bottom nodes, ufB contains just the top node - to tackle the backwash problem
    private final WeightedQuickUnionUF ufA, ufB;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        ufA = new WeightedQuickUnionUF(n * n + 2);
        ufB = new WeightedQuickUnionUF(n * n + 1);
        opened = new boolean[n * n + 2];

        topNode = n*n;
        bottomNode = n*n + 1;
        searchOffsets = new int[] {n, -n, 1, -1};
    }

    public int numberOfOpenSites() {
        return openedCount;
    }

    public boolean isOpen(int row, int col) {
        int index = getIndex(row, col);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        return opened[index];
    }

    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        return opened[index] && ufB.connected(topNode, index);
    }

    public void open(int row, int col) {
        int index = getIndex(row, col);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        if (!opened[index]) {
            opened[index] = true;
            openedCount++;
            if (index < n) {
                ufA.union(index, topNode);
                ufB.union(index, topNode);
            }
            if (index < n * n && index >= n * n - n) {
                ufA.union(index, bottomNode);
            }
            // Searching above, below, left and right of index
            for (int offset: searchOffsets) {
                int searchIndex = index + offset;
                if ((offset == -1 && col == 1) || (offset == 1 && col == n) || searchIndex < 0 || searchIndex >= n * n) {
                    continue;
                }
                if (opened[searchIndex]) {
                    ufA.union(searchIndex, index);
                    ufB.union(searchIndex, index);
                }
            }
        }
    }

    public boolean percolates() {
        return ufA.connected(topNode, bottomNode);
    }

    private int getIndex(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            return -1;
        }
        else {
            return n *(row-1)+(col-1);
        }
    }
}
