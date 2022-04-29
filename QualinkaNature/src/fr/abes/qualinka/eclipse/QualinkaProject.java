
package fr.abes.qualinka.eclipse;

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
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_TESTS;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_TESTS_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_UTIL;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.FOLDER_UTIL_DEFAULT;
import static fr.abes.qualinka.eclipse.preferences.QualinkaPreferences.PREFS_QUALIFIER;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.IType;

import fr.abes.qualinka.eclipse.navigator.resources.QualinkaContainer;
import fr.abes.qualinka.eclipse.preferences.QualinkaPreferences;
import fr.abes.sudoqual.rule_engine.predicate.Predicate;

public class QualinkaProject extends QualinkaContainer {

	public static final String BENCHS_DIR_NAME = "Benchs";
	public static final String UTILS_DIR_NAME = "Utils";
	public static final String HEURISTICS_DIR_NAME = "Heuristics";
	public static final String CRITERIONS_DIR_NAME = "Criterions";
	public static final String FILTERS_DIR_NAME = "Filters";
	public static final String FEATURES_DIR_NAME = "Features";
	public static final String SCENARIOS_DIR_NAME = "Scenarios";
	public static final String RESOURCES_DIR_NAME = "Resources";
	public static final String RULES_DIR_NAME = "Rules";
	public static final String TEST_FILES_DIR_NAME = "Tests";

	public static final FileInfoMatcherDescription DLGP_FILTER = new FileInfoMatcherDescription("org.eclipse.core.resources.regexFilterMatcher",
	                                                                                            ".*\\.dlp");
	public static final FileInfoMatcherDescription SCENARIO_FILTER = new FileInfoMatcherDescription("org.eclipse.core.resources.regexFilterMatcher",
	                                                                                                ".*\\.properties");
	public static final FileInfoMatcherDescription RESOURCE_FILTER = new FileInfoMatcherDescription("org.eclipse.core.resources.regexFilterMatcher",
	                                                                                                ".*\\.(?!properties|dlp).*");

	// private static final Collection<String> DATALOG_EXTENSIONS =
	// Arrays.asList("dlp", "dlgp"); //$NON-NLS-1$ //$NON-NLS-2$
	private static final Collection<String> SCENARIO_EXTENSIONS = Arrays.asList("properties"); //$NON-NLS-1$
	private static final long REFRESH_INTERVAL = 5000; // ms

	private final WeakReference<IProject> platformProject;

	private BusinessClassLoader businessClassLoader;
	private Set<String> scenarios;

	private static final Map<IProject, QualinkaProject> map = new WeakHashMap<>();

	public static QualinkaProject from(IProject project) {
		synchronized (map) {
			QualinkaProject toReturn = map.get(project);
			if (toReturn == null) {
				toReturn = new QualinkaProject(project);
				map.put(project, toReturn);
			}
			return toReturn;
		}
	}

	/**
	 * @param name
	 */
	private QualinkaProject(IProject project) {
		super(project.getName(), project.getParent(), null); // $NON-NLS-1$
		this.platformProject = new WeakReference<>(project);
		this.refresh();
	}

	public IEclipsePreferences getPreferences() {
		return new ProjectScope(this.platformProject.get()).getNode(PREFS_QUALIFIER);
	}

