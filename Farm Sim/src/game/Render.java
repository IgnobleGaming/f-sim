package game;

import interfaces.Variables;
import interfaces.file.Logging;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;

import java.util.PriorityQueue;

import renderable.Renderable;

public class Render
{
	private PriorityQueue<Renderable> RenderQueue;
	private int maxfps = 60;
	
	public Render()
	{
		try
		{
			Logging.getInstance().Write(Logging.Type.INFO, "== GFX INIT ==");
			RenderQueue = new PriorityQueue<Renderable>();
			int width = (int)Variables.GetInstance().Get("vid_width").Current();
			int height = (int)Variables.GetInstance().Get("vid_height").Current();
			boolean vsync = (boolean)Variables.GetInstance().Get("vid_vsync").Current();			
			int maxfps = (int)Variables.GetInstance().Get("vid_maxfps").Current();
			Logging.getInstance().Write(Logging.Type.INFO, "width is %d // height is %d // vsync is %b // maxfps is %d", width, height, vsync, maxfps);

			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(vsync);

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);

			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glClearDepth(1);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glViewport(0, 0, width, height);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, width, height, 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		} catch (LWJGLException e)
		{
			Logging.getInstance().Write(Logging.Type.ERROR, "Unable to initialized the OpenGL Context! -> %s", e.getLocalizedMessage());
			System.exit(0);
		}
	}

	public boolean Update()
	{
		if (!Display.isCloseRequested())
		{
			UpdateRenderable();
			while (RenderQueue.size() > 0)
			{
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				RenderQueue.remove().Draw();
			}
			Display.update();
			Display.sync(maxfps);
			return true;
		}
		
		start.Main.GameObject.IsRunning = false;
		return false;
	}
	
	private void UpdateRenderable()
	{
		for (Renderable R : interfaces.Objects.GetInstance().Objs())
		{
			RenderQueue.add(R);
		}
	}
	
	public void AddRenderElement(Renderable R)
	{
		RenderQueue.add(R);
	}
}