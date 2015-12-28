package de.grundid.drinker;

public enum Category {
	softdrink("Softdrink"), water("Wasser"), beer("Bier"),
	drink("Drink"), longdrink("Longdrink"), cocktails("Cocktails"),
	tee("Tee"), coffee("Kaffee"), shot("Shots"), hot("Heißgetränke"), sparkling("Sekt"),
	wine("Wein"), other("Sonstige");
	String label;

	Category(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
