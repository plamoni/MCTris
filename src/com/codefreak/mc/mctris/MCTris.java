package com.codefreak.mc.mctris;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.codefreak.mc.mctris.controls.MCTrisInput;
import com.codefreak.mc.mctris.controls.MCTrisPlayerListener;
import com.codefreak.mc.mctris.game.Game;

public class MCTris extends JavaPlugin {
	private Server server;
	private Game currentGame;
	private Player currentPlayer;
	private MCTrisPlayerListener playerListener = new MCTrisPlayerListener(this);
		
    public MCTris(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        this.server = instance;
    }

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Priority.Normal, this);
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        
    }
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " has been disabled!" );
    }
    
    public Player getCurrentPlayer() {
    	return this.currentPlayer;
    }
    
    public void setCurrentPlayer(Player player) {
    	this.currentPlayer = player;
    }
    
    public void gameInput(MCTrisInput value) {
    	this.currentGame.keyPress(value);
    }
    
    public void newGame(Player player) {
    	if(this.currentGame != null) this.currentGame.endCurrentGame();
    	
    	this.currentPlayer = player;
    	this.currentGame = new Game(this.currentPlayer.getWorld(), this);
		this.portCurPlayerToViewingArea();
    }
    
    public void endGame() {
    	this.currentGame.endCurrentGame();
		this.portCurPlayerToEndGame();
    	this.currentPlayer = null;
    }
    
    public void loseGame() {
    	this.currentGame.endCurrentGame();
		this.portCurPlayerToLose();
    	this.currentPlayer = null;
    }
    
    public void portCurPlayerToViewingArea() {
    	Location destination = new Location(this.currentPlayer.getWorld(), 68.5f, 66f, -2.5f, 0f, -26.25f);
    	this.currentPlayer.teleportTo(destination);
    }
    
    public void portCurPlayerToEndGame() {
    	Location destination = new Location(this.currentPlayer.getWorld(), 68.5f, 70f, -3.5f, 0f, -37f);
    	this.currentPlayer.teleportTo(destination);
    	
    }
    
    public void portCurPlayerToLose() {
    	Location destination = new Location(this.currentPlayer.getWorld(), 40.5f, 65f, -14f, 90f, 0f);
    	this.currentPlayer.teleportTo(destination);  
    }
}