package object;

import specifier.Direction;
import specifier.Vector;
import specifier.Vector2D;
import game.Tile;
import interfaces.Game;
import interfaces.Render;
import interfaces.Variables;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;

import java.util.*;

public class Entity extends Renderable
{
	/**
	 * Entity class extends Renderable
	 * 
	 * @author Michael
	 *
	 */
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
	protected int MovementSpeed = 5000;
	protected double MovementSpeedScale = 2;
	protected ArrayList<specifier.Animation> Animation;
	protected game.Tile LastTile;
	protected int CurrentTile;
	protected int CurrentStep = 0;
	protected int TotalMoveTime = 0;
	protected long LastMoveTime = 0;

	/**
	 * Creates a new entity
	 * 
	 * @param Name
	 *            ( display name of entity )
	 * @param Desc
	 *            ( display description of entity )
	 * @param Position
	 *            ( initial position )
	 * @param Velocity
	 *            ( initial velocity )
	 * @param width
	 *            ( width in pixels of ent )
	 * @param height
	 *            ( height in pixels of ent )
	 * @param Flags
	 *            ( initial array of flags )
	 */
	public Entity(String Name, String Desc, Vector2D Position, Vector Velocity, int width, int height, Flag... Flags)
	{
		super(width, height);
		this.Position = new Vector2D(0, 0);
		this.Name = Name;
		this.Description = Desc;
		this.Velocity = Velocity;
		this.ZIndex(11);
		this.Flags = EnumSet.noneOf(Flag.class);
		for (Flag F : Flags)
			this.Flags.add(F);
		CurrentState = State.STATIONARY;
		Animation = new ArrayList<specifier.Animation>(5);
		for (int i = 0; i < 5; i++)
		{
			Animation.add(null); // this is so we can access them by index later
		}
		LastTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y);
		CurrentTile = game.Map.GetInstance().GetCoordIndex(Position.x, Position.y);
		this.Position.x = game.Map.GetInstance().GetCoordPos(CurrentTile).x;
		this.Position.y = game.Map.GetInstance().GetCoordPos(CurrentTile).y;
	}

	/**
	 * Updates the current entity's name
	 * 
	 * @param NewName
	 *            ( string containing new name )
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

	/**
	 * Render the entity's current sprite ( overrides renderable draw )
	 */
	public void Draw()
	{
		if (CurrentSprite == null)
			CurrentSprite = null;
		interfaces.Render.DrawImage(CurrentSprite, Position);
	}

	/**
	 * Move the current entity in a specific direction
	 * 
	 * @param Dir
	 *            ( direction )
	 */
	@Override
	public void Move(Direction.Relative Dir)
	{
		/*
		 * int TileSize = (int)Variables.GetInstance().Get("m_tilesize").Current();
		 * 
		 * if (LastTile != game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y + 16)) { LastTile.RemoveFlag(game.Tile.Flag.BLOCKED); LastTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y + 16); LastTile.AddFlag(game.Tile.Flag.BLOCKED); }
		 */

		int StepSize = (MovementSpeed / game.Map.GetInstance().TileSize());

		game.Tile CollisionTile = null;
		int XPlus = 0;
		int YPlus = 0;

		switch (Dir)
		{
			case UP:
				CollisionTile = game.Map.GetInstance().GetNextTile(this.CurrentTile, specifier.Direction.Relative.UP);
				YPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 10;
				break;
			case DOWN:
				CollisionTile = game.Map.GetInstance().GetNextTile(this.CurrentTile, specifier.Direction.Relative.DOWN);
				YPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 10;
				break;
			case LEFT:
				CollisionTile = game.Map.GetInstance().GetNextTile(this.CurrentTile, specifier.Direction.Relative.LEFT);
				XPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 10;
				break;
			case RIGHT:
				CollisionTile = game.Map.GetInstance().GetNextTile(this.CurrentTile, specifier.Direction.Relative.RIGHT);
				XPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 10;
				break;
		}

		Logging.getInstance().Write(Type.DEBUG, "moving from tile index %d [ %d, %d ] => %d [ %d, %d ]", CurrentTile, this.Position().x, this.Position().y, CollisionTile.TileID, CollisionTile.Position().x, CollisionTile.Position().y);

		LastMoveTime += Game.GetInstance().Delta();

		if (LastMoveTime >= StepSize)
			TotalMoveTime += StepSize;

		//if (CollisionTile.TileID != CurrentTile)
		{
			Position.x += XPlus;
			Position.y += YPlus;
		}

		if (TotalMoveTime >= MovementSpeed)
		{
			CurrentTile = CollisionTile.TileID;
			TotalMoveTime = 0;
			LastMoveTime = 0;
		}
	}

	/**
	 * Process the current entity's state ( does not change state )
	 */
	public void Update()
	{
		switch (CurrentState)
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
		} else
			Logging.getInstance().Write(Type.WARNING, "entity \"%s\" has has no animation for current state \"%s\"", this.Name, this.CurrentState.toString());
	}

	/**
	 * Change the current entity's state
	 * 
	 * @param S
	 *            ( state to set to )
	 */
	public void SetState(State S)
	{
		if (!Flags.contains(Flag.LOCKED))
			CurrentState = S;
	}

	public double MovementSpeed()
	{
		return MovementSpeed;
	}

	/**
	 * Scale the entity's movement speed ( less precise factors work better )
	 * 
	 * @param factor
	 *            ( speed to scale by )
	 */
	public void MovementSpeed(double factor)
	{
		MovementSpeed = 500;
		for (specifier.Animation A : Animation)
		{
			if (A != null)
				A.UpdateSpeedScale(factor);
		}
	}

	/**
	 * Add a new animation to current entity
	 * 
	 * @param AnimState
	 *            ( the state that the animation is played in )
	 * @param length
	 *            ( how long the animation should last for )
	 * @param Textures
	 *            ( array of material files consisting of each frame *no nulls* )
	 */
	public void AddAnimation(State AnimState, int length, MaterialFile... Textures) // length the animation time
	{
		specifier.Animation NewAnim = new specifier.Animation(length, Textures);
		if (NewAnim.Valid)
			Animation.add(AnimState.val, NewAnim);
	}

	public int TileID()
	{
		return CurrentTile;
	}
}
