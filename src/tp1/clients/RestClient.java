package tp1.clients;

import java.net.URI;
import java.util.function.Supplier;
import java.util.logging.Logger;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import tp1.api.service.util.Result;

public class RestClient {
	private static Logger Log = Logger.getLogger(RestClient.class.getName());

	protected static final int READ_TIMEOUT = 5000;
	protected static final int CONNECT_TIMEOUT = 5000;

	protected static final int RETRY_SLEEP = 3000;
	protected static final int MAX_RETRIES = 10;

	final URI serverURI;
	final Client client;
	final ClientConfig config;

	RestClient(URI serverURI) {
		this.serverURI = serverURI;
		this.config = new ClientConfig();

		config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
		config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
		
		this.client = ClientBuilder.newClient(config);
	}

	protected <T> T reTry(Supplier<T> func) {
		for (int i = 0; i < MAX_RETRIES; i++)
			try {
				return func.get();
			} catch (ProcessingException x) {
				Log.fine("ProcessingException: " + x.getMessage());
				sleep(RETRY_SLEEP);
			} catch (Exception x) {
				Log.fine("Exception: " + x.getMessage());
				x.printStackTrace();
				break;
			}
		return null;
	}

	/**
	 * Converts status code to respective enum ErrorCode
	 * @param status
	 * @return
	 */
	protected Result.ErrorCode statusToErrorCode(int status) {
		switch (status) {
			case 409: return Result.ErrorCode.CONFLICT;
			case 404: return Result.ErrorCode.NOT_FOUND;
			case 400: return Result.ErrorCode.BAD_REQUEST;
			case 403: return Result.ErrorCode.FORBIDDEN;
			case 500: return Result.ErrorCode.INTERNAL_ERROR;
			case 501: return Result.ErrorCode.NOT_IMPLEMENTED;
			default: break;
		}
		return null;
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException x) { // nothing to do...
		}
	}
}
