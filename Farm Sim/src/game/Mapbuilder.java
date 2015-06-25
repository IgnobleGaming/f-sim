package game;

import interfaces.Game;
import interfaces.Variables;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;

import java.util.EnumSet;
import java.util.Random;

import specifier.Vector2D;

/**
 * Map Buider An exercise in generating random maps
 * 
 * @author Cocoa
 *
 */

public class Mapbuilder
{
	/**
	 * Enum for Size of Map NOTE: May be deprecated as our size is a game variable although can still be used as random, unsure
	 */
	public enum Size
	{
		SMALL(512), MEDIUM(1024), LARGE(2048);
		private int value;

		Size(int val)
		{
			this.value = val;
		}
	}

	/**
	 * MASS An abstract class used to define and detail terrain
	 */
	private class Mass
	{
		/* North, South, East, West */
		protected Orientation MassOrientation;
		/* Array of tile indices that share borders with a different tile type */
		protected int Border[];

		/* Ending points of each box created by the mass */
		/* Size of each array is number of boxes + 1 */
		protected int EndX[];
		protected int EndY[];

		protected int RandMin;
		protected int RandMax;

		private int start;

		protected Tile.Type TType;

		/**
		 * Initializes and builds the structure of the Terrain Mass
		 * 
		 * @param Orient
		 *            - Map orientation (i.e. North South East West)
		 */
		protected void Init(Orientation Orient, Tile.Type T, boolean solid)
		{
			Rand = new Random(System.currentTimeMillis());

			this.MassOrientation = Orient;
			this.TType = T;

			if (TType == Tile.Type.WATER)
			{
				EndX = new int[(Dimension / 2) + 1];
				EndY = new int[(Dimension / 2) + 1];

				RandMin = 0;
				RandMax = 2;

				Max_Mass = (int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) * 4);

				start = 1;

				GenerateOrigin();
			} else if (TType == Tile.Type.SAND)
			{
				EndX = new int[(Dimension) + 1];
				EndY = new int[(Dimension) + 1];

				RandMin = 1;
				RandMax = 2;

				Max_Mass = (int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) * 2);

				start = 0;
			} else if (TType == Tile.Type.MOUNTAIN)
			{
				EndX = new int[(Dimension) + 1];
				EndY = new int[(Dimension) + 1];

				RandMin = 0;
				RandMax = 3;

				Max_Mass = (int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) * 5);

				start = 1;

