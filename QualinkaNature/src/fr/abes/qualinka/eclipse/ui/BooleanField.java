package fr.abes.qualinka.eclipse.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class BooleanField extends AbstractField<Boolean> implements IField<Boolean> {

	private Button checkbox;

	public BooleanField(IPage page, String label, Boolean value) {
		super(page, label, value);
		init();
		int nbSuppFilledCols = hookAfterInit();
		this.getPage().fillLastColumns(2 + nbSuppFilledCols);
	}

	private final void init() {
		addLabel(getLabel());

		this.checkbox = new Button(this.getPage().getParentContainer(), SWT.CHECK);
	}

	/**
	 * To add supplementary components
	 *
	 * @return the number of added components
	 */
	protected int hookAfterInit() {
		return 0;
	}

	@Override
	public Boolean getValue() {
		return this.checkbox.getSelection();
	}

	@Override
	public void setValue(Boolean value) {
		this.checkbox.setSelection(value);
	}

}
