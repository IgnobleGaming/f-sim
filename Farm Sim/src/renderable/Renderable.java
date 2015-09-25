package renderable;

import specifier.*;
import interfaces.Camera;
import interfaces.Game;
import interfaces.Render;
import interfaces.file.types.MaterialFile;

public abstract class Renderable
{
	protected int XPos;
	protected int YPos;
	private int ZIndex;
	protected int Width;
	protected int Height;
	public boolean Visible;
	protected MaterialFile CurrentSprite;
	protected RenderType R;
	
	public boolean showing;
	
	
	public enum Position
	{
		TOPLEFT, TOPCENTER, TOPRIGHT, CENTERLEFT, CENTERCENTER, CENTERRIGHT, BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT
	}

	public enum PositionType
	{
		RELATIVE, ABSOLUTE
	}
	
	public enum RenderType {
		MENU, GAME
	}

	public void Draw()
	{
	}
	
	public void Update()
	{
	}

	protected Renderable(int width, int height)
	{
		Visible = true;
		ZIndex = 0; // default zindex
		Width = width;
		Height = height;
		//Logging.getInstance().Write(Logging.Type.INFO, "New renderable object created! [ z=%d, visible=%b ]", ZIndex, Visible);
	}
	
	public RenderType RenType()
	{
		return R;
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

	public specifier.Vector2D translatedRelativePos()
	{
		//return this.Position();
		return new specifier.Vector2D((Camera.getInstance().cameraLookPoint().x - Render.GetInstance().Width() / 2) + this.Position().x, (Camera.getInstance().cameraLookPoint().y - Render.GetInstance().Height() / 2 ) + this.Position().y);
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
		}
	}

	public void Resize(int Width, int Height)
	{
		if (Width > 0 && Height > 0)
		{
			this.Width = Width;
			this.Height = Height;
		}
	}

	public void Move(Direction.Relative Dir)
	{
		
	}

	public void SetSprite(MaterialFile S) // for object with no animation
	{
		//S.Resize(this.Width, this.Height); // so the texture scales to the size of the object
		CurrentSprite = S;
	}
	
	public MaterialFile getSprite()
	{
		return CurrentSprite;
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
			case TOPLEFT:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D(0 + padx, 0 + pady);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentTopY = parent.YPos - (parent.Height / 2);
					int ParentTopX = parent.XPos - (parent.Width / 2);
					return new Vector2D(ParentTopX + padx, ParentTopY + pady);
				}
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
					return new Vector2D(Render.GetInstance().Width()/2 + padx, Render.GetInstance().Height()/2 + pady);
				if (Pt == PositionType.RELATIVE)
					return new Vector2D(parent.XPos + padx - (width / 2), parent.YPos + pady - ( height / 2 ));
			case TOPRIGHT:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D(Render.GetInstance().Width() + padx, 0 + pady);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentBottomX = parent.XPos + (parent.Width / 2);
					int ParentBottomY = parent.YPos + (parent.Height / 2);
					
					return new Vector2D(ParentBottomX - (width / 2) + padx, ParentBottomY - (height / 2) + pady);
				}
			case BOTTOMLEFT:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D(0 + padx - width /2 , Render.GetInstance().Height() + pady - width / 2);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentBottomX = parent.XPos - (parent.Width / 2);
					int ParentBottomY = parent.YPos + (parent.Height / 2);
					
					return new Vector2D(ParentBottomX + (width / 2) + padx, ParentBottomY - (height / 2) + pady);
				}
			case BOTTOMRIGHT:
				if (Pt == PositionType.ABSOLUTE)
					return new Vector2D(Render.GetInstance().Width() + padx, Render.GetInstance().Height() + pady);
				if (Pt == PositionType.RELATIVE)
				{
					int ParentBottomX = parent.XPos + (parent.Width / 2);
					int ParentBottomY = parent.YPos + (parent.Height / 2);
					
					return new Vector2D(ParentBottomX - (width / 2) + padx, ParentBottomY - (height / 2) + pady);
				}
			default:
				return new Vector2D(0,0);
		}
	}
	

}
