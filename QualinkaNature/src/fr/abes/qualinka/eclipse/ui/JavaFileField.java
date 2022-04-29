package fr.abes.qualinka.eclipse.ui;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

import fr.abes.qualinka.eclipse.QualinkaUtils;

public class JavaFileField extends TextField {

	private JavaFileFieldSelectionAdapter selectionListener;

	public JavaFileField(IPage page, String label, String value, IContainer initialRoot) {
		super(page, label, value);
		this.selectionListener.setRoot((initialRoot != null) ? initialRoot : ResourcesPlugin.getWorkspace().getRoot());
	}

	@Override
	protected int hookAfterInit() {
		Button button = new Button(this.getPage().getParentContainer(), SWT.PUSH);
		button.setText("Browse...");
		this.selectionListener = new JavaFileFieldSelectionAdapter(this.getPage().getShell(), this);
		button.addSelectionListener(this.selectionListener);

		return 1;
	}

	private static class JavaFileFieldSelectionAdapter extends SelectionAdapter {

		private Shell shell;
		private IContainer root;
		private TextField field;

		public JavaFileFieldSelectionAdapter(Shell shell, TextField field) {
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
			SelectionDialog dialog = new FilteredResourcesSelectionDialog(shell, false, root, IResource.FILE);
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1 && result[0] instanceof IFile) {
					String javaPath = QualinkaUtils.fileToJavaPath((IFile) result[0]);
					this.field.setValue(javaPath);
				} else {
					QualinkaUtils.handleError("Please select a Java file containing a main method.");
				}
			}
		}
	}
}