	public void configure() {
		cleanConfigure();

		IEclipsePreferences prefs = new ProjectScope(this.platformProject.get()).getNode(PREFS_QUALIFIER);
		assert prefs != null;
		this.createLinkToFolder(FEATURES_DIR_NAME, prefs.get(FOLDER_FEATURE, FOLDER_FEATURE_DEFAULT));
		this.createLinkToFolder(FILTERS_DIR_NAME, prefs.get(FOLDER_FILTER, FOLDER_FILTER_DEFAULT));
		this.createLinkToFolder(CRITERIONS_DIR_NAME, prefs.get(FOLDER_CRITERION, FOLDER_CRITERION_DEFAULT));
		this.createLinkToFolder(HEURISTICS_DIR_NAME, prefs.get(FOLDER_HEURISTIC, FOLDER_HEURISTIC_DEFAULT));
		this.createLinkToFolder(UTILS_DIR_NAME, prefs.get(FOLDER_UTIL, FOLDER_UTIL_DEFAULT));
		this.createLinkToFolder(BENCHS_DIR_NAME, prefs.get(FOLDER_BENCH, FOLDER_BENCH_DEFAULT));
		this.createLinkToFolder(SCENARIOS_DIR_NAME, prefs.get(FOLDER_SCENARIO, FOLDER_SCENARIO_DEFAULT),
		    SCENARIO_FILTER);
		this.createLinkToFolder(TEST_FILES_DIR_NAME, prefs.get(FOLDER_TESTS, FOLDER_TESTS_DEFAULT));
		this.createLinkToFolder(RESOURCES_DIR_NAME, prefs.get(FOLDER_SCENARIO, FOLDER_SCENARIO_DEFAULT),
		    RESOURCE_FILTER);
		this.createLinkToFolder(RULES_DIR_NAME, prefs.get(FOLDER_RULE, FOLDER_RULE_DEFAULT), DLGP_FILTER);
	}

	public final void cleanConfigure() {
		try {
			this.removeLinkToFolders();
		} catch (CoreException e) {
			QualinkaUtils.handleError("Error while removing virtual folders", e);
		}
	}

	private static long lastRefresh = 0L;

	public void refresh() {
		final long currentTime = System.currentTimeMillis();
		if (currentTime - lastRefresh > REFRESH_INTERVAL) {
			lastRefresh = currentTime;
			if (this.hasPredicateChanged()) {
				this.businessClassLoader = new BusinessClassLoader(this.platformProject.get(),
				                                                   this.getBusinessClassPackage());
			}
		}
	}

	public final void deconfigure() {
		// do nothing
	}

	public Set<String> getPredicatesNames() {
		this.refresh();
		return this.businessClassLoader.getPredicateNames();
	}

	public IType getPredicateType(String name) {
		this.refresh();
		return this.businessClassLoader.getPredicateType(name);
	}

	public Predicate getPredicateInstance(String name) {
		this.refresh();
		return this.businessClassLoader.createInstanceOf(name);
	}

	/**
	 * Returns a class instance or null
	 *
	 * @param className
	 * @return
	 */
	public Object createInstance(String className) {
		try {
			this.refresh();
			return this.businessClassLoader.createInstance(className);
		} catch (QualinkaInstantiationException e) {
			return null;
		}
	}

	public String[] getScenarioNames() {
		if (this.scenarios == null) {
			this.scenarios = new HashSet<String>();
			IEclipsePreferences prefs = new ProjectScope(this.platformProject.get()).getNode(PREFS_QUALIFIER);
			assert prefs != null;
			Collection<IResource> scenarioFiles;
			try {
				scenarioFiles = findResourcesByExtensions(prefs.get(FOLDER_SCENARIO, FOLDER_SCENARIO_DEFAULT),
				    SCENARIO_EXTENSIONS);
				this.scenarios = scenarioFiles.stream()
				                              .map(resource -> QualinkaUtils.removeExtension(resource.getName()))
				                              .collect(Collectors.toSet());
			} catch (CoreException e) {
				QualinkaUtils.handleError("Unable to get resources from scenario folder.", e);
			}
		}
		return this.scenarios.toArray(new String[this.scenarios.size()]);
	}

	public String getJavadoc(String name) {
		return QualinkaUtils.getJavadoc(this.businessClassLoader.getPredicateType(name), false);
	}

	private IFolder createLinkToFolder(String name, String pathName, FileInfoMatcherDescription filter) {
		IFolder folder = createLinkToFolder(name, pathName);
		try {
			// remove existing filters
			for (IResourceFilterDescription d : folder.getFilters()) {
				d.delete(0, null);
			}
			// add the specified one
			folder.createFilter(IResourceFilterDescription.INCLUDE_ONLY | IResourceFilterDescription.FILES, filter,
			    IResource.BACKGROUND_REFRESH, null);
		} catch (CoreException e) {
			QualinkaUtils.handleError("Error while adding filter on " + name, e);
		}
		return folder;
	}

