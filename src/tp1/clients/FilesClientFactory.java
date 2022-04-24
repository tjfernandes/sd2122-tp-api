package tp1.clients;

import java.net.URI;

import tp1.api.service.util.Files;

public class FilesClientFactory {
    
    public static Files getClient(URI serverURI){

        String[] client = serverURI.toString().split("/");
        if( client[client.length - 1].equals("rest"))
           return new RestFilesClient(serverURI );
        else
           return new SoapFilesClient( serverURI );
    }
}
