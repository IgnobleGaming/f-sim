package game;

import utilities.SimplexNoise;

public class Mapbuilder
{
	public static void MapGeneration(Map M)
	{
		float[][] Map = GenerateSimplexNoise(M.HorizontalTileNum(), M.VerticalTileNum());

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

		SimplexNoise SimplexNoise = new SimplexNoise(141, 0.1, 55410);

		float[][] simplexnoise = new float[width][height];
		float frequency = 5.0f / (float) width;

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				simplexnoise[x][y] = (float) SimplexNoise.Noise(x * frequency, y * frequency);
			}
		}

		return simplexnoise;
	}

	private static Tile.Type ProcessSimplex(float S)
	{
		// System.out.println(S);

		if (S < -.8)
			return Tile.Type.WATER;
		else if (S > -.8 && S < -.6)
			return Tile.Type.SAND;
		else if (S > -.6 && S < .8)
			return Tile.Type.GRASS;
		else if (S > .8)
			return Tile.Type.MOUNTAIN;
		else
			return Tile.Type.WATER;
	}
}
