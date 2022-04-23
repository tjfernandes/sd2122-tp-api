package tp1.clients;

import java.net.URI;
import java.util.List;

import tp1.api.User;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

public class SoapUsersClient extends RestClient implements Users {



    public SoapUsersClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public Result<String> createUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
