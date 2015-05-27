package game;

import interfaces.Game;
import interfaces.Variables;
import interfaces.file.FileManager;

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

	private class Mass
	{
		protected int Orientation;
		protected int Border[];

		protected int EndX[];
		protected int EndY[];

		protected void Init(int Orient)
		{
			Orientation = Orient;
			EndX = new int[(int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) / 2) + 1];
			EndY = new int[(int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) / 2) + 1];

			Border = new int[Dimension];

			if (Orientation < 2)
			{
				for (int i = 1; i < EndX.length; i++)
				{
					EndX[i] = ((Dimension / (EndX.length)) * i);
					EndY[i] = Rand.nextInt((Max_Mass / 2)) + Max_Mass / 2;
				}
			}
			else
			{
				for (int i = 1; i < EndX.length; i++)
				{
					EndY[i] = ((Dimension / (EndX.length)) * i);
					EndX[i] = Rand.nextInt((Max_Mass / 2)) + Max_Mass / 2;
				}
			}

			switch (Orientation)
			{
				case 0: // NORTH
					EndX[0] = 0;
					EndY[0] = 0;
					break;
				case 1: // SOUTH
					EndX[0] = 0;
					EndY[0] = Dimension - EndY[1];
					break;
				case 2: // EAST
					EndX[0] = Dimension - EndX[1];
					EndY[0] = 0;
					break;
				case 3: // WEST
					EndX[0] = 0;
					EndY[0] = 0;
					break;
			}
		}
	}

	public enum MassType
	{
		SEA, MOUNTAIN, LAKE
	}

	private Size Size;
	private int Dimension;
	private Map Map;
	private int Max_Mass;
	private Random Rand;

	private int Shore[];

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
		Dimension = (int) Variables.GetInstance().Get("m_width").Current();
		Max_Mass = Dimension / 16;

		Shore = new int[Dimension];

		Map = game.Map.GetInstance();
	}

	public void init()
	{

	}

	public Map Build()
	{
		Max_Mass = Dimension / 16;

		Map = game.Map.GetInstance();

		Land();

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
			Map.MapTiles[i].TileID = i;
		}
		Map.Resize(Dimension * Map.TileSize, Dimension * Map.TileSize);
	}


}
