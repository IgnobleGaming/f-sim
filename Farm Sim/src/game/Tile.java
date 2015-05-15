package game;

import java.io.IOException;
import java.util.EnumSet;

import object.Entity.Flag;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Tile extends renderable.Renderable
{

	public enum Flag
	{
		BLOCKED, RESOURCE, OCCUPIED, DRAWABLE, INTERACTABLE, FARMABLE, LOCKED
	}

	private int Height;
	private int Width;
	private Texture Texture;

	private EnumSet<Flag> Flags;

	private Object Res;

	public Tile()
	{
		init();
	}

	public void init()
	{
		try
		{
			// Change resourceLoader to file manager
			Texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("butts"));
			// height = fm[].getData();
			// width = fm[].getData();
		} catch (IOException e)
		{

		}
	}

	public int Height()
	{
		return Height;
	}

	public int Width()
	{
		return Width;
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
			this.Res = Resource;
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
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(check))
			return true;

		return false;
	}

	public void RemoveFlag(Flag Removing)
	{
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(Removing))
			Flags.remove(Removing);
	}
}
