package fr.abes.sudoqual

import fr.abes.qualinka.eclipse.QualinkaProject
import org.eclipse.emf.ecore.EObject
import java.util.Set
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path

class DlgpUtils {
	
	private new() {
	}

	def static boolean search(String label, Set<String> set) {
		return set.contains(removeNotFrom(label));
	}

	def static QualinkaProject getQualinkaProjectFrom(EObject object) {
		val platformProject = getProjectFrom(object);
		QualinkaProject.from(platformProject);
	}

	def static IProject getProjectFrom(EObject object) {
		val platformString = object.eResource.URI.toPlatformString(true);
		val myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
		return myFile.getProject();
	}
	
	def static String removeNotFrom(String predicateName) {
		if (predicateName.startsWith("not_")) {
			return predicateName.substring("not_".length)
		}
		return predicateName;
	}
	

}
