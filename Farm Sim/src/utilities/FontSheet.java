package utilities;

/**
 * Font sheet class used to render text from a material file
 * @author Michael
 *
 */
public class FontSheet
{
	public float Scale;
	public int BitMapWidth;
	public int BitMapHeight;
	public int LineSpacing;
	public int NumChar;
	public int CharWidth[];
	public int CharHeight[];
	public int CharX[];
	public int CharY[];
	public int CharS[];
	public int CharT[];
	public int A;
	public interfaces.file.types.MaterialFile BitMap;
	
	public float s0 (int index)
	{
		float recip_width = 1.0f / BitMapWidth;
		return CharS[index] * recip_width;
	}
	
	public float t0 (int index)
	{
		float recip_height = 1.0f / 512;
		return CharT[index] * recip_height;
	}
	
	public float s1 (int index)
	{
		float recip_width = 1.0f / BitMapWidth;
		return (CharS[index] + CharWidth[index]) * recip_width;
	}
	
	public float t1 (int index)
	{
		float recip_height = 1.0f / 512;
		return (CharT[index] + CharHeight[index]) * recip_height;
	}
	
	public int x0 (int index)
	{
		return CharX[index];
	}
	
	public int y0 (int index)
	{
		return CharY[index];
	}
	
	public int x1 (int index)
	{
		return CharX[index] + CharWidth[index];
	}
	
	public int y1 (int index)
	{
		return CharY[index] + CharHeight[index];
	}
	
	public int Advance(int index)
	{
		return (A + 8) >> 4;
	}
	
	// float version
	
	public float s0f(int index)
	{
		float recip_width = 1.0f / BitMapWidth;
		return (CharS[index] - 0.5f) * recip_width;
	}
	
	public float t0f(int index)
	{
		float recip_height = 1.0f / BitMapHeight;
		return (CharT[index] - 0.5f) * recip_height;
	}
	
	public float s1f (int index)
	{
		float recip_width = 1.0f / BitMapWidth;
		return (CharS[index] + CharWidth[index] + 0.5f) * recip_width;
	}
	
	public float t1f(int index)
	{
		float recip_height = 1.0f / BitMapHeight;
		return (CharT[index] + CharHeight[index] + 0.5f) * recip_height;
	}
	
	public float x0f (int index)
	{
		return CharX[index] - 0.5f;
	}
	
	public float y0f (int index)
	{
		return CharY[index] - 0.5f;
	}
	
	public float x1f (int index)
	{
		return CharX[index] + CharWidth[index] + 0.5f;
	}
	
	public float y1f (int index)
	{
		return CharY[index] + CharHeight[index] + 0.5f;
	}
	
	public float Advancef (int index)
	{
		return A/16.0f;
	}
}