package tp1.server.resources;

import java.util.List;

import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Users;
import tp1.server.javas.JavaUsers;


public class UsersResource extends Resource implements RestUsers {

    final Users impl = new JavaUsers();

    @Override
    public String createUser(User user) {
        return super.handleResults(impl.createUser(user));
    }

    @Override
    public User getUser(String userId, String password) {
        return super.handleResults(impl.getUser(userId, password));
    }

    @Override
    public User updateUser(String userId, String password, User user) {
        return super.handleResults(impl.updateUser(userId, password, user));
    }

    @Override
    public User deleteUser(String userId, String password) {
        return super.handleResults(impl.deleteUser(userId, password));
    }

    @Override
    public List<User> searchUsers(String pattern) {
        return super.handleResults(impl.searchUsers(pattern));
    }
    
}
