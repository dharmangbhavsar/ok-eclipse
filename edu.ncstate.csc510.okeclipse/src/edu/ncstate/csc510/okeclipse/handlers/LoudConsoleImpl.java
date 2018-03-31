package edu.ncstate.csc510.okeclipse.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.TextConsole;

import edu.ncstate.csc510.okeclipse.common.ILoudConsole;

/**
 * 
 * Loud console Implementation
 *
 */
public class LoudConsoleImpl implements ILoudConsole {
	public static void main(String[] args) {

		LoudConsoleImpl consoleImpl = new LoudConsoleImpl();
		List<String> output = consoleImpl.extract("java.lang.ASDERror, java.lang.NasdaException");
		//
		System.out.println(output);
	}

	/**
	 * @author kashy
	 */
	@Override
	public List<String> extract(String consoleLog) {
		ArrayList<String> result = new ArrayList<String>();
		String searchTerm_Except = "Exception";
		String searchTerm_Error = "error";
		String text = consoleLog;
		String sPattern_Except = "(?i)\\b\\w*" + Pattern.quote(searchTerm_Except) + "\\w*\\b";
		String sPattern_Error = "(?i)\\b\\w*" + Pattern.quote(searchTerm_Error) + "\\w*\\b";
		Pattern pattern_Except = Pattern.compile(sPattern_Except);
		Pattern pattern_Error = Pattern.compile(sPattern_Error);
		Matcher matcher_Except = pattern_Except.matcher(text);
		while (matcher_Except.find()) {
			result.add("java.lang." + matcher_Except.group());
		}
		Matcher matcher_Error = pattern_Error.matcher(text);
		while (matcher_Error.find()) {
			result.add("java.lang." + matcher_Error.group());
		}
		Set<String> hs = new HashSet<>();
		hs.addAll(result);
		result.clear();
		result.addAll(hs);
		return result;
	}

	@Override
	public String getConsoleContent() {
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();

		IConsole[] consoles = manager.getConsoles();

		for (IConsole console : consoles) {
			if (console instanceof TextConsole) {
				IDocument doc = ((TextConsole) console).getDocument();
				return doc.get();
			}
		}
		return null;
	}
}
