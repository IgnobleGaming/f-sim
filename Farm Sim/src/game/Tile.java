package game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Tile {
	
	private int height;
	private int width;
	private Texture texture;
	
	private boolean farmable;
	private boolean occupied;
	
	public Tile() {
		init();
	}
	
	public void init() {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("butts"));
			//height = fm[].getData();
			//width  = fm[].getData();
		} catch (IOException e) {
			
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
}
