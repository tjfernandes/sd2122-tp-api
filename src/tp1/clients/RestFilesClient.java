package tp1.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import java.net.URI;

public class RestFilesClient extends RestClient implements Files {

    final WebTarget target;

    public RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestFiles.PATH);
    }

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        return super.reTry(() -> clt_writeFile(fileId, data, token));
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return super.reTry(() -> clt_deleteFile(fileId, token));
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return super.reTry(() -> clt_getFile(fileId, token));
    }

    private Result<Void> clt_writeFile(String fileId, byte[] data, String token) {
        Response r = target.path(fileId).request()
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
        if( r.getStatus() == Status.NO_CONTENT.getStatusCode())
            return Result.ok();
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return Result.error(super.statusToErrorCode(r.getStatus()));
    }

    private Result<Void> clt_deleteFile(String fileId, String token) {
        Response r = target.path(fileId).request()
            .delete();

        if( r.getStatus() == Status.NO_CONTENT.getStatusCode())
            return Result.ok();
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return Result.error(super.statusToErrorCode(r.getStatus()));
    }

    private Result<byte[]> clt_getFile(String fileId, String token) {
        Response r = target.path(fileId).request()
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .get();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
            return Result.ok(r.readEntity(byte[].class));
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );

        return Result.error(super.statusToErrorCode(r.getStatus()));
    }

   

}
