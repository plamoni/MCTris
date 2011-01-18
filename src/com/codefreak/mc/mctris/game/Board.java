package com.codefreak.mc.mctris.game;

import java.util.ArrayList;
import java.util.LinkedList;

public class Board {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 22;
	public static final int VISIBLE_HEIGHT = 18;
	
	private LinkedList<Integer[]> board = new LinkedList<Integer[]>();
	
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
		ArrayList<Cell> cells = p.getAbsoluteCellsWithOrigin(position);
		for(Cell c : cells) {
			this.setCell(c);
		}
	}
	
	//clears out filled rows and returns an arraylist containing the row numbers cleared
	public ArrayList<Integer> checkAndClearRows() {
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
			this.board.remove(result.get(i));
			this.pushNewRow();
		}
		
		return result;
	}
	
	public int getValueOfCell(Cell cell) {
		if(!inBounds(cell)) return -1;
		
		return this.board.get(cell.row)[cell.col];
	}
	
	public boolean cellsAreOpen(ArrayList<Cell> cells) {
		for(Cell c : cells) {
			if(getValueOfCell(c) != 0) {
				return false;
			}
		}
		
		return true;
	}

	private boolean inBounds(int row, int col) {
		return (row < HEIGHT && row >= 0 && col < WIDTH && col >= 0);
	}
	
	private boolean inBounds(Cell cell) {
		return inBounds(cell.row, cell.col);
	}
	
	private void pushNewRow() {
		this.board.add(new Integer[WIDTH]);
	}
	
	private void setCell(Cell c) {
		if(inBounds(c)) {
			this.board.get(c.row)[c.col] = c.value;
		}
	}
	
}
