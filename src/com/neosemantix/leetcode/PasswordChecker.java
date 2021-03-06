// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Strong Password (Leetcode: https://leetcode.com/problems/strong-password-checker/)
 * 
 * A password is considered strong if below conditions are all met:
 * It has at least 6 characters and at most 20 characters.
 * It must contain at least one lowercase letter, at least one uppercase letter, 
 * and at least one digit.
 * It must NOT contain three repeating characters in a row 
 * ("...aaa..." is weak, but "...aa...a..." is strong, assuming other conditions are met).
 * 
 * Write a function strongPasswordChecker(s), that takes a string s as input, 
 * and return the MINIMUM change required to make s a strong password. 
 * If s is already strong, return 0.
 * 
 * Insertion, deletion or replace of any one character are all considered as one change.
 * 
 * Solution: We divide the problem in 3 parts: when input is smaller than 6 (we need to add),
 * 				when input is of length 6 (only replacement) and when the input is larger
 * 				than 6 when we have to chop the string (combination of delete and replacement).
 * 
 * 				Major implementation is for the 3rd case since we need to evaluate optimal
 * 				solution between character deletion and character replacement. We compute
 * 				number of transformations needed in both strategies - A) replacement substituted
 * 				by deletes and B) greedy 'delete' followed by mandatory replacements. 
 * 				We pick minimum of these 2 strategies as the answer.
 * 
 * @author umeshpatil
 *
 */
public class PasswordChecker {
	
	// key length of the string with same char
	// we start with minimum of 3 chars
	private Map<Integer, Integer> sameCharSubStrs;
	boolean foundLowerCase = false;
	boolean foundUpperCase = false;
	boolean foundDigit = false;

	/**
	 * @param s User input string.
	 * @return Minimum number of transformations needed to change the
	 * 			argument to confirm as a strong password as per guidelines.
	 */
	public int strongPasswordChecker(String s) {
		int result = 0;
		if (s == null || s.length() == 0) {
			return 6;	// we have to add all 6...
		}
		initialize();
		int sl = s.length();
		
		int i = 0;
    	int j = 0;
    	int l = s.length();
    	while (j < l) {
    		char cj = s.charAt(j);
    		
    		if (Character.isDigit(cj)) {
    			foundDigit = true;
    		}
    		if (Character.isUpperCase(cj)) {
    			foundUpperCase = true;
    		}
    		if (Character.isLowerCase(cj)) {
    			foundLowerCase = true;
    		}
    		
    		if (s.charAt(i) == cj) {
    			j++;
    		} else {
    			count(i, j);   			
    			i = j;
    			j++;
    		}
    	} 
    	count(i, j);
    	
    	int deleteCount = 0;
        if (sl > 20) {
        	deleteCount = sl - 20;
        }
        
        int addCount = 0;
        if (sl < 6) {
        	addCount = 6 - sl;
        }
        
        if (sl < 6) {
        	
        	// At the most there can be one sub-string
        	// of length 3 or 4 or 5 which will need
        	// one replacement or addition. So we check.
        	int countToBreakRepeatStr = charToBreakRepeatStr();
        	int ra = Math.max(lowerUpperDigitCount(), countToBreakRepeatStr);
        	int left = ra - addCount;
        	
        	if (left < 1) {
        		// we have enough to add
        		result = addCount;
        	} else {
        		result = addCount + left;
        	}

        } else if (sl == 6) {
        	
        	// no addition or deletion, only replacements
        	result = Math.max(lowerUpperDigitCount(), charToBreakRepeatStr());
        	
        } else {
        	
        	// A) First strategy - We do only all replacements to break same char blocks.
        	//					   For every block of any size, only one delete can be
        	//						substituted for a replacement which is the last char
        	//						since every other 3rd char in the block is replaced.
        	int resultStratA = Math.max(6, sl);	// set it to max
        	if (!sameCharSubStrs.isEmpty()) {
            	int rpl = replacementsNeeded();
            	// deletes which can substitute a replacement
            	// not all substitutions are same
            	// some replacements need can be substituted by a single delete 
            	// while other types of replacements would need 2 or 3 deletes
            	List<Integer> subDel = substitutableDeletes();
            	int[] res = substituteDelsForReplacement(subDel, deleteCount);
            	int replsSubstituted = res[0];
            	int remainingRpls = rpl - replsSubstituted;
            	// if there are 3 deletes left, then it can substitute one more replace
            	remainingRpls = remainingRpls - (res[1] / 3);
            	resultStratA = deleteCount + Math.max(remainingRpls, lowerUpperDigitCount());
        	}

        	
        	// B) Second strategy - we do 'greedy' delete and then apply replacement 
        	//						to break any left over same char blocks
        	
        	// deletes break repeat strings, so we use those delete strings first
        	// and break as many repeat strings as possible.
        	breakRepeatStrByDelete(deleteCount);
        	
        	// now whatever is left we have to break those repeat string by replacement
        	// first we find how many replacements needed
        	int repl = replacementsNeeded();
        	
        	// deletes are must
        	int resultStratB = deleteCount + Math.max(repl, lowerUpperDigitCount());
        	
        	// Final answer is minimum of these 2
        	result = Math.min(resultStratA, resultStratB);
        			
        }
    	
		return result;
	}
	
