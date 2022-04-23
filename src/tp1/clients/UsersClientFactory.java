package tp1.clients;

import java.net.URI;
import java.net.URISyntaxException;

import tp1.api.service.util.Users;
import tp1.server.Discovery;

public class UsersClientFactory {
    
    public static Users getClient() {

        URI serverURI = null;
        while (serverURI == null)
            try {
                serverURI = Discovery.getInstance().knownUrisOf("users")[0];
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        String[] client = serverURI.toString().split("/");
        if( client[client.length - 1].equals("rest"))
           return new RestUsersClient( serverURI );
        else
           return new SoapUsersClient( serverURI );
     }
}
