package specifier;

import java.util.ArrayList;

public class Paperdoll
{
	private enum Set
	{
		BODY, SHIRT, PANTS, SHOES
	}

	protected ArrayList<Animation> Body;
	protected ArrayList<Animation> Shirt;
	protected ArrayList<Animation> Pants;
	protected ArrayList<Animation> Shoes;

	public Paperdoll()
	{

	}

	public void LoadPaperDoll(ArrayList<Animation> Body, ArrayList<Animation> Shirt, ArrayList<Animation> Pants, ArrayList<Animation> Shoes)
	{
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
}
