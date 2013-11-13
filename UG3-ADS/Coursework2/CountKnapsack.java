
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;

/**
 * An implementation of Knapsack Counting Algorithms
 */

public class CountKnapsack {

	private static Random generator = new Random(System.currentTimeMillis());

	/*
     * Generate random numbers
     */
    private static double randomGenerator() {
		return generator.nextDouble();
	}

    /** The method below will implement the (inefficient)
     * recursive algorithm of section 1 for exactly counting knapsack
     * solutions, which comes from a direct implementation
     * of the standard recurrence given in the cwk specification.
     **/
    public static long countKnapsackRecurse(int[] w, int B) {
    	int k = w.length;
    	if (k == 0)
    		return 1;
    	else {
    		int[] temp = new int[k-1];
    		System.arraycopy(w, 0, temp, 0, k-1);
    		if (k > 0 && w[k-1] > B)
    			return countKnapsackRecurse(temp, B);
    		else
    			return countKnapsackRecurse(temp, B) + countKnapsackRecurse(temp, B-w[k-1]);
    	}
    }

    /** The method below will implement the Theta(nB) time
     * dynamic programming algorithm to exactly count knapsack
     * solutions, presented in section 2 of cwk3.  The input
     * is a list of integers, representing the weights of the
     * items. The output is a count of the number of knapsack
     * solutions which obey the bound B.
     **/
    public static long countKnapsackDP(int[] w, int B) {
    	long[][] C = buildKnapsackTable(w, B);
    	return C[w.length][B];
    }

    /** You might decide to implement the method below, to
     * be used as the main building block of countKnapsackDP,
     * for the dynamic programming counting (and sampling)
     * algorithms of the assignment.  You don't have to implement
     * buildKnapsackTable as a separate method if you don't want
     * to, though.  */
    public static long[][] buildKnapsackTable (int[] w, int B) {
    	int n = w.length;
    	long[][] C = new long[n+1][B+1];
    	for (int b = 0; b <= B; b++) C[0][b] = 1;

    	for (int k = 0; k < n; k++) {
    		for (int b = 0; b <= B; b++) {
    			if (w[k] > b) C[k+1][b] = C[k][b];
    			else C[k+1][b] = C[k][b] + C[k][b-w[k]];
    		}
    	}
    	return C;
    }


    /** The method below will implement the algorithm which
     * uses the Theta(n^3) time (n+1)-approximation, together
     * with a sampling subroutine, to closely-approximate the
     * number of knapsack solutions, presented in section 3 of
     * the cwk specification.  The input is a list of integers,
     * representing the weights of the items. The output is a
     * count of the number of knapsack solutions which obey
     * the bound B.   I happened to have an extra 'helper'
     * method countKnapsackAppr (with slightly different
     * parameters), to control the amount of sampling.  You
     * may or may not have this.
     **/
    public static long countKnapsackApprox(int[] w, int B) {
    	int n = w.length;
    	int m = 10*n;
    	int l = 0;
    	int[] a = new int[n];
    	for (int i = 0; i < n; i++)
    		a[i] = (int) Math.floor((double) (n*n * w[i]) / B);
    	long[][] table = buildKnapsackTable(a, (int) n*n);

    	long count = countKnapsackDP(a, n*n);
    	for (int k = 0; k < m; k++) {
    		LinkedList<Integer> S = drawRandomSample(a, n, table);
    		if (testFeasibleSolution(S, B, w))
    			l += 1;
    	}
    	double p = (double) l / m;
    	return (int) Math.floor(p * count);
    }
    
    public static long countKnapsackApprox(int[] w, int B, int m) {
    	int n = w.length;
    	int l = 0;
    	int[] a = new int[n];
    	for (int i = 0; i < n; i++)
    		a[i] = (int) Math.floor((double) (n*n * w[i]) / B);
    	long[][] table = buildKnapsackTable(a, (int) n*n);

    	long count = countKnapsackDP(a, n*n);
    	for (int k = 0; k < m; k++) {
    		LinkedList<Integer> S = drawRandomSample(a, n, table);
    		if (testFeasibleSolution(S, B, w))
    			l += 1;
    	}
    	double p = (double) l / m;
    	return (int) Math.floor(p * count);
    }

    private static LinkedList<Integer> drawRandomSample(int[] a, int n, long[][] table) {
    	LinkedList<Integer> solutions = new LinkedList<Integer>();
    	int n2 = n*n;
    	int bound = n2;

    	for (int i = n; i > 0; i--) {
    		if (bound >= a[i-1]) {
	    		double ratio = ((double) table[i-1][bound - a[i-1]]) / table[i][bound];
	    		double random = randomGenerator();
	    		if (ratio <= random) {
	    			solutions.add(i);
	    			bound = bound - a[i-1];
	    		}
    		}
    	}
    	return solutions;
    }

    /*
     * Test for a feasible solution.
     */
    public static boolean testFeasibleSolution(LinkedList<Integer> S, long B, int[] w) {
    	long sum = 0;
    	for (Integer i : S) sum += w[i-1];
    	return sum <= B;
    }


  /** Below is a helper method (for the testing phase) to read the list
   * of words in a given file "fileName" and return these words in order
   * as a list of strings. */

