package game;

import game.Tile.Flag;
import interfaces.Camera;
import interfaces.Game;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;
import specifier.Vector2D;

import java.util.Random;

import object.Entity;

import org.newdawn.slick.Color;

public class Map extends renderable.Renderable
{
	private int HorizontalTileNum;
	private int VerticalTileNum;
	protected int TileSize = 0;
	protected Tile[] MapTiles;
	private static Map Instance;
	private int Dimension;

	public enum Direction
	{
		NORTH, SOUTH, EAST, WEST, CURRENT, UNKNOWN
	}

	private Map()
	{
		super(0, 0);
		this.ZIndex(0);
		Dimension = (int) Variables.GetInstance().Get("m_width").Current();
		HorizontalTileNum = (int) Variables.GetInstance().Get("m_width").Current();
		VerticalTileNum = (int) Variables.GetInstance().Get("m_height").Current();
		TileSize = (int) Variables.GetInstance().Get("m_tilesize").Current();
		MapTiles = new Tile[HorizontalTileNum * VerticalTileNum];
	}

	public static Map GetInstance()
	{
		if (Instance == null)
		{
			Instance = new Map();
			Instance.init();

			return Instance;
		} else
			return Instance;
	}

	/*
	 * Loads the map ( depreciated -- see MapBuilder.java )
	 */
	public void Load()
	{
		/*
		 * for (int num = 0; num < MapTiles.length; num++) { MapTiles[num] = new Tile(); MapTiles[num].SetSprite(GetRandomTile()); // this will be randomized if (TileSize == 0 ) TileSize = MapTiles[0].Width(); //Vector2D TilePos = GetTilePos(num); //MapTiles[num].Position(TilePos.x, TilePos.y); }
		 * this.Resize(HorizontalTileNum * TileSize, VerticalTileNum * TileSize);
		 */
	}

	public int TileSize()
	{
		return TileSize;
	}

	public void init()
	{

		MaterialFile Grass1 = new MaterialFile("resources\\ingame\\tiles\\g1.png", MaterialFile.Type.PNG);
		Grass1.Open();
		FileManager.getInstance().Add(Grass1);

		MaterialFile Grass2 = new MaterialFile("resources\\ingame\\tiles\\g2.png", MaterialFile.Type.PNG);
		Grass2.Open();
		FileManager.getInstance().Add(Grass2);

		MaterialFile Grass3 = new MaterialFile("resources\\ingame\\tiles\\g3.png", MaterialFile.Type.PNG);
		Grass3.Open();
		FileManager.getInstance().Add(Grass3);

		MaterialFile Grass4 = new MaterialFile("resources\\ingame\\tiles\\g4.png", MaterialFile.Type.PNG);
		Grass4.Open();
		FileManager.getInstance().Add(Grass4);

		MaterialFile Grass5 = new MaterialFile("resources\\ingame\\tiles\\g5.png", MaterialFile.Type.PNG);
		Grass5.Open();
		FileManager.getInstance().Add(Grass5);

		MaterialFile Grass6 = new MaterialFile("resources\\ingame\\tiles\\g6.png", MaterialFile.Type.PNG);
		Grass6.Open();
		FileManager.getInstance().Add(Grass6);

		MaterialFile TileSprite2 = new MaterialFile("resources\\ingame\\tiles\\dirt.png", MaterialFile.Type.PNG);
		TileSprite2.Open();
		FileManager.getInstance().Add(TileSprite2);

		MaterialFile TileSprite3 = new MaterialFile("resources\\ingame\\tiles\\grass_stone.png", MaterialFile.Type.PNG);
		TileSprite3.Open();
		FileManager.getInstance().Add(TileSprite3);

		MaterialFile TileSprite4 = new MaterialFile("resources\\ingame\\tiles\\water.png", MaterialFile.Type.PNG);
		TileSprite4.Open();
		FileManager.getInstance().Add(TileSprite4);

		MaterialFile SandTile = new MaterialFile("resources\\ingame\\tiles\\sand.png", MaterialFile.Type.PNG);
		SandTile.Open();
		FileManager.getInstance().Add(SandTile);

		MaterialFile MountainTile = new MaterialFile("resources\\ingame\\tiles\\mountain.png", MaterialFile.Type.PNG);
		MountainTile.Open();
		FileManager.getInstance().Add(MountainTile);
		
		int SizeActual = Dimension * Dimension;

		for (int i = 0; i < SizeActual; i++)
		{
			MapTiles[i] = new Tile(Tile.Type.GRASS);
			if (TileSize == 0)
				TileSize = MapTiles[0].Width();
			Vector2D TilePos = GetCoordPos(i);
			MapTiles[i].Position(TilePos.x, TilePos.y);
			MapTiles[i].TileID = i;
		}
		Resize(Dimension * TileSize, Dimension * TileSize);
	}

