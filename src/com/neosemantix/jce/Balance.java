// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.jce;

import java.util.Stack;

/**
 * Opening bracket: '<' or '{' or '[' or '('
 * Closing bracket: '>' or '}' or ']' or ')'
 * 
 * @author umeshpatil
 *
 */
public class Balance {


	public Balance() {
		// default constructor
	}
	
	public String balanceUsingStack(String input) {
		String result = "";
		if (input != null && !input.isEmpty()) {
			StringBuffer output = new StringBuffer();
			char[] ins = input.toCharArray();
			int len = ins.length;
			Stack<Character> stk = new Stack<Character>();
			int i = 0;
			int advance = 0;
			boolean done = false;
			while (!done) {
				char c = ins[i];
				if (c == '<') {
					// flush closing brackets
					if (advance > 0) {
						output.append(input.substring(i-advance, i));
						// reset advance
						advance = 0;
					}
					stk.push(c);
				} else {
					// it is a closing bracket
					if (stk.isEmpty()) {
						// for the incoming closing bracket, there is no matching opening
						// so we add and advance
						output.append('<');
						advance++;
					} else {
						// there is a matching
						output.append(stk.pop());
						output.append(c);
					}
				}
				i++;
				if (i == len) {
					if (advance > 0) {
						output.append(input.substring(i-advance, i));
					} else {
						StringBuffer mcb = new StringBuffer();
						while (!stk.isEmpty()) {
							mcb.append('>');
							output.append(stk.pop());
						}
						if (mcb.length() > 0) {
							output.append(mcb);
						}
					}
					done = true;
				}
			}
			result = output.toString();
		}
		return result;
	}
	
	/**
	 * We simply find difference in opening brackets and add those brackets: 
	 * 	missing opening brackets always at the start and
	 *  missing closing brackets always at the end.
	 *  
	 * @param input
	 * @return String
	 */
	public String balance(String input) {
		String result = "";
		if (input != null && !input.isEmpty()) {
			StringBuffer output = new StringBuffer();
			int ob = 0;
			int cb = 0;
			int l = input.length();
			for(int i=0; i<l; i++) {
				if (input.charAt(i) == '<') {
					ob++;
				} else {
					cb++;
				}
			}
			while (ob < cb) {
				output.append('<');
				ob++;
			}
			output.append(input);
			while (ob > cb) {
				output.append('>');
				cb++;
			}
			result = output.toString();
		}
		return result;
	}
	
	public void output(String input) {
		System.out.println("Input: " + input + " Balanced: " + balance(input) + " Using stack: " +balanceUsingStack(input));
	}
	
	
	public static void main(String[] args) {
		Balance obj = new Balance();
		
		String input = "";
		obj.output(input);
		
		input = "<>";
		obj.output(input);
		
		input = ">>";
		obj.output(input);
		
		input = "<<<<>>";
		obj.output(input);
		
		input = "<<><><>";
		obj.output(input);
		
		input = "<<<><";
		obj.output(input);
		
		input = "<<><<<><<<";
		obj.output(input);
		
		/*
		 
Expected  Output:

Input:  Balanced:  Using stack: 
Input: <> Balanced: <> Using stack: <>
Input: >> Balanced: <<>> Using stack: <<>>
Input: <<<<>> Balanced: <<<<>>>> Using stack: <><><<>>
Input: <<><><> Balanced: <<><><>> Using stack: <><><><>
Input: <<<>< Balanced: <<<><>>> Using stack: <><<<>>>
Input: <<><<<><<< Balanced: <<><<<><<<>>>>>> Using stack: <><><<<<<<>>>>>>
		 */
	}
	
}