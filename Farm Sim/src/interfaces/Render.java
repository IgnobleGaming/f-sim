package interfaces;

import interfaces.file.Logging;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.LWJGLException;

import java.util.ArrayList;
import java.util.PriorityQueue;

import renderable.Renderable;

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
			Logging.getInstance().Write(Logging.Type.ERROR, "Unable to initialized the OpenGL Context! -> %s", e.getLocalizedMessage());
			System.exit(0);
		}
	}

	public static Render GetInstance()
	{
		if (Instance == null)
			Instance = new Render();
		return Instance;
	}

	public boolean Update()
	{
		if (!Display.isCloseRequested())
		{
			UpdateRenderable();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			while (RenderQueue.size() > 0)
				RenderQueue.remove().Draw();

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
			if (R.Visible)
				RenderQueue.add(R);
		}
	}

	public void AddRenderElement(Renderable R)
	{
		RenderQueue.add(R);
	}

	public void Draw(float x, float y, float z, float w)
	{
		GL11.glVertex4f(x, y, z, w);
	}

	public static void DrawImage(interfaces.file.types.MaterialFile Mat, specifier.Vector2D Pos)
	{
		Mat.Texture().bind();
		GL11.glBegin(GL11.GL_QUADS);
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
	}

	public static void DrawPartialImage(interfaces.file.types.MaterialFile Mat, specifier.Vector2D Pos, float VerticalOffset, float HorizontalOffset, int Width, int Height)
	{
		Mat.Texture().bind();
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

	public static void DrawString(String Text, int x, int y, float size, utilities.FontSheet Sheet)
	{
		//Text = Text.toUpperCase();
		Sheet.BitMap.Bind();
		GL11.glBegin(GL11.GL_QUADS);
		for (int c = 0; c < Text.length(); c++)
		{
			int i = Text.charAt(c) - 32;
			
			float ww = Sheet.x1(i) * size;
			float hh = Sheet.y1(i) * size;
			
			float xx = x + Sheet.x0(i) * size;
			float yy = y + Sheet.y0(i) * size;
			// s0 = g.u, t0 = g.v
			GL11.glTexCoord2f(Sheet.s0(i), Sheet.t0(i)); 
			GL11.glVertex2f(xx, yy);
			
			GL11.glTexCoord2f(Sheet.s0(i), Sheet.t1(i)); 
			GL11.glVertex2f(xx, yy + hh);

			GL11.glTexCoord2f(Sheet.s1(i), Sheet.t1(i)); 
			GL11.glVertex2f(xx + ww, yy + hh);

			GL11.glTexCoord2f(Sheet.s1(i), Sheet.t0(i)); 
			GL11.glVertex2f(xx + ww, yy);
	     
	        x += Sheet.advance(i) * size;
		}

		GL11.glEnd();
	}

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
			FPS = (int) AccurateFPS - 1;
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
