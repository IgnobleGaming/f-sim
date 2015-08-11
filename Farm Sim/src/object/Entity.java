package object;

import specifier.Direction;
import specifier.Vector;
import specifier.Vector2D;
import game.Tile;
import interfaces.Game;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;

import java.util.*;

import org.newdawn.slick.Color;

public class Entity extends WorldObject
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
		STATIONARY(0), MOVINGLEFT(1), MOVINGRIGHT(2), MOVINGUP(3), MOVINGDOWN(4), INTERACTING(5);
		protected int val;

		private State(int val)
		{
			this.val = val;
		}
		
		public int State()
		{
			return val;
		}
	};

	protected int ID;
	protected String Name;
	protected String Description;
	protected Vector2D Position;
	protected Vector Velocity;
	protected EnumSet<Flag> Flags;
	protected State CurrentState;
	protected ArrayList<specifier.Animation> Animation;
	protected Vector2D CurrentTile;
	protected MaterialFile Shadow;

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
		this.ZIndex(2);
		this.Flags = EnumSet.noneOf(Flag.class);
		if (Flags != null)
		{
			for (Flag F : Flags)
				this.Flags.add(F);
		}
		CurrentState = State.STATIONARY;
		Animation = new ArrayList<specifier.Animation>(5);
		for (int i = 0; i < 5; i++)
		{
			Animation.add(null); // this is so we can access them by index later
		}
		CurrentTile = game.Map.GetInstance().GetIndexFromCoord(Position.x, Position.y);
		Position(Position);

		Shadow = new MaterialFile("resources\\test_shadow.png", MaterialFile.Type.PNG);
		Shadow.Open();

		HitboxOffsetX = -11;
		HitboxOffsetY = 9;
		HitboxHeight = 7;
		HitboxWidth = 22;
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
		{
			Position = NewPosition;
			XPos = Position.x;
			YPos = Position.y;
		}
	}

	public Vector2D Position()
	{
		return Position;
	}

	public void SetPosition(Vector2D NewPosition)
	{
		this.Position(NewPosition);
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

		interfaces.Render.DrawImage(Shadow, new Vector2D(Position.x, Position.y + 4));
		interfaces.Render.DrawImage(CurrentSprite, Position);

		interfaces.Render.DrawQuad(XPos, YPos + this.HitboxHeight / 2 + this.HitboxOffsetY, this.HitboxWidth, this.HitboxHeight, Color.orange);
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

	}

	/**
	 * Process the current entity's state ( does not change state )
	 */
	public void Update()
	{
		
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

	public void Interact()
	{

	}

	@Override
	public void Interact(Renderable R)
	{
		
	}

	// THIS IS WHERE COLLISION NEEDS WORK. THE FIRST 2 ARGS OF THE FIRST CONDITIONAL ARE RIGHT
	protected boolean Collide(int x, int y)
	{
		Tile[] Tiles = game.Map.GetInstance().SurroundingTiles(this);

		boolean C1 = x - HitboxOffsetX <= 0;
		boolean C2 = x + HitboxOffsetX >= game.Map.GetInstance().maxPixelWidth() - 40;
		boolean C3 = y + HitboxOffsetY <= 0;
		boolean C4 = y + HitboxOffsetX >= game.Map.GetInstance().maxPixelHeight() - 40;

		if (C1 || C2 || C3 || C4)
			return true;

		for (Tile T : Tiles)
		{
			// to do tile collision
		}

		/*
		 * if (x - HitboxOffsetX <= 0 || x + HitboxOffsetX > (game.Map.GetInstance().VerticalTileNum() * game.Map.GetInstance().TileSize()) || y + HitboxOffsetY < 0 || y > (game.Map.GetInstance().HorizontalTileNum() * game.Map.GetInstance().TileSize())) { return true; }
		 * 
		 * for (int i = 0; i < Tiles.length; i++) { Tile T = game.Map.GetInstance().GetTileFromIndex(Tiles[i]);
		 * 
		 * if (T.CheckFlag(Tile.Flag.BLOCKED) && x + this.HitboxOffsetX + this.HitboxWidth >= game.Map.GetInstance().GetCoordPos(Tiles[i]).x + T.HitboxOffsetX() && y + this.HitboxOffsetY + this.HitboxHeight >= game.Map.GetInstance().GetCoordPos(Tiles[i]).y + T.HitboxOffsetY() && x +
		 * this.HitboxOffsetX <= game.Map.GetInstance().GetCoordPos(Tiles[i]).x + T.HitboxOffsetX() + T.HitboxWidth() && y + this.HitboxOffsetY <= game.Map.GetInstance().GetCoordPos(Tiles[i]).y + T.HitboxOffsetY() + T.HitboxHeight()) { System.out.println("Entity.Collide ||| L - " + (x +
		 * this.HitboxOffsetX) + " | R - " + (x + this.HitboxOffsetX + this.HitboxWidth) + " | T - " + (y + this.HitboxOffsetY) + " | B - " + (y + this.HitboxOffsetY + this.HitboxHeight)); return true; } }
		 */

		return false;
	}

	/*
	 * private boolean Collide(Tile T) { if (T.CheckFlag(Tile.Flag.BLOCKED) && this.Position.x + this.HitboxOffsetX + this.HitboxWidth > T.Position().x + T.HitboxOffsetX() && this.Position.y + HitboxOffsetY + HitboxHeight > T.Position().y + T.HitboxOffsetY() && this.Position.x + this.HitboxOffsetX <
	 * T.Position().x + T.HitboxOffsetX() + T.HitboxWidth() && this.Position.y + this.HitboxOffsetY < T.Position().y + T.HitboxOffsetY() + T.HitboxHeight()) return false; else return true; }
	 */

	public Vector2D CurrentTile()
	{
		return CurrentTile;
	}
}
