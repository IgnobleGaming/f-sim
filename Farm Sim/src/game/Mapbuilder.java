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
		
		protected int EndX [];
		protected int EndY [];
		
		protected void Init(int Orient)
		{
			Orientation = Orient;
			EndX = new int [(int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) / 2) + 1];
			EndY = new int [(int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) / 2) + 1];
			
			Border = new int [Dimension];
			
			for(int i = 1; i < EndX.length; i++)
			{
				EndX[i] = ((Dimension / (EndX.length - 1)) * i);
				EndY[i] =  Rand.nextInt((Max_Mass / 2)) + Max_Mass / 2;
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
		Sea();
		DetailSea();

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

	// sea - 0
	private void Sea()
	{
		int SizeActual = Dimension * Dimension;
		int LineSize;
		sea = Rand.nextInt(4);
		sea = 0;
		int count = 0;
		
		int sea_sizes[] = new int [(int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) / 2)];
		


		switch (sea)
		{

			case 0: // NORTH
				for (int current = 0; current < (Max_Mass * Dimension); current++)
				{
					if (current > (Dimension * Max_Mass) - Dimension - 1)
					{
						Shore[count] = current;
						count++;
					}

					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				}
				break;

			case 1: // SOUTH
				LineSize = ((Dimension - Max_Mass) * Dimension) + Dimension;
				for (int current = (Dimension - Max_Mass) * Dimension; current < SizeActual; current++)
				{
					if (current < LineSize)
					{
						Shore[count] = current;
						count++;
					}

					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				}
				break;

			case 2: // EAST
				for (int current = Map.GetTileIndex(Dimension - Max_Mass, 0); current < SizeActual; current++)
				{
					if (count == 0)
					{
						Shore[count] = current;
						count++;
					}

					if (current % Dimension == 0)
					{
						current += (Dimension - Max_Mass);
						Shore[count] = current;
						count++;
					}

					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				}
				break;

			case 3: // WEST
				for (int current = 0; current < SizeActual - (Dimension - Max_Mass); current++)
				{
					if (current == SizeActual - (Dimension - Max_Mass) - 1)
					{
						Shore[count] = current;
					}

					if (Map.GetCoordPos(current).x / Map.TileSize >= Max_Mass)
					{
						Shore[count] = current - 1;
						current += Dimension - Max_Mass;
						count++;
					}

					Map.MapTiles[current].ChangeType(Tile.Type.WATER);
				}
				break;
		}
	}

	private void DetailSea()
	{
		int h = 0;
		int w = 0;
		int r = 0;

		for (int i = 0; i < Shore.length; i++)
		{
			if (Rand.nextInt(7) % 2 == 0)
			{
				switch (sea)
				{
					case 0: // NORTH
						h = 2;
						w = Rand.nextInt(20) + 1;
						r = Rand.nextInt(w + 1 / h) + 1;
						break;
					case 1: // SOUTH
						h = 2;
						w = Rand.nextInt(20) + 1;
						r = Rand.nextInt(w + 1 / h) + 1;
						break;
					case 2: // EAST
						h = Rand.nextInt(20) + 1;
						w = 2;
						r = Rand.nextInt(h + 1 / w) + 1;
						break;
					case 3: // WEST
						h = Rand.nextInt(20) + 1;
						w = 2;
						r = Rand.nextInt(h + 1 / w) + 1;
						break;
				}

				int TileX = Map.GetCoordPos(Shore[i]).x / Map.TileSize;
				int TileY = Map.GetCoordPos(Shore[i]).y / Map.TileSize;

				CreateSquare(w, h, TileX, TileY, Tile.Type.WATER, MassType.SEA, r);

				if (sea < 2)
					i += Rand.nextInt(5) + w + 1;
				else
					i += Rand.nextInt(5) + h + 1;
			}
		}
	}

	public void Laker()
	{
		Lake();
	}

	private void Lake()
	{
		for (int i = 0; i < Shore.length; i++)
		{
			System.out.println(Shore[i]);
			Map.MapTiles[Shore[i]].ChangeType(Tile.Type.DIRT);
		}

		System.out.println(Max_Mass + " " + Dimension);
	}



	private void CreateSquare(int W, int H, int StartX, int StartY, Tile.Type T, MassType Mass, int recursive)
	{
		if (H <= 0 || W <= 0)
			return;
		else
		{
			for (int y = StartY; y < H + StartY; y++)
			{
				for (int x = StartX; x < W + StartX; x++)
				{
					if (Map.GetTileIndex(x, y) < Map.MapTiles.length)
					{
						Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(T);
					}
				}
			}
			if (recursive > 0)
			{
				switch (Mass)
				{
					case SEA:
						if (sea < 2)
						{
							if (W == 3)
								W -= 1;
							W -= 2;

							if (sea == 0)
							{
								StartX += 1;
								StartY += 1;
							} else
							{
								StartX += 1;
								StartY -= 1;
							}
						} else
						{
							if (H == 3)
								H -= 1;
							H -= 2;

							if (sea == 2)
							{
								StartX -= 1;
								StartY += 1;
							} else
							{
								StartX += 1;
								StartY += 1;
							}
						}
						break;
					case MOUNTAIN:
						break;
					case LAKE:
						break;
				}
				CreateSquare(W, H, StartX, StartY, T, Mass, recursive - 1);
			}
		}
	}
	
	public void Stair(Mass Mass)
	{
		
		
	}

	public static interfaces.file.types.MaterialFile GetRandomTile()
	{
		interfaces.file.types.MaterialFile M;
		/*
		 * M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_flower.png"); switch (R.nextInt(10)) { case 0: case 1: case 2: case 3: M = (interfaces.file.types.MaterialFile)
		 * FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_flower.png"); break; case 4: M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_dirt.png"); break; case 7: case 8: M = (interfaces.file.types.MaterialFile)
		 * FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_stone.png"); break; case 9: M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\grass_water.png"); break; }
		 */
		M = (interfaces.file.types.MaterialFile) FileManager.getInstance().Retrieve("resources\\ingame\\tiles\\dirt.png");
		return M;
	}
}
