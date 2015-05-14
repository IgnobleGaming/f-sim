package renderable;

import interfaces.Objects;

import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.Color;

public class Console extends HUD
{
	private LinkedList<GUIFont> Lines;
	private static Console Instance;
	
	private Console()
	{
		Lines = new LinkedList<GUIFont>();
	}
	
	public static Console GetInstance()
	{
		if (Instance == null)
			Instance = new Console();
		
		return Instance;
	}
	
	public void WriteLine(String Message)
	{
		if (Lines.size() >=8);
		{
			//Lines.remove();
			Iterator<GUIFont> iter = Lines.iterator();
			while(iter.hasNext())
				iter.next().MoveUp();
		}
		GUIFont Adding = new GUIFont("Segoe UI", Message, GUIFont.Size.SMALL, Color.black, 5, Lines.size() * 15);	
		Adding.ZIndex(Lines.size() -1);
		Lines.addFirst(Adding);
	}
	
	public void Draw()
	{
		Iterator<GUIFont> iter = Lines.iterator();
		while(iter.hasNext())
			iter.next().Draw();
	}
}
