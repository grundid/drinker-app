package de.grundid.drinker;

public enum Category {
	softdrink("Softdrink"), water("Wasser"), beer("Bier"), juice("Säfte und Schorlen"),
	drink("Spirituosen"), longdrink("Longdrink"), cocktails("Cocktails"), cocktails_nonalcoholic(
			"Cocktails ohne Alkohol"), liqueur("Liköre"),
	tee("Tee"), coffee("Kaffee"), shot("Shots"), hot("Heißgetränke"), sparkling("Sekt"), aperitiv("Aperitiv"),
	wine("Wein"), other("Sonstige");
	String label;

	Category(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
