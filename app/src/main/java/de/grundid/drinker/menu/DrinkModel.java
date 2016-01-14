package de.grundid.drinker.menu;

import de.grundid.drinker.utils.Utils;

public class DrinkModel implements SimpleDrink {

	private String name;
	private String brand;
	private int price;
	private Integer volume;
	private String category;
	private String description;
	private String locationId;

	public Double getPricePerVolume() {
		return Utils.getPricePerVolume(price, volume);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public String getCategory() {
		return category;
	}

	@Override
	public long getModifiedDate() {
		return 0;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}
