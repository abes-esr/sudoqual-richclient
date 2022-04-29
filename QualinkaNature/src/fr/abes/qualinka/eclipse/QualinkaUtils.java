package fr.abes.qualinka.eclipse;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavadocContentAccess;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.statushandlers.StatusManager;

import fr.abes.qualinka.eclipse.nature.QualinkaStatus;

public final class QualinkaUtils {
	private QualinkaUtils() {
	}

	public static long contentBasedHash(IContainer folder) throws CoreException {
		long stamp = folder.getModificationStamp();
		for (IResource r : folder.members()) {
			if (r instanceof IContainer) {
				stamp += 83 * contentBasedHash((IContainer) r);
			} else {
				stamp += 83 * r.getModificationStamp();
			}
		}
		return stamp;
	}

	public static String folderPathToJavaPackageName(String path) {
		return path.replaceAll("^.?/?src/main/java/", "").replaceAll("[ \\t]", "").replaceAll("/", " ").trim()
		           .replaceAll(" ", ".");
	}

	public static String getJavadoc(IMember member, boolean allowInherited) {
		Reader reader;
		try {
			reader = JavadocContentAccess.getContentReader(member, allowInherited);
			if (reader != null) {
				return toString(reader);
			} else {
				return "";
			}
		} catch (JavaModelException | IOException e) {
			return "";
		}
	}

	public static String toString(Reader reader) throws IOException {
		char[] arr = new char[8 * 1024];
		StringBuilder buffer = new StringBuilder();
		int numCharsRead;
		while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
			buffer.append(arr, 0, numCharsRead);
		}
		reader.close();
		return buffer.toString();
	}

	public static String fileToJavaPath(IFile file) {
		String s = file.getLocation().toString();
		int len = file.getProject().findMember("src/main/java").getLocation().toString().length();
		s = s.substring(len + 1, s.length() - ".java".length());
		return s.replace("/", ".");
	}

	public static IJavaElement iAdaptableToIJavaElement(IAdaptable adaptable) {
		if (adaptable instanceof IResource) {
			adaptable = findOriginalIFile((IResource) adaptable);
		}
		return adaptable.getAdapter(IJavaElement.class);
	}

	/**
	 * IResource can be under a linked folder so {@link IResource#getFullPath()}
	 * does not reflect file system. This method try to find an IFile representing
	 * the file system.
	 *
	 * @param file
	 * @return
	 * @see {@link IResource#isLinked()}
	 */
	public static IResource findOriginalIFile(IResource file) {
		IProject project = file.getProject();
		IPath projectLocation = project.getLocation();
		IPath realFileLocation = file.getLocation().makeRelativeTo(projectLocation);
		return project.findMember(realFileLocation);
	}

	public static String removeExtension(String filename) {
		int idx = filename.lastIndexOf('.');
		return filename.substring(0, idx);
	}

	public static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	public static Shell getShell() {
		return PlatformUI.getWorkbench().getModalDialogShellProvider().getShell();
	}

	public static void runWithProgress(boolean cancelable, IRunnableWithProgress runnable) {
		try {
			PlatformUI.getWorkbench().getProgressService().run(false, cancelable, runnable);
		} catch (InvocationTargetException | InterruptedException e) {
			handleError("Error when running task", e);
		}
	}

	public static void handleError(String msg) {
		StatusManager.getManager().handle(new QualinkaStatus(IStatus.ERROR, msg),
		    StatusManager.SHOW | StatusManager.LOG);
	}

	public static void handleError(String msg, Throwable e) {
		StatusManager.getManager().handle(new QualinkaStatus(IStatus.ERROR, msg, e),
		    StatusManager.SHOW | StatusManager.LOG);
	}
}
