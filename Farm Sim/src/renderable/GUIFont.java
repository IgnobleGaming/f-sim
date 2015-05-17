package renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;

public class GUIFont extends Renderable
{
	private TrueTypeFont aFont;
	private String Text;
	private Color FontColor;
	
	public enum Size 
	{ 
		TINY(4), SMALL(12), MEDIUM(24), LARGE(36), HUGE(48);
		private int val;
		
		private Size(int val)
		{
			this.val = val;
		}
	};
	
	public GUIFont(String FontFamily, String Text, Size FontSize, Color FontColor, int x, int y)
	{
		super(Text.length() * 1, 20);
		this.Text = Text;
		this.FontColor = FontColor;
		aFont = new TrueTypeFont(new Font(FontFamily, 0, FontSize.val), false);
		
		XPos = x;
		YPos = y;
	}
	
	public void MoveUp()
	{
		YPos -= 15;
	}
	
	public void Draw()
	{
		aFont.drawString(XPos, YPos, Text, FontColor);
	}
	
	public void Text(String NewText)
	{
		Text = NewText;
	}
	
	public String Text()
	{
		return Text;
	}
}
