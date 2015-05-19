package utilities;

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
	
	public int advance(int index)
	{
		return (440 + 8) >> 4;
	}
}