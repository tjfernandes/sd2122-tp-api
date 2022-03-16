package tp1.api.service.util;

public interface Files {

	/**
	 * Write a new file.
	 * 
	 * @param password - secret for accessing the file server (in the first 
	 * project this is a system-wide constant).
     *
	 * @return 200 if success + unique id of the file.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<String> writeFile(String password);

	/**
	 * Delete an existing file.
	 * 
	 * @param fileId - unique id of the file. 
	 * @param password - secret for accessing the file server (in the first 
	 * project this is a system-wide constant).
	 * 
	 * @return 204 if success; 
	 *		   404 if the uniqueId does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<Void> deleteFile(String fileId, String password);

	/**
	 * Get the contents of the file. 
	 * 
	 * @param fileId - unique id of the file. 
	 * @param password - secret for accessing the file server (in the first 
	 * project this is a system-wide constant).
	 * 
	 * @return 200 if success + contents (through redirect to the File server); 
	 *		   404 if the uniqueId does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<byte[]> getFile(String userId, String password);

}
