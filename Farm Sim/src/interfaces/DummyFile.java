package interfaces;

public class DummyFile extends IFile {

	DummyFile()
	{
		Path = "FILE";
		Size = 0;
		Hash = "FILE";
		Data = new byte[1];
		InUse = false;
	}
}
