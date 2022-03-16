package tp1.api.service.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path(RestDirectory.PATH)
public interface RestFiles {

	static final String PATH="/files";

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
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	String writeFile(@QueryParam("password") String password);

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
	@DELETE
	@Path("/{fileId}")
	void deleteFile(@PathParam("fileId") String fileId, @QueryParam("password") String password);

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
	@GET
	@Path("/{fileId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	byte[] getFile(@PathParam("fileId") String userId, @QueryParam("password") String password);

}
