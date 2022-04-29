package fr.abes.qualinka.eclipse.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class TextField extends AbstractField<String> {

	private Text text;

	public TextField(IPage page, String label, String value) {
		super(page, label, value);
		init();
		int nbSuppFilledCols = hookAfterInit();
		this.getPage().fillLastColumns(2 + nbSuppFilledCols);
	}

	private final void init() {
		addLabel(getLabel());

		this.text = new Text(this.getPage().getParentContainer(), SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gd);
		text.setText(getInitialValue());
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
	public String getValue() {
		return this.text.getText();
	}

	@Override
	public void setValue(String value) {
		this.text.setText(value);
	}

}
