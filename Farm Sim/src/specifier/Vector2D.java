package specifier;

public class Vector2D
{
	public int x, y;
	
	public Vector2D(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean Match(int x, int y)
	{
		if (x == this.x && y == this.y)
			return true;
		else 
			return false;
	}
}