				GenerateOrigin();
			}

			Border = new int[Dimension];

			if (solid)
			{
				// If North or South
				if (MassOrientation == Orientation.NORTH || MassOrientation == Orientation.SOUTH)
				{

					System.out.println("EndX length - " + EndX.length + ", EndY length - " + EndY.length);

					for (int i = start; i < EndX.length; i++)
					{

						// Even sized blocks on X-axis for now
						EndX[i] = ((Dimension / (EndX.length - 1)) * i);
						// We generate a random height from half of the maximum mass to the max mass
						EndY[i] = Rand.nextInt(Max_Mass) + Max_Mass / 2;

						if ((i > 0 && MassOrientation == Orientation.SOUTH) || (i > 1 && MassOrientation == Orientation.NORTH))
						{
							while (Math.abs(EndY[i - 1] - EndY[i]) < RandMin || Math.abs(EndY[i - 1] - EndY[i]) > RandMax)
							{
								EndY[i] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
								System.out.println("endy @ i - " + EndY[i] + " Endy @ i -1 - " + EndY[i - 1]);
							}
						}

					}
				}
				// If East or West
				else
				{
					for (int i = start; i < EndX.length; i++)
					{
						EndY[i] = ((Dimension / (EndX.length - 1)) * i);
						EndX[i] = Rand.nextInt((Max_Mass / 2)) + Max_Mass / 2;

						if (i > 0 || (i == 1 && TType == Tile.Type.SAND))
						{
							while (Math.abs(EndX[i - 1] - EndX[i]) < RandMin || Math.abs(EndX[i - 1] - EndX[i]) > RandMax)
								EndX[i] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
						}
					}
				}
			}
		}

		private void GenerateOrigin()
		{
			// Initialize the first X & Y index at their starting point
			switch (MassOrientation)
			{
				case NORTH:
					EndX[0] = 0;
					EndY[0] = 0;
					break;
				case SOUTH:
					EndX[0] = 0;
					EndY[0] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
					break;
				case EAST:
					EndX[0] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
					EndY[0] = 0;
					break;
				case WEST:
					EndX[0] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
					EndY[0] = 0;
					break;
			}
		}
	}

	private enum Orientation
	{
		NORTH, SOUTH, EAST, WEST, CENTER, DEFAULT;

		@SuppressWarnings({ "incomplete-switch", "unused" })
		public Orientation Opposite(Orientation O)
		{
			switch (O)
			{
				case NORTH:
					return SOUTH;
				case SOUTH:
					return NORTH;
				case EAST:
					return WEST;
				case WEST:
					return EAST;
			}

			return O;
		}
	}

	public enum MassType
	{
		SEA, MOUNTAIN, LAKE;
	}

	private Size Size;
	private int Dimension;
	private Map Map;
	private int Max_Mass;
	private Random Rand;
	private EnumSet<Orientation> Orientations;

	private Mass Ocean;
	private Mass Sand;
	private Mass Mountain;
	private Mass River;

	public Mapbuilder()
	{
		Rand = new Random(System.currentTimeMillis());
		// int Random = Rand.nextInt(3);
		// change depending on random

		Dimension = Size.value;
	}

	public Mapbuilder(Size S)
	{
		this.Rand = new Random(System.currentTimeMillis());
		this.Dimension = (int) Variables.GetInstance().Get("m_width").Current();
		this.Orientations = EnumSet.noneOf(Orientation.class);
		this.Ocean = new Mass();
		this.Sand = new Mass();
		this.Mountain = new Mass();
		this.River = new Mass();

		Map = game.Map.GetInstance();
	}

	public void init()
	{
		Orientations.add(Orientation.DEFAULT);
	}

	/**
	 * Build - When the MapBuilder has finished initializing all necessary variables, this is called to build the map
	 * 
	 * @return - Map that is generated
	 */
	public Map Build()
	{

		Map = game.Map.GetInstance();

		Land();
		Sea();
		Mountain();
		River();
		Decorate(Dimension * 3);
		DecorateBorders();

		return Map;
	}

	/**
	 * Land - Initializes all tiles in Map.MapTiles and sets them all to Grass
	 */
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

	private void Sea()
	{
		int count = 0;

		Orientation O = GenerateOrient();
		O = Orientation.NORTH;
		LockOrient(O);

		if (O != Orientation.DEFAULT)
		{
			Ocean.Init(O, Tile.Type.WATER, true);
			Sand.Init(O, Tile.Type.SAND, true);

			for (int i = 1; i < Ocean.EndX.length; i++)
			{

				switch (Ocean.MassOrientation)
				{
					case NORTH:
						for (int y = Ocean.EndY[0]; y < Ocean.EndY[i]; y++)
						{
							for (int x = Ocean.EndX[i - 1]; x < Ocean.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Ocean.TType);

								if (y == Ocean.EndY[i] - 1)
								{
									// System.out.println("Y = " + y);

									for (int yy = Ocean.EndY[i]; yy < Sand.EndY[i] + Ocean.EndY[i]; yy++)
									{
										Map.MapTiles[Map.GetTileIndex(x, yy)].ChangeType(Sand.TType);

										if (yy == (Sand.EndY[i] + Ocean.EndY[i]) - 1)
										{
											Sand.Border[count] = Map.GetTileIndex(x, yy);
											count++;
										}
									}
								}
							}
						}
						break;
					case SOUTH:
						for (int y = Dimension - (Ocean.EndY[i - 1] + Sand.EndY[i - 1]); y < Dimension - Ocean.EndY[i - 1]; y++)
						{
							for (int x = Ocean.EndX[i - 1]; x < Ocean.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Sand.TType);

								if (y == Dimension - (Ocean.EndY[i - 1] + Sand.EndY[i - 1]))
								{
									Sand.Border[count] = Map.GetTileIndex(x, y);
									count++;
								}
							}
						}

						for (int y = Dimension - Ocean.EndY[i - 1]; y < Dimension; y++)
						{
							for (int x = Ocean.EndX[i - 1]; x < Ocean.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Ocean.TType);
							}
						}
						break;
					case EAST:
						for (int y = Ocean.EndY[i - 1]; y < Ocean.EndY[i]; y++)
						{
							for (int x = Dimension - (Ocean.EndX[i - 1] + Sand.EndX[i - 1]); x < Dimension - Ocean.EndX[i - 1]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Sand.TType);

								if (x == Dimension - (Sand.EndX[i - 1] + Ocean.EndX[i - 1]))
								{
									Sand.Border[count] = Map.GetTileIndex(x, y);
									count++;
								}
							}
						}

						for (int y = Ocean.EndY[i - 1]; y < Ocean.EndY[i]; y++)
						{
							for (int x = Dimension - Ocean.EndX[i - 1]; x < Dimension; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Ocean.TType);
							}
						}
						break;
					case WEST:
						for (int y = Ocean.EndY[i - 1]; y < Ocean.EndY[i]; y++)
						{
							for (int x = 0; x < Ocean.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Ocean.TType);

								if (x == Ocean.EndX[i] - 1)
								{
									// System.out.println("Y = " + y);

									for (int xx = Ocean.EndX[i]; xx < Sand.EndX[i] + Ocean.EndX[i]; xx++)
									{
										Map.MapTiles[Map.GetTileIndex(xx, y)].ChangeType(Sand.TType);

										if (xx == (Sand.EndX[i] + Ocean.EndX[i]) - 1)
										{
											Sand.Border[count] = Map.GetTileIndex(xx, y);
											count++;
										}
									}
								}
							}
						}
						break;
					case DEFAULT:
					case CENTER:
						Logging.getInstance().Write(Type.WARNING, "Something Terrible has happened while generating OCEAN in MapBuilder");
						break;
				}
			}
		}
	}

	private void Mountain()
	{
		int count = 0;

		Orientation O = Orientation.DEFAULT;

		if (Orientations.contains(Orientation.NORTH))
		{
			O = Orientation.SOUTH;
			LockOrient(O);
		} else if (Orientations.contains(Orientation.SOUTH))
		{
			O = Orientation.NORTH;
			LockOrient(O);
		} else if (Orientations.contains(Orientation.EAST))
		{
			O = Orientation.WEST;
			LockOrient(O);
		} else if (Orientations.contains(Orientation.WEST))
		{
			O = Orientation.EAST;
			LockOrient(O);
		}

		Mountain.Init(O, Tile.Type.MOUNTAIN, true);

		if (O != Orientation.DEFAULT)
		{

			for (int i = 1; i < Mountain.EndX.length; i++)
			{

				switch (Mountain.MassOrientation)
				{
					case NORTH:
						for (int y = Mountain.EndY[0]; y < Mountain.EndY[i]; y++)
						{
							for (int x = Mountain.EndX[i - 1]; x < Mountain.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Mountain.TType);

								if (y == Mountain.EndY[i] - 1)
								{
									Mountain.Border[count] = Map.GetTileIndex(x, y);
									count++;
								}
							}
						}
						break;
					case SOUTH:
						for (int y = Dimension - Mountain.EndY[i - 1]; y < Dimension; y++)
						{
							for (int x = Mountain.EndX[i - 1]; x < Mountain.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Mountain.TType);

								if (y == Dimension - Mountain.EndY[i - 1])
								{
									Mountain.Border[count] = Map.GetTileIndex(x, y);
									count++;
								}
							}
						}
						break;
					case EAST:
						for (int y = Mountain.EndY[i - 1]; y < Mountain.EndY[i]; y++)
						{
							for (int x = Dimension - Mountain.EndX[i - 1]; x < Dimension; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Mountain.TType);

								if (x == Dimension - Mountain.EndX[i - 1])
								{
									Mountain.Border[count] = Map.GetTileIndex(x, y);
									count++;
								}
							}
						}
						break;
					case WEST:
						for (int y = Mountain.EndY[i - 1]; y < Mountain.EndY[i]; y++)
						{
							for (int x = 0; x < Mountain.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Mountain.TType);

								if (x == Mountain.EndX[i] - 1)
								{
									Mountain.Border[count] = Map.GetTileIndex(x, y);
									count++;
								}
							}
						}
						break;
					case DEFAULT:
					case CENTER:
					default:
						Logging.getInstance().Write(Type.ERROR, "Something Terrible has happened while generating OCEAN in MapBuilder");
						break;
				}
			}
		}
	}

	private void River()
	{
		Orientation MountainO = Mountain.MassOrientation;
		Orientation Direction = Mountain.MassOrientation.Opposite(Mountain.MassOrientation);

		int bends = 7;

		switch (MountainO)
		{
			case NORTH:
				break;
			case SOUTH:
				break;
			case EAST:
				break;
			case WEST:
				break;
			case DEFAULT:
			default:
				Logging.getInstance().Write(Type.ERROR, "Default case in River Generation");
				break;

		}
	}

	private Orientation GenerateOrient()
	{
		int rand = -1;
		int i = 0;

		Orientation O = Orientation.DEFAULT;

		while (!Orientations.contains(O))
		{
			rand = Rand.nextInt(4);

			switch (rand)
			{
				case 0:
					O = Orientation.NORTH;
					break;
				case 1:
					O = Orientation.SOUTH;
					break;
				case 2:
					O = Orientation.EAST;
					break;
				case 3:
					O = Orientation.WEST;
					break;
				default:
					O = Orientation.SOUTH;
					break;
			}

			i++;

			if (i == 3)
			{
				O = Orientation.DEFAULT;
				break;
			}
		}

		return O;
	}

	/**
	 * Lock Orientation - Once a Land Mass is Created we need to lock its orientation so no other can use it
	 * 
	 * @param Orient
	 *            - the orientation that is used
	 */
	private void LockOrient(Orientation Orient)
	{
		Orientations.add(Orient);
	}

	/**
	 * Check Orientation - Determine if check is contained in the Enum Set
	 * 
	 * @param check
	 *            - The orientation in question
	 * @return - True if the Enum Set contains that flag, false otherwise
	 */
	private boolean CheckOrient(Orientation check)
	{
		if (Orientations.contains(check))
			return true;

		return false;
	}

	/**
	 * 
	 * @param max
	 */
	private void Decorate(int max)
	{
		int Index = Map.MapTiles[Rand.nextInt(Dimension * Dimension)].TileID;
		int Num_Deco = 0;
		int Max_Deco = max;

		boolean Running = true;

		while (Running)
		{
			if (Map.MapTiles[Index].Type() == Tile.Type.DIRT || Map.MapTiles[Index].Type() == Tile.Type.GRASS)
			{
				Map.MapTiles[Index].ChangeType(Tile.Type.WATER);

				Num_Deco++;
			}

			if (Map.MapTiles[Index].Type() == Tile.Type.SAND)
			{
				Map.MapTiles[Index].ChangeType(Tile.Type.MOUNTAIN);

				Num_Deco++;
			}

			if (Num_Deco == Max_Deco)
			{
				Running = false;
			}
			
			Index = Rand.nextInt(Dimension * Dimension);
		}
	}
	
	private void DecorateBorders()
	{
		int Max_Deco = Dimension / 10;
		int Random;
		
		for (int i = 0; i < Max_Deco; i++)
		{
			Random = Rand.nextInt(Dimension);
			Map.MapTiles[Ocean.Border[Random]].ChangeType(Tile.Type.DIRT);
			
			Random = Rand.nextInt(Dimension);
			Map.MapTiles[Mountain.Border[Random]].ChangeType(Tile.Type.SAND);
		}
	}
}
