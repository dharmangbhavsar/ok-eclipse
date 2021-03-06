package edu.ncstate.csc510.okeclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import edu.ncstate.csc510.okeclipse.common.ISoundProgrammer;
import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * 
 * @author Tushita R
 *Invoked for Git Commit command
 */
public class GitCommitHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISoundProgrammer programmer = new SoundProgrammerImpl();

		try {
			programmer.gitCommit();
//			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//			IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
//			handlerService.executeCommand("org.eclipse.jdt.ui.edit.text.java.format", null);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}
}
