package fr.abes.qualinka.eclipse.run;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.QualinkaUtils;
import fr.abes.qualinka.eclipse.wizards.util.RunJava;

abstract class JSONFileLaunchShortcut extends QualinkaLaunchShortcut {

	private String[] filterExtensions = { "*.json" };

	protected abstract String getArgs();

	@Override
	protected void run(String projectName, Object[] resources, String mode) {
		QualinkaUtils.handleError("Multi-selection not allowed.");
		return;
	}

	@Override
	protected void run(String projectName, IResource resource, String mode) {
		QualinkaProject project = QualinkaProject.from(resource.getProject());

		if (resource.getName().endsWith(".json")) {
			run(projectName, project.getMainClass(), resource.getLocation().toOSString(), mode);
		} else {
			Shell shell = QualinkaUtils.getShell();
			FileDialog dialog = new FileDialog(shell);
			dialog.setFilterExtensions(filterExtensions);
			String file = dialog.open();
			if (file != null && !file.isBlank()) {
				run(projectName, project.getMainClass(), file, mode);
			}
		}
	}

	private void run(String projectName, String mainClass, String filepaths, String mode) {
		try {
			RunJava.run(projectName, mainClass, mode, getArgs() + filepaths, null);

		} catch (CoreException e) {
			QualinkaUtils.handleError("Error when running java.", e);
		}
	}

}
