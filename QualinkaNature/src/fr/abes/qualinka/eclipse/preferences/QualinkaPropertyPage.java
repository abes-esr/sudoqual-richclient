package fr.abes.qualinka.eclipse.preferences;

import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_BENCH;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_BENCH_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_CRITERION;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_CRITERION_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_FEATURE;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_FEATURE_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_FILTER;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_FILTER_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_HEURISTIC;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_HEURISTIC_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_RULE;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_RULE_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_SCENARIO;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_SCENARIO_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_UTIL;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_UTIL_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.MAIN_CLASS;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.MAIN_CLASS_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.PREFS_QUALIFIER;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.osgi.service.prefs.BackingStoreException;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.ui.BooleanField;
import fr.abes.qualinka.eclipse.ui.FolderField;
import fr.abes.qualinka.eclipse.ui.GridPage;
import fr.abes.qualinka.eclipse.ui.IPage;
import fr.abes.qualinka.eclipse.ui.JavaFileField;

public class QualinkaPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

	private FolderField benchFolder;
	private FolderField criterionFolder;
	private FolderField featureFolder;
	private FolderField filterFolder;
	private FolderField heuristicFolder;
	private FolderField ruleFolder;
	private FolderField scenarioFolder;
	private FolderField utilFolder;

	private BooleanField enableProjectExplorerContentProvider;
	private JavaFileField mainFile;

	private IEclipsePreferences prefs;

	public QualinkaPropertyPage() {
		super();
	}

	private void initialize(Composite parent) {
		prefs = new ProjectScope((IProject) getElement()).getNode(PREFS_QUALIFIER);
		assert prefs != null;

		IPage page = new GridPage(parent, 3, getShell());
		IContainer root = ((IProject) getElement());

		// create field
		mainFile = new JavaFileField(page, "Select project main class", MAIN_CLASS_DEFAULT, root);
		enableProjectExplorerContentProvider = new BooleanField(page, "Enable Project Explorer view",
		                                                        ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER_DEFAULT);

		benchFolder = new FolderField(page, "Path to bench folder", FOLDER_BENCH_DEFAULT, root);
		criterionFolder = new FolderField(page, "Path to criterion folder", FOLDER_CRITERION_DEFAULT, root);
		featureFolder = new FolderField(page, "Path to feature folder", FOLDER_FEATURE_DEFAULT, root);
		filterFolder = new FolderField(page, "Path to filter folder", FOLDER_FILTER_DEFAULT, root);
		heuristicFolder = new FolderField(page, "Path to heuristic folder", FOLDER_HEURISTIC_DEFAULT, root);
		ruleFolder = new FolderField(page, "Path to rule folder", FOLDER_RULE_DEFAULT, root);
		scenarioFolder = new FolderField(page, "Path to scenario folder", FOLDER_SCENARIO_DEFAULT, root);
		utilFolder = new FolderField(page, "Path to util folder", FOLDER_UTIL_DEFAULT, root);

		// set current value
		mainFile.setValue(prefs.get(MAIN_CLASS, mainFile.getValue()));
		enableProjectExplorerContentProvider.setValue(prefs.getBoolean(ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER,
		    enableProjectExplorerContentProvider.getValue()));

		benchFolder.setValue(prefs.get(FOLDER_BENCH, benchFolder.getValue()));
		criterionFolder.setValue(prefs.get(FOLDER_CRITERION, criterionFolder.getValue()));
		featureFolder.setValue(prefs.get(FOLDER_FEATURE, featureFolder.getValue()));
		filterFolder.setValue(prefs.get(FOLDER_FILTER, filterFolder.getValue()));
		heuristicFolder.setValue(prefs.get(FOLDER_HEURISTIC, heuristicFolder.getValue()));
		ruleFolder.setValue(prefs.get(FOLDER_RULE, ruleFolder.getValue()));
		scenarioFolder.setValue(prefs.get(FOLDER_SCENARIO, scenarioFolder.getValue()));
		utilFolder.setValue(prefs.get(FOLDER_UTIL, utilFolder.getValue()));

	}

	@Override
	protected Control createContents(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		this.initialize(composite);

		return composite;
	}

	@Override
	public boolean performOk() {
		prefs.put(FOLDER_BENCH, benchFolder.getValue());
		prefs.put(FOLDER_CRITERION, criterionFolder.getValue());
		prefs.put(FOLDER_FEATURE, featureFolder.getValue());
		prefs.put(FOLDER_FILTER, filterFolder.getValue());
		prefs.put(FOLDER_HEURISTIC, heuristicFolder.getValue());
		prefs.put(FOLDER_RULE, ruleFolder.getValue());
		prefs.put(FOLDER_SCENARIO, scenarioFolder.getValue());
		prefs.put(FOLDER_UTIL, utilFolder.getValue());

		prefs.put(MAIN_CLASS, mainFile.getValue());
		prefs.put(ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER, enableProjectExplorerContentProvider.getValue().toString());

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			return false;
		}

		IProject platformProject = (IProject) getElement();
		QualinkaProject project = QualinkaProject.from(platformProject);
		project.configure();
		try {
			platformProject.touch(null);
		} catch (CoreException e) {
			// do nothing - touch is just for trigger refresh
		}

		return true;
	}

	// //////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES
	// //////////////////////////////////////////////////////////////////

	/*
	 * private abstract static class Field<T> { private final String key; private
	 * final String label; private final T defaultValue;
	 *
	 * Field(String key, String label, T defaultValue) { this.key = key; this.label
	 * = label; this.defaultValue = defaultValue; }
	 *
	 * public void init(Composite parent, IEclipsePreferences prefs) { assert prefs
	 * != null;
	 *
	 * initLabel(parent, this.label); initField(parent, prefs); }
	 *
	 * private static void initLabel(Composite parent, String label) { Label
	 * ownerLabel = new Label(parent, SWT.NONE); ownerLabel.setText(label); }
	 *
	 * protected abstract void initField(Composite parent, IEclipsePreferences
	 * prefs);
	 *
	 * public T getDefaultValue() { return this.defaultValue; }
	 *
	 * public abstract String getValue();
	 *
	 * public abstract void setValue(T value);
	 *
	 * public String getKey() { return this.key; }
	 *
	 * public void save(IEclipsePreferences prefs) { prefs.put(this.getKey(),
	 * this.getValue()); } }
	 *
	 * private class StringField extends Field<String> {
	 *
	 * Text field;
	 *
	 * StringField(String key, String label, String defaultValue) { super(key,
	 * label, defaultValue); }
	 *
	 * public void initField(Composite parent, IEclipsePreferences prefs) {
	 * this.field = new Text(parent, SWT.SINGLE | SWT.BORDER); GridData gd = new
	 * GridData(); gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
	 * this.field.setLayoutData(gd); this.setValue(prefs.get(this.getKey(),
	 * this.getDefaultValue())); }
	 *
	 * public void setValue(String value) { this.field.setText(value); }
	 *
	 *
	 * public String getValue() { return this.field.getText(); } }
	 *
	 * private static class BooleanField extends Field<Boolean> {
	 *
	 * Button field;
	 *
	 * BooleanField(String key, String label, Boolean defaultValue) { super(key,
	 * label, defaultValue); }
	 *
	 * public void initField(Composite parent, IEclipsePreferences prefs) {
	 * this.field = new Button(parent, SWT.CHECK);
	 * this.setValue(prefs.getBoolean(this.getKey(), this.getDefaultValue())); }
	 *
	 * public String getValue() { return
	 * Boolean.toString(this.field.getSelection()); }
	 *
	 * public void setValue(Boolean value) { this.field.setSelection(value); } }
	 */

}