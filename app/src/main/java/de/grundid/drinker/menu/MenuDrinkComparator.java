package de.grundid.drinker.menu;

import java.util.Comparator;

import static de.grundid.drinker.Utils.hasText;

public class MenuDrinkComparator implements Comparator<MenuDrink> {

	@Override public int compare(MenuDrink lhs, MenuDrink rhs) {
		int c = 0;
		if (hasText(lhs.getBrand())) {
			if (hasText(rhs.getBrand())) {
				c = lhs.getBrand().compareTo(rhs.getBrand());
			}
			else {
				c = -1;
			}
		}
		else {
			if (hasText(rhs.getBrand())) {
				c = 1;
			}
		}
		if (c == 0) {
			c = lhs.getName().compareTo(rhs.getName());
		}
		if (c == 0) {
			if (hasText(lhs.getDescription())) {
				if (hasText(rhs.getDescription())) {
					c = lhs.getDescription().compareTo(rhs.getDescription());
				}
				else {
					c = -1;
				}
			}
			else {
				if (hasText(rhs.getDescription())) {
					c = 1;
				}
			}
		}
		return c;
	}
}
