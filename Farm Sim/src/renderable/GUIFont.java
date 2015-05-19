package renderable;

import interfaces.Render;
import org.newdawn.slick.Color;
import interfaces.file.types.MaterialFile;

public class GUIFont extends Renderable
{
	private String Text;
	private utilities.FontSheet Sheet;
	private Float Size;

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
		super(0, 0);

		this.Text = Text;

		Sheet = new utilities.FontSheet();
		Size = LoadFontInfo(FontSize);
	}

	public void MoveUp()
	{
		YPos -= 15;
	}

	public void Draw()
	{
		Render.DrawString(this.Text, this.Position().x, this.Position().y, Size, Sheet);
	}

	public void Text(String NewText)
	{
		Text = NewText;
	}

	public String Text()
	{
		return Text;
	}

	private float LoadFontInfo(Size S)
	{
		Sheet.NumChar = 95;
		Sheet.BitMapHeight = 288;
		Sheet.BitMapWidth = 256;
		Sheet.CharWidth = new int[] { 0, 7, 16, 26, 23, 28, 27, 7, 15, 15, 20, 24, 13, 16, 9, 22, 24, 22, 22, 20, 26, 20, 22, 23, 23, 23, 8, 12, 20, 22, 20, 16, 28, 28, 22, 23, 24, 20, 18, 24, 23, 20, 18, 23, 20, 26, 23, 26, 22, 27, 22, 23,
				24, 23, 28, 26, 27, 28, 23, 13, 22, 13, 23, 28, 17, 21, 22, 20, 22, 23, 26, 24, 21, 20, 19, 22, 20, 24, 21, 24, 22, 22, 22, 20, 23, 21, 25, 27, 26, 25, 20, 19, 5, 19, 25 };
		Sheet.CharHeight = new int[] { 0, 36, 12, 32, 43, 36, 35, 12, 48, 48, 22, 25, 17, 5, 9, 41, 34, 33, 33, 34, 32, 33, 33, 32, 34, 33, 26, 34, 29, 13, 29, 36, 46, 32, 32, 34, 32, 32, 32, 34, 32, 32, 33, 32, 32, 32, 32, 34, 32, 42, 32,
				34, 32, 33, 32, 32, 32, 32, 32, 47, 41, 47, 16, 5, 11, 26, 36, 26, 36, 26, 35, 36, 35, 35, 46, 35, 35, 25, 25, 26, 36, 36, 25, 26, 34, 26, 25, 25, 25, 36, 25, 47, 51, 47, 11 };
		Sheet.CharX = new int[] { 0, 10, 6, 1, 2, 0, 1, 10, 7, 6, 4, 2, 5, 6, 9, 2, 2, 3, 3, 4, 1, 4, 3, 2, 2, 2, 10, 6, 3, 3, 5, 7, 0, 0, 3, 2, 2, 4, 5, 1, 2, 4, 4, 3, 5, 1, 2, 1, 3, 1, 4, 2, 2, 2, 0, 1, 0, 0, 2, 8, 3, 7, 2, 0, 0, 3, 3, 3,
				2, 2, 0, 2, 3, 4, 3, 4, 4, 2, 3, 2, 3, 2, 4, 4, 1, 3, 1, 0, 1, 1, 4, 3, 11, 5, 1, };
		Sheet.CharY = new int[] { 37, 2, 2, 5, 0, 2, 3, 2, 0, 0, 2, 11, 29, 21, 29, 2, 4, 4, 4, 4, 5, 5, 5, 5, 4, 4, 12, 12, 9, 17, 9, 2, 2, 5, 5, 4, 5, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 4, 5, 4, 5, 4, 5, 5, 5, 5, 5, 5, 5, 1, 2, 1, 5, 43, 2, 12,
				2, 12, 2, 12, 2, 12, 2, 2, 2, 2, 2, 12, 12, 12, 12, 12, 12, 12, 4, 12, 12, 12, 12, 12, 12, 1, -3, 1, 18, };
		Sheet.CharS = new int[] { 254, 119, 210, 71, 156, 64, 72, 248, 23, 7, 128, 1, 149, 215, 205, 208, 175, 47, 70, 1, 197, 117, 138, 107, 224, 93, 245, 241, 22, 187, 1, 1, 107, 121, 98, 151, 46, 25, 234, 22, 210, 189, 185, 150, 224, 80,
				56, 124, 174, 180, 229, 200, 204, 161, 131, 29, 1, 160, 1, 79, 231, 93, 163, 215, 187, 43, 41, 89, 150, 65, 45, 173, 219, 1, 136, 22, 198, 223, 201, 110, 127, 18, 178, 135, 100, 156, 26, 52, 101, 93, 80, 59, 1, 39, 227, };
		Sheet.CharT = new int[] { 1, 53, 257, 194, 1, 53, 90, 90, 1, 1, 257, 257, 257, 277, 271, 1, 90, 126, 126, 126, 194, 126, 126, 161, 90, 126, 194, 53, 227, 257, 227, 53, 1, 194, 194, 90, 194, 194, 161, 126, 161, 161, 126, 194, 194,
				161, 161, 90, 194, 1, 126, 90, 126, 126, 161, 161, 161, 161, 194, 1, 1, 1, 257, 271, 271, 227, 53, 227, 53, 227, 90, 53, 53, 90, 1, 90, 53, 227, 227, 227, 53, 53, 227, 227, 90, 227, 257, 257, 257, 53, 257, 1, 1, 1, 257 };
		MaterialFile Map = new MaterialFile("resources\\hud\\fonts\\consolas_huge.png", MaterialFile.Type.PNG);
		Map.Open();
		Sheet.BitMap = Map;
		switch (S)
		{
			case HUGE:
				return 1f;
			case LARGE:
				return .75f;
			case MEDIUM:
				return .6f;
			case SMALL:
				return .3f;
			case TINY:
				return .1f;
			default:
				return 1f;
		}
	}
}
