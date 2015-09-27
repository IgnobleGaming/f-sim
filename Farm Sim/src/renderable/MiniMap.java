package renderable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import specifier.MinimapItem;
import specifier.Vector2D;
import game.Map;
import interfaces.Camera;
import interfaces.Objects;
import interfaces.Render;
import interfaces.Variables;
import interfaces.file.types.MaterialFile;
import interfaces.file.types.MaterialFile.Type;

public class MiniMap extends Renderable
{
	private static MiniMap Instance;

	public boolean Visible;

	public MiniMap(int width, int height)
	{
		super(width, height);

		DateFormat formatter = new SimpleDateFormat("HH_mm_ss");
		String dateFormatted = formatter.format(start.Main.GameObject.GameTime());

		writeMMImage("MiniMap-" + dateFormatted, Map.GetInstance());

		this.ZIndex(100);
	}

	public static MiniMap GetInstance()
	{
		if (Instance == null)
			Instance = new MiniMap(Map.GetInstance().HorizontalTileNum(), Map.GetInstance().VerticalTileNum());
		return Instance;
	}

	public void Draw()
	{
		
		Render.DrawImage(CurrentSprite, new Vector2D(Camera.getInstance().cameraLookPoint().x - (int) Variables.GetInstance().Get("m_width").Current() / 40, Camera.getInstance().cameraLookPoint().y
				- (int) Variables.GetInstance().Get("m_width").Current() / 40));
	}

	private void writeMMImage(String Name, Map M)
	{
		String path = "resources\\hud\\MiniMaps\\" + Name + ".png";

		MinimapItem[][] MM = M.GetMinimap();

		BufferedImage image = new BufferedImage(M.HorizontalTileNum(), M.VerticalTileNum(), BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < M.VerticalTileNum(); x++)
		{
			for (int y = 0; y < M.HorizontalTileNum(); y++)
			{
				int rgb = ((MM[x][y].Color.getRed() & 0x0ff) << 16) | ((MM[x][y].Color.getGreen() & 0x0ff) << 8) | (MM[x][y].Color.getBlue() & 0x0ff);

				image.setRGB(x, y, rgb);
			}
		}

		File ImageFile = new File(path);

		try
		{
			// Write to Image location
			ImageIO.write(image, "png", ImageFile);

			// Cast to MaterialFile and set as Sprite
			MaterialFile Mini = new MaterialFile(path, Type.PNG);
			Mini.Open();
			this.SetSprite(Mini);
			// Add to Game Objects
			Objects.GetInstance().Add(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
