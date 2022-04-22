package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;

public class Resource {

    protected static final int MAX_RETRIES = 10;
    protected static final int RETRY_SLEEP = 1000;

    public <T> T handleResults(Result<T> func) {
        
        if (func.isOK())
            return func.value();

        handleErrors(func.error());

        return null; // Failure
    }

    public void handleErrors(ErrorCode error) {
        switch(error) {
            case CONFLICT:
                throw new WebApplicationException(Status.CONFLICT);
            case NOT_FOUND:
                throw new WebApplicationException(Status.NOT_FOUND);
            case BAD_REQUEST:
                throw new  WebApplicationException(Status.BAD_REQUEST);
            case FORBIDDEN:
                throw new WebApplicationException(Status.FORBIDDEN);
            case INTERNAL_ERROR:
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
            case NOT_IMPLEMENTED:
                throw new WebApplicationException(Status.NOT_IMPLEMENTED);
            default:
                break;
            
        }
    }

}