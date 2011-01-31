package com.codefreak.mc.mctris.settings;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import org.bukkit.Server;
import org.bukkit.World;
import org.yaml.snakeyaml.Yaml;

import com.codefreak.mc.mctris.MCTrisLogManager;

public class MCTrisSettings {
	private World world;
	private boolean loaded = false;
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	
	public MCTrisSettings() {
		
	}
	
	public void generateDefaults(World world, Location player_location) {
		int yaw = ((int)player_location.getYaw() % 360);
		if(yaw >= 45 && yaw < 135) {
			this.dataMap.put("RightDirection", Axis.NegZ.toString());
		} else if(yaw >= 135 && yaw < 225) {
			this.dataMap.put("RightDirection", Axis.NegX.toString());
		} else if(yaw >= 225 || yaw < 315) {
			this.dataMap.put("RightDirection", Axis.PosZ.toString());
		} else {
			this.dataMap.put("RightDirection", Axis.PosX.toString());
		}
		
		this.dataMap.put("BoardLocation", new Float[] {80f, 77f, -13f, 0f, 0f});
		this.dataMap.put("PlayLocation", arrayForLocation(player_location));
		this.dataMap.put("LeaveGameLocation", arrayForLocation(player_location));
		this.dataMap.put("LoseGameLocation", arrayForLocation(player_location));
		this.dataMap.put("UpDirection", Axis.PosY.toString());
		this.dataMap.put("WorldId", world.getId());
		
		this.loaded = true;
		MCTrisLogManager.logger.fine(new Yaml().dump(this.dataMap));
	}
	
	public static World worldById(Server server, long world_id) {
		World[] worlds = server.getWorlds();
		for(World world : worlds) {
			if(world.getId() == world_id) {
				return world;
			}
		}
		
		return null;
	}
	
	public static long worldId() {
		//return world_id;
		return 5869420205761920628L;
	}
	
	public void setWorld(World world) {
		this.world = world;
		this.dataMap.put("WorldId", world.getId());
	}
	
	public void setWorld(Server server, long world_id) {
		this.setWorld(MCTrisSettings.worldById(server, world_id));
	}
	
	private static Float[] arrayForLocation(Location loc) {
		return (new Float[] {
			(float)loc.getX(),
			(float)loc.getY(),
			(float)loc.getZ(),
			loc.getYaw(),
			loc.getPitch()
		});
	}
	
    public Location locPlay() {
    	if(!this.loaded) return null;
    	return this.loc("PlayLocation");
    }
    
    
    public Location locLeaveGame() {
    	if(!this.loaded) return null;
    	return this.loc("LeaveGameLocation");   	
    }
    

    public Location locLoseGame() {
    	if(!this.loaded) return null;
    	return this.loc("LoseGameLocation");
    }
    
    //This is the bottom, left-hand block of the board. The board is 12 blocks wide and 19 blocks tall
    public Location locGameBoard() {
    	if(!this.loaded) return null;
    	return this.loc("BoardLocation");
    }
        
    public Axis UpDir() {
    	if(!this.loaded) return null;
    	return Axis.valueOf((String)this.dataMap.get("UpDirection"));
    }
    
    public Axis RightDir() {
    	if(!this.loaded) return null;
    	return Axis.valueOf((String)this.dataMap.get("RightDirection"));
    }
    
    private Location loc(String key) {
    	return new Location(this.world,
    			((Float[])this.dataMap.get(key))[0], //X
    			((Float[])this.dataMap.get(key))[1], //Y
    			((Float[])this.dataMap.get(key))[2], //Z
    			((Float[])this.dataMap.get(key))[3], //Pitch
    			((Float[])this.dataMap.get(key))[4]);//Yaw
    }
}
