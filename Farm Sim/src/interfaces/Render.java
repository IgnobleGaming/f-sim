package interfaces;

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
			RenderQueue = new PriorityQueue<Renderable>(new utilities.RenderPriorityCompare());
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
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			while (RenderQueue.size() > 0)
				RenderQueue.remove().Draw();
			
			GL11.glEnd();
			
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
	
	public void Draw(float x, float y, float z, float w)
	{
		GL11.glVertex4f(x, y, z, w);
	}
	
	public static void DrawImage(interfaces.file.types.MaterialFile Mat, specifier.Vector2D Pos)
	{
		Mat.Bind();
		GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(100,100);
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(100+Mat.Width(),100);
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(100+Mat.Width(),100+Mat.Height());
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(100,100+Mat.Height());
	}
}
