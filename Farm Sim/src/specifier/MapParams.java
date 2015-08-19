package specifier;

import game.Tile;

public class MapParams
{

	public int LargestFeat;
	public float Persistence;
	public int Seed;
	public float Frequency;
	public float Frequencies;
	public Tile.Type Type;
	
	public MapParams(int LF, float P, int S, float F, float FS, Tile.Type T)
	{
		this.LargestFeat = LF;
		this.Persistence = P;
		this.Seed = S;
		this.Frequency = F;
		this.Frequencies = FS;
		this.Type = T;
	}
}
