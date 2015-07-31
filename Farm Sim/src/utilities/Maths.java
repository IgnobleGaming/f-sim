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
}
