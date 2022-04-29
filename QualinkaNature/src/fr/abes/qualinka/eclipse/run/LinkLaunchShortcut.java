package fr.abes.qualinka.eclipse.run;

public class LinkLaunchShortcut extends JSONFileLaunchShortcut {

	@Override
	protected String getArgs() {
		return "-v link -f ";
	}

}