package interfaces;

import object.Entity;

public class Camera
{
	private static Camera Instance;
	private object.Entity Focus;
	
	private int Width;
	private int Height;
	private double Distance;

	public Camera(int Width, int Height, float Distance)
	{
		this.Width = Width;
		this.Height = Height;
		this.Distance = Distance;

		Focus = new Entity("Camera Focus", "Position where the camera is cented", new specifier.Vector2D(0, 0), new specifier.Vector(), 0, 0, null);
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
		else if (R instanceof game.Map || R instanceof renderable.HUD)
			return true;

		else if (R instanceof object.Entity || R instanceof object.Resource)
		{
			int maxWidth = (int) (Focus.Position().x + (Width / 2) * Distance);
			int minWidth = (int) (Focus.Position().x - (Width / 2) * Distance);
			int maxHeight = (int) (Focus.Position().y + (Height / 2) * Distance);
			int minHeight = (int) (Focus.Position().y - (Height / 2) * Distance);
			
			if (R.Position().x < maxWidth && R.Position().x > minWidth - 32 && R.Position().y < maxHeight && R.Position().y > minHeight - 32)
			{
				R.showing = true;
				return true;
			} else
			{
				R.showing = false;
				return false;
			}
		} else
			return false;
	}

	public boolean inViewPlane(game.Tile T)
	{
		if (Focus == null)
			return false;

		double maxWidth = Focus.Position().x + Width / 1.65 * Distance;
		double minWidth = Focus.Position().x - Width / 1.65 * Distance;
		double maxHeight = Focus.Position().y + Height / 1.65 * Distance;
		double minHeight = Focus.Position().y - Height / 1.65 * Distance;

		if (T.Position().x <= maxWidth && T.Position().x >= minWidth && T.Position().y <= maxHeight && T.Position().y >= minHeight)
			return true;

		return false;
	}

	public void Update()
	{
		Entity potentialFocus = Game.GetInstance().Player();
		
		if (potentialFocus == null)
			return;
		
		int mWidth = (int)interfaces.Variables.GetInstance().Get("m_width").Current() * 32;
		int mHeight = (int)interfaces.Variables.GetInstance().Get("m_height").Current() * 32;
		
		if (potentialFocus.Position().x + ((Width / 2) - game.Map.GetInstance().TileSize() / 2) * Distance > mWidth || potentialFocus.Position().x - ((Width / 2) - game.Map.GetInstance().TileSize() / 2) * Distance < 0)	
			Focus.SetPosition(new specifier.Vector2D(Focus.Position().x, potentialFocus.Position().y));
		else if (potentialFocus.Position().y + ((Height / 2)- game.Map.GetInstance().TileSize() / 2) * Distance > mHeight || potentialFocus.Position().y - ((Height / 2) - game.Map.GetInstance().TileSize() / 2) * Distance  < 0)
			Focus.SetPosition(new specifier.Vector2D(potentialFocus.Position().x, Focus.Position().y));		
		else
			Focus.SetPosition(new specifier.Vector2D(potentialFocus.Position().x, potentialFocus.Position().y));
		
		renderable.HUD.GetInstance().Position(Focus.Position());
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
		if (Focus == null)
			Focus = Game.GetInstance().Player();

		double left = Focus.Position().x - Width / 2 * Distance;
		double right = Focus.Position().x + Width / 2 * Distance;
		double top = Focus.Position().y - Height / 2 * Distance;
		double bottom = Focus.Position().y + Height / 2 * Distance;

		return new double[] { left, right, bottom, top };
	}
}
