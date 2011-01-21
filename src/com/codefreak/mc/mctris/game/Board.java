package com.codefreak.mc.mctris.game;

import java.util.ArrayList;
import java.util.LinkedList;

public class Board {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 22;
	public static final int VISIBLE_HEIGHT = 18;
	
	public static final int PIECE_SPAWN_ROW = 19;
	public static final int PIECE_SPAWN_COL = 5;
	
	private LinkedList<int[]> board = new LinkedList<int[]>();
	
	public Board() {
		this.resetBoard();
	}
	
	public void resetBoard() {
		this.board.clear();
		for(int i=0; i<HEIGHT; i++) {
			this.pushNewRow();
		}
	}
	
	public void cementPiece(Piece p, Cell position) {
		ArrayList<Cell> cells = p.getAbsoluteCellsWithLocation(position);
		for(Cell c : cells) {
			this.setCell(c);
		}
	}
	
	//clears out filled rows and returns an arraylist containing the row numbers cleared
	public ArrayList<Integer> checkAndClearRows() {
		
		System.out.println("Checking rows");
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for(int i=0; i<this.board.size(); i++) {
			boolean filled = true;
			for(int j=0; j<this.board.get(i).length; j++) {
				if(this.board.get(i)[j] == 0) {
					filled = false;
					break;
				}
			}
			if(filled) {
				result.add(i);
			}
		}
		
		//go top down since we're removing items
		for(int i=result.size()-1; i>=0; i--) {
			this.board.remove((int)result.get(i));
			this.pushNewRow();
		}
		
		return result;
	}
	
	public int getColorOfCell(Cell cell) {
		if(!this.inBounds(cell)) return -1;

		//return this.board.get(cell.row)[cell.col];
		int[] i = this.board.get(cell.row);
			
		return i[cell.col];
	}
	
	public boolean cellsAreOpen(ArrayList<Cell> cells) {
		for(Cell c : cells) {
			if(getColorOfCell(c) != 0) {
				return false;
			}
		}
		
		return true;
	}

	public boolean cellIsVisible(Cell cell) {
		return (cell.row < VISIBLE_HEIGHT && cell.row >= 0 && cell.col < WIDTH && cell.col >= 0); 
	}
	
	private boolean inBounds(int row, int col) {
		return (row < HEIGHT && row >= 0 && col < WIDTH && col >= 0);
	}
	
	private boolean inBounds(Cell cell) {
		return inBounds(cell.row, cell.col);
	}
	
	private void pushNewRow() {
		this.board.add(new int[WIDTH]);
	}
	
	private void setCell(Cell c) {
		if(inBounds(c)) {
			this.board.get(c.row)[c.col] = c.value;
		}
	}
	
	//used for testing... Just randomizes the board.
	public void randomize() {
		for(int i=0; i<this.board.size(); i++) {
			for(int j=0; j<this.board.get(i).length; j++) {
				this.board.get(i)[j] = (int)(Math.random() * (Piece.NUM_COLORS + 1));
			}
		}
	}
}
