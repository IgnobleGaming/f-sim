package interfaces.file;

import interfaces.file.types.DummyFile;

import java.util.Hashtable;

/**
 * FileManager - Delegation class using a Hashtable container to hold all files
 * needed by the game.
 * 
 * 
 * @author RaidMax
 * @author cocoa_muffs (in documentation only)
 *
 */

public class FileManager {

	private final int MaxFiles = 65536;

	protected Hashtable<String, IFile> Container;
	protected int Size = 0;

	public FileManager() {
		Container = new Hashtable<String, IFile>();
	}

	/**
	 * Add - Adds a file to the container to be more portable
	 * 
	 * @param NewFile
	 *            - File to be added to container.
	 * @return - true if size has not reached limit and item was added,
	 *         otherwise return false
	 */
	public boolean Add(IFile NewFile) {
		if (Size < MaxFiles) // Do we have too many files?
		{
			Container.put(NewFile.getHash(), NewFile);
			Size++;
			return true;
		}

		// log that max file size reached
		return false;
	}

	/**
	 * Remove - Removes a file from the container as it is no longer needed
	 * 
	 * @param RemovingFile
	 *            - File to be removed, uses hash key for retrieval
	 * @return
	 */
	public boolean Remove(IFile RemovingFile) {
		if (RemovingFile.InUse) // trying to remove a file that is still in use!
			return false;

		IFile Result = Container.remove(RemovingFile.getHash()); // Gets hash to
																	// ensure
																	// item
																	// exists
		if (Result != null) // Does it?
		{
			Result.Delete();
			Size--;
			return true; // GREAT!
		}

		// log that file could not be found!
		return false;

	}

	/**
	 * Retrieve - Gets an item, you dumbass
	 * 
	 * @param HashKey
	 *            - Hash of item to get
	 * @return - item in question if it is found, dummyfile if not found
	 */
	public IFile Retrieve(String HashKey) {
		IFile LookingFor = Container.get(HashKey);

		if (LookingFor.Hash == "FILE") // could not actually find the file, but
										// we don't want to return null
			return new DummyFile();
		else
			return LookingFor;
	}

}
