package interfaces.FileTypes;

import interfaces.IFile;


public class DummyFile extends IFile {

	public DummyFile()
	{
		Path = "FILE";
		Size = 0;
		Hash = "FILE";
		Data = new byte[1];
		InUse = false;
	}
}
