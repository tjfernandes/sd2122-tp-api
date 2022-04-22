package tp1.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import tp1.server.resources.FilesResource;
import tp1.server.util.GenericExceptionMapper;
import util.Debug;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestFilesServer {

    private static Logger Log = Logger.getLogger(RestFilesServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "files";
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    public static void main(String[] args) {
        try {
            Debug.setLogLevel( Level.INFO, Debug.TP1 );

            ResourceConfig config = new ResourceConfig();
            config.register(FilesResource.class);
            //config.register(CustomLoggingFilter.class);
            config.register(GenericExceptionMapper.class);

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);

            Discovery.getInstance().start(SERVICE, serverURI);
            JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config );

            Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

        } catch( Exception e) {
            Log.severe(e.getMessage());
        }
    }
}