	/**
	 * Initialize internal map so that in the 'main' we can test multiple strings.
	 */
	private void initialize() {
		sameCharSubStrs = new HashMap<Integer, Integer>();
		foundLowerCase = false;
		foundUpperCase = false;
		foundDigit = false;
	}
	
	/**
	 * @param subDel
	 * @param delsAvail
	 * @return Returns number of replacements which are substituted by deletes as the first index
	 * 			and number of deletes which are still not used
	 */
	private int[] substituteDelsForReplacement(List<Integer> subDel, int delsAvail) {
		int sz = subDel.size();
		int i = 0;
		int delCapacity = delsAvail;
		while (i < sz && delCapacity > 0) {
			if (subDel.get(i) <= delCapacity) {
				delCapacity -= subDel.get(i);
				i++;
			} else {
				// we cannot fully substitute this replacement by 'deletion'
				// we need to get out
				delCapacity = 0;
			}
		}
		int[] result = {i, delCapacity};
		return result;
	}
	
	/**
	 * Find occasions where instead of 'replacing' a character we can undertake
	 * deletion to achieve substring without 3 contagious same characters.
	 * 
	 * For example for substring 'aaa' we can replace last char to make it 'aaY'.
	 * Alternatively we can delete the last char and make it acceptable 'aa'.
	 * 
	 * Things start getting tricky when substring is of size more than 3 or more
	 * than multiple of 3. For substring 'aaaa' we need to delete 2 while for 
	 * 'aaaaa' we need to delete to 3 to achieve acceptable resultant substring.
	 * 
	 * @return int
	 */
	private List<Integer> substitutableDeletes() {
		List<Integer> rs = new ArrayList<Integer>();
		if (!sameCharSubStrs.isEmpty()) {
			Set<Integer> blockSizes = sameCharSubStrs.keySet();
			for (int s: blockSizes) {
				int howMany = sameCharSubStrs.get(s);
				int rem = s % 3;
				switch (rem) {
				case 0:
					// sufficient to remove the last one
					for (int k=0; k<howMany; k++) {
						rs.add(1);
					}
					break;
				case 1:
					// we will have to remove 2 for example when it 7
					for (int k=0; k<howMany; k++) {
						rs.add(2);
					}
					break;
				case 2:
					// we will have to remove 3 for example when it is 8
					for (int k=0; k<howMany; k++) {
						rs.add(3);
					}
					break;
				}
			}
		}
		rs.sort(null);
		return rs;
	}
	
	/**
	 * We determine how many replacements are needed so that there is no block
	 * of same characters of size 3 or more.
	 * 
	 * @return int
	 */
	private int replacementsNeeded() {
		if (sameCharSubStrs.isEmpty()) {
			// nothing left
			return 0;
		} else {
			// we walk through all remaining repeat strings to break
			// for each repeat string of size s, it would take s/3 chars to break
			// so we all add up and find the total replacement char need
			int replacementsNeeded = 0;
			int currentRepeatStrSize = 3;
			int initKeyCount = sameCharSubStrs.size();
			int k = 0;
			while (k < initKeyCount) {
				Integer numRs = sameCharSubStrs.get(currentRepeatStrSize);
				if (numRs != null) {
					int nrs = numRs.intValue();
					int numToBreakSingleRS = currentRepeatStrSize / 3;
					replacementsNeeded += nrs * numToBreakSingleRS;
					k++;
				}
				currentRepeatStrSize++;
			}
			return replacementsNeeded;
		}
	}
	
