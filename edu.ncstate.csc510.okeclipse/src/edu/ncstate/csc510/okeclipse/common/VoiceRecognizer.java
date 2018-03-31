package edu.ncstate.csc510.okeclipse.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.ncstate.csc510.okeclipse.builder.CustomDictionaryBuilder;
import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * 
 * @author Shrikanth N C
 * Wrapper to the sphinx library
 */
public class VoiceRecognizer {

	private static LiveSpeechRecognizer recognizer;

	private static final String FILENAME = "okeclipse.dict";

	/**
	 * Loads the sphinx library
	 * @param monitor
	 * @throws IOException
	 */
	public static void start(IProgressMonitor monitor) throws IOException {

		if (isDictionaryNeeded()) {
			monitor.setTaskName("Preparing custom speech dictionary...");
			writeDictionary();

		}

		monitor.worked(1);
		monitor.setTaskName("Initializing configuration..");
		Configuration configuration = new Configuration();
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath(getCustomDictionaryFile().toURI().toURL().toString());
		// configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
		recognizer = new LiveSpeechRecognizer(configuration);
		monitor.worked(2);
		monitor.setTaskName("Enabling recognition (please wait)");
		recognizer.startRecognition(true);
		monitor.worked(3);
	}

	private static void writeDictionary() throws IOException {

		Map<String, String> customDictionary = new CustomDictionaryBuilder().build();

		System.out.println(getCustomDictionaryFile() + " -- ??>>>");
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(getCustomDictionaryFile()), StandardCharsets.UTF_8)))) {

			for (String key : customDictionary.keySet()) {
				{
					out.println(customDictionary.get(key));
				}

			}

		} catch (IOException e) {
			throw e;
		}

	}

	private static File getCustomDictionaryFile() {
		return new File(Util.getPrefixPath() + FILENAME);
	}

	private static boolean isDictionaryNeeded() {
		return true;
	}

	public static LiveSpeechRecognizer getRecognizer() {
		return recognizer;
	}

}
