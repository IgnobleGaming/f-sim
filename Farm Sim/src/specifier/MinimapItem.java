package specifier;

public class MinimapItem
{
	public int ID;
	public org.newdawn.slick.Color Color;
	public specifier.Vector2D Position;
	
	public MinimapItem(int I, org.newdawn.slick.Color C, specifier.Vector2D P)
	{
		ID = I;
		Color = C;
		Position = P;
	}
}
