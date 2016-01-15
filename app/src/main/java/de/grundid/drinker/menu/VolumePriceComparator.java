package de.grundid.drinker.menu;

import java.util.Comparator;

public class VolumePriceComparator implements Comparator<VolumePrice> {

	@Override public int compare(VolumePrice lhs, VolumePrice rhs) {
		int c = 0;
		if (lhs.getVolume() != null) {
			if (rhs.getVolume() != null) {
				c = lhs.getVolume().compareTo(rhs.getVolume());
			}
			else {
				c = 1;
			}
		}
		if (c == 0) {
			c = lhs.getPrice() - rhs.getPrice();
		}
		return c;
	}
}
