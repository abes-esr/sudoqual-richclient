package fr.abes.qualinka.eclipse.debug;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.ui.ILaunchShortcut;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.debug.ui.FeatureOrPredicateInputDialog;
import fr.abes.qualinka.eclipse.nature.QualinkaStatus;
import fr.abes.sudoqual.rule_engine.predicate.Filter;

public class FilterLaunchShortcut extends QualinkaElementLaunchShortcut<FeatureOrPredicateInputDialog>
                                  implements ILaunchShortcut {

	private static final String INITIAL_DATA_PATTERN = "{\n%s\n}";

	@Override
	protected FeatureOrPredicateInputDialog createInputDialog(Object o, QualinkaProject project) {
		Filter filter = ((Filter) o);
		String initialData = String.format(INITIAL_DATA_PATTERN, Utils.createInputPart(filter.featureSet()));
		return new FeatureOrPredicateInputDialog(null, "Filter input", project.getScenarioNames(), 17, initialData);
	}

	@Override
	protected IStatus checkObject(Object o) {
		if (o instanceof Filter) {
			return Status.OK_STATUS;
		} else {
			return new QualinkaStatus(IStatus.ERROR, "Specified object does not represent a Filter.");
		}
	}

	@Override
	protected String getArgs(Object o, FeatureOrPredicateInputDialog dialog) {

		String filterName = ((Filter) o).getKey();
		String data = dialog.getProtectedData();
		String scenario = dialog.getScenario();

		String pattern = "predicate %s %s --data \"%s\"";
		return String.format(pattern, scenario, filterName, data);
	}

}
