import java.util.Scanner;
import java.util.*;

/**
 * This program counts the coefficient of percolation
 * for n * n - cells vessel. One cell is open or closed.
 * If the cell is open, there are a liquid can percolates
 * throw it from top to bottom. With one step, one random
 * cell is opened. If any bottom cell is full of liquid
 * then the system is percolates.
 */
public class Percolation
{
    private int[][] size;
    private ArrayList<Integer>[][] id;
    private boolean[][] isOpen;
    int topCell;
    int bottomCell;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        size = new int[n + 2][n + 2];
        id = new ArrayList[n + 2][n + 2];
        isOpen = new boolean[n + 2][n + 2];
        topCell = n;
        bottomCell = n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                size[i][j] = 1;
                id[i][j] = new ArrayList<>();
                id[i][j].add(i);
                id[i][j].add(j);
                isOpen[i][j] = false;
            }
        }
        id[n][n] = new ArrayList<>();
        id[n][n].add(topCell);
        id[n][n].add(topCell);
        id[n + 1][n + 1] = new ArrayList<>();
        id[n + 1][n + 1].add(bottomCell);
        id[n + 1][n + 1].add(bottomCell);
        size[n][n] = 1;
        size[n + 1][n + 1] = 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        isOpen[row][col] = true;
        if (row == 0) {
            unionSites(row, col, topCell, topCell);
        } else if (isOpen(row - 1, col)) {
            unionSites(row, col, row - 1, col);
        }
        if (row == id.length - 3) {
            unionSites(row, col, bottomCell, bottomCell);
        } else if (isOpen(row + 1, col)) {
                unionSites(row, col, row + 1, col);
        }
        if (col != 0 && isOpen(row, col - 1)){
            unionSites(row, col, row, col - 1);
        }
        if (col != id.length - 3 && isOpen(row, col + 1)){
            unionSites(row, col, row, col + 1);
        }
    }

    private void unionSites(int row1, int col1, int row2, int col2){
        int[] r1 = root(row1, col1);
        int i1 = r1[0];
        int j1 = r1[1];

        int[] r2 = root(row2, col2);
        int i2 = r2[0];
        int j2 = r2[1];

        if (i1 == i2 && j1 == j2){
            return;
        }
        if (size[i1][j1] < size[i2][j2]){
            id[i1][j1].set(0, i2);
            id[i1][j1].set(1, j2);
            size[i2][j2] += size[i1][j1];
        }
        else {
            id[i2][j2].set(0, i1);
            id[i2][j2].set(1, j1);
            size[i1][j1] += size[i2][j2];
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        return isOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        return (root(row, col)[0] == root(topCell, topCell)[0] && root(row, col)[1] == root(topCell, topCell)[1]);
    }

    private int[] root(int row, int col) {
        int[] r = new int[2];
        while (row != id[row][col].get(0) && col != id[row][col].get(1)) {
            int row0 = id[row][col].get(0);
            int col0 = id[row][col].get(1);
            id[row][col].set(0, id[row0][col0].get(0));
            id[row][col].set(1, id[row0][col0].get(1));
            int row1 = id[row][col].get(0);
            col = id[row][col].get(1);
            row = row1;
        }
        r[0] = id[row][col].get(0);//row;
        r[1] = id[row][col].get(1);//col;
        return r;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        int count = 0;
        for (int i = 0; i < isOpen.length - 2; i++){
            for (int j = 0; j < isOpen.length - 2; j++){
                if (isOpen[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates(){
        return isFull(bottomCell, bottomCell);
    }

    // test client (optional)
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = 0;
        double coefficient = -1.0;
        while (n <= 0) {
            System.out.println("Input the number of cells in row (column):");
            var s = sc.next();
            try{
                n = Integer.parseInt(s);
            }
            catch(Throwable t){
                System.out.println("Error! Wrong number!");
                continue;
            }
            if (n <= 0) {
                System.out.println("The number must be more than zero!");
            }
        }
        sc.close();

        Percolation percolation = new Percolation(n);
        System.out.println("The initialization is complete");
        int i = 0;
        while (!percolation.percolates()) {
            int row = (int) (Math.random() * n);
            int col = (int) (Math.random() * n);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
            }
            i++;
        }
        double coefficientOfPercolation = ((double)percolation.numberOfOpenSites())/(n * n);
        System.out.println("The coefficient of percolation is " + coefficientOfPercolation);
    }
}
