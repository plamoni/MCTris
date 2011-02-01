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
	
	public void generateBoard(World world, Location player_location) {
		this.world = world;
		
		int yaw = (int)player_location.getYaw();
		while(yaw < 0) yaw += 360;
		
		Axis right_dir;
		
		if(yaw >= 45 && yaw < 135) {
			right_dir = Axis.NegZ;
		} else if(yaw >= 135 && yaw < 225) {
			right_dir = Axis.PosX;
		} else if(yaw >= 225 && yaw < 315) {
			right_dir = Axis.PosZ;
		} else {
			right_dir = Axis.NegX;
		}

		Axis front_dir;

		if(right_dir == Axis.NegZ) front_dir = Axis.NegX;
		else if(right_dir == Axis.PosX) front_dir = Axis.NegZ;
		else if(right_dir == Axis.PosZ) front_dir = Axis.PosX;
		else front_dir = Axis.PosZ;
		
		
		player_location.setPitch(-24);

		this.dataMap.put("FrontDirection", front_dir.toString());
		this.dataMap.put("RightDirection", right_dir.toString());
		this.dataMap.put("BoardLocation", arrayForLocation(generateDefaultBoardLocation(right_dir, front_dir, player_location)));
		this.dataMap.put("PlayLocation", arrayForLocation(player_location));
		this.dataMap.put("LeaveGameLocation", arrayForLocation(player_location));
		this.dataMap.put("LoseGameLocation", arrayForLocation(player_location));
		this.dataMap.put("WorldId", world.getId());
		
		this.loaded = true;
		MCTrisLogManager.logger.fine(new Yaml().dump(this.dataMap));
	}
	
	private static Location generateDefaultBoardLocation(Axis right_dir, Axis front_dir, Location playerLoc) {
		Location loc = playerLoc.clone();
		
		loc.setY(loc.getY() + 7);
		loc = right_dir.offset(loc, -5.5);
		loc = front_dir.offset(loc, 15);
		
		return loc;
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
	
	public long worldId() {
		//return world_id;
		return this.world.getId();
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
    	return Axis.PosY;
    }
    
    public Axis RightDir() {
    	if(!this.loaded) return null;
    	return Axis.valueOf((String)this.dataMap.get("RightDirection"));
    }
    
    public Axis FrontDir() {
    	if(!this.loaded) return null;
    	return Axis.valueOf((String)this.dataMap.get("FrontDirection"));
    }
    
    private Location loc(String key) {
    	Location ret = null;
    	try {
    		ret = new Location(this.world,
    			((Float[])this.dataMap.get(key))[0], //X
    			((Float[])this.dataMap.get(key))[1], //Y
    			((Float[])this.dataMap.get(key))[2], //Z
    			((Float[])this.dataMap.get(key))[3], //Pitch
    			((Float[])this.dataMap.get(key))[4]);//Yaw
    	} catch(ArrayIndexOutOfBoundsException e) {
    		MCTrisLogManager.logger.severe("Corrupted data map. This is probably due to a missing value in a location in the settings YAML file.");
    	}
    	return ret;
    }
}
