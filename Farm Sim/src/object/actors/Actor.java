package object.actors;

import interfaces.Game;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import interfaces.file.types.MaterialFile;
import object.Entity;
import object.Entity.Flag;
import renderable.Renderable;
import specifier.Direction.Relative;
import specifier.Vector;
import specifier.Vector2D;

public class Actor extends Entity
{
	
	private Stats PlayerStats;
	protected Vector2D LookAt;
	
	protected int MovementSpeed = 5000;
	protected double MovementSpeedScale = 2;
	protected Vector2D CurrentTile;
	protected int CurrentStep = 0;
	protected int TotalMoveTime = 0;
	protected long LastMoveTime = 0;
	
	public Actor(String Name, String Desc, Vector2D Position, Vector Velocity, int width, int height, Flag... Flags)
	{
		super(Name, Desc, Position, Velocity, width, height, Flags);
		PlayerStats = new Stats();
		

		LookAt = new Vector2D(0, 0);
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
	
	public void Update()
	{
		switch (CurrentState)
		{
			case STATIONARY:
				break;
			case MOVINGUP:
				this.Move(specifier.Direction.Relative.UP);
				break;
			case MOVINGDOWN:
				this.Move(specifier.Direction.Relative.DOWN);
				break;
			case MOVINGLEFT:
				this.Move(specifier.Direction.Relative.LEFT);
				break;
			case MOVINGRIGHT:
				this.Move(specifier.Direction.Relative.RIGHT);
				break;
			case INTERACTING:
				this.Interact();
		}
		
		CurrentTile = game.Map.GetInstance().GetIndexFromCoord(Position.x, Position.y);

		if (Animation.size() > CurrentState.State() && Animation.get(CurrentState.State()) != null)
		{
			MaterialFile NewSprite = Animation.get(CurrentState.State()).RequestNextFrame();
			if (NewSprite != null)
				CurrentSprite = NewSprite;
		} else
			Logging.getInstance().Write(Type.WARNING, "entity \"%s\" has has no animation for current state \"%s\"", this.Name, this.CurrentState.toString());
	
	}
	
	public void Move(Relative Dir)
	{
		int StepSize = (MovementSpeed / game.Map.GetInstance().TileSize());

		// game.Tile CollisionTile = null;
		int XPlus = 0;
		int YPlus = 0;

		LookAt.x = 0;
		LookAt.y = 0;

		switch (Dir)
		{
			case UP:
				// CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.UP);
				YPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.y = -(CurrentSprite.Height() / 2);
				break;
			case DOWN:
				// CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.DOWN);
				YPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.y = (CurrentSprite.Height() / 2);
				break;
			case LEFT:
				// CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.LEFT);
				XPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.x = -(CurrentSprite.Width() / 2);
				break;
			case RIGHT:
				// CollisionTile = game.Map.GetInstance().GetNextTile(CurrentTile, specifier.Direction.Relative.RIGHT);
				XPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.x = (CurrentSprite.Width() / 2);
				break;
		}

		// Logging.getInstance().Write(Type.DEBUG, "moving from tile index %d [ %d, %d ] => %d [ %d, %d ]", CurrentTile, this.Position().x, this.Position().y, CollisionTile.TileID, CollisionTile.Position().x, CollisionTile.Position().y);

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
	
	public void Interact()
	{
		Vector2D Targeting = new Vector2D(XPos + HitboxOffsetX + (HitboxWidth / 2) + LookAt.x, YPos + HitboxOffsetY + (HitboxHeight / 2) + LookAt.y);
		Renderable Target = null;

		for (Entity E : interfaces.Objects.GetInstance().Entities())
		{
			if (E.IsTargetedBy(Targeting))
				Target = E;
		}

		if (Target == null)
			Target = game.Map.GetInstance().GetTileFromIndex(Targeting.x, Targeting.y);

		Interact(Target);
	}
	
	@Override
	public void Interact(Renderable R)
	{
		
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
}
