
package edu.ncstate.csc510.okeclipse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

import edu.ncstate.csc510.okeclipse.common.VoiceRecognizer;
import edu.ncstate.csc510.okeclipse.resources.Resources;
import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * 
 * @author Shrikanth N C 
 * Class that Initializes speech recognition on eclipse
 *         startup
 *
 */
public class OkEclipseStartup implements IStartup {

	/**
	 * Customized progress monitor
	 *
	 */
	class CustomProgressMonitor extends ProgressMonitorDialog {

		public CustomProgressMonitor(Shell parent) {
			super(parent);
		}

		@Override
		protected Image getImage() {
			return new Image(super.getShell().getDisplay(), Resources.class.getResourceAsStream("sample@6x.png"));
		}

		@Override
		public Image getInfoImage() {
			return new Image(super.getShell().getDisplay(), Resources.class.getResourceAsStream("sample@6x.png"));

		}
	}

	@Override
	public void earlyStartup() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			private boolean success = false;

			public void run() {
				Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				ProgressMonitorDialog dialog = new CustomProgressMonitor(activeShell);

				try {
					dialog.run(false, false, new IRunnableWithProgress() {

						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {

							monitor.beginTask("Ok Eclipse : Initializing speech recognition..", 3);

							try {
								VoiceRecognizer.start(monitor);
								monitor.done();
								success = true;
							} catch (IOException e) {

								e.printStackTrace();
								Util.showError(e, "Error staring voice recoginizer");
							}

						}

					});

					if (success) {
						MessageDialog.openConfirm(activeShell, "Ok Eclipse",
								"'shift+z' and recite your request. See commands in Ok Eclipse view!");
						Util.showOkEclipseView();
					}

				} catch (InvocationTargetException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

}