package object;

import java.util.EnumSet;

import renderable.Renderable;
import specifier.Vector2D;

public class WorldObject extends Renderable
{
	public enum Flag
	{
		COLLIDABLE, VISIBLE, INTERACTABLE, LOCKED, NONE
	}
	
	protected int HitboxOffsetX;
	protected int HitboxOffsetY;
	protected int HitboxHeight;
	protected int HitboxWidth;
	
	protected EnumSet<Flag> Flags;
	
	
	
	protected WorldObject(int width, int height)
	{
		super(width, height);
		
		HitboxOffsetX = 0;
		HitboxOffsetY = 0;
		HitboxHeight = 0;
		HitboxWidth = 0;
	}

	public int HitboxOffsetX()
	{
		return HitboxOffsetX;
	}
	
	public int HitboxOffsetY()
	{
		return HitboxOffsetY;
	}

	public int HitboxHeight()
	{
		return HitboxHeight;
	}

	
	public int HitboxWidth()
	{
		return HitboxWidth;
	}
	
	public boolean IsTargetedBy(Vector2D V)
	{
		if (V.x >= this.XPos + this.HitboxOffsetX && V.x <= this.XPos + this.HitboxOffsetX + this.HitboxWidth && V.y >= this.YPos + this.HitboxOffsetY && V.y <= this.YPos + this.HitboxOffsetY + this.HitboxHeight)
		{
			return true;
		}
		else 
			return false;
	}

	public void Interact(WorldObject R)
	{
		// TODO Auto-generated method stub
		
	}
}
