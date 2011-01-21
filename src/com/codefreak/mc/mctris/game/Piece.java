package com.codefreak.mc.mctris.game;

import java.util.ArrayList;

public class Piece {
	public static final int NUM_COLORS = 4;
	
	private static final int ROW_OFFSET = 0;
	private static final int COL_OFFSET = 1;
	private static final Integer[][][] PIECE_TEMPLATES = {
		{{0,0}, {1,0}, {-1,0}, {2,0}}, //line
		{{0,0}, {0,-1}, {1,0}, {1,1}}, //S
		{{0,0}, {1,-1}, {1,0}, {0,1}}, //reverse S
		{{0,0}, {1,0}, {0,1}, {2,0}}, //L
		{{0,0}, {1,0}, {0,-1}, {2,0}}, //reverse L
		{{0,0}, {1,0}, {0,-1}, {0,1}}, //T
		{{0,0}, {1,0}, {1,1}, {0,1}}  //Square
	};
	
	private int templateIndex;
	private int rotation = 0;
	private int color = 0;
	
	public Piece() {
		this.templateIndex = (int)(Math.random() * PIECE_TEMPLATES.length);
		this.color = (int)(Math.random() * NUM_COLORS) + 1;
	}
	
	public void rotate() {
		this.rotation = nextRotation();
	}
	
	private int nextRotation() {
		return (this.rotation + 1) % 4;
	}
	
	private ArrayList<Cell> getAbsoluteCellsWithRotationAndLocation(int rot, Cell org) {
		ArrayList<Cell> result = new ArrayList<Cell>();
		
		for(int i=0; i<this.blocksInPiece(); i++) {
			Cell c = getOffsetOfBlock(i);
			
			for(int r=0; r<rot; r++) {
				c.setValues(-c.col, c.row); 
			}
				
			c.add(org);
			result.add(c);
		}
		return result;
	}
	
	public ArrayList<Cell> getAbsoluteCellsWithLocation(Cell org) {
		return this.getAbsoluteCellsWithRotationAndLocation(this.rotation, org);
	}
	
	public int getColor() {
		return this.color;
	}
	
	public ArrayList<Cell> getAbsoluteCellsWithLocationAfterRotation(Cell org) {
		return this.getAbsoluteCellsWithRotationAndLocation(nextRotation(), org);
	}
	
	private int blocksInPiece() {
		return PIECE_TEMPLATES[this.templateIndex].length;
	}
	
	private Cell getOffsetOfBlock(int block) {
		if(block < 0 || block > PIECE_TEMPLATES[this.templateIndex].length) return null;
		return new Cell(PIECE_TEMPLATES[this.templateIndex][block][ROW_OFFSET], PIECE_TEMPLATES[this.templateIndex][block][COL_OFFSET], this.color);
	}
}
