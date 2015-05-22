package object;

import specifier.Direction;
import specifier.Vector;
import specifier.Vector2D;
import game.Tile;
import interfaces.Game;
import interfaces.Variables;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;
import java.util.ArrayList;

import java.util.*;

public class Entity extends Renderable
{

	public enum Flag
	{
		COLLIDABLE, VISIBLE, INTERACTABLE, LOCKED
	}
	
	public enum State
	{
		STATIONARY(0), MOVINGLEFT(1), MOVINGRIGHT(2), MOVINGUP(3), MOVINGDOWN(4);
		private int val;

		private State(int val)
		{
			this.val = val;
		}
	};

	protected int ID;
	protected String Name;
	protected String Description;
	protected Vector2D Position;
	protected Vector Velocity;
	protected EnumSet<Flag> Flags; // sadly we can't `bitwise and` :(
	protected State CurrentState;
	protected int MovementSpeed = 1;
	protected ArrayList<specifier.Animation> Animation;
	protected game.Tile LastTile;

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
		Animation = new ArrayList<specifier.Animation>(5);
		for (Flag F : Flags)
		{
			Animation.add(null); // this is so we can access them by index later
		}
		LastTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y);
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
		if (CurrentSprite == null)
			CurrentSprite = null;
		interfaces.Render.DrawImage(CurrentSprite, Position);
	}
	
	@Override public void Move(Direction.Relative Dir)
	{
		int TileSize = (int)Variables.GetInstance().Get("m_tilesize").Current();
		
		if (LastTile != game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y + 16))
		{
			LastTile.RemoveFlag(game.Tile.Flag.BLOCKED);
			LastTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y + 16);
			LastTile.AddFlag(game.Tile.Flag.BLOCKED);
		}
		
		game.Tile CollisionTile;
		switch (Dir)
		{			
			case UP:
				CollisionTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y - 16 - TileSize/2);
				if (!CollisionTile.CheckFlag(Tile.Flag.BLOCKED) && CollisionTile.Position().y > 0 || LastTile == CollisionTile)
					Position.y -= MovementSpeed;
				break;
			case DOWN:
				CollisionTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y + 16 + TileSize/2);
				if (!CollisionTile.CheckFlag(Tile.Flag.BLOCKED) && CollisionTile.Position().y > 0 || LastTile == CollisionTile)
					Position.y += MovementSpeed;
				break;
			case LEFT:
				CollisionTile = game.Map.GetInstance().GetTileFromIndex(Position.x - TileSize/2, Position.y + 16);
				if (!CollisionTile.CheckFlag(Tile.Flag.BLOCKED) || LastTile == CollisionTile )
					Position.x -= MovementSpeed;
				break;
			case RIGHT:
				CollisionTile = game.Map.GetInstance().GetTileFromIndex(Position.x + TileSize/2, Position.y + 16);
				if (!CollisionTile.CheckFlag(Tile.Flag.BLOCKED) && CollisionTile.Position().y > 0 || LastTile == CollisionTile)
					Position.x += MovementSpeed;
				break;
			default:
					CollisionTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y -16);
		}
		
		LastTile.SetSprite(game.Mapbuilder.GetRandomTile());
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
		
		if (Animation.size() > CurrentState.val && Animation.get(CurrentState.val) != null)
		{
			MaterialFile NewSprite = Animation.get(CurrentState.val).RequestNextFrame();
			if (NewSprite != null)
				CurrentSprite = NewSprite;
		}
		else
			Logging.getInstance().Write(Type.WARNING, "entity \"%s\" has has no animation for current state \"%s\"", this.Name, this.CurrentState.toString());
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
	
	public void AddAnimation(State AnimState, int length, MaterialFile... Textures) // length the animation time
	{
		specifier.Animation NewAnim = new specifier.Animation(length, Textures);
		if (NewAnim.Valid)
			Animation.add(AnimState.val,NewAnim);
	}
}
