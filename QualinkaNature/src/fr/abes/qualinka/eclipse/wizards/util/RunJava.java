package fr.abes.qualinka.eclipse.wizards.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import fr.abes.qualinka.eclipse.QualinkaUtils;

public final class RunJava {

	private RunJava() {
	}

	public static void build(IProject project, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
		subMonitor.setTaskName("refresh & build project");

		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, subMonitor.split(1));
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, subMonitor.split(3));
		} catch (CoreException e) {
			QualinkaUtils.handleError("Error during project building", e);
		}
	}

	/**
	 *
	 * @param projectName
	 * @param mode
	 *                        "run" or "debug"
	 * @param args
	 *                        java arguments
	 * @throws CoreException
	 */
	public static void run(String projectName, String mainClass, String mode, String args, IProgressMonitor monitor)
	    throws CoreException {
		assert projectName != null && !projectName.isBlank();
		assert args != null;
		if (projectName == null || projectName.isBlank() || args == null || mainClass == null || mode == null) {
			throw new IllegalArgumentException("Parameters must be not null");
		}
		SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
		subMonitor.setTaskName("run Java class: " + mainClass + " with args:" + args);

		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(
		    IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy wc = type.newInstance(null, mainClass);
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainClass);
		if (args != null) {
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, args);
			subMonitor.worked(1);
			wc.launch(mode, subMonitor.split(3), true);
		}
	}
}
