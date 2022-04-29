package fr.abes.qualinka.eclipse.debug;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.ui.ILaunchShortcut;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.debug.ui.FeatureOrPredicateInputDialog;
import fr.abes.qualinka.eclipse.nature.QualinkaStatus;
import fr.abes.sudoqual.rule_engine.feature.Feature;

public class FeatureLaunchShortcut extends QualinkaElementLaunchShortcut<FeatureOrPredicateInputDialog>
                                   implements ILaunchShortcut {

	@Override
	protected FeatureOrPredicateInputDialog createInputDialog(Object o, QualinkaProject project) {
		return new FeatureOrPredicateInputDialog(null, "Feature input", project.getScenarioNames(), 7);
	}

	@Override
	protected IStatus checkObject(Object o) {
		if (o instanceof Feature) {
			return Status.OK_STATUS;
		} else {
			return new QualinkaStatus(IStatus.ERROR, "Specified object does not represent a Feature.");
		}
	}

	@Override
	protected String getArgs(Object o, FeatureOrPredicateInputDialog dialog) {

		String featureName = ((Feature) o).getKey();
		String data = dialog.getProtectedData();
		String scenario = dialog.getScenario();

		String pattern = "feature %s %s --data \"%s\"";
		return String.format(pattern, scenario, featureName, data);
	}

}
