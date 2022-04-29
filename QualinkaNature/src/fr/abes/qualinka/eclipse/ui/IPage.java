package fr.abes.qualinka.eclipse.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public interface IPage {

	Composite getParentContainer();

	void fillLastColumns(int nbFilledCols);

	Shell getShell();
}
