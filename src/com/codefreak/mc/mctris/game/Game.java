package com.codefreak.mc.mctris.game;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.codefreak.mc.mctris.MCTris;
import com.codefreak.mc.mctris.controls.MCTrisInput;
import com.codefreak.mc.mctris.settings.Axis;


public class Game implements Runnable {
	private Board board = new Board();
	private Piece curPiece = new Piece();
	private Cell curPieceLocation = new Cell(Board.PIECE_SPAWN_ROW, Board.PIECE_SPAWN_COL);
	private World gameWorld;
	private Location boardLocation;
	private Axis upAxis;
	private Axis rightAxis;
		
	private static final int TICK_LENGTH = 1000;
	
	private boolean gameOver = false;
	private MCTris plugin;
	
	
	public Game(World gameWorld, MCTris plugin, Location boardLocation, Axis up, Axis right) {
		this.gameWorld = gameWorld;
		this.plugin = plugin;
		this.boardLocation = plugin.getSettings().locGameBoard();
		this.upAxis = up;
		this.rightAxis = right;
		
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
				b.setData(getDataForColor(value));
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
		for(int i=-1; i<Board.WIDTH+1; i++) {
			this.getBlockAt(new Cell(-1,i)).setType(containerMaterial);
		}
			
		//draw left tower
		for(int i=0; i<Board.VISIBLE_HEIGHT; i++) {
			this.getBlockAt(new Cell(i,-1)).setType(containerMaterial);
		}
		
		//draw right tower
		for(int i=0; i<Board.VISIBLE_HEIGHT; i++) {
			this.getBlockAt(new Cell(i,Board.WIDTH)).setType(containerMaterial);
		}
	
	}
	
	//clears the area used by the game board
	//TODO: Fix for axis modification
	public void clearArea() {
		Axis frontAxis = Axis.otherAxis(this.upAxis, this.rightAxis);
		
		for(int front_offset=-1; front_offset<=1; front_offset++) {
			for(int right_offset=-2; right_offset<Board.WIDTH+2; right_offset++) {
				for(int up_offset=-2; up_offset<Board.VISIBLE_HEIGHT+2; up_offset++) {
					Location tmp_loc = this.boardLocation;
					tmp_loc = frontAxis.offset(tmp_loc, front_offset);
					tmp_loc = this.rightAxis.offset(tmp_loc, right_offset);
					tmp_loc = this.upAxis.offset(tmp_loc, up_offset);
					
					this.gameWorld.getBlockAt(tmp_loc.getBlockX(), tmp_loc.getBlockY(), tmp_loc.getBlockZ()).setType(Material.AIR);
				}
			}
		}
	}
	
	public void clearBoard() {
		this.board.resetBoard();
		this.drawBoard();
	}
	
	//TODO: Fix for axis modification
	private Block getBlockAt(Cell cell) {
		Location loc = this.boardLocation;
		
		loc = this.rightAxis.offset(loc, cell.col + 1);
		loc = this.upAxis.offset(loc, cell.row + 1);
		
		return this.gameWorld.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	private Material getMaterialForColor(int value) {
		Material result;
		
		switch(value) {
			case 0: result = Material.AIR; break;
			case 1: 
			case 2: 
			case 3: 
			case 4: 
			case 5: result = Material.WOOL; break;
			default: result = Material.AIR;
		}
		
		return result;
	}
	
	private byte getDataForColor(int value) {
		byte result;
		
		switch(value) {
			case 0: result = 0; break;
			case 1: result = DyeColor.BLUE.getData(); break;
			case 2: result = DyeColor.GREEN.getData(); break;
			case 3: result = DyeColor.RED.getData(); break;
			case 4: result = DyeColor.YELLOW.getData(); break;
			case 5: result = DyeColor.ORANGE.getData(); break;
			default: result = 0;
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
				this.getBlockAt(cell).setData(this.getDataForColor(this.curPiece.getColor()));
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
