package renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;

public class GUIFont extends Renderable
{
	private TrueTypeFont Font;
	private Font awtFont;
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
		this.Text = Text;
		this.FontColor = FontColor;
		awtFont = new Font(FontFamily, 0, FontSize.val);
		Font = new TrueTypeFont(awtFont, true);
		XPos = x;
		YPos = y;
	}
	
	public void MoveUp()
	{
		YPos -= 15;
	}
	
	public void Draw()
	{
		Font.drawString(XPos, YPos, Text, FontColor);
	}
}
