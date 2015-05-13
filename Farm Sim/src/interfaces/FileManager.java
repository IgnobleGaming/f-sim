package interfaces;

import interfaces.FileTypes.DummyFile;

import java.util.Hashtable;

public class FileManager {
	int MaxFiles = 65536;
	Hashtable<String, IFile> Container;
	int Size = 0;
	
	public FileManager()
	{
		Container = new Hashtable<String, IFile>();
	}
	
	public boolean Add(IFile NewFile)
	{
		if (Size < MaxFiles)
		{
			Container.put(NewFile.getHash(), NewFile);
			Size++;
			return true;
		}
		
		// log that max file size reached
		return false;
	}
	
	public boolean Remove(IFile RemovingFile)
	{
		if (RemovingFile.InUse) // trying to remove a file that is still in use!
			return false;
		
		IFile Result = Container.remove(RemovingFile.getHash());
		if (Result != null)
		{
			Result.Delete();
			Size--;
			return true;
		}
		
		// log that file could not be found!
		return false;
		
	}
	
	public IFile Retrieve(String HashKey)
	{
		IFile LookingFor = Container.get(HashKey);
		
		if (LookingFor.Hash == "FILE") // could not actually find the file, but we don't want to return null
			return new DummyFile();
		else
			return LookingFor;
	}

}
