// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

/**
 * @author umeshpatil
 *
 */
public class BitManipulations {

	/**
	 * Leetcode problem #190. Reverse Bits (https://leetcode.com/problems/reverse-bits/)
	 * 	Reverse bits of a given 32 bits unsigned integer.
	 * 
	 * @param n
	 * @return int
	 */
	public int reverseBits(int n) {
        String br = Integer.toBinaryString(n);
        System.out.println(br);
        char[] ca = br.toCharArray();
        int bitCount = ca.length;
        if (bitCount < 32) {
        	int pad = 32 - bitCount;
        }
        char[] ra = new char[32];
        for (int i=0; i<bitCount; i++) {
        	ra[i] = ca[bitCount-1-i];
        }
        for (int j=bitCount; j<32; j++) {
        	ra[j] = '0';
        }
        String rs = new String(ra);
        System.out.println(rs);
        return Integer.parseUnsignedInt(rs, 2);
    }
	
	/**
	 * Leetcode problem #191. Number of 1 Bits (https://leetcode.com/problems/number-of-1-bits/)
	 * Write a function that takes an unsigned integer and return the number of '1' bits it has 
	 * (also known as the Hamming weight).
	 * 
	 * @param n
	 * @return count of 1 bits
	 */
	public int hammingWeight(int n) {
		int hammingWeight = 0;
		String br = Integer.toBinaryString(n);
        System.out.println(br);
        char[] ca = br.toCharArray();
        int bitCount = ca.length;
        for (int i=0; i<bitCount; i++) {
        	if (ca[i] == '1') {
        		hammingWeight++;
        	}
        }
        return hammingWeight;
    }
	
	public static void main(String[] args) {
		BitManipulations bm = new BitManipulations();
		System.out.println(bm.reverseBits(43261596));
		System.out.println(bm.reverseBits(-3));
		
		System.out.println(bm.hammingWeight(11));
		System.out.println(bm.hammingWeight(256));
	}

}
