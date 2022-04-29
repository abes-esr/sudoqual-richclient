package fr.abes.qualinka.eclipse.nature;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.abes.qualinka.eclipse.QualinkaUtils;

public class AddRemoveQualinkaNatureHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		//
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = ((IAdaptable) element).getAdapter(IProject.class);
				}
				if (project != null) {
					toggleNature(project);
				}
			}
		}

		return null;
	}

	/**
	 * Toggles sample nature on a project
	 *
	 * @param project
	 *                    to have sample nature added or removed
	 */
	private void toggleNature(IProject project) {
		IProjectDescription description = null;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			QualinkaUtils.handleError(
			    "Error during Qualinka nature activation/desactivation due to error when getting project description.",
			    e);
			return;
		}

		String[] natures = description.getNatureIds();
		String[] newNatures;
		int pos = hasQualinkaNature(natures);
		if (pos >= 0) {
			newNatures = removeNature(natures, pos);
		} else {
			newNatures = addNature(natures);
		}
		description.setNatureIds(newNatures);
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			QualinkaUtils.handleError(
			    "Error during Qualinka nature activation/desactivation due to error when setting project description.",
			    e);
			return;
		}

	}

	/**
	 *
	 * @param natures
	 * @return the position of the qualinkaNature in the specified array, -1 if the
	 *         nature was not found
	 * @throws CoreException
	 */
	private int hasQualinkaNature(String[] natures) {
		for (int i = 0; i < natures.length; ++i) {
			if (QualinkaNature.NATURE_ID.equals(natures[i])) {
				return i;
			}
		}
		return -1;
	}

	private String[] addNature(String[] natures) {
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 1, natures.length);
		newNatures[0] = QualinkaNature.NATURE_ID;
		return newNatures;
	}

	private String[] removeNature(String[] natures, int pos) {
		String[] newNatures = new String[natures.length - 1];
		System.arraycopy(natures, 0, newNatures, 0, pos);
		System.arraycopy(natures, pos + 1, newNatures, pos, natures.length - pos - 1);
		return newNatures;
	}
}