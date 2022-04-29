package fr.abes.qualinka.eclipse.debug;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.QualinkaUtils;
import fr.abes.qualinka.eclipse.wizards.util.RunJava;

public abstract class QualinkaElementLaunchShortcut<T extends Dialog> implements ILaunchShortcut {

	protected abstract T createInputDialog(Object o, QualinkaProject project);

	protected abstract String getArgs(Object o, T dialog);

	protected abstract IStatus checkObject(Object o);

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = input.getAdapter(IFile.class);
		if (file != null) {
			QualinkaUtils.runWithProgress(false, new Launcher(file, mode));
		} else {
			QualinkaUtils.handleError("Error while launching Qualinka: file associated to editor not found.");
			return;
		}
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof ITreeSelection) {
			ITreeSelection treeSelection = (ITreeSelection) selection;
			Object object = treeSelection.getFirstElement();
			if (object instanceof IJavaElement) {
				// in case where JDT navigatorContent is activated
				try {
					object = ((IJavaElement) object).getCorrespondingResource();
				} catch (JavaModelException e) {
					QualinkaUtils.handleError("Error when trying to retrive corresponding file to a Java element", e);
				}
			}

			if (object instanceof IFile) {
				QualinkaUtils.runWithProgress(false, new Launcher((IFile) object, mode));
			} else {
				QualinkaUtils.handleError("Selected element was not recognized as a Java File");
			}
		} else {
			QualinkaUtils.handleError("Selected elements cannot be processed");
		}
	}

	private class Launcher implements IRunnableWithProgress {
		private IFile file;
		private String mode;

		public Launcher(IFile file, String mode) {
			this.file = file;
			this.mode = mode;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
			subMonitor.setTaskName("run Qualinka Element");

			IProject platformProject = file.getProject();
			RunJava.build(platformProject, subMonitor.split(3));
			QualinkaProject project = QualinkaProject.from(platformProject);
			Object o;
			try {
				o = project.createInstance(QualinkaUtils.fileToJavaPath(file));
				subMonitor.worked(1);
			} catch (Exception e) {
				QualinkaUtils.handleError("An error occured during the class instanciation.", e);
				return;
			}
			if (o == null) {
				QualinkaUtils.handleError("An error occured during the class instanciation.");
				return;
			}
			IStatus status = checkObject(o);
			subMonitor.worked(1);
			if (!status.isOK()) {
				QualinkaUtils.handleError(status.getMessage());
				return;
			}
			T dialog = createInputDialog(o, project);

			subMonitor.setTaskName("Waiting for dialog validation");
			if (dialog.open() == Window.OK) {
				subMonitor.worked(1);
				String args = getArgs(o, dialog);
				try {
					RunJava.run(project.getName(), project.getMainClass(), mode, args, subMonitor.split(4));
				} catch (CoreException e) {
					QualinkaUtils.handleError("Error while launching Qualinka", e);
				}
			}
		}

	}

}
