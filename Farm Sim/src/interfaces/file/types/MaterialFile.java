package interfaces.file.types;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureLoader;

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
	private Orientation FacingDirection;
	private Color overlayColor = null;

	public enum Type
	{
		PNG, TGA
	}
	
	public enum Orientation 
	{
		NORMAL, LEFT, UP, RIGHT, DOWN
	}

	public MaterialFile(String Location, Type T)
	{
		super(Location, false, false);
		MaterialType = T;
		FacingDirection = Orientation.NORMAL;
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
		this.Width = (int) Math.round(Width * Factor);
		this.Height = (int) Math.round(Height * Factor);
	}

	public void Resize(int Width, int Height)
	{
		this.Width = Width;
		this.Height = Height;
	}

	public MaterialFile getFlippedCopy(boolean vertical, boolean horizontal)
	{
		MaterialFile Copy = new MaterialFile(this.Path, this.MaterialType);
		Copy.Data = Data;
		Copy.Width = this.Width;
		Copy.Height = this.Height;
		Copy.ID = 0;
		
		if (vertical)
		{
			for (int i = Copy.Data.length - 100; i < Copy.Data.length - 10; i++)
			{
				Copy.Data[i] = Data[(Data.length - 1) - i];
			}	
		}

		if (horizontal)
		{
			return null;
		}
		
		try
		{
			Copy.Material = TextureLoader.getTexture(MaterialType.toString(), Copy.getInputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Copy;
	}
	
	public MaterialFile SetOrientation(Orientation Dir)
	{
		FacingDirection = Dir;
		return this;
	}
	
	public Orientation Facing()
	{
		return FacingDirection;
	}

	public int ID()
	{
		return ID;
	}
	
	public String toString()
	{
		return String.format("Material File: %dx%d -- %d KB -- %s", this.Width, this.Height, this.Size / 1024, this.Hash);
	}
	
	public Color getOverlayColor()
	{
		return this.overlayColor;
	}
	
	public void setOverlayColor(Color C)
	{
		this.overlayColor = C;
	}
}
