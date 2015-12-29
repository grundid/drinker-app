package de.grundid.drinker.addplace;

import android.os.Parcel;
import android.os.Parcelable;
import de.grundid.drinker.Utils;

public class NewPlace implements Parcelable {

	private String address;
	private double latitude;
	private double longitude;

	public NewPlace(CharSequence address, double latitude, double longitude) {
		this.address = address == null ? null : address.toString();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	protected NewPlace(Parcel in) {
		address = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
	}

	public static final Creator<NewPlace> CREATOR = new Creator<NewPlace>() {

		@Override
		public NewPlace createFromParcel(Parcel in) {
			return new NewPlace(in);
		}

		@Override
		public NewPlace[] newArray(int size) {
			return new NewPlace[size];
		}
	};

	public boolean hasAddress() {
		return Utils.hasText(address);
	}

	public String getAddress() {
		return address;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(address);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}
}
