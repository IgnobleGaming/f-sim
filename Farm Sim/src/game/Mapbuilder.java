package game;

import interfaces.Game;
import interfaces.file.Logging;

import java.util.Random;

import specifier.MapParams;
import utilities.SimplexNoise;

public class Mapbuilder
{
	private static float Max = 0;
	private static float Min = 0;
	private static float Difference = 0;

	private static Random Rand;
	
	private static Map Map;
	private static int TileSize;
	private static int XTileNum;
	private static int YTileNum;
	
	
	private static MapParams MapParams [];

	public static void MapGeneration(Map M)
	{
		Rand = new Random(System.currentTimeMillis());
		
		MapParams = GenerateNewParameters();
		
		Map = M;
		TileSize = M.TileSize();
		XTileNum = M.HorizontalTileNum();
		YTileNum = M.VerticalTileNum();
		
		GenerateNewMap();
		//VerifyQuality();
	}

	private static void GenerateNewMap()
	{
		float[][] TileSimplex = new float[XTileNum][YTileNum];
		int count = 0;
		
		ClearMap();
		
		for (Tile.Type T : Tile.Type.values())
		{
			if (T != Tile.Type.GRASS && T != Tile.Type.NONE)
			{
				TileSimplex = GenerateSimplexNoise(XTileNum, YTileNum, MapParams[count]);
				Difference = Max - Min;
	
				for (int y = 0; y < YTileNum; y++)
				{
					for (int x = 0; x < XTileNum; x++)
					{
						Map.GetTileFromPosition(x * TileSize, y * TileSize).ChangeType(ProcessSimplex(TileSimplex[x][y], T));
					}
				}
			}
			
			count++;
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

	private static MapParams[] GenerateNewParameters()
	{
		MapParams = new MapParams[Tile.Type.values().length];
		int count = 0;
				
		for(Tile.Type T : Tile.Type.values())
		{
			Game.GetInstance().Log().Write(Logging.Type.INFO, "MapBuilder.GeneratingNewParameters(): Generating %s", T.toString());
			MapParams[count] = GenerateParameter(T);
			
			count++;
		}
		
		return MapParams;
	}
	
	private static void GenerateNewParameter(Tile.Type T)
	{
		if (T == Tile.Type.OCEAN)
		{
			MapParams[0] = new MapParams(100, (Rand.nextInt(10) + 15) / 10, Rand.nextInt(), 1, 2, Tile.Type.OCEAN);
			MapParams[1] = new MapParams(MapParams[0].LargestFeat, MapParams[0].Persistence, MapParams[0].Seed, MapParams[0].Frequency, MapParams[0].Frequencies, Tile.Type.SAND);
			MapParams[2] = new MapParams(MapParams[0].LargestFeat, MapParams[0].Persistence, MapParams[0].Seed, MapParams[0].Frequency, MapParams[0].Frequencies, Tile.Type.GRASS);
		}
		else if (T == Tile.Type.MOUNTAIN)
		{
			MapParams[5] = new MapParams(4, 12, Rand.nextInt(), 2.5f, (float) .15, Tile.Type.MOUNTAIN);
		}
	}
	
	
	private static MapParams GenerateParameter(Tile.Type T)
	{
		switch (T)
		{	
			case OCEAN:
				return new MapParams(100, (Rand.nextInt(10) + 15) / 10, Rand.nextInt(), 1, 2, T);
			case SAND:
				return new MapParams(MapParams[0].LargestFeat, MapParams[0].Persistence, MapParams[0].Seed, MapParams[0].Frequency, MapParams[0].Frequencies, T);
			case GRASS:
				return new MapParams(MapParams[0].LargestFeat, MapParams[0].Persistence, MapParams[0].Seed, MapParams[0].Frequency, MapParams[0].Frequencies, T);
			case POND:
				return new MapParams(2020, 10 / 100, 500, 4f, 2, T);
			case DIRT:
				return new MapParams(MapParams[3].LargestFeat, MapParams[3].Persistence, MapParams[3].Seed, MapParams[3].Frequency, MapParams[3].Frequencies, T);
			case MOUNTAIN:
				return new MapParams(4, 12, Rand.nextInt(), 2.5f, (float) .15, T);
			case NONE:
			default:
				return new MapParams(1, 1, 1, 1, 2, T);

		}
	}
	
	public static int [] VerifyQuality()
	{
		int [] TypesCount = new int[Tile.Type.values().length]; // OCEAN, SAND, GRASS, POND, DIRT, MOUNTAIN, NONE
		
		for (int y = 0; y < YTileNum; y++)
		{
			for (int x = 0; x < XTileNum; x++)
			{
				TypesCount[Map.GetTileFromIndex(x, y).Type().ordinal()]++;
			}
		}
		
		return TypesCount;
		
		//CheckCount(TypesCount);
	}
	
	private static boolean[] CheckCount(int [] TC)
	{
		boolean[] Types = new boolean[TC.length]; 
		int count = 0;
		
		for (int C : TC)
		{
			switch(count)
			{
				case 0: // Ocean
					Types[count] = true;
					break;
				case 1: // Sand
					Types[count] = true;
					break;
				case 2: // Grass
					Types[count] = true;
					break;
				case 3: // Pond
					Types[count] = true;
					break;
				case 4: // Dirt
					Types[count] = true;
					break;
				case 5: // Mountain
					System.out.println(C);
					break;
				case 6: // None
					if (C != 0)
						Types[count] = true;
					break;
			}
		}
		
		return Types;
	}
	
	private static void ClearMap()
	{
		for (int y = 0; y < YTileNum; y++)
		{
			for (int x = 0; x < XTileNum; x++)
			{
				Map.GetTileFromIndex(x, y).ChangeType(Tile.Type.GRASS);
			}
		}
	}
	
	/*
	 * take in map 
	 * init random
	 * get map params 
	 * 	- OCEAN, SAND, GRASS, POND, DIRT, MOUNTAIN, NONE
	 * 
	 * 
	 * generate new map with params
	 */
}
