// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find int pairs, sum of which is a given number; from the given integer array.
 * 
 * We sort the input array and exclude numbers which are repeated.
 * 
 * The implementation below converts the sorted input array into a doubly linked list
 * and then iterate over it from both ends. (In itself that is not necessary, we could
 * implement over the sorted array itself starting from both ends moving the indices
 * appropriately.)
 * 
 * @author umeshpatil
 *
 */
public class PairFinder {

	/**
	 * Main for testing purposes.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PairFinder pf = new PairFinder();
		int[] sample1 = {2,5,7,6,8,4,9,11,7};
		Node fn = pf.buildDoublyLinkedList(sample1);
		List<Pair> pairsFound = pf.findPairs(fn, 14);
	}
	
	/**
	 * @param firstNode
	 * @param sum
	 * @return List<Pair> List of found pairs
	 */
	public List<Pair> findPairs(Node firstNode, int sum){
		List<Pair> result = new ArrayList<Pair>();
		if (firstNode != null) {
			Node start = firstNode;
			Node last = getLastNode(start);
			boolean done = false;
			while (!done) {
				int s = start.data + last.data;
				if (s < sum) {
					start = start.next;
				} else if (s == sum) {
					Pair p = new Pair(start.data, last.data);
					result.add(p);
					System.out.println("Pair found: " + p);
					start = start.next;
					last = last.previous;
					if ((start.data > last.data) || (start.equals(last))) {
						// we have crossed each other, cannot find any additional pairs
						done = true;
					}
					// else more pairs to be found
				} else {
					last = last.previous;
				}
			}
		}
		return result;
	}
	
	/**
	 * @param ia Input arrary of integers.
	 * @return Node First Node of the doubly linked list build.
	 */
	private Node buildDoublyLinkedList(int[] ia) {
		Node result = null;
		if (ia != null && ia.length > 0) {
			Arrays.sort(ia);
			result = new Node(ia[0]);
			Node p = result;
			int pval = p.data;
			for (int i=1; i<ia.length; i++) {
				if (ia[i] > pval) {
					Node nd = new Node(ia[i]);
					nd.previous = p;
					// we mark the next of 'previous' node as this node
					p.next = nd;		// since p is at least the first node, we know for sure it is non-null
					// and now we move p
					p = nd;
					pval = p.data;
				}
				// else it is same value, we skip
			}
		}
		return result;
	}
	
	/**
	 * @param fn
	 * @return Node Last of the list.
	 * 
	 * Two alternatives where we would not need to traverse the list are:	
	 * 1) Keep a reference to the last node during the building of the doubly linked list.
	 * 2) Or if we are using int array instead of a doubly linked list, we could use 
	 *    array.length - 1 as the last index value directly.
	 * 
	 */
	private Node getLastNode(Node fn) {
		Node n = fn;
		while (n.next != null) {
			//System.out.println(n.data);
			n = n.next;
		}
		return n;
	}
	
	private class Node {
		private int data;
		private Node next;
		private Node previous;
		
		private Node(int d) {
			data = d;
		}
	}
	
	public class Pair {
		public int num1;
		public int num2;
		
		public Pair(int n1, int n2) {
			num1 = n1;
			num2 = n2;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			sb.append(num1);
			sb.append(", ");
			sb.append(num2);
			sb.append(")");
			return sb.toString();
		}
	}

}
