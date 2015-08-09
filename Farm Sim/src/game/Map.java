package game;

import interfaces.Camera;
import interfaces.Game;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.types.MaterialFile;
import renderable.Renderable;
import specifier.Vector2D;
import utilities.Maths;

import java.util.Random;

import object.Entity;

import org.newdawn.slick.Color;

public class Map extends renderable.Renderable
{
	private int HorizontalTileNum;
	private int VerticalTileNum;
	protected int TileSize = 0;
	protected Tile[][] MapTiles;
	private static Map Instance;
	private int Dimension;
	public boolean MM;
	private Tile[][] activeTiles;
	private int activeTileWidth, activeTileHeight;
	private int paddingTiles;
	
	public enum Direction
	{
		NORTH, SOUTH, EAST, WEST, CURRENT, UNKNOWN
	}

	private Map()
	{
		super(0, 0);
		this.ZIndex(-1);
		Dimension = (int) Variables.GetInstance().Get("m_width").Current();
		HorizontalTileNum = (int) Variables.GetInstance().Get("m_width").Current();
		VerticalTileNum = (int) Variables.GetInstance().Get("m_height").Current();
		TileSize = (int) Variables.GetInstance().Get("m_tilesize").Current();
		paddingTiles = 2;
		MapTiles = new Tile[HorizontalTileNum][VerticalTileNum];
		activeTileWidth = (int)Math.ceil(interfaces.Render.GetInstance().Width() / TileSize + paddingTiles);
		activeTileHeight = (int)Math.ceil(interfaces.Render.GetInstance().Height() / TileSize + paddingTiles);		
		activeTiles = new Tile[activeTileWidth][activeTileHeight];
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

		MaterialFile Sand1 = new MaterialFile("resources\\ingame\\tiles\\sand1.png", MaterialFile.Type.PNG);
		Sand1.Open();
		FileManager.getInstance().Add(Sand1);
		
		MaterialFile Sand2 = new MaterialFile("resources\\ingame\\tiles\\sand2.png", MaterialFile.Type.PNG);
		Sand2.Open();
		FileManager.getInstance().Add(Sand2);

		MaterialFile Sand3 = new MaterialFile("resources\\ingame\\tiles\\sand3.png", MaterialFile.Type.PNG);
		Sand3.Open();
		FileManager.getInstance().Add(Sand3);
		
		MaterialFile Sand4 = new MaterialFile("resources\\ingame\\tiles\\sand4.png", MaterialFile.Type.PNG);
		Sand4.Open();
		FileManager.getInstance().Add(Sand4);
		
		MaterialFile Sand5 = new MaterialFile("resources\\ingame\\tiles\\sand5.png", MaterialFile.Type.PNG);
		Sand5.Open();
		FileManager.getInstance().Add(Sand5);
		
		MaterialFile Sand6 = new MaterialFile("resources\\ingame\\tiles\\sand6.png", MaterialFile.Type.PNG);
		Sand6.Open();
		FileManager.getInstance().Add(Sand6);
		
		MaterialFile MountainTile = new MaterialFile("resources\\ingame\\tiles\\mountain.png", MaterialFile.Type.PNG);
		MountainTile.Open();
		FileManager.getInstance().Add(MountainTile);

		// int SizeActual = Dimension * Dimension;
		/*
		 * for (int i = 0; i < SizeActual; i++) { MapTiles[i] = new Tile(Tile.Type.GRASS); if (TileSize == 0) TileSize = MapTiles[0].Width(); Vector2D TilePos = GetCoordPos(i); MapTiles[i].Position(TilePos.x, TilePos.y); MapTiles[i].TileID = i; }
		 */

		for (int width = 0; width < HorizontalTileNum; width++)
		{
			for (int height = 0; height < VerticalTileNum; height++)
			{
				MapTiles[width][height] = new Tile(Tile.Type.GRASS);
				MapTiles[width][height].Position(width * TileSize, height * TileSize);
				MapTiles[width][height].TileID = width * height; // this is probably bad
			}
		}

		Resize(Dimension * TileSize, Dimension * TileSize);

		System.out.println("Num of active tiles  = " + activeTileWidth * activeTileHeight);
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
		int x = (int) (num % HorizontalTileNum); // offset for
		int y = (int) (Math.floor(num / VerticalTileNum)); // offset
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
	 * 
	 */
	public Tile GetTileFromIndex(int x, int y)
	{
		return MapTiles[x / TileSize][y / TileSize];
	}

	/*
	 * public Tile GetTileFromIndex(Vector2D V) { int index = GetTileIndex(V.x, V.y); if (index > -1 && index < MapTiles.length) return MapTiles[index]; else return new Tile(Tile.Type.GRASS); }
	 * 
	 * public Tile GetTileFromIndex(int i) { if (i > -1 && i < MapTiles.length) return MapTiles[i]; else return new Tile(Tile.Type.GRASS); }
	 */

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
		for (int width = 0; width < activeTileWidth; width++)
		{
			for (int height = 0; height < activeTileHeight; height++)
			{
				Tile T = activeTiles[width][height];
				if (T != null)
					T.Draw();
			}
		}
	}

