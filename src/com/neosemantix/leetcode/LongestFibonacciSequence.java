// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

/**
 * Leetcode Problem #873. Length of Longest Fibonacci Subsequence
 * https://leetcode.com/problems/length-of-longest-fibonacci-subsequence/
 * 
 * A sequence X_1, X_2, ..., X_n is fibonacci-like if:
 * n >= 3
 * X_i + X_{i+1} = X_{i+2} for all i + 2 <= n
 * Given a strictly increasing array A of positive integers forming a sequence, 
 * find the length of the longest fibonacci-like subsequence of A.  
 * If one does not exist, return 0.
 * 
 * (Recall that a subsequence is derived from another sequence A by deleting any number 
 * of elements (including none) from A, without changing the order of the remaining elements.  
 * For example, [3, 5, 8] is a subsequence of [3, 4, 5, 6, 7, 8].)
 * 
 * @author umeshpatil
 *
 */
public class LongestFibonacciSequence {
	
	/**
	 * Iterate through all possible start points and subsequences.
	 * 
	 * @param A
	 * @return int Longest possible Fibonacci subsequence
	 */
	public int lenLongestFibSubseq(int[] A) {
        int result = 0;
        if (A != null && A.length > 2) {
        	int l = A.length;
        	for (int s=0; s < l-2; s++) {
        		int r = longestFibonacci(s, A);
        		if (r > result) {
        			result = r;
        		}
        	}
        }
        return result;
    }
	
	/**
	 * For the fixed starting point, find all possible Fibonacci sequences and longest of that.
	 * 
	 * @param startIndex
	 * @param seq
	 * @return int
	 */
	private int longestFibonacci(int startIndex, int[] seq) {
		int lfs = 0;
		for (int j = startIndex+1; j<seq.length; j++) {
			int l = longestFibonacci(startIndex, j, seq);
			//System.out.println("l: " + l + " lfs: " + lfs);
			if (l > lfs){
				lfs = l;
			}
		}
		return lfs;
	}
	
	/**
	 * For the given first 2 numbers, find Fibonacci sequences and longest of that.
	 * 
	 * @param startIndex
	 * @param secondIndex
	 * @param seq
	 * @return int
	 */
	private int longestFibonacci(int startIndex, int secondIndex, int[] seq) {
		int lengthOfFS = 0;
		int firstNum = seq[startIndex];
		int secondNum = seq[secondIndex];
		int sum = firstNum + secondNum;
		for (int i = secondIndex + 1; i < seq.length; i++) {
			if (seq[i] > sum) {
				break;
			} else if (seq[i] == sum) {
				//System.out.println("fn: " +  firstNum + " sn: " + secondNum + " = seq[" + i + "] (" + seq[i] + ")" );
				firstNum = secondNum;
				secondNum = seq[i];
				sum = firstNum + secondNum;
				lengthOfFS++;
			}
		}
		if (lengthOfFS > 0) {
			lengthOfFS += 2; 	// since we need to include firstNum and secondNum
		}
		return lengthOfFS;	
	}
	
	public static void main(String[] args) {
		int[] a = {1,2,3,4,5,6,7,8};
				//{1, 3, 5};
				//{1,3,7,11,12,14,18};
				//
		LongestFibonacciSequence lfsObj = new LongestFibonacciSequence();
		int r = lfsObj.lenLongestFibSubseq(a);
		System.out.println("r: " +r);
	}

}
