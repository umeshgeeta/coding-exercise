// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

/**
 * LeetCode Problem - Valid Number (#65).
 * 
 * Validate if a given string can be interpreted as a decimal number.
 * Some examples:
 * "0" => true
 * " 0.1 " => true
 * "abc" => false
 * "1 a" => false
 * "2e10" => true
 * " -90e3   " => true
 * " 1e" => false
 * "e3" => false
 * " 6e-1" => true
 * " 99e2.5 " => false
 * "53.5e93" => true
 * " --6 " => false
 * "-+3" => false
 * "95a54e53" => false
 * 
 * Note: It is intended for the problem statement to be ambiguous. You should gather all requirements up front before implementing one. However, here is a list of characters that can be in a valid decimal number:
 * 
 * Numbers 0-9
 * Exponent - "e"
 * Positive/negative sign - "+"/"-"
 * Decimal point - "."
 * 
 * Of course, the context of these characters also matters in the input.
 * 
 * Submision Details:
 * 
 * 1481 / 1481 test cases passed.
 * Status: Accepted
 * Runtime: 14 ms
 * Memory Usage: 36.6 MB
 * 
 * Runtime: 14 ms, faster than 14.53% of Java online submissions for Valid Number.
 * Memory Usage: 36.6 MB, less than 100.00% of Java online submissions for Valid Number.
 * 
 * Use https://www.freeformatter.com/java-regex-tester.html to get the right regular expression.
 * 
 * Needed to add special handling for numbers starting with '.' and ending with '.' as well as
 * those starting by 'e'.
 * 
 * @author umeshpatil
 *
 */
public class ValidNumber {

    public boolean isNumber(String s) {
        boolean result = false;
        if (s != null) {
        	String input = s.trim();
        	if (input.length() > 0 && !input.startsWith("e") && !input.startsWith("-e")) {
                if (input.startsWith(".")){
                    java.util.regex.Pattern p = java.util.regex.Pattern.compile("^(\\.\\d+)?(e(\\+|-)?\\d+)?");
                    java.util.regex.Matcher m = p.matcher(input);
                    result = m.matches();
                } else if (input.endsWith(".")){
                    java.util.regex.Pattern p = java.util.regex.Pattern.compile("^(-?\\d+\\.)");
                    java.util.regex.Matcher m = p.matcher(input);
                    result = m.matches();
                } else {
                    java.util.regex.Pattern p = java.util.regex.Pattern.compile("^(\\+|-)?\\d*(\\.\\d*)?(e(\\+|-)?\\d+)?");
                    java.util.regex.Matcher m = p.matcher(input);
                    result = m.matches();
                }

        	}
        }
        return result;
    }

}
