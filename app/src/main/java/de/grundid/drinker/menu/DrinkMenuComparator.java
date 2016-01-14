package de.grundid.drinker.menu;

import java.util.Comparator;

public class DrinkMenuComparator implements Comparator<MenuDrink> {

	private boolean byName;

	public DrinkMenuComparator(boolean byName) {
		this.byName = byName;
	}

	@Override public int compare(MenuDrink lhs, MenuDrink rhs) {
		int c = lhs.getCategory().compareTo(rhs.getCategory());
		if (c == 0) {
/*	FIXME		if (!byName) {
				if (!Double.isNaN(lhs.getPricePerVolume())) {
                    if (!Double.isNaN(rhs.getPricePerVolume())) {
                        if (lhs.getPricePerVolume() < rhs.getPricePerVolume()) {
                            c = -1;
                        }
                        else if (lhs.getPricePerVolume() > rhs.getPricePerVolume()) {
                            c = 1;
                        }
                        else {
                            c = 0;
                        }
                    }
                    else {
                        c = -1;
                    }
                }
                else {
                    if (!Double.isNaN(rhs.getPricePerVolume())) {
                        c = 1;
                    }
                }
			}*/

			if (c == 0) {
				c = lhs.getName().compareTo(rhs.getName());
			}
			if (c == 0) {
				c = lhs.getDrinkId().compareTo(rhs.getDrinkId());
			}
		}
		return c;
	}
}
