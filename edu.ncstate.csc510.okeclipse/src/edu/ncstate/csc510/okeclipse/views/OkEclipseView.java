package edu.ncstate.csc510.okeclipse.views;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.ncstate.csc510.okeclipse.builder.CommandsBuilder;
import edu.ncstate.csc510.okeclipse.model.OECommand;
import edu.ncstate.csc510.okeclipse.resources.Resources;
import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 * 
 * @author Charan
 */

public class OkEclipseView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "edu.ncstate.csc510.okeclipse.views.OkEclipseView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action action1;
	// private Action action2;
	private Action doubleClickAction;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}

	}

	@Override
	public void createPartControl(Composite parent) {

//		parent.setLayout(new RowLayout());
//
//		Composite top = new Composite(parent, SWT.NONE);
//
//		Label label = new Label(top, SWT.LEFT);
//		label.setText("Ok eclipse log : " + Util.getLogFile());
//
//		top.setLayout(new RowLayout());

		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new OECommandContentProvider());
		viewer.setLabelProvider(new OECommandLabelProvider(parent, viewer));

		Table table = viewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Add the First column : Command
		TableColumn tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Command");
		tc.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_DEF_VIEW)
				.createImage());

		// Add the Second column : Command ID
		tc = new TableColumn(table, SWT.LEFT);
		tc.setText("Command ID");
		tc.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD)
				.createImage());

		viewer.setInput(CommandsBuilder.getCommands());

		// Pack the columns
		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack();
		}

		// Turn on the header and the lines
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Create the help context id for the viewer's control
		// workbench.getHelpSystem().setHelp(viewer.getControl(),
		// "edu.ncstate.csc510.okeclipse.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		// hookContextMenu();
		// hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				OkEclipseView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		// manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		// manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				try {
					Desktop.getDesktop().open(CommandsBuilder.getCommandsfile());
				} catch (IOException e) {
					e.printStackTrace();
					Util.showError(e, "Unable to open commands editor.");
				}

			}
		};
		action1.setText("Edit");
		action1.setToolTipText("Open file to edit commands!");
		action1.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UP));

		// action2 = new Action() {
		// public void run() {
		// showMessage("Action 2 executed");
		// }
		// };
		// action2.setText("Action 2");
		// action2.setToolTipText("Action 2 tooltip");
		// action2.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Ok Eclipse View", message);
	}

	@Override
	public void setFocus() {
		// viewer.getControl().setFocus();
	}
}

/**
 * This class contains constants for the OECOmmandsTable application
 */

class ColumnConst {
	// Column variable constants
	public static final int COLUMN_COMMAND_NAME = 0;

	public static final int COLUMN_COMMAND_ID = 1;

}

/**
 * This class provides the labels for OECommandsTable
 */

class OECommandLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

	private Composite parent;
	private TableViewer viewer;

	// Constructs a OECommandLabelProvider
	public OECommandLabelProvider(Composite parent, TableViewer viewer) {
		this.parent = parent;
		this.viewer = viewer;
	}

	@Override
	public Image getImage(Object element) {
		return new Image(parent.getDisplay(), Resources.class.getResourceAsStream("sample@2x.png"));
	}

	@Override
	public Font getFont(Object element) {

		ArrayList list = (ArrayList) viewer.getInput();
		int index = list.indexOf(element);
		if ((index % 2) == 0) {
			return new Font(parent.getDisplay(), "Arial", 10, SWT.ITALIC);
		} else {
			return new Font(parent.getDisplay(), "Arial", 10, SWT.BOLD);
		}

	}

	public Color getBackground(Object element) {
		ArrayList list = (ArrayList) viewer.getInput();
		int index = list.indexOf(element);
		if ((index % 2) == 0) {
			return parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
		} else {
			return null;
		}
	}

	/**
	 * Gets the text for the specified column
	 * 
	 * @param arg0
	 *            the OECommand
	 * @param arg1
	 *            the column
	 * @return String
	 */
	public String getColumnText(Object arg0, int arg1) {
		OECommand cmnd = (OECommand) arg0;
		String text = "";
		switch (arg1) {
		case ColumnConst.COLUMN_COMMAND_NAME:
			text = cmnd.getName();
			break;
		case ColumnConst.COLUMN_COMMAND_ID:
			text = cmnd.getId();
			break;
		}
		return text;
	}

	/**
	 * Adds a listener
	 * 
	 * @param arg0
	 *            the listener
	 */
	public void addListener(ILabelProviderListener arg0) {
		// Throw it away
	}

	/**
	 * Dispose any created resources
	 */
	public void dispose() {

	}

	/**
	 * Returns whether the specified property, if changed, would affect the label
	 * 
	 * @param arg0
	 *            the OECommand
	 * @param arg1
	 *            the property
	 * @return boolean
	 */
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	/**
	 * Removes the specified listener
	 * 
	 * @param arg0
	 *            the listener
	 */
	public void removeListener(ILabelProviderListener arg0) {
		// Do nothing
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}

/**
 * This class provides the content for the OECommandsTable
 */

class OECommandContentProvider implements IStructuredContentProvider {

	/**
	 * Gets the elements for the table
	 * 
	 * @param arg0
	 *            the model
	 * @return Object[]
	 */
	public Object[] getElements(Object arg0) {
		// Returns all the commands in the specified team
		return CommandsBuilder.getCommands().toArray();
	}

	/**
	 * Disposes any resources
	 */
	public void dispose() {
		// We don't create any resources, so we don't dispose any
	}

	/**
	 * Called when the input changes
	 * 
	 * @param arg0
	 *            the parent viewer
	 * @param arg1
	 *            the old input
	 * @param arg2
	 *            the new input
	 */
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// Nothing to do
	}
}