package tp1.server;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import tp1.server.resources.UsersResource;
import util.Debug;

public class RestUsersServer {

	private static Logger Log = Logger.getLogger(RestUsersServer.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}
	
	public static final int PORT = 8080;
	public static final String SERVICE = "users";
	private static final String SERVER_URI_FMT = "http://%s:%s/rest";
	
	public static void main(String[] args) {
		try {
			Debug.setLogLevel( Level.INFO, Debug.TP1 );
			
		ResourceConfig config = new ResourceConfig();
		config.register(UsersResource.class);
		//config.register(CustomLoggingFilter.class);
		//config.register(GenericExceptionMapper.class);
		
		String ip = InetAddress.getLocalHost().getHostAddress();
		String serverURI = String.format(SERVER_URI_FMT, ip, PORT);

		Discovery.getInstance().start(SERVICE, serverURI);

		JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);
	
		Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

		} catch( Exception e) {
			Log.severe(e.getMessage());
		}
	}	
}
