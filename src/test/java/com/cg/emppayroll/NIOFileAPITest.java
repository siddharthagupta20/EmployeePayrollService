package com.cg.emppayroll;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class NIOFileAPITest {
	
	
	private static final String HOME=System.getProperty("user.dir");
	private static final String SPECIFIC="demo";
	@Test
	public void givenPath_whenExists_performsActions() throws IOException {
		
		Path p=Paths.get(HOME+"/"+SPECIFIC);
		assertTrue(Files.exists(p));
		
		FileUtils.deleteFiles(p.toFile());
		assertFalse(Files.exists(p));
		
		Files.createDirectory(p);
		assertTrue(Files.exists(p));
		
		for(int i=0;i<10;i++) {
			FileUtils.populatiingFile(p.toFile());
		}
		assertTrue(Files.exists(p));
		
		Files.list(p).filter(Files::isRegularFile).forEach(System.out::println);
		System.out.println("---------");
		Files.newDirectoryStream(p).forEach(System.out::println);
		System.out.println("---------");
		Files.newDirectoryStream(p,p1->p1.toFile().isFile()&&p1.toString().contains("0")).forEach(System.out::println);
		
		
	
	}

}
