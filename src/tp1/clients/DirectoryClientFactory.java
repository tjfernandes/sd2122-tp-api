package tp1.clients;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import tp1.api.service.util.Directory;
import tp1.server.Discovery;

public class DirectoryClientFactory {
    
    public static Directory getClient() throws MalformedURLException {

        URI serverURI = null;
        while (serverURI == null)
            try {
                serverURI = Discovery.getInstance().knownUrisOf("directory")[0];
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        String[] client = serverURI.toString().split("/");
        if( client[client.length - 1].equals("rest"))
           return new RestDirectoryClient(serverURI );
        else
           return new SoapDirectoryClient( serverURI );
     }
}

