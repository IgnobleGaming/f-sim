package interfaces;

import object.Entity;
import game.Mapbuilder;

public class Camera
{
	private static Camera Instance;
	private object.Entity Focus;
	

	// private

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
		boolean debug = true;
		
		
		if (Focus == null)
			return false;
		else if (R instanceof game.Map)
			return true;

		else if (R instanceof object.Entity || R instanceof object.Resource)
		{
			int maxWidth = (int) (Focus.Position().x + (Width / 2));
			int minWidth = (int) (Focus.Position().x - (Width / 2));
			int maxHeight = (int) (Focus.Position().y + (Height / 2));
			int minHeight = (int) (Focus.Position().y - (Height / 2));

			// System.out.println("MW - " + maxWidth + " mW " + minWidth + " MH - " + maxHeight + " mH - " + minHeight);

			if (R.Position().x < maxWidth && R.Position().x > minWidth && R.Position().y < maxHeight && R.Position().y > minHeight)
			{
				// System.out.println(R.getClass() + " = P - " + Focus.Position().x + ", " + Focus.Position().y + " || R - " + R.Position().x + ", " + R.Position().y);
				R.showing = true;
				return true;
			} else
			{
				if (R.showing)
				{
					boolean t1 = (R.Position().x <= maxWidth);
					boolean t2 = R.Position().x >= minWidth;
					boolean t3 = R.Position().y <= maxHeight;
					boolean t4 = R.Position().y >= minHeight;

					if (debug)
					{
						System.out.println(R.getClass());
						System.out.println("----------------------------------------------------");
						System.out.println("|                " + t4 + " " + minHeight);
						System.out.println("| " + t2 + " " + minWidth + "                               " + t1 + " " + maxWidth);
						System.out.println("|                     " + t3 + " " + maxHeight);
						System.out.println("----------------------------------------------------");

						System.out.println(Mapbuilder.Direction.DirectionOf(Focus.Position().x, Focus.Position().y, R.Position().x, R.Position().y));
						System.out.println("P - " + Focus.Position().x + ", " + Focus.Position().y + " || R - " + R.Position().x + ", " + R.Position().y);
						System.out.println("=======================================================");
					}
				}

				R.showing = false;
				return false;
			}
		} else
			return false;

		// Logging.getInstance().Write(Type.INFO, "x =  " + R.Position().x + " y = " + R.Position().y );
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
		
		if ((potentialFocus.Position().x + Width / 1.65) * Distance > mWidth || (potentialFocus.Position().x - Width / 1.65 ) * Distance < 0)	
			Focus.SetPosition(new specifier.Vector2D(Focus.Position().x, potentialFocus.Position().y));
		else if ((potentialFocus.Position().y + Height / 1.65) * Distance > mHeight || (potentialFocus.Position().y - Height / 1.65 ) * Distance  < 0)
			Focus.SetPosition(new specifier.Vector2D(potentialFocus.Position().x, Focus.Position().y));		
		else
			Focus.SetPosition(new specifier.Vector2D(potentialFocus.Position().x, potentialFocus.Position().y));
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

	private int Width;
	private int Height;
	private double Distance;
}
