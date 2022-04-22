package tp1.server.resources;

import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Files;
import tp1.server.javas.JavaFiles;

public class FilesResource extends Resource implements RestFiles {

    final Files impl = new JavaFiles();

	@Override
	public void writeFile(String fileId, byte[] data, String token) {
		super.handleResults(impl.writeFile(fileId, data, token));
	}

	@Override
	public void deleteFile(String fileId, String token) {
		super.handleResults(impl.deleteFile(fileId, token));
	}

	@Override
	public byte[] getFile(String fileId, String token) {
		return super.handleResults(impl.getFile(fileId, token));
	}
}