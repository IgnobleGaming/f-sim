package game;

import utilities.SimplexNoise;

public class Mapbuilder
{
	private static float Max = 0;
	private static float Min = 0;
	private static float Difference = 0;
	
	public static void MapGeneration(Map M)
	{
		float[][] Map = GenerateSimplexNoise(M.HorizontalTileNum(), M.VerticalTileNum());

		Difference = Max - Min;
		
		System.out.println("Max - " + Max + " | Min - " + Min + " | Diff - " + Difference + " | D/10 - " + Difference / 10);
		
		for (int y = 0; y < M.VerticalTileNum(); y++)
		{
			for (int x = 0; x < M.HorizontalTileNum(); x++)
			{
				// System.out.println(M.GetTileFromIndex(x, y));

				M.GetTileFromIndex(x * M.TileSize(), y * M.TileSize()).ChangeType(ProcessSimplex(Map[x][y]));
			}
		}
	}

	private static float[][] GenerateSimplexNoise(int width, int height)
	{

		SimplexNoise SimplexNoise = new SimplexNoise(4, 12, 1337);

		float[][] simplexnoise = new float[width][height];
		float frequency = 2.5f / (float) width;

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

	private static Tile.Type ProcessSimplex(float S)
	{
		//System.out.println("m + D/5 - " + (Min + Difference / 5) + " | m + D/4 - " + (Min + Difference / 4) + "");

	/*	if (S < Min + Difference / 5)
			return Tile.Type.WATER;
		else if (S > Min + Difference / 5 && S < Min + Difference / 4.8)
			return Tile.Type.SAND;
		else if (S > Min + Difference / 4.8 && S < Max - Difference / 5)
			return Tile.Type.GRASS;*/
		if (S > Max - Difference / 5)
			return Tile.Type.MOUNTAIN;
		else
			return Tile.Type.GRASS;
	}
}
