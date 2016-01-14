package de.grundid.drinker.menu;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDrink implements SimpleDrink, Parcelable {

	private String drinkId;
	private String name;
	private String brand;
	private String description;
	private String category;
	private long modifiedDate;
	private List<VolumePrice> volumePrices;

	public MenuDrink() {
	}

	protected MenuDrink(Parcel in) {
		drinkId = in.readString();
		name = in.readString();
		brand = in.readString();
		description = in.readString();
		category = in.readString();
		modifiedDate = in.readLong();
		volumePrices = in.readArrayList(MenuDrink.class.getClassLoader());
	}

	public static final Creator<MenuDrink> CREATOR = new Creator<MenuDrink>() {

		@Override
		public MenuDrink createFromParcel(Parcel in) {
			return new MenuDrink(in);
		}

		@Override
		public MenuDrink[] newArray(int size) {
			return new MenuDrink[size];
		}
	};

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

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(drinkId);
		dest.writeString(name);
		dest.writeString(brand);
		dest.writeString(description);
		dest.writeString(category);
		dest.writeLong(modifiedDate);
		dest.writeList(volumePrices);
	}
}
