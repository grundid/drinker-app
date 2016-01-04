package de.grundid.drinker.menu;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.grundid.drinker.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDrink implements Parcelable, SimpleDrink {

	private String drinkId;
	private String name;
	private String brand;
	private int price;
	private Integer volume;
	private String description;
	private String category;
	private transient Double pricePerVolume;
	private long modifiedDate;

	public MenuDrink() {
	}



	protected MenuDrink(Parcel in) {
		drinkId = in.readString();
		name = in.readString();
		brand = in.readString();
		price = in.readInt();
		description = in.readString();
		category = in.readString();
		volume = in.readInt();
		if (volume == -1) {
			volume = null;
		}
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

	@Override
	public Double getPricePerVolume() {
		if (pricePerVolume == null) {
			pricePerVolume = Utils.getPricePerVolume(price, volume);
		}
		return pricePerVolume;
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

	@Override
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDrinkId() {
		return drinkId;
	}

	public void setDrinkId(String drinkId) {
		this.drinkId = drinkId;
	}

	@Override
	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(drinkId);
		dest.writeString(name);
		dest.writeString(brand);
		dest.writeInt(price);
		dest.writeString(description);
		dest.writeString(category);
		dest.writeInt(volume == null ? -1 : volume.intValue());
	}

	@Override
	public long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
