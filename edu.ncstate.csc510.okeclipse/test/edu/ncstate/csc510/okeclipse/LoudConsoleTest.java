package edu.ncstate.csc510.okeclipse;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.ncstate.csc510.okeclipse.common.ILoudConsole;
import edu.ncstate.csc510.okeclipse.handlers.LoudConsoleImpl;

public class LoudConsoleTest {

	@Test
	public void testExtract() {
		ILoudConsole console = new LoudConsoleImpl();

		List<String> extracted = console.extract("xsadlkjasdkljas java.lang.Exception asdlasdlkaksjd ");
		assertEquals("java.lang.Exception", extracted.get(0));
	}

	@Test
	public void testGetConsoleContent() {

			//TODO: Cannot be tested in this mode.
	}

}
