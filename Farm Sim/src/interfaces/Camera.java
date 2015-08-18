package interfaces;

import game.Map;
import game.Tile;
import object.Entity;
import specifier.Vector2D;
import utilities.Maths;

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

		// Focus = new object.Entity("CameraFocus", "Entity that the camera points at", new specifier.Vector2D(0,0), new specifier.Vector(), 1, 1, object.Entity.Flag.VISIBLE);
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

		else if (R instanceof object.Entity)
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
		
		
		
		Focus.SetPosition(CameraCenterPos(potentialFocus));
		renderable.HUD.GetInstance().Position(Focus.Position());
		
		//System.out.println(Maths.borderClampLeft(Width, Map.GetInstance().TileSize()));
		//System.out.println("at " + curHorizontalViewMax);
		//System.out.println("max " + maxWidth);
		
		
		//Render.DrawQuad(Focus.Position().x, Focus.Position().y, curHorizontalViewMax - curHorizontalViewMin, curVerticalViewMax - curVerticalViewMin, Color.red);
			
		/*
	
		if (potentialFocus.Position().x + ((Width / 2) - game.Map.GetInstance().TileSize() / 2) * Distance > mWidth || potentialFocus.Position().x - ((Width / 2) - game.Map.GetInstance().TileSize() / 2) * Distance < 0)	
		{
			if (potentialFocus.Position().y + ((Height / 2)- game.Map.GetInstance().TileSize() / 2) * Distance > mHeight || potentialFocus.Position().y - ((Height / 2) - game.Map.GetInstance().TileSize() / 2) * Distance  < 0)
			{
				Focus.SetPosition(new specifier.Vector2D(Focus.Position().x, Focus.Position().y));
			}
			else
			Focus.SetPosition(new specifier.Vector2D(Focus.Position().x, potentialFocus.Position().y));
		}
		else if (potentialFocus.Position().y + ((Height / 2)- game.Map.GetInstance().TileSize() / 2) * Distance > mHeight || potentialFocus.Position().y - ((Height / 2) - game.Map.GetInstance().TileSize() / 2) * Distance  < 0)
			if (potentialFocus.Position().x + ((Width / 2) - game.Map.GetInstance().TileSize() / 2) * Distance > mWidth || potentialFocus.Position().x - ((Width / 2) - game.Map.GetInstance().TileSize() / 2) * Distance < 0)
			{
				Focus.SetPosition(new specifier.Vector2D(Focus.Position().x, Focus.Position().y));
			}
			else
				Focus.SetPosition(new specifier.Vector2D(potentialFocus.Position().x, Focus.Position().y));		
		else
			Focus.SetPosition(new specifier.Vector2D(potentialFocus.Position().x, potentialFocus.Position().y));*/
		
		renderable.HUD.GetInstance().Position(Focus.Position());
	}
	
	private Vector2D CameraCenterPos(Entity potentialFocus)
	{
		int maxWidth = Map.GetInstance().maxPixelWidth();
		int maxHeight = Map.GetInstance().maxPixelHeight();
		
		int curHorizontalViewMax = potentialFocus.Position().x + (Width / 2);
		int curHorizontalViewMin = potentialFocus.Position().x - (Width / 2); // we want to hit that 0 index
		
		int curVerticalViewMax = potentialFocus.Position().y + (Height / 2);
		int curVerticalViewMin = potentialFocus.Position().y - (Height / 2);
		
		int newX = -1, newY = -1;
		specifier.Vector2D newPos = new specifier.Vector2D(Focus.Position().x, Focus.Position().y);
		
		if (curHorizontalViewMax < maxWidth - 12 && curHorizontalViewMin > Maths.borderClampLeft(Width, Map.GetInstance().TileSize()))
			newX = potentialFocus.Position().x;
			
		if (curVerticalViewMax < maxHeight - 14 && curVerticalViewMin > Maths.borderClampTop(Height, Map.GetInstance().TileSize()))
			newY = potentialFocus.Position().y;
		
		if (newX > 0)
			newPos.x = newX;
		if (newY > 0)
			newPos.y = newY;
		
		return newPos;
	}
	
	public Tile CameraCenterTile(Entity potentialFocus)
	{
		int maxWidth = Map.GetInstance().maxPixelWidth();
		int maxHeight = Map.GetInstance().maxPixelHeight();
		
		int curHorizontalViewMax = potentialFocus.Position().x + (Width / 2);
		int curHorizontalViewMin = potentialFocus.Position().x - (Width / 2); // we want to hit that 0 index
		
		int curVerticalViewMax = potentialFocus.Position().y + (Height / 2);
		int curVerticalViewMin = potentialFocus.Position().y - (Height / 2);
		
		int newX = -1, newY = -1;
		specifier.Vector2D newPos = new specifier.Vector2D(Focus.Position().x, Focus.Position().y);
		
		if (curHorizontalViewMax < maxWidth - 12 && curHorizontalViewMin > Maths.borderClampLeft(Width, Map.GetInstance().TileSize()))
			newX = potentialFocus.Position().x;
			
		if (curVerticalViewMax < maxHeight - 14 && curVerticalViewMin > Maths.borderClampTop(Height, Map.GetInstance().TileSize()))
			newY = potentialFocus.Position().y;
		
		if (newX > 0)
			newPos.x = newX;
		if (newY > 0)
			newPos.y = newY;
		
		return Map.GetInstance().GetTileFromPosition(newPos.x, newPos.y);
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
		
		double left = Focus.Position().x - (Width / 2) * Distance;
		double right = Focus.Position().x + (Width / 2) * Distance;
		double top = Focus.Position().y - (Height / 2) * Distance;
		double bottom = Focus.Position().y + (Height / 2) * Distance;
		
		return new double[] { left, right, bottom, top };
	}
}
