package interfaces;

import java.awt.TrayIcon.MessageType;

import interfaces.file.Logging;
import interfaces.file.Logging.Type;

public class Camera
{
	private static Camera Instance;
	private object.Entity Focus;
	
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
	
	public boolean inViewPlane(renderable.Renderable R)
	{
		if (Focus == null)
			return false;
		
		
		Logging.getInstance().Write(Type.INFO, "entity class is: " + R.getClass().toString() );
		
		if (R.getClass().isInstance(object.Entity.class) || R.getClass().isInstance(game.Tile.class) || R.getClass().isInstance(game.Map.class))
		{
			int maxWidth = Focus.Position().x + Width / 4;
			int minWidth = Focus.Position().x - Width / 4;
			int maxHeight = Focus.Position().y + Height / 4;
			int minHeight = Focus.Position().y - Height / 4;
		
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
		
		int maxWidth = Focus.Position().x + Width / 2;
		int minWidth = Focus.Position().x - Width / 2;
		int maxHeight = Focus.Position().y + Height / 2;
		int minHeight = Focus.Position().y - Height / 2;
		
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
	
	private int Width;
	private int Height;
	private float Distance;
}
