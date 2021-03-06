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
 * @author Vismay Golwala
 *Invoked for inject code command
 */
public class TestHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISoundProgrammer programmer = new SoundProgrammerImpl();

		try {
			programmer.testCode(Util.getCurrentEditorContent().get());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}
}
