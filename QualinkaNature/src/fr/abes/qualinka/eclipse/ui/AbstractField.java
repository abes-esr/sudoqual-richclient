package fr.abes.qualinka.eclipse.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

public abstract class AbstractField<T> implements IField<T> {
	private final String label;
	private T initialValue;
	private IPage page;

	public AbstractField(IPage page, String label, T initialValue) {
		this.page = page;
		this.label = label;
		this.initialValue = initialValue;
	}

	@Override
	public T getInitialValue() {
		return this.initialValue;
	}

	@Override
	public void setInitialValue(T value) {
		this.initialValue = value;
		this.setValue(value);
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	protected void addLabel(String label) {
		new Label(this.page.getParentContainer(), SWT.NULL).setText(label);
	}

	protected IPage getPage() {
		return this.page;
	}

}