	public Tile GetNextTile(int[] CurTile, specifier.Direction.Relative Dir)
	{
		switch (Dir)
		{
			case RIGHT:
				if (CurTile[0] + 1 < HorizontalTileNum)
					return MapTiles[CurTile[0] + 1][CurTile[1]];
			case LEFT:
				if (CurTile[0] - 1 > 0)
					return MapTiles[CurTile[0] - 1][CurTile[1]];
			case UP:
				if (CurTile[1] - 1 > 0)
					return MapTiles[CurTile[0]][CurTile[1] - 1];
			case DOWN:
				if (CurTile[1] + 1 < VerticalTileNum)
					return MapTiles[CurTile[0]][CurTile[1] + 1];
		}
		return MapTiles[CurTile[0]][CurTile[1]];
	}

	public Tile[] SurroundingTiles(Renderable R)
	{
		Tile[] Tiles = new Tile[4];
		int[] Curr = Game.GetInstance().Controllable().CurrentTile();
		
		Tiles[0] = MapTiles[Curr[0]][Maths.Clamp(0, VerticalTileNum -1, Curr[1] - 1)]; // top
		Tiles[1] = MapTiles[Maths.Clamp(0, HorizontalTileNum -1, Curr[0] + 1)][Curr[1]]; // left
		Tiles[2] = MapTiles[Curr[0]][Maths.Clamp(0, VerticalTileNum - 1, Curr[1] + 1)]; // bottom
		Tiles[1] = MapTiles[Maths.Clamp(0, HorizontalTileNum - 1, Curr[0] - 1)][Curr[1]];
		

		/*boolean Top = R.Position().y > 0 && R.Position().y < TileSize;
		boolean Bottom = R.Position().y < Dimension * TileSize && R.Position().y > (Dimension * TileSize) - TileSize;
		boolean Left = R.Position().x > 0 && R.Position().x < TileSize;
		boolean Right = R.Position().x < Dimension * TileSize && R.Position().x > (Dimension * TileSize) - TileSize;	
	
		/*if (Top)
		{
			if (Left)
			{
				Tiles = new int[4];

				Tiles[0] = Curr + 1; // MR
				Tiles[1] = Curr + Dimension; // BM
				Tiles[2] = Curr + Dimension + 1; // BR
				Tiles[3] = Curr;
			} else if (Right)
			{
				Tiles = new int[4];

				Tiles[0] = Curr - 1; // ML
				Tiles[1] = Curr + Dimension - 1; // BL
				Tiles[2] = Curr + Dimension; // BM
				Tiles[3] = Curr;
			} else
			{
				Tiles = new int[6];

				Tiles[0] = Curr - 1; // ML
				Tiles[1] = Curr;
				Tiles[2] = Curr + 1; // MR
				Tiles[3] = Curr + Dimension - 1; // BL
				Tiles[4] = Curr + Dimension; // BM
				Tiles[5] = Curr + Dimension + 1; // BR
			}
		} else if (Bottom)
		{
			if (Left)
			{
				Tiles = new int[4];

				Tiles[0] = Curr - Dimension; // TM
				Tiles[1] = Curr - Dimension + 1; // TR
				Tiles[2] = Curr + 1; // MR
				Tiles[3] = Curr;
			} else if (Right)
			{
				Tiles = new int[4];

				Tiles[0] = Curr - 1; // ML
				Tiles[1] = Curr - Dimension - 1; // TL
				Tiles[2] = Curr - Dimension; // TM
				Tiles[3] = Curr;
			} else
			{
				Tiles = new int[6];

				Tiles[0] = Curr - Dimension - 1; // TL
				Tiles[1] = Curr - Dimension; // TM
				Tiles[2] = Curr - Dimension + 1; // TR
				Tiles[3] = Curr - 1; // ML
				Tiles[4] = Curr;
				Tiles[5] = Curr + 1; // MR
			}
		} else
		{
			Tiles = new int[9];

			Tiles[0] = Curr - Dimension - 1; // TL
			Tiles[1] = Curr - Dimension; // TM
			Tiles[2] = Curr - Dimension + 1; // TR
			Tiles[3] = Curr - 1; // ML
			Tiles[4] = Curr;
			Tiles[5] = Curr + 1; // MR
			Tiles[6] = Curr + Dimension - 1; // BL
			Tiles[7] = Curr + Dimension; // BM
			Tiles[8] = Curr + Dimension + 1; // BR
		}*/

		return Tiles;
	}
	