	/**
	 * Get the exact position of a tile based on index
	 * 
	 * @param num
	 *            ( index of tile from map tile array )
	 * @return 2D position of tile location
	 */
	public Vector2D GetCoordPos(int num)
	{
		int x = (int) (num % HorizontalTileNum) * TileSize; // offset for
		int y = (int) (Math.floor(num / VerticalTileNum) * TileSize); // offset
		return new Vector2D(x, y);
	}

	public Vector2D GetMinimapCoord(int num)
	{
		int x = (int) (num % HorizontalTileNum) * 2; // offset for
		int y = (int) (Math.floor(num / VerticalTileNum)) * 2; // offset
		return new Vector2D(x, y);
	}

	/**
	 * Get the horizontal tile index of a give position
	 * 
	 * @param V
	 *            ( Position of requested tile )
	 * @return index of closest tile
	 */
	public int GetTileIndex(Vector2D V)
	{
		return (HorizontalTileNum * V.y) + V.x;
	}

	/**
	 * Get the horizontal tile index of a give position
	 * 
	 * @param x
	 *            ( horizontal coordinate of location )
	 * @param y
	 *            ( vertical coordinate of location )
	 * @return index of closest tile
	 */
	public int GetTileIndex(int x, int y)
	{
		return (HorizontalTileNum * y) + x;
	}

	/**
	 * Get the index of a tile based on a 2D position ( used by GetCoordIndex )
	 * 
	 * @param x
	 *            ( horizontal coordinate of location )
	 * @param y
	 *            ( vertical coordinate of location )
	 * @return index of closest tile from the map array of tiles
	 */
	public int GetCoordIndex(int x, int y)
	{
		int TileX = x - (x % TileSize);
		int TileY = y - (y % TileSize);
		return GetTileIndex(TileX / TileSize, TileY / TileSize);
	}

	/**
	 * Get a tile based off of a 2D position
	 * 
	 * @param x
	 *            ( horizontal coordinate of location )
	 * @param y
	 *            ( vertical coordinate of location )
	 * @return closest tile from the map array of tiles
	 */
	public Tile GetTileFromIndex(int x, int y)
	{
		int index = GetCoordIndex(x, y);
		if (index > -1 && index < MapTiles.length)
			return MapTiles[index];
		else
			return new Tile(Tile.Type.GRASS);
	}
	
	public Tile GetTileFromIndex(Vector2D V)
	{
		int index = GetTileIndex(V.x, V.y);
		if (index > -1 && index < MapTiles.length)
			return MapTiles[index];
		else
			return new Tile(Tile.Type.GRASS);
	}
	
	public Tile GetTileFromIndex(int i)
	{
		if (i > -1 && i < MapTiles.length)
			return MapTiles[i];
		else 
			return new Tile(Tile.Type.GRASS);
	}

	public Direction GetCardinalPositionOfTarget(Vector2D TV, Vector2D CV)
	{
		int x = TV.x - CV.x;
		int y = TV.y - CV.y;

		if (TV.x == CV.x && TV.y == CV.y)
			return Direction.CURRENT;

		if (Math.abs(x) > Math.abs(y))
		{
			if (x < 0)
				return Direction.WEST;
			else if (x > 0)
				return Direction.EAST;
		} else
		{
			if (y < 0)
				return Direction.NORTH;
			else
				return Direction.SOUTH;
		}
		return Direction.UNKNOWN;

	}

	/**
	 * Draws all visible tiles
	 */
	public void Draw()
	{
		for (Tile T : MapTiles)
		{
			if (T != null && T.CheckFlag(Flag.DRAWABLE) && Camera.getInstance().inViewPlane(T))
				T.Draw();
		}
		interfaces.Render.DrawMap(GetMinimap());
	}

	public Tile GetNextTile(int CurTile, specifier.Direction.Relative Dir)
	{
		int tileInt = 0;
		switch (Dir)
		{
			case RIGHT:
				tileInt = CurTile + 1;
				if (CurTile % HorizontalTileNum == HorizontalTileNum - 1)
					return MapTiles[CurTile];
				else
					return MapTiles[tileInt];
			case LEFT:
				tileInt = CurTile - 1;
				if (CurTile % HorizontalTileNum == 0)
					return MapTiles[CurTile];
				else
					return MapTiles[tileInt];
			case UP:
				tileInt = CurTile - HorizontalTileNum;
				if (tileInt > 0)
					return MapTiles[tileInt];
				else
					return MapTiles[CurTile];
			case DOWN:
				tileInt = CurTile + HorizontalTileNum;
				if (tileInt < MapTiles.length)
					return MapTiles[tileInt];
				else
					return MapTiles[CurTile];
			default:
				return MapTiles[CurTile];
		}
	}

