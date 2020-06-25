// MIT License
// Author: Umesh Patil, Neosemantix, Inc.
//
// Finding longest palindrome substring:
// https://leetcode.com/explore/interview/card/apple/347/dynamic-programming/2028/
//
// Leetcode accepted the submission.
//
package com.neosemantix.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LongestPalindromeSubstring {
	
    public String longestPalindrome(String s) {
    	if (s == null || s.length() == 0) {
    		return s;
    	}
    	int argLength = s.length();
    	if (argLength == 1) {
    		return s;
    	}
    	// first we find palindromes of size 2
    	List<Integer> ps2 = populatePalindromesOfSize2(s);
    	if (argLength > 2) {
    		// next we find palindromes of size 3
        	List<Integer> ps3 = populatePalindromesOfSize3(s);
        	if (ps2.isEmpty() && ps3.isEmpty()) {
        		// we did not find any palindromes of size 2 or 3, so the longest palindrome possible
        		// is of length 1; trivially.
        		return s.substring(0, 1);
        	} else {
        		Map<Integer, List<Integer>> foundPalindroms = new HashMap<Integer, List<Integer>>();
        		if (!ps2.isEmpty()) {
        			foundPalindroms.put(2, ps2);
        		}
        		int maxPSize = 2;
        		if (!ps3.isEmpty()) {
        			maxPSize++;
        			foundPalindroms.put(3, ps3);
        		}
        		int psSize = 4;
        		boolean done = false;
        		while (!done) {
        			List<Integer> psList = new ArrayList<Integer>();
        			List<Integer> previousPs = foundPalindroms.get(psSize-2);
        			if (previousPs != null) {
        				for (int i=0; i<previousPs.size(); i++) {
            				int psStart = previousPs.get(i);
            				int left = psStart - 1;
            				int right = psStart + psSize - 2;	// size of the previous palindrome is psSize - 2
            				if (left > -1 && right < argLength) {
            					// indices are not going out of bounds
            					if (s.charAt(left) == s.charAt(right)) {
            						// it is a palindrome
            						psList.add(left);
            					}
            				}
            			}
        			}
        			if (!psList.isEmpty()) {
        				foundPalindroms.put(psSize, psList);
        				maxPSize = psSize;
        			} else {
        				if (maxPSize == psSize - 2) {
        					// previous was empty as well
        					done = true;
        				}
        			}
        			if (psSize < argLength) {
    					psSize++;
    				}else {
    					// we have spanned the entire string, there cannot be any higher palindrome substring
    					done = true;
    				}
        		}
        		// we look at the max size and return that palindrome
        		List<Integer> mps = foundPalindroms.get(maxPSize);
        		// take the first palindrome and return that
        		Integer psStart = mps.get(0);
        		return s.substring(psStart, psStart+maxPSize);
        	}
    	} else {
    		// meaning the string length is exactly 2 only
    		if (ps2.isEmpty()) {
    			// again string is of size 2 and it is not a palindrome, we return the single char
    			return s.substring(0, 1);
    		} else {
    			// ps2 was non empty, so the string must be the palidrome of size 2
    			// no need to go to ps2 and find the string
    			return s;
    		}
    	}
    	
    }
    
    private static List<Integer> populatePalindromesOfSize2(String s) {
    	List<Integer> result = new ArrayList<Integer>();
    	int length = s.length();
    	int i=0;
    	while (i+1 < length) {
    		if (s.charAt(i) == s.charAt(i+1)) {
    			result.add(i);
    		} 
    		i++;
    	}
    	return result;
    }
    
    private static List<Integer> populatePalindromesOfSize3(String s) {
    	List<Integer> result = new ArrayList<Integer>();
    	int length = s.length();
    	int i=0;
    	while (i+2 < length) {
    		if (s.charAt(i) == s.charAt(i+2)) {
    			result.add(i);
    		} 
    		i++;
    	}
    	return result;
    }
    
    public static void main(String[] args) {
    	LongestPalindromeSubstring sol = new LongestPalindromeSubstring();
    	String s = "";
    	
    	s = "abcba";
    	System.out.println("Longest Palindrome: " + sol.longestPalindrome(s));
    	
    	s = "babad";
    	System.out.println("Longest Palindrome: " + sol.longestPalindrome(s));
    	
    	s = "aaaa";
    	System.out.println("Longest Palindrome: " + sol.longestPalindrome(s));
    }
    
    
}
