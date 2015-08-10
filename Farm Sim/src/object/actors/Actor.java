package object.actors;

import object.Entity;
import specifier.Vector;
import specifier.Vector2D;

public class Actor extends Entity
{
	
	public Actor(String Name, String Desc, Vector2D Position, Vector Velocity, int width, int height, Flag[] Flags)
	{
		super(Name, Desc, Position, Velocity, width, height, Flags);
	}

}
