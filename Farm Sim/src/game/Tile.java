package game;

import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.MaterialFile;

import java.util.EnumSet;
import java.util.Random;

import object.Entity;

import org.newdawn.slick.Color;

import renderable.GUIFont.FontFamily;

public class Tile extends renderable.Renderable
{

	public enum Flag
	{
		BLOCKED, RESOURCE, COLLIDABLE, DRAWABLE, INTERACTABLE, FARMABLE, LOCKED
	}

	public enum Type
	{
		OCEAN, SAND, GRASS, POND, DIRT, MOUNTAIN, NONE
	}

	private EnumSet<Flag> Flags;
	private Type TileType;
	// private int hash;
	public int TileID = 0;
	private object.Resource Resource;
	private renderable.GUIFont debugText;

	public Tile(Type T)
	{
		super(32, 32);
		Flags = EnumSet.of(Flag.DRAWABLE);
		this.ZIndex(-1);
		if ((boolean) Variables.GetInstance().Get("g_debuginfo").Current() == true)
		{
			 debugText = new renderable.GUIFont(FontFamily.Consolas, "" + (this.YPos), renderable.GUIFont.Size.LARGE, Color.red, this.XPos, this.YPos);
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

	public object.Resource Resource()
	{
		if (Resource != null)
			return Resource;
		return null;
	}

	public void Interact(renderable.Renderable R)
	{
		if (this.CheckFlag(Flag.LOCKED))
			return;

		if (R instanceof Entity)
		{
			if (this.CheckFlag(Flag.RESOURCE))
			{
				Resource.Interact(R);
				if (Resource.Depleted())
				{
					Resource = null;
					Flags.remove(Flag.RESOURCE);
					Flags.remove(Flag.BLOCKED);
					HitboxOffsetX = 0;
					HitboxOffsetY = 0;
					HitboxHeight = 0;
					HitboxWidth = 0;
					
					if ((TileType == Type.GRASS || TileType == Type.DIRT) && !CheckFlag(Flag.FARMABLE))
						AddFlag(Flag.FARMABLE);
				}
			} else if (TileType.equals(Type.GRASS))
			{
				this.ChangeType(Type.DIRT);

			}
			else 
			{
			}
		}
	}

	public void Resource(object.Resource Resource)
	{
		if (!Flags.contains(Flag.RESOURCE) && Resource != null)
		{
			Flags.add(Flag.INTERACTABLE);
			Flags.add(Flag.RESOURCE);
			Flags.add(Flag.BLOCKED);

			if (CheckFlag(Flag.FARMABLE))
				RemoveFlag(Flag.FARMABLE);

			HitboxOffsetX = 2;
			HitboxOffsetY = 7;
			HitboxHeight = 18;
			HitboxWidth = 28;

			this.Resource = Resource;
			this.Resource.Init(Position());
		}
	}

	public void ChangeType(Type T)
	{
		if (T == Tile.Type.NONE)
			return;
		
		TileType = T;

		if (TileType == Type.OCEAN)
		{
			if (CheckFlag(Flag.FARMABLE))
				RemoveFlag(Flag.FARMABLE);
			AddFlag(Flag.BLOCKED);
			HitboxOffsetX = 0;
			HitboxOffsetY = 0;
			HitboxHeight = 32;
			HitboxWidth = 32;
		} else if (TileType == Type.GRASS)
		{
			if (!CheckFlag(Flag.INTERACTABLE))
				AddFlag(Flag.INTERACTABLE);
			if (!CheckFlag(Flag.FARMABLE))
				AddFlag(Flag.FARMABLE);
		} else
		{
			if (CheckFlag(Flag.FARMABLE))
				RemoveFlag(Flag.FARMABLE);
			RemoveFlag(Flag.BLOCKED);

			this.HitboxOffsetX = 0;
			this.HitboxOffsetY = 0;
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
