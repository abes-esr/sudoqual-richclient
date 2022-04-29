package fr.abes.qualinka.eclipse.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import fr.abes.qualinka.eclipse.QualinkaProject;

public class QualinkaNature implements IProjectNature {

	/**
	 * ID of this project nature
	 */
	public static final String NATURE_ID = "fr.abes.qualinka.eclipse.nature.QualinkaNature";

	private IProject platformProject;
	private QualinkaProject project;

	public QualinkaNature() {
		super();
	}

	@Override
	public void configure() throws CoreException {
		this.project.configure();
	}

	@Override
	public void deconfigure() throws CoreException {
		this.project.deconfigure();
		this.platformProject = null;
		this.project = null;
	}

	@Override
	public IProject getProject() {
		return platformProject;
	}

	@Override
	public void setProject(IProject project) {
		this.platformProject = project;
		this.project = QualinkaProject.from(project);
	}

}
