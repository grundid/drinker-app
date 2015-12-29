package de.grundid.drinker.utils;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.location.places.Place;

public class PlaceWrapper implements Parcelable {

	private String id;
	private String name;
	private String address;
	private double lat;
	private double lon;

	public PlaceWrapper(Place place) {
		this.id = place.getId();
		this.name = place.getName() != null ? place.getName().toString() : null;
		this.address = place.getAddress() != null ? place.getAddress().toString() : null;
		this.lat = place.getLatLng().latitude;
		this.lon = place.getLatLng().longitude;
	}

	protected PlaceWrapper(Parcel in) {
		id = in.readString();
		name = in.readString();
		address = in.readString();
		lat = in.readDouble();
		lon = in.readDouble();
	}

	public static final Creator<PlaceWrapper> CREATOR = new Creator<PlaceWrapper>() {

		@Override
		public PlaceWrapper createFromParcel(Parcel in) {
			return new PlaceWrapper(in);
		}

		@Override
		public PlaceWrapper[] newArray(int size) {
			return new PlaceWrapper[size];
		}
	};

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
	}
}
