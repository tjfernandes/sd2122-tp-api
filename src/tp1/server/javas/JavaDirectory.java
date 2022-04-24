package tp1.server.javas;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;
import tp1.clients.FilesClientFactory;
import tp1.clients.UsersClientFactory;
import tp1.server.Discovery;

public class JavaDirectory implements Directory{
    
    private static final Logger Log = Logger.getLogger(JavaDirectory.class.getName());
    private static Map<String, Map<String, FileInfo>> userFiles = new HashMap<>();

    public JavaDirectory() {}

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        Log.info("Writing " + filename + " of user " + userId + "...");

        String fileId = String.format("%s-%s", filename, userId);

        var user = UsersClientFactory.getClient().getUser(userId, password);
        if (!user.isOK()) {
            return Result.error(user.error());
        }

        URI serverURI = null;
        try {
            serverURI = Discovery.getInstance().knownUrisOf("files")[0];
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        var file = FilesClientFactory.getClient(serverURI);
        var result = file.writeFile(fileId, data, "token");
        if (!result.isOK()) {
            return Result.error(result.error());
        }

        if (!userFiles.containsKey(userId))
            userFiles.put(userId, new HashMap<String, FileInfo>());
    
        String path = String.format("%s%s/%s", serverURI, RestFiles.PATH, fileId);
        FileInfo fileInfo = new FileInfo(userId, filename, path, new HashSet<>());

        FileInfo fInfo = userFiles.get(userId).get(filename);

        if (fInfo == null) { 
            userFiles.get(userId).put(filename, fileInfo);
        } else
            fileInfo = fInfo;
         
        return Result.ok(fileInfo);
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        
        Log.info("Deleting " + filename + " of user " + userId + "...");

        String fileId = String.format("%s-%s", filename, userId);

        var user = UsersClientFactory.getClient().getUser(userId, password);
        if (!user.isOK()) {
            return Result.error(user.error());
        }

        URI serverURI = null;
        try {
            serverURI = Discovery.getInstance().knownUrisOf("files")[0];
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        var file = FilesClientFactory.getClient(serverURI).deleteFile(fileId, "token");
        if (!file.isOK()) {
            return Result.error(file.error());
        }



        Set<String> sharedUsers = userFiles.get(userId).get(filename).getSharedWith();
        for (String id : sharedUsers)
             userFiles.get(id).remove(filename);

        userFiles.get(userId).remove(filename);
        

        return Result.ok();
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        
        Log.info("ShareFile " + filename + " of user " + userId + "with user " + userIdShare + "...");


        String fileId = String.format("%s-%s", filename, userId);


        var user = UsersClientFactory.getClient().getUser(userId, password);
        if (!user.isOK()) {
            return Result.error(user.error());
        }
        
        var userShare = UsersClientFactory.getClient().getUser(userIdShare, "");
        if(userShare.error() == Result.ErrorCode.NOT_FOUND)
            return Result.error(Result.ErrorCode.NOT_FOUND);

        URI serverURI = null;
        try {
            serverURI = Discovery.getInstance().knownUrisOf("files")[0];
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        var file = FilesClientFactory.getClient(serverURI).getFile(fileId, "token");
        if (!file.isOK()) {
            return Result.error(file.error());
        }

        FileInfo fInfo = userFiles.get(userId).get(filename);

        Set<String> shared = fInfo.getSharedWith();
        if (shared == null)
            shared = new HashSet<>();
        if (!shared.contains(userIdShare))
            shared.add(userIdShare);
        if (!userFiles.get(userIdShare).containsKey(filename)) {
            fInfo.setSharedWith(shared);
            userFiles.get(userIdShare).put(filename, fInfo);
        }

        return Result.ok();
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        Log.info("UnshareFile " + filename + " of user " + userId + "with user " + userIdShare + "...");

        String fileId = String.format("%s-%s", filename, userId);


        var user = UsersClientFactory.getClient().getUser(userId, password);
        if (!user.isOK()) {
            return Result.error(user.error());
        }

        URI serverURI = null;
        try {
            serverURI = Discovery.getInstance().knownUrisOf("files")[0];
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        var file = FilesClientFactory.getClient(serverURI).getFile(fileId, "token");
        if (!file.isOK()) {
            return Result.error(file.error());
        }

        if (!userId.equals(userIdShare)) {
            FileInfo fInfo = userFiles.get(userId).get(filename);
            Set<String> sharedUsers = fInfo.getSharedWith();
            sharedUsers.remove(userIdShare);
    
            userFiles.get(userIdShare).remove(filename);
        }

        return Result.ok();
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        Log.info("Getting file " + filename + " from user " + userId + "...");

        String fileId = String.format("%s-%s", filename, userId);

        var result = UsersClientFactory.getClient().getUser(accUserId, password);
        if (!result.isOK()) {
            return Result.error(result.error());
        }
        
        FileInfo fileInfo = userFiles.get(userId).get(filename);
        
        if (fileInfo == null)
            return Result.error(ErrorCode.NOT_FOUND);

        Set<String> shared = fileInfo.getSharedWith();
        
        if(!shared.contains(accUserId) && !fileInfo.getOwner().equals(accUserId))
            return Result.error(ErrorCode.FORBIDDEN);

        URI serverURI = null;
        try {
            serverURI = Discovery.getInstance().knownUrisOf("directory")[0];
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        URI uri = URI.create(fileInfo.getFileURL().split("/files")[0]);
        if (serverURI.toString().endsWith("rest") && uri.toString().endsWith("rest")) {
            var r = Response.temporaryRedirect(URI.create(fileInfo.getFileURL())).build();
            throw new WebApplicationException(r);
        }
        
        var res = FilesClientFactory.getClient(uri).getFile(fileId, "token");
        if (res.isOK())
            return res;
        return Result.error(res.error());
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        Log.info("List files from user " + userId + "...");
            
        var result = UsersClientFactory.getClient().getUser(userId, password);
        if (!result.isOK()) {
            return Result.error(result.error());
        }

        if (!userFiles.containsKey(userId)) {
            return Result.ok(new ArrayList<>());
        }
        var files = userFiles.get(userId).values();
        List<FileInfo> res = new ArrayList<FileInfo>(); 

        if (files.size() == 0) {
            return Result.ok(res);
        }
            
        
        for (FileInfo fInfo : files)
            res.add(fInfo);
        
        return Result.ok(res);
    }

    
}
