package game;

import java.io.IOException;
import java.util.EnumSet;

import object.Entity.Flag;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Tile {
	
	public enum Flag {
		BLOCKED, OCCUPIED, DRAWABLE, INTERACTABLE, FARMABLE, LOCKED
	}
	
	
	private int Height;
	private int Width;
	private Texture Texture;
	
	private EnumSet<Flag> Flags;
	//private Resource Res;
	
	public Tile() {
		init();
	}
	
	public void init() {
		try {
			Texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("butts"));
			//height = fm[].getData();
			//width  = fm[].getData();
		} catch (IOException e) {
			
		}
	}
	
	public int getHeight() {
		return Height;
	}
	
	public int getWidth() {
		return Width;
	}

	public void AddFlag(Flag NewFlag) {
		if (!Flags.contains(Flag.LOCKED))
			Flags.add(NewFlag);
	}

	public boolean CheckFlag(Flag check) {
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(check))
			return true;

		return false;
	}

	public void RemoveFlag(Flag Removing) {
		if (!Flags.contains(Flag.LOCKED) && Flags.contains(Removing))
			Flags.remove(Removing);
	}
}
