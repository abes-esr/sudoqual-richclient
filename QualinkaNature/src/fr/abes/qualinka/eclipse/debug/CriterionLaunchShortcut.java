package fr.abes.qualinka.eclipse.debug;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.ui.ILaunchShortcut;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.debug.ui.FeatureOrPredicateInputDialog;
import fr.abes.qualinka.eclipse.nature.QualinkaStatus;
import fr.abes.sudoqual.rule_engine.predicate.Criterion;

public class CriterionLaunchShortcut extends QualinkaElementLaunchShortcut<FeatureOrPredicateInputDialog>
                                     implements ILaunchShortcut {

	private static final String INITIAL_DATA_PATTERN = "{\n    \"source\": {\n%s\n    },\n    \"target\": {\n%s\n    }\n}";

	@Override
	protected FeatureOrPredicateInputDialog createInputDialog(Object o, QualinkaProject project) {
		Criterion criterion = ((Criterion) o);
		String sourceContent = Utils.createInputPart(criterion.sourceFeatureSet());
		String targetContent = Utils.createInputPart(criterion.targetFeatureSet());
		String initialData = String.format(INITIAL_DATA_PATTERN, sourceContent, targetContent);
		return new FeatureOrPredicateInputDialog(null, "Criterion input", project.getScenarioNames(), 17, initialData);
	}

	@Override
	protected IStatus checkObject(Object o) {
		if (o instanceof Criterion) {
			return Status.OK_STATUS;
		} else {
			return new QualinkaStatus(IStatus.ERROR, "Specified object does not represent a Criterion.");
		}
	}

	@Override
	protected String getArgs(Object o, FeatureOrPredicateInputDialog dialog) {

		String criterionName = ((Criterion) o).getKey();
		String data = dialog.getProtectedData();
		String scenario = dialog.getScenario();

		String pattern = "predicate %s %s --data \"%s\"";
		return String.format(pattern, scenario, criterionName, data);
	}

}
