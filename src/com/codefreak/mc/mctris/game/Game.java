package com.codefreak.mc.mctris.game;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.codefreak.mc.mctris.MCTris;
import com.codefreak.mc.mctris.controls.MCTrisInput;


public class Game implements Runnable {
	private Board board = new Board();
	private Piece curPiece = new Piece();
	private Cell curPieceLocation = new Cell(Board.PIECE_SPAWN_ROW, Board.PIECE_SPAWN_COL);
	private World gameWorld;
	
	//defines the bottom left-hand corner block of the game board.
	// the game board is 12 by 19.
	private static final int GAME_LOC_X = 62;
	private static final int GAME_LOC_Y = 77;
	private static final int GAME_LOC_Z = 15;
	
	private static final int TICK_LENGTH = 1000;
	
	private boolean gameOver = false;
	private MCTris plugin;
	
	
	public Game(World gameWorld, MCTris plugin) {
		this.gameWorld = gameWorld;
		this.plugin = plugin;
		(new Thread(this)).start();
	}
	
	@Override
	public void run() {
		try {
			this.clearArea();
			this.drawContainer();
			while(!gameOver) {
				this.tick();
				
				this.drawBoard();
				Thread.sleep(TICK_LENGTH);
			}
			
			this.clearBoard();
			this.curPiece = null;
			this.drawBoard();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//generate a new piece and reset the curPieceLocation
	private void nextPiece() {
		this.curPiece = new Piece();
		this.curPieceLocation = new Cell(Board.PIECE_SPAWN_ROW, Board.PIECE_SPAWN_COL);
		if(!this.movePiece(0, 0)) {
			//game over!
			this.plugin.loseGame();
		}
	}
	
	//perform a tick (moving pieces and whatnot)
	private void tick() {
		if(!this.movePiece(-1, 0)) { //hit bottom... cement and continue
			this.board.cementPiece(this.curPiece, this.curPieceLocation);
			this.board.checkAndClearRows();
			this.nextPiece();
		}
	}
	
	//returns whether the move was successful.
	private boolean movePiece(int rows, int cols) {
		if(this.pieceCanMove(rows, cols)) {
			this.curPieceLocation.row = this.curPieceLocation.row + rows;
			this.curPieceLocation.col = this.curPieceLocation.col + cols;
			return true;
		}
		
		return false;
	}
	
	private boolean rotatePiece() {
		if(this.pieceCanRotate()) {
			this.curPiece.rotate();
			return true;
		}
		
		return false;
	}
	
	private boolean pieceCanMove(int rows, int cols) {
		Cell nextLoc = new Cell(this.curPieceLocation.row + rows, this.curPieceLocation.col + cols);
		
		return !this.cellsAreOccupied(this.curPiece.getAbsoluteCellsWithLocation(nextLoc));
	}
	
	private boolean pieceCanRotate() {
		return !this.cellsAreOccupied(this.curPiece.getAbsoluteCellsWithLocationAfterRotation(this.curPieceLocation));
	}
	
	//check to see if any cells are occupied from a list given
	private boolean cellsAreOccupied(ArrayList<Cell> cells) {
		for(Cell cell : cells) {
			if(this.board.getColorOfCell(cell) != 0) {
				return true; //cell is occupied
			}
		}
		
		return false;
	}
	
	public void drawBoard() {
		for(int row=0; row<Board.VISIBLE_HEIGHT; row++) {
			for(int col=0; col<Board.WIDTH; col++) {
				Cell cell = new Cell(row, col);
				int value = board.getColorOfCell(cell);
				Block b = this.getBlockAt(cell);
				b.setType(getMaterialForColor(value));
			}
		}
		
		this.drawPiece();
	}
	
	public void endCurrentGame() {
		this.gameOver = true;
	}
	
	public void drawContainer() {
		Material containerMaterial = Material.OBSIDIAN;
		
		//draw base
		for(int i=0; i<Board.WIDTH+2; i++) {
			this.getBlockAt(new Cell(-1,i-1)).setType(containerMaterial);
		}
			
		//draw left tower
		for(int i=0; i<Board.VISIBLE_HEIGHT+1; i++) {
			this.getBlockAt(new Cell(i,-1)).setType(containerMaterial);
		}
		
		//draw right tower
		for(int i=0; i<Board.VISIBLE_HEIGHT+1; i++) {
			this.getBlockAt(new Cell(i,Board.WIDTH)).setType(containerMaterial);
		}
	}
	
	//clears the area used by the game board
	public void clearArea() {
		for(int z=GAME_LOC_Z-1; z<GAME_LOC_Z+2; z++) {
			for(int x=GAME_LOC_X-2; x<GAME_LOC_X+Board.WIDTH+2; x++) {
				for(int y=GAME_LOC_Y-2; y<GAME_LOC_Y+Board.VISIBLE_HEIGHT+2; y++) {
					this.gameWorld.getBlockAt(x,y,z).setType(Material.AIR);
				}
			}
		}
	}
	
	public void clearBoard() {
		this.board.resetBoard();
		this.drawBoard();
	}
	
	private Block getBlockAt(Cell cell) {
		return this.gameWorld.getBlockAt(GAME_LOC_X + cell.col + 1, GAME_LOC_Y + cell.row + 1, GAME_LOC_Z);
	}
	
	private Material getMaterialForColor(int value) {
		Material result;
		
		switch(value) {
			case 0: result = Material.AIR; break;
			case 1: result = Material.GOLD_BLOCK; break;
			case 2: result = Material.DIAMOND_BLOCK; break;
			case 3: result = Material.LAPIS_BLOCK; break;
			case 4: result = Material.SNOW_BLOCK; break;
			default: result = Material.AIR;
		}
		
		return result;
	}
	
	//probably will only be called after drawBoard(). So don't use it for anything else.
	private void drawPiece() {
		if(this.curPiece == null) return;
		
		ArrayList<Cell> cells = this.curPiece.getAbsoluteCellsWithLocation(this.curPieceLocation);
		for(Cell cell : cells) {
			if(this.board.cellIsVisible(cell)) {
				this.getBlockAt(cell).setType(this.getMaterialForColor(this.curPiece.getColor()));
			}
		}
	}
	
	public void keyPress(MCTrisInput value) {
		if(value.equals(MCTrisInput.UP)) {
			this.rotatePiece();
		} else if(value.equals(MCTrisInput.DOWN)) {
			this.movePiece(-1, 0);
		} else if(value.equals(MCTrisInput.RIGHT)) {
			this.movePiece(0, -1);
		} else if(value.equals(MCTrisInput.LEFT)) {
			this.movePiece(0, 1);
		}
		
		this.drawBoard();
	}
}
