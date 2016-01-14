package de.grundid.drinker.menu;

import java.util.ArrayList;
import java.util.List;

public class DrinkModel implements SimpleDrink {

	private String name;
	private String brand;
	private String category;
	private String description;
	private String locationId;
	private List<VolumePrice> volumePrices;

	public void addVolumePrice(VolumePrice volumePrice) {
		if (volumePrices == null) {
			volumePrices = new ArrayList<>();
		}
		volumePrices.add(volumePrice);
	}

	public List<VolumePrice> getVolumePrices() {
		return volumePrices;
	}

	public void setVolumePrices(List<VolumePrice> volumePrices) {
		this.volumePrices = volumePrices;
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
