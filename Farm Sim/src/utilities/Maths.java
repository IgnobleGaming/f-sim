package utilities;

public class Maths
{

	public static int Clamp(int min, int max, int cur)
	{
		if (cur < min)
			return min;
		if (cur > max)
			return max;
		return cur;
	}

	public static int borderClampLeft(int Width, int TileSize)
	{
		int rem = Width % TileSize;

		if (rem > 15)
			return -(Width / 2 / TileSize) + 1;

		else
			return -(Width / 2 / TileSize);
	}

	public static int borderClampRight(int Width, int currentPos, int TileSize)
	{
		return Width / 2  + currentPos;
	}
	
	public static int borderClampTop(int Height, int TileSize)
	{
		int rem = Height % TileSize;

		if (rem > 15)
			return -(Height / 2 / TileSize) - 8;

		else
			return -(Height / 2 / TileSize);
	}

	
}
