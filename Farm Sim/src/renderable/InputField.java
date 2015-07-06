package renderable;

import org.newdawn.slick.Color;

import interfaces.Render;
import interfaces.file.FileManager;
import interfaces.file.types.MaterialFile;


public class InputField extends Renderable
{

	public GUIFont InputText;

	public InputField(int width, int height)
	{
		super(width, height);
		MaterialFile bg = new MaterialFile("resources\\hud\\input_field.png", MaterialFile.Type.PNG);
		bg.Open();
		FileManager.getInstance().Add(bg);
		SetSprite(bg);
		ZIndex(10001);	
		InputText = new GUIFont(GUIFont.FontFamily.Consolas, ">", GUIFont.Size.SMALL, Color.white, 0,0);	
		InputText.ZIndex(1000200);
		R = RenderType.MENU;
	}
	
	
	public void Draw()
	{
		InputText.Draw();
		Render.DrawImage(CurrentSprite, translatedRelativePos());
	}
}
