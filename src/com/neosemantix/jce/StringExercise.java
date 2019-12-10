package com.neosemantix.jce;

import java.util.Arrays;

public class StringExercise {
	
	public static String reverse(String arg) {
		String result = null;
		if (arg != null) {
			if (!arg.isEmpty()) {
				char[] ca = arg.toCharArray();
				int sz = ca.length;
				int midPoint = sz / 2;
				for(int i=0; i<midPoint; i++) {
					char c = ca[i];
					int index = (sz - 1) - i;
					ca[i] = ca[index];
					ca[index] = c;
				}
				result = new String(ca);
			} else {
				result = new String();	// assign empty string
			}
		}
		return result;
	}
	
	public static boolean isAnagram(String str1, String str2) {
		boolean result = false;
		if (str1 != null) {
			if (str2 != null) {
				String s1 = str1.replaceAll("\\s", "");
				String s2 = str2.replaceAll("\\s", "");
				int l1 = s1.length();
				int l2 = s2.length();
				if (l1 == l2) {
					char[] c1 = s1.toLowerCase().toCharArray();
					char[] c2 = s2.toLowerCase().toCharArray();
					Arrays.sort(c1);
					Arrays.sort(c2);
//					System.out.println(c1);
//					System.out.println(c2);
					result = Arrays.equals(c1, c2);
				}
				// lengths are unequal, they are not anagram
			}
			// first is not null, while second is not; they are not anagram
		} else if (str2 == null) {
			// both are null, we regard them anagram trivially
			result = true;
		}
		// else first is null, second is not; they are not anagram
		return result;
	}

	public static void main(String[] args) {
//		String str1 = "test";
//		String str2 = "house";
//		System.out.println(reverse(str1));
//		System.out.println(reverse(str2));
		
		String s1 = "Mother In Law";
		String s2 = "Hitler Woman";
		System.out.println(s1 + " and " + s2 + " are Anagrams? " + isAnagram(s1, s2));
		
	}

}
