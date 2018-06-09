package com.neosemantix.jce;

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str1 = "test";
		String str2 = "house";
		System.out.println(reverse(str1));
		System.out.println(reverse(str2));
	}

}
