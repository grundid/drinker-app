package de.grundid.drinker;

public class Utils {

	public static boolean hasText(String s) {
		return s != null && s.trim().length() > 0;
	}

	public static boolean hasText(CharSequence s) {
		return s != null && s.toString().trim().length() > 0;
	}
}
