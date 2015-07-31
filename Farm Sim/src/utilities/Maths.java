package utilities;

public class Maths
{
	
	public static int Clamp(int min, int cur)
	{
		if (cur < min)
			return min;
		return cur;
	}
}
