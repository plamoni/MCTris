package com.codefreak.mc.mctris;

import java.io.File;
import java.net.InetAddress;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import com.codefreak.mc.mctris.controller.ControllerServer;
import com.codefreak.mc.mctris.game.Game;

public class MCTris extends JavaPlugin {
	private ControllerServer cs;
	private Server server;
	private Game currentGame;
	
    public MCTris(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        this.server = instance;
        this.cs = new ControllerServer(9100, server, this);   
    }

    public void onEnable() {
        // Register our events
        //PluginManager pm = getServer().getPluginManager();
        //pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Normal, this);
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        
    }
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " has been disabled!" );
    }
    
    //Would be nice if Minecraft sent all keypresses. But it doesn't. So here's to hoping it might someday.
    public void onKeyPress(int key) {
    	if(key == 'n') {
    		Player p = this.getActivePlayer();
			Block b = p.getWorld().getBlockAt(45, 90, 11);
			
			System.out.println("TOGGLE");
			if(b.getType() == Material.AIR)
				b.setType(Material.COBBLESTONE);
			else
				b.setType(Material.AIR);
    	}
    	//this.currentGame.keyPress(key);
    }
    
    
    public Player getActivePlayer() {
    	InetAddress address = this.cs.getInetAddressOfActivePlayer();
    	
    	for(Player p : this.server.getOnlinePlayers()) {
    		if(p.getAddress().equals(address)) {
    			return p;
    		}
    	}
    	
    	return null;
    }
}