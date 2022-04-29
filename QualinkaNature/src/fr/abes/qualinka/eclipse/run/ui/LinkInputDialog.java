package fr.abes.qualinka.eclipse.run.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LinkInputDialog extends Dialog {

	private static final int style = SWT.BORDER;
	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * The text input value; the empty string by default.
	 */
	private String data;

	/**
	 * Input text widget.
	 */
	private Text text;

	private Combo combo;

	private int nbRows;

	private String[] scenarioArray;
	private String scenario;

	public LinkInputDialog(Shell parentShell, String dialogTitle, String[] scenarios, int nbRows) {
		this(parentShell, dialogTitle, scenarios, nbRows, "");
	}

	public LinkInputDialog(Shell parentShell, String dialogTitle, String[] scenarios, int nbRows,
	    String initialDataValue) {
		super(parentShell);
		this.title = dialogTitle;
		this.data = initialDataValue;
		this.nbRows = nbRows;
		this.scenarioArray = scenarios;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			data = text.getText();
			int idx = combo.getSelectionIndex();
			if (idx >= 0) {
				scenario = this.scenarioArray[idx];
			} else {
				scenario = null;
			}
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	protected Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.WRAP);
		label.setText(text);
		applyDialogFont(label);

		GridData labelData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		labelData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		label.setLayoutData(labelData);

		return label;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);
		applyDialogFont(composite);

		createLabel(composite, "Scenario:");

		combo = new Combo(composite, style | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		combo.setItems(this.scenarioArray);
		combo.select(0);
		applyDialogFont(combo);

		createLabel(composite, "Data:");

		text = new Text(composite, style | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_text = new GridData(GridData.FILL_BOTH);
		gd_text.heightHint = convertHeightInCharsToPixels(this.nbRows);
		text.setLayoutData(gd_text);
		text.setText(data);
		applyDialogFont(text);

		return composite;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Returns the string typed into this input dialog.
	 *
	 * @return the input string
	 */
	public String getData() {
		return data;
	}

	public String getScenario() {
		return scenario;
	}
}
