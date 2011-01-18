package com.codefreak.mc.mctris;

import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import com.codefreak.mc.mctris.controller.ControllerServer;

public class MCTris extends JavaPlugin {
	private ControllerServer cs;
	
    public MCTris(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        
        this.cs = new ControllerServer(9100);
        
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
}