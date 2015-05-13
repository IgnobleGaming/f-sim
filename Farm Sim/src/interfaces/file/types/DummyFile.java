package interfaces.file.types;

import interfaces.file.IFile;


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
