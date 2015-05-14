package renderable;

import specifier.Direction;
import java.util.Comparator;
public abstract class Renderable
{
	protected int XPos;
	protected int YPos;
	private int ZIndex;
	public boolean Visible;
	
	public void Draw() {}
	
	public void Position (int x, int y)
	{
		XPos = x;
		YPos = y;
		ZIndex = 0;
		Visible = true;
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
