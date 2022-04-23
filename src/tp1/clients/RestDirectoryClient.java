package tp1.clients;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

public class RestDirectoryClient extends RestClient implements Directory {

    final WebTarget target;

    public RestDirectoryClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry(() -> {
            clt_deleteFile(filename, userId, password);
            return null;
        });
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
            clt_unshareFile(filename, userId, userIdShare, password);
            return null;
        });
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return super.reTry(() -> clt_lsFile(userId, password));
    }

    private Result<Void> clt_deleteFile(String filename, String userId, String password) {
        target.path(userId).path(filename)
            .queryParam("password", password).request()
            .delete();
        
        return Result.ok();
    }

    private Result<Void> clt_unshareFile(String filename, String userId, String userIdShare, String password) {
        target.path(userId).path(filename).path("/shared").path(userIdShare).request().delete();

        return Result.ok();
    }

    private Result<List<FileInfo>> clt_lsFile(String userId, String password) {
        Response r = target.path(userId)
            .queryParam("password", password).request()
            .accept(MediaType.APPLICATION_JSON)
            .get();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) 
            return Result.ok(r.readEntity(new GenericType<List<FileInfo>>() {}));
        else 
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return Result.error(super.statusToErrorCode(r.getStatus()));
    }

}