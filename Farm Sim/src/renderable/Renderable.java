package renderable;

import specifier.Direction;
import interfaces.file.Logging;
import interfaces.file.types.MaterialFile;

import java.util.Comparator;
import java.util.ArrayList;

public abstract class Renderable
{
	protected int XPos;
	protected int YPos;
	private int ZIndex;
	public boolean Visible;
	protected ArrayList<MaterialFile> Sprites;
	
	public void Draw() {}
	
	protected Renderable()
	{
		Sprites = new ArrayList<MaterialFile>();
		Visible = true;
		ZIndex = 0;
		Logging.getInstance().Write(Logging.Type.INFO, "New renderable object created! [ z=%d, visible=%b ]", ZIndex, Visible);
	}
	
	public void Position (int x, int y)
	{
		XPos = x;
		YPos = y;
	}
	
	public specifier.Vector2D Position()
	{
		return new specifier.Vector2D(XPos, YPos);
	}
	
	public void Move(Direction.Relative Dir)
	{
		int Speed = 1;
		switch (Dir)
		{			
			case UP:
				YPos -= Speed * 2;
				break;
			case DOWN:
				YPos += Speed * 2;
				break;
			case LEFT:
				XPos -= Speed * 2;
				break;
			case RIGHT:
				XPos += Speed * 2;
				break;
		}
	}
	
	public void AddSprites(MaterialFile... Sprites)
	{
		for (MaterialFile M : Sprites)
			this.Sprites.add(M);
	}
	
	public int ZIndex()
	{
		return ZIndex;
	}
	
	public void ZIndex(int I)
	{
		if (I > -1)
			ZIndex = I;
	}
}
