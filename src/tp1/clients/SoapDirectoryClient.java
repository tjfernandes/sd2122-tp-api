package tp1.clients;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;
import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

public class SoapDirectoryClient extends SoapClient implements Directory {

    private static SoapDirectory directory;

    private static Service service = null;

    public SoapDirectoryClient(URI serverURI) {
        super(serverURI);	
		try {
            QName qname = new QName(SoapDirectory.NAMESPACE, SoapDirectory.NAME);	
            this.service = Service.create( URI.create(serverURI + "/?wsdl").toURL(), qname);
            directory = this.service.getPort(tp1.api.service.soap.SoapDirectory.class);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        FileInfo result = null;
        try {
            result = directory.writeFile(filename, data, userId, password);
            return Result.ok(result);
        } catch (DirectoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        try {
            directory.deleteFile(filename, userId, password);
            return Result.ok();
        } catch (DirectoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        try {
            directory.shareFile(filename, userId, userIdShare, password);
            return Result.ok();
        } catch (DirectoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        try {
            directory.shareFile(filename, userId, userIdShare, password);
            return Result.ok();
        } catch (DirectoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        try {
            var result = directory.getFile(filename, userId, accUserId, password);
            return Result.ok(result);
        } catch (DirectoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        List<FileInfo> result = null;
        try {
            result = directory.lsFile(userId, password);
            return Result.ok(result);
        } catch (DirectoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }
    
}
