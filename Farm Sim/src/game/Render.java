package game;

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
	
	public Render()
	{
		try
		{
			start.Main.GameObject.Log().Write(Logging.Type.INFO, "== GFX INIT ==");
			RenderQueue = new PriorityQueue<Renderable>();
			int width = (int) start.Main.GameObject.Variables().get("vid_width").Current();
			int height = (int) start.Main.GameObject.Variables().get("vid_height").Current();
			boolean vsync = (boolean) start.Main.GameObject.Variables().get("vid_vsync").Current();
			start.Main.GameObject.Log().Write(Logging.Type.INFO, "width is %d // height is %d // vsync is %b", width, height, vsync);

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
			start.Main.GameObject.Log().Write(Logging.Type.ERROR, "Unable to initialized the OpenGL Context! -> %s", e.getLocalizedMessage());
			System.exit(0);
		}
	}

	public boolean Update()
	{
		if (!Display.isCloseRequested())
		{
			while (RenderQueue.size() > 0)
			{
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				RenderQueue.remove().Draw();
			}
			
			int maxfps = (int) start.Main.GameObject.Variables().get("vid_maxfps").Current();
			Display.update();
			Display.sync(maxfps);
			return true;
		}
		
		start.Main.GameObject.IsRunning = false;
		return false;
	}
	
	public void AddRenderElement(Renderable R)
	{
		RenderQueue.add(R);
	}
}
