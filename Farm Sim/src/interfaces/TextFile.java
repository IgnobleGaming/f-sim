package interfaces;

public class TextFile extends IFile {

	public TextFile(String Location, boolean Create, boolean WriteAccess)
	{
		super(Location, Create, WriteAccess);
	}
	
	public String getText()
	{
		String Text = "";
		for (int i = 0; i < Data.length; i++)
			Text += (char)Data[i];
		return Text;
	}
	
	public int Length()
	{
		return Size;
	}
	
}
