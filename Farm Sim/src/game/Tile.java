package game;

import interfaces.file.FileManager;
import interfaces.file.types.MaterialFile;

import java.io.IOException;
import java.util.EnumSet;

public class Tile extends renderable.Renderable
{

	public enum Flag
	{
		BLOCKED, RESOURCE, OCCUPIED, DRAWABLE, INTERACTABLE, FARMABLE, LOCKED
	}

	public enum Type
	{
		GRASS, WATER, DIRT
	}
	
	private EnumSet<Flag> Flags;
	private Type TileType;
	private int hash;
	
	public Tile()
	{
		super(32,32); // default tile size
		Flags = EnumSet.noneOf(Flag.class);
		Flags.add(Flag.DRAWABLE);
	}
	
	public Tile(Type T)
	{
		super(32,32); // default tile size
		Flags = EnumSet.noneOf(Flag.class);
		Flags.add(Flag.DRAWABLE);
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
	
	public void Resource(Object Resource)
	{
		if (!Flags.contains(Flag.RESOURCE))
		{
			Flags.add(Flag.INTERACTABLE);
			Flags.add(Flag.OCCUPIED);
		}
	}
	
	public void ChangeType(Type T)
	{
		TileType = T;
		
		if(TileType == Type.WATER)
			AddFlag(Flag.BLOCKED);
		else
			RemoveFlag(Flag.BLOCKED);
		
		UpdateTexture();
	}
	
	private void UpdateTexture()
	{
		
		Sprites.clear();
		
		switch (TileType)
		{
			case GRASS:
				Sprites.add((MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass.png"));
				break;
			case DIRT:
				Sprites.add((MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\dirt.png"));
				break;
			case WATER:
				Sprites.add((MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\water.png"));
				break;
		}
	}

	public void AddFlag(Flag NewFlag)
	{
		if (!Flags.contains(Flag.LOCKED))
			Flags.add(NewFlag);
	}

	public boolean CheckFlag(Flag check)
	{
		if (Flags.contains(check))
			return true;
		return false;
	}

	public void RemoveFlag(Flag Removing)
	{
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(Removing))
			Flags.remove(Removing);
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
		interfaces.Render.DrawPartialImage(Sprites.get(0), Position(), 0, 0, this.Height(), this.Width());
	}
}
