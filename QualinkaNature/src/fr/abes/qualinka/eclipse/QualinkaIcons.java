/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package fr.abes.qualinka.eclipse;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @since 3.2
 *
 */
public final class QualinkaIcons {
	public static final IPath ICONS_PATH = new Path("$nl$/icons/"); //$NON-NLS-1$
	static final ImageRegistry imageRegistry = QualinkaPlugin.getDefault().getImageRegistry();

	private QualinkaIcons() {
	}

	public static enum Key {
							FEATURE("attr.png"), CRITERION("crit.png"), FILTER("filter.png"), RULESET("rule.png"), HEURISTIC("heuristic.png"), SCENARIO("cache.png"), PROJECT("books-16x16.png"), UTIL("exec.png"), BENCH("eval.png");

		private Key(String filename) {
			createImage(this.name(), filename);
		}
	}

	public static Image get(Key key) {
		return imageRegistry.get(key.name());
	}

	private static void createImage(String key, String name) {
		IPath path = new Path("/plugin")//$NON-NLS-1$
		                                .append(QualinkaPlugin.getDefault().getBundle().getSymbolicName())
		                                .append(ICONS_PATH).append(name);
		URL foundUrl = null;
		try {
			URL url = new URI("platform", null, path.toString(), null).toURL(); //$NON-NLS-1$
			foundUrl = FileLocator.find(url);
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}

		if (foundUrl != null) {
			imageRegistry.put(key, ImageDescriptor.createFromURL(foundUrl));
		}
	}

}
