package renderable;

import specifier.Direction;

public abstract class Renderable
{
	protected int XPos;
	protected int YPos;
	
	public void Draw() {}
	
	public void Position (int x, int y)
	{
		XPos = x;
		YPos = y;
	}
	
	public void Move(Direction.Relative Dir)
	{
		switch (Dir)
		{
			case UP:
				YPos -= 1;
				break;
			case DOWN:
				YPos += 1;
				break;
			case LEFT:
				XPos -= 1;
				break;
			case RIGHT:
				XPos += 1;
				break;
		}
	}
}
