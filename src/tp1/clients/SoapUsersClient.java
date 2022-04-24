package tp1.clients;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import jakarta.xml.ws.Service;
import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

public class SoapUsersClient extends SoapClient implements Users {

    private static SoapUsers users;

    private static Service service = null;

    public SoapUsersClient(URI serverURI) {
        super(serverURI);
        		
		try {
            QName qname = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);
            this.service = Service.create( URI.create(serverURI + "/?wsdl").toURL(), qname);
            users = this.service.getPort(tp1.api.service.soap.SoapUsers.class);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
		

        //((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        //((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);     
    }

    @Override
    public Result<String> createUser(User user) {
        String result = null;
        try {
            result = users.createUser(user);
            return Result.ok(result);
        } catch (UsersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
        
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        User result = null;
        try {
            result = users.getUser(userId, password);
            return Result.ok(result);
        } catch (UsersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        User result = null;
        try {
            result = users.updateUser(userId, password, user);
            return Result.ok(result);
        } catch (UsersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        User result = null;
        try {
            result = users.deleteUser(userId, password);
            return Result.ok(result);
        } catch (UsersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        List<User> result = null;
        try {
            result = users.searchUsers(pattern);
            return Result.ok(result);
        } catch (UsersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Result.error(Result.ErrorCode.BAD_REQUEST);
    }
    
}
