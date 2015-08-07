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
		STATIONARY(0), MOVINGLEFT(1), MOVINGRIGHT(2), MOVINGUP(3), MOVINGDOWN(4), INTERACTING(5);
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
	protected Vector2D LookAt;
	protected Vector Velocity;
	protected EnumSet<Flag> Flags; // sadly we can't `bitwise and` :(
	protected State CurrentState;
	protected int MovementSpeed = 5000;
	protected double MovementSpeedScale = 2;
	protected ArrayList<specifier.Animation> Animation;
	protected game.Tile LastTile;
	protected int[] CurrentTile;
	protected int CurrentStep = 0;
	protected int TotalMoveTime = 0;
	protected long LastMoveTime = 0;
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
		LastTile = game.Map.GetInstance().GetTileFromIndex(Position.x, Position.y);
		CurrentTile = game.Map.GetInstance().getIndexFromPos(Position.x, Position.y);
		Position(Position);
		LookAt = new Vector2D(0, 0);

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
		int StepSize = (MovementSpeed / game.Map.GetInstance().TileSize());

		//game.Tile CollisionTile = null;
		int XPlus = 0;
		int YPlus = 0;
		
		LookAt.x = 0;
		LookAt.y = 0;

		switch (Dir)
		{
			case UP:
				//CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.UP);
				YPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.y = -(CurrentSprite.Height() / 2);
				break;
			case DOWN:
				//CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.DOWN);
				YPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.y = (CurrentSprite.Height() / 2);
				break;
			case LEFT:
				//CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.LEFT);
				XPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.x = -(CurrentSprite.Width() / 2);
				break;
			case RIGHT:
				//CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.RIGHT);
				XPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.x = (CurrentSprite.Width() / 2);
				break;
		}

		//Logging.getInstance().Write(Type.DEBUG, "moving from tile index %d [ %d, %d ] => %d [ %d, %d ]", CurrentTile, this.Position().x, this.Position().y, CollisionTile.TileID, CollisionTile.Position().x, CollisionTile.Position().y);

		LastMoveTime += Game.GetInstance().Delta();

		if (LastMoveTime >= StepSize)
			TotalMoveTime += StepSize;

		if ((XPlus != 0 || YPlus != 0) && !Collide(Position().x + XPlus, Position().y + YPlus))
		{
			Position(new Vector2D(Position.x + XPlus, Position.y + YPlus));
			if (TotalMoveTime >= MovementSpeed)
			{
				// System.out.println("Entity.Move: CurrT - " + CurrentTile);
				TotalMoveTime = 0;
				LastMoveTime = 0;
			}
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
			case INTERACTING:
				this.Interact();
		}
		
		CurrentTile[0] = (int)Math.ceil(Position.x / (int)Variables.GetInstance().Get("m_tilesize").Current());
		CurrentTile[1] = (int)Math.ceil(Position.y / (int)Variables.GetInstance().Get("m_tilesize").Current());

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

	public void Interact()
	{
		Vector2D Targeting = new Vector2D(XPos + HitboxOffsetX + (HitboxWidth / 2) + LookAt.x, YPos + HitboxOffsetY + (HitboxHeight / 2) + LookAt.y);
		Renderable Target = null;

		for (Renderable R : interfaces.Objects.GetInstance().Entities())
		{
			if (R.IsTargetedBy(Targeting))
				Target = R;
		}

		if (Target == null)
			Target = game.Map.GetInstance().GetTileFromIndex(Targeting.x, Targeting.y);

		Interact(Target);
	}

	@Override
	public void Interact(Renderable R)
	{
		if (R == null)
			return;
		else if (R instanceof Tile)
		{
			if (((Tile) R).CheckFlag(Tile.Flag.INTERACTABLE))
				R.Interact(this);
		} else if (R instanceof Resource)
		{
			R.Interact(this);
		} else
		{

		}
	}

	// THIS IS WHERE COLLISION NEEDS WORK. THE FIRST 2 ARGS OF THE FIRST CONDITIONAL ARE RIGHT
	private boolean Collide(int x, int y)
	{
		Tile[] Tiles = game.Map.GetInstance().SurroundingTiles(this);
		
		boolean C1 = x - HitboxOffsetX <= 0;
		boolean C2 = x + HitboxOffsetX >= game.Map.GetInstance().maxPixelWidth() - 40;
		boolean C3 = y + HitboxOffsetY <= 0;
		boolean C4 = y + HitboxOffsetX >= game.Map.GetInstance().maxPixelHeight() - 40;
		
		if(C1 || C2 || C3 || C4)
			return true;
		
		for(Tile T : Tiles)
		{
			// to do tile collision
		}
		
		
		/*
		if (x - HitboxOffsetX <= 0 || x + HitboxOffsetX > (game.Map.GetInstance().VerticalTileNum() * game.Map.GetInstance().TileSize()) || y + HitboxOffsetY < 0 || y > (game.Map.GetInstance().HorizontalTileNum() * game.Map.GetInstance().TileSize()))
		{
			return true;
		}

		for (int i = 0; i < Tiles.length; i++)
		{
			Tile T = game.Map.GetInstance().GetTileFromIndex(Tiles[i]);

			if (T.CheckFlag(Tile.Flag.BLOCKED) && x + this.HitboxOffsetX + this.HitboxWidth >= game.Map.GetInstance().GetCoordPos(Tiles[i]).x + T.HitboxOffsetX()
					&& y + this.HitboxOffsetY + this.HitboxHeight >= game.Map.GetInstance().GetCoordPos(Tiles[i]).y + T.HitboxOffsetY() && x + this.HitboxOffsetX <= game.Map.GetInstance().GetCoordPos(Tiles[i]).x + T.HitboxOffsetX() + T.HitboxWidth()
					&& y + this.HitboxOffsetY <= game.Map.GetInstance().GetCoordPos(Tiles[i]).y + T.HitboxOffsetY() + T.HitboxHeight())
			{
				System.out.println("Entity.Collide ||| L - " + (x + this.HitboxOffsetX) + " | R - " + (x + this.HitboxOffsetX + this.HitboxWidth) + " | T - " + (y + this.HitboxOffsetY) + " | B - " + (y + this.HitboxOffsetY + this.HitboxHeight));
				return true;
			}
		}*/

		return false;
	}

	/*
	 * private boolean Collide(Tile T) { if (T.CheckFlag(Tile.Flag.BLOCKED) && this.Position.x + this.HitboxOffsetX + this.HitboxWidth > T.Position().x + T.HitboxOffsetX() && this.Position.y + HitboxOffsetY + HitboxHeight > T.Position().y + T.HitboxOffsetY() && this.Position.x + this.HitboxOffsetX <
	 * T.Position().x + T.HitboxOffsetX() + T.HitboxWidth() && this.Position.y + this.HitboxOffsetY < T.Position().y + T.HitboxOffsetY() + T.HitboxHeight()) return false; else return true; }
	 */

	public int[] CurrentTile()
	{
		return CurrentTile;
	}
}
