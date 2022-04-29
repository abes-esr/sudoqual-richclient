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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.IPipelinedTreeContentProvider2;
import org.eclipse.ui.navigator.PipelinedShapeModification;
import org.eclipse.ui.navigator.PipelinedViewerUpdate;

import fr.abes.qualinka.eclipse.QualinkaProject;
import fr.abes.qualinka.eclipse.navigator.resources.QualinkaContainer;
import fr.abes.qualinka.eclipse.navigator.resources.QualinkaElement;

/**
 * @since 3.2
 *
 */
public class QualinkaPipelinedTreeContentProvider extends QualinkaTreeContentProvider
                                                  implements IPipelinedTreeContentProvider2 {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#getPipelinedChildren(
	 * java.lang.Object, java.util.Set)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getPipelinedChildren(Object aParent, Set theCurrentChildren) {
		if (aParent instanceof IProject) {
			IProject project = (IProject) aParent;
			QualinkaProject qualinka = QualinkaProject.from(project);

			if (qualinka.specificViewEnabled()) {
				QualinkaContainer rawFolder = new QualinkaContainer("Raw", aParent,
				                                                    PlatformUI.getWorkbench().getSharedImages()
				                                                              .getImage(ISharedImages.IMG_OBJ_FOLDER));

				Iterator<?> it = theCurrentChildren.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					if (!(o instanceof IFolder && qualinka.isQualinkaFolder((IFolder) o))) {
						it.remove();
						rawFolder.add(o);
					}
				}

				theCurrentChildren.addAll(qualinka.getChildren());
				if (qualinka.rawFolderEnabled()) {
					theCurrentChildren.add(rawFolder);
				}
			} else {
				// filter specific Qualinka folders
				Iterator<?> it = theCurrentChildren.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					if (o instanceof IFolder && qualinka.isQualinkaFolder((IFolder) o)) {
						it.remove();
					}
				}
			}
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof QualinkaElement) {
			return ((QualinkaElement) element).getParent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#getPipelinedElements(
	 * java.lang.Object, java.util.Set)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void getPipelinedElements(Object anInput, Set theCurrentElements) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#getPipelinedParent(
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object getPipelinedParent(Object anObject, Object aSuggestedParent) {
		return aSuggestedParent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptAdd(org.
	 * eclipse.ui.navigator.PipelinedShapeModification)
	 */
	@Override
	public PipelinedShapeModification interceptAdd(PipelinedShapeModification anAddModification) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptRemove(org.
	 * eclipse.ui.navigator.PipelinedShapeModification)
	 */
	@Override
	public PipelinedShapeModification interceptRemove(PipelinedShapeModification aRemoveModification) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptRefresh(org.
	 * eclipse.ui.navigator.PipelinedViewerUpdate)
	 */
	@Override
	public boolean interceptRefresh(PipelinedViewerUpdate aRefreshSynchronization) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptUpdate(org.
	 * eclipse.ui.navigator.PipelinedViewerUpdate)
	 */
	@Override
	public boolean interceptUpdate(PipelinedViewerUpdate anUpdateSynchronization) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.ICommonContentProvider#init(org.eclipse.ui.navigator
	 * .ICommonContentExtensionSite)
	 */
	@Override
	public void init(ICommonContentExtensionSite aConfig) {
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
	 * @see
	 * org.eclipse.ui.navigator.IPipelinedTreeContentProvider2#hasPipelinedChildren(
	 * java.lang.Object, boolean)
	 */
	@Override
	public boolean hasPipelinedChildren(Object anInput, boolean currentHasChildren) {
		return currentHasChildren;
	}

}
