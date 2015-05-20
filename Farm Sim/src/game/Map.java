package game;

import game.Tile.Flag;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import specifier.Vector2D;
import java.util.Random;

public class Map extends renderable.Renderable
{
	private int HorizontalTileNum;
	private int VerticalTileNum;
	protected int TileSize = 0;
	protected Tile[] MapTiles;

	public Map()
	{
		super(0,0);
		HorizontalTileNum = (int) Variables.GetInstance().Get("m_maxwidth").Current();
		VerticalTileNum = (int) Variables.GetInstance().Get("m_maxheight").Current();
		MapTiles = new Tile[HorizontalTileNum * VerticalTileNum];
	}
	
	public Map(int d)
	{
		super(0,0);
		HorizontalTileNum = d;
		VerticalTileNum = d;
		MapTiles = new Tile[d*d];
		TileSize = 32;
	}

	public void Load()
	{
		for (int num = 0; num < MapTiles.length; num++)
		{
			MapTiles[num] = new Tile();
			MapTiles[num].AddSprites(GetRandomTile()); // this will be randomized
			if (TileSize == 0 )
				TileSize = MapTiles[0].Width();
			//Vector2D TilePos = GetTilePos(num);
			//MapTiles[num].Position(TilePos.x, TilePos.y);
		}
		this.Resize(HorizontalTileNum * TileSize, VerticalTileNum * TileSize);
	}

	public Vector2D GetCoordPos(int num)
	{
		int x = (int) (num % HorizontalTileNum) * TileSize + TileSize / 2; // offset for
		int y = (int) (Math.floor(num / VerticalTileNum) * TileSize + TileSize/ 2); // offset
		return new Vector2D(x, y);
	}
	
	public int GetTileIndex(Vector2D V)
	{
		return (HorizontalTileNum * V.y) + V.x;
	}
	
	public int GetTileIndex(int x, int y)
	{
		return (HorizontalTileNum * y) + x;
	}

	public void Draw()
	{
		for (Tile T : MapTiles)
			if (T != null && T.CheckFlag(Flag.DRAWABLE))
				T.Draw();
	}
	
	public interfaces.file.types.MaterialFile GetRandomTile()
	{
		Random R = new Random();
		interfaces.file.types.MaterialFile M;
		M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_flower.png");
		switch (R.nextInt(10))
		{
			case 0:
			case 1:
			case 2:
			case 3:
				M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_flower.png");
				break;
			case 4:
				M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_dirt.png");
				break;
			case 7:
			case 8:
				M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_stone.png");
				break;
			case 9:
				M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_water.png");
				break;
		}

		return M;
	} 
}