	public specifier.MinimapItem[][] GetMinimap()
	{
		specifier.MinimapItem[][] Minimap = new specifier.MinimapItem[HorizontalTileNum][VerticalTileNum];
		for (int height = 0; height < VerticalTileNum; height++)
		{
			for (int width = 0; width < HorizontalTileNum; width++)
			{
				Tile T = MapTiles[width][height];
				switch (T.Type())
				{
					case GRASS:
						Minimap[width][height] = new specifier.MinimapItem(T.TileID, Color.green, new Vector2D(width, height));
						break;
					case OCEAN:
						Minimap[width][height] = new specifier.MinimapItem(T.TileID, Color.blue, new Vector2D(width, height));
						break;
					case DIRT:
						Minimap[width][height] = new specifier.MinimapItem(T.TileID, new Color(115, 69, 3, 1), new Vector2D(width, height));
						break;
					case SAND:
						Minimap[width][height] = new specifier.MinimapItem(T.TileID, Color.yellow, new Vector2D(width, height));
						break;
					case MOUNTAIN:
						Minimap[width][height] = new specifier.MinimapItem(T.TileID, Color.darkGray, new Vector2D(width, height));
						break;
					default:
						Minimap[width][height] = new specifier.MinimapItem(T.TileID, Color.black, new Vector2D(width, height));
				}

				//if (T.TileID == Game.GetInstance().Player().TileID())
					//Minimap[T.TileID] = new specifier.MinimapItem(T.TileID, Color.red, GetMinimapCoord(T.TileID));
			}
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

	public void Update()
	{
		// here we need to update the tiles that are visible to camera.
		Tile centerTile = Camera.getInstance().CameraCenterTile(Game.GetInstance().Controllable());
		
		int x = Maths.Clamp(0, HorizontalTileNum, ((centerTile.Position().x / 32) + 1- activeTileWidth / 2));
		int y = Maths.Clamp(0, VerticalTileNum, (centerTile.Position().y / 32) + 1 - activeTileHeight / 2);
		
		//for (int width = Maths.Clamp(0, centerTile[0] - (interfaces.Render.GetInstance().Width() / TileSize) / 2); width < centerTile[0] + (interfaces.Render.GetInstance().Width() / TileSize) / 2; width++)
		for(int width = 0; width < activeTileWidth; width++)
		{
			//for (int height = Maths.Clamp(0, centerTile[1] - (interfaces.Render.GetInstance().Height() / TileSize) / 2); height < centerTile[1] + (interfaces.Render.GetInstance().Height() / TileSize) / 2; height++)
			for (int height = 0; height < activeTileHeight; height++)
			{
				activeTiles[width][height] = MapTiles[x][y];
				if (y + 1 < VerticalTileNum)
					y++;
			}
			y = Maths.Clamp(0, VerticalTileNum - 1, (centerTile.Position().y / 32) + 1 - activeTileHeight / 2);
			x = Maths.Clamp(0, HorizontalTileNum - 1, x + 1);
			//x++;
		}
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
	
	public int[] getIndexFromPos(int x, int y)
	{
		int[] pos = { x / TileSize, y / TileSize };
		return pos;
	}
	
	public int maxPixelWidth()
	{
		return HorizontalTileNum * TileSize;
	}
	
	public int maxPixelHeight()
	{
		return VerticalTileNum * TileSize;
	}
	
}
