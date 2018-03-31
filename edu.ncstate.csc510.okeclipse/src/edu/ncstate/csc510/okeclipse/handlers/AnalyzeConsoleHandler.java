package edu.ncstate.csc510.okeclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import edu.ncstate.csc510.okeclipse.builder.SOAnswerBuilder;
import edu.ncstate.csc510.okeclipse.common.ILoudConsole;

/**
 * 
 * @author Shrikanth N C
 * Invoked for Loud console command
 */
public class AnalyzeConsoleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		SOAnswerBuilder response = new SOAnswerBuilder();

		ILoudConsole console = new LoudConsoleImpl();

		try {
			response.build(console.extract(console.getConsoleContent()));
		} catch (Exception e) {
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(activeShell, "Ok Eclipse - Analyze console", "Empty Console : " + e.getMessage());

		}

		return null;
	}

}
