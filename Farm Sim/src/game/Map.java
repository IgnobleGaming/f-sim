package game;

import game.Tile.Flag;
import interfaces.Variables;
import interfaces.file.FileManager;
import specifier.Vector2D;
import java.util.Random;

public class Map extends renderable.Renderable
{
	private int HorizontalTileNum;
	private int VerticalTileNum;
	protected int TileSize = 0;
	protected Tile[] MapTiles;
	private static Map Instance;

	private Map()
	{
		super(0,0);
		HorizontalTileNum = (int) Variables.GetInstance().Get("m_width").Current();
		VerticalTileNum = (int) Variables.GetInstance().Get("m_height").Current();
		TileSize = (int) Variables.GetInstance().Get("m_tilesize").Current();
		MapTiles = new Tile[HorizontalTileNum * VerticalTileNum];
	}
			
	public static Map GetInstance()
	{
		if (Instance == null)
			Instance = new Map();
		return Instance;	
	}

	/*
	 * Loads the map ( depreciated -- see MapBuilder.java )
	 */
	public void Load()
	{
		for (int num = 0; num < MapTiles.length; num++)
		{
			MapTiles[num] = new Tile();
			MapTiles[num].SetSprite(GetRandomTile()); // this will be randomized
			if (TileSize == 0 )
				TileSize = MapTiles[0].Width();
			//Vector2D TilePos = GetTilePos(num);
			//MapTiles[num].Position(TilePos.x, TilePos.y);
		}
		this.Resize(HorizontalTileNum * TileSize, VerticalTileNum * TileSize);
	}

	/**
	 * Get the exact position of a tile based on index
	 * @param num ( index of tile from map tile array )
	 * @return 2D position of tile location
	 */
	public Vector2D GetCoordPos(int num)
	{
		int x = (int) (num % HorizontalTileNum) * TileSize + TileSize / 2; // offset for
		int y = (int) (Math.floor(num / VerticalTileNum) * TileSize + TileSize/ 2); // offset
		return new Vector2D(x, y);
	}
	
	/**
	 * Get the horizontal tile index of a give position
	 * @param V ( Position of requested tile )
	 * @return  index of closest tile
	 */
	public int GetTileIndex(Vector2D V)
	{
		return (HorizontalTileNum * V.y) + V.x;
	}
	
	/**
	 * Get the horizontal tile index of a give position
	 * @param x ( horizontal coordinate of location )
	 * @param y ( vertical coordinate of location )
	 * @return  index of closest tile
	 */
	public int GetTileIndex(int x, int y)
	{
		return (HorizontalTileNum * y) + x;
	}
	
	/**
	 * Get the index of a tile based on a 2D position
	 * ( used by GetCoordIndex )
	 * @param x ( horizontal coordinate of location )
	 * @param y ( vertical coordinate of location )
	 * @return  index of closest tile from the map array of tiles
	 */
	public int GetCoordIndex(int x, int y)
	{
		int TileX = x - (x % TileSize);
		int TileY = y - (y % TileSize);
		return GetTileIndex(TileX/TileSize, TileY/TileSize);
	}
	
	/**
	 * Get a tile based off of a 2D position
	 * @param x ( horizontal coordinate of location )
	 * @param y ( vertical coordinate of location )
	 * @return  closest tile from the map array of tiles
	 */
	public Tile GetTileFromIndex(int x, int y)
	{
		int index = GetCoordIndex(x, y);
		if (index > -1 && index < MapTiles.length)
			return MapTiles[index];
		else 
			return new Tile();
	}

	/**
	 * Draws all visible tiles
	 */
	public void Draw()
	{
		for (Tile T : MapTiles)
			if (T != null && T.CheckFlag(Flag.DRAWABLE))
				T.Draw();
	}
	
	/**
	 * Retrieves a random tile texture from the tiles folder ( depreciated, see Mapbuilder.java )
	 * @return Material File *tile*
	 */
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
