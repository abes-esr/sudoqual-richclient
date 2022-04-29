package fr.abes.qualinka.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class QualinkaPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "fr.abes.qualinka.eclipse";

	private static class SingletonHolder {
		final static QualinkaPlugin instance = new QualinkaPlugin();
	}

	private QualinkaPlugin() {
	}

	/*
	 * @Override public void start(BundleContext context) throws Exception {
	 * super.start(context); plugin = this; }
	 *
	 * @Override public void stop(BundleContext context) throws Exception { plugin =
	 * null; super.stop(context); }
	 *
	 *
	 * public static Activator getDefault() { return plugin; }
	 */

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static QualinkaPlugin getDefault() {
		return SingletonHolder.instance;
	}

}
