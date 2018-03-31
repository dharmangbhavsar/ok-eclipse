package edu.ncstate.csc510.okeclipse.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ncstate.csc510.okeclipse.model.OECommand;
import edu.ncstate.csc510.okeclipse.resources.Resources;
import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * 
 * @author Shrikanth N C
 * Builds the custom dictionary
 */
public class CustomDictionaryBuilder {


	private Map<String, String> fullDictionary = new HashMap<>();
	private Map<String, String> customDictionary = new HashMap<>();

	public Map<String, String> build() throws IOException {

		loadFullDictionary();

		// CommandManager commandManager =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		// .getService(CommandManager.class);

		// CommandManager commandManager =
		// PlatformUI.getWorkbench().getService(CommandManager.class);

		List<OECommand> commands = CommandsBuilder.getCommands();

		for (OECommand c : commands) { 

			String name = c.getName();

			addEntry(name);

		} 

		return customDictionary;

	}

	private void loadFullDictionary() throws IOException {

		InputStream inputStream = Resources.class.getResourceAsStream("dict.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] entry = line.split(" ");
				fullDictionary.put(entry[0], line.substring(line.indexOf(entry[0])));
			}
		}
	}

	private void addEntry(String name) {

		String[] keywords = name.split(" ");

		for (String key : keywords) {

			if (!Util.isNullString(key) && !Util.isNullString(fullDictionary.get(key))) {
				customDictionary.put(key, fullDictionary.get(key));
			}
		}

	}

}
