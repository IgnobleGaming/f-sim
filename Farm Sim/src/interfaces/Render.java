package interfaces;

import interfaces.file.Logging;
import interfaces.file.types.MaterialFile.Orientation;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.LWJGLException;

import java.util.ArrayList;
import java.util.PriorityQueue;

import renderable.Renderable;
import renderable.Renderable.Position;
import renderable.Renderable.PositionType;

/**
 * Handles all OpenGl calls and image draws
 * @author Michael
 *
 */
public class Render
{
	private PriorityQueue<Renderable> RenderQueue;
	private int maxfps = 60;
	private long TotalFrames = 0;
	private int DeltaTime = 0;
	private long LastFrame = 0;
	private int FPS;
	private int ScreenWidth, ScreenHeight;
	private static Render Instance;

	private Render()
	{
		try
		{
			Logging.getInstance().Write(Logging.Type.INFO, "== GFX INIT ==");
			RenderQueue = new PriorityQueue<Renderable>(new utilities.RenderPriorityCompare());
			int width = (int) Variables.GetInstance().Get("vid_width").Current();
			int height = (int) Variables.GetInstance().Get("vid_height").Current();
			boolean vsync = (boolean) Variables.GetInstance().Get("vid_vsync").Current();
			maxfps = (int) Variables.GetInstance().Get("vid_maxfps").Current();
			ScreenWidth = (int) Variables.GetInstance().Get("vid_width").Current();
			ScreenHeight = (int) Variables.GetInstance().Get("vid_height").Current();
			Logging.getInstance().Write(Logging.Type.INFO, "width is %d // height is %d // vsync is %b // maxfps is %d", width, height, vsync, maxfps);

			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(vsync);

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);

			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			GL11.glClearDepth(1);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glViewport(0, 0, width, height);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, width, height, 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
		} catch (LWJGLException e)
		{
			Logging.getInstance().Write(Logging.Type.ERROR, "unable to initialized the OpenGL Context! -> %s", e.getLocalizedMessage());
			System.exit(0);
		}
	}

	public static Render GetInstance()
	{
		if (Instance == null)
			Instance = new Render();
		return Instance;
	}

	/**
	 * Updates the render queue, clears the OpenGL buffer, and renders all the items in Render Queue
	 * @return ( true if the method completes, false if display needs to be closed )
	 */
	public boolean Update()
	{
		int counts [] = new int [3];
		counts[0] = 0; // E
		counts[1] = 0; // T
		counts[2] = 0; // R
		
		
		if (!Display.isCloseRequested())
		{
			UpdateRenderable();
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			while (RenderQueue.size() > 0)
			{
				Renderable R = RenderQueue.remove();

				
				if (R instanceof object.Entity)
					counts[0]++;
				if (R instanceof object.Resource)
					counts[2]++;
				
				R.Draw();
			}
			
			System.out.println("dump [e, t, r]" + counts[0] + " " + counts[1] + " " + counts[2]);

			Display.update();
			Display.sync((int) Variables.GetInstance().Get("vid_maxfps").Current());
			return true;
		}

		start.Main.GameObject.IsRunning = false;
		return false;
	}

	private void UpdateRenderable()
	{
		for (Renderable R : interfaces.Objects.GetInstance().Objs())
		{
			if (R.Visible && interfaces.Camera.getInstance().inViewPlane(R))
			{
				RenderQueue.add(R);
			}
		}
	}

	public void AddRenderElement(Renderable R)
	{
		RenderQueue.add(R);
	}

	/**
	 * 
	 * @param Mat ( Material file that contains the textures to be drawn )
	 * @param Pos ( 2 dimensional position to draw the textures at )
	 */
	public static void DrawImage(interfaces.file.types.MaterialFile Mat, specifier.Vector2D Pos)
	{
		Mat.Texture().bind();

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		double[] orthoPos = Camera.getInstance().translatedOrtho();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(orthoPos[0],orthoPos[1],orthoPos[2],orthoPos[3], 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		
		GL11.glBegin(GL11.GL_QUADS);

		switch (Mat.Facing())
		{
			case NORMAL:
				break;
			case UP:
				GL11.glRotatef(270, 0.0f, 0.0f, 1.0f);
				break;
			case LEFT:
				GL11.glRotatef(180, 0.0f, 0.0f, 1.0f);
				break;
			case RIGHT: 
				GL11.glRotatef(0, 0.0f, 0.0f, 1.0f);
				break;
			case DOWN:
				GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
				break;
		}
		
		// TOP LEFT
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(Pos.x - (Mat.Width() / 2), Pos.y - (Mat.Height() / 2));
		// TOP RIGHT
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(Pos.x + Mat.Width() / 2, Pos.y - (Mat.Height() / 2));
		// BOTTOM RIGHT
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(Pos.x + Mat.Width() / 2, (Pos.y) + Mat.Height() / 2);
		// BOTTOM LEFT
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(Pos.x - Mat.Width() / 2, (Pos.y) + Mat.Height() / 2);

		GL11.glEnd();
		
		Mat.SetOrientation(Orientation.NORMAL);

	}

	public static void DrawPartialImage(interfaces.file.types.MaterialFile Mat, specifier.Vector2D Pos, float VerticalOffset, float HorizontalOffset, int Width, int Height)
	{
		Mat.Texture().bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBegin(GL11.GL_QUADS);
		// TOP LEFT
		GL11.glTexCoord2f(0 + HorizontalOffset, 0 + VerticalOffset);
		GL11.glVertex2f(Pos.x - (Width / 2), Pos.y - (Height / 2));
		// TOP RIGHT	
		GL11.glTexCoord2f(1 - HorizontalOffset, 0 + VerticalOffset);
		GL11.glVertex2f(Pos.x + Width / 2, Pos.y - (Height / 2));
		// BOTTOM RIGHT
		GL11.glTexCoord2f(1 - HorizontalOffset, 1 - VerticalOffset);
		GL11.glVertex2f(Pos.x + Width / 2, (Pos.y) + Height / 2);
		// BOTTOM LEFT
		GL11.glTexCoord2f(0 + HorizontalOffset, 1 - VerticalOffset);
		GL11.glVertex2f(Pos.x - Width / 2, (Pos.y) + Height / 2);

		GL11.glEnd();
	}

	/**
	 * 
	 * @param Text ( string of characters to be drawn )
	 * @param x ( horizontal position -- absolute )
	 * @param y ( vertical position -- absolute )
	 * @param Colour ( color of text )
	 * @param Sheet ( the font sheet which includes the material and coordinates of the characters )
	 */
	public static void DrawString(String Text, int x, int y, org.newdawn.slick.Color Colour, utilities.FontSheet Sheet)
	{
		Sheet.BitMap.Bind();		
		GL11.glColor4f(Colour.r, Colour.g, Colour.b, Colour.a);
		GL11.glBegin(GL11.GL_QUADS);
		for (int c = 0; c < Text.length(); c++)
		{
			int i = Text.charAt(c) - 32;
		
			GL11.glTexCoord2f(Sheet.s0f(i), Sheet.t0f(i)); 
			GL11.glVertex2f(x + Sheet.x0f(i), y + Sheet.y0f(i));
			
			GL11.glTexCoord2f(Sheet.s0f(i), Sheet.t1f(i)); 
			GL11.glVertex2f(x + Sheet.x0f(i), y + Sheet.y1f(i));

			GL11.glTexCoord2f(Sheet.s1f(i), Sheet.t1f(i)); 
			GL11.glVertex2f(x + Sheet.x1f(i), y + Sheet.y1f(i));

			GL11.glTexCoord2f(Sheet.s1f(i), Sheet.t0f(i)); 
			GL11.glVertex2f(x + Sheet.x1f(i), y + Sheet.y0f(i));
	     
	        x += Sheet.Advancef(i);
		}
		GL11.glColor4f(1,1,1,1);
		GL11.glEnd();		
	}
	
	public static void DrawMap(specifier.MinimapItem[] Map)
	{
		specifier.Vector2D tempPos = renderable.Renderable.GetPosFromLocation(Position.TOPLEFT, PositionType.ABSOLUTE, Map.length, Map.length, 0,0, null);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glBegin(GL11.GL_QUADS);
		for (specifier.MinimapItem M : Map)
		{		
		    GL11.glColor3f(M.Color.r, M.Color.g, M.Color.b);
		    int x = tempPos.x + M.Position.x;
		    int y = tempPos.y + M.Position.y;
		    		
		    GL11.glVertex2i(x, y);
		    GL11.glVertex2i(x + 2, y);
		    GL11.glVertex2i(x + 2, y + 2);
		    GL11.glVertex2i(x, y + 2);
		    
		    GL11.glColor3f(1, 1, 1);
		}	
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	/**
	 *  Updates the current frame counter ( called every frame )
	 */
	public void updateFPS()
	{

		Game I = Game.GetInstance();

		if (LastFrame == 0)
			LastFrame = I.GameTime();

		TotalFrames += 1;
		DeltaTime += I.GameTime() - LastFrame;

		if (TotalFrames > maxfps)
		{
			double RenderTime = (double) (DeltaTime / 1000.0);
			double AccurateFPS = (double) (TotalFrames / RenderTime);
			FPS = (int) AccurateFPS;
			TotalFrames = 0;
			DeltaTime = 0;
		}

		LastFrame = I.GameTime();
	}

	public int Width()
	{
		return ScreenWidth;
	}

	public int Height()
	{
		return ScreenHeight;
	}

	public int FPS()
	{
		return FPS;
	}
}
