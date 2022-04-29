package fr.abes.qualinka.eclipse.run;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import fr.abes.qualinka.eclipse.QualinkaUtils;
import fr.abes.qualinka.eclipse.wizards.util.RunJava;

abstract class QualinkaLaunchShortcut implements ILaunchShortcut {

	protected abstract void run(String projectName, Object[] resources, String mode);

	protected abstract void run(String projectName, IResource resource, String mode);

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = input.getAdapter(IFile.class);
		if (file != null) {
			RunJava.build(file.getProject(), null);
			run(file.getProject().getName(), file, mode);
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
			if (object instanceof IResource) {
				Object[] resources = treeSelection.toArray();
				RunJava.build(((IResource) object).getProject(), null);
				if (resources.length == 1 && resources[0] instanceof IResource) {
					run(((IResource) object).getProject().getName(), (IResource) resources[0], mode);
				} else {
					run(((IResource) object).getProject().getName(), resources, mode);
				}
			} else {
				QualinkaUtils.handleError("Selection not relevant");
				return;
			}
		}
	}

}