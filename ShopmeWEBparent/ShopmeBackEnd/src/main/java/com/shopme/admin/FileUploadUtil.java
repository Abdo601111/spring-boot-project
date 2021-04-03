package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(String fileDir,String fileName,MultipartFile multiPartFile) throws IOException {
		
		Path fileUpload = Paths.get(fileDir);
		if(!Files.exists(fileUpload)) {
			Files.createDirectory(fileUpload);
		}
		try(InputStream inputStream =multiPartFile.getInputStream()){
			Path filePath = fileUpload.resolve(fileName);
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException ex) {
			throw new IOException("Could Not Save File"+fileName,ex);
			
		}
		
	}
	
	
	public static void cleanDir(String dir) {
		Path pathdir = Paths.get(dir);
		
		try {
			Files.list(pathdir).forEach(file ->{
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
						
					}catch(IOException ex) {
						System.out.println("Could Not Delete" +file);
						
					}
				}
			});
			
		}catch(IOException ex) {
			System.out.println("Could Not Save File"+pathdir);
			
		}
		
	}
	
	public static void removeDir(String dir) {
		cleanDir(dir);
		
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + dir);
		}
		
	}

	
}
