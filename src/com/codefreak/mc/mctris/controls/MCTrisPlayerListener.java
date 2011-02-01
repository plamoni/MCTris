package com.codefreak.mc.mctris.controls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.codefreak.mc.mctris.MCTris;

public class MCTrisPlayerListener extends PlayerListener {
	private MCTris plugin;

		
	//Allow a player to pre-empt another player to play the game. Useful for testing.
	private static boolean ALLOW_PREEMPT = false;
	
	
	public MCTrisPlayerListener(MCTris plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerCommand(PlayerChatEvent event) {
		super.onPlayerCommand(event);
		
		Player player = event.getPlayer();
		if(event.getMessage().trim().equals("/play")) {
			if(ALLOW_PREEMPT || this.plugin.getCurrentPlayer() == null) {
				this.plugin.newGame(event.getPlayer());
			} else {
				player.sendMessage("Sorry, another player is playing. Try again later!");
			}
		} else if(event.getMessage().trim().equals("/endgame")) {
			if(this.plugin.getCurrentPlayer().equals(player)) {
				this.plugin.endGame();
			} else {
				player.sendMessage("Sorry, you are not playing!");
			}
		} 
		//REMOVE THESE (they are for my debugging):
		else if(event.getMessage().startsWith("/loc")) {
    		player.sendMessage(String.format("You are at [%.2f, %.2f, %.2f] [yaw: %.2f, pitch:%.2f]", (float)player.getLocation().getX(), 
					(float)player.getLocation().getY(), 
					(float)player.getLocation().getZ(),
					(float)player.getLocation().getYaw(),
					(float)player.getLocation().getPitch()));
		} else if(event.getMessage().startsWith("/set")) {
			Pattern setPat = Pattern.compile("^/set\\W([0-9]+)\\W([0-9]+)\\W([0-9]+)\\W([0-9]+)");
			Matcher m = setPat.matcher(event.getMessage());
			
			if(m.find()) {
				int x = Integer.parseInt(m.group(1));
				int y = Integer.parseInt(m.group(2));
				int z = Integer.parseInt(m.group(3));
				int value = Integer.parseInt(m.group(4));
				
				event.getPlayer().getWorld().getBlockAt(x, y, z).setTypeId(value);
			} else {
				event.getPlayer().sendMessage("FORMAT: /set <X> <Y> <Z> <VALUE>");
			}
		}
		//END REMOVE
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		super.onPlayerMove(event);
		
		if(event.getPlayer().equals(this.plugin.getCurrentPlayer())) {
			Location from = event.getFrom();
			Location to = event.getTo();
				
			double deltaX = to.getX() - from.getX();
			double deltaZ = to.getZ() - from.getZ();
			
			if(Math.abs(deltaZ) > Math.abs(deltaX)) { //then it's a "Z" move
				if(deltaZ > 0) {
					this.plugin.gameInput(MCTrisInput.UP);
					event.setCancelled(true);
				} else {
					this.plugin.gameInput(MCTrisInput.DOWN);
					event.setCancelled(true);
				}
			} else { //then it's an "X" move...
				if(deltaX > 0) {
					this.plugin.gameInput(MCTrisInput.LEFT);
					event.setCancelled(true);
				} else {
					this.plugin.gameInput(MCTrisInput.RIGHT);
					event.setCancelled(true);
				}
			}
			
			event.getPlayer().teleportTo(this.plugin.getPlayLocation());
		}
	}
}
