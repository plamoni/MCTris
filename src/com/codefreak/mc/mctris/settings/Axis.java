package com.codefreak.mc.mctris.settings;

import org.bukkit.Location;

public enum Axis {
	PosX (1,0,0),
	PosY (0,1,0),
	PosZ (0,0,1),
	NegX (-1,0,0),
	NegY (0,-1,0),
	NegZ (0,0,-1);
	
	private int x;
	private int y;
	private int z;
	
	Axis(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Axis opposite() {
		switch(this) {
		case PosX: return Axis.NegX;
		case PosY: return Axis.NegY;
		case PosZ: return Axis.NegZ;
		case NegX: return Axis.PosX;
		case NegY: return Axis.PosY;
		case NegZ: return Axis.PosZ;
		}
		
		return null;
	}
	
	public boolean isPositive() {
		return (this == Axis.PosX || this == Axis.PosY || this == Axis.PosZ);
	}
	
	public boolean perpendicularTo(Axis a) {
		return !((this == a) || this.opposite() == a);
	}
	
	/***
	* Assuming a and b are perpendicular, this returns the third axis. 
	* For instance, Axis.otherAxis(Axis.PosX, Axis.PosY) == Axis.PosZ
	* The return will be positive or negative based on the "right hand rule" http://en.wikipedia.org/wiki/Right-hand_rule
	* If the two provided axes are the same or opposite one another, null is returned
	***/
	public static Axis otherAxis(Axis a, Axis b) {
		if(!a.perpendicularTo(b)) return null;	
		
		Axis ret = null;
		
		if(a.x == 0 && b.x == 0) ret = Axis.PosX;
		if(a.y == 0 && b.y == 0) ret = Axis.PosY;
		if(a.z == 0 && b.z == 0) ret = Axis.PosZ;
		
		return ret;
	}
	
	//Returns a location offset by a given distance along this axis.
	public Location offset(Location origin, double dist) {
		return new Location(origin.getWorld(),
							origin.getX() + (dist * this.x),
							origin.getY() + (dist * this.y),
							origin.getZ() + (dist * this.z));
	}
}
