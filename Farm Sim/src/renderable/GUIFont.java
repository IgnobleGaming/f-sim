package renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import interfaces.Render;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import java.util.ArrayList;

import java.awt.Font;

public class GUIFont extends Renderable
{
	private TrueTypeFont aFont;
	private String Text;
	private Color FontColor;
	private int CharHeight = 60;
	private int CharWidth = 34;
	private int NumWidth = 15;
	private int NumHeight = 7;
	final ArrayList<utilities.Text.BitmapGlyph> glyphs = new ArrayList<utilities.Text.BitmapGlyph>();
	
	public enum Size
	{
		TINY(4), SMALL(12), MEDIUM(24), LARGE(36), HUGE(48);
		private int val;

		private Size(int val)
		{
			this.val = val;
		}
	};

	public enum FontFamily
	{
		Consolas
	}

	public GUIFont(FontFamily FF, String Text, Size FontSize, Color FontColor, int x, int y)
	{
		super(0,0);
		
		switch (FF)
		{
			case Consolas:
				LoadFontTexture("resources\\hud\\fonts\\consolas_huge.png");
		}
		for (int i = 33; i < 128; i++)
		{
			utilities.Text.BitmapGlyph t = new utilities.Text.BitmapGlyph();
			t.width = 512;
			t.height = 512;
			t.x = 60;
			t.y = 34;
			glyphs.add(t);
		}
		for (utilities.Text.BitmapGlyph glyph : glyphs) {
		    glyph.u = glyph.x / (float) 512.0;
		    glyph.v = glyph.y / (float) 512.0;
		    glyph.u2 = (glyph.x + glyph.width) / (float) 512.0;
		    glyph.v2 = (glyph.y + glyph.height) / (float) 512.0;
		}
		this.Text = Text;
		this.FontColor = FontColor;
		XPos = x;
		YPos = y;
	}

	public void MoveUp()
	{
		YPos -= 15;
	}

	public void Draw()
	{
		/*for (int i = 0; i < this.Text.length(); i++)
		{
			specifier.Vector2D SpriteSheetPos = GetFontPos(this.Text.charAt(i)); // relative position on the font sprite
			
			float PixelsDown = (1f*SpriteSheetPos.y / 512);
			float PixelsLeft = (1f*SpriteSheetPos.x / 512);
			//Render.DrawPartialImage(Sprites.get(0), new specifier.Vector2D(Position().x + CharWidth*(i+1), Position().y), PixelsDown, PixelsLeft, CharWidth, CharHeight);

		}*/
		Render.DrawString(Sprites.get(0), this.Text, this.Position().x, this.Position().y, 2f, glyphs);
	}

	public void Text(String NewText)
	{
		Text = NewText;
	}

	public String Text()
	{
		return Text;
	}
	
	private void LoadFontTexture(String Loc)
	{
		interfaces.file.types.MaterialFile FMat = (interfaces.file.types.MaterialFile)FileManager.getInstance().Retrieve(Loc);
		AddSprites(FMat);
		this.Resize(FMat.Width(), FMat.Height());
	}
	
	private specifier.Vector2D GetFontPos(char in)
	{
		specifier.Vector2D Pos;
		int relativeChar = in - 33; // because we're not printing control characters
		
		int x = (relativeChar % NumWidth)*CharWidth;
		int y = CharHeight * (relativeChar / NumWidth); // get the character's y position from the 'sprite-sheet'
		Pos = new specifier.Vector2D(x,y);
		return Pos;
	}
}
