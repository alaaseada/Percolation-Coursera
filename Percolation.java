/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final int gridDim, topSite, bottomSite;
    private final WeightedQuickUnionUF wqu;
    private int openSitesCount;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("The site dimension cannot be <=0.");
        gridDim = n;
        grid = new boolean[n][n];
        wqu = new WeightedQuickUnionUF(n * n + 2);
        openSitesCount = 0;
        topSite = n * n;
        bottomSite = n * n + 1;
    }

    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) return;

        grid[row - 1][col - 1] = true;
        openSitesCount++;

        int flatIndex = getFlatIndex(row, col);

        if (row == 1) {
            wqu.union(getFlatIndex(row, col), topSite);
        }
        if (row == gridDim) {
            wqu.union(flatIndex, bottomSite);
        }
        if (isValid(row, (col - 1)) && isOpen(row, (col - 1))) {
            wqu.union(getFlatIndex(row, (col - 1)), flatIndex);
        }
        if (isValid(row, (col + 1)) && isOpen(row, (col + 1))) {
            wqu.union(getFlatIndex(row, (col + 1)), flatIndex);
        }
        if (isValid((row - 1), col) && isOpen((row - 1), col)) {
            wqu.union(getFlatIndex((row - 1), col), flatIndex);
        }
        if (isValid((row + 1), col) && isOpen((row + 1), col)) {
            wqu.union(getFlatIndex((row + 1), col), flatIndex);
        }
    }

    private int getFlatIndex(int row, int col) {
        int zeroBasedRow = row - 1;
        int zeroBasedCol = col - 1;
        return gridDim * zeroBasedRow + zeroBasedCol;
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    private void validate(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid indexes.");
        }
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return wqu.find(getFlatIndex(row, col)) == wqu.find(topSite);
    }

    private boolean isValid(int row, int col) {
        int zeroBasedRow = row - 1;
        int zeroBasedColumn = col - 1;
        return (zeroBasedRow >= 0 && zeroBasedRow < gridDim && zeroBasedColumn >= 0
                && zeroBasedColumn < gridDim);
    }

    public int getOpenSiteCount() {
        return openSitesCount;
    }

    public boolean percolates() {
        return wqu.find(topSite) == wqu.find(bottomSite);
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation newSystem = new Percolation(n);
        while (!newSystem.percolates()) {
            int selectedRow = StdRandom.uniform(1, n + 1);
            int selectedCol = StdRandom.uniform(1, n + 1);
            newSystem.open(selectedRow, selectedCol);
            System.out.println(
                    "Site (" + selectedRow + "," + selectedCol + ") has been opened, Is it Full? "
                            + newSystem
                            .isFull(selectedRow, selectedCol));
        }
        System.out.println("The number of opened sites = " + newSystem.getOpenSiteCount());
        System.out.println("Does the system percolate? " + newSystem.percolates());
    }
}
