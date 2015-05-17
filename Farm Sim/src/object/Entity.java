package object;

import specifier.Direction;
import specifier.Vector;
import specifier.Vector2D;
import interfaces.Game;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;



import java.util.*;

public class Entity extends Renderable
{

	public enum Flag
	{
		COLLIDABLE, VISIBLE, INTERACTABLE, LOCKED
	}
	
	public enum State
	{
		STATIONARY, MOVINGLEFT, MOVINGRIGHT, MOVINGUP, MOVINGDOWN
	}

	protected int ID;
	protected String Name;
	protected String Description;
	protected Vector2D Position;
	protected Vector Velocity;
	protected EnumSet<Flag> Flags; // sadly we can't `bitwise and` :(
	protected State CurrentState;
	protected int MovementSpeed = 1;

	public Entity(String Name, String Desc, Vector2D Position, Vector Velocity, int width, int height, Flag... Flags)
	{
		super(width,height);
		
		this.Name = Name;
		this.Description = Desc;
		this.Position = Position;
		this.Velocity = Velocity;
		this.Flags = EnumSet.noneOf(Flag.class);
		for (Flag F : Flags)
			this.Flags.add(F);
		CurrentState = State.STATIONARY;
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
	
	public void Draw()
	{
		interfaces.Render.DrawImage(Sprites.get(0), Position);
	}
	
	public void Move(Direction.Relative Dir)
	{
		switch (Dir)
		{			
			case UP:
				Position.y -= MovementSpeed;
				break;
			case DOWN:
				Position.y += MovementSpeed;
				break;
			case LEFT:
				Position.x -= MovementSpeed;
				break;
			case RIGHT:
				Position.x += MovementSpeed;
				break;
		}
	}
	
	public void Update()
	{
		switch(CurrentState)
		{
			case STATIONARY:
				break;
			case MOVINGUP:
				this.Move(Direction.Relative.UP);
				break;
			case MOVINGDOWN:
				this.Move(Direction.Relative.DOWN);
				break;
			case MOVINGLEFT:
				this.Move(Direction.Relative.LEFT);
				break;
			case MOVINGRIGHT:
				this.Move(Direction.Relative.RIGHT);
				break;
		}
	}
	
	public void SetState(State S)
	{
		if (!Flags.contains(Flag.LOCKED))
			CurrentState = S;
	}
	
	public int MovementSpeed()
	{
		return MovementSpeed;
	}
	
	public void MovementSpeed(int factor)
	{
		MovementSpeed = factor;
	}
}
