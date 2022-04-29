package fr.abes.qualinka.eclipse.run;

public class DiagnosticLaunchShortcut extends JSONFileLaunchShortcut {

	@Override
	protected String getArgs() {
		return "-v diagnostic -f ";
	}

}