package game;

import interfaces.Variables;
import interfaces.file.FileManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import specifier.Vector2D;

/**
 * Sea - 0 Grass - 1 Lake - 2 River - 3 Sand - 4 Mountain - 5
 * 
 * 
 * @author Cocoa
 *
 */

public class Mapbuilder
{
	public enum Size
	{
		SMALL(512), MEDIUM(1024), LARGE(2048);
		private int value;

		Size(int val)
		{
			this.value = val;
		}
	}

	private Size Size;
	private int Dimension;
	private Map Map;
	private int Max_Mass;
	private Random Rand;

	private int sea;

	public Mapbuilder()
	{
		Rand = new Random(System.currentTimeMillis());
		int Random = Rand.nextInt(3);
		// change depending on random
		Dimension = Size.value;
	}

	public Mapbuilder(Size S)
	{
		Rand = new Random(System.currentTimeMillis());
		Dimension = (int)Variables.GetInstance().Get("m_width").Current();
		Max_Mass = Dimension / 16;
		
		Map = game.Map.GetInstance();
	}

	public Map Build()
	{
		Rand = new Random(System.currentTimeMillis());
		Max_Mass = Dimension / 16;

		Map = game.Map.GetInstance();

		Land();
		Sea();

		return Map;
	}

	private void Land()
	{
		int SizeActual = Dimension * Dimension;

		for (int i = 0; i < SizeActual; i++)
		{
			Map.MapTiles[i] = new Tile(Tile.Type.GRASS);
			if (Map.TileSize == 0)
				Map.TileSize = Map.MapTiles[0].Width();
			Vector2D TilePos = Map.GetCoordPos(i);
			Map.MapTiles[i].Position(TilePos.x, TilePos.y);
		}
		Map.Resize(Dimension * Map.TileSize, Dimension * Map.TileSize);
	}

	// sea - 0
	private void Sea()
	{
		int SizeActual = Dimension * Dimension;
		sea = Rand.nextInt(4);
		sea = 3;

		switch (sea)
		{
			case 0: // NORTH
				for (int current = 0; current < (Max_Mass * Dimension); current++)
					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				break;
			case 1: // SOUTH
				for (int current = (Dimension - Max_Mass) * Dimension; current < SizeActual; current++)
					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				break;
			case 2: // EAST
				for (int current = Map.GetTileIndex(Dimension - Max_Mass, 0); current < SizeActual; current++)
				{
					if (current % Dimension == 0)
						current += (Dimension - Max_Mass);
					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				}
				break;
			case 3: // WEST
				for (int current = 0; current < SizeActual - (Dimension - Max_Mass); current++)
				{
					if (Map.GetCoordPos(current).x / Map.TileSize >= Max_Mass)
					{
						current += Dimension - Max_Mass;
					}
					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				}
				break;
		}
	}

	private void Lake()
	{

	}

	private void River()
	{
		int Random = Rand.nextInt(5) + 3;
		int startingX = (Dimension * Map.TileSize)/ Random;
		
		//for (int i = startingX; i < )
	}

	private void Mountain()
	{

	}

	private void Town()
	{

	}

	private void createSquare(int w, int h, int startX, int startY)
	{
		for (int i = startX; i < w + startX; i++)
		{

		}
	}

	public static interfaces.file.types.MaterialFile GetRandomTile()
	{
		Random R = new Random();
		interfaces.file.types.MaterialFile M;
		/*M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_flower.png");
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
		}*/
		M  = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\dirt.png");
		return M;
	}
}
