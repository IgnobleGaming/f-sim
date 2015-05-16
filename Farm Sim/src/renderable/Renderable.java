package renderable;

import specifier.*;
import interfaces.Variables;
import interfaces.file.Logging;
import interfaces.file.types.MaterialFile;

import java.util.Comparator;
import java.util.ArrayList;

public abstract class Renderable
{
	protected int XPos;
	protected int YPos;
	private int ZIndex;
	private static int ScreenWidth; // less calls
	private static int ScreenHeight; // ditto
	public boolean Visible;
	protected ArrayList<MaterialFile> Sprites;
	
	public enum Position
	{
		TOPLEFT, TOPCENTER, TOPRIGHT, CENTERLEFT, CENTERCENTER, CENTERRIGHT, BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT
	}
	
	public enum PositionType 
	{
		RELATIVE, ABSOLUTE
	}
	
	public void Draw() {}
	
	protected Renderable()
	{
		ScreenWidth = (int)Variables.GetInstance().Get("vid_width").Current();
		ScreenWidth = (int)Variables.GetInstance().Get("vid_height").Current();
		Sprites = new ArrayList<MaterialFile>();
		Visible = true;
		ZIndex = 0; // default zindex
		Logging.getInstance().Write(Logging.Type.INFO, "New renderable object created! [ z=%d, visible=%b ]", ZIndex, Visible);
	}
	
	public void Position (int x, int y)
	{
		XPos = x;
		YPos = y;
	}
	
	public void Position(Vector2D V)
	{
		XPos = V.x;
		YPos= V.y;
	}
	
	public specifier.Vector2D Position()
	{
		return new specifier.Vector2D(XPos, YPos);
	}
	
	public void Scale(double Factor)
	{
		if (Sprites.size() < 0)
			Logging.getInstance().Write(Logging.Type.WARNING, "Attempting to scale object with no materials!");
		for (MaterialFile M : Sprites)
			M.Scale(Factor);
	}
	
	public void Resize(int Width, int Height)
	{
		if (Sprites.size() < 0)
			Logging.getInstance().Write(Logging.Type.WARNING, "Attempting to resize object with no materials!");
		for (MaterialFile M : Sprites)
			M.Resize(Width, Height);
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
	
	public void AddSprites(MaterialFile... S)
	{
		for (MaterialFile M : S)
			Sprites.add(M);
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
	
	public static Vector2D GetPosFromLocation(Position P, PositionType Pt, int width, int height, int padx, int pady, Renderable parent)
	{
		switch (P)
		{
			case TOPCENTER:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D((ScreenWidth/2)+ padx, 0 + pady);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentTopBorder = ScreenHeight - parent.YPos / 2;
					int ParentLeftBorder = ScreenWidth
					return new Vector2D()
				}
			case CENTERCENTER:
				return new Vector2D(width/2, height/2);
			default:
				return new Vector2D(0,0);
		}
	}
}
