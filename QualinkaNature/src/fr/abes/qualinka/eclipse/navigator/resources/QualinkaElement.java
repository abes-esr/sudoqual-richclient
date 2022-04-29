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

package fr.abes.qualinka.eclipse.navigator.resources;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.swt.graphics.Image;

/**
 * @since 3.2
 *
 */
public class QualinkaElement extends PlatformObject {

	private final String name;
	private final Image image;
	private final Object parent;

	public QualinkaElement(String name, Object parent, Image image) {
		this.name = name;
		this.image = image;
		this.parent = parent;
	}

	public Image getImage() {
		return this.image;
	}

	public String getName() {
		return this.name;
	}

	public Object getParent() {
		return this.parent;
	}

	public Collection<Object> getChildren() {
		return Collections.emptyList();
	}

	public boolean hasChildren() {
		return false;
	}
}
