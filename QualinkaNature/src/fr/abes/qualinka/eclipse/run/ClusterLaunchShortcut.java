package fr.abes.qualinka.eclipse.run;

public class ClusterLaunchShortcut extends JSONFileLaunchShortcut {

	@Override
	protected String getArgs() {
		return "-v link --clustering -f ";
	}

}