/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * Mickael Istria (Red Hat Inc.) Bug 264404 - Problem decorators
 *******************************************************************************/
package fr.abes.qualinka.eclipse.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import fr.abes.qualinka.eclipse.QualinkaIcons;
import fr.abes.qualinka.eclipse.QualinkaIcons.Key;
import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.navigator.resources.QualinkaElement;

public class QualinkaLabelProvider implements ICommonLabelProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof IFolder) {
			IFolder folder = (IFolder) element;
			if (folder.isLinked()) {
				switch (folder.getName()) {
					case QualinkaProject.FEATURES_DIR_NAME:
						return QualinkaIcons.get(Key.FEATURE);
					case QualinkaProject.CRITERIONS_DIR_NAME:
						return QualinkaIcons.get(Key.CRITERION);
					case QualinkaProject.FILTERS_DIR_NAME:
						return QualinkaIcons.get(Key.FILTER);
					case QualinkaProject.HEURISTICS_DIR_NAME:
						return QualinkaIcons.get(Key.HEURISTIC);
					case QualinkaProject.BENCHS_DIR_NAME:
						return QualinkaIcons.get(Key.BENCH);
					case QualinkaProject.UTILS_DIR_NAME:
						return QualinkaIcons.get(Key.UTIL);
					case QualinkaProject.SCENARIOS_DIR_NAME:
						return QualinkaIcons.get(Key.SCENARIO);
					case QualinkaProject.RULES_DIR_NAME:
						return QualinkaIcons.get(Key.RULESET);
				}
			}
		} else if (element instanceof QualinkaElement) {
			return ((QualinkaElement) element).getImage();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof QualinkaElement) {
			return ((QualinkaElement) element).getName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.
	 * viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.
	 * Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface
	 * .viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IMementoAware#restoreState(org.eclipse.ui.IMemento)
	 */
	@Override
	public void restoreState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IMementoAware#saveState(org.eclipse.ui.IMemento)
	 */
	@Override
	public void saveState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.navigator.IDescriptionProvider#getDescription(java.lang.
	 * Object)
	 */
	@Override
	public String getDescription(Object anElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.ICommonLabelProvider#init(org.eclipse.ui.navigator.
	 * ICommonContentExtensionSite)
	 */
	@Override
	public void init(ICommonContentExtensionSite aConfig) {
		// TODO Auto-generated method stub

	}

}
