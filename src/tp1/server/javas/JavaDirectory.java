package tp1.server.javas;

import java.io.File;
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
import tp1.clients.RestFilesClient;
import tp1.clients.RestUsersClient;
import tp1.server.Discovery;

public class JavaDirectory implements Directory{
    
    private static final Logger Log = Logger.getLogger(JavaDirectory.class.getName());
    private static Map<String, Map<String, FileInfo>> userFiles = new HashMap<>();

    public JavaDirectory() {}

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        Log.info("Writing " + filename + " of user " + userId + "...");

        URI[] filesUris = null;
    
        Discovery discovery = Discovery.getInstance();

        String fileId = String.format("%s-%s", filename, userId);

        try {

            var usersUri = discovery.knownUrisOf("users");
            while(usersUri == null)
                usersUri = discovery.knownUrisOf("users");
            
            var user = ((new RestUsersClient(usersUri[0])).getUser(userId, password));

            if (!user.isOK())
                return Result.error(user.error());

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        URI fileUri = null;

        try {

            filesUris = discovery.knownUrisOf("files");
            while(filesUris == null)
                filesUris = discovery.knownUrisOf("files");

            // posteriormente definir o server de files
            fileUri = filesUris[0];
            RestFilesClient fileClient = new RestFilesClient(fileUri);
            fileClient.writeFile(fileId, data, "token");
            
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        if (!userFiles.containsKey(userId))
            userFiles.put(userId, new HashMap<String, FileInfo>());
        

        String path = String.format("%s%s/%s", fileUri, RestFiles.PATH, fileId);
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

        URI[] filesUris = null;
    
        Discovery discovery = Discovery.getInstance();

        String fileId = String.format("%s-%s", filename, userId);



        try {

            var usersUri = discovery.knownUrisOf("users");
            while(usersUri == null)
                usersUri = discovery.knownUrisOf("users");
            
            var user = ((new RestUsersClient(usersUri[0])).getUser(userId, password));

            if (!user.isOK())
                return Result.error(user.error());

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }


        try {

            filesUris = discovery.knownUrisOf("files");
            while(filesUris == null)
                filesUris = discovery.knownUrisOf("files");

            // posteriormente definir o server de files
            Result<Void> file = null;
            for(URI uri: filesUris)
                if (file == null)
                    file = (new RestFilesClient(uri)).deleteFile(fileId, "token");
            if (!file.isOK())
                return Result.error(file.error());

        } catch (URISyntaxException e) {
            e.printStackTrace();
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

        URI[] filesUris = null;
    
        Discovery discovery = Discovery.getInstance();

        String fileId = String.format("%s-%s", filename, userId);


        try {

            var usersUri = discovery.knownUrisOf("users");
            while(usersUri == null)
                usersUri = discovery.knownUrisOf("users");
            
            var user = (new RestUsersClient(usersUri[0]).getUser(userId, password));
            var userShare = (new RestUsersClient(usersUri[0])).getUser(userIdShare, "");
            
            if(userShare.error() != Result.ErrorCode.FORBIDDEN)
                return Result.error(Result.ErrorCode.NOT_FOUND);

            if (!user.isOK())
                return Result.error(user.error());

            
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        try {

            filesUris = discovery.knownUrisOf("files");
            while(filesUris == null)
                filesUris = discovery.knownUrisOf("files");

            // posteriormente definir o server de files
            Result<byte[]> file = null;
            for(URI uri: filesUris)
                if (file == null)
                    file = (new RestFilesClient(uri)).getFile(fileId, "token");
            if (!file.isOK())
                return Result.error(file.error());

        } catch (URISyntaxException e) {
            e.printStackTrace();
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

        URI[] filesUris = null;
    
        Discovery discovery = Discovery.getInstance();

        String fileId = String.format("%s-%s", filename, userId);


        try {

            var usersUri = discovery.knownUrisOf("users");
            while(usersUri == null)
                usersUri = discovery.knownUrisOf("users");
            
            var user = (new RestUsersClient(usersUri[0]).getUser(userId, password));

            if (!user.isOK())
                return Result.error(user.error());

            
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        try {

            filesUris = discovery.knownUrisOf("files");
            while(filesUris == null)
                filesUris = discovery.knownUrisOf("files");

            // posteriormente definir o server de files
            Result<byte[]> file = null;
            for(URI uri: filesUris)
                if (file == null)
                    file = (new RestFilesClient(uri)).getFile(fileId, "token");
            if (!file.isOK())
                return Result.error(file.error());

        } catch (URISyntaxException e) {
            e.printStackTrace();
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

        Discovery discovery = Discovery.getInstance();
        
        try {

            var usersUri = discovery.knownUrisOf("users");
            while(usersUri == null)
                usersUri = discovery.knownUrisOf("users");
            
            var result = (new RestUsersClient(usersUri[0])).getUser(accUserId, password);

            if (!result.isOK())
                return Result.error(result.error());


        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        
        FileInfo fileInfo = userFiles.get(userId).get(filename);
        
        if (fileInfo == null)
            return Result.error(ErrorCode.NOT_FOUND);

        Set<String> shared = fileInfo.getSharedWith();
        
        if(!shared.contains(accUserId) && !fileInfo.getOwner().equals(accUserId))
            return Result.error(ErrorCode.FORBIDDEN);

        var r = Response.temporaryRedirect(URI.create(fileInfo.getFileURL())).build();
        if (r != null)
            throw new WebApplicationException(r);
        return Result.ok();
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        Log.info("List files from user " + userId + "...");

        Discovery discovery = Discovery.getInstance();
            
        try {

            var usersUri = discovery.knownUrisOf("users");
            while(usersUri == null)
                usersUri = discovery.knownUrisOf("users");
            
            var result = (new RestUsersClient(usersUri[0])).getUser(userId, password);

            if (!result.isOK()) {
                return Result.error(result.error());
            }
                


        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        if (!userFiles.containsKey(userId)) {
            return Result.ok(new ArrayList<>());
        }
        var files = userFiles.get(userId).values();
        List<FileInfo> result = new ArrayList<FileInfo>(); 

        if (files.size() == 0) {
            Log.info("O " + userId + " não tem files\n\n\n");
            return Result.ok(result);
        }
            
        
        for (FileInfo fInfo : files)
            result.add(fInfo);
        
        return Result.ok(result);
    }

    
}
