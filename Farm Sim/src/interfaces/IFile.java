package interfaces;

import java.io.FileInputStream;
import java.io.File;
import java.io.DataInputStream;
import java.io.IOException;

public abstract class IFile {
	protected String Path = "";
	protected long Size = 0;
	protected String Hash = "";
	protected byte[] Data = null;
	boolean InUse = false;
	
	public boolean Open(String Location, boolean Create, boolean WriteAccess) {
		Path = Location;
		File Buff = new File(Path);
		
		if (!Buff.exists() && !Create)
			return false;
		
		if (!Buff.canRead() || (WriteAccess && !Buff.canWrite()))
			return false;

		if (!Buff.exists())
			Size = 0;
		else
			Size = Buff.length();
		
		try {
			DataInputStream FS = new DataInputStream(new FileInputStream(Buff));
		
			if (Size > 0) {
					Data = new byte[(int)Size]; // bad because what if files size > int.maxvalue!!!
					FS.readFully(Data, 0, (int)Size);
			}
		
			FS.close();
			InUse = true;
		
			return true;
		}
		
		catch (IOException E)
		{
			// logger here
			return false;
		}
	}
	
	public void Delete()
	{
		InUse = false;
		Path = "";
		Size = 0;
		Hash = "";
		Data = null;
	}
	
	public String getHash()
	{
		return "Hash";
	}
}