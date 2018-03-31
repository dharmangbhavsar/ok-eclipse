package edu.ncstate.csc510.okeclipse.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.ncstate.csc510.okeclipse.views.OkEclipseView;

/**
 * 
 * @author Shrikanth N C 
 * Frequently used reusable methods dumped here
 */
public class Util {

	public static String getPrefixPath() {

		String prefixPath = System.getProperty("user.dir") + File.separator + "ok_eclipse" + File.separator;

		File folder = new File(prefixPath);

		if (!folder.exists()) {
			folder.mkdirs();
		}

		return prefixPath;
	}

	public static Command[] getEclipseCommands() {

		CommandManager commandManager = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(CommandManager.class);

		Command[] commands = commandManager.getAllCommands();

		return commands;
	}

	public static boolean isNullString(String content) {

		return content == null || content.trim().length() <= 0 || content.trim().toLowerCase().equals("null");
	}

	public static IDocument getCurrentEditorContent() {
		final IEditorPart editor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		IDocumentProvider dp = ((ITextEditor) editor).getDocumentProvider();
		IDocument doc = dp.getDocument(editor.getEditorInput());
		return doc;
	}

	public static void showOkEclipseView() {
		try {

			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(OkEclipseView.ID));
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(OkEclipseView.ID);

		} catch (PartInitException e) {
		}
	}

	public static void showError(Exception e, String message) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				MessageDialog.openError(activeShell, "Ok Eclipse", message + e.getMessage());
			}
		});
	}

	private static File logFile = new File(Util.getPrefixPath() + "okeclipselog.csv");

	public static File getLogFile() {
		return logFile;
	}

	public static void appendToLog(String entry) {

		String log = "User" + "," + entry + "," + new Date(System.currentTimeMillis()) + "\n";

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {

			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			fw = new FileWriter(logFile.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(log);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();
				System.err.println("Unable to write log!");

			}
		}
	}
}
