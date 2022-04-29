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

package fr.abes.qualinka.eclipse.navigator;

import org.eclipse.jface.viewers.ITreeContentProvider;

import fr.abes.qualinka.eclipse.navigator.resources.QualinkaContainer;
import fr.abes.qualinka.eclipse.navigator.resources.QualinkaElement;

/**
 * @since 3.2
 *
 */
public class QualinkaTreeContentProvider implements ITreeContentProvider {

	private static final Object[] NO_CHILDREN = new Object[0];

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return NO_CHILDREN;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof QualinkaContainer) {
			return ((QualinkaContainer) parentElement).getChildren().toArray();
		}
		return NO_CHILDREN;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return ((QualinkaElement) element).getParent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return ((QualinkaElement) element).hasChildren();
	}

}
