package object.actors;

import org.newdawn.slick.Color;

import game.Map;
import game.Tile;
import interfaces.Game;
import interfaces.Render;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import interfaces.file.types.MaterialFile;
import object.Entity;
import object.WorldObject.Flag;
import object.WorldObject;
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
		
		//System.out.println(CurrentTile.x + ", " + CurrentTile.y);
		
		if (Animation.size() > CurrentState.Value() && Animation.get(CurrentState.Value()) != null)
		{
			MaterialFile NewSprite = Animation.get(CurrentState.Value()).RequestNextFrame();
			if (NewSprite != null)
				CurrentSprite = NewSprite;
		} else
			Logging.getInstance().Write(Type.WARNING, "entity \"%s\" has has no animation for current state \"%s\"", this.Name, this.CurrentState.toString());
	
		
		HitboxOffsetX = -8;
		HitboxOffsetY = 0;
		HitboxHeight = 16;
		HitboxWidth = 16;
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
				YPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				//YPlus -= 5;
				LookAt.y = -(CurrentSprite.Height() / 4) * 3;
				break;
			case DOWN:
				//YPlus += 5;
				YPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.y = (CurrentSprite.Height() / 4) * 3;
				break;
			case LEFT:
				//XPlus -= 5;
				XPlus -= game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.x = -(CurrentSprite.Width());
				break;
			case RIGHT:
				//XPlus += 5;
				XPlus += game.Map.GetInstance().TileSize() / StepSize / 2 * 7;
				LookAt.x = (CurrentSprite.Width());
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
		WorldObject Target = null;

		for (Entity E : interfaces.Objects.GetInstance().Entities())
		{
			if (E.IsTargetedBy(Targeting))
				Target = E;
		}

		if (Target == null)
			Target = game.Map.GetInstance().GetTileFromPosition(Targeting.x, Targeting.y);

		Interact(Target);
		SetState(State.STATIONARY);
	}
	
	@Override
	public void Interact(WorldObject R)
	{
		if (R instanceof Tile)
		{
			switch (((Tile) R).Type())
			{
				case GRASS:
					((Tile) R).ChangeType(Tile.Type.DIRT);
					break;
				default:
					break;
			}
		}
	}
	
	public double MovementSpeed()
	{
		return MovementSpeed;
	}
	
	public void Draw()
	{
		super.Draw();
	
		//Tile T = Map.GetInstance().GetTileFromIndex(Map.GetInstance().GetIndexFromCoord(XPos + LookAt.x + (Map.GetInstance().TileSize() / 2), YPos + HitboxOffsetY + (HitboxHeight / 2) + LookAt.y + (Map.GetInstance().TileSize() / 2)));
		//Tile T = Map.GetInstance().GetTileFromIndex(Map.GetInstance().GetIndexFromCoord(XPos, YPos));
		Render.DrawQuad(XPos + HitboxOffsetX + (HitboxWidth / 2), YPos + HitboxOffsetY + (HitboxHeight / 2), HitboxWidth, HitboxHeight, Color.cyan);
		//System.out.println("t " + T.Position().x + " " + T.Position().y + " p " + this.XPos + " " + this.YPos);
		//interfaces.Render.DrawQuad(XPos + LookAt.x, YPos + HitboxOffsetY + (HitboxHeight / 2) + LookAt.y, 2, 2, Color.black);
		//interfaces.Render.DrawQuad(T.Position().x, T.Position().y , T.Width(), T.Height(), Color.red);
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
