/**
 * This file is part of the SudoQual project.
 */
package fr.abes.qualinka.eclipse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import fr.abes.sudoqual.rule_engine.predicate.Predicate;

/**
 * @author Clément Sipieter {@literal <clement@6pi.fr>}
 */
public class BusinessClassLoader {

	private Map<String, IType> predicateMap;
	private IJavaProject project;

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs an InstanceLoader which will look for classes from the specified
	 * package (and its subpackage).
	 *
	 * @param fromPackage
	 */
	public BusinessClassLoader(IProject myProject, String fromPackageName) {
		this.project = JavaCore.create(myProject);

		// construct predicateMap
		predicateMap = new ConcurrentHashMap<>();
		Map<Predicate, IType> predicates = getAllPredicateInstances();
		predicates.forEach((x, y) -> predicateMap.put(x.getKey(), y));
		this.predicateMap = Collections.unmodifiableMap(predicateMap);

	}

	// /////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// /////////////////////////////////////////////////////////////////////////

	// @Override
	public Set<String> getPredicateNames() {
		return predicateMap.keySet();
	}

	public IType getPredicateType(String name) {
		return predicateMap.get(name);
	}

	public Predicate createInstanceOf(String name) {
		return createInstanceOf(predicateMap.get(name));
	}

	public Predicate createInstanceOf(IType type) {
		return createInstanceOf(this.createLoader(), type);
	}

	public static Predicate createInstanceOf(ClassLoader loader, IType type) {
		if (type == null) {
			return null;
		}
		Object o = null;
		try {
			o = createInstance(loader, type.getFullyQualifiedName());
		} catch (QualinkaInstantiationException e) {
			QualinkaUtils.handleError("Unable to create instance of " + type.getFullyQualifiedName(), e);
			return null;
		}
		if (o instanceof Predicate) {
			return (Predicate) o;
		} else if (o != null) {
			QualinkaUtils.handleError(
			    "Try to instantiate the following class: " + type.getFullyQualifiedName() + " as Predicate");
		}
		return null;
	}

	public Object createInstance(String className) throws QualinkaInstantiationException {
		return createInstance(createLoader(), className);
	}

	public static Object createInstance(ClassLoader loader, String className) throws QualinkaInstantiationException {
		Class<?> cl = null;
		try {
			cl = loader.loadClass(className);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			throw new QualinkaInstantiationException("Unable to load the following class: " + className, e);
		}
		if (cl != null) {
			int modifiers = cl.getModifiers();
			if (!Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
				try {
					return cl.getDeclaredConstructor().newInstance();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				         | NoSuchMethodException | SecurityException | NoClassDefFoundError
				         | InstantiationException e) {
					throw new QualinkaInstantiationException("Unable to instantiate the following class: " + cl
					        + " because: ", e);
				}
			}
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	// /////////////////////////////////////////////////////////////////////////

	private Map<Predicate, IType> getAllPredicateInstances() {
		IJavaProject javaProject = this.project;
		Map<Predicate, IType> map = new HashMap<>();
		IType predicateType = null;
		ITypeHierarchy hierarchy;
		try {
			predicateType = javaProject.findType(Predicate.class.getName());
			if (predicateType == null) {
				QualinkaUtils.handleError(
				    "Unable to find the Predicate class, please check your project configuration.");
				return map;
			}
			IPath path = javaProject.getPath().append("/src/main/java/");
			IJavaElement frag = javaProject.findPackageFragmentRoot(path);
			IRegion region = JavaCore.newRegion();
			region.add(frag);

			hierarchy = javaProject.newTypeHierarchy(predicateType, region, null);
		} catch (JavaModelException e) {
			QualinkaUtils.handleError("Error during available predicate detection");
			return map;
		}

		ClassLoader loader = this.createLoader();
		for (IType subType : hierarchy.getAllSubtypes(predicateType)) {
			Predicate predicate = createInstanceOf(loader, subType);
			if (predicate != null) {
				map.put(predicate, subType);
			}

		}
		return map;
	}

	private final ClassLoader createLoader() {
		try {
			URL[] urls = getURLsForClassLoader();
			return new URLClassLoader(urls, QualinkaUtils.class.getClassLoader());
		} catch (CoreException | MalformedURLException e) {
			QualinkaUtils.handleError("Error during classLoader creation.", e);
			return null;
		}
	}

	private URL[] urlsForClassLoader = null;
	private Object lockGetURLsForClassLoader = new Object();

	private URL[] getURLsForClassLoader() throws CoreException, MalformedURLException {
		synchronized (lockGetURLsForClassLoader) {
			if (urlsForClassLoader == null) {
				ILaunchConfigurationType type = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(
				    IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
				ILaunchConfigurationWorkingCopy config;
				config = type.newInstance(null, "toConstructClassLoader");
				config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getElementName());

				IRuntimeClasspathEntry[] entries;
				entries = JavaRuntime.computeUnresolvedRuntimeClasspath(config);
				entries = JavaRuntime.resolveRuntimeClasspath(entries, config);

				URL[] urls = new URL[entries.length];
				for (int i = 0; i < entries.length; ++i) {
					String location = entries[i].getLocation();
					if (!location.endsWith(".jar") && !location.endsWith("/")) {
						location += "/";
					}
					urls[i] = new URL("file:" + location);

				}
				urlsForClassLoader = urls;
			}
		}
		return urlsForClassLoader;
	}

}
