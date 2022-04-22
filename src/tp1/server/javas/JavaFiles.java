package tp1.server.javas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Map;

import jakarta.inject.Singleton;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;

@Singleton
public class JavaFiles implements Files {

    public static Map<String, byte[]> files = new HashMap<>();

    private static final Logger Log = Logger.getLogger(JavaFiles.class.getName());

    public JavaFiles() {

    }

	@Override
	public Result<Void> writeFile(String fileId, byte[] data, String token) {
		Log.info("writeFile: " + fileId);

        if (fileId == null)
            return Result.error(ErrorCode.BAD_REQUEST); 


        //Does nothing
        File file = new File(fileId);
        
        try (FileOutputStream fOutput = new FileOutputStream(file)) {
			fOutput.write(data);
			fOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        files.put(fileId, data);
		return Result.ok();
	}

	@Override
	public Result<Void> deleteFile(String fileId, String token) {
		if (fileId == null)
            return Result.error(ErrorCode.BAD_REQUEST); 
        if (!files.containsKey(fileId))
            return Result.error(ErrorCode.NOT_FOUND);
        files.remove(fileId);
		return Result.ok();
	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
        if (fileId == null)
            return Result.error(ErrorCode.BAD_REQUEST); 
		if (!files.containsKey(fileId))
            return Result.error(ErrorCode.NOT_FOUND);
		return Result.ok(files.get(fileId));
	}


}