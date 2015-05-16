package game;

import game.Tile.Flag;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import specifier.Vector2D;

public class Map extends renderable.Renderable
{
	private int HorizontalTileNum;
	private int VerticalTileNum;
	private Tile[] MapTiles;

	public Map()
	{
		HorizontalTileNum = (int) Variables.GetInstance().Get("m_maxwidth").Current();
		VerticalTileNum = (int) Variables.GetInstance().Get("m_maxheight").Current();
		MapTiles = new Tile[HorizontalTileNum * VerticalTileNum];
	}

	public void Load()
	{
		for (int num = 0; num < MapTiles.length; num++)
		{
			MapTiles[num] = new Tile();
			MapTiles[num].AddSprites((interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_flower.png")); // this will be randomized
			Vector2D TilePos = GetTilePos(num);
			MapTiles[num].Position(TilePos.x, TilePos.y);
		}
	}

	public Vector2D GetTilePos(int num)
	{
		int x = (int) ((((num % HorizontalTileNum) * 32 + 8)) * 1.5); // offset for
		int y = (int) (((int) (Math.floor(num / VerticalTileNum) * 32)) * 1.5 + 16); // offset
		Logging.getInstance().Write(Logging.Type.INFO, "Tile centered @ %d, %d", x, y);
		return new Vector2D(x, y);
	}

	public void Draw()
	{
		for (Tile T : MapTiles)
			if (T != null && T.CheckFlag(Flag.DRAWABLE))
				T.Draw();
	}
}
