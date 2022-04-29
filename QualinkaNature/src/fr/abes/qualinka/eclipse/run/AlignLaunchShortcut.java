package fr.abes.qualinka.eclipse.run;

public class AlignLaunchShortcut extends JSONFileLaunchShortcut {

	@Override
	protected String getArgs() {
		return "-v align -f ";
	}

}