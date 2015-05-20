package specifier;

import interfaces.Game;
import interfaces.Render;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
//import java.util.ArrayList;
import interfaces.file.types.MaterialFile;

public class Animation
{
	private MaterialFile[] Frames;
	private int CurrentFrameIndex = 0;
	private int AnimationTime = 0;
	private int FrameDuration;
	private int LastFrameChange = 0;
	public boolean Valid = false;
	
	public Animation(int length, MaterialFile... Anim)
	{
		for (int i = 0; i < Anim.length; i++)
		{
			if (Anim[i] == null)
			{
				Logging.getInstance().Write(Type.WARNING, "animation file has null frames!");
				return;
			}
		}
		
		Frames = Anim;
		AnimationTime = length;
		FrameDuration = AnimationTime / Frames.length;	
		Valid = true;
	}
	
	public MaterialFile GetCurrentFrame() // may be obselete
	{
		return Frames[CurrentFrameIndex];
	}
	
	public MaterialFile RequestNextFrame() // if animation frame time > delta we increment
	{
		LastFrameChange += Game.GetInstance().Delta(); // how much time has changed between last request
		
		if (LastFrameChange >= FrameDuration) // safe for us to increment
		{
			LastFrameChange = 0; // reset our counter
			CurrentFrameIndex++;
			if (CurrentFrameIndex >= Frames.length)
				CurrentFrameIndex = 0; // so we don't go out of bounds
		}
		
		return Frames[CurrentFrameIndex]; // will ALWAYS be a non null because we check during constructor
	}
}
