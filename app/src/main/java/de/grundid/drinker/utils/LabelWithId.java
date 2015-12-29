package de.grundid.drinker.utils;

public class LabelWithId implements CharSequence, Comparable<LabelWithId> {

	private String label;
	private int id;

	public LabelWithId(String label, int id) {
		this.label = label;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override public int length() {
		return label.length();
	}

	@Override public char charAt(int index) {
		return label.charAt(index);
	}

	@Override public CharSequence subSequence(int start, int end) {
		return label.subSequence(start, end);
	}

	@Override public String toString() {
		return label;
	}

	@Override public int compareTo(LabelWithId another) {
		return label.compareTo(another.label);
	}
}
