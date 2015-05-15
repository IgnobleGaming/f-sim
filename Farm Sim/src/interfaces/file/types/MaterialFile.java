package interfaces.file.types;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import interfaces.file.IFile;

import java.io.IOException;

public class MaterialFile extends IFile
{

	private Texture Material;
	private Type MaterialType;
	private int Width = 0;
	private int Height = 0;
	private int ID = 0;

	public enum Type
	{
		PNG, TGA
	}

	public MaterialFile(String Location, Type T)
	{
		super(Location, false, false);
		MaterialType = T;
	}

	public boolean Initialize()
	{

		try
		{
			Material = TextureLoader.getTexture(MaterialType.toString(), ResourceLoader.getResourceAsStream("resources/player.png"));
			Width = Material.getImageWidth();
			Height = Material.getImageHeight();
			ID = Material.getTextureID();
			return true;
		}

		catch (IOException InitError)
		{
			// log the initialization error
			return false;
		}

	}
	
	public int Width()
	{
		return Width;
	}
	
	
	public int Height()
	{
		return Height;
	}
	
	public void Bind()
	{
		Material.bind();
	}

}
