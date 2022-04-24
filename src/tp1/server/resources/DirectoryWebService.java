package tp1.server.resources;

import java.util.List;

import jakarta.jws.WebService;
import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.util.Directory;
import tp1.server.javas.JavaDirectory;

@WebService(serviceName=SoapDirectory.NAME, targetNamespace=SoapDirectory.NAMESPACE, endpointInterface=SoapDirectory.INTERFACE)
public class DirectoryWebService implements SoapDirectory {

    final Directory impl = new JavaDirectory();

    public DirectoryWebService() {}

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException {
        var result = impl.writeFile(filename, data, userId, password);
        if (result.isOK())
            return result.value();
        else
            throw new DirectoryException();
    }

    @Override
    public void deleteFile(String filename, String userId, String password) throws DirectoryException {

        var result = impl.deleteFile(filename, userId, password);

        if(!result.isOK())
            throw new DirectoryException(); 
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password)
            throws DirectoryException {
            var result = impl.shareFile(filename, userId, userIdShare, password);

            if(!result.isOK())
                throw new DirectoryException(); 
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password)
            throws DirectoryException {
            var result = impl.unshareFile(filename, userId, userIdShare, password);

            if(!result.isOK())
                throw new DirectoryException();
        
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException {
        var result = impl.getFile(filename, userId, accUserId, password);

        if(result.isOK())
            return result.value();
        else
            throw new DirectoryException();
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) throws DirectoryException {
        var result = impl.lsFile(userId, password);

        if(result.isOK())
            return result.value();
        else
            throw new DirectoryException();
    }
    
}
