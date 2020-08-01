package ru.algorithms.countInversions;

import java.io.*;
/**
 * This program counts the number of inversions using O(n*ln(n)) algorithm of merge sorting.
 * If in massive A[i] i < j and A[i] > A[j] - this is called inversion.
 * It reads the massive of integer numbers from file "input.txt"
 * and output the number of inversions into the file "output.txt".
 */
public class Program {

    private int[] main;
    private int countInversions = 0;

    public static void main (String [] args){
        System.out.println("Reading massive from file...");
        Program program = new Program();
        program.readFromInput();
        System.out.println("Data have been reading correctly");
        System.out.println("Counting inversions...");
        program.countInversions(0, program.main.length - 1);
        program.writeToOutput();
        System.out.println("The number of inversions has been counting and writing into output.txt");
    }

    private void readFromInput(){
        File dir = new File(System.getProperty("user.dir"));
        String filename = dir.toString() + "\\input.txt";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
            String str = new String();

            str = br.readLine();

            String[] strData = str.split(" ");
            main = new int[strData.length];

            for (int i = 0; i < strData.length; i++) {
                main[i] = Integer.parseInt(strData[i]);
            }
            br.close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Incorrect input data");
            Runtime.getRuntime().exit(0);
        }
    }

    private void writeToOutput(){
        File dir = new File(System.getProperty("user.dir"));
        String filename = dir.toString() + "\\output.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(((Integer)countInversions).toString());
            bw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void countInversions(int start, int end){
        int middle = (start + end) / 2;
        if (middle > start){
            countInversions(start, middle);
            countInversions(middle + 1, end);
        }
        mergeSort(start, middle, end);
    }

    //Merge two sorted fragments of massive main: one from p to q and other from q + 1 to s
    private void mergeSort(int p, int q, int s){

        // left sorted fragment of massive main
        int[] partLeft = new int[q - p + 2];
        // right sorted fragment of massive main
        int[] partRight = new int[s - q + 1];

        // fill partLeft and partRight with values
        for (int i = p; i < q + 1; i++){
            partLeft[i - p] = main[i];
        }
        for (int i = q + 1; i < s + 1; i++){
            partRight[i - q - 1] = main[i];
        }

        // index of observing partLeft
        int l = 0;
        // index of observing partRight
        int r = 0;
        // marker the bound of left and right parts
        partLeft[q - p + 1] = Integer.MAX_VALUE;
        partRight[s - q] = Integer.MAX_VALUE;
        // walk throw left and right parts in the same time
        while (l <= q - p && r <= s - q)
        {
            // if the value in left part is minimum, then there are no inversions
            if (partLeft[l] <= partRight[r]){
                main[p + l + r] = partLeft[l];
                l++;
            }
            // if the value in right part is minimum, there are so many inversions
            // as unwatched elements in left part
            else {
                main[p + l + r] = partRight[r];
                countInversions += q - p - l + 1;
                r++;
            }
        }
    }
}