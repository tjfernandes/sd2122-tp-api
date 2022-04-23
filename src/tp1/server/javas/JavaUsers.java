package tp1.server.javas;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.Discarder;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.inject.Singleton;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;
import tp1.api.service.util.Result.ErrorCode;
import tp1.clients.RestDirectoryClient;
import tp1.server.Discovery;

@Singleton
public class JavaUsers implements Users {

    public static Map<String,User> users = new ConcurrentHashMap<>();

	private static final Logger Log = Logger.getLogger(JavaUsers.class.getName());

    public JavaUsers() {

    }

	@Override
    public Result<String> createUser(User user) {
        Log.info("createUser : " + user);
		
		// Check if user data is valid
		if(user.getUserId() == null || user.getPassword() == null || user.getFullName() == null || 
				user.getEmail() == null) {
			Log.info("User object invalid.");
            return Result.error(ErrorCode.BAD_REQUEST);
		}
		
		// Check if userId already exists
		if( users.containsKey(user.getUserId())) {
			Log.info("User already exists.");
            return Result.error(ErrorCode.CONFLICT);
		}

		//Add the user to the map of users
        String userId = user.getUserId();
		users.put(userId, user);
        return Result.ok(userId);
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        Log.info("getUser : user = " + userId + "; pwd = " + password);
		
		User user = users.get(userId);
		
		// Check if user exists 
		if( user == null ) {
			Log.info("User does not exist.");
			return Result.error( ErrorCode.NOT_FOUND );
		}
		
		//Check if the password is correct
		if( !user.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			return Result.error( ErrorCode.FORBIDDEN );
		}
		
        return Result.ok(user);
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + user);
		if ( !users.containsKey(userId) ) {
			Log.info("User does not exist.");
			return Result.error( ErrorCode.NOT_FOUND );
		}

		User oldUser = users.get(userId);

		if( !oldUser.getPassword().equals(password)) {
			Log.info("Password is incorrect.");
			return Result.error( ErrorCode.FORBIDDEN );
		}

		if( user == null )
			return Result.error( ErrorCode.BAD_REQUEST );

		if ( badUserData(user) ) {
			if (user.getEmail() == null)
				user.setEmail(oldUser.getEmail());

			if (user.getFullName() == null)
				user.setFullName(oldUser.getFullName());

			if (user.getPassword() == null)
				user.setPassword(oldUser.getPassword());
		}

		user.setUserId(oldUser.getUserId());
        users.remove(userId);
        users.put(userId, user);
		return Result.ok(users.get(userId));
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        Log.info("deleteUser : user = " + userId + "; pwd = " + password);

		if ( !users.containsKey(userId) ) {
			Log.info("User does not exist.");
			return Result.error( ErrorCode.NOT_FOUND );
		}
		if( !users.get(userId).getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			return Result.error( ErrorCode.FORBIDDEN );
		}

		//remove files from shared

		Discovery discovery = Discovery.getInstance();

		try {
			var directoryURIs = discovery.knownUrisOf("directory");
			while( directoryURIs == null )
				directoryURIs = discovery.knownUrisOf("directory");

			var dirClient = new RestDirectoryClient(directoryURIs[0]);

			List<FileInfo> filesInfo = dirClient.lsFile(userId, password).value();
			for(FileInfo fInfo : filesInfo) {
				dirClient.deleteFile(fInfo.getFilename(), userId, password);
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		return Result.ok(users.remove(userId));
    }

	@Override
    public Result<List<User>> searchUsers(String pattern) {
        if(pattern == null)
			return Result.error( ErrorCode.BAD_REQUEST );

		Log.info("searchUsers : pattern = " + pattern);

		List<User> patternUsers = new ArrayList<User>();

		for(User u : users.values()) {
			if( u.getFullName().toLowerCase().contains(pattern.toLowerCase()) ) {
				User userNoPw = new User(u.getUserId(), u.getFullName(), u.getEmail(), "");
				patternUsers.add(userNoPw);
			}
		}

		return Result.ok(patternUsers);
    }

    private boolean badUserData(User user) {
		return user.getUserId() == null || user.getEmail() == null ||
				user.getFullName() == null || user.getPassword() == null;
	}
    
}