package fr.abes.qualinka.eclipse.propertyTester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.statushandlers.StatusManager;

import fr.abes.qualinka.eclipse.QualinkaPlugin;
import fr.abes.qualinka.eclipse.QualinkaProject;

public class IContainerPropertyTester extends PropertyTester {

	private static final String PROPERTY_NAME = "containsJson"; //$NON-NLS-1$
	private static final String IS_FEATURE_DIR = "isFeatureDir"; //$NON-NLS-1$
	private static final String IS_CRITERION_DIR = "isCriterionDir"; //$NON-NLS-1$
	private static final String IS_FILTER_DIR = "isFilterDir"; //$NON-NLS-1$

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof IContainer) {
			IContainer container = (IContainer) receiver;
			switch (property) {
				case PROPERTY_NAME:
					return check(container);
				case IS_FILTER_DIR:
					return container.getName().equals(QualinkaProject.FILTERS_DIR_NAME);
				case IS_FEATURE_DIR:
					return container.getName().equals(QualinkaProject.FEATURES_DIR_NAME);
				case IS_CRITERION_DIR:
					return container.getName().equals(QualinkaProject.CRITERIONS_DIR_NAME);
			}
		}
		return false;
	}

	public static boolean check(IContainer container) {
		boolean res = false;
		IResource[] members;
		try {
			members = container.members();
		} catch (CoreException e) {
			StatusManager.getManager().handle(e, QualinkaPlugin.PLUGIN_ID);
			return false;
		}
		for (int i = 0; i < members.length; ++i) {
			res |= check(members[i]);
		}
		return res;
	}

	public static boolean check(IResource resource) {
		if (resource instanceof IContainer) {
			return check((IContainer) resource);
		} else if (resource instanceof IFile) {
			return check((IFile) resource);
		}
		return false;
	}

	public static boolean check(IFile file) {
		return file.getName().endsWith(".json");
	}

}
