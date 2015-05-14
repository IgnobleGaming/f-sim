package object;

import specifier.Vector;
import specifier.Vector2D;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;

import java.util.*;

public class Entity extends Renderable
{

	public enum Flag
	{
		COLLIDABLE, VISIBLE, INTERACTABLE, LOCKED
	}

	protected int ID;
	protected String Name;
	protected String Description;
	protected Vector2D Position;
	protected Vector Velocity;
	protected MaterialFile Sprite;
	protected EnumSet<Flag> Flags; // sadly we can't `bitwise and` :(

	public Entity(String Name, String Desc, Vector2D Position, Vector Velocity, MaterialFile Sprite, Flag Flags)
	{
		this.Name = Name;
		this.Description = Desc;
		this.Position = Position;
		this.Velocity = Velocity;
		this.Sprite = Sprite;
		this.Flags = EnumSet.noneOf(Flag.class);
		this.Flags.add(Flags);
	}

	/**
	 * Name
	 * 
	 * @param NewName
	 */
	public void Name(String NewName)
	{
		if (!Flags.contains(Flag.LOCKED))
			Name = NewName;
	}

	public String Name()
	{
		return Name;
	}

	public void Description(String NewDesc)
	{
		if (!Flags.contains(Flag.LOCKED))
			Description = NewDesc;
	}

	public String Description()
	{
		return Description;
	}

	public void Position(Vector2D NewPosition)
	{
		if (!Flags.contains(Flag.LOCKED))
			Position = NewPosition;
	}

	public Vector2D Position()
	{
		return Position;
	}

	public void Velocity(Vector NewVelocity)
	{
		if (!Flags.contains(Flag.LOCKED))
			Velocity = NewVelocity;
	}

	public Vector Velocity()
	{
		return Velocity;
	}

	public void AddFlag(Flag NewFlag)
	{
		if (!Flags.contains(Flag.LOCKED))
			Flags.add(NewFlag);
	}

	public boolean CheckFlag(Flag check)
	{
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(check))
			return true;

		return false;
	}

	public void RemoveFlag(Flag Removing)
	{
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(Removing))
			Flags.remove(Removing);
	}
}
