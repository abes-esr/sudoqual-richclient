package fr.abes.qualinka.eclipse.run;

public class CompleteLaunchShortcut extends JSONFileLaunchShortcut {

	@Override
	protected String getArgs() {
		return "-v complete -f ";
	}

}