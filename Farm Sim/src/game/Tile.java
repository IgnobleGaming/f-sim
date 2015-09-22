package game;

import interfaces.Render;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.MaterialFile;

import java.util.EnumSet;
import java.util.Random;

import object.Entity;
import object.WorldObject;

import org.newdawn.slick.Color;

import renderable.GUIFont.FontFamily;

public class Tile extends WorldObject
{

	public enum Type
	{
		OCEAN, SAND, GRASS, POND, DIRT, MOUNTAIN, NONE
	}

	//private EnumSet<Flag> Flags;
	private Type TileType;
	// private int hash;
	public int TileID = 0;
	private renderable.GUIFont debugText;
	public boolean Hitbox = false;

	public Tile(Type T)
	{
		super(32, 32);
		Flags = EnumSet.of(Flag.VISIBLE);
		this.ZIndex(-1);
		if ((boolean) Variables.GetInstance().Get("g_debuginfo").Current() == false)
		{
			 debugText = new renderable.GUIFont(FontFamily.Consolas, "" + (this.YPos), renderable.GUIFont.Size.LARGE, Color.red, new specifier.Vector2D(XPos,YPos));
			 debugText.ZIndex(100);
		}
		ChangeType(T);
	}

	public int Height()
	{
		return super.Height();
	}

	public int Width()
	{
		return super.Width();
	}

	public boolean interact()
	{
		if (Flags.contains(Flag.LOCKED) || !Flags.contains(Flag.INTERACTABLE))
			return false;

		return true;
	}

	public void Interact(renderable.Renderable R)
	{
		if (this.CheckFlag(Flag.LOCKED))
			return;

		if (R instanceof Entity)
		{
			if (TileType.equals(Type.GRASS))
			{
				this.ChangeType(Type.DIRT);

			}
			else 
			{
			}
		}
	}

	public void ChangeType(Type T)
	{
		if (T == Tile.Type.NONE)
			return;
		
		TileType = T;

		if (TileType == Type.OCEAN || TileType == Type.MOUNTAIN)
		{
			if (CheckFlag(Flag.INTERACTABLE))
				RemoveFlag(Flag.INTERACTABLE);
			if (!CheckFlag(Flag.COLLIDABLE))
				AddFlag(Flag.COLLIDABLE);
			HitboxOffsetX = -16;
			HitboxOffsetY = -16;
			HitboxHeight = 32;
			HitboxWidth = 32;
		} else if (TileType == Type.GRASS)
		{
			if(CheckFlag(Flag.COLLIDABLE))
				RemoveFlag(Flag.COLLIDABLE);
			if (!CheckFlag(Flag.INTERACTABLE))
				AddFlag(Flag.INTERACTABLE);
		} else
		{
			if (CheckFlag(Flag.INTERACTABLE))
				RemoveFlag(Flag.INTERACTABLE);
			if (CheckFlag(Flag.COLLIDABLE))
				RemoveFlag(Flag.COLLIDABLE);

			this.HitboxOffsetX = -16;
			this.HitboxOffsetY = -16;
			this.HitboxHeight = 32;
			this.HitboxWidth = 32;
		}

		UpdateTexture();
	}

	private void UpdateTexture()
	{

		// Sprites.clear();
		

		Random rand = new Random();

		switch (TileType)
		{
			case GRASS:
				int g = rand.nextInt(6);
				switch (g)
				{
					case 0:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\g1.png");
						break;
					case 1:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\g2.png");
						break;
					case 2:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\g3.png");
						break;
					case 3:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\g4.png");
						break;
					case 4:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\g5.png");
						break;
					case 5:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\g6.png");
						break;
				}

				break;
			case DIRT:
				CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\dirt.png");
				break;
			case OCEAN:
				CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\water.png");
				break;
			case SAND:
				int s = rand.nextInt(6);
				switch (s)
				{
					case 0:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand1.png");
						break;
					case 1:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand2.png");
						break;
					case 2:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand3.png");
						break;
					case 3:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand4.png");
						break;
					case 4:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand5.png");
						break;
					case 5:
						CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand6.png");
						break;
				}
				break;
			case MOUNTAIN:
				CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\mountain.png");
				break;
		}
	}

	public String TileType()
	{
		switch (TileType)
		{
			case GRASS:
				return "Grass";
			case DIRT:
				return "Dirt";
			case OCEAN:
				return "Water";
			case SAND:
				return "Sand";
			case MOUNTAIN:
				return "Mountain";
		}

		return "Butts";
	}

	public void AddFlag(Flag NewFlag)
	{
		if (!Flags.contains(Flag.LOCKED) && !Flags.contains(NewFlag))
		{
			Flags.add(NewFlag);
			Logging.getInstance().Write(Logging.Type.INFO, "added flag \"%s\" for tile at ( %d, %d )", NewFlag.toString(), this.Position().x, this.Position().y);
		}
	}

	public boolean CheckFlag(Flag check)
	{
		if (Flags.contains(check))
			return true;
		return false;
	}

	public void RemoveFlag(Flag Removing)
	{
		if (!Flags.contains(Flag.LOCKED))
		{
			Flags.remove(Removing);
			Logging.getInstance().Write(Logging.Type.INFO, "removed flag \"%s\" for tile at ( %d, %d )", Removing.toString(), this.Position().x, this.Position().y);
		}
	}

	public void Lock()
	{
		if (!Flags.contains(Flag.LOCKED))
			Flags.add(Flag.LOCKED);
	}

	public void Unlock()
	{
		if (Flags.contains(Flag.LOCKED))
			Flags.remove(Flag.LOCKED);
	}

	public void Draw()
	{
		if (debugText != null)
		{
			//debugText.Text("" + this.XPos);
			//debugText.Position(this.XPos, this.YPos);
			//debugText.Draw();
		}
		interfaces.Render.DrawImage(CurrentSprite, Position());
		
		if(Hitbox)
			Render.DrawQuad(Position().x, Position().y, HitboxWidth, HitboxHeight, Color.red);
	
	}

	public int ID()
	{
		return TileID;
	}

	public void ID(int id)
	{
		TileID = id;
	}

	public Type Type()
	{
		return this.TileType;
	}
}
