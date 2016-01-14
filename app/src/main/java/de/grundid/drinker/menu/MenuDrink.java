package de.grundid.drinker.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDrink implements SimpleDrink {

	private String drinkId;
	private String name;
	private String brand;
	private String description;
	private String category;
	private long modifiedDate;
	private List<VolumePrice> volumePrices;

	public List<VolumePrice> getVolumePrices() {
		return volumePrices;
	}

	public void setVolumePrices(List<VolumePrice> volumePrices) {
		this.volumePrices = volumePrices;
	}

	public void addVolumePrice(VolumePrice volumePrice) {
		if (volumePrices == null) {
			volumePrices = new ArrayList<>();
		}
		volumePrices.add(volumePrice);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDrinkId() {
		return drinkId;
	}

	public void setDrinkId(String drinkId) {
		this.drinkId = drinkId;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
