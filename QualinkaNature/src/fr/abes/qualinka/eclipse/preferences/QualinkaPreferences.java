package fr.abes.qualinka.eclipse.preferences;

import fr.abes.qualinka.eclipse.QualinkaPlugin;

public final class QualinkaPreferences {

	private QualinkaPreferences() {
	}

	public static final String PREFS_QUALIFIER = QualinkaPlugin.PLUGIN_ID + ".preferences";

	public static final String MAIN_CLASS = "class.main";
	public static final String MAIN_CLASS_DEFAULT = "fr.abes.sudoqual.myproject.MyMain";

	public static final String FOLDER_UTIL = "folder.util";
	public static final String FOLDER_UTIL_DEFAULT = "./src/main/java/fr/abes/sudoqual/util/";

	public static final String FOLDER_HEURISTIC = "folder.heuristic";
	public static final String FOLDER_HEURISTIC_DEFAULT = "./src/main/java/fr/abes/sudoqual/heuristic/";

	public static final String FOLDER_CRITERION = "folder.criterion";
	public static final String FOLDER_CRITERION_DEFAULT = "./src/main/java/fr/abes/sudoqual/criterion/";

	public static final String FOLDER_FILTER = "folder.filter";
	public static final String FOLDER_FILTER_DEFAULT = "./src/main/java/fr/abes/sudoqual/filter/";

	public static final String FOLDER_FEATURE = "folder.feature";
	public static final String FOLDER_FEATURE_DEFAULT = "./src/main/java/fr/abes/sudoqual/feature/";

	public static final String ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER = "explorer.contentProvider.enable";
	public static final boolean ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER_DEFAULT = true;

	public static final String FOLDER_BENCH = "folder.bench";
	public static final String FOLDER_BENCH_DEFAULT = "./src/main/resources/bench/";

	public static final String FOLDER_TESTS = "folder.tests";
	public static final String FOLDER_TESTS_DEFAULT = "./src/test/resources/";

	public static final String FOLDER_SCENARIO = "folder.scenario";
	public static final String FOLDER_SCENARIO_DEFAULT = "./src/main/resources/scenarios/";

	public static final String FOLDER_RULE = "folder.rule";
	public static final String FOLDER_RULE_DEFAULT = FOLDER_SCENARIO_DEFAULT;

}
