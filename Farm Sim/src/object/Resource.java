package object;

import interfaces.Game;
import interfaces.Objects;
import interfaces.file.FileManager;
import interfaces.file.types.MaterialFile;

import java.util.Random;

import renderable.Renderable;
import specifier.Vector2D;

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
	private static Resource Instance;
	//private Random Rand;
	private static Random Rand;
	private MaterialFile CurrentSprite;
	
	
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
		
		this.Res_Type = T;
		
		this.ID = ID;
		
		this.ZIndex(1);
		
		depleted = false;
	}
	
	// DEPRECATING NOT GOING TO WORK USING THIS
	public static Resource GetInstance()
	{
		if (Instance == null)
		{
			Instance = new Resource(32, 32, Type.MINERAL, 0);
		}
		return Instance;
	}
	
	public void Init(Vector2D pos)
	{
		this.XPos = pos.x;
		this.YPos = pos.y;
		
		MaterialFile Rock = new MaterialFile("resources\\rock1.png", MaterialFile.Type.PNG);
		Rock.Open();

		FileManager.getInstance().Add(Rock);
		
		SetResource();
	}
	
	public Object Harvest()
	{
		return null;
	}
	
	public int HarvestsLeft()
	{
		return Num_Harvests;
	}
	
	public int MaxNumberOfHarvests()
	{
		return MaxNumberOfHarvests(this.Res_Type, this.ID);
	}
	
	@Override
	public void Draw()
	{
		if (CurrentSprite == null)
			CurrentSprite = null;
		interfaces.Render.DrawImage(CurrentSprite, new Vector2D(XPos, YPos));
	}
	
	@Override
	public void Interact(Renderable R)
	{
		if (R instanceof Entity)
		{
			Num_Harvests -= 1;
			
			if (Num_Harvests <= 0)
			{
				Objects.GetInstance().Remove(this);
				depleted = true;
			}
		}
		else
		{
			
		}
	}
	
	public static int MaxNumberOfHarvests(Resource.Type T, int ID)
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
				return 1;
			case FLORA:
			case MISC:
			case SEASHORE:
			default:
				return 1;
			case UNKNOWN:
				return 0;
				
		}
	}
	
	public static int SkillReq(Resource R)
	{
		switch(R.Res_Type)
		{
			case VEGETATION:
				switch (R.ID)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					default:
				}
			case MINERAL:
				return 1;
			case FLORA:
			case MISC:
			case SEASHORE:
			default:
				return 1;
			case UNKNOWN:
				return 0;
				
		}
	}
	
	private void SetResource()
	{
		switch(this.Res_Type)
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
				switch (this.ID)
				{
					case 0:
						this.CurrentSprite = (MaterialFile) FileManager.getInstance().Retrieve("resources\\rock1.png");
					case 1:
					case 2:
						
				}
			case FLORA:
			case MISC:
			case SEASHORE:
			default:
			case UNKNOWN:
				
		}
		
		this.Num_Harvests = MaxNumberOfHarvests();
	}
	
	public MaterialFile Sprite()
	{
		return CurrentSprite;
	}
	
	public Type Type()
	{
		return this.Res_Type;
	}
	
	public boolean Depleted()
	{
		return depleted;
	}
}
