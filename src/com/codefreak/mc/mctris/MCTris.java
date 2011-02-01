package com.codefreak.mc.mctris;

import java.io.File;
import java.util.logging.Logger;

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
import com.codefreak.mc.mctris.settings.MCTrisSettings;

public class MCTris extends JavaPlugin {
	private Server server;
	private Game currentGame;
	private Player currentPlayer;
	private MCTrisSettings settings = new MCTrisSettings();
	private MCTrisPlayerListener playerListener = new MCTrisPlayerListener(this);
	
	public static Logger logger = MCTrisLogManager.logger;
		
    public MCTris(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        MCTrisLogManager.init();
        this.server = instance;
    }
    
    public MCTrisSettings getSettings() {
    	return this.settings;
    }

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Priority.Normal, this);
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        
    }
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
    	logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " has been disabled!" );
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
    	this.server.broadcastMessage(this.currentPlayer.getDisplayName() + " has begun a game of MCTris! Good luck!");
    	if(this.currentGame != null) this.currentGame.endCurrentGame();
    	
    	this.currentPlayer = player;

    	//REMOVE THIS:
    	this.settings.generateBoard(player.getWorld(), player.getLocation());
    	//END REMOVE
    	
    	this.currentGame = new Game(this.currentPlayer.getWorld(), this, settings.locGameBoard(), settings.UpDir(), settings.RightDir());
    	this.currentPlayer.teleportTo(settings.locPlay());
    }
    
    public void endGame() {
    	this.server.broadcastMessage(this.currentPlayer.getDisplayName() + " has quit MCTris!");
    	this.currentGame.endCurrentGame();
    	this.currentPlayer.teleportTo(settings.locLeaveGame());
    	this.currentPlayer = null;
    }
    
    public void loseGame() {
    	this.server.broadcastMessage(this.currentPlayer.getDisplayName() + " has lost at MCTris!");
    	this.currentGame.endCurrentGame();
    	this.currentPlayer.teleportTo(settings.locLoseGame());
    	this.currentPlayer = null;
    }
    
    public Location getPlayLocation() {
    	return settings.locPlay();
    }
    
    public void checkIfPlayerOnline() {
    	if(!this.currentPlayer.isOnline()) {
    		this.endGame();
    	}
    }
}