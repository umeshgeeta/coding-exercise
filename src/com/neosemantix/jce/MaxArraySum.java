// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.jce;

/**
 * https://www.hackerrank.com/challenges/max-array-sum/problem
 * 
 * Given an array of integers, find the subset of non-adjacent elements with the
 * maximum sum. Calculate the sum of that subset.
 * 
 * For example, given an array we have the following possible subsets:
 * 
 * Subset Sum 
 * [-2, 3, 5] 6 
 * [-2, 3] 1 
 * [-2, -4] -6 
 * [-2, 5] 3 
 * [1, -4] -3 
 * [1, 5] 6
 * [3, 5] 8 
 * Our maximum subset sum is 8.
 * 
 * Function Description
 * 
 * Complete the function in the editor below. It should return an integer
 * representing the maximum subset sum for the given array.
 * 
 * maxSubsetSum has the following parameter(s):
 * 
 * arr: an array of integers Input Format
 * 
 * The first line contains an integer, n. 
 * The second line contains n space-separated integers arr[i].
 * 
 * Constraints
 * 
 * 1 <= n < 10**5
 * -10**4 <= arr[i] <= 10**4
 * 
 * Output Format
 * 
 * Return the maximum sum described in the statement.
 * 
 * Sample Input 0
 * 5 
 * 3 7 4 6 5 
 * Sample Output 0
 * 13 
 * 
 * Sample Input 1
 * 5 
 * 2 1 5 8 4 
 * Sample Output 1
 * 11 
 * 
 * Sample Input 2
 * 5 
 * 3 5 -7 8 10 
 * Sample Output 2
 * 15 
 * 
 * @author umeshpatil
 *
 */
public class MaxArraySum {

	
	/**
	 * We start from the last element and start tracking 2 sums:
	 * - one where the previous element is involved (maxSumAdjacenet) and
	 * - another one where the previous element is NOT involved (maxSumNonAdjacent).
	 * 
	 * We add the current element with maxSumNonAdjacent and then adjust these two
	 * values to move to the previous element. 
	 * 
	 * @param a Input array
	 * @return Max sum of non-adjacent elements of the array.
	 */
	public static int findMax(int[] a) {
		int len = a.length;
		int maxSumAdjacenet = a[len - 1];
		int maxSumNonAdjacent = 0;
		for (int j = len - 2; j > -1; j--) {
			int newAd = a[j] + maxSumNonAdjacent;
			int t = maxSumAdjacenet;
			maxSumAdjacenet = max(newAd, maxSumNonAdjacent, a[j]);
			if (newAd > maxSumNonAdjacent) {
				if (maxSumAdjacenet != a[j]) {
					maxSumAdjacenet = newAd;
				}
				// else keep it as is
				maxSumNonAdjacent = t;
			} else {
				maxSumAdjacenet = Math.max(a[j], maxSumNonAdjacent);
				maxSumNonAdjacent = Math.max(t, maxSumNonAdjacent);
			}
			if (maxSumAdjacenet < maxSumNonAdjacent) {
				maxSumAdjacenet = maxSumNonAdjacent;
			}
		}
		return Math.max(maxSumAdjacenet, maxSumNonAdjacent);
	}

	private static int max(int a, int b, int c) {
		int result = a;
		result = Math.max(a, b);
		result = Math.max(result, c);
		return result;
	}

	public static void main(String[] args) {

		int max = Integer.MIN_VALUE;

		int[] ar6 = { 8, 4, -4 };
		max = MaxArraySum.findMax(ar6);
		assert (8 == max);
		System.out.println(max);

		int[] ar5 = { 3, 5, -7, 8, 1, -10, 5, 6, 2, -1, 4, -3 };
		max = MaxArraySum.findMax(ar5);
		assert (24 == max);
		System.out.println(max);

		int[] ar1 = { -2, 1, 3, -4, 5 };
		max = MaxArraySum.findMax(ar1);
		assert (8 == max);
		System.out.println(max);

		int[] ar2 = { 3, 7, 4, 6, 5 };
		max = MaxArraySum.findMax(ar2);
		assert (13 == max);
		System.out.println(max);

		int[] ar3 = { 2, 1, 5, 8, 4 };
		max = MaxArraySum.findMax(ar3);
		assert (11 == max);
		System.out.println(max);

		int[] ar4 = { 3, 5, -7, 8, 10 };
		max = MaxArraySum.findMax(ar4);
		assert (15 == max);
		System.out.println(max);

	}

}
