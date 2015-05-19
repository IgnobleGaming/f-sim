package utilities;

public class Text
{
	public static boolean isAlphaNumeric(char c)
	{
		if((c > 64 && c < 90) || (c > 96 && c < 122) || ( c > 47 && c < 58) || c == 95 || c == 32)
			return true;
		return false;
	}
	
	public static class BitmapGlyph
	{
		 public int id;
		 public int x, y, width, height;
		 public int xoffset, yoffset, xadvance;
		 public float u, v, u2, v2;
	}
	

}
