package edu.ncstate.csc510.okeclipse.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import edu.ncstate.csc510.okeclipse.builder.CommandsBuilder;
import edu.ncstate.csc510.okeclipse.common.VoiceRecognizer;
import edu.ncstate.csc510.okeclipse.model.OECommand;
import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * 
 * @author Shrikanth N C Main class that processes the speech request
 */
public class SpeechHandler extends AbstractHandler {

	private static String spokenTextFinal = "";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(activeShell);

			dialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Speak now", 3);

					SpeechResult result;
					String spokenText = "";
					while ((result = VoiceRecognizer.getRecognizer().getResult()) != null) {

						for (WordResult r : result.getWords()) {
							spokenText += r.getWord() + " ";
						}

						if (spokenText != null && spokenText.length() > 0) {
							break;
						}

					}

					spokenText = clean(spokenText);

					spokenTextFinal = spokenText;

					monitor.setTaskName("You've asked for " + spokenText);

					monitor.worked(1);
					monitor.done();
				}

			});

		}

		catch (Exception e) {

			MessageDialog.openError(activeShell, "Ok Eclipse", "Error while hearing   your request " + e.getMessage());
			e.printStackTrace();

		}

		processSpokenText(window);

		return null;
	}

	private void processSpokenText(IWorkbenchWindow window) {
		String cmdResult = "";
		try {

			cmdResult = executeCommand(spokenTextFinal, window);
		} catch (Exception e) {
			cmdResult = "NOT_APPLICABLE";
			Util.showError(e, "Error while executing your request");
			e.printStackTrace();

		}
		Util.appendToLog("" + spokenTextFinal + "," + cmdResult);
	}

	private String clean(String spokenText) {

		if (spokenText != null) {

			spokenText = spokenText.replace("<sil>", "");
			spokenText = spokenText.replace("[SPEECH]", "");
			spokenText = spokenText.replace("[NOISE]", "");
			spokenText = spokenText.replace("[noise]", "");
		}

		return spokenText.trim().toLowerCase();
	}

	private String executeCommand(String spokenText, IWorkbenchWindow window)
			throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException {

		String commandId = findCommand(spokenText);

		if (Util.isNullString(commandId)) {

			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openInformation(activeShell, "Ok Eclipse", "Couldn't catch that! " + spokenText
					+ "', Feel free to add/customize commands at " + CommandsBuilder.getCommandsfile() + ".");
			return "NOT_RECOGNIZED";
		}

		try {
			IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
			handlerService.executeCommand(commandId, null);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return "SUCCESS";

	}

	private String findCommand(String spokenText) {

		List<OECommand> commands = CommandsBuilder.getCommands();

		for (OECommand command : commands) {

			if (spokenText.contains(command.getName())) {
				return command.getId();
			}
		}

		return null;
	}

}
