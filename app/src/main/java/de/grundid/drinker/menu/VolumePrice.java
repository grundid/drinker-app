package de.grundid.drinker.menu;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.grundid.drinker.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumePrice implements Parcelable {

	private int price;
	private Integer volume;
	private transient Double pricePerVolume;

	public VolumePrice() {
	}

	protected VolumePrice(Parcel in) {
		price = in.readInt();
		volume = in.readInt();
		if (volume == -1) {
			volume = null;
		}
	}

	public static final Creator<VolumePrice> CREATOR = new Creator<VolumePrice>() {

		@Override
		public VolumePrice createFromParcel(Parcel in) {
			return new VolumePrice(in);
		}

		@Override
		public VolumePrice[] newArray(int size) {
			return new VolumePrice[size];
		}
	};

	public Double getPricePerVolume() {
		if (pricePerVolume == null) {
			pricePerVolume = Utils.getPricePerVolume(price, volume);
		}
		return pricePerVolume;
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

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(price);
		dest.writeInt(volume == null ? -1 : volume.intValue());
	}
}
