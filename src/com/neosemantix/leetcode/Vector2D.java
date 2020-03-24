// Copyright 2019; All rights reserved with NeoSemantix, Inc.
package com.neosemantix.leetcode;

/**
 * Leetcode Problem # 251. Flatten 2D Vector
 * https://leetcode.com/problems/flatten-2d-vector/
 * 
 * Design and implement an iterator to flatten a 2d vector. 
 * It should support the following operations: next and hasNext.
 * 
 * Example:
 * 
 * Vector2D iterator = new Vector2D([[1,2],[3],[4]]);
 * iterator.next(); // return 1
 * iterator.next(); // return 2
 * iterator.next(); // return 3
 * iterator.hasNext(); // return true
 * iterator.hasNext(); // return true
 * iterator.next(); // return 4
 * iterator.hasNext(); // return false
 * 
 * @author umeshpatil
 *
 */
class Vector2D {
	
	private int[][] input;
	private int rowCount;
	private int colCount;
	private int row;		// where we are
	private int col;
	private boolean endReached;

    public Vector2D(int[][] v) {
    	if (v != null) {
            input = v;
            rowCount = input.length;
            row = -1;	//so that we start from the first row
            toNonEmptyRow();
    	} else {
    		// no rows...
    		endReached = true;
    	}
    }
    
    private void toNonEmptyRow() {
    	int r = row + 1;
        while ((r < rowCount) && (input[r] == null || input[r].length == 0)) {
        	r++;
        }
        if (r == rowCount) {
        	// all rows are empty
        	endReached = true;
        } else {
        	// we found the first non-empty row
        	colCount = input[r].length;
       	 	row = r;
            col = 0;
            endReached = false;
        }
    }
    
    public int next() {
    	int result = -1;
    	if (!endReached) {
    		result = input[row][col];
    		updateMarkers();
    	} else {
    		throw new IndexOutOfBoundsException("No more elements.");
    	}
        return result;
    }
    
    private void updateMarkers() {
    	if (col < colCount-1) {
    		col++;
    	} else {
    		// we need to go to the next non empty row
    		toNonEmptyRow();
    	}
    }
    
    public boolean hasNext() {
//    	System.out.print("For row: " + row + " and col: " + col);
//    	System.out.println(" next is: " + input[row][col]);
        return !endReached;
    }
    
    public static void main(String[] args) {
    	int[][] in = new int[][] {{}, {1,2},{7,8,9}, {3},{4}, {}};
    	/*
    	System.out.println(in[0][0]);
    	System.out.println(in[0][1]);
    	System.out.println(in[1][0]);
    	System.out.println(in[2][0]);
    	System.out.println("rows: " + in.length);
    	System.out.println("cols: " + in[0].length);
    	System.out.println("cols: " + in[1].length);
    	*/
    	Vector2D iterator = new Vector2D(in);

    	System.out.println(iterator.next()); // return 1
    	System.out.println(iterator.next()); // return 2
    	System.out.println(iterator.next()); // return 3
    	System.out.println(iterator.hasNext()); // return true
    	System.out.println(iterator.hasNext()); // return true
    	System.out.println(iterator.next()); // return 4
    	System.out.println(iterator.hasNext()); // return false
    }
}
