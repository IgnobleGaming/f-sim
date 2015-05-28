package game;

import interfaces.Game;
import interfaces.Variables;
import interfaces.file.FileManager;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;

import java.util.EnumSet;
import java.util.Random;

import object.Entity.Flag;
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

		protected Tile.Type TType;

		/**
		 * Initializes and builds the structure of the Terrain Mass
		 * 
		 * @param Orient
		 *            - Map orientation (i.e. North South East West)
		 */
		@SuppressWarnings("incomplete-switch")
		protected void Init(Orientation Orient, Tile.Type T)
		{
			Rand = new Random(System.currentTimeMillis());

			this.MassOrientation = Orient;
			this.TType = T;
			EndX = new int [17];
			EndY = new int [17];

			Border = new int[Dimension];

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
					EndX[0] = 0;
					EndY[0] = 0;
					break;
			}
			
			// If North or South
			if (MassOrientation == Orientation.NORTH || MassOrientation == Orientation.SOUTH)
			{

				System.out.println("EndX length - " + EndX.length + ", EndY length - " + EndY.length);

				for (int i = 1; i < EndX.length; i++)
				{

					// Even sized blocks on X-axis for now
					EndX[i] = ((Dimension / (EndX.length - 1)) * i);
					// We generate a random height from half of the maximum mass to the max mass
					EndY[i] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
						
					while (Math.abs(EndY[i - 1] - EndY[i]) < 4 || Math.abs(EndY[i - 1] - EndY[i]) > 15)
						EndY[i] = Rand.nextInt(Max_Mass) + Max_Mass / 2;

					System.out.println("EndX & EndY @ " + i + " is " + EndX[i] + " " + EndY[i]);

				}
			}
			// If East or West
			else
			{
				for (int i = 1; i < EndX.length; i++)
				{
					EndY[i] = ((Dimension / (EndX.length)) * i);
					EndX[i] = Rand.nextInt((Max_Mass / 2)) + Max_Mass / 2;
					
					while (Math.abs(EndY[i - 1] - EndY[i]) < 4 || Math.abs(EndY[i - 1] - EndY[i]) > 15)
						EndY[i] = Rand.nextInt(Max_Mass) + Max_Mass / 2;
				}
			}


		}
	}

	private enum Orientation
	{
		NORTH, SOUTH, EAST, WEST, CENTER, DEFAULT;
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
	private Mass Mountain;

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
		Max_Mass = (int) (Math.round(Math.log10((double) Dimension) / Math.log10(2)) * 4);

		Map = game.Map.GetInstance();

		Land();
		Sea();

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

		Orientation O = GenerateOrient();
		O = Orientation.SOUTH;
		LockOrient(O);

		if (O != Orientation.DEFAULT)
		{
			Ocean.Init(O, Tile.Type.WATER);

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
							}
						}
						break;
					case SOUTH:
						for (int y = Dimension - Ocean.EndY[i - 1]; y < Dimension; y++)
						{
							for (int x = Ocean.EndX[i - 1]; x < Ocean.EndX[i]; x++)
							{
								Map.MapTiles[Map.GetTileIndex(x, y)].ChangeType(Ocean.TType);
							}
						}
						break;
					case EAST:
						break;
					case WEST:
						break;
					case DEFAULT:
					case CENTER:
						Logging.getInstance().Write(Type.WARNING, "Something Terrible has happened while generating OCEAN in MapBuilder");
						break;
				}
			}
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
}
