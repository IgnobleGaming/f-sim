package specifier;

import java.util.ArrayList;

import interfaces.file.types.MaterialFile;
import object.Entity.State;

public class Paperdoll
{
	public enum Set
	{
		HAIR, BODY, SHIRT, PANTS, SHOES
	}

	protected ArrayList<Animation> Hair;
	protected ArrayList<Animation> Body;
	protected ArrayList<Animation> Shirt;
	protected ArrayList<Animation> Pants;
	protected ArrayList<Animation> Shoes;

	public Paperdoll()
	{

	}

	public void LoadPaperDoll(ArrayList<Animation> Hair, ArrayList<Animation> Body, ArrayList<Animation> Shirt, ArrayList<Animation> Pants, ArrayList<Animation> Shoes)
	{
		this.Hair = Hair;
		this.Body = Body;
		this.Shirt = Shirt;
		this.Pants = Pants;
		this.Shoes = Shoes;
	}

	public void UpdateSet(ArrayList<Animation> Update, Set S)
	{
		if (Update == null)
		{
			switch (S)
			{
				case HAIR:
					Hair = Update;
					break;
				case BODY:
					Body = Update;
					break;
				case PANTS:
					Pants = Update;
					break;
				case SHIRT:
					Shirt = Update;
					break;
				case SHOES:
					Shoes = Update;
					break;
				default:
					break;
			}
		}
	}
	
	public ArrayList<Animation> GetSet(Set S)
	{
		switch (S)
		{
			case HAIR:
				return Hair;
			case BODY:
				return Body;
			case PANTS:
				return Pants;
			case SHIRT:
				return Shirt;
			case SHOES:
				return Shoes;
			default:
				return new ArrayList<Animation>();
		}
	}
	
	private ArrayList<Animation> GetAnimations(State state)
	{
		ArrayList<Animation> Animations = new ArrayList<Animation>();
		
		Animations.add(Hair.get(state.Value()));
		Animations.add(Body.get(state.Value()));
		Animations.add(Shirt.get(state.Value()));
		Animations.add(Pants.get(state.Value()));
		Animations.add(Shoes.get(state.Value()));
		
		return Animations;
	}
	
	public ArrayList<MaterialFile> CurrentImages(State AnimState)
	{
		ArrayList<MaterialFile> Image = new ArrayList<MaterialFile>();
		
		for(Animation A : GetAnimations(AnimState))
			Image.add(A.RequestNextFrame());
		
		return Image;
	}
}
