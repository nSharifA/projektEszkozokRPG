package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
