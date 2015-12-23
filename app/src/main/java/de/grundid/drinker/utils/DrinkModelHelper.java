package de.grundid.drinker.utils;

import de.grundid.drinker.Utils;
import de.grundid.drinker.menu.DrinkModel;

public class DrinkModelHelper {

	public static Integer parseDrinkPrice(CharSequence input) {
		try {
			String content = input.toString().replaceAll(",", ".").trim();
			double value = Double.parseDouble(content);
			if (value == 0) {
				return null;
			}
			return (int)Math.round(value * 100);
		}
		catch (Exception e) {
		}
		return null;
	}

	public static Integer parseDrinkVolume(CharSequence input) {
		try {
			String userInput = input.toString();
			if (Utils.hasText(userInput)) {
				String volume = userInput.replaceAll("ml", "").replaceAll("l","").replace(',','.').trim();
				double value = Double.valueOf(volume);
				if (value < 5) {
					value = value * 1000;
				}
				if (value == 0) {
					return null;
				}
				return (int)Math.round(value);
			}
		}
		catch (Exception e) {
		}
		return null;
	}

	public static boolean isDrinkModelValid(DrinkModel model) {
		return Utils.hasText(model.getName()) && model.getCategory() != null && model.getPrice() != null;
	}


}