	/**
	 * Here we consume allowed number of 'delete characters' to break 'same char chunks' of
	 * size 3 or more.
	 *  
	 * @param dc Count of characters to be deleted
	 */
	private void breakRepeatStrByDelete(int dc) {
		int remainingDeletesToUse = dc;
		int currentRepeatStrSize = 3;
		// we want to stick to the initial key iteration only since
		// during the process of consumption for the left over string
		// an entry back can get added
		int initKeyCount = sameCharSubStrs.size();
		int k = 0;
		while (k < initKeyCount && remainingDeletesToUse > 0) {
			Integer numRs = sameCharSubStrs.get(currentRepeatStrSize);
			if (numRs != null) {
				// consume
				int nrs = numRs.intValue();
				int j = 0;
				int howManyPerRS = currentRepeatStrSize - 2;
				int reduceCount = 0;
				while (remainingDeletesToUse > 0 && j < nrs) {
					if (remainingDeletesToUse >= howManyPerRS) {
						remainingDeletesToUse -= howManyPerRS;
						reduceCount++;
					} else {
						// we are having fraction here
						int remainingStrSize = howManyPerRS - remainingDeletesToUse;
						remainingDeletesToUse = 0;
						// we may add back a entry which will have to be broken then
						// by replace action since all 'deletes' are done
						count(0, remainingStrSize+2);
						reduceCount++;
					}
					j++;
				}
				// we covered all repeat string of this size; we can adjust / remove the entry
				int diff = nrs - reduceCount;
				if (diff == 0) {
					sameCharSubStrs.remove(currentRepeatStrSize);
				} else {
					sameCharSubStrs.put(currentRepeatStrSize, diff);
				}
				k++;
			}
			currentRepeatStrSize++;
		}
	}
	
	/**
	 * For substrings of the size 3, 4 & 5 (for input less than 6 char); we want to know
	 * how chunks are present.
	 * 
	 * @return int
	 */
	private int charToBreakRepeatStr() {
		int countToBreakRepeatStr = 0;
		
		Integer rs3 = sameCharSubStrs.get(3);
		Integer rs4 = sameCharSubStrs.get(4);
		Integer rs5 = sameCharSubStrs.get(5);
		
		if (rs3 != null) {
			countToBreakRepeatStr += rs3.intValue();
		}
		if (rs4 != null) {
			countToBreakRepeatStr += rs4.intValue();
		}
		if (rs5 != null) {
			countToBreakRepeatStr += rs5.intValue();
		}

		return countToBreakRepeatStr;
	}
	
	/**
	 * @return How many changes needed to confirm to the constraint that the argument
	 * 			string should contain at least one lower case, one upper case and one digit.
	 */
	private int lowerUpperDigitCount() {
		int result = 3;
		if (foundLowerCase) {
			result--;
		}
		if (foundUpperCase) {
			result--;
		}
		if (foundDigit) {
			result--;
		}
		return result;
	}
	
	/**
	 * Updates the internal data structure where we track same char substrings of various lengths.
	 * 
	 * @param s Start index of the substring with same character
	 * @param e End index of the substring
	 */
	private void count(int s, int e) {
		int repeatSize = e - s;
		if (repeatSize > 2) {
			Integer ct = sameCharSubStrs.get(repeatSize);
			if (ct == null) {
				sameCharSubStrs.put(repeatSize, 1);
			} else {
				int ctt = ct.intValue() + 1;
				sameCharSubStrs.put(repeatSize, ctt);
			}
		}
		// else it is ignored
	}

	/**
	 * Unit tests. Leetcode has 42 unit test cases for this problem. As I fixed the code, I included failed
	 * test cases here in the main.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PasswordChecker s = new PasswordChecker();
		String input = "";
		int cc = 0;
		
		input = "aaaaaaaAAAAAA6666bbbbaaaaaaABBC";
		cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 13);
		
		input = "1234567890123456Baaaaa";
		cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 3);
		
		input = "abababababababababaaa";
		cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 3);
		
		input = "ABABABABABABABABABAB1";
		cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 2);
		
		input = "AAAAAABBBBBB123456789a";
		cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 4);
		
		input = "aaaabbaaabbaaa123456A";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 3);
		
		input = "aaa111";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 2);
    	
    	input = "1010101010aaaB10101010";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 2);
    	
    	input = "aaaaaaaaaaaaaaaaaaaaa";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 7);
    	
    	input = "aaa123";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 1);
    	
    	input = "";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 6);
    	
    	input = "sdfs 3esdf sfssdfsfyuyss234234 234343";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 18);
    	
    	input = "xzyaaa";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 2);
    	
    	input = "kkkkkkksdf weraf assss ssefse";
    	cc = s.strongPasswordChecker(input);
    	System.out.println("Changes needed: " + cc);
    	assert(cc == 11);
	}
}
