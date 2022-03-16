package tp1.api.service.util;

import java.util.*;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import tp1.api.FileInfo;

public interface Directory {

	/**
	 * Write a new file. Only the owner can write the file.
	 * 
	 * @param filename - name of the file.
	 * @param data - contents of the file.
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return 200 if success + FileInfo representing the file.
	 *		   404 if the userId does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<FileInfo> writeFile(String filename, byte []data, 
			String userId, String password);

	/**
	 * Delete an existing file. Only the owner can delete the file.
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return 204 if success; 
	 *		   404 if the userId or filename does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<Void> deleteFile(String filename, String userId, String password);

	/**
	 * Share a file with a user. Only the owner can share the file.
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param userIdShare - id of the user to share the file with.
	 * @param password - the password of the user.
	 * 
	 * @return 204 if success; 
	 *		   404 if the userId or userIdShare or filename does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<Void> shareFile(String filename, String userId, 
			String userIdShare, String password);

	/**
	 * Unshare a file with a user. Only the owner can share the file.
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param userIdShare - id of the user to unshare the file with.
	 * @param password - the password of the user.
	 * 
	 * @return 204 if success; 
	 *		   404 if the userId or userIdShare or filename does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<Void> unshareFile(String filename, String userId, 
			String userIdShare, String password);

	/**
	 * Get the contents of the file. 
	 * Who can read a file: the owner and the user with whom the file has been shared.
	 * 
	 * This operation should be implemented using HTTP redirect on success.
	 * 
	 * @param filename - name of the file.
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return 200 if success + contents (through redirect to the File server); 
	 *		   404 if the userId or filename does not exist.
	 *         403 if the password is incorrect or the user cannot access the file.
	 * 		   400 otherwise.
	 */
	Result<byte[]> getFile(String filename, String userId, String password);

	/**
	 * List the files a given user has access to - this includes both its own files
	 * and the files shared with him. 
	 *  
	 * @param userId - id of the user.
	 * @param password - the password of the user.
	 * 
	 * @return 200 if success + list of FileInfo; 
	 *		   404 if the userId does not exist.
	 *         403 if the password is incorrect.
	 * 		   400 otherwise.
	 */
	Result<List<FileInfo>> lsFile(String userId, String password);



}
