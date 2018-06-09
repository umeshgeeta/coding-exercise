package com.neosemantix.jce;

import java.util.List;
import java.util.Random;

/**
 * @author umeshpatil
 *
 */
public class NumberPyramid {
	
	private static int blankSpace = -1;
	
	private int[][] pyramid;
	private int rowLength;
	private int height;
	
	public NumberPyramid(int h, int maxLimit) {
		if (h < 2) {
			throw new IllegalArgumentException("Pyramid base needs to be at least 2");
		}
		height = h;
		if (maxLimit < 1) {
			throw new IllegalArgumentException("Limit needs to be a positive number");
		}
		rowLength = (2 * height) - 1;
		pyramid = new int[height][rowLength];
		for(int i=0; i<height; i++) {
			for(int j=0; j<rowLength; j++) {
				pyramid[i][j] = blankSpace;
			}
		}
		Random random = new Random();
		int row = height - 1;		// last row index
		int col = 0;
		while (col < rowLength) {
			pyramid[row][col] = random.nextInt(maxLimit);
			col = col + 2;
		}
		while (row > 0) {
			computeNextRow(row);
			row--;
		}
	}
	
	private void computeNextRow(int currentRow) {
		int col = 0;
		// skip all the initial blank spaces
		while (pyramid[currentRow][col] == blankSpace) {
			col++;
		}
		// at this point at 'col' place we have non-blank value
		while (col < rowLength - 2) {
			int val = pyramid[currentRow][col];
			int nextVal = pyramid[currentRow][col+2];
			if (val != blankSpace && nextVal != blankSpace) {
				pyramid[currentRow-1][col+1] =  val + nextVal;
			} 
			col++;
		}
	}
	
	public void print() {
		for(int row=0; row<height; row++) {
			for(int col=0; col<rowLength; col++) {
				int val = pyramid[row][col];
				if (val != blankSpace) {
					System.out.format("% 3d", val);
				} else {
					System.out.print("   ");
				}
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		NumberPyramid np = new NumberPyramid(4, 10);
		np.print();
	}

}
