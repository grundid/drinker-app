package de.grundid.drinker.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.grundid.drinker.Category;

import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {

	private String name;
	private String placeId;
	private String locationId;
	private String address;
	private long lastUpdated;
	private Map<Category, Set<MenuDrink>> drinks;

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Map<Category, Set<MenuDrink>> getDrinks() {
		return drinks;
	}

	public void setDrinks(
			Map<Category, Set<MenuDrink>> drinks) {
		this.drinks = drinks;
	}
}
