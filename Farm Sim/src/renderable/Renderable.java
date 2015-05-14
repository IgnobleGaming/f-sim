package renderable;

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
}
