package object;

import specifier.Direction;
import specifier.Vector;
import specifier.Vector2D;
import game.Map;
import game.Tile;
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

	public enum State
	{
		STATIONARY(0), MOVINGLEFT(1), MOVINGRIGHT(2), MOVINGUP(3), MOVINGDOWN(4), INTERACTING(5);
		protected int val;

		private State(int val)
		{
			this.val = val;
		}

		public int Value()
		{
			return val;
		}
	};

	protected int ID;
	protected String Name;
	protected String Description;
	protected Vector2D Position;
	protected Vector Velocity;
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

		HitboxOffsetX = -10;
		HitboxOffsetY = 2;
		HitboxHeight = 16;
		HitboxWidth = 19;
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
		HitboxOffsetX = -10;
		HitboxOffsetY = 2;
		HitboxHeight = 16;
		HitboxWidth = 19;
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
	public void Interact(WorldObject R)
	{

	}

	// THIS IS WHERE COLLISION NEEDS WORK. THE FIRST 2 ARGS OF THE FIRST CONDITIONAL ARE RIGHT
	protected boolean Collide(int x, int y)
	{
		boolean C1 = x + HitboxOffsetX <= -Map.GetInstance().TileSize() / 2;
		boolean C2 = x + HitboxOffsetX + HitboxWidth >= game.Map.GetInstance().maxPixelWidth() - Map.GetInstance().TileSize() / 2;
		boolean C3 = y + HitboxOffsetY <= Map.GetInstance().TileSize() / 2;
		boolean C4 = y + HitboxOffsetY + HitboxHeight >= game.Map.GetInstance().maxPixelHeight() - Map.GetInstance().TileSize() / 2;

		if (C1 || C2 || C3 || C4)
			return true;

		Tile[] Tiles = game.Map.GetInstance().SurroundingTiles(x, y, this);
		
		for (Tile T : Tiles)
		{
			C1 = x + this.HitboxOffsetX < T.Position().x + T.HitboxOffsetX + T.HitboxWidth;
			C2 = x + this.HitboxOffsetX + this.HitboxWidth > T.Position().x + T.HitboxOffsetX();
			C3 = y + this.HitboxOffsetY < T.Position().y + T.HitboxOffsetY + T.HitboxHeight;
			C4 = y + this.HitboxOffsetY + this.HitboxHeight > T.Position().y + T.HitboxOffsetY();

			if (T.CheckFlag(Tile.Flag.COLLIDABLE) && C1 && C2 && C3 && C4)
			{
				MoveNearest(T);
				return true;
			}
		}

		return false;
	}
	
	private void MoveNearest(Tile T)
	{
		game.Map.Direction D = Map.GetCardinalPositionOfTarget(T.Position(), this.Position());
		
		switch (D)
		{
			case EAST:
				this.Position(new Vector2D(T.Position().x - (T.Width() - (T.Width() / 4)), this.Position().y));
				break;
			case NORTH:
				this.Position(new Vector2D(this.Position().x, T.Position().y + (T.Width() / 2)));
				break;
			case SOUTH:
				this.Position(new Vector2D(this.Position().x, T.Position().y - T.Width()));
				break;
			case WEST:
				this.Position(new Vector2D(T.Position().x + (T.Width() - (T.Width() / 4)), this.Position().y));
				break;
			case UNKNOWN:
			case CURRENT:
			default:
				break;
		}
	}


	public Vector2D CurrentTile()
	{
		return CurrentTile;
	}
	
	public specifier.Animation getAnimation(int index)
	{
		return Animation.get(index);
	}
	
	//later this will take in an arg that tells it what group of animations to update
	public void UpdateAnimSetColor(Color C)
	{
		for (specifier.Animation A : this.Animation)
			A.updateColorOverlay(C);
	}
}
