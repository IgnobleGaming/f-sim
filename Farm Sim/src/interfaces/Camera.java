package interfaces;

import interfaces.file.Logging;
import interfaces.file.Logging.Type;

public class Camera
{
	private static Camera Instance;
	private object.Entity Focus;
	//private 
	
	public Camera(int Width, int Height, float Distance)
	{
		this.Width = Width;
		this.Height = Height;
		this.Distance = Distance;
		
		//Focus = new object.Entity("CameraFocus", "Entity that the camera points at",  new specifier.Vector2D(0,0), new specifier.Vector(), 1, 1, object.Entity.Flag.VISIBLE);		
		Focus = Game.GetInstance().Player();
	}
	
	public static Camera getInstance()
	{
		if (Instance == null)
			Instance = new Camera(640, 480, 1);
		return Instance;		
	}
	
	public static Camera getInstance(int Width, int Height, float Distance)
	{
		if (Instance == null)
			Instance = new Camera(Width, Height, Distance);
		return Instance;		
	}
	
	public void SetDistance(double dist)
	{
		Distance = dist;
	}
	
	public boolean inViewPlane(renderable.Renderable R)
	{
		if (Focus == null)
			return false;
		
		if (R.getClass().isInstance(object.Entity.class) || R.getClass().isInstance(game.Tile.class) || R.getClass().isInstance(game.Map.class))
		{
			int maxWidth = Focus.Position().x + Width;
			int minWidth = Focus.Position().x - Width;
			int maxHeight = Focus.Position().y + Height;
			int minHeight = Focus.Position().y - Height;
		
			if (R.Position().x <= maxWidth && R.Position().x >= minWidth && R.Position().y <= maxHeight && R.Position().y >= minHeight )
				return true;
			else
				return false;
		}
		
		else
			return true;
		
		//Logging.getInstance().Write(Type.INFO, "x =  " + R.Position().x + " y = " + R.Position().y );
	}
	
	public boolean inViewPlane(game.Tile T)
	{
		if (Focus == null)
			return false;	
		
		double maxWidth = Focus.Position().x + Width / 1.75 * Distance;
		double minWidth = Focus.Position().x - Width / 1.75 * Distance;
		double maxHeight = Focus.Position().y + Height / 1.75 * Distance;
		double minHeight = Focus.Position().y - Height / 1.75 * Distance;
		
		if (T.Position().x <= maxWidth && T.Position().x >= minWidth && T.Position().y <= maxHeight && T.Position().y >= minHeight )
				return true;

		return false;
	}
	
	public void Update()
	{
		Focus = Game.GetInstance().Player();
	}
	
	public specifier.Vector2D cameraLookPoint()
	{
		return new specifier.Vector2D(Focus.Position().x, Focus.Position().y);
	}
	
	public int Width()
	{
		return Width;
	}
	
	public int Height()
	{
		return Height;
	}
	
	public double Distance()
	{
		return Distance;
	}
	
	public double[] translatedOrtho()
	{
		double left = Focus.Position().x - Width / 2 * Distance;
		double right = Focus.Position().x + Width / 2 * Distance;
		double top = Focus.Position().y - Height / 2 * Distance;
		double bottom = Focus.Position().y + Height / 2 * Distance;
		
		return new double[] { left, right, bottom, top };
	}
	
	private int Width;
	private int Height;
	private double Distance;
}
