package fr.abes.qualinka.eclipse.nature;

import org.eclipse.core.runtime.Status;

import fr.abes.qualinka.eclipse.QualinkaPlugin;

public class QualinkaStatus extends Status {

	public QualinkaStatus(int severity, String message) {
		super(severity, QualinkaPlugin.PLUGIN_ID, message);
	}

	public QualinkaStatus(int severity, String message, Throwable exception) {
		super(severity, QualinkaPlugin.PLUGIN_ID, message, exception);
	}

	public QualinkaStatus(int severity, int code, String message, Throwable exception) {
		super(severity, QualinkaPlugin.PLUGIN_ID, code, message, exception);
	}

}
