package interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public abstract class IFile {
	protected String Path = "";
	protected int Size = 0;
	protected String Hash = "";
	protected byte[] Data = new byte[0];
	protected boolean InUse = false;
	protected boolean IsOpen = false;
	protected boolean IsWriteable = false;
	protected boolean WasCreated = false;

	public IFile() {

	}

	public IFile(String Location, boolean Create, boolean WriteAccess) {
		Path = System.getProperty("user.dir") + "\\" + Location;
		WasCreated = Create;
		IsWriteable = WriteAccess;
	}

	public boolean Open() {
		File Buff = new File(Path);

		if (Buff.length() > Integer.MAX_VALUE) // file requested is > 2.1GB no thank you ( also makes it safe to type cast long -> int)
			return false;

		if (!Buff.exists() && !WasCreated)
			return false;

		if (!Buff.canRead() || (IsWriteable && !Buff.canWrite()))
			return false;

		if (!Buff.exists())
			Size = 0;
		else
			Size = (int) Buff.length();

		try {
			DataInputStream FS = new DataInputStream(new FileInputStream(Buff));

			if (Size > 0) {
				Data = new byte[(int) Size]; // bad because what if files size > int.maxvalue!!!
				FS.readFully(Data, 0, (int) Size);
			}

			FS.close();
			InUse = true;

			return true;
		}

		catch (IOException E) {
			// logger here
			return false;
		}
	}

	public void Close() {
		Data = null;
		InUse = false;
		IsOpen = false;
	}

	public void Delete() {
		InUse = false;
		Path = "";
		Size = 0;
		Hash = "";
		Data = null;
	}

	public boolean Modify(byte[] NewData) {
		Data = null;
		Data = new byte[(int) NewData.length]; // update the in memory version

		for (int i = 0; i < Data.length; i++)
			Data[i] = NewData[i];
		Size = Data.length;

		if (WasCreated) // since we created it this run, we also want to modify it on the disk!
		{
			try {
				FileOutputStream OS = new FileOutputStream(Path);
				OS.write(Data);
				OS.close();
				return true;
			}

			catch (IOException FError) {
				return false;
			}

		}

		return true;
	}

	public String getHash() {
		return Path;
	}

	public InputStream getInputStream() {
		InputStream IS = new ByteArrayInputStream(Data);
		return IS;
	}
}