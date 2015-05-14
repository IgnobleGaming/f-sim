package renderable;

import specifier.Direction;

public abstract class Renderable
{
	private int Speed = 1;
	private boolean Running;
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
		if(Running)
			Speed = 2;
		else
			Speed = 1;
		
		
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
	
	public void Running(boolean IsRunning) {
		Running = IsRunning;
	}
}
