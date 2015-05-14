package renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;

public class GUIFont extends Renderable
{
	private TrueTypeFont Font;
	private Font awtFont;
	private int XPos = 100;
	private int YPos = 100;
	private String Text;
	
	public enum Size 
	{ 
		TINY(4), SMALL(9), MEDIUM(12), LARGE(18), HUGE(24);
		private int val;
		
		private Size(int val)
		{
			this.val = val;
		}
	};
	
	public GUIFont(String FontFamily, String Text, Size FontSize)
	{
		this.Text = Text;
		awtFont = new Font(FontFamily, 0, FontSize.val);
		Font = new TrueTypeFont(awtFont, true);
	}
	
	public void Draw()
	{
		Color.white.bind();
		Font.drawString(XPos, YPos, Text);
	}

}
