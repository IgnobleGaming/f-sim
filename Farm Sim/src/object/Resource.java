package object;

import java.util.Random;

import renderable.Renderable;

public class Resource extends Renderable
{
	/**
	 * Resource Class
	 * 
	 * @author Cocoa
	 */
	public enum Type
	{
		VEGETATION, FLORA, MINERAL, SEASHORE, MISC, UNKNOWN;
		
		public static Type Random()
		{
			Random Random = new Random();
			int rand = Random.nextInt(5);
			
			switch (rand)
			{
				case 0:
					return Type.VEGETATION;
				case 1:
					return Type.FLORA;
				case 2:
					return Type.MINERAL;
				case 3:
					return Type.SEASHORE;
				case 4:
					return Type.MISC;
				default:
					return Type.UNKNOWN;
			}
		}
	}
	
	/* Identification Variables */
	private Type Res_Type;
	private int ID;
	
	private int Num_Harvests;
	private boolean depleted;
	private int Skill_Req;
	//private Random Rand;
	private static Random Rand;
	
	
	/**
	 * Resource Constructor - Build a random resource
	 * @param width - Texture Width
	 * @param height - Texture Height
	 */
	protected Resource(int width, int height)
	{
		super(width, height);
		
		Rand = new Random();
		
		Res_Type = Type.Random();
		
		depleted = false;
	}
	
	/**
	 * Resource Constructor - Builds a resource of a specific type
	 * @param width - Texture Width
	 * @param height - Texture Height
	 * @param T - Type of Resource
	 * @param ID - ID of resource within a Type
	 */
	public Resource(int width, int height, Type T, int ID)
	{
		super(width, height);
		
		Res_Type = T;
		
		depleted = false;
	}
	
	public Object Harvest()
	{
		return null;
	}
	
	public int NumberOfHarvests()
	{
		return NumberOfHarvests(this.Res_Type, this.ID);
	}
	
	private static int NumberOfHarvests(Resource.Type T, int ID)
	{
		switch(T)
		{
			case VEGETATION:
				switch (ID)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					default:
				}
			case MINERAL:
				return Rand.nextInt();
			case FLORA:
			case MISC:
			case SEASHORE:
			default:
				return 1;
			case UNKNOWN:
				return 0;
				
		}
	}
}