	public int[] SurroundingTiles(Renderable R)
	{
		int[] Tiles;
		int Curr = GetCoordIndex(R.Position().x, R.Position().y);
		
		boolean Top = R.Position().y > 0 && R.Position().y < TileSize;
		boolean Bottom = R.Position().y < Dimension * TileSize && R.Position().y > (Dimension * TileSize) - TileSize;
		boolean Left = R.Position().x > 0 && R.Position().x < TileSize;
		boolean Right = R.Position().x < Dimension * TileSize && R.Position().x > (Dimension * TileSize) - TileSize;
		
		if (Top)
		{
			if (Left)
			{
				Tiles = new int [4];
				
				Tiles[0] = Curr + 1; // MR
				Tiles[1] = Curr + Dimension; // BM
				Tiles[2] = Curr + Dimension + 1; // BR
				Tiles[3] = Curr;
			}
			else if (Right)
			{
				Tiles = new int [4];
				
				Tiles[0] = Curr - 1; // ML
				Tiles[1] = Curr + Dimension - 1; // BL
				Tiles[2] = Curr + Dimension; // BM
				Tiles[3] = Curr;
			}
			else
			{
				Tiles = new int [6];
				
				Tiles[0] = Curr - 1; // ML
				Tiles[1] = Curr;
				Tiles[2] = Curr + 1; // MR
				Tiles[3] = Curr + Dimension - 1; // BL
				Tiles[4] = Curr + Dimension; // BM
				Tiles[5] = Curr + Dimension + 1; // BR
			}
		}
		else if (Bottom)
		{
			if (Left)
			{
				Tiles = new int [4];

				Tiles[0] = Curr - Dimension; // TM
				Tiles[1] = Curr - Dimension + 1; // TR
				Tiles[2] = Curr + 1; // MR
				Tiles[3] = Curr;
			}
			else if (Right)
			{
				Tiles = new int [4];

				Tiles[0] = Curr - 1; // ML
				Tiles[1] = Curr - Dimension - 1; // TL
				Tiles[2] = Curr - Dimension; // TM
				Tiles[3] = Curr;
			}
			else
			{
				Tiles = new int [6];
				
				Tiles[0] = Curr - Dimension - 1; // TL
				Tiles[1] = Curr - Dimension; // TM
				Tiles[2] = Curr - Dimension + 1; // TR
				Tiles[3] = Curr - 1; // ML
				Tiles[4] = Curr;
				Tiles[5] = Curr + 1; // MR
			}
		}
		else
		{
			Tiles = new int [9];
			
			Tiles[0] = Curr - Dimension - 1; // TL
			Tiles[1] = Curr - Dimension; // TM
			Tiles[2] = Curr - Dimension + 1; // TR
			Tiles[3] = Curr - 1; // ML
			Tiles[4] = Curr;
			Tiles[5] = Curr + 1; // MR
			Tiles[6] = Curr + Dimension - 1; // BL
			Tiles[7] = Curr + Dimension; // BM
			Tiles[8] = Curr + Dimension + 1; // BR
		}
		
		return Tiles;
	}

	public specifier.MinimapItem[] GetMinimap()
	{
		specifier.MinimapItem[] Minimap = new specifier.MinimapItem[game.Map.GetInstance().MapTiles.length];
		for (game.Tile T : game.Map.GetInstance().MapTiles)
		{
			switch (T.Type())
			{
				case GRASS:
					Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.green, GetMinimapCoord(T.TileID));
					break;
				case WATER:
					Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.blue, GetMinimapCoord(T.TileID));
					break;
				case DIRT:
					Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, new Color(115, 69, 3, 1), GetMinimapCoord(T.TileID));
					break;
				case SAND:
					Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.yellow, GetMinimapCoord(T.TileID));
					break;
				case MOUNTAIN:
					Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.darkGray, GetMinimapCoord(T.TileID));
					break;
				default:
					Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.black, GetMinimapCoord(T.TileID));
			}

			if (T.TileID == Game.GetInstance().Player().TileID())
				Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.red, GetMinimapCoord(T.TileID));
		}
		return Minimap;
	}

	/**
	 * Retrieves a random tile texture from the tiles folder ( depreciated, see Mapbuilder.java )
	 * 
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

	public Entity GetEntityFromIndex(int index)
	{
		return null;
	}
	
	public int HorizontalTileNum()
	{
		return HorizontalTileNum;
	}
	
	public int VerticalTileNum()
	{
		return VerticalTileNum;
	}
}
