package de.grundid.drinker.utils;

public class EmptyElement extends ListElement {

	public EmptyElement(Integer textResourceId) {
		super(EmptyStateAdapter.TYPE_EMPTY, textResourceId);
	}
}
