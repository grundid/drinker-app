package de.grundid.drinker.menu;

import java.util.List;

public interface SimpleDrink {

	String getName();

	String getBrand();

	String getDescription();

	String getCategory();

	long getModifiedDate();

	List<VolumePrice> getVolumePrices();
}
