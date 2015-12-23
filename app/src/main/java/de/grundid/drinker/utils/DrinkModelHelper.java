package de.grundid.drinker.utils;

import de.grundid.drinker.Utils;

public class DrinkModelHelper {

	public static Integer parseDrinkPrice(CharSequence input) {
		try {
			String content = input.toString().replaceAll(",", ".").trim();
			double value = Double.parseDouble(content);
			return (int)Math.round(value * 100);
		}
		catch (Exception e) {
			return null;
		}
	}

	private Integer parseDrinkVolume(CharSequence input) {
		try {
			String userInput = input.toString();
			if (Utils.hasText(userInput)) {
				String volume = userInput.replaceAll("ml", "").trim();
				return Integer.valueOf(volume);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
