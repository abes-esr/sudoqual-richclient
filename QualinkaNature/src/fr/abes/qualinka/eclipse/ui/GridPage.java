package fr.abes.qualinka.eclipse.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class GridPage implements IPage {

	private static final int VERTICAL_SPACING = 10;
	private static final int SWT_STYLE = SWT.NULL;

	private int numCols;
	private Composite container;
	private Shell shell;

	/**
	 *
	 * @param parent
	 *                    parent a widget which will be the parent of the new
	 *                    instance (cannot be null)
	 * @param numCols
	 *                    must be greater or equals to 2
	 */
	public GridPage(Composite parent, int numCols, Shell shell) {
		assert (numCols >= 2);
		assert (parent != null);

		GridLayout layout = new GridLayout();
		layout.numColumns = this.numCols = numCols;
		layout.verticalSpacing = VERTICAL_SPACING;

		this.container = new Composite(parent, SWT_STYLE);
		this.container.setLayout(layout);

		this.shell = shell;
	}

	@SuppressWarnings("unused")
	@Override
	public void fillLastColumns(int nbFilledCols) {
		// fills last columns
		for (int i = nbFilledCols; i < this.numCols; ++i) {
			new Label(this.container, SWT.NULL);
		}
	}

	@Override
	public Composite getParentContainer() {
		return this.container;
	}

	@Override
	public Shell getShell() {
		return this.shell;
	}

}
