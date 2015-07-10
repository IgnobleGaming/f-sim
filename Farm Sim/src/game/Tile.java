package game;

import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.types.MaterialFile;

import java.util.EnumSet;
import java.util.Random;

public class Tile extends renderable.Renderable
{

	public enum Flag
	{
		BLOCKED, RESOURCE, OCCUPIED, DRAWABLE, INTERACTABLE, FARMABLE, LOCKED
	}

	public enum Type
	{
		GRASS, WATER, DIRT, SAND, MOUNTAIN
	}

	private EnumSet<Flag> Flags;
	private Type TileType;
	private int hash;
	public int TileID = 0;
	private object.Resource Resource;

	public Tile()
	{
		super(32, 32); // default tile size
		Flags = EnumSet.of(Flag.DRAWABLE);
		
		this.ZIndex(0);
	}

	public Tile(Type T)
	{
		super(32, 32); // default tile size
		Flags = EnumSet.of(Flag.DRAWABLE);
		ChangeType(T);
		this.ZIndex(0);
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

	public void Resource(object.Resource Resource)
	{
		if (!Flags.contains(Flag.RESOURCE))
		{
			Flags.add(Flag.INTERACTABLE);
			Flags.add(Flag.OCCUPIED);
			Flags.add(Flag.RESOURCE);
			
			
			if (Resource != null)
			{
				this.Resource = Resource;
				this.Resource.Init(Position());
			}
		}
	}

	public void ChangeType(Type T)
	{
		TileType = T;

		if (TileType == Type.WATER)
			AddFlag(Flag.BLOCKED);
		else
			RemoveFlag(Flag.BLOCKED);

		UpdateTexture();
	}

	private void UpdateTexture()
	{

		// Sprites.clear();

		switch (TileType)
		{
			case GRASS:
				Random rand = new Random();
				int i = rand.nextInt(6);
				switch (i)
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
			case WATER:
				CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\water.png");
				break;
			case SAND:
				CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\sand.png");
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
			case WATER:
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
