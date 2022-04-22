package tp1.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.rest.RestFiles;
import java.net.URI;

public class RestFilesClient extends RestClient implements RestFiles {

    final WebTarget target;

    public RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestFiles.PATH);
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry(() -> {
            clt_writeFile(fileId, data, token);
            return null;
        });
    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry(() -> {
            clt_deleteFile(fileId, token);
            return null;
            });
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return super.reTry(() -> clt_getFile(fileId, token));
    }

    private void clt_writeFile(String fileId, byte[] data, String token) {
        target.path(fileId).request()
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
    }

    private void clt_deleteFile(String fileId, String token) {
        target.path(fileId).request()
            .accept(MediaType.APPLICATION_OCTET_STREAM)
            .delete();
    }

    private byte[] clt_getFile(String fileId, String token) {
        Response r = target.path(fileId).request()
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .get();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
            return r.readEntity(byte[].class);
        else
            System.out.println("Error, HTTP error status: " + r.getStatus() );
                
        return null;
    }
}
