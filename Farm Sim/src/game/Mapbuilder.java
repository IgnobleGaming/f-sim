package game;

import java.util.Random;

import specifier.MapParams;
import utilities.SimplexNoise;

public class Mapbuilder
{
	private static float Max = 0;
	private static float Min = 0;
	private static float Difference = 0;

	private static Random Rand;

	static MapParams MP;

	public static void MapGeneration(Map M)
	{
		Rand = new Random(System.currentTimeMillis());

		float[][] Map = new float[M.HorizontalTileNum()][M.VerticalTileNum()];

		for (Tile.Type T : Tile.Type.values())
		{
			if (T != Tile.Type.GRASS && T != Tile.Type.NONE)
			{
				MP = GenerateParameters(T);

				Map = GenerateSimplexNoise(M.HorizontalTileNum(), M.VerticalTileNum(), MP);
				Difference = Max - Min;

				for (int y = 0; y < M.VerticalTileNum(); y++)
				{
					for (int x = 0; x < M.HorizontalTileNum(); x++)
					{
						M.GetTileFromPosition(x * M.TileSize(), y * M.TileSize()).ChangeType(ProcessSimplex(Map[x][y], T));
					}
				}
			}
		}
		
		
	}

	private static float[][] GenerateSimplexNoise(int width, int height, MapParams MP)
	{

		SimplexNoise SimplexNoise = new SimplexNoise(MP.LargestFeat, MP.Persistence, MP.Seed, MP.Frequencies);

		float[][] simplexnoise = new float[width][height];
		float frequency = MP.Frequency / (float) width;

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				simplexnoise[x][y] = (float) SimplexNoise.Noise(x * frequency, y * frequency);

				if (simplexnoise[x][y] > Max)
					Max = simplexnoise[x][y];
				if (simplexnoise[x][y] < Min)
					Min = simplexnoise[x][y];
			}
		}

		return simplexnoise;
	}

	private static Tile.Type ProcessSimplex(float S, Tile.Type T)
	{

		// System.out.println("m + D/5 - " + (Min + Difference / 5) + " | m + D/4 - " + (Min + Difference / 4) + "");

		if (S < Min + Difference / 5 && T == Tile.Type.OCEAN)
			return Tile.Type.OCEAN;
		
		else if (S >= Min + Difference / 5 && S < Min + Difference / 4.5 && T == Tile.Type.SAND)
			return Tile.Type.SAND;
		
		else if (S >= Min + Difference / 4.5 && S < Max - Difference / 5 && T == Tile.Type.GRASS)
			return Tile.Type.GRASS;
			
		else if (S >= Max - Difference / 5 && T == Tile.Type.MOUNTAIN)
			return Tile.Type.MOUNTAIN;
		
		else
			return Tile.Type.NONE;
	}

	private static MapParams GenerateParameters(Tile.Type T)
	{
		switch (T)
		{
			case DIRT:
				return new MapParams(MP.LargestFeat, MP.Persistence, MP.Seed, MP.Frequency, MP.Frequencies);
			case GRASS:
				return new MapParams(MP.LargestFeat, MP.Persistence, MP.Seed, MP.Frequency, MP.Frequencies);
			case MOUNTAIN:
				return new MapParams(4, 12, Rand.nextInt(), 2.5f, (float) .15);
			case POND:
				return new MapParams(2020, 10 / 100, 500, 4f, 2);
			case SAND:
				return new MapParams(MP.LargestFeat, MP.Persistence, MP.Seed, MP.Frequency, MP.Frequencies);
			case OCEAN:
				return new MapParams(100, (Rand.nextInt(10) + 15) / 10, Rand.nextInt(), 1, 2);
			default:
				return new MapParams(1, 1, 1, 1, 2);

		}
	}
}
