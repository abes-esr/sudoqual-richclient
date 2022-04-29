package fr.abes.qualinka.eclipse.ui;

interface IField<T> {

	String getLabel();

	T getValue();

	void setValue(T value);

	T getInitialValue();

	void setInitialValue(T value);

}