	private IFolder createLinkToFolder(String name, String pathName) {
		assert pathName != null && !pathName.isBlank();
		assert name != null && !name.isBlank();

		IProject project = this.platformProject.get();
		IResource resource = project.findMember(pathName);
		if (resource == null) {
			// TODO LOG
			// QualinkaUtils.handleError("Error while creating virtual folder " + name + "
			// folder:" +
			// "related path " + pathName + " does not exist, please edit Qualinka project
			// properties.");
			return null;
		}

		URI uri;
		try {
			uri = new URI("PROJECT_LOC/" + resource.getFullPath().makeRelativeTo(project.getFullPath()).toString());
			IFolder file = project.getFolder(name);
			if (!file.exists()) {
				try {
					file.createLink(uri, IResource.BACKGROUND_REFRESH, null);
				} catch (CoreException e) {
					QualinkaUtils.handleError("Error while creating virtual folder " + name + " folder", e);
				}
			}
			return file;
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

	}

	private void removeLinkToFolders() throws CoreException {
		IProject project = this.platformProject.get();
		for (IResource resource : project.members()) {
			if (resource.exists() && resource instanceof IFolder) {
				IFolder folder = (IFolder) resource;
				if (folder.isLinked()) {
					folder.delete(IResource.NEVER_DELETE_PROJECT_CONTENT, null);
				}
			}
		}
	}

	private Collection<IResource> findResourcesByExtensions(String path, Collection<String> extensions)
	    throws CoreException {
		IResource dir = this.platformProject.get().findMember(path);
		Collection<IResource> result = new LinkedList<>();
		if (dir instanceof IContainer) {
			IResource[] members = ((IContainer) dir).members();
			for (IResource resource : members) {
				if (resource instanceof IFile) {
					if (extensions.contains(((IFile) resource).getFileExtension())) {
						result.add(resource);
					}
				}
			}
		}
		return result;
	}

	public boolean rawFolderEnabled() {
		return true;
	}

	public boolean specificViewEnabled() {
		return this.getPreferences().getBoolean(QualinkaPreferences.ENABLE_PROJECT_EXPLORER_CONTENT_PROVIDER, true);
	}

	/**
	 *
	 * @return true if the specified folder was created by this class.
	 */
	public boolean isQualinkaFolder(IFolder folder) {
		if (!folder.isLinked()) {
			return false;
		}
		switch (folder.getName()) {
			case FEATURES_DIR_NAME:
			case FILTERS_DIR_NAME:
			case CRITERIONS_DIR_NAME:
			case HEURISTICS_DIR_NAME:
			case UTILS_DIR_NAME:
			case BENCHS_DIR_NAME:
			case SCENARIOS_DIR_NAME:
			case RULES_DIR_NAME:
			case RESOURCES_DIR_NAME:
			case TEST_FILES_DIR_NAME:
				return true;
			default:
				return false;
		}

	}

	public String getMainClass() {
		return this.getPreferences().get(QualinkaPreferences.MAIN_CLASS, QualinkaPreferences.MAIN_CLASS_DEFAULT);
	}

	public String getBusinessClassPackage() {
		return "fr.abes.sudoqual"; // FIXME
	}

	private long oldPredicateFolderStamp = 0;

	private boolean hasPredicateChanged() {
		IProject project = this.platformProject.get();
		long stamp = 1;
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			IResource resource = project.findMember(CRITERIONS_DIR_NAME);
			if (resource instanceof IContainer) {
				stamp += QualinkaUtils.contentBasedHash((IContainer) resource);
			}
			resource = project.findMember(FILTERS_DIR_NAME);
			if (resource instanceof IContainer) {
				stamp += 41 * QualinkaUtils.contentBasedHash((IContainer) resource);
			}
		} catch (CoreException e) {
			return false;
		}
		if (stamp != this.oldPredicateFolderStamp) {
			this.oldPredicateFolderStamp = stamp;
			return true;
		}
		return false;
	}

}
