package renderable;

import specifier.*;
import interfaces.Render;
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
	private int Width;
	private int Height;
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

	public void Draw()
	{
	}

	protected Renderable(int width, int height)
	{
		Sprites = new ArrayList<MaterialFile>();
		Visible = true;
		ZIndex = 0; // default zindex
		Width = width;
		Height = height;
		//Logging.getInstance().Write(Logging.Type.INFO, "New renderable object created! [ z=%d, visible=%b ]", ZIndex, Visible);
	}

	public void Position(int x, int y)
	{
		XPos = x;
		YPos = y;
	}

	public void Position(Vector2D V)
	{
		XPos = V.x;
		YPos = V.y;
	}

	public specifier.Vector2D Position()
	{
		return new specifier.Vector2D(XPos, YPos);
	}

	public void Scale(double Factor)
	{
		if (Factor > 0)
		{
			Width = (int) (Width * Factor);
			Height = (int) (Height * Factor);
			if (Sprites.size() < 0)
				Logging.getInstance().Write(Logging.Type.WARNING, "Attempting to scale object with no materials!");
			for (MaterialFile M : Sprites)
				M.Scale(Factor);
		}
	}

	public void Resize(int Width, int Height)
	{
		if (Width > 0 && Height > 0)
		{
			this.Width = Width;
			this.Height = Height;
			if (Sprites.size() < 0)
				Logging.getInstance().Write(Logging.Type.WARNING, "Attempting to resize object with no materials!");
			for (MaterialFile M : Sprites)
				M.Resize(Width, Height);
		}
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
		{
			M.Resize(this.Width, this.Height); // so the texture scales to the size of the object
			Sprites.add(M);
		}
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
	
	public int Width()
	{
		return Width;
	}

	public int Height()
	{
		return Height;
	}
	public static Vector2D GetPosFromLocation(Position P, PositionType Pt, int width, int height, int padx, int pady, Renderable parent)
	{
		switch (P)
		{
			case TOPCENTER:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D((Render.GetInstance().Width()/2) + padx, width/2 + pady);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentTopY = Render.GetInstance().Height() - (Render.GetInstance().Height() - (parent.YPos - (parent.Height / 2)));
					int ParentTopX = Render.GetInstance().Width() - (Render.GetInstance().Width() - (parent.XPos - (parent.Width / 2)));
					return new Vector2D(ParentTopX + padx, ParentTopY + pady);
				}
			case CENTERCENTER:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D(Render.GetInstance().Width()/2, Render.GetInstance().Height()/2);
				if (Pt == PositionType.RELATIVE)
					return new Vector2D(parent.XPos + padx, parent.YPos + pady);
			case BOTTOMLEFT:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D(0 + padx, Render.GetInstance().Height() + pady);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentBottomX = parent.XPos - (parent.Width/2);
					int ParentBottomY = parent.YPos + (parent.Height / 2);
					
					return new Vector2D(ParentBottomX + (width / 2) + padx, ParentBottomY + (height /2) + pady);
				}
			default:
				return new Vector2D(0,0);
		}
	}
}
