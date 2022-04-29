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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;

/**
 * @since 3.2
 *
 */
public class QualinkaContainer extends QualinkaElement {

	private final Collection<Object> members = new ArrayList<>();

	/**
	 * @param name
	 */
	public QualinkaContainer(String name, Object parent, Image image) {
		super(name, parent, image);
	}

	@Override
	public Collection<Object> getChildren() {
		return Collections.unmodifiableCollection(members);
	}

	@Override
	public boolean hasChildren() {
		return !this.members.isEmpty();
	}

	/**
	 * @param m
	 */
	public void add(Object m) {
		this.members.add(m);
	}

	/**
	 * @param m
	 * @param findResourcesByExtensions
	 */
	public void addAll(Collection<Object> m) {
		this.members.addAll(m);

	}

	/**
	 * @param m
	 * @param findResourcesByExtensions
	 */
	public void addAll(IResource[] array) {
		this.members.addAll(Arrays.asList(array));
	}

	public void clear() {
		this.members.clear();
	}

	@Override
	public String toString() {
		return "[QualinkaContainer: " + this.getName() + ", hasChildren: " + this.hasChildren() + "]";
	}

}
