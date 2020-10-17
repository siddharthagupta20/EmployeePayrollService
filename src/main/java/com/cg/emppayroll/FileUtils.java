package com.cg.emppayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtils {

	public static boolean deleteFiles(File contentToDelete) {
		File[] allContents = contentToDelete.listFiles();
		if (allContents != null) {
			for (File f : allContents) {
				deleteFiles(f);
			}
		}
		return contentToDelete.delete();
	}

	public static File populatiingFile(File folderToFill) {
		Path p = Paths.get(folderToFill.getPath() + "/" + UUID.randomUUID().toString() + ".txt");
		try {
			Files.createFile(p);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return p.toFile();
	}

}
