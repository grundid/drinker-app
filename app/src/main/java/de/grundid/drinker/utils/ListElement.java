package de.grundid.drinker.utils;

public class ListElement {

	private int type;
	private Object element;

	public ListElement(int type, Object element) {
		this.type = type;
		this.element = element;
	}

	public int getType() {
		return type;
	}

	public Object getElement() {
		return element;
	}
}
