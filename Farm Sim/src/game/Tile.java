package game;

import java.io.IOException;
import java.util.EnumSet;

public class Tile extends renderable.Renderable
{

	public enum Flag
	{
		BLOCKED, RESOURCE, OCCUPIED, DRAWABLE, INTERACTABLE, FARMABLE, LOCKED
	}

	private EnumSet<Flag> Flags;
	
	public Tile()
	{
		super(32,32); // default tile size
		Flags = EnumSet.noneOf(Flag.class);
		Flags.add(Flag.DRAWABLE);
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
