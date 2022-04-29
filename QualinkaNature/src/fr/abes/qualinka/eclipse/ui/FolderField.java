package fr.abes.qualinka.eclipse.ui;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import fr.abes.qualinka.eclipse.ui.dialog.MyContainerSelectionDialog;

public class FolderField extends TextField {

	private FolderFieldSelectionAdapter selectionListener;

	public FolderField(IPage page, String label, String value, IContainer initialRoot) {
		super(page, label, value);
		this.selectionListener.setRoot((initialRoot != null) ? initialRoot : ResourcesPlugin.getWorkspace().getRoot());
	}

	@Override
	protected int hookAfterInit() {
		Button button = new Button(this.getPage().getParentContainer(), SWT.PUSH);
		button.setText("Browse...");
		this.selectionListener = new FolderFieldSelectionAdapter(this.getPage().getShell(), this);
		button.addSelectionListener(this.selectionListener);

		return 1;
	}

	private static class FolderFieldSelectionAdapter extends SelectionAdapter {

		private Shell shell;
		private IContainer root;
		private TextField field;

		public FolderFieldSelectionAdapter(Shell shell, TextField field) {
			this.shell = shell;
			this.field = field;
		}

		public void setRoot(IContainer initialRoot) {
			this.root = initialRoot;
		}

		/**
		 * Uses the standard container selection dialog to choose the new value for the
		 * text field.
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			MyContainerSelectionDialog dialog = new MyContainerSelectionDialog(shell, root, ((IProject) root).getFolder(
			    "./src/main/java"), "Select new " + field.getLabel().toLowerCase());
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1) {
					IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(((IPath) result[0]));
					if (folder != null) {
						this.field.setValue(folder.getProjectRelativePath().toString());
					}
				}
			}
		}
	}
}
