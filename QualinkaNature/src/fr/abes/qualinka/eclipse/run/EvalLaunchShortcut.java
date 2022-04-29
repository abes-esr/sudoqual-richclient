package fr.abes.qualinka.eclipse.run;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.statushandlers.StatusManager;

import fr.abes.qualinka.eclipse.QualinkaPlugin;
import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.QualinkaUtils;
import fr.abes.qualinka.eclipse.wizards.util.RunJava;

public class EvalLaunchShortcut extends QualinkaLaunchShortcut {

	@Override
	public void run(String projectName, Object[] resources, String mode) {
		StringBuilder sb = new StringBuilder();
		for (Object o : resources) {
			if (o instanceof IResource) {
				sb.append(((IResource) o).getLocation());
				sb.append(" ");
			} else {
				QualinkaUtils.handleError("One of selected item is nor a file, nor a directory.");
				return;
			}
		}
	}

	@Override
	public void run(String projectName, IResource resource, String mode) {
		QualinkaProject project = QualinkaProject.from(resource.getProject());
		this.run(projectName, project.getMainClass(), resource.getLocation().toOSString(), mode);
	}

	private void run(String projectName, String mainClass, String filepaths, String mode) {
		try {
			RunJava.run(projectName, mainClass, mode, "-v eval " + filepaths, null);

		} catch (CoreException e) {
			StatusManager.getManager().handle(e, QualinkaPlugin.PLUGIN_ID);
		}
	}

}
