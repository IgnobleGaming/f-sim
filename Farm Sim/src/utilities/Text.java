package utilities;

public class Text
{
	public static boolean isAlphaNumeric(char c)
	{
		if((c < 64 && c > 90) || (c > 96 && c < 122) || ( c > 47 && c < 58) || c == 95)
			return true;
		return false;
	}
}
