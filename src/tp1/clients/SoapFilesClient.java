package tp1.clients;

import java.net.MalformedURLException;
import java.net.URI;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class SoapFilesClient extends RestClient implements Files {

    private static SoapFiles files;

    private static Service service = null;

    public SoapFilesClient(URI serverURI) {
        super(serverURI);	
		try {
            QName qname = new QName(SoapFiles.NAMESPACE, SoapFiles.NAME);	
            this.service = Service.create( URI.create(serverURI + "/?wsdl").toURL(), qname);
            files = this.service.getPort(tp1.api.service.soap.SoapFiles.class);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
    }

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        try {
            files.writeFile(fileId, data, token);
            return Result.ok();
        } catch (FilesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        try {
            files.deleteFile(fileId, token);
            return Result.ok();
        } catch (FilesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
        
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        byte[] result = null;
        try {
            result = files.getFile(fileId, token);
            return Result.ok(result);
        } catch (FilesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

}
