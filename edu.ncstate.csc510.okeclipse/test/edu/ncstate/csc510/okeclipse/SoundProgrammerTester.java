
	//@Test
	//public void testGenerateMainMethod() {
	package edu.ncstate.csc510.okeclipse;
	import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.ncstate.csc510.okeclipse.common.ISoundProgrammer;
import edu.ncstate.csc510.okeclipse.handlers.SoundProgrammerImpl;

	public class SoundProgrammerTester {

//Checking for a change
		@Test
		//Function is called to test the extract function
		public void testExtract() {
			ISoundProgrammer soundprogram = new SoundProgrammerImpl();
			String exp_output = new String("public class Apple {\r\n" +
					"\r\n" +
					"	private int seeds;\r\n" +
					"	private String color;\r\n" +
					"	\r\n" +
					"	public int getSeeds() {\r\n" +
					"		return seeds;\r\n" +
					"	}\r\n" +
					"	public void setSeeds(int seeds) {\r\n" +
					"		this.seeds = seeds;\r\n" +
					"	}\r\n" +
					"	public String getColor() {\r\n" +
					"		return color;\r\n" +
					"	}\r\n" +
					"	public void setColor(String color) {\r\n" +
					"		this.color = color;\r\n" +
					"	}\r\n" +
					"\r\n" +
					"}");
			String output = soundprogram.generateGetterSetter("public class Apple {\r\n" +
					"\r\n" +
					"	private int seeds;\r\n" +
					"	private String color;\r\n" +
					"\r\n" +
					"}\r\n" +
					"");
			List<String> bothList1= Arrays.asList(output.split("\\s+"));
			List<String> bothList2= Arrays.asList(exp_output.split("\\s+"));

			System.out.println(bothList1);
				System.out.println(bothList2);
				if (bothList1.equals(bothList2))
					System.out.println("Test PASS");
				else
				System.out.println("Test FAIL");

		}

	@Test
	public void main() {
		SoundProgrammerTester LCTest = new SoundProgrammerTester();
		LCTest.testExtract();
		LCTest.testImplementInterface();
	}

	@Test
	//Test function is called to test implement interface
	public void testImplementInterface() {
		ISoundProgrammer soundprogram = new SoundProgrammerImpl();
		String exp_output = new String("public class Car implements Vehicle {\r\n" +
				"	\r\n" +
				"	public void start(){ }\r\n" +
				"	\r\n" +
				"	public boolean stop(){ return false; }\r\n" +
				"\r\n" +
				"}");
		String output = soundprogram.implementInterface("public class Car {\r\n" +
				"	\r\n" +
				"\r\n" +
				"}\r\n" +
				"","public interface Vehicle {\r\n" +
				"\r\n" +
				"	public void start();\r\n" +
				"	\r\n" +
				"	public boolean stop();\r\n" +
				"\r\n" +
				"} ");
		List<String> bothList1= Arrays.asList(output.split("\\s+"));
		List<String> bothList2= Arrays.asList(exp_output.split("\\s+"));
		System.out.println(bothList1);
		System.out.println(bothList2);
		if (bothList1.equals(bothList2))
		{
			System.out.println("Test Case Pass");
		}
		else
			System.out.println("Test Case fail");

	}
	}
