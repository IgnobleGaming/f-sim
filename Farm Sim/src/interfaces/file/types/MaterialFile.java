package interfaces.file.types;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import interfaces.file.IFile;
import interfaces.file.Logging;

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
	
	public boolean Open()
	{
		super.Open();
		try
		{
			Material = TextureLoader.getTexture(MaterialType.toString(), getInputStream());
			Width = Material.getImageWidth();
			Height = Material.getImageHeight();
			ID = Material.getTextureID();
			return true;
		}

		catch (IOException InitError)
		{
			Logging.getInstance().Write(Logging.Type.ERROR, "Unable read date for material file %s - %s", Hash, InitError.getMessage());
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
	
	public Texture Texture()
	{
		return Material;
	}
	
	public void Scale(double Factor)
	{
		this.Width = (int)Math.round(Width*Factor);
		this.Height = (int)Math.round(Height*Factor);
	}
	
	public void Resize(int Width, int Height)
	{
		this.Width = Width;
		this.Height = Height;
	}
	


}
