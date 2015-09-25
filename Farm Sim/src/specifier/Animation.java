package specifier;

import interfaces.Game;
import interfaces.Render;
import interfaces.file.Logging;
import interfaces.file.Logging.Type;
import org.newdawn.slick.Color;
import interfaces.file.types.MaterialFile;

/**
 * Animation class for renderable objects
 * 
 * @author Michael
 *
 */
public class Animation
{
	private MaterialFile[] Frames;
	private int[] FrameTimes;
	private int CurrentFrameIndex = 0;
	private int AnimationTime = 0;
	private int CurFrameDuration;
	private int LastFrameChange = 0;
	private double SpeedScale = 1;
	public boolean Valid = false;

	/**
	 * Create the new animation object
	 * 
	 * @param length
	 *            ( total length, in milliseconds of, animation, 0 if constant )
	 * @param Anim
	 *            ( array of texture files *no nulls* )
	 */
	public Animation(int length, MaterialFile... Anim)
	{
		for (int i = 0; i < Anim.length; i++)
		{
			if (Anim[i] == null)
			{
				Logging.getInstance().Write(Type.WARNING, "animation array has null frames!");
				return;
			}
		}

		Frames = Anim;
		AnimationTime = length;
		CurFrameDuration = (AnimationTime / Frames.length);
		Valid = true;
	}

	/**
	 * Overloaded animation object creation with separate frame times -- times and anim must be the same size!
	 * 
	 * @param length
	 *            ( total length, in milliseconds, of animation )
	 * @param times
	 *            ( integer array of each frame time )
	 * @param anim
	 *            ( array of texture files * no nulls *
	 */
	public Animation(int length, int[] times, MaterialFile... anim)
	{
		this(length, anim);
		FrameTimes = times;
	}

	public MaterialFile GetCurrentFrame() // may be obselete
	{
		return Frames[CurrentFrameIndex];
	}
	
	public MaterialFile GetNextFrame()
	{
		if (CurrentFrameIndex + 1 > Frames.length)
			CurrentFrameIndex = 0;
		else
			CurrentFrameIndex++;
		
		return Frames[CurrentFrameIndex];
	}
	
	public MaterialFile GetPrevFrame()
	{
		if (CurrentFrameIndex == 0)
			CurrentFrameIndex = Frames.length - 1;
		else
			CurrentFrameIndex--;
		
		return Frames[CurrentFrameIndex];
	}

	/**
	 * Get the material file for next frame if delta > frame duration
	 * 
	 * @return Material file of appropriate frame
	 */
	public MaterialFile RequestNextFrame() // if animation frame time > delta we increment
	{
		if (AnimationTime != 0)
		{
			LastFrameChange += Game.GetInstance().Delta(); // how much time has changed between last request

			if (FrameTimes != null) // check to see if we have a custom frame interval time
				CurFrameDuration = FrameTimes[CurrentFrameIndex] * (int) (1 / SpeedScale);

			if (LastFrameChange >= CurFrameDuration * (1 / SpeedScale)) // safe for us to increment
			{
				LastFrameChange = 0; // reset our counter
				CurrentFrameIndex++;
				if (CurrentFrameIndex >= Frames.length)
					CurrentFrameIndex = 0; // so we don't go out of bounds
			}
		}
		return Frames[CurrentFrameIndex]; // will ALWAYS be a non null because we check during constructor
	}

	/**
	 * Updates the animation speed when an entity's movement speed to scaled
	 * 
	 * @param Sc
	 *            ( factor by which to scale animation speed )
	 */
	public void UpdateSpeedScale(double Sc)
	{
		SpeedScale = Sc;
	}
	
	public void updateColorOverlay(Color C)
	{
		for (MaterialFile M : Frames)
			M.setOverlayColor(C);
	}
}
