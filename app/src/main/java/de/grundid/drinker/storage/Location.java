package de.grundid.drinker.storage;

public class Location {
	private String placeId;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
	private int visits;
	private long lastVisit;

	public long getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(long lastVisit) {
		this.lastVisit = lastVisit;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}
}
