package tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Test;

import main.Main;

public class MapGeneratorMainTests {

	@Test
	public void mainTest1() {
		String[] test = { "a" };
		assertThrows(Exception.class, ()-> Main.runFromCommandLine(test));
	}
	@Test
	public void mainTest2() {
		String[] test = { "0","0","100","100" };
		assertThrows(Exception.class, ()-> Main.runFromCommandLine(test));
	}
	@Test
	public void mainTest3() {
		String[] test = { "200", "300", "400", "500", "60000" };
		assertThrows(Exception.class, ()-> Main.runFromCommandLine(test));
	}
	@Test
	public void mainTest4() {
		String[] test = { "200", "300", "g", "500", "a"  };
		assertThrows(Exception.class, ()-> Main.runFromCommandLine(test));
	}
	@Test
	public void mainTest5() {
		String[] test = { "-1", "100", "40", "60", "100"  };
		assertThrows(Exception.class, ()-> Main.runFromCommandLine(test));
	}
	@Test
	public void mainTest6() {
		String[] test = { "100", "100", "40", "60" };
		assertDoesNotThrow(()-> Main.runFromCommandLine(test));
	}
	
	@AfterClass
	public static void removeResources() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		File folder = new File(s);
		File fList[] = folder.listFiles();
		for (int i = 0; i < fList.length; i++) {
		    String pes = fList[i].toString();
		    if (pes.endsWith(".json") || pes.endsWith(".jpg")) {
		        boolean success = (new File(pes).delete());
		    }
		}
	}
}
