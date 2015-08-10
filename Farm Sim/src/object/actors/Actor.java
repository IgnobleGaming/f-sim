package object.actors;

import object.Entity;
import specifier.Vector;
import specifier.Vector2D;

public class Actor extends Entity
{
	
	private Stats PlayerStats;
	
	public Actor(String Name, String Desc, Vector2D Position, Vector Velocity, int width, int height, Flag[] Flags)
	{
		super(Name, Desc, Position, Velocity, width, height, Flags);
		PlayerStats = new Stats();
	}
	
	public int Health()
	{
		return PlayerStats.Health().Current();
	}
	
	public int Fatigue()
	{
		return PlayerStats.Fatigue().Current();
	}
	
	public int Hunger()
	{
		return PlayerStats.Hunger().Current();
	}

}