    public static int[] readIntsFile(String fileName) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    try {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext())
        list.add(Integer.parseInt(scanner.next()));
        scanner.close();
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
       return unboxInts(list);
    }

    public static int[] unboxInts(ArrayList<Integer> list) {
    	int[] result = new int[list.size()];
    	for (int i = 0; i < result.length; i++)
    		result[i] = list.get(i);
    	return result;
    }

    
    /* ******************************************************
     * *********** TIMING AND ACCURACY TESTS ****************
     * ****************************************************** */
    /*
     * Generate tests cases linearly. B = n
     */
    public static HashMap<Integer, int[]> generateLinearTests(int n) {
    	HashMap<Integer, int[]> tests = new HashMap<Integer, int[]>();
    	int[] w = new int[n];
    	for (int i = 0; i < n; i++) w[i] = i;
    	for (int i = 0; i < n; i++) {
    		int[] temp = new int[i];
    		System.arraycopy(w, 0, temp, 0, i);
    		tests.put(i, temp);
    	}
    	return tests;
    }
    
    /*
     * Time computation time for Recursive version
     */
    public static double timeCountKnapsackRecurse(int[] w, int B) {
    	long startTime = System.nanoTime();
    	long sol = countKnapsackRecurse(w, B);
    	long endTime = System.nanoTime();
    	return (double) (endTime - startTime) / Math.pow(10, 9);
    }
    
    /*
     * Time computation time for DP version
     */
    public static double timeCountKnapsackDP(int[] w, int B) {
    	long startTime = System.nanoTime();
    	long sol = countKnapsackDP(w, B);
    	long endTime = System.nanoTime();
    	return (double) (endTime - startTime) / Math.pow(10, 9);
    }
    
    /*
     * Time computation time for Approx version
     */
    public static double timeCountKnapsackApprox(int[] w, int B) {
    	long startTime = System.nanoTime();
    	long sol = countKnapsackApprox(w, B);
    	long endTime = System.nanoTime();
    	return (double) (endTime - startTime) / Math.pow(10, 9);
    }
    
    /*
     * Print computation times for the linear test set
     */
    public static void linearTimeTestRecurse(HashMap<Integer, int[]> tests) {
    	for (Integer i: tests.keySet())
    		System.out.println(timeCountKnapsackRecurse(tests.get(i), i));
    }
    
    /*
     * Print computation times for the linear test set
     */
    public static void linearTimeTestDP(HashMap<Integer, int[]> tests) {
    	for (Integer i: tests.keySet())
    		System.out.println(timeCountKnapsackDP(tests.get(i), i));
    }
    
    /*
     * Print computation times for the linear test set
     */
    public static void linearTimeTestApprox(HashMap<Integer, int[]> tests) {
    	for (Integer i: tests.keySet())
    		System.out.println(countKnapsackApprox(tests.get(i), i));
    }
    
    /*
     * Generate random weights for the backpack within low - high (exclusive)
     */  
    public static int[] generateRandomWeights(int size, int high, int low) {
    	Random r = new Random();
    	int[] samples = new int[size];
    	for (int i = 0; i < size; i++) 
    		samples[i] = r.nextInt(high-low) + low;
    	return samples;
    }
    
    /*
     * Return absolute error between DP and Approx
     */
    public static double testApproxAccuracy(int[] w, int B) {
    	long dp = countKnapsackDP(w, B);
    	long approx = countKnapsackApprox(w, B);
    		System.out.println(arrayToString(w) + "  B: " + B);
    		System.out.println("DP: " + dp + "      Approx: " + approx);
    	return Math.abs((dp - approx) / (double) dp) *100;
    }
    
    public static int[][] generateTestsBCloseToN2(int size) {
    	Random r = new Random();
    	int[][] tests = new int[size][r.nextInt(10)];
    	for (int i = 0; i < size; i++) {
    		int[] randomWeights = generateRandomWeights(r.nextInt(), 100, 0);
    		tests[i] = randomWeights;
    	}
    	return tests;
    }
    
    public static void printErrorsBCloseToN2(int size) {
    	int[][] tests = generateTestsBCloseToN2(size);
    	for (int i = 0; i < size; i++) {
    		int B = (int) (tests[i].length * tests[i].length * 1.15);
    		System.out.println(testApproxAccuracy(tests[i], B));
    	}
    }
    
    private static String arrayToString(int[] a) {
    	String i = "[";
    	for (int k = 0; k < a.length; k++) {
    		i += a[k] + ", ";
    	}
    	return i + "]";
    }
    


    public static void main(String[] args) {
    	Random r = new Random();
//    	LinkedList<int[]> tests = new LinkedList<>();
//    	for (int i = 1; i < 100; i++) {
//    		// Generate random sequence
//    		int[] w = new int[i];
//    		for (int j = 0; j < i; j++) {
//    			w[j] = r.nextInt((int) Math.floor(i*i*1.05));
//    		}
//    		tests.add(w);
//    	}
//    	
//    	for (int[] t : tests) {
//    		long dp = countKnapsackDP(t, (int) Math.floor(t.length*t.length*1.05));
//    		long approx = countKnapsackApprox(t, (int) Math.floor(t.length*t.length*1.05));
//    		System.out.println(100 * Math.abs(dp-approx) / (double) dp);
//    	}
    	
//    	Test Large n with varying m
    	int[] w = new int[25];
    	int b = 1000000;
    	for (int i = 0; i < 25; i++) {
    		w[i] = r.nextInt(b);
    	}
    	int m = 1;
    	while (m < 10*b) {
    		System.out.println(countKnapsackApprox(w, b, m*w.length));
    		m = (int) Math.ceil(m*1.01);
    	}

    }

}