package de.grundid.drinker.menu;

import java.util.Date;

public class Footer {

	private Date responseDate;
	private long lastUpdated;

	public Footer(Date responseDate, long lastUpdated) {
		this.responseDate = responseDate;
		this.lastUpdated = lastUpdated;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}
}
