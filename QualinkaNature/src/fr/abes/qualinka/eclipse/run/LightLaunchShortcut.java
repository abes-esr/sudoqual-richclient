package fr.abes.qualinka.eclipse.run;

public class LightLaunchShortcut extends JSONFileLaunchShortcut {

	@Override
	protected String getArgs() {
		return "-v link --diagnostic -f ";
	}

}