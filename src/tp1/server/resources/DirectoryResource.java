package tp1.server.resources;

import java.util.List;

import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.server.javas.JavaDirectory;

public class DirectoryResource extends Resource implements RestDirectory{

    final Directory impl = new JavaDirectory();

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        return super.handleResults(impl.writeFile(filename, data, userId, password));
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
       super.handleResults(impl.deleteFile(filename, userId, password));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        super.handleResults(impl.shareFile(filename, userId, userIdShare, password));
        
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        super.handleResults(impl.unshareFile(filename, userId, userIdShare, password));
        
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return super.handleResults(impl.getFile(filename, userId, accUserId, password));
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return super.handleResults(impl.lsFile(userId, password));
    }
    
}
