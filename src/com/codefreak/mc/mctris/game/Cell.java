package com.codefreak.mc.mctris.game;

public class Cell {
	public int value = 1; //not sure if i'm going to use this.
	public int row;
	public int col;
	
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Cell(int row, int col, int value) {
		this(row, col);
		this.value = value;
	}
	
	public void setValues(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void add(Cell c) {
		this.row += c.row;
		this.col += c.col;
	}
